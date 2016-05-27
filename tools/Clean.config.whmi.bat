@ECHO off

CALL LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_name%.%v_strdt%.log

SET DESTINATION_BASE=%TOOLS_BASE%\..\..\conf.merged

RMDIR /S /Q %DESTINATION_BASE%

IF NOT EXIST "%DESTINATION_BASE%" MKDIR %DESTINATION_BASE%

REM PAUSE
