@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_integration%.%v_strdt%.log

echo "" > %LOG_FILE%

REM start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_integration%

echo Starting build %sp_integration% process and log to %LOG_FILE%...

echo Cleaning before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_integration%...
call mvn clean gwt:clean install >> %LOG_FILE%

cd ..

cd tools
 
echo End of build %sp_integration%

REM PAUSE