@echo off

call LoadPath.bat

call %TOOLS_BASE%/Log.Clear.Backup.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

start /B Launch.Tail.bat %LOG_FILE%

cd %sp%

echo starting to build...

echo Build all...

call %TOOLS_BASE%/Build.workspace.wrapper.bat %strdt%

call %TOOLS_BASE%/Build.workspace.whmi.bat %strdt%

call %TOOLS_BASE%/Build.workspace.webapp-fun.bat %strdt%

call %TOOLS_BASE%/Build.workspace.webapp.bat %strdt%
 
echo End of build

call %TOOLS_BASE%/Log.Clear.Backup.bat

REM PAUSE
