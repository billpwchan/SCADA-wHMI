@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

SET _folder="%DEPOT_HOME%\%v_strdt%"

SET _folderlogs=%_folder%\logs

ECHO _folder=%_folder% 
ECHO _folderlogs=%_folderlogs%

IF NOT EXIST %_folder% MD %_folder%

IF NOT EXIST %_folder%\logs MD %_folder%\logs

SET _F03_SCADAGEN_BA=%SOURCE_BASE_HOME%\f03\scadagen-ba
SET _F03_SCADAGEN_BA_EXPORT_PATH=%_folder%\scadagen-f03-ba.zip

ECHO COPY %_F03_SCADAGEN_BA%\target\scadagen-ba*.zip %_folder%

COPY %_F03_SCADAGEN_BA%\target\scadagen-ba*.zip %_folder%

MOVE %_folder%\scadagen-ba*.zip %_F03_SCADAGEN_BA_EXPORT_PATH%

REM Export Logs

COPY %TOOLS_BASE%\Build.connector*.log %_folder%\logs

ECHO END OF EXPORT F03
