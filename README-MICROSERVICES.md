# E-commerce Microservices Architecture

## 📋 Description du Projet

Application e-commerce complète basée sur une architecture microservices utilisant Spring Boot et Spring Cloud.

## 🏗️ Architecture

Le projet est composé de 6 microservices :

### 1. **config-service** (Port 8888)
- Serveur de configuration centralisé
- Gère les configurations de tous les services
- Utilise Spring Cloud Config Server

### 2. **discovery-service** (Port 8761)
- Service Registry (Eureka Server)
- Enregistrement et découverte des microservices
- Console accessible via : http://localhost:8761

### 3. **gateway-service** (Port 9999)
- API Gateway (Point d'entrée unique)
- Routage dynamique vers les microservices
- Configuration basée sur Eureka Discovery

### 4. **customer-service** (Port 8081)
- Gestion des clients
- Entité : Customer (id, name, email)
- Base de données : H2 (In-Memory)
- API REST exposée via Spring Data REST

### 5. **inventory-service** (Port 8082)
- Gestion de l'inventaire produits
- Entité : Product (id, name, price, quantity)
- Base de données : H2 (In-Memory)
- API REST exposée via Spring Data REST

### 6. **billing-service** (Port 8083)
- Gestion des factures
- Entités : Bill, ProductItem
- Utilise OpenFeign pour communiquer avec customer-service et inventory-service
- Base de données : H2 (In-Memory)

## 🛠️ Stack Technique

- **Java** : 21
- **Spring Boot** : 3.5.6
- **Spring Cloud** : 2025.0.0
- **Base de données** : H2 (In-Memory)
- **Build Tool** : Maven
- **Outils** : Lombok, Spring Boot Actuator, Spring Data REST, OpenFeign

## 📁 Structure du Projet

```
ecom-microservices/
├── pom.xml (Parent POM)
├── config-service/
│   ├── pom.xml
│   └── src/
├── discovery-service/
│   ├── pom.xml
│   └── src/
├── gateway-service/
│   ├── pom.xml
│   └── src/
├── customer-service/
│   ├── pom.xml
│   └── src/
├── inventory-service/
│   ├── pom.xml
│   └── src/
├── billing-service/
│   ├── pom.xml
│   └── src/
└── config-repo/
    ├── customer-service.properties
    ├── inventory-service.properties
    ├── billing-service.properties
    └── gateway-service.properties
```

## 🚀 Démarrage de l'Application

### Prérequis
- Java 21
- Maven 3.9+

### Ordre de Démarrage (Important !)

1. **Config Service** (en premier)
```bash
cd config-service
mvn spring-boot:run
```
Attendre que le service soit complètement démarré avant de passer au suivant.

2. **Discovery Service**
```bash
cd discovery-service
mvn spring-boot:run
```
Vérifier que Eureka est accessible : http://localhost:8761

3. **Gateway Service**
```bash
cd gateway-service
mvn spring-boot:run
```

4. **Services Métiers** (peuvent être lancés en parallèle)

Customer Service :
```bash
cd customer-service
mvn spring-boot:run
```

Inventory Service :
```bash
cd inventory-service
mvn spring-boot:run
```

Billing Service :
```bash
cd billing-service
mvn spring-boot:run
```

### Alternative : Scripts de Démarrage

**Windows (PowerShell)** :
```powershell
# Créer un script start-all.ps1
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd config-service; mvn spring-boot:run"
Start-Sleep -Seconds 15
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd discovery-service; mvn spring-boot:run"
Start-Sleep -Seconds 15
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd gateway-service; mvn spring-boot:run"
Start-Sleep -Seconds 10
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd customer-service; mvn spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd inventory-service; mvn spring-boot:run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd billing-service; mvn spring-boot:run"
```

**Linux/Mac (Bash)** :
```bash
# Créer un script start-all.sh
#!/bin/bash
cd config-service && mvn spring-boot:run &
sleep 15
cd ../discovery-service && mvn spring-boot:run &
sleep 15
cd ../gateway-service && mvn spring-boot:run &
sleep 10
cd ../customer-service && mvn spring-boot:run &
cd ../inventory-service && mvn spring-boot:run &
cd ../billing-service && mvn spring-boot:run &
```

## 🔍 Tests des APIs

### Via Gateway (Recommandé)

**Customers** :
```bash
# Liste des clients
curl http://localhost:9999/customer-service/customers

# Client spécifique
curl http://localhost:9999/customer-service/customers/1
```

**Products** :
```bash
# Liste des produits
curl http://localhost:9999/inventory-service/products

# Produit spécifique
curl http://localhost:9999/inventory-service/products/1
```

**Bills** :
```bash
# Liste des factures
curl http://localhost:9999/billing-service/bills

# Facture complète avec détails client et produits
curl http://localhost:9999/billing-service/fullBill/1
```

### Accès Direct aux Services

**Customer Service** :
```bash
curl http://localhost:8081/customers
```

**Inventory Service** :
```bash
curl http://localhost:8082/products
```

**Billing Service** :
```bash
curl http://localhost:8083/fullBill/1
```

## 🔧 Consoles et Monitoring

- **Eureka Dashboard** : http://localhost:8761
- **Config Server** : http://localhost:8888/customer-service/default
- **H2 Console - Customer Service** : http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:customer-db`
- **H2 Console - Inventory Service** : http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:inventory-db`
- **H2 Console - Billing Service** : http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:billing-db`

## 📊 Actuator Endpoints

Chaque service expose des endpoints Actuator :
- `/actuator/health` - État de santé du service
- `/actuator/info` - Informations sur le service
- `/actuator/metrics` - Métriques du service

Exemples :
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/metrics
```

## 🔄 Communication entre Services

Le **billing-service** utilise **OpenFeign** pour communiquer avec les autres services :

```java
@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
}

@FeignClient(name = "INVENTORY-SERVICE")
public interface ProductRestClient {
    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable Long id);
}
```

## 📝 Configuration Centralisée

Les fichiers de configuration sont stockés dans le dossier `config-repo/` :
- `customer-service.properties`
- `inventory-service.properties`
- `billing-service.properties`
- `gateway-service.properties`

Ces configurations sont chargées par le **config-service** au démarrage de chaque microservice.

## 🐛 Troubleshooting

### Problème : Service ne démarre pas
- Vérifier que le port n'est pas déjà utilisé
- S'assurer que Java 21 est installé : `java -version`
- Vérifier les logs pour identifier l'erreur

### Problème : Services ne se trouvent pas
- Vérifier que discovery-service est démarré et accessible
- Consulter le dashboard Eureka : http://localhost:8761
- Attendre 30 secondes après le démarrage pour l'enregistrement

### Problème : Billing-service ne récupère pas les données
- Vérifier que customer-service et inventory-service sont enregistrés dans Eureka
- Tester les endpoints directement
- Consulter les logs du billing-service

## 📦 Build du Projet

Construire tous les services :
```bash
mvn clean install
```

Construire un service spécifique :
```bash
cd customer-service
mvn clean package
```

## 🎯 Endpoints Principaux

| Service | Port | Endpoint Principal | Description |
|---------|------|-------------------|-------------|
| Config Service | 8888 | `/customer-service/default` | Configuration |
| Discovery Service | 8761 | `/` | Eureka Dashboard |
| Gateway Service | 9999 | `/` | API Gateway |
| Customer Service | 8081 | `/customers` | Gestion clients |
| Inventory Service | 8082 | `/products` | Gestion produits |
| Billing Service | 8083 | `/fullBill/{id}` | Factures complètes |

## 📚 Documentation API

### Customer API
- `GET /customers` - Liste tous les clients
- `GET /customers/{id}` - Récupère un client
- `POST /customers` - Crée un client
- `PUT /customers/{id}` - Met à jour un client
- `DELETE /customers/{id}` - Supprime un client

### Product API
- `GET /products` - Liste tous les produits
- `GET /products/{id}` - Récupère un produit
- `POST /products` - Crée un produit
- `PUT /products/{id}` - Met à jour un produit
- `DELETE /products/{id}` - Supprime un produit

### Bill API
- `GET /bills` - Liste toutes les factures
- `GET /bills/{id}` - Récupère une facture
- `GET /fullBill/{id}` - Récupère une facture avec tous les détails (client, produits)

## 🔐 Sécurité

Pour l'instant, l'application n'implémente pas de sécurité. Pour une utilisation en production, il est recommandé d'ajouter :
- Spring Security
- OAuth2 / JWT
- HTTPS
- Rate Limiting

## 🚧 Améliorations Futures

- [ ] Ajouter un frontend Angular
- [ ] Implémenter Spring Security
- [ ] Ajouter des tests unitaires et d'intégration
- [ ] Mettre en place un système de logs centralisé (ELK Stack)
- [ ] Ajouter Circuit Breaker (Resilience4j)
- [ ] Implémenter le tracing distribué (Zipkin)
- [ ] Dockeriser les services
- [ ] Ajouter une base de données persistante (PostgreSQL)

## 👨‍💻 Auteur

Projet développé dans le cadre d'un TP sur les architectures microservices.

## 📄 Licence

Ce projet est à des fins éducatives.

