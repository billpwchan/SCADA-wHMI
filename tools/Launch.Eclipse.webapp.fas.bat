REM @echo off

CALL LoadPath.bat
load
start %ECLIPSE_HOME%\eclipse.exe -data %SOURCE_BASE%/%sp_fas%

ECHO END