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

SET _F10_AUTOLOGOUT="%SOURCE_BASE_HOME%\f10\autologout\spring-boot\target"
SET _F10_SRC_AUTOLOGOUT_ZIP=scadagen-f10-autologout*.zip
SET _F10_AUTOLOGOUT_ZIP=scadagen-f10-autologout.zip
ECHO _F10_AUTOLOGOUT=%_F10_AUTOLOGOUT% _F10_SRC_AUTOLOGOUT_ZIP=%_F10_SRC_AUTOLOGOUT_ZIP% _F10_AUTOLOGOUT_ZIP=%_F10_AUTOLOGOUT_ZIP%

SET _F10_OPM="%SOURCE_BASE_HOME%\f10\opm\spring-boot\target"
SET _F10_SRC_OPM_ZIP=scadagen-f10-opm*.zip
SET _F10_OPM_ZIP=scadagen-f10-opm.zip
ECHO _F10_OPM=%_F10_OPM% _F10_SRC_OPM_ZIP=%_F10_SRC_OPM_ZIP% _F10_OPM_ZIP=%_F10_OPM_ZIP%

SET _F10_TIMESCHEDULE="%SOURCE_BASE_HOME%\f10\timeSchedule\spring-boot\target"
SET _F10_SRC_TIMESCHEDULE_ZIP=scadagen-f10-timeSchedule*.zip
SET _F10_TIMESCHEDULE_ZIP=scadagen-f10-timeSchedule.zip
ECHO _F10_TIMESCHEDULE=%_F10_TIMESCHEDULE% _F10_SRC_TIMESCHEDULE_ZIP=%_F10_SRC_TIMESCHEDULE_ZIP% _F10_TIMESCHEDULE_ZIP=%_F10_TIMESCHEDULE_ZIP%

SET _F10_POINTSELECT="%SOURCE_BASE_HOME%\f10\pointSelect\spring-boot\target"
SET _F10_SRC_POINTSELECT_TGZ=point-select-*.tgz
SET _F10_POINTSELECT_TGZ=point-select.tgz
ECHO _F10_POINTSELECT=%_F10_POINTSELECT% _F10_SRC_POINTSELECT_TGZ=%_F10_SRC_POINTSELECT_TGZ% _F10_POINTSELECT_TGZ=%_F10_POINTSELECT_TGZ%

SET _F10_TMS="%SOURCE_BASE_HOME%\f10\tms\spring-boot\target"
SET _F10_SRC_TMS_ZIP=scadagen-f10-tms*.zip
SET _F10_TMS_ZIP=scadagen-f10-tms.zip
ECHO _F10_TMS=%_F10_TMS% _F10_SRC_TMS_ZIP=%_F10_SRC_TMS_ZIP% _F10_TMS_ZIP=%_F10_TMS_ZIP%

SET _F10_AVA="%SOURCE_BASE_HOME%\f10\ava\spring-boot\target"
SET _F10_SRC_AVA_ZIP=scadagen-f10-ava*.zip
SET _F10_AVA_ZIP=scadagen-f10-ava.zip
ECHO _F10_AVA=%_F10_AVA% _F10_SRC_AVA_ZIP=%_F10_SRC_AVA_ZIP% _F10_AVA_ZIP=%_F10_AVA_ZIP%

REM Export Logs

COPY "%TOOLS_BASE%\build.f10*.log" %_folder%\logs

COPY %_F10_AUTOLOGOUT%\%_F10_SRC_AUTOLOGOUT_ZIP% %_folder%\
MOVE %_folder%\%_F10_SRC_AUTOLOGOUT_ZIP% %_folder%\%_F10_AUTOLOGOUT_ZIP%

COPY %_F10_OPM%\%_F10_SRC_OPM_ZIP% %_folder%\
MOVE %_folder%\%_F10_SRC_OPM_ZIP% %_folder%\%_F10_OPM_ZIP%

COPY %_F10_TIMESCHEDULE%\%_F10_SRC_TIMESCHEDULE_ZIP% %_folder%\
MOVE %_folder%\%_F10_SRC_TIMESCHEDULE_ZIP% %_folder%\%_F10_TIMESCHEDULE_ZIP%

COPY %_F10_POINTSELECT%\%_F10_SRC_POINTSELECT_TGZ% %_folder%\
MOVE %_folder%\%_F10_SRC_POINTSELECT_TGZ% %_folder%\%_F10_POINTSELECT_TGZ%

COPY %_F10_TMS%\%_F10_SRC_TMS_ZIP% %_folder%\
MOVE %_folder%\%_F10_SRC_TMS_ZIP% %_folder%\%_F10_TMS_ZIP%

REM COPY %_F10_AVA%\%_F10_SRC_AVA_ZIP% %_folder%\
REM MOVE %_folder%\%_F10_SRC_AVA_ZIP% %_folder%\%_F10_AVA_ZIP%

ECHO END OF EXPORT F10