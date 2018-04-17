@echo off

cd /D %~dp0
set WEBAPP=AVAWEBAPP
echo Stopping %WEBAPP%

IF EXIST %WEBAPP%.pid goto killtask
goto exit

:killtask
set /p pid=<%WEBAPP%.pid
echo "taskkill /PID %pid%"
taskkill /F /T /PID %pid%

del /Q %WEBAPP%.pid

:exit