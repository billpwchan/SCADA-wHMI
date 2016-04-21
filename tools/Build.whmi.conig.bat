@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp%.%v_strdt%.log

SET SOURCE_BASE=%TOOLS_BASE%\..\config.extracted
SET DESTINATION_BASE=%TOOLS_BASE%\..\config.merged

RMDIR /S /Q %DESTINATION_BASE%

IF NOT EXIST "%DESTINATION_BASE%" MKDIR %DESTINATION_BASE%

REM SET CONFIG_BASE=%scstraning_loc%\scspaths\WEBAPP\apache-tomcat%TOMCAT_VER%

XCOPY /Y /S /I %SOURCE_BASE%\e01\* %DESTINATION_BASE%
XCOPY /Y /S /I %SOURCE_BASE%\e02\* %DESTINATION_BASE%
XCOPY /Y /S /I %SOURCE_BASE%\p01\* %DESTINATION_BASE%
