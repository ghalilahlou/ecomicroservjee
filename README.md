# Microservices e-commerce (Spring Boot + Angular)

Application de démonstration d’architecture microservices pour un commerce électronique. Trois services métier (clients, inventaire, facturation) sont exposés via une passerelle API ; un serveur de configuration et un service de découverte assurent la configuration centralisée et la résolution dynamique. Le front Angular consomme les API via la passerelle.

## Architecture et services

- **Config Service (Spring Cloud Config Server)** : sert la configuration versionnée (Git) à tous les services.  
- **Discovery Service (Eureka)** : enregistre/découvre les services pour permettre le routage dynamique.  
- **Gateway Service (Spring Cloud Gateway)** : point d’entrée unique, routage `/customer-service/**`, `/inventory-service/**`, `/billing-service/**`, CORS activé pour `http://localhost:4200`.  
- **Customer Service** : CRUD clients (Spring Boot, H2).  
- **Inventory Service** : CRUD produits/stock (Spring Boot, H2, données préchargées).  
- **Billing Service** : création/consultation de factures, agrège clients et produits via clients Feign.  
- **Front-end Angular (Angular 18 + Bootstrap)** : SPA pour gérer clients, produits et factures (CRUD, totaux, formulaires).

### Logique métier (vue synthétique)
- **Customer Service** : expose les endpoints clients ; la passerelle relaie `/customer-service/customers`.  
- **Inventory Service** : expose produits et quantités ; la passerelle relaie `/inventory-service/products`.  
- **Billing Service** : crée des factures en combinant un client (id) et une liste de lignes produits (id, quantité, prix). Lorsqu’une facture est consultée, le service interroge `customer-service` et `inventory-service` via Feign pour enrichir les données (noms clients/produits).  
- **Gateway** : résout les noms de service via Eureka et applique `StripPrefix=1`, gère CORS pour le front.  
- **Front Angular** : appelle le Gateway (`http://localhost:9999`) pour lister/ajouter/modifier/supprimer clients, produits et factures ; calcule et affiche les totaux côté UI.

## Démarrage local (ordre recommandé)
1. `config-service` (port 8888) – nécessite `config-repo` initialisé en Git.  
2. `discovery-service` (port 8761).  
3. `customer-service` (8081), `inventory-service` (8082), `billing-service` (8083).  
4. `gateway-service` (9999).  
5. Front : `cd ecom-frontend && npm install && npm start` (Angular dev server sur `http://localhost:4200`).  

Accès rapides :  
- Eureka : `http://localhost:8761`  
- Gateway : `http://localhost:9999`  
- Front : `http://localhost:4200`  
- H2 consoles : services 8081/8082/8083  

## Captures d’écran (interface et services)
Les fichiers sont déjà dans `image/`. Les liens ci-dessous utilisent l’encodage des espaces et apostrophes pour s’afficher correctement sur GitHub. Chaque image est suivie d’une courte légende.

### Interface Angular (front)
- ![Clients (CRUD)](image/Capture%20d%27%C3%A9cran%202025-12-11%20090400.png)  
  _Liste des clients avec actions modifier/supprimer, consommée via `customer-service` derrière le Gateway._
- ![Produits (CRUD + stock)](image/Capture%20d%27%C3%A9cran%202025-12-11%20090409.png)  
  _Inventaire produits, prix et quantités ; données servies par `inventory-service` via la passerelle._
- ![Création facture](image/Capture%20d%27%C3%A9cran%202025-12-11%20090420.png)  
  _Formulaire de facture : saisie clientId et date ; l’enregistrement appelle `billing-service` qui agrège client/produits._
- ![Clients (services arrêtés)](image/Capture%20d%27%C3%A9cran%202025-12-11%20085514.png)  
  _Vue front affichant l’erreur de chargement quand les services back ne sont pas démarrés._

### Services, passerelle, consoles
- ![Eureka Dashboard](image/Capture%20d%27%C3%A9cran%202025-12-11%20083057.png)  
  _Instances enregistrées : gateway, customer, inventory, billing ; santé et URLs exposées._
- ![Customer-service HAL](image/Capture%20d%27%C3%A9cran%202025-12-11%20083116.png)  
  _Endpoints HAL du service clients (`/customers`, `/profile`)._
- ![Billing/Inventory HAL](image/Capture%20d%27%C3%A9cran%202025-12-11%20083142.png)  
  _Racine HAL du service billing/inventory : liens `productItems`, `bills`, `profile`._
- ![IDE – customer logs](image/Capture%20d%27%C3%A9cran%202025-12-11%20083332.png)  
  _Vue Services IntelliJ : instances Spring Boot et logs du `customer-service` (enregistrement Eureka, requêtes)._
- ![IDE – inventory logs](image/Capture%20d%27%C3%A9cran%202025-12-11%20083349.png)  
  _Logs du `inventory-service` avec données préchargées (produits, quantités) et enregistrement Eureka._
- ![Console H2 – inventory](image/Capture%20d%27%C3%A9cran%202025-12-11%20084321.png)  
  _Connexion à la base H2 `jdbc:h2:mem:inventory-db` (mode mémoire) du service inventaire._
- ![Alerte CSP navigateur](image/Capture%20d%27%C3%A9cran%202025-12-10%20095908.png)  
  _Avertissement Content-Security-Policy côté navigateur (blocage de `eval`) à traiter si vous chargez des scripts externes._

> Astuce : si vous préférez des noms plus courts (ex. `clients.png`, `products.png`), renommez les fichiers dans `image/` et mettez à jour les liens ci-dessus.

## Flux typique
1. Le front appelle `GET http://localhost:9999/customer-service/customers` pour lister les clients.  
2. Pour les produits : `GET http://localhost:9999/inventory-service/products`.  
3. Pour créer une facture : le front envoie un clientId et des lignes produit à `POST http://localhost:9999/billing-service/bills`. `billing-service` récupère les détails clients/produits via Feign, calcule le total, puis retourne la facture enrichie.  
4. Les réponses sont rendues dans l’UI (tables, badges de quantité, totaux).

## Stack technique
- Back-end : Java, Spring Boot, Spring Cloud (Config, Eureka, Gateway), Spring Data JPA, H2, Feign.  
- Front-end : Angular 18, TypeScript, Bootstrap.  
- Build : Maven, npm/Angular CLI.

## Extensions possibles
- Persistance PostgreSQL/MySQL au lieu de H2 en mémoire.  
- Authentification/autorisation (Spring Security, Keycloak).  
- Observabilité (metrics, traces distribuées).  
- Conteneurisation (Docker/Compose, Kubernetes).