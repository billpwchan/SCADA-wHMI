@ECHO OFF

SET CUR_PATH=%~dp0
SET SCRIPT_PATH=%CUR_PATH:~0,-1%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO Remove repo dir...
CD %SCRIPT_PATH%
CALL remove_repo_dir_scadagen.bat

ECHO Remove release dir...
CD %SCRIPT_PATH%
CALL remove_release_dir_scadagen.bat

ECHO Cloning from git...
CD %SCRIPT_PATH%
CALL git_clone_whmi.bat

ECHO Backup preview log...
CALL %SCRIPT_PATH%\..\whmi\tools\Log.Backup.bat

ECHO Building workspace...
CALL %SCRIPT_PATH%\..\whmi\tools\Build.workspace.bat %v_strdt%
