# Script PowerShell pour démarrer tous les microservices
Write-Host "========================================" -ForegroundColor Green
Write-Host " DEMARRAGE DES MICROSERVICES E-COMMERCE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

$projectRoot = $PSScriptRoot

# Fonction pour démarrer un service
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [int]$Port,
        [int]$WaitSeconds
    )
    
    Write-Host "Demarrage de $ServiceName (Port $Port)..." -ForegroundColor Cyan
    $fullPath = Join-Path $projectRoot $ServicePath
    
    Start-Process powershell -ArgumentList @(
        "-NoExit",
        "-Command",
        "cd '$fullPath'; Write-Host 'Demarrage de $ServiceName...' -ForegroundColor Yellow; mvn spring-boot:run"
    ) -WindowStyle Normal
    
    Write-Host "Attente de $WaitSeconds secondes..." -ForegroundColor Yellow
    Start-Sleep -Seconds $WaitSeconds
    Write-Host "$ServiceName : OK" -ForegroundColor Green
    Write-Host ""
}

# Démarrer les services dans le bon ordre
Start-Service -ServiceName "Config Service" -ServicePath "config-service" -Port 8888 -WaitSeconds 25
Start-Service -ServiceName "Discovery Service" -ServicePath "discovery-service" -Port 8761 -WaitSeconds 30
Start-Service -ServiceName "Customer Service" -ServicePath "customer-service" -Port 8081 -WaitSeconds 20
Start-Service -ServiceName "Inventory Service" -ServicePath "inventory-service" -Port 8082 -WaitSeconds 20
Start-Service -ServiceName "Gateway Service" -ServicePath "gateway-service" -Port 9999 -WaitSeconds 20
Start-Service -ServiceName "Billing Service" -ServicePath "billing-service" -Port 8083 -WaitSeconds 10

Write-Host "========================================" -ForegroundColor Green
Write-Host " TOUS LES SERVICES SONT DEMARRES !" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Points d'acces:" -ForegroundColor Cyan
Write-Host "- Eureka Dashboard : http://localhost:8761"
Write-Host "- Gateway API      : http://localhost:9999"
Write-Host "- Config Server    : http://localhost:8888"
Write-Host "- Customer Service : http://localhost:8081"
Write-Host "- Inventory Service: http://localhost:8082"
Write-Host "- Billing Service  : http://localhost:8083"
Write-Host ""
Write-Host "Verifiez sur http://localhost:8761 que tous les services sont UP." -ForegroundColor Yellow
Write-Host ""
Write-Host "Tests rapides:" -ForegroundColor Cyan
Write-Host "curl http://localhost:9999/customer-service/customers"
Write-Host "curl http://localhost:9999/inventory-service/products"
Write-Host "curl http://localhost:9999/billing-service/fullBill/1"
Write-Host ""
pause

