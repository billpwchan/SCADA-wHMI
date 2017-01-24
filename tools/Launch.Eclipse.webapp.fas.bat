@ECHO OFF

CALL LoadPath.bat
load
start %ECLIPSE_HOME%\eclipse.exe -data %SOURCE_BASE%/%sp_webapp_fas%

ECHO END