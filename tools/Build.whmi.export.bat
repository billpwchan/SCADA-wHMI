@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

CALL "%TOOLS_PATH%\LoadFolder.bat"

CALL %TOOLS_PATH%\Build.workspace.bat %v_strdt%

CALL %TOOLS_PATH%\Export.whmi.binary.config.log.bat %v_strdt%

REM PAUSE