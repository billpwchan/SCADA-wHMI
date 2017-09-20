@ECHO OFF

CALL LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO Start of build e03

SET LOG_FILE=%LOG_HOME%\build.e03.%v_strdt%.log

CD /d %SOURCE_BASE%/e03
CD 02_MA_generated/scadagen-data-model

ECHO Starting build e03 process and log to %LOG_FILE%...

ECHO Cleaning e03 before build
CALL mvn clean -V > %LOG_FILE%

ECHO Building e03
CALL mvn install >> %LOG_FILE%

cd %TOOLS_PATH%
 
ECHO End of build e03

REM PAUSE