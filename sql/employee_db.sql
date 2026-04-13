-- ═══════════════════════════════════════════════════════════════════════════
-- employee_db.sql — Script de création de la base de données
-- Employee Manager — JavaFX
--
-- Usage : mysql -u root -p < employee_db.sql
-- ═══════════════════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS employee_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE employee_db;

-- ── Table des utilisateurs (authentification) ─────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id         INT           NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50)   NOT NULL UNIQUE,
    password   VARCHAR(255)  NOT NULL,        -- Hash BCrypt ($2a$12$...)
    role       VARCHAR(20)   NOT NULL DEFAULT 'EMPLOYE',
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── Table des employés ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employees (
    id          INT            NOT NULL AUTO_INCREMENT,
    nom         VARCHAR(80)    NOT NULL,
    prenom      VARCHAR(80)    NOT NULL,
    email       VARCHAR(150)   NOT NULL UNIQUE,
    telephone   VARCHAR(20)    NOT NULL,
    poste       VARCHAR(100)   NOT NULL,
    salaire     DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX idx_nom_prenom (nom, prenom),
    INDEX idx_poste (poste)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ═══════════════════════════════════════════════════════════════════════════
-- DONNÉES INITIALES
-- ─────────────────────────────────────────────────────────────────────────
-- Mot de passe de l'admin : "admin123"  (hash BCrypt, salt rounds = 12)
-- Mot de passe de l'employé RH : "rh123456"
-- ─────────────────────────────────────────────────────────────────────────
-- IMPORTANT : Ne jamais stocker de mots de passe en clair en production.
--             Ces hashes ont été générés avec BCrypt.gensalt(12).
-- ═══════════════════════════════════════════════════════════════════════════

INSERT INTO users (username, password, role) VALUES
('admin',    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj1JtjCvL/Ki', 'ADMIN'),
('rh',       '$2a$12$8K1p/a0dR1ovDdJoaqByvOREDjCGbcLtnBj9S9CUiSjh5vvwFbCvC', 'RH'),
('employe1', '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'EMPLOYE');
-- Mot de passe par défaut employe1 : "password"

INSERT INTO employees (nom, prenom, email, telephone, poste, salaire) VALUES
('Emp1',   'Prenom1',  'example@company.com',   '701000000', 'Développeur Full Stack',    10000.00);
