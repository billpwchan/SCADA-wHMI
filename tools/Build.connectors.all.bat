@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

CALL %TOOLS_PATH%\Log.Backup.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO starting to build...

ECHO Build all connectors...

CALL %TOOLS_PATH%\Build.connector.fasapi.bat %v_strdt%

CALL %TOOLS_PATH%\Build.connector.scadagen-ba-comp.bat %v_strdt%

CALL %TOOLS_PATH%\Build.connector.scadagen-bps.bat %v_strdt%

CALL %TOOLS_PATH%\Build.connector.scadagen-ba.bat %v_strdt%

ECHO End of building connectors.

