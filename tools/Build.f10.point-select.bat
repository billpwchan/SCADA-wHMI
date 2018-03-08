@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET SOURCE_BASE_F10=%SOURCE_BASE%/f10
SET NODEJS_HOME=%SOFTS_BASE%/%SOFTS_DIR%/node-v8.1.3-win-x64
SET PATH=%PATH%;%NODEJS_HOME%

set COMPONENT=pointSelect
set NODE_MODULES=node_modules
set NODE_MODULES_7Z=%COMPONENT%.7z

SET LOG_FILE=%LOG_HOME%\build.f10.%COMPONENT%.%v_strdt%.log

echo "" > %LOG_FILE%

IF EXIST %SOURCE_BASE_F10%/%COMPONENT%/%NODE_MODULES% goto :build_dist

ECHO Extract node_modules to %COMPONENT% build folder

%PATH_7Z_BIN% x %REPO_BASE%/%NODE_MODULES%/%NODE_MODULES_7Z% -o%SOURCE_BASE_F10%/%COMPONENT%/%NODE_MODULES%

:build_dist
CD /d %SOURCE_BASE_F10%/%COMPONENT%

ECHO Starting build %COMPONENT% angular process and log to %LOG_FILE%...

CALL ./build_release.cmd >> %LOG_FILE%
REM CALL ./build_dev.cmd >> %LOG_FILE%

IF NOT EXIST ./dist goto :cleanup

REM %PATH_7Z_BIN% a scadagen-f10-%COMPONENT%.zip ./dist/*

REM CD ../%SPRING_BOOT%

REM echo Cleaning %COMPONENT% spring-boot before build...
REM call mvn clean -V >> %LOG_FILE%

REM echo Building %COMPONENT% spring-boot
REM call mvn clean install >> %LOG_FILE%

CD %TOOLS_BASE%

:cleanup

ECHO End of build %COMPONENT%

REM PAUSE