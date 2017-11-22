@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of e03

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _E03="D:\Build.SCADAgen\whmi\e03\02_MA_generated\scadagen-data-model\target\*.jar"
ECHO _E03=%_E03%

REM Export E03

CALL ARCHIVE.cmd :ZIP "%_folder%\e03" "%_E03%" "-xr!.gitignore -xr!.gitkeep"

ECHO END OF e03