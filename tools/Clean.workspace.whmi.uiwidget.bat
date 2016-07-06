@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Clean.%sp_uiwidget%.%v_strdt%.log

echo "" > %LOG_FILE%

start /B %TOOLS_BASE%/Launch.Tail.bat %LOG_FILE%

cd /d %SOURCE_BASE%/%sp_uiwidget%

echo Starting Clean %sp_uiwidget% process and log to %LOG_FILE%...

call mvn clean -V > %LOG_FILE%

cd ..
 
echo End of Clean %sp_uiwidget%

REM PAUSE