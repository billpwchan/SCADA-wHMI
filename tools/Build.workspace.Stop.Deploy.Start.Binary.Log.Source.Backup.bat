@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "strdt=%strdt%"

CALL "%TOOLS_PATH%\LoadFolder.bat"

REM CD %TOOLS_PATH%
REM CALL %TOOLS_PATH%\Log.Clear.Backup
CD %TOOLS_PATH%
CALL %TOOLS_PATH%\Build.workspace.Stop.Deploy.Start.bat %v_strdt%
CD %TOOLS_PATH%
CALL %TOOLS_PATH%\Binary.Log.Source.Backup.bat %v_strdt%
