#!/bin/bash

echo "========================================"
echo " Démarrage des Microservices E-commerce"
echo "========================================"
echo ""
echo "IMPORTANT: Attendez que chaque service soit complètement démarré!"
echo ""

echo "[1/6] Démarrage de Config Service (Port 8888)..."
cd config-service
mvn spring-boot:run &
CONFIG_PID=$!
cd ..
echo "Attendre 25 secondes pour Config Service..."
sleep 25
echo ""

echo "[2/6] Démarrage de Discovery Service (Port 8761)..."
cd discovery-service
mvn spring-boot:run &
DISCOVERY_PID=$!
cd ..
echo "Attendre 30 secondes pour Discovery Service (Eureka)..."
sleep 30
echo ""

echo "[3/6] Démarrage de Customer Service (Port 8081)..."
cd customer-service
mvn spring-boot:run &
CUSTOMER_PID=$!
cd ..
echo "Attendre 20 secondes pour Customer Service..."
sleep 20
echo ""

echo "[4/6] Démarrage de Inventory Service (Port 8082)..."
cd inventory-service
mvn spring-boot:run &
INVENTORY_PID=$!
cd ..
echo "Attendre 20 secondes pour Inventory Service..."
sleep 20
echo ""

echo "[5/6] Démarrage de Gateway Service (Port 9999)..."
cd gateway-service
mvn spring-boot:run &
GATEWAY_PID=$!
cd ..
echo "Attendre 20 secondes pour Gateway Service..."
sleep 20
echo ""

echo "[6/6] Démarrage de Billing Service (Port 8083)..."
cd billing-service
mvn spring-boot:run &
BILLING_PID=$!
cd ..
echo ""

echo "========================================"
echo " Tous les services sont en cours de démarrage!"
echo "========================================"
echo ""
echo "Points d'accès:"
echo "- Eureka Dashboard : http://localhost:8761"
echo "- Gateway API      : http://localhost:9999"
echo "- Config Server    : http://localhost:8888"
echo "- Customer Service : http://localhost:8081"
echo "- Inventory Service: http://localhost:8082"
echo "- Billing Service  : http://localhost:8083"
echo ""
echo "Process IDs:"
echo "Config    : $CONFIG_PID"
echo "Discovery : $DISCOVERY_PID"
echo "Customer  : $CUSTOMER_PID"
echo "Inventory : $INVENTORY_PID"
echo "Gateway   : $GATEWAY_PID"
echo "Billing   : $BILLING_PID"
echo ""
echo "Pour arrêter tous les services:"
echo "kill $CONFIG_PID $DISCOVERY_PID $CUSTOMER_PID $INVENTORY_PID $GATEWAY_PID $BILLING_PID"
echo ""

wait

