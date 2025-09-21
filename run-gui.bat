@echo off
echo =========================================================
echo     Advanced Vehicle Rental Management System - GUI
echo =========================================================
echo.
echo Starting the application...
echo.
cd /d "%~dp0"
java -cp target/classes com.rowusu.vehiclerental.gui.VehicleRentalGUI
echo.
echo Application closed.
pause