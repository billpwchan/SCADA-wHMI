@ECHO OFF

CALL LoadPath.bat

start %ECLIPSE_HOME%\eclipse.exe -data %SOURCE_BASE%/%sp_wrapper%

ECHO END