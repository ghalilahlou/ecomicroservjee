@echo off
echo ========================================
echo  Demarrage des Microservices E-commerce
echo ========================================
echo.
echo IMPORTANT: Attendez que chaque service soit completement demarre avant de passer au suivant!
echo.

echo [1/6] Demarrage de Config Service (Port 8888)...
start "Config Service - 8888" cmd /k "cd config-service && mvn spring-boot:run"
echo Attendre 25 secondes pour Config Service...
timeout /t 25 /nobreak
echo.

echo [2/6] Demarrage de Discovery Service (Port 8761)...
start "Discovery Service - 8761" cmd /k "cd discovery-service && mvn spring-boot:run"
echo Attendre 30 secondes pour Discovery Service (Eureka)...
timeout /t 30 /nobreak
echo.

echo [3/6] Demarrage de Customer Service (Port 8081)...
start "Customer Service - 8081" cmd /k "cd customer-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Customer Service...
timeout /t 20 /nobreak
echo.

echo [4/6] Demarrage de Inventory Service (Port 8082)...
start "Inventory Service - 8082" cmd /k "cd inventory-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Inventory Service...
timeout /t 20 /nobreak
echo.

echo [5/6] Demarrage de Gateway Service (Port 9999)...
start "Gateway Service - 9999" cmd /k "cd gateway-service && mvn spring-boot:run"
echo Attendre 20 secondes pour Gateway Service...
timeout /t 20 /nobreak
echo.

echo [6/6] Demarrage de Billing Service (Port 8083)...
start "Billing Service - 8083" cmd /k "cd billing-service && mvn spring-boot:run"
echo.

echo ========================================
echo  Tous les services sont en cours de demarrage!
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
echo Attendez 1-2 minutes que tous les services s'enregistrent dans Eureka.
echo Verifiez sur http://localhost:8761 que tous les services sont UP.
echo.
pause

