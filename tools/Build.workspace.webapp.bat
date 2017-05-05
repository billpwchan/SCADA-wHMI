@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_name%.%v_strdt%.log

REM echo & echo. >> %SOURCE_BASE_F01%/%sp_webapp%\mywebapp\src\main\resources\com\thalesgroup\scadasoft\gwebhmi\main\ScsMain.gwt.xml

REM echo & echo. >> %SOURCE_BASE_F01%/%sp_webapp%\mywebapp\src\main\resources\com\thalesgroup\scadasoft\gwebhmi\security\ScsLogin.gwt.xml

echo "" > %LOG_FILE%

REM start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE_F01%/%sp_webapp%

echo Starting build %sp_webapp_name% process and log to %LOG_FILE%...

cd mywebapp

echo Cleaning %sp_webapp_name% before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_webapp_name%...
REM call mvn clean war:exploded install >> %LOG_FILE%
call mvn clean gwt:clean install >> %LOG_FILE%

cd %TOOLS_PATH%
 
echo End of build %sp_webapp_name%

REM PAUSE