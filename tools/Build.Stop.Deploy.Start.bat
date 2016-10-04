@echo off

call LoadFolder.bat

start /B %CUR_PATH%\WEB1_Shutdown.bat

call %CUR_PATH%\Build.workspace.bat

ping 127.0.0.1 -n 1 > nul

call %CUR_PATH%\Deploy.jar.bat

REM ping 127.0.0.1 -n 35 > nul

call %CUR_PATH%\Deploy.war.bat

ping 127.0.0.1 -n 1 > nul

start /B %CUR_PATH%\WEB1_Launch.bat

ping 127.0.0.1 -n 5 > nul

start /B %CUR_PATH%\Launch.Chrome.bat

REM PAUSE