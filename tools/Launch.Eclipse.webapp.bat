REM @echo off

CALL LoadPath.bat

start %ECLIPSE_HOME%\eclipse.exe -data %SOURCE_BASE%/%sp_webapp%

ECHO END