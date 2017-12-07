@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET SOURCE_BASE_F10=%SOURCE_BASE%/f10
SET NODEJS_HOME=%SOFTS_BASE%/%SOFTS_DIR%/node-v6.11.0-win-x64
SET PATH=%PATH%;%NODEJS_HOME%

set AUTOLOGOUT=autologout
set ANGULAR=angular
set SPRING_BOOT=spring-boot
set NODE_MODULES=node_modules
set NODE_MODULES_7Z=%NODE_MODULES%.7z

SET LOG_FILE=%LOG_HOME%\build.f10.%AUTOLOGOUT%.%v_strdt%.log

echo "" > %LOG_FILE%

IF EXIST %SOURCE_BASE_F10%/%AUTOLOGOUT%/%ANGULAR%/%NODE_MODULES% goto :build_dist

ECHO Zip and copy node_modules libraries from cots...

%PATH_7Z_BIN% a %NODE_MODULES_7Z% %REPO_BASE%/%NODE_MODULES%/%AUTOLOGOUT%/*

ECHO Extract node_modules to %AUTOLOGOUT% build folder

%PATH_7Z_BIN% x %NODE_MODULES_7Z% -o%SOURCE_BASE_F10%/%AUTOLOGOUT%/%ANGULAR%/%NODE_MODULES%


:build_dist
CD /d %SOURCE_BASE_F10%/%AUTOLOGOUT%/%ANGULAR%

ECHO Starting build %AUTOLOGOUT% angular process and log to %LOG_FILE%...

CALL ./build_release.cmd >> %LOG_FILE%

IF NOT EXIST ./dist goto :cleanup

REM %PATH_7Z_BIN% a scadagen-f10-%AUTOLOGOUT%.zip ./dist/*

CD ../%SPRING_BOOT%

echo Cleaning %AUTOLOGOUT% spring-boot before build...
call mvn clean -V >> %LOG_FILE%

echo Building %AUTOLOGOUT% spring-boot
call mvn clean install >> %LOG_FILE%

CD %TOOLS_BASE%

:cleanup
DEL /s /f /q %NODE_MODULES_7Z%

ECHO End of build %AUTOLOGOUT%

REM PAUSE