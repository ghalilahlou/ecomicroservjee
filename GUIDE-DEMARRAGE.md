# 🚀 Guide de Démarrage des Microservices

## ⚠️ ORDRE DE DÉMARRAGE IMPORTANT

Pour que tous les services fonctionnent correctement, vous **DEVEZ** respecter cet ordre :

### 1️⃣ Config Service (Port 8888)
**Démarrage :**
```bash
cd config-service
mvn spring-boot:run
```
**Attendre :** 20-25 secondes

**Vérification :** 
- Logs : "Started ConfigServiceApplication"
- URL : http://localhost:8888/actuator/health

---

### 2️⃣ Discovery Service / Eureka (Port 8761)
**Démarrage :**
```bash
cd discovery-service
mvn spring-boot:run
```
**Attendre :** 25-30 secondes

**Vérification :**
- Logs : "Started DiscoveryServiceApplication"
- Dashboard : http://localhost:8761
- Vous devez voir "Instances currently registered with Eureka"

---

### 3️⃣ Customer Service (Port 8081)
**Démarrage :**
```bash
cd customer-service
mvn spring-boot:run
```
**Attendre :** 15-20 secondes

**Vérification :**
- Logs : "Started CustomerServiceApplication"
- Logs : "Customer: Alice Martin - alice@example.com" (données pré-chargées)
- Eureka : Vérifier que CUSTOMER-SERVICE apparaît dans http://localhost:8761
- API : http://localhost:8081/customers

---

### 4️⃣ Inventory Service (Port 8082)
**Démarrage :**
```bash
cd inventory-service
mvn spring-boot:run
```
**Attendre :** 15-20 secondes

**Vérification :**
- Logs : "Started InventoryServiceApplication"
- Logs : "Product: Laptop Dell XPS 15 - Price: $1299.99" (données pré-chargées)
- Eureka : Vérifier que INVENTORY-SERVICE apparaît dans http://localhost:8761
- API : http://localhost:8082/products

---

### 5️⃣ Gateway Service (Port 9999)
**Démarrage :**
```bash
cd gateway-service
mvn spring-boot:run
```
**Attendre :** 15-20 secondes

**Vérification :**
- Logs : "Started GatewayServiceApplication"
- Eureka : Vérifier que GATEWAY-SERVICE apparaît dans http://localhost:8761
- Routes : http://localhost:9999/actuator/gateway/routes

---

### 6️⃣ Billing Service (Port 8083)
**Démarrage :**
```bash
cd billing-service
mvn spring-boot:run
```
**Attendre :** 15-20 secondes

**Vérification :**
- Logs : "Started BillingServiceApplication"
- Logs : "Bills created successfully!" (factures créées)
- Eureka : Vérifier que BILLING-SERVICE apparaît dans http://localhost:8761
- API : http://localhost:8083/fullBill/1

---

## 🎯 Script Automatique (Recommandé)

### Windows
```bash
start-services.bat
```

### Linux/Mac
```bash
chmod +x start-services.sh
./start-services.sh
```

Ces scripts démarrent automatiquement tous les services dans le bon ordre avec les bons délais.

---

## 🔍 Vérifications Post-Démarrage

### 1. Vérifier Eureka Dashboard
Ouvrez http://localhost:8761

Vous devez voir **5 services** enregistrés :
- CUSTOMER-SERVICE
- INVENTORY-SERVICE
- BILLING-SERVICE
- GATEWAY-SERVICE
- (config-service ne s'enregistre pas, c'est normal)

### 2. Tester via Gateway

**Customers :**
```bash
curl http://localhost:9999/customer-service/customers
```

**Products :**
```bash
curl http://localhost:9999/inventory-service/products
```

**Bills (avec détails complets) :**
```bash
curl http://localhost:9999/billing-service/fullBill/1
```

### 3. Tester directement les services

```bash
# Customer Service
curl http://localhost:8081/customers

# Inventory Service
curl http://localhost:8082/products

# Billing Service (avec détails Customer et Product)
curl http://localhost:8083/fullBill/1
```

---

## ❌ Problèmes Courants et Solutions

### Erreur : "No servers available for service: CUSTOMER-SERVICE"

**Cause :** Billing-service démarre avant que customer-service soit enregistré dans Eureka.

**Solution :**
1. Arrêter billing-service
2. Attendre 30 secondes que customer-service et inventory-service s'enregistrent
3. Vérifier sur http://localhost:8761 qu'ils sont UP
4. Redémarrer billing-service

### Erreur : Gateway Service ne démarre pas

**Cause :** Conflit entre application.properties et application.yml

**Solution :**
- Supprimer `gateway-service/src/main/resources/application.properties`
- Garder uniquement `application.yml`
- Recompiler : `mvn clean install -DskipTests`

### Erreur : "Unable to find main class"

**Cause :** Maven ne trouve pas la classe principale

**Solution :**
- Vérifier que les pom.xml contiennent `<mainClass>...</mainClass>`
- Recompiler : `mvn clean install -DskipTests`

### Erreur : Port déjà utilisé (Address already in use)

**Cause :** Un service est déjà en cours d'exécution sur ce port

**Solution Windows :**
```bash
# Trouver le processus
netstat -ano | findstr :8081

# Tuer le processus (remplacer PID par le numéro trouvé)
taskkill /F /PID [PID]
```

**Solution Linux/Mac :**
```bash
# Trouver et tuer le processus
lsof -ti:8081 | xargs kill -9
```

---

## 🛠️ Commandes Utiles

### Arrêter tous les services

**Windows :** Fermer toutes les fenêtres CMD ouvertes par les scripts

**Linux/Mac :**
```bash
# Si vous avez les PIDs
kill $CONFIG_PID $DISCOVERY_PID $CUSTOMER_PID $INVENTORY_PID $GATEWAY_PID $BILLING_PID

# Ou forcer l'arrêt de tous
pkill -f "spring-boot:run"
```

### Recompiler tout le projet
```bash
mvn clean install -DskipTests
```

### Recompiler un service spécifique
```bash
cd customer-service
mvn clean package -DskipTests
```

### Voir les logs d'un service
Les logs s'affichent dans la console où vous avez lancé `mvn spring-boot:run`

---

## 📊 Consoles d'Administration

| Service | URL | Credentials |
|---------|-----|------------|
| Eureka Dashboard | http://localhost:8761 | Aucun |
| Config Server | http://localhost:8888/customer-service/default | Aucun |
| H2 Console - Customer | http://localhost:8081/h2-console | jdbc:h2:mem:customer-db |
| H2 Console - Inventory | http://localhost:8082/h2-console | jdbc:h2:mem:inventory-db |
| H2 Console - Billing | http://localhost:8083/h2-console | jdbc:h2:mem:billing-db |

**Credentials H2 :**
- Username : `sa`
- Password : (vide)

---

## 📝 Tests API Complets

Utilisez le fichier `test-services.http` pour tester tous les endpoints.

Ou importez cette collection dans Postman/Insomnia.

---

## ⏱️ Temps de Démarrage Estimé

| Service | Temps |
|---------|-------|
| Config Service | ~20 secondes |
| Discovery Service | ~25 secondes |
| Customer Service | ~15 secondes |
| Inventory Service | ~15 secondes |
| Gateway Service | ~15 secondes |
| Billing Service | ~15 secondes |
| **TOTAL** | **~2-3 minutes** |

---

## 🎓 Ordre de Priorité pour le Débogage

1. **Config Service** - Doit démarrer en premier
2. **Discovery Service (Eureka)** - Tous les autres services en dépendent
3. **Customer + Inventory Services** - Doivent être enregistrés avant Billing
4. **Gateway Service** - Point d'entrée pour les APIs
5. **Billing Service** - Dépend de Customer et Inventory

---

## 📞 Support

Si vous rencontrez des problèmes :

1. Vérifier l'ordre de démarrage
2. Consulter les logs dans les consoles
3. Vérifier Eureka Dashboard (http://localhost:8761)
4. Vérifier que tous les ports sont libres
5. Recompiler avec `mvn clean install -DskipTests`

Bonne chance ! 🚀

