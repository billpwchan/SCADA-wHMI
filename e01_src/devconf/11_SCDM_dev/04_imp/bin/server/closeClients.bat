@echo off
:start
call listConnectedClient.bat
cd /d %~dp0
set /p user=Which user to disconnect?
echo Disconnect user %user%?
pause
call deconnectClient %user%
@echo off
set /p continue=Continue to disconnect users(y/n)?
if %continue%==y goto start
pause
