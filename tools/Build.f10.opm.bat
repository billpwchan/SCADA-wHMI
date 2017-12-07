@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET SOURCE_BASE_F10=%SOURCE_BASE%/f10
SET NODEJS_HOME=%SOFTS_BASE%/%SOFTS_DIR%/node-v6.11.0-win-x64
SET PATH=%PATH%;%NODEJS_HOME%

set OPM=opm
set ANGULAR=angular
set SPRING_BOOT=spring-boot
set NODE_MODULES=node_modules
set NODE_MODULES_7Z=%NODE_MODULES%.7z

SET LOG_FILE=%LOG_HOME%\build.f10.%OPM%.%v_strdt%.log

echo "" > %LOG_FILE%

IF EXIST %SOURCE_BASE_F10%/%OPM%/%ANGULAR%/%NODE_MODULES% goto :build_dist

ECHO Zip and copy node_modules libraries from cots...

%PATH_7Z_BIN% a %NODE_MODULES_7Z% %REPO_BASE%/%NODE_MODULES%/%OPM%/*

ECHO Extract node_modules to %OPM% build folder

%PATH_7Z_BIN% x %NODE_MODULES_7Z% -o%SOURCE_BASE_F10%/%OPM%/%ANGULAR%/%NODE_MODULES%


:build_dist
CD /d %SOURCE_BASE_F10%/%OPM%/%ANGULAR%

ECHO Starting build %OPM% angular process and log to %LOG_FILE%...

CALL ./build_release.cmd >> %LOG_FILE%

IF NOT EXIST ./dist goto :cleanup

REM %PATH_7Z_BIN% a scadagen-f10-%OPM%.zip ./dist/*

CD ../%SPRING_BOOT%

echo Cleaning %OPM% spring-boot before build...
call mvn clean -V >> %LOG_FILE%

echo Building %OPM% spring-boot
call mvn clean install >> %LOG_FILE%

CD %TOOLS_BASE%

:cleanup
DEL /s /f /q %NODE_MODULES_7Z%

ECHO End of build %OPM%

REM PAUSE