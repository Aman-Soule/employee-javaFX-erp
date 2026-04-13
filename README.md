# 📦 Management ERP JavaFX

Un projet ERP (Enterprise Resource Planning) développé en **JavaFX**, permettant de gérer les employés, matériels, catégories, utilisateurs, factures et autres entités métiers.  
Ce projet est conçu pour être **modulaire, extensible et intuitif**, avec une interface graphique moderne et des services robustes.

---

## 🚀 Fonctionnalités

- **Authentification sécurisée**
    - Connexion avec mots de passe hashés via **BCrypt**
    - Gestion des rôles (`ADMIN`, `USER`)
- **Gestion des utilisateurs**
    - CRUD complet
    - Mise à jour des rôles et des mots de passe
- **Gestion des employés**
    - Ajout, modification, suppression
    - Recherche et affichage détaillé
- **Gestion des matériels**
    - CRUD complet
    - Suivi de l’état, localisation, valeur et date d’acquisition
    - Association à une catégorie
- **Gestion des catégories**
    - Organisation des matériels par type
- **Gestion des factures**
    - Génération automatique de factures en PDF
    - Historique et suivi des paiements
- **Tableaux de bord**
    - Statistiques globales (nombre d’éléments, sommes des valeurs, etc.)
    - Interface intuitive en JavaFX

---

## 🏗️ Architecture du projet

Le projet est organisé en plusieurs packages :

- `models` → Définit les entités (Employee, Materiel, User, Categorie, Facture, etc.)
- `services` → Contient la logique métier et les opérations CRUD (MaterielService, UserService, EmployeeService, etc.)
- `config` → Gestion de la base de données (classe `DB`)
- `controllers` → Contrôleurs JavaFX pour les vues
- `views` → Interfaces graphiques (FXML)

---

## ⚙️ Technologies utilisées

- **Java 17+**
- **JavaFX**
- **MySQL** (ou autre SGBD compatible)
- **BCrypt** pour le hachage des mots de passe
- **Maven** pour la gestion des dépendances
- **PDF Generation** pour les factures

---

## 📂 Installation et exécution

1. **Cloner le projet :**
   ```bash
   git clone https://github.com/Aman-Soule/Management_ERP_JAVA_FX.git
   cd Management_ERP_JAVA_FX
2. Configurer la base de données :

Créer une base erp_javafx

Importer le script SQL fourni (tables users, materiel, categorie, employee, facture, etc.)

Vérifier la configuration dans config/DB.java

Lancer l’application :

mvn clean install
mvn javafx:run

Authentification
Les mots de passe sont hashés avec BCrypt avant stockage.

L’authentification compare le mot de passe saisi avec le hash enregistré.

Les rôles permettent de gérer les droits d’accès (ex. ADMIN, USER).

📊 Statistiques disponibles
Nombre total de matériels

Valeur totale du parc matériel

Nombre d’utilisateurs enregistrés

Nombre d’employés actifs

Nombre de factures générées

🖼️ Aperçu des vues
Tableau de bord principal

Gestion des employés

Gestion des matériels

Gestion des catégories

Gestion des factures

Authentification et gestion des utilisateurs

🤝 Contribution
Les contributions sont les bienvenues !
Merci de créer une issue ou une pull request pour proposer des améliorations.

📜 Licence
Projet open-source