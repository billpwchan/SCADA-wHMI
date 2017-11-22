@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start OF EXPORT F10

SET _folder="%DEPOT_HOME%\%v_strdt%"

SET _folderlogs=%_folder%\logs

ECHO _folder=%_folder% 
ECHO _folderlogs=%_folderlogs%

IF NOT EXIST %_folder% MD %_folder%
IF NOT EXIST %_folder%\logs MD %_folder%\logs

SET _F10_AUTOLOGOUT="%SOURCE_BASE_HOME%\f10\autologout"
SET _F10_OPM="%SOURCE_BASE_HOME%\f10\opm"
SET _F10_TIMESCHEDULE="%SOURCE_BASE_HOME%\f10\timeSchedule"

ECHO _F10_AUTOLOGOUT=%_F10_AUTOLOGOUT%
ECHO _F10_OPM=%_F10_OPM%
ECHO _F10_TIMESCHEDULE=%_F10_TIMESCHEDULE%

SET _F10_AUTOLOGOUT_ZIP=scadagen-f10-autologout.zip
SET _F10_OPM_ZIP=scadagen-f10-opm.zip
SET _F10_TIMESCHEDULE_ZIP=scadagen-f10-timeSchedule.zip

COPY %_F10_AUTOLOGOUT%\%_F10_AUTOLOGOUT_ZIP% %_folder%\%_F10_AUTOLOGOUT_ZIP%
COPY %_F10_OPM%\%_F10_OPM_ZIP% %_folder%\%_F10_OPM_ZIP%
COPY %_F10_TIMESCHEDULE%\%_F10_TIMESCHEDULE_ZIP% %_folder%\%_F10_TIMESCHEDULE_ZIP%

REM Export Logs

COPY "%TOOLS_BASE%\build.f10*.log" %_folder%\logs

ECHO END OF EXPORT F10
