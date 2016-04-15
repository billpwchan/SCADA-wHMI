REM @echo off

CALL LoadPath.bat

start %ECLIPSE_HOME%\eclipse.exe -data %sp_whmi%

ECHO END