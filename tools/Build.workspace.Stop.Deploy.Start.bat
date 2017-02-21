@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

CALL "%TOOLS_PATH%\LoadFolder.bat"

START /B %TOOLS_PATH%\WEB1_Shutdown.bat

CALL %TOOLS_PATH%\Build.workspace.bat %v_strdt%

PING 127.0.0.1 -n 1 > nul

CALL %TOOLS_PATH%\Deploy.jar.bat

REM PING 127.0.0.1 -n 35 > nul

CALL %TOOLS_PATH%\Deploy.war.bat

PING 127.0.0.1 -n 1 > nul

START /B %TOOLS_PATH%\WEB1_Launch.bat

PING 127.0.0.1 -n 5 > nul

START /B %TOOLS_PATH%\Launch.Chrome.bat

REM PAUSE