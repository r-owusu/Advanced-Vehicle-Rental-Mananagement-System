@echo off
echo.
echo ========================================
echo  Advanced Vehicle Rental Web Server
echo ========================================
echo.

:: Set paths
set PROJECT_ROOT=%~dp0
set WEBAPP_DIR=%PROJECT_ROOT%src\main\webapp
set LIB_DIR=%PROJECT_ROOT%lib

:: Create lib directory if it doesn't exist
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

echo Starting web server...
echo Web application directory: %WEBAPP_DIR%
echo.
echo Please ensure you have:
echo 1. Java installed and in PATH
echo 2. Required JAR files in lib\ directory:
echo    - servlet-api.jar
echo    - jetty-server.jar
echo    - jetty-servlet.jar
echo    - jetty-webapp.jar
echo.
echo Access the application at: http://localhost:8080
echo.

:: Note: This is a simplified launcher
:: For full functionality, you would need to compile with proper dependencies
echo To run the web version, you need to:
echo 1. Install Maven: https://maven.apache.org/download.cgi
echo 2. Run: mvn clean compile
echo 3. Run: mvn exec:java -Dexec.mainClass="com.rowusu.vehiclerental.web.WebServer"
echo.
echo Alternative: Use the GUI version with run-gui.bat
pause