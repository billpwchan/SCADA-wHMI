@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of REF

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _REF="D:\Build.SCADAgen\whmi\ref"
ECHO _REF=%_REF%

REM Export REF

ECHO "%v_strdt% > "%_REF%\appli\version.txt"

CALL ARCHIVE.cmd :ZIP "%_folder%\ref_appli" "%_REF%\appli\*" "-xr!.gitignore -xr!.gitkeep"

ECHO "%v_strdt%" > "%_REF%\conn\version.txt"

CALL ARCHIVE.cmd :ZIP "%_folder%\ref_conn" "%_REF%\conn\*" "-xr!.gitignore -xr!.gitkeep"

ECHO "%v_strdt%" > "%_REF%\webapp\version.txt"

CALL ARCHIVE.cmd :ZIP "%_folder%\ref_webapp" "%_REF%\webapp\*" "-xr!.gitignore -xr!.gitkeep"

ECHO END OF REF