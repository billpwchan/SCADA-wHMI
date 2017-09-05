@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of E01

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _E01="D:\Build.SCADAgen\whmi\e01"
ECHO _E01=%_E01%

REM Export E01

%SEVEN_ZIP_HOME% a %_folder%\e01 %_E01%

ECHO END OF E01