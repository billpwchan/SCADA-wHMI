@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.connector.scadagen-ba.%v_strdt%.log

echo "" > %LOG_FILE%

cd /d %SOURCE_BASE_F03%/scadagen-ba

echo Starting build scadagen-ba process and log to %LOG_FILE%...

echo Cleaning before build...
call mvn clean -V > %LOG_FILE%

echo Building scadagen-ba...
call mvn clean install >> %LOG_FILE%

cd %TOOLS_PATH%
 
echo End of build scadagen-ba.