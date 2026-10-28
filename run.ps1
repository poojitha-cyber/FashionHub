# PowerShell Launcher for FashionHub
Set-Location -Path $PSScriptRoot

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting FashionHub Application" -ForegroundColor Green
Write-Host "  Directory: $(Get-Location)" -ForegroundColor Gray
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host ""

if (Test-Path ".\mvnw.cmd") {
    .\mvnw.cmd spring-boot:run
} else {
    mvn spring-boot:run
}
