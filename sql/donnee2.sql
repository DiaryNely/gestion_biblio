-- =============================================
-- INSERTION DE DONNÉES DE TEST - GESTION BIBLIO
-- =============================================


-- --------------------------
-- 1. AUTEURS
-- --------------------------
INSERT INTO auteur (nom_complet, biographie, date_naissance) VALUES
('George Orwell', 'Auteur britannique connu pour ses romans dystopiques.', '1903-06-25'),
('J.K. Rowling', 'Auteure de la célèbre série Harry Potter.', '1965-07-31'),
('Yuval Noah Harari', 'Historien et auteur israélien, spécialisé en histoire du monde.', '1976-02-24'),
('Stephen King', 'Maître américain de l''horreur et du suspense.', '1947-09-21');

-- --------------------------
-- 2. LIVRES
-- --------------------------
INSERT INTO livre (titre, date_edition, isbn, id_langue, id_genre, mots_cles, rayon, etagere, editeur, nombre_pages, resume) VALUES
('1984', '1949-06-08', '978-2070368228', (SELECT id_langue FROM langue WHERE nom = 'Français'), (SELECT id_genre FROM genre_litteraire WHERE nom = 'Science-Fiction'), 'dystopie, totalitarisme, surveillance', 'A', '1', 'Gallimard', 376, 'Dans un futur totalitaire, Winston Smith tente de résister à un régime qui contrôle les pensées.'),
('Harry Potter à l''école des sorciers', '1997-06-26', '978-2070643028', (SELECT id_langue FROM langue WHERE nom = 'Français'), (SELECT id_genre FROM genre_litteraire WHERE nom = 'Roman'), 'magie, école, amitié', 'B', '3', 'Gallimard Jeunesse', 320, 'Un jeune orphelin découvre qu''il est un sorcier et intègre l''école de Poudlard.'),
('Sapiens : Une brève histoire de l''humanité', '2011-01-01', '978-2226257017', (SELECT id_langue FROM langue WHERE nom = 'Français'), (SELECT id_genre FROM genre_litteraire WHERE nom = 'Essai'), 'histoire, anthropologie, évolution', 'C', '2', 'Albin Michel', 512, 'Une exploration fascinante de l''histoire de l''Homo sapiens, de l''âge de pierre à nos jours.'),
('Algorithmes : Notions de base', '2019-05-15', '978-2100825134', (SELECT id_langue FROM langue WHERE nom = 'Français'), (SELECT id_genre FROM genre_litteraire WHERE nom = 'Informatique'), 'programmation, algorithme, données', 'D', '5', 'Dunod', 896, 'Un manuel complet sur les structures de données et les algorithmes fondamentaux.'),
('Shining', '1977-01-28', '978-2253151623', (SELECT id_langue FROM langue WHERE nom = 'Français'), (SELECT id_genre FROM genre_litteraire WHERE nom = 'Roman'), 'horreur, hôtel, paranormal', 'A', '2', 'Le Livre de Poche', 608, 'Un gardien d''hôtel isolé avec sa famille pendant l''hiver est confronté à des forces surnaturelles et à sa propre folie.');

-- --------------------------
-- 3. ASSOCIATION LIVRES-AUTEURS
-- --------------------------
INSERT INTO livre_auteur (id_livre, id_auteur) VALUES
((SELECT id_livre FROM livre WHERE titre = '1984'), (SELECT id_auteur FROM auteur WHERE nom_complet = 'George Orwell')),
((SELECT id_livre FROM livre WHERE titre = 'Harry Potter à l''école des sorciers'), (SELECT id_auteur FROM auteur WHERE nom_complet = 'J.K. Rowling')),
((SELECT id_livre FROM livre WHERE titre = 'Sapiens : Une brève histoire de l''humanité'), (SELECT id_auteur FROM auteur WHERE nom_complet = 'Yuval Noah Harari')),
((SELECT id_livre FROM livre WHERE titre = 'Shining'), (SELECT id_auteur FROM auteur WHERE nom_complet = 'Stephen King'));
-- Note: Le livre d'algorithmes n'a pas d'auteur dans cette liste, c'est possible.

-- --------------------------
-- 4. EXEMPLAIRES
-- --------------------------
-- Plusieurs exemplaires pour "1984"
INSERT INTO exemplaire (id_livre, code_inventaire, etat_physique, disponible) VALUES
((SELECT id_livre FROM livre WHERE titre = '1984'), 'INV-1984-01', 'Bon', TRUE),
((SELECT id_livre FROM livre WHERE titre = '1984'), 'INV-1984-02', 'Moyen', TRUE),
((SELECT id_livre FROM livre WHERE titre = '1984'), 'INV-1984-03', 'Abîmé', FALSE); -- Un exemplaire non disponible (déjà en prêt ou en réparation)

-- Plusieurs exemplaires pour "Harry Potter"
INSERT INTO exemplaire (id_livre, code_inventaire, etat_physique, disponible) VALUES
((SELECT id_livre FROM livre WHERE titre = 'Harry Potter à l''école des sorciers'), 'INV-HP1-01', 'Neuf', TRUE),
((SELECT id_livre FROM livre WHERE titre = 'Harry Potter à l''école des sorciers'), 'INV-HP1-02', 'Bon', TRUE);

-- Un seul exemplaire pour les autres
INSERT INTO exemplaire (id_livre, code_inventaire, etat_physique, disponible) VALUES
((SELECT id_livre FROM livre WHERE titre = 'Sapiens : Une brève histoire de l''humanité'), 'INV-SAP-01', 'Bon', TRUE),
((SELECT id_livre FROM livre WHERE titre = 'Algorithmes : Notions de base'), 'INV-ALGO-01', 'Neuf', TRUE),
((SELECT id_livre FROM livre WHERE titre = 'Shining'), 'INV-SHI-01', 'Moyen', TRUE);


-- --------------------------
-- 5. NOUVEL UTILISATEUR ETUDIANT
-- --------------------------
-- Création de l'utilisateur
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe_hash, statut, date_naissance, telephone, adresse_ligne1, code_postal, ville) VALUES
('Dupont', 'Marie', 'marie.dupont@email.com', crypt('etudiant123', gen_salt('bf')), 'actif', '2002-05-15', '0341122334', '123 Rue de l''Université', '101', 'Antananarivo')
RETURNING id_utilisateur;

-- Création de l'adhérent correspondant de type "Etudiant"
-- IMPORTANT: Remplacez l'ID ci-dessous par celui retourné par la commande précédente si vous exécutez manuellement.
-- Sinon, la sous-requête s'en chargera.
INSERT INTO adherent (id_utilisateur, id_type_adherent, statut_adhesion) VALUES
(
    (SELECT id_utilisateur FROM utilisateur WHERE email = 'marie.dupont@email.com'),
    (SELECT id_type_adherent FROM type_adherent WHERE nom = 'Etudiant'),
    'actif'
);

-- --------------------------
-- 6. ABONNEMENT POUR L'ÉTUDIANT
-- --------------------------
-- Création d'un abonnement annuel actif pour Marie Dupont
INSERT INTO abonnement (id_adherent, type_abonnement, date_debut, date_fin) VALUES
(
    (SELECT id_adherent FROM adherent WHERE id_utilisateur = (SELECT id_utilisateur FROM utilisateur WHERE email = 'marie.dupont@email.com')),
    'annuel',
    CURRENT_DATE - INTERVAL '2 months', -- A commencé il y a 2 mois
    CURRENT_DATE + INTERVAL '10 months' -- Expire dans 10 mois
);

-- --------------------------
-- 7. (Optionnel) UN PREMIER PRET ET UNE PENALITE POUR TESTER LES BLOCAGES
-- --------------------------
/*
-- Décommentez cette section si vous voulez tester le blocage par pénalité.
-- D'abord, on crée un prêt en retard pour Marie
INSERT INTO pret (id_exemplaire, id_adherent, date_emprunt, date_retour_prevue, id_type, id_etat) VALUES
(
    (SELECT id_exemplaire FROM exemplaire WHERE code_inventaire = 'INV-SHI-01'), -- Elle emprunte Shining
    (SELECT id_adherent FROM adherent WHERE id_utilisateur = (SELECT id_utilisateur FROM utilisateur WHERE email = 'marie.dupont@email.com')),
    '2024-01-01',
    '2024-01-15', -- La date de retour est largement dépassée
    (SELECT id_type FROM type_pret WHERE nom = 'Domicile'),
    (SELECT id_etat FROM etat_pret WHERE nom = 'En retard')
);

-- Ensuite, on lui met une pénalité non payée
INSERT INTO penalite (id_adherent, id_pret, type_penalite, montant, statut, motif) VALUES
(
    (SELECT id_adherent FROM adherent WHERE id_utilisateur = (SELECT id_utilisateur FROM utilisateur WHERE email = 'marie.dupont@email.com')),
    (SELECT id_pret FROM pret WHERE date_emprunt = '2024-01-01'),
    'retard',
    5000.00,
    'en_attente',
    'Retard de plusieurs mois sur le livre "Shining".'
);

-- On met à jour l'exemplaire pour qu'il ne soit plus disponible
UPDATE exemplaire SET disponible = FALSE WHERE code_inventaire = 'INV-SHI-01';
*/

-- Message de fin
SELECT 'Données de test insérées avec succès.' as status;