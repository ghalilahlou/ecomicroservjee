# 🔧 Solutions aux Problèmes Courants

## ❌ Problème 1 : "No servers available for service: CUSTOMER-SERVICE"

### 📋 Description
```
WARN o.s.c.l.core.RoundRobinLoadBalancer : No servers available for service: CUSTOMER-SERVICE
Error creating bills: [503] during [GET] to [http://CUSTOMER-SERVICE/customers]
```

### 🔍 Cause
Le **billing-service** démarre **AVANT** que le **customer-service** ne soit enregistré dans Eureka.

### ✅ Solution

#### Option 1 : Redémarrer dans le bon ordre (RECOMMANDÉ)

1. **Arrêter tous les services**
2. **Démarrer dans cet ordre précis :**

```bash
# 1. Config Service (attendre 25 secondes)
cd config-service && mvn spring-boot:run

# 2. Discovery Service (attendre 30 secondes)
cd discovery-service && mvn spring-boot:run

# 3. Customer Service (attendre 20 secondes)
cd customer-service && mvn spring-boot:run

# 4. Inventory Service (attendre 20 secondes)
cd inventory-service && mvn spring-boot:run

# 5. Gateway Service (attendre 15 secondes)
cd gateway-service && mvn spring-boot:run

# 6. Billing Service (démarrer en dernier)
cd billing-service && mvn spring-boot:run
```

#### Option 2 : Utiliser le script automatique

**Windows :**
```bash
start-services.bat
```

**Linux/Mac :**
```bash
chmod +x start-services.sh
./start-services.sh
```

#### Option 3 : Redémarrer uniquement Billing Service

Si les autres services sont déjà en cours d'exécution :

1. Arrêter billing-service
2. Vérifier sur http://localhost:8761 que CUSTOMER-SERVICE et INVENTORY-SERVICE sont UP
3. Attendre 30 secondes
4. Redémarrer billing-service

### 🎯 Vérification

Après le démarrage, vérifier :
```bash
# 1. Eureka Dashboard
Ouvrir http://localhost:8761
Vérifier que tous les services sont présents

# 2. Tester l'API
curl http://localhost:8083/fullBill/1
```

---

## ❌ Problème 2 : Gateway Service ne démarre pas ou erreur

### 📋 Description
- Gateway Service a une icône rouge dans l'IDE
- Erreur au démarrage
- Configuration en conflit

### 🔍 Cause
Il y avait **deux fichiers de configuration** :
- `application.properties`
- `application.yml`

Spring Boot ne sait pas lequel utiliser.

### ✅ Solution

J'ai déjà supprimé le fichier `application.properties` en doublon.

Si le problème persiste :

1. **Vérifier qu'il n'y a qu'un seul fichier :**
```bash
ls gateway-service/src/main/resources/
# Devrait montrer seulement application.yml
```

2. **Nettoyer et recompiler :**
```bash
cd gateway-service
mvn clean package -DskipTests
```

3. **Redémarrer le service :**
```bash
mvn spring-boot:run
```

### 🎯 Vérification

```bash
# Vérifier que Gateway démarre
curl http://localhost:9999/actuator/health

# Vérifier les routes
curl http://localhost:9999/actuator/gateway/routes

# Tester via Gateway
curl http://localhost:9999/customer-service/customers
```

---

## ❌ Problème 3 : Port déjà utilisé

### 📋 Description
```
Port 8081 is already in use
Address already in use: bind
```

### 🔍 Cause
Un autre processus utilise déjà ce port, ou le service n'a pas été correctement arrêté.

### ✅ Solution Windows

```bash
# Trouver le processus qui utilise le port 8081
netstat -ano | findstr :8081

# Résultat : TCP 0.0.0.0:8081 0.0.0.0:0 LISTENING 12345
# Le dernier nombre (12345) est le PID

# Tuer le processus
taskkill /F /PID 12345
```

### ✅ Solution Linux/Mac

```bash
# Trouver et tuer le processus
lsof -ti:8081 | xargs kill -9

# Ou pour tous les ports
lsof -ti:8081,8082,8083,8761,8888,9999 | xargs kill -9
```

---

## ❌ Problème 4 : Eureka ne trouve pas les services

### 📋 Description
- Les services démarrent mais n'apparaissent pas dans Eureka Dashboard
- "DOWN" dans Eureka

### 🔍 Cause
- Discovery Service (Eureka) n'est pas démarré
- Les services n'ont pas eu le temps de s'enregistrer
- Problème de configuration

### ✅ Solution

1. **Vérifier que Eureka est démarré :**
```bash
curl http://localhost:8761
# Devrait afficher le dashboard
```

2. **Attendre 30 secondes** après le démarrage d'un service

3. **Vérifier les logs** du service pour :
```
DiscoveryClient_CUSTOMER-SERVICE - registration status: 204
```

4. **Vérifier la configuration** dans `application.properties` :
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

## ❌ Problème 5 : Données non chargées (H2 Database)

### 📋 Description
- Les endpoints retournent des listes vides
- Pas de données pré-chargées

### 🔍 Cause
- Le CommandLineRunner n'a pas été exécuté
- Erreur dans les données de test

### ✅ Solution

1. **Vérifier les logs** au démarrage :
```
Customer: Alice Martin - alice@example.com
Product: Laptop Dell XPS 15 - Price: $1299.99
```

2. **Accéder à H2 Console :**
```
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:customer-db
Username: sa
Password: (vide)
```

3. **Exécuter une requête :**
```sql
SELECT * FROM CUSTOMER;
```

---

## ❌ Problème 6 : OpenFeign ne fonctionne pas

### 📋 Description
```
FeignException: [404] Not Found
FeignException: [503] Service Unavailable
```

### 🔍 Cause
- Les services cibles ne sont pas disponibles
- Mauvaise configuration des FeignClients

### ✅ Solution

1. **Vérifier que les services sont UP dans Eureka**

2. **Tester les endpoints directement :**
```bash
curl http://localhost:8081/customers
curl http://localhost:8082/products
```

3. **Vérifier les FeignClients :**
```java
@FeignClient(name = "CUSTOMER-SERVICE") // Nom en MAJUSCULES
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
}
```

4. **Redémarrer billing-service**

---

## 🎯 Checklist de Démarrage Complet

Utilisez cette checklist à chaque fois que vous démarrez les services :

- [ ] Config Service démarré (port 8888)
  - [ ] Logs : "Started ConfigServiceApplication"
  - [ ] Test : `curl http://localhost:8888/actuator/health`

- [ ] Discovery Service démarré (port 8761)
  - [ ] Logs : "Started DiscoveryServiceApplication"
  - [ ] Dashboard accessible : http://localhost:8761

- [ ] Customer Service démarré (port 8081)
  - [ ] Logs : "Started CustomerServiceApplication"
  - [ ] Logs : Données chargées (Alice, Bob, etc.)
  - [ ] Enregistré dans Eureka
  - [ ] API : `curl http://localhost:8081/customers`

- [ ] Inventory Service démarré (port 8082)
  - [ ] Logs : "Started InventoryServiceApplication"
  - [ ] Logs : Produits chargés
  - [ ] Enregistré dans Eureka
  - [ ] API : `curl http://localhost:8082/products`

- [ ] Gateway Service démarré (port 9999)
  - [ ] Logs : "Started GatewayServiceApplication"
  - [ ] Enregistré dans Eureka
  - [ ] Routes : `curl http://localhost:9999/actuator/gateway/routes`

- [ ] Billing Service démarré (port 8083)
  - [ ] Logs : "Started BillingServiceApplication"
  - [ ] Logs : "Bills created successfully!"
  - [ ] Enregistré dans Eureka
  - [ ] API : `curl http://localhost:8083/fullBill/1`

- [ ] Tests finaux via Gateway
  - [ ] `curl http://localhost:9999/customer-service/customers`
  - [ ] `curl http://localhost:9999/inventory-service/products`
  - [ ] `curl http://localhost:9999/billing-service/fullBill/1`

---

## 🚨 En Cas d'Urgence : Reset Complet

Si rien ne fonctionne, faire un reset complet :

```bash
# 1. Arrêter tous les processus Java
# Windows
taskkill /F /IM java.exe

# Linux/Mac
pkill -9 java

# 2. Nettoyer tous les builds
mvn clean

# 3. Recompiler tout
mvn clean install -DskipTests

# 4. Utiliser le script de démarrage
start-services.bat  # Windows
./start-services.sh  # Linux/Mac
```

---

## 📞 Support Rapide

| Problème | Vérifier | Solution Rapide |
|----------|----------|-----------------|
| 503 Service Unavailable | Eureka Dashboard | Redémarrer dans le bon ordre |
| 404 Not Found | Logs du service | Vérifier que l'endpoint existe |
| Port déjà utilisé | `netstat -ano` | Tuer le processus |
| Service ne démarre pas | Logs d'erreur | `mvn clean install` |
| Pas de données | H2 Console | Vérifier CommandLineRunner |

---

Suivez le **GUIDE-DEMARRAGE.md** pour des instructions détaillées ! 🚀

