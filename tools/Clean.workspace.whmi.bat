@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Clean.%sp_whmi%.%v_strdt%.log

SET softpackage=whmi

echo "" > %LOG_FILE%

start /B Launch.Tail.bat %LOG_FILE%

cd %sp_whmi%

echo Starting Clean %softpackage% process and log to %LOG_FILE%...

echo Cleaning all module(s) before Clean
call mvn clean -V > %LOG_FILE%

cd ..
 
echo End of Clean %sp_whmi%

REM PAUSE