@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_name%.631.%v_strdt%.log

echo "" > %LOG_FILE%

start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d R:\1166B\whmi\f02.fas\workspace.devweb.631

echo Starting build %sp_webapp_name% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning %sp_webapp_name% before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_webapp_name%...
REM call mvn clean war:exploded install >> %LOG_FILE%
call mvn clean gwt:clean install >> %LOG_FILE%

cd ..

cd tools
 
echo End of build %sp_webapp_name%

REM PAUSE