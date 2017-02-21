@ECHO OFF

CALL LoadPath.bat

start %ECLIPSE_HOME%\eclipse.exe -data %SOURCE_BASE_F01%/%sp_webapp%

ECHO END