@echo off
cd /d "%~dp0"
if not exist build\classes mkdir build\classes
javac -encoding UTF-8 -source 8 -target 8 -d build\classes src\*.java
if errorlevel 1 exit /b 1
java -cp build\classes Main
