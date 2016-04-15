@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_wrapper%.%v_strdt%.log

echo "" > %LOG_FILE%

start /B Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_wrapper%

echo Starting build %sp_wrapper% process and log to %LOG_FILE%...

echo Cleaning before build...
call mvn clean -V > %LOG_FILE%

echo Building wrapper...
call mvn clean install >> %LOG_FILE%

cd ..
 
echo End of build %sp_wrapper%

REM PAUSE