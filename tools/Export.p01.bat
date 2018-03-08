@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of P01

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _P01="%SOURCE_BASE_HOME%\p01\*"
ECHO _P01=%_P01%

REM Export P01

CALL ARCHIVE.cmd :ZIP "%_folder%\p01" "%_P01%" "-xr!.gitignore -xr!.gitkeep"

ECHO END OF P01