#!/bin/bash

echo "Starting E-commerce Microservices..."
echo ""

echo "[1/6] Starting Config Service..."
cd config-service
mvn spring-boot:run &
CONFIG_PID=$!
cd ..
sleep 20

echo "[2/6] Starting Discovery Service..."
cd discovery-service
mvn spring-boot:run &
DISCOVERY_PID=$!
cd ..
sleep 20

echo "[3/6] Starting Gateway Service..."
cd gateway-service
mvn spring-boot:run &
GATEWAY_PID=$!
cd ..
sleep 15

echo "[4/6] Starting Customer Service..."
cd customer-service
mvn spring-boot:run &
CUSTOMER_PID=$!
cd ..

echo "[5/6] Starting Inventory Service..."
cd inventory-service
mvn spring-boot:run &
INVENTORY_PID=$!
cd ..

echo "[6/6] Starting Billing Service..."
cd billing-service
mvn spring-boot:run &
BILLING_PID=$!
cd ..

echo ""
echo "All services are starting..."
echo ""
echo "Access points:"
echo "- Eureka Dashboard: http://localhost:8761"
echo "- Gateway: http://localhost:9999"
echo "- Config Server: http://localhost:8888"
echo ""
echo "Process IDs:"
echo "Config: $CONFIG_PID"
echo "Discovery: $DISCOVERY_PID"
echo "Gateway: $GATEWAY_PID"
echo "Customer: $CUSTOMER_PID"
echo "Inventory: $INVENTORY_PID"
echo "Billing: $BILLING_PID"
echo ""
echo "To stop all services, run: kill $CONFIG_PID $DISCOVERY_PID $GATEWAY_PID $CUSTOMER_PID $INVENTORY_PID $BILLING_PID"

wait

