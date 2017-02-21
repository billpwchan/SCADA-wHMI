@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_func_name%.%v_strdt%.log

echo & echo. >> %SOURCE_BASE_F01%/%sp_webapp_func%\mywebapp\src\main\resources\com\thalesgroup\scadasoft\gwebhmi\main\ScsMain.gwt.xml

echo & echo. >> %SOURCE_BASE_F01%/%sp_webapp_func%\mywebapp\src\main\resources\com\thalesgroup\scadasoft\gwebhmi\security\ScsLogin.gwt.xml

echo "" > %LOG_FILE%

REM start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE_F01%/%sp_webapp_func%

echo Starting build %sp_webapp_func_name% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning %sp_webapp_func% before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_webapp_func%
REM call mvn clean war:exploded install >> %LOG_FILE%
call mvn clean install >> %LOG_FILE%

cd ..

cd tools
 
echo End of build %sp_webapp_func_name%

REM PAUSE