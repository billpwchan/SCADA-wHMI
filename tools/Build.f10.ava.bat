@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET SOURCE_BASE_F10=%SOURCE_BASE%/f10
SET NODEJS_HOME=%SOFTS_BASE%/%SOFTS_DIR%/node-v8.1.3-win-x64
SET PATH=%PATH%;%NODEJS_HOME%

set COMPONENT=ava
set ANGULAR=angular
set SPRING_BOOT=spring-boot
set NODE_MODULES=node_modules
set NODE_MODULES_7Z=%COMPONENT%.7z

SET LOG_FILE=%LOG_HOME%\build.f10.%COMPONENT%.%v_strdt%.log

echo "" > %LOG_FILE%

IF EXIST %SOURCE_BASE_F10%/%COMPONENT%/%ANGULAR%/%NODE_MODULES% goto :build_dist

ECHO Extract node_modules to %COMPONENT% build folder

%PATH_7Z_BIN% x %REPO_BASE%/%NODE_MODULES%/%NODE_MODULES_7Z% -o%SOURCE_BASE_F10%/%COMPONENT%/%ANGULAR%/%NODE_MODULES%

:build_dist
CD /d %SOURCE_BASE_F10%/%COMPONENT%/%ANGULAR%

ECHO Starting build %COMPONENT% angular process and log to %LOG_FILE%...

CALL ./build_release.cmd >> %LOG_FILE%

IF NOT EXIST ./dist goto :cleanup

REM %PATH_7Z_BIN% a scadagen-f10-%COMPONENT%.zip ./dist/*

CD ../%SPRING_BOOT%

echo Cleaning %COMPONENT% spring-boot before build...
call mvn clean -V >> %LOG_FILE%

echo Building %COMPONENT% spring-boot
call mvn clean install >> %LOG_FILE%

CD %TOOLS_BASE%

:cleanup

ECHO End of build %COMPONENT%

REM PAUSE