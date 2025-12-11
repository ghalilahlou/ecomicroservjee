# ⚡ Tests Rapides - Microservices E-commerce

## 🎯 Vérification Rapide (5 minutes)

Après avoir démarré tous les services, exécutez ces commandes pour vérifier que tout fonctionne :

```bash
# 1. Eureka Dashboard
curl http://localhost:8761

# 2. Tous les services sont-ils enregistrés ?
curl http://localhost:8761/eureka/apps -H "Accept: application/json"

# 3. Customer Service
curl http://localhost:9999/customer-service/customers

# 4. Inventory Service
curl http://localhost:9999/inventory-service/products

# 5. Billing Service (avec détails complets)
curl http://localhost:9999/billing-service/fullBill/1
```

Si toutes ces commandes fonctionnent ✅, votre architecture microservices est **opérationnelle** !

---

## 🔍 Tests Détaillés par Service

### 1. Config Service (Port 8888)

```bash
# Health check
curl http://localhost:8888/actuator/health

# Configuration de Customer Service
curl http://localhost:8888/customer-service/default

# Configuration d'Inventory Service
curl http://localhost:8888/inventory-service/default
```

**Résultat attendu :** Fichiers de configuration JSON

---

### 2. Discovery Service / Eureka (Port 8761)

```bash
# Dashboard (navigateur)
http://localhost:8761

# Liste des services enregistrés (JSON)
curl http://localhost:8761/eureka/apps -H "Accept: application/json"

# Health check
curl http://localhost:8761/actuator/health
```

**Résultat attendu :** 5 services (CUSTOMER, INVENTORY, BILLING, GATEWAY)

---

### 3. Customer Service (Port 8081)

```bash
# Tous les clients
curl http://localhost:8081/customers

# Client spécifique
curl http://localhost:8081/customers/1

# Avec pagination
curl "http://localhost:8081/customers?page=0&size=2"

# H2 Console (navigateur)
http://localhost:8081/h2-console
# JDBC URL: jdbc:h2:mem:customer-db
# Username: sa
# Password: (vide)
```

**Résultat attendu :** 
```json
{
  "_embedded": {
    "customers": [
      {"id": 1, "name": "Alice Martin", "email": "alice@example.com"},
      {"id": 2, "name": "Bob Dupont", "email": "bob@example.com"},
      ...
    ]
  }
}
```

---

### 4. Inventory Service (Port 8082)

```bash
# Tous les produits
curl http://localhost:8082/products

# Produit spécifique
curl http://localhost:8082/products/1

# Avec pagination
curl "http://localhost:8082/products?page=0&size=3"

# H2 Console (navigateur)
http://localhost:8082/h2-console
# JDBC URL: jdbc:h2:mem:inventory-db
```

**Résultat attendu :**
```json
{
  "_embedded": {
    "products": [
      {"id": 1, "name": "Laptop Dell XPS 15", "price": 1299.99, "quantity": 75},
      {"id": 2, "name": "iPhone 15 Pro", "price": 999.99, "quantity": 42},
      ...
    ]
  }
}
```

---

### 5. Gateway Service (Port 9999)

```bash
# Health check
curl http://localhost:9999/actuator/health

# Routes configurées
curl http://localhost:9999/actuator/gateway/routes

# Tester les routes
curl http://localhost:9999/customer-service/customers
curl http://localhost:9999/inventory-service/products
curl http://localhost:9999/billing-service/bills
```

**Résultat attendu :** Toutes les requêtes passent par la Gateway

---

### 6. Billing Service (Port 8083)

```bash
# Toutes les factures (basique)
curl http://localhost:8083/bills

# Facture avec détails complets (Customer + Products)
curl http://localhost:8083/fullBill/1

# Via Gateway (RECOMMANDÉ)
curl http://localhost:9999/billing-service/fullBill/1

# H2 Console (navigateur)
http://localhost:8083/h2-console
# JDBC URL: jdbc:h2:mem:billing-db
```

**Résultat attendu :**
```json
{
  "id": 1,
  "billingDate": "2025-11-27T14:30:00.000+00:00",
  "customerId": 1,
  "customer": {
    "id": 1,
    "name": "Alice Martin",
    "email": "alice@example.com"
  },
  "productItems": [
    {
      "id": 1,
      "productId": 2,
      "price": 999.99,
      "quantity": 2,
      "product": {
        "id": 2,
        "name": "iPhone 15 Pro",
        "price": 999.99,
        "quantity": 42
      }
    }
  ]
}
```

---

## 🚀 Tests via Gateway (Production-Ready)

Utilisez **toujours** la Gateway en production :

```bash
# GET Customers
curl http://localhost:9999/customer-service/customers

# GET Customer par ID
curl http://localhost:9999/customer-service/customers/1

# POST Nouveau Customer
curl -X POST http://localhost:9999/customer-service/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Eva Laurent","email":"eva@example.com"}'

# GET Products
curl http://localhost:9999/inventory-service/products

# POST Nouveau Product
curl -X POST http://localhost:9999/inventory-service/products \
  -H "Content-Type: application/json" \
  -d '{"name":"AirPods Pro","price":249.99,"quantity":50}'

# GET Bills
curl http://localhost:9999/billing-service/bills

# GET Full Bill
curl http://localhost:9999/billing-service/fullBill/1
```

---

## 📊 Tests SQL (H2 Console)

### Customer Service Database

```sql
-- Voir tous les clients
SELECT * FROM CUSTOMER;

-- Compter les clients
SELECT COUNT(*) FROM CUSTOMER;

-- Rechercher par nom
SELECT * FROM CUSTOMER WHERE NAME LIKE '%Martin%';
```

### Inventory Service Database

```sql
-- Voir tous les produits
SELECT * FROM PRODUCT;

-- Produits par prix
SELECT * FROM PRODUCT ORDER BY PRICE DESC;

-- Stock faible
SELECT * FROM PRODUCT WHERE QUANTITY < 50;
```

### Billing Service Database

```sql
-- Voir toutes les factures
SELECT * FROM BILL;

-- Voir tous les items
SELECT * FROM PRODUCT_ITEM;

-- Factures par client
SELECT b.ID, b.BILLING_DATE, b.CUSTOMER_ID, 
       COUNT(pi.ID) as NB_ITEMS
FROM BILL b
LEFT JOIN PRODUCT_ITEM pi ON b.ID = pi.BILL_ID
GROUP BY b.ID, b.BILLING_DATE, b.CUSTOMER_ID;

-- Total par facture
SELECT b.ID, b.CUSTOMER_ID, 
       SUM(pi.PRICE * pi.QUANTITY) as TOTAL
FROM BILL b
LEFT JOIN PRODUCT_ITEM pi ON b.ID = pi.BILL_ID
GROUP BY b.ID, b.CUSTOMER_ID;
```

---

## 🧪 Tests Postman/Insomnia

Importez cette collection :

```json
{
  "info": {
    "name": "E-commerce Microservices",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Gateway - Get Customers",
      "request": {
        "method": "GET",
        "url": "http://localhost:9999/customer-service/customers"
      }
    },
    {
      "name": "Gateway - Get Products",
      "request": {
        "method": "GET",
        "url": "http://localhost:9999/inventory-service/products"
      }
    },
    {
      "name": "Gateway - Get Full Bill",
      "request": {
        "method": "GET",
        "url": "http://localhost:9999/billing-service/fullBill/1"
      }
    }
  ]
}
```

---

## 🎭 Scénario de Test Complet

### Scénario : Créer une nouvelle commande

```bash
# 1. Créer un nouveau client
curl -X POST http://localhost:9999/customer-service/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sophie Durand",
    "email": "sophie@example.com"
  }'

# Réponse : {"id": 5, "name": "Sophie Durand", "email": "sophie@example.com"}

# 2. Vérifier les produits disponibles
curl http://localhost:9999/inventory-service/products

# 3. Créer un nouveau produit
curl -X POST http://localhost:9999/inventory-service/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPad Pro 12.9",
    "price": 1099.99,
    "quantity": 25
  }'

# 4. Vérifier les factures existantes
curl http://localhost:9999/billing-service/bills

# 5. Voir une facture complète
curl http://localhost:9999/billing-service/fullBill/1
```

---

## 📈 Tests de Performance

```bash
# Test simple avec Apache Bench (si installé)
ab -n 100 -c 10 http://localhost:9999/customer-service/customers

# Ou avec curl en boucle
for i in {1..10}; do
  echo "Test $i:"
  time curl -s http://localhost:9999/billing-service/fullBill/1 > /dev/null
done
```

---

## ✅ Checklist de Test Final

Avant de considérer le système comme opérationnel :

- [ ] Tous les services sont UP dans Eureka (http://localhost:8761)
- [ ] Gateway répond : `curl http://localhost:9999/actuator/health`
- [ ] Customers accessibles via Gateway
- [ ] Products accessibles via Gateway
- [ ] Bills avec détails complets (Customer + Products)
- [ ] Nouveaux clients peuvent être créés
- [ ] Nouveaux produits peuvent être créés
- [ ] H2 Consoles accessibles pour tous les services
- [ ] Config Server retourne les configurations
- [ ] Pas d'erreurs dans les logs

---

## 🎯 Tests Automatisés (Bonus)

Créez un fichier `test-all.sh` :

```bash
#!/bin/bash

echo "🧪 Tests Automatisés des Microservices"
echo "======================================"

echo ""
echo "1️⃣  Test Eureka..."
if curl -s http://localhost:8761 > /dev/null; then
    echo "✅ Eureka UP"
else
    echo "❌ Eureka DOWN"
    exit 1
fi

echo ""
echo "2️⃣  Test Customer Service..."
if curl -s http://localhost:9999/customer-service/customers > /dev/null; then
    echo "✅ Customer Service UP"
else
    echo "❌ Customer Service DOWN"
fi

echo ""
echo "3️⃣  Test Inventory Service..."
if curl -s http://localhost:9999/inventory-service/products > /dev/null; then
    echo "✅ Inventory Service UP"
else
    echo "❌ Inventory Service DOWN"
fi

echo ""
echo "4️⃣  Test Billing Service..."
if curl -s http://localhost:9999/billing-service/fullBill/1 > /dev/null; then
    echo "✅ Billing Service UP"
else
    echo "❌ Billing Service DOWN"
fi

echo ""
echo "======================================"
echo "✅ Tests terminés !"
```

Exécutez :
```bash
chmod +x test-all.sh
./test-all.sh
```

---

Utilisez le fichier **test-services.http** pour des tests complets dans VS Code ! 🚀

