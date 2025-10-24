# Gestion de Location de Salle de Sport

Application web développée avec Spring Boot, Thymeleaf et PostgreSQL pour la gestion des locations de salles de sport.

## Fonctionnalités

- **Gestion des salles** : Créer, modifier, supprimer et consulter les salles de sport
- **Gestion des clients** : Gérer les informations des clients avec différents types d'abonnements
- **Gestion des réservations** : Système complet de réservation avec validation des conflits
- **Interface web responsive** : Interface moderne avec Bootstrap 5
- **Données d'exemple** : Initialisation automatique avec des données de test

## Technologies utilisées

- **Backend** : Spring Boot 3.2.0
- **Frontend** : Thymeleaf, Bootstrap 5, JavaScript
- **Base de données** : PostgreSQL
- **Build** : Maven
- **Java** : 17

## Prérequis

- Java 17 ou plus
- PostgreSQL 12 ou plus
- Maven 3.6 ou plus (ou utiliser le wrapper inclus)

## Installation et démarrage

### 1. Cloner le projet
```bash
git clone <repository-url>
cd sport
```

### 2. Configurer la base de données

Créer une base de données PostgreSQL :
```sql
CREATE DATABASE salle_sport_db;
```

### 3. Configurer les paramètres de connexion

Modifier le fichier `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/salle_sport_db
spring.datasource.username=votre_username
spring.datasource.password=votre_password
```

### 4. Lancer l'application

Avec Maven :
```bash
mvn spring-boot:run
```

Ou avec le wrapper Maven :
```bash
./mvnw spring-boot:run
```

### 5. Accéder à l'application

Ouvrez votre navigateur et accédez à : http://localhost:8080

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/gestion/sallesport/
│   │       ├── config/          # Configuration et initialisation des données
│   │       ├── controller/      # Contrôleurs web
│   │       ├── entity/          # Entités JPA
│   │       ├── exception/       # Gestion des exceptions
│   │       ├── repository/      # Repositories Spring Data JPA
│   │       ├── service/         # Services métier
│   │       └── SalleSportApplication.java
│   └── resources/
│       ├── static/              # Ressources statiques (CSS, JS)
│       ├── templates/           # Templates Thymeleaf
│       └── application*.properties
└── test/                        # Tests unitaires et d'intégration
```

## Entités principales

### Salle
- Nom, type, capacité
- Prix par heure
- Description et équipements
- Statut de disponibilité

### Client
- Informations personnelles
- Type d'abonnement (Occasionnel, Mensuel, Trimestriel, Annuel)
- Date d'inscription

### Reservation
- Référence au client et à la salle
- Dates de début et fin
- Calcul automatique du prix
- Statut (En attente, Confirmée, Annulée, Terminée)

## Fonctionnalités avancées

- **Validation des conflits** : Empêche la double réservation d'une salle
- **Calcul automatique des prix** : Basé sur la durée et le tarif horaire
- **Recherche de clients** : Par nom ou prénom
- **Filtrage des salles** : Par disponibilité, type, capacité
- **Interface responsive** : S'adapte aux différentes tailles d'écran

## Profils d'environnement

- **dev** : Développement avec logs détaillés
- **prod** : Production avec optimisations

## Tests

Lancer les tests :
```bash
mvn test
```

## Déploiement

Pour créer un JAR exécutable :
```bash
mvn clean package
java -jar target/salle-sport-0.0.1-SNAPSHOT.jar
```

## Contribution

1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Push vers la branche
5. Ouvrir une Pull Request

## Support

Pour toute question ou problème, ouvrez une issue dans le repository.

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.