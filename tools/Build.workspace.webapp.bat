@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp%.%v_strdt%.log

echo & echo. >> %SOURCE_BASE%/%sp_webapp%\mywebapp\src\main\resources\com\thalesgroup\scadasoft\gwebhmi\main\ScsMain.gwt.xml

echo "" > %LOG_FILE%

start /B Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_webapp%

echo Starting build %sp_webapp% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning webapp before build...
call mvn clean -V > %LOG_FILE%

echo Building webapp...
REM call mvn clean war:exploded install >> %LOG_FILE%
call mvn clean install >> %LOG_FILE%

cd ..
 
echo End of build %sp_webapp%

REM PAUSE