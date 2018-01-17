@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

CALL %TOOLS_PATH%\Log.f10.Backup.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO starting to build...

ECHO Build all f10 release packages...

CALL %TOOLS_PATH%\Build.f10.autologout.bat %v_strdt%

CALL %TOOLS_PATH%\Build.f10.opm.bat %v_strdt%

CALL %TOOLS_PATH%\Build.f10.time-schedule.bat %v_strdt%

CALL %TOOLS_PATH%\Build.f10.tms.bat %v_strdt%

CALL %TOOLS_PATH%\Build.f10.point-select.bat %v_strdt%

ECHO End of building f10 packages.
