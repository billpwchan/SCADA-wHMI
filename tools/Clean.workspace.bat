@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

cd /d %SOURCE_BASE%

echo starting to Clean...

echo Clean all...

call %TOOLS_BASE%/Clean.workspace.integration.bat %v_strdt%

call %TOOLS_BASE%/Clean.workspace.wrapper.bat %v_strdt%

call %TOOLS_BASE%/Clean.workspace.whmi.bat %v_strdt%

call %TOOLS_BASE%/Clean.workspace.webapp-func.bat %v_strdt%

call %TOOLS_BASE%/Clean.workspace.webapp.bat %v_strdt%
 
echo End of Clean

REM PAUSE
