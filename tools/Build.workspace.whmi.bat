@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_whmi%.%v_strdt%.log

SET softpackage=whmi

echo "" > %LOG_FILE%

start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_whmi%

echo Starting build %softpackage% process and log to %LOG_FILE%...

echo Cleaning all module(s) before build
call mvn clean -V > %LOG_FILE%

echo Building common module(s)
call mvn war:exploded install >> %LOG_FILE%

cd ..

cd tools
 
echo End of build %sp_whmi%

REM PAUSE