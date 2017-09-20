@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of WEBAPP

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _E02="D:\Build.SCADAgen\whmi\webapp"
ECHO _E02=%_E02%

REM Export WEBAPP

%SEVEN_ZIP_HOME% a %_folder%\webapp %_E02%

ECHO END OF WEBAPP