@ECHO off

CALL LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp_name%.%v_strdt%.log

SET SOURCE_BASE_e01=%TOOLS_BASE%\..\..\hmi\e01
SET SOURCE_BASE_e02=%TOOLS_BASE%\..\..\hmi\e02
SET SOURCE_BASE_p01=%TOOLS_BASE%\..\p01

SET DESTINATION_BASE=%TOOLS_BASE%\..\..\conf.merged

IF NOT EXIST "%DESTINATION_BASE%" MKDIR %DESTINATION_BASE%

XCOPY /Y /S /I %SOURCE_BASE_e01%\* %DESTINATION_BASE%
XCOPY /Y /S /I %SOURCE_BASE_e02%\* %DESTINATION_BASE%
XCOPY /Y /S /I %SOURCE_BASE_p01%\* %DESTINATION_BASE%
