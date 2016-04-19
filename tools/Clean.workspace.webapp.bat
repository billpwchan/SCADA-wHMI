@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Clean.%sp_webapp%.%v_strdt%.log

echo "" > %LOG_FILE%

start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_webapp%

echo Starting Clean %sp_webapp% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning webapp before Clean...
call mvn clean -V > %LOG_FILE%

cd ..
 
echo End of Clean %sp_webapp%

REM PAUSE