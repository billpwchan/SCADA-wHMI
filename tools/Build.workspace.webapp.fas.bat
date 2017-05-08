@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_fas_name%.%v_strdt%.log

echo "" > %LOG_FILE%

REM start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE_F01%/%sp_webapp_fas%

echo Starting build %sp_webapp_fas_name% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning %sp_webapp_fas_name% before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_webapp_fas_name%...
call mvn clean gwt:clean install >> %LOG_FILE%

cd ..\..

cd tools
 
echo End of build %sp_webapp_fas_name%

REM PAUSE