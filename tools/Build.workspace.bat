@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

CALL %TOOLS_PATH%\Log.Backup.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

REM start /B Launch.Tail.bat %LOG_FILE%

CD %sp%

ECHO starting to build...

ECHO Build all...

CALL %TOOLS_PATH%\Echo.LastCompilation.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.whmi.uiwidget.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.wrapper.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.whmi.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.fas.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.webapp-fun.bat %strdt%

CALL %TOOLS_PATH%\Build.workspace.webapp.bat %strdt%
 
ECHO End of build

REM CALL %TOOLS_BASE%/Log.Backup.bat

REM PAUSE
