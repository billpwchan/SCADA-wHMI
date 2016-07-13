@echo off
:: ------------------------------------------------
:: This script is the master script to configure CDV 
:: to be backup.
::
:: Usage -  
::
:: ------------------------------------------------


::defines CDV name to be saved
set arg1=%~1
set arg2=%~2
set arg3=%~3
set cdvnamerev=%~n1

set HOME=%~dp0
set log=%HOME%DbSave.log

setlocal enabledelayedexpansion

set S=%cdvnamerev%
set I=0
set L=-1
:l
if "!S:~%I%,1!" == "" goto ld
if "!S:~%I%,1!" == "_" set L=%I%
set /a I+=1
goto l
:ld

set cdvname=!cdvnamerev:~0,%L%!


::calling backup script to save active cdv defined above
cd %HOME%
call backupCDV.bat %cdvname% %* >> %log%
if %errorlevel% == 0 echo.backupCDV done.

cd /d %HOEM%..\..\..\..\..\backup\cdv\
for /f %%i in ('dir /od /ad /b %cdvname%_*') do (
	set backupFilename=%%i
)


zip -r -q -u %backupFilename%.zip %backupFilename%
rmdir /Q /S %backupFilename%
echo %date% %time% %backupFilename%.zip created!
