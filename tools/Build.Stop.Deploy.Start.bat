@echo off

call LoadFolder.bat

call %CUR_PATH%\Build.workspace.bat

ping 127.0.0.1 -n 5 > nul

start /B %CUR_PATH%\WEB1_Shutdown.bat

ping 127.0.0.1 -n 25 > nul

start /B %CUR_PATH%\Deploy.war.bat

ping 127.0.0.1 -n 5 > nul

start /B %CUR_PATH%\WEB1_Launch.bat

ping 127.0.0.1 -n 25 > nul

start /B %CUR_PATH%\Launch.Chrome.bat

REM PAUSE