@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

SET _folder="%DEPOT_HOME%\%v_strdt%"

SET _PACKAGE_SCADAGEN="com\thalesgroup\scadagen"

SET _REPO_SCADAGEN="%M2_REPO%\%_PACKAGE_SCADAGEN%"

ECHO _REPO_SCADAGEN

SET _REPO_DEPO="%_folder%\repo"

SET _REPO_DEPO_SCADAGEN="%_REPO_DEPO%\%_PACKAGE_SCADAGEN%"

ECHO %_REPO_DEPO_SCADAGEN%

MKDIR %_REPO_DEPO_SCADAGEN%

XCOPY %_REPO_SCADAGEN% %_REPO_DEPO_SCADAGEN% /s/h/e/k/f/c





