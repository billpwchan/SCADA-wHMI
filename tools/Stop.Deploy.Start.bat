@echo off

call LoadFolder.bat

start /B %CUR_PATH%\WEB1_Shutdown.bat

ping 127.0.0.1 -n 25 > nul

call /B %CUR_PATH%\Deploy.jar.bat

call /B %CUR_PATH%\Deploy.war.bat

start /B %CUR_PATH%\WEB1_Launch.bat

ping 127.0.0.1 -n 25 > nul

start /B %CUR_PATH%\Launch.Chrome.bat
