@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start OF EXPORT F11

SET _folder="%DEPOT_HOME%\%v_strdt%"

SET _folderlogs=%_folder%\logs

ECHO _folder=%_folder% 
ECHO _folderlogs=%_folderlogs%

IF NOT EXIST %_folder% MD %_folder%
IF NOT EXIST %_folder%\logs MD %_folder%\logs

SET _F11_OPMMGT="%SOURCE_BASE_HOME%\f11\opmmgt\target"
SET _F11_SRC_OPMMGT_ZIP=opmmgt*.zip
SET _F11_OPMMGT_ZIP=scadagen-f11-opmmgt.zip
ECHO _F11_OPMMGT=%_F11_OPMMGT% _F11_SRC_OPMMGT_ZIP=%_F11_SRC_OPMMGT_ZIP% _F11_OPMMGT_ZIP=%_F11_OPMMGT_ZIP%

REM Export Logs

COPY "%TOOLS_BASE%\build.f11*.log" %_folder%\logs

COPY %_F11_OPMMGT%\%_F11_SRC_OPMMGT_ZIP% %_folder%\
MOVE %_folder%\%_F11_SRC_OPMMGT_ZIP% %_folder%\%_F11_OPMMGT_ZIP%

ECHO END OF EXPORT F11