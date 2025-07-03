-- Base de données PostgreSQL pour la gestion de bibliothèque
-- Version finale avec corrections des contraintes

-- ========================================
-- EXTENSIONS ET TYPES PERSONNALISÉS
-- ========================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

DROP TYPE IF EXISTS type_etat_physique CASCADE;
DROP TYPE IF EXISTS type_statut_utilisateur CASCADE;
DROP TYPE IF EXISTS type_statut_adhesion CASCADE;
DROP TYPE IF EXISTS type_statut_paiement CASCADE;
DROP TYPE IF EXISTS type_abonnement CASCADE;
DROP TYPE IF EXISTS type_restriction CASCADE;
DROP TYPE IF EXISTS type_penalite CASCADE;
DROP TYPE IF EXISTS type_notification CASCADE;
DROP TYPE IF EXISTS type_canal_notification CASCADE;
DROP TYPE IF EXISTS type_statut_reservation CASCADE;
DROP TYPE IF EXISTS type_paiement_source CASCADE;

CREATE TYPE type_etat_physique AS ENUM ('Neuf', 'Bon', 'Moyen', 'Abîmé', 'Inutilisable');
CREATE TYPE type_statut_utilisateur AS ENUM ('actif', 'suspendu', 'inactif');
CREATE TYPE type_statut_adhesion AS ENUM ('actif', 'suspendu', 'expire');
CREATE TYPE type_statut_paiement AS ENUM ('en_attente', 'paye', 'annule', 'rembourse');
CREATE TYPE type_abonnement AS ENUM ('mensuel', 'annuel', 'permanent');
CREATE TYPE type_restriction AS ENUM ('interdit', 'autorise_uniquement');
CREATE TYPE type_penalite AS ENUM ('retard', 'deterioration', 'perte', 'autre');
CREATE TYPE type_notification AS ENUM ('echeance_proche', 'retard', 'fin_abonnement', 'reservation_disponible', 'penalite', 'information');
CREATE TYPE type_canal_notification AS ENUM ('email', 'sms', 'notification_app');
CREATE TYPE type_statut_reservation AS ENUM ('en_attente', 'satisfaite', 'expiree', 'annulee');
CREATE TYPE type_paiement_source AS ENUM ('abonnement', 'penalite', 'don', 'autre');


-- ========================================
-- TABLES DE RÉFÉRENCE
-- ========================================
CREATE TABLE type_adherent ( id_type_adherent SERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, description TEXT, actif BOOLEAN DEFAULT TRUE, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE type_pret ( id_type SERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, description TEXT );
CREATE TABLE etat_pret ( id_etat SERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, description TEXT );
CREATE TABLE statut_prolongement ( id_statut_prolongement SERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, description TEXT );
CREATE TABLE genre_litteraire ( id_genre SERIAL PRIMARY KEY, nom VARCHAR(100) NOT NULL UNIQUE, description TEXT, actif BOOLEAN DEFAULT TRUE );
CREATE TABLE langue ( id_langue SERIAL PRIMARY KEY, nom VARCHAR(50) NOT NULL UNIQUE, code_iso VARCHAR(5) NOT NULL UNIQUE, actif BOOLEAN DEFAULT TRUE );
CREATE TABLE auteur ( id_auteur SERIAL PRIMARY KEY, nom_complet VARCHAR(255) NOT NULL UNIQUE, biographie TEXT, date_naissance DATE, date_deces DATE, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );

-- ========================================
-- TABLES PRINCIPALES
-- ========================================

CREATE TABLE livre ( id_livre SERIAL PRIMARY KEY, titre VARCHAR(255) NOT NULL, date_edition DATE, isbn VARCHAR(20) UNIQUE, id_langue INTEGER REFERENCES langue(id_langue) ON DELETE SET NULL, id_genre INTEGER REFERENCES genre_litteraire(id_genre) ON DELETE SET NULL, mots_cles TEXT, rayon VARCHAR(50), etagere VARCHAR(50), resume TEXT, editeur VARCHAR(255), nombre_pages INTEGER, actif BOOLEAN DEFAULT TRUE, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE livre_auteur ( id_livre INTEGER NOT NULL REFERENCES livre(id_livre) ON DELETE CASCADE, id_auteur INTEGER NOT NULL REFERENCES auteur(id_auteur) ON DELETE CASCADE, PRIMARY KEY (id_livre, id_auteur) );
CREATE TABLE exemplaire ( id_exemplaire SERIAL PRIMARY KEY, id_livre INTEGER NOT NULL REFERENCES livre(id_livre) ON DELETE CASCADE, code_inventaire VARCHAR(50) UNIQUE NOT NULL, etat_physique type_etat_physique DEFAULT 'Bon', disponible BOOLEAN DEFAULT TRUE, date_acquisition DATE DEFAULT CURRENT_DATE, prix_achat DECIMAL(10,2), notes TEXT, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE utilisateur ( id_utilisateur SERIAL PRIMARY KEY, nom VARCHAR(100) NOT NULL, prenom VARCHAR(100) NOT NULL, email VARCHAR(255) UNIQUE, telephone VARCHAR(30), adresse_ligne1 VARCHAR(255), adresse_ligne2 VARCHAR(255), code_postal VARCHAR(10), ville VARCHAR(100), pays VARCHAR(100) DEFAULT 'Madagascar', date_inscription DATE DEFAULT CURRENT_DATE, date_naissance DATE, numero_carte UUID DEFAULT uuid_generate_v4() UNIQUE, mot_de_passe_hash VARCHAR(255), statut type_statut_utilisateur DEFAULT 'actif', photo_profil TEXT, notes_admin TEXT, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );

CREATE TABLE adherent (
    id_adherent SERIAL PRIMARY KEY,
    id_utilisateur INTEGER NOT NULL UNIQUE REFERENCES utilisateur(id_utilisateur) ON DELETE CASCADE,
    id_type_adherent INTEGER NOT NULL REFERENCES type_adherent(id_type_adherent) ON DELETE RESTRICT,
    statut_adhesion type_statut_adhesion DEFAULT 'actif',
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paiement (
    id_paiement SERIAL PRIMARY KEY,
    -- CORRECTION: Lié à l'adhérent, pas à l'utilisateur, pour plus de cohérence.
    id_adherent INTEGER NOT NULL REFERENCES adherent(id_adherent) ON DELETE RESTRICT,
    montant DECIMAL(10,2) NOT NULL,
    date_paiement TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    mode_paiement VARCHAR(50),
    reference_paiement TEXT,
    statut_paiement type_statut_paiement DEFAULT 'paye',
    source_paiement type_paiement_source NOT NULL,
    id_objet_concerne INTEGER, -- Ex: id_abonnement ou id_penalite
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE abonnement (
    id_abonnement SERIAL PRIMARY KEY,
    id_adherent INTEGER NOT NULL REFERENCES adherent(id_adherent) ON DELETE CASCADE,
    type_abonnement type_abonnement NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE, -- Un abonnement permanent n'a pas de date de fin.
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_dates_coherentes CHECK (date_debut <= date_fin OR date_fin IS NULL)
);

CREATE TABLE pret (
    id_pret SERIAL PRIMARY KEY,
    id_exemplaire INTEGER NOT NULL REFERENCES exemplaire(id_exemplaire) ON DELETE RESTRICT,
    -- CORRECTION: Contrainte plus stricte pour préserver l'historique
    id_adherent INTEGER NOT NULL REFERENCES adherent(id_adherent) ON DELETE RESTRICT,
    date_emprunt DATE NOT NULL DEFAULT CURRENT_DATE,
    date_retour_prevue DATE NOT NULL,
    date_retour_reelle DATE,
    id_type INTEGER NOT NULL REFERENCES type_pret(id_type),
    id_etat INTEGER NOT NULL REFERENCES etat_pret(id_etat),
    etat_livre_emprunt type_etat_physique DEFAULT 'Bon',
    etat_livre_retour type_etat_physique,
    bibliothecaire_emprunt_id INTEGER REFERENCES utilisateur(id_utilisateur) ON DELETE SET NULL,
    bibliothecaire_retour_id INTEGER REFERENCES utilisateur(id_utilisateur) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE penalite (
    id_penalite SERIAL PRIMARY KEY,
    id_adherent INTEGER NOT NULL REFERENCES adherent(id_adherent) ON DELETE CASCADE,
    -- CORRECTION: Si le prêt est supprimé, la pénalité orpheline est conservée
    id_pret INTEGER REFERENCES pret(id_pret) ON DELETE SET NULL,
    type_penalite type_penalite NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_creation DATE NOT NULL DEFAULT CURRENT_DATE,
    statut type_statut_paiement DEFAULT 'en_attente',
    -- CORRECTION MAJEURE: Suppression de id_paiement pour casser la dépendance circulaire
    motif TEXT,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Les autres tables restent globalement inchangées car leur structure était déjà saine.
CREATE TABLE prolongement_pret ( id_prolongement SERIAL PRIMARY KEY, id_pret INTEGER NOT NULL REFERENCES pret(id_pret) ON DELETE CASCADE, date_prolongement DATE NOT NULL DEFAULT CURRENT_DATE, ancienne_date_retour DATE NOT NULL, nouvelle_date_retour DATE NOT NULL, motif TEXT, id_statut_prolongement INTEGER REFERENCES statut_prolongement(id_statut_prolongement), approuve_par_id INTEGER REFERENCES utilisateur(id_utilisateur), created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE parametre_pret ( id_parametre SERIAL PRIMARY KEY, id_type_adherent INTEGER UNIQUE NOT NULL REFERENCES type_adherent(id_type_adherent) ON DELETE CASCADE, max_livres INTEGER NOT NULL DEFAULT 3, duree_max_pret_jours INTEGER NOT NULL DEFAULT 14, penalite_par_jour DECIMAL(5,2) DEFAULT 0.50, jours_tolerance INTEGER DEFAULT 2, peut_prolonger BOOLEAN DEFAULT TRUE, nb_max_prolongements INTEGER DEFAULT 2, actif BOOLEAN DEFAULT TRUE, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE reservation ( id_reservation SERIAL PRIMARY KEY, id_adherent INTEGER NOT NULL REFERENCES adherent(id_adherent) ON DELETE CASCADE, id_livre INTEGER NOT NULL REFERENCES livre(id_livre) ON DELETE CASCADE, date_reservation TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, date_expiration DATE NOT NULL, statut type_statut_reservation DEFAULT 'en_attente', position_file_attente INTEGER, notes TEXT, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE parametre_systeme ( id_parametre SERIAL PRIMARY KEY, cle VARCHAR(100) NOT NULL UNIQUE, valeur TEXT NOT NULL, type_valeur VARCHAR(20) DEFAULT 'string' CHECK (type_valeur IN ('string', 'integer', 'decimal', 'boolean', 'json')), description TEXT, categorie VARCHAR(50), modifiable BOOLEAN DEFAULT TRUE, created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP );

-- ========================================
-- INDEX POUR OPTIMISER LES PERFORMANCES
-- ========================================
CREATE INDEX idx_livre_titre ON livre USING gin (to_tsvector('french', titre));
CREATE INDEX idx_utilisateur_email ON utilisateur(email);
CREATE INDEX idx_pret_adherent ON pret(id_adherent);
CREATE INDEX idx_pret_exemplaire ON pret(id_exemplaire);
CREATE INDEX idx_exemplaire_livre ON exemplaire(id_livre);
CREATE INDEX idx_penalite_adherent ON penalite(id_adherent);
CREATE INDEX idx_paiement_adherent ON paiement(id_adherent);

-- ========================================
-- TRIGGERS POUR AUTOMATISER CERTAINES ACTIONS
-- ========================================

CREATE OR REPLACE FUNCTION update_updated_at_column() RETURNS TRIGGER AS $$
BEGIN NEW.updated_at = CURRENT_TIMESTAMP; RETURN NEW; END;
$$ language 'plpgsql';

CREATE TRIGGER update_livre_updated_at BEFORE UPDATE ON livre FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_utilisateur_updated_at BEFORE UPDATE ON utilisateur FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_adherent_updated_at BEFORE UPDATE ON adherent FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_pret_updated_at BEFORE UPDATE ON pret FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_exemplaire_updated_at BEFORE UPDATE ON exemplaire FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_penalite_updated_at BEFORE UPDATE ON penalite FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- CORRECTION: Trigger de disponibilité rendu plus robuste pour gérer les suppressions.
CREATE OR REPLACE FUNCTION gerer_disponibilite_exemplaire_sur_pret()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Nouveau prêt: marquer l'exemplaire comme non disponible
        UPDATE exemplaire SET disponible = FALSE WHERE id_exemplaire = NEW.id_exemplaire;
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        -- Retour de livre: marquer l'exemplaire comme disponible
        IF OLD.date_retour_reelle IS NULL AND NEW.date_retour_reelle IS NOT NULL THEN
            UPDATE exemplaire SET disponible = TRUE WHERE id_exemplaire = NEW.id_exemplaire;
        END IF;
        RETURN NEW;
    -- AJOUT: Gérer la suppression d'un prêt
    ELSIF TG_OP = 'DELETE' THEN
        -- Prêt supprimé: l'exemplaire redevient disponible
        UPDATE exemplaire SET disponible = TRUE WHERE id_exemplaire = OLD.id_exemplaire;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_disponibilite_exemplaire
    AFTER INSERT OR UPDATE OR DELETE ON pret
    FOR EACH ROW EXECUTE FUNCTION gerer_disponibilite_exemplaire_sur_pret();



ALTER TABLE parametre_pret
ALTER COLUMN penalite_par_jour TYPE DECIMAL(8, 2);


