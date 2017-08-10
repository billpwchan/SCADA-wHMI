@ECHO OFF

call LoadPath.bat

SET SOURCE_BASE_F10=%SOURCE_BASE%/f10
SET NODEJS_HOME=%SOFTS_BASE%/%SOFTS_DIR%/node-v8.1.3-win-x64
SET PATH=%PATH%;%NODEJS_HOME%

set TIME_SCHEDULE=timeSchedule
set NODE_MODULES=node_modules
set NODE_MODULES_7Z=%NODE_MODULES%.7z

IF EXIST %SOURCE_BASE_F10%/%TIME_SCHEDULE%/%NODE_MODELS% goto :build_dist

ECHO Zip and copy node_modules libraries from cots...

%SEVEN_ZIP_HOME% a %NODE_MODULES_7Z% %REPO_BASE%/%NODE_MODULES%/%TIME_SCHEDULE%/*

ECHO Extract node_modules to timeSchedule build folder

%SEVEN_ZIP_HOME% x %NODE_MODULES_7Z% -o%SOURCE_BASE_F10%/%TIME_SCHEDULE%/%NODE_MODULES%


:build_dist
CD /d %SOURCE_BASE_F10%/%TIME_SCHEDULE%

ECHO Starting build %TIME_SCHEDULE% process...

CALL ./build.cmd

IF NOT EXIST ./dist goto :cleanup

%SEVEN_ZIP_HOME% a scadagen-f10-%TIME_SCHEDULE%.zip ./dist/*

:cleanup
DEL /s /f /q %NODE_MODULES_7Z%

CD %TOOLS_BASE%
 
ECHO End of build %TIME_SCHEDULE%

REM PAUSE