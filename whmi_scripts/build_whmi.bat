REM @ECHO OFF

SET CUR_PATH=%~dp0
SET SCRIPT_PATH=%CUR_PATH:~0,-1%

CD %SCRIPT_PATH%
CALL remove_repo_dir_scadagen.bat

CD %SCRIPT_PATH%
CALL remove_release_dir_scadagen.bat

CD %SCRIPT_PATH%
CALL git_clone_whmi.bat

CALL %SCRIPT_PATH%\..\whmi\tools\Log.Backup.bat

CALL %SCRIPT_PATH%\..\whmi\tools\Build.workspace.bat