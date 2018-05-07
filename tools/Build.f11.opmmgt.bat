@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.f11.opmmgt.%v_strdt%.log

echo "" > %LOG_FILE%

cd /d %SOURCE_BASE%/f11/opmmgt

echo Starting build opmmgt process and log to %LOG_FILE%...

echo Cleaning before build...
call mvn clean -V > %LOG_FILE%

echo Building opmmgt...
call mvn clean install >> %LOG_FILE%

cd %TOOLS_PATH%
 
echo End of build opmmgt.