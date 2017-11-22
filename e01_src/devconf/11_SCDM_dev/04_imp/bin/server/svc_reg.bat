@echo off
call %~dp0svc_env.bat %*

echo Install Service %SVCNAME%

>nul: 2>&1 %~dp0runservice -remove %SVCNAME%
%~dp0runservice -install %SVCNAME% "cmd /c start /B /WAIT /d %~dp0 startServer.bat" "cmd /c start /B /WAIT /d %~dp0 killServer.bat" "" "%~dp0runservice.log"
sc failure %SVCNAME% reset= 0 actions= restart/5000 

pause
