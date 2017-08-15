@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of Export GIT COMMIT LOG

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _folderlogs=%_folder%\logs
ECHO _folderlogs=%_folderlogs%
MD %_folderlogs%

REM Export Logs

COPY "%TOOLS_BASE%\Export.git.commit.%v_strdt%.log" %_folder%\logs

ECHO END OF Export GIT COMMIT LOG
