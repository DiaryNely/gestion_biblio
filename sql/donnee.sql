
-- ========================================
-- DONNÉES INITIALES
-- ========================================

INSERT INTO type_adherent (nom, description) VALUES
('Etudiant', 'Etudiant inscrit dans un établissement'),
('Enseignant', 'Personnel enseignant'),
('Grand Public', 'Utilisateur grand public'),
('Administrateur', 'Personnel de la bibliothèque');

INSERT INTO type_pret (nom, description) VALUES ('Domicile', 'Prêt à emporter'), ('Sur place', 'Consultation uniquement');
INSERT INTO etat_pret (nom, description) VALUES ('En cours', 'Prêt actif'), ('Retourné', 'Livre retourné'), ('En retard', 'Prêt en retard'), ('Perdu', 'Livre déclaré perdu');
INSERT INTO langue (nom, code_iso) VALUES ('Français', 'fr'), ('Anglais', 'en'), ('Malagasy', 'mg');
INSERT INTO genre_litteraire (nom) VALUES ('Roman'), ('Essai'), ('Science-Fiction'), ('Informatique'), ('Histoire');

INSERT INTO parametre_pret (id_type_adherent, max_livres, duree_max_pret_jours, penalite_par_jour) VALUES
((SELECT id_type_adherent FROM type_adherent WHERE nom = 'Etudiant'), 5, 21, 500.00),
((SELECT id_type_adherent FROM type_adherent WHERE nom = 'Enseignant'), 10, 30, 500.00),
((SELECT id_type_adherent FROM type_adherent WHERE nom = 'Grand Public'), 3, 14, 1000.00),
((SELECT id_type_adherent FROM type_adherent WHERE nom = 'Administrateur'), 99, 365, 0.00);

-- Créer un utilisateur administrateur par défaut
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe_hash, statut) VALUES
('Admin', 'Système', 'admin@bibliotheque.mg', crypt('admin123', gen_salt('bf')), 'actif')
RETURNING id_utilisateur;

-- Créer l'adhérent administrateur correspondant
INSERT INTO adherent (id_utilisateur, id_type_adherent, statut_adhesion) VALUES
(
    (SELECT id_utilisateur FROM utilisateur WHERE email = 'admin@bibliotheque.mg'),
    (SELECT id_type_adherent FROM type_adherent WHERE nom = 'Administrateur'),
    'actif'
)
RETURNING id_adherent;

-- CORRECTION: Création d'un abonnement permanent pour l'admin, sans paiement associé.
INSERT INTO abonnement (id_adherent, type_abonnement, date_debut) VALUES
(
    (SELECT id_adherent FROM adherent WHERE id_utilisateur = (SELECT id_utilisateur FROM utilisateur WHERE email = 'admin@bibliotheque.mg')),
    'permanent',
    CURRENT_DATE
);