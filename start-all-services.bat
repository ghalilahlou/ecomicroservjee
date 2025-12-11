@echo off
cls
echo ========================================
echo  DEMARRAGE DES MICROSERVICES E-COMMERCE
echo ========================================
echo.
echo IMPORTANT: Attendez que chaque service soit completement demarre !
echo.

echo [1/6] Demarrage de Config Service (Port 8888)...
start "Config-Service-8888" cmd /k "cd /d %~dp0config-service && mvn spring-boot:run"
echo Attendre 25 secondes pour Config Service...
timeout /t 25 /nobreak >nul
echo Config Service : OK
echo.

echo [2/6] Demarrage de Discovery Service (Port 8761)...
start "Discovery-Service-8761" cmd /k "cd /d %~dp0discovery-service && mvn spring-boot:run"
echo Attendre 30 secondes pour Discovery Service (Eureka)...
timeout /t 30 /nobreak >nul
echo Discovery Service : OK
echo.

echo [3/6] Demarrage de Customer Service (Port 8081)...
start "Customer-Service-8081" cmd /k "cd /d %~dp0customer-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Customer Service...
timeout /t 20 /nobreak >nul
echo Customer Service : OK
echo.

echo [4/6] Demarrage de Inventory Service (Port 8082)...
start "Inventory-Service-8082" cmd /k "cd /d %~dp0inventory-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Inventory Service...
timeout /t 20 /nobreak >nul
echo Inventory Service : OK
echo.

echo [5/6] Demarrage de Gateway Service (Port 9999)...
start "Gateway-Service-9999" cmd /k "cd /d %~dp0gateway-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Gateway Service...
timeout /t 20 /nobreak >nul
echo Gateway Service : OK
echo.

echo [6/6] Demarrage de Billing Service (Port 8083)...
start "Billing-Service-8083" cmd /k "cd /d %~dp0billing-service && mvn spring-boot:run"
echo.

echo ========================================
echo  TOUS LES SERVICES SONT EN COURS DE DEMARRAGE !
echo ========================================
echo.
echo Points d'acces:
echo - Eureka Dashboard : http://localhost:8761
echo - Gateway API      : http://localhost:9999
echo - Config Server    : http://localhost:8888
echo - Customer Service : http://localhost:8081
echo - Inventory Service: http://localhost:8082
echo - Billing Service  : http://localhost:8083
echo.
echo Verifiez sur http://localhost:8761 que tous les services sont UP.
echo.
echo Pour tester:
echo curl http://localhost:9999/customer-service/customers
echo curl http://localhost:9999/inventory-service/products
echo curl http://localhost:9999/billing-service/fullBill/1
echo.
pause

