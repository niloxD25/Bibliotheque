-- Créer la base de données
CREATE DATABASE gestion_bibliotheque;

-- Se connecter à la base
\c gestion_bibliotheque

-- Table des genres
CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

-- Table des livres
CREATE TABLE livres (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    date_publication DATE,
    editeur VARCHAR(255),
    age INTEGER,
    id_genre INTEGER REFERENCES genres(id) ON DELETE SET NULL
);

-- Table des états des exemplaires
CREATE TABLE etats_exemplaires (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL UNIQUE
);

-- Table des exemplaires
CREATE TABLE exemplaires (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL UNIQUE,
    id_livre INTEGER NOT NULL REFERENCES livres(id) ON DELETE CASCADE,
    disponible BOOLEAN DEFAULT true
);

-- Table de relation entre exemplaires et états_exemplaires
CREATE TABLE exemplaire_etat (
    id SERIAL PRIMARY KEY,
    id_exemplaire INTEGER NOT NULL REFERENCES exemplaires(id) ON DELETE CASCADE,
    id_etat_exemplaire INTEGER NOT NULL REFERENCES etats_exemplaires(id) ON DELETE RESTRICT,
    date_attribue TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des types de clients
CREATE TABLE typeclients (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE,
    nbr_livre_max INTEGER NOT NULL,
    jours_penalisation INTEGER NOT NULL,
    nbr_jours_emprunt INTEGER NOT NULL,
    nbr_prolongement INTEGER NOT NULL,
    nbr_reservation INTEGER NOT NULL
);

-- Table des clients
CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    age INTEGER,
    id_typeclient INTEGER NOT NULL REFERENCES typeclients(id) ON DELETE RESTRICT,
    date_enregistrement DATE DEFAULT CURRENT_DATE,
    fin_abonnement DATE,
    actif BOOLEAN DEFAULT true
);

-- Table des prêts
CREATE TABLE prets (
    id SERIAL PRIMARY KEY,
    id_client INTEGER NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    id_exemplaire INTEGER NOT NULL REFERENCES exemplaires(id) ON DELETE CASCADE,
    date_emprunt DATE DEFAULT CURRENT_DATE,
    date_retour_prevue DATE,
    date_retour_effective DATE
);

-- Table des états possibles d’un prêt
CREATE TABLE etats (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL UNIQUE
);

-- Table de relation entre prêts et états
CREATE TABLE prets_etats (
    id SERIAL PRIMARY KEY,
    id_pret INTEGER NOT NULL REFERENCES prets(id) ON DELETE CASCADE,
    id_etat INTEGER NOT NULL REFERENCES etats(id) ON DELETE RESTRICT,
    date_etat TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des pénalités
CREATE TABLE penalites (
    id SERIAL PRIMARY KEY,
    id_client INTEGER NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    date_debut DATE NOT NULL DEFAULT CURRENT_DATE,
    date_fin DATE,
    raison VARCHAR(255) NOT NULL
);

-- Table pour enregistrer les dimanches et jours fériés
CREATE TABLE jours_feries (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE reservation_statut (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    id_client BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    id_exemplaire BIGINT NOT NULL REFERENCES exemplaires(id) ON DELETE CASCADE,
    date_reservation DATE NOT NULL DEFAULT CURRENT_DATE,
    date_souhaitee DATE NOT NULL,
    actif BOOLEAN DEFAULT true
);

CREATE TABLE reservations_etats (
    id BIGSERIAL PRIMARY KEY,
    id_reservation BIGINT NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    id_statut BIGINT NOT NULL REFERENCES reservation_statut(id) ON DELETE CASCADE,
    date_statut TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prolongements (
    id BIGSERIAL PRIMARY KEY,
    pret_id BIGINT NOT NULL REFERENCES prets(id) ON DELETE CASCADE,
    date_demande TIMESTAMP NOT NULL,
    actif BOOLEAN DEFAULT true,
    commentaire TEXT
);

INSERT INTO reservation_statut (nom) VALUES
('EN_ATTENTE'),
('VALIDEE'),
('ANNULEE');

-- Dimanches et Jours fériés 2025
INSERT INTO jours_feries (date, description) VALUES
('2025-07-13', 'Dimanche'),
('2025-07-20', 'Dimanche'),
('2025-07-27', 'Dimanche'),
('2025-08-03', 'Dimanche'),
('2025-08-10', 'Dimanche'),
('2025-08-17', 'Dimanche'),
('2025-08-24', 'Dimanche'),
('2025-07-17', ''),
('2025-07-19', ''),
('2025-08-15', ''),
('2025-11-01', ''),
('2025-12-25', '');

-- Genres
INSERT INTO genres (nom) VALUES
('Litterature classique'),
('Philosophie'),
('Jeunesse / Fantastique');

-- Livres
INSERT INTO livres (titre, auteur, date_publication, editeur, age, id_genre) VALUES
('Les Miserables', 'Victor Hugo', '1949-06-08', '', 0, 1),
('Etranger', 'Albert Camus', '1943-04-06', '', 0, 2),
('Harry Potter a l*ecole des sorciers', 'J.K. Rowling', '2007-10-04', '', 0, 3);

INSERT INTO livres (titre, auteur, date_publication, editeur, age, id_genre) VALUES
('Memoire', '', '1949-06-08', '', 0, 3);

-- Exemplaires
INSERT INTO exemplaires (nom, id_livre, disponible) VALUES
('MIS001', 1, true),
('MIS002', 1, true),
('MIS003', 1, true),
('ETR001', 2, true),
('ETR002', 2, true),
('HAR001', 3, true);

INSERT INTO exemplaires (nom, id_livre, disponible) VALUES
('MEM001', 4, true);

-- Types de clients
INSERT INTO typeclients (type, nbr_livre_max, jours_penalisation, nbr_jours_emprunt, nbr_prolongement, nbr_reservation) VALUES
('Etudiant', 2, 7, 5, 1, 1),
('Enseignant', 3, 3, 10, 2, 2),
('Professionnel', 4, 3, 12, 3, 3);

-- Clients
INSERT INTO clients (nom, age, id_typeclient, actif, date_enregistrement) VALUES
('Amine Bensaid', 0, 1, true, '2025-02-01'),
('Sarah El Khattabi', 0, 1, false, '2025-02-01'),
('Youssef Moujahid', 0, 1, true, '2025-04-01'),
('Nadia Benali', 0, 2, true, '2025-07-01'),
('Karim Haddadi', 0, 2, false, '2025-08-01'),
('Salima Touhami', 0, 2, true, '2025-07-01'),
('Rachid El Mansouri', 0, 3, true, '2025-06-01'),
('Amina Zerouali', 0, 3, false, '2024-10-01');


-- Prêts
-- INSERT INTO prets (id_client, id_exemplaire, date_emprunt, date_retour_prevue, date_retour_effective) VALUES
-- (1, 1, CURRENT_DATE - 10, CURRENT_DATE + 4, NULL),
-- (2, 3, CURRENT_DATE - 35, CURRENT_DATE - 5, CURRENT_DATE - 3),
-- (4, 2, CURRENT_DATE - 5, CURRENT_DATE + 9, NULL);

-- États possibles
INSERT INTO etats (nom) VALUES
('En cours'),
('Retard'),
('Rendu'),
('Prolonge');

-- État actuel des prêts
-- INSERT INTO prets_etats (id_pret, id_etat, date_etat) VALUES
-- (1, 1, CURRENT_DATE - 10),
-- (2, 3, CURRENT_DATE - 3),
-- (3, 1, CURRENT_DATE - 5);


UPDATE clients SET fin_abonnement = '2025-12-01' WHERE id = 1;
UPDATE clients SET fin_abonnement = '2025-07-01' WHERE id = 2;
UPDATE clients SET fin_abonnement = '2025-12-01' WHERE id = 3;
UPDATE clients SET fin_abonnement = '2025-07-15' WHERE id = 4;
UPDATE clients SET fin_abonnement = '2026-05-01' WHERE id = 5;
UPDATE clients SET fin_abonnement = '2026-06-01' WHERE id = 6;
UPDATE clients SET fin_abonnement = '2025-12-01' WHERE id = 7;
UPDATE clients SET fin_abonnement = '2025-06-01' WHERE id = 8;