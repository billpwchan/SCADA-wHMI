@echo off

for /f "tokens=1,2,3 delims=@ eol=." %%a in ('echo %~n0') do ( 
  set CMDNAME=%%~na
  set CMDARGS=%%~nb %%~nc
  set CDV_NAME=%%~nb
)	
call %~dp0%CMDNAME% %CDV_NAME% %*

cd /d ..\..\backup\cdv\ 

for /f %%i in ('dir /od /ad /b %CMDARGS%*') do (
	set backupFilename=%%i
)	

for %%a in (%backupFilename%) do set filedate=%%~ta
if %filedate:~0,10%==%date% (
	if not exist %backupFilename%.7z (
		echo --------------------------------
		echo - %date% %time% - Start to compress file
		7z a -r %backupFilename%.7z %backupFilename%
		rem echo --------------------------------
		rem echo - %date% %time% - Copy to X:\CQ_Backup
		rem xcopy /c /y /z %backupFilename%.7z X:\\CQ_Backup
		rem rem del /Q /F %backupFilename%.7z
		rem RMDIR /S /Q %backupFilename%
		echo --------------------------------
		echo - %date% %time% - Backup Completed!
		echo --------------------------------
	) else (
		echo --------------------------------
		echo - %date% %time% - Backup file exist!
		echo --------------------------------
	)
) else (
	echo --------------------------------
	echo - %date% %time% - No updated backup!
	echo --------------------------------
)
