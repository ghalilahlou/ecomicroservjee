@echo off
echo Starting E-commerce Microservices...
echo.

echo [1/6] Starting Config Service...
start "Config Service" cmd /k "cd config-service && mvn spring-boot:run"
timeout /t 20 /nobreak

echo [2/6] Starting Discovery Service...
start "Discovery Service" cmd /k "cd discovery-service && mvn spring-boot:run"
timeout /t 20 /nobreak

echo [3/6] Starting Gateway Service...
start "Gateway Service" cmd /k "cd gateway-service && mvn spring-boot:run"
timeout /t 15 /nobreak

echo [4/6] Starting Customer Service...
start "Customer Service" cmd /k "cd customer-service && mvn spring-boot:run"

echo [5/6] Starting Inventory Service...
start "Inventory Service" cmd /k "cd inventory-service && mvn spring-boot:run"

echo [6/6] Starting Billing Service...
start "Billing Service" cmd /k "cd billing-service && mvn spring-boot:run"

echo.
echo All services are starting...
echo.
echo Access points:
echo - Eureka Dashboard: http://localhost:8761
echo - Gateway: http://localhost:9999
echo - Config Server: http://localhost:8888
echo.
pause

