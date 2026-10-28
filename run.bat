@echo off
title FashionHub E-Commerce Store
cd /d "%~dp0"

echo ===================================================
echo   Starting FashionHub Application
echo   Directory: %CD%
echo ===================================================
echo.

if exist mvnw.cmd (
    call mvnw.cmd spring-boot:run
) else (
    call mvn spring-boot:run
)

pause
