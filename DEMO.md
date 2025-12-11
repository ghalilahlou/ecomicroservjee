# Démonstration du Projet Spring Boot - Gestion des Comptes

## 🚀 Démarrage rapide

### 1. Compilation et lancement
```bash
# Windows
start.bat

# Linux/Mac
./start.sh

# Ou manuellement
mvn clean compile
mvn spring-boot:run
```

### 2. Accès aux interfaces
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Console H2**: http://localhost:8080/h2-console
- **GraphiQL**: http://localhost:8080/graphiql

## 📋 Fonctionnalités implémentées

### ✅ 1. Projet Spring Boot
- Spring Boot 3.2.0 avec Java 17
- Dépendances : Web, JPA, H2, Lombok, GraphQL, MapStruct
- Configuration automatique

### ✅ 2. Entité JPA Compte
```java
@Entity
public class Compte {
    private Long id;
    private String numeroCompte;
    private Double solde;
    private TypeCompte typeCompte;
    private LocalDateTime dateCreation;
    private String proprietaire;
    private Boolean actif;
}
```

### ✅ 3. Repository Spring Data
```java
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
    List<Compte> findByProprietaire(String proprietaire);
    List<Compte> findByTypeCompte(TypeCompte typeCompte);
    // ... autres méthodes
}
```

### ✅ 4. Tests de la couche DAO
- Tests unitaires avec `@DataJpaTest`
- Tests d'intégration avec `TestRestTemplate`
- Couverture complète des méthodes du repository

### ✅ 5. Web Service RESTful
**Endpoints disponibles :**
- `GET /api/v1/comptes` - Tous les comptes
- `GET /api/v1/comptes/{id}` - Compte par ID
- `POST /api/v1/comptes` - Créer un compte
- `PUT /api/v1/comptes/{id}` - Modifier un compte
- `DELETE /api/v1/comptes/{id}` - Supprimer un compte
- `POST /api/v1/comptes/virement` - Effectuer un virement

### ✅ 6. Tests avec Postman
Utilisez le fichier `test-api.http` pour tester toutes les API :
```http
GET http://localhost:8080/api/test/init-data
GET http://localhost:8080/api/v1/comptes
POST http://localhost:8080/api/v1/comptes
```

### ✅ 7. Documentation Swagger
- Interface interactive : http://localhost:8080/swagger-ui.html
- Documentation automatique des API
- Exemples de requêtes et réponses
- Configuration personnalisée avec OpenAPI 3

### ✅ 8. Spring Data REST avec projections
**Endpoints automatiques :**
- `GET /api/comptes` - Tous les comptes
- `GET /api/comptes/{id}` - Compte par ID
- `POST /api/comptes` - Créer un compte
- `PUT /api/comptes/{id}` - Modifier un compte
- `DELETE /api/comptes/{id}` - Supprimer un compte

**Projections disponibles :**
- `?projection=compteProjection` - Vue complète
- `?projection=compteSummary` - Vue résumée
- `?projection=compteMinimal` - Vue minimale

### ✅ 9. DTOs et Mappers
```java
// DTOs pour la validation et la sérialisation
public class CompteDto { ... }
public class CompteCreateDto { ... }
public class VirementDto { ... }

// Mapper MapStruct pour la conversion
@Mapper(componentModel = "spring")
public interface CompteMapper {
    CompteDto toDto(Compte entity);
    Compte toEntity(CompteCreateDto dto);
}
```

### ✅ 10. Couche Service
```java
@Service
@Transactional
public class CompteService {
    // Logique métier pour la gestion des comptes
    // Gestion des virements avec validation
    // Mapping entre entités et DTOs
}
```

### ✅ 11. Service GraphQL
**Schéma GraphQL :**
```graphql
type Query {
    comptes: [Compte]
    compte(id: ID!): Compte
    comptesByProprietaire(proprietaire: String!): [Compte]
}

type Mutation {
    createCompte(input: CompteInput!): Compte
    effectuerVirement(compteSource: String!, compteDestination: String!, montant: Float!): String
}
```

**Interface GraphiQL :** http://localhost:8080/graphiql

## 🧪 Tests et validation

### Tests unitaires
```bash
mvn test
```

### Tests d'intégration
- Tests des contrôleurs REST
- Tests des repositories JPA
- Tests des services métier

### Tests manuels avec Postman
1. Initialiser les données : `GET /api/test/init-data`
2. Tester les CRUD operations
3. Tester les virements
4. Tester les recherches

## 📊 Base de données

### Console H2
- URL : http://localhost:8080/h2-console
- JDBC URL : `jdbc:h2:mem:testdb`
- Username : `sa`
- Password : (vide)

### Données d'initialisation
Le fichier `data.sql` charge automatiquement des données de test au démarrage.

## 🔧 Configuration

### Fichiers de configuration
- `application.yml` - Configuration principale
- `application-test.yml` - Configuration pour les tests
- `data.sql` - Données d'initialisation

### Propriétés importantes
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
  data:
    rest:
      base-path: /api
```

## 📁 Structure du projet

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── controller/          # Contrôleurs REST et GraphQL
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── entity/              # Entités JPA
│   │   ├── graphql/             # Contrôleurs GraphQL
│   │   ├── mapper/              # Mappers MapStruct
│   │   ├── projection/          # Projections Spring Data REST
│   │   ├── repository/          # Repositories Spring Data
│   │   ├── service/             # Services métier
│   │   └── config/              # Configurations
│   └── resources/
│       ├── application.yml      # Configuration
│       ├── data.sql            # Données d'initialisation
│       └── graphql/            # Schémas GraphQL
└── test/
    └── java/com/example/
        ├── controller/          # Tests d'intégration
        └── repository/          # Tests unitaires
```

## 🎯 Points forts du projet

1. **Architecture complète** : Couches bien séparées (Controller, Service, Repository)
2. **API REST moderne** : Documentation Swagger, validation, gestion d'erreurs
3. **Spring Data REST** : API automatique avec projections personnalisées
4. **GraphQL** : API flexible pour requêtes complexes
5. **Tests complets** : Tests unitaires et d'intégration
6. **Documentation** : README détaillé, exemples d'utilisation
7. **Configuration flexible** : Profils de test, configuration externalisée

## 🚀 Prochaines étapes

1. **Déployer l'application** sur un serveur
2. **Ajouter la sécurité** avec Spring Security
3. **Implémenter la pagination** pour les grandes listes
4. **Ajouter la validation avancée** des données
5. **Implémenter la gestion des erreurs** centralisée
6. **Ajouter la monitoring** avec Actuator
