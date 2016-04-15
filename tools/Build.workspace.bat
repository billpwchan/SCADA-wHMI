@echo off

call LoadPath.bat

call %CUR_PATH%/Log.Clear.Backup.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

start /B Launch.Tail.bat %LOG_FILE%

cd %sp%

echo starting to build...

echo Build all...

call Build.workspace.wrapper.bat %strdt%

call Build.workspace.whmi.bat %strdt%

call Build.workspace.webapp.bat %strdt%
 
echo End of build

call %CUR_PATH%/Log.Clear.Backup.bat

REM PAUSE
