@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Clean.%sp%.%v_strdt%.log

echo "" > %LOG_FILE%

start /B Launch.Tail.bat %LOG_FILE%

cd %sp%

echo starting to Clean...

echo Clean all...

call Clean.workspace.wrapper.bat %v_strdt%

call Clean.workspace.whmi.bat %v_strdt%

call Clean.workspace.webapp.bat %v_strdt%
 
echo End of Clean

REM PAUSE
