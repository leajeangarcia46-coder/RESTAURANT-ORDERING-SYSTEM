@echo off
setlocal
title FoodEase Restaurant Ordering System
cd /d "%~dp0"

if not exist "build\classes" mkdir "build\classes"
type nul > "build\sources.txt"
for /r "src" %%F in (*.java) do echo "%%F" >> "build\sources.txt"

echo.
echo ============================================================
echo                 FOODEASE RESTAURANT
echo                  ORDERING SYSTEM
echo ============================================================
echo.
echo Compiling project...
javac -encoding UTF-8 -d "build\classes" @"build\sources.txt"
if errorlevel 1 (
    echo.
    echo Build failed. Check that Java JDK 21 is installed and on PATH.
    pause
    exit /b 1
)

echo Starting FoodEase...
echo.
java -cp "build\classes" com.foodease.Main
echo.
echo FoodEase has closed.
pause
endlocal
