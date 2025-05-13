# Supermarché Application

## Description
Supermarché Application est un système de gestion complet pour une supérette, développé en Java avec une base de données PostgreSQL. Cette application permet de gérer efficacement les produits, les fournisseurs, les contrats, les achats, les ventes et les stocks. Elle inclut également des fonctionnalités avancées comme la génération de tickets de caisse et un tableau de bord pour le suivi des performances.

## Fonctionnalités
### Gestion des Produits
- Ajout, modification, suppression et recherche de produits.
- Association des produits à des catégories.
- Gestion des variations via des descriptions détaillées.

### Gestion des Fournisseurs
- Ajout, modification et suppression de fournisseurs.
- Gestion des informations de contact (nom, SIRET, adresse, email, téléphone).

### Gestion des Contrats
- Association des contrats aux produits et fournisseurs.
- Mise à jour automatique de la date de fin lors d’un changement de prix.
- Création d’un nouveau contrat lors de la modification du prix.

### Gestion des Achats
- Enregistrement des achats avec quantités, prix unitaire, dates d'achat et de péremption.
- Association des achats aux fournisseurs et produits.

### Gestion des Stocks
- Mise à jour dynamique des stocks après chaque achat et vente.
- Suivi des lots proches de la date de péremption.

### Gestion des Ventes
- Sélection des produits à vendre avec leur prix.
- Enregistrement des ventes avec numéro de lot, date et prix.
- Génération automatique d’un ticket de caisse.
- Gestion des produits expirés à 0 €.

### Tableau de Bord
- Affichage des ventes journalières et mensuelles.
- Classement des 10 meilleures ventes en quantité et bénéfices.
- Rafraîchissement dynamique après chaque vente.

## Technologies Utilisées
- **Langage** : Java
- **Base de données** : PostgreSQL
- **Frameworks** : JDBC pour la connexion à la base de données
- **IDE** : Eclipse

## Installation
1. Importer le projet dans Eclipse.
2. Configurer la base de données PostgreSQL avec les scripts SQL fournis.
3. Exécuter l'application via la classe `SuperetteApp`.

## Contraintes Techniques
- Hébergement : Fonctionne sur le serveur universitaire `pedago.univ-avignon.fr`.
- Respect des normes de développement en Java.

## Améliorations Possibles
- Ajout d’une gestion avancée des clients avec historique des achats.
- Interface utilisateur plus ergonomique et intuitive.
- Gestion automatique des alertes pour les stocks bas et les produits expirés.
- Ajout de statistiques avancées et de prévisions de ventes.

## Auteurs
- **Nom de l'équipe ou des développeurs** : Chaabane Mohamed

