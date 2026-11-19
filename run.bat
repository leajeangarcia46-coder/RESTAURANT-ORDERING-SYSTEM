@echo off
setlocal
title FoodEase Restaurant Ordering System
cd /d "%~dp0"
if not exist "build\classes" mkdir "build\classes"

javac -encoding UTF-8 -sourcepath "src" -d "build\classes" "src\com\foodease\Main.java"
if errorlevel 1 (
    echo.
    echo Build failed. Check that Java JDK 21 is installed and on PATH.
    pause
    exit /b 1
)

java -cp "build\classes" com.foodease.Main
echo.
echo FoodEase has closed.
pause
endlocal
