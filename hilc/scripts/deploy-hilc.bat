@echo off

set INSTALL_PATH=%~dp0
set INSTALL_PATH=%INSTALL_PATH:~0,-17%

REM Binaries installation
mkdir %INSTALL_PATH%\softs\QATARsoft
xcopy /E /R /Y QATARsoft %INSTALL_PATH%\softs\QATARsoft

REM Data installation
for %%i in (SRV2 SRV3) do (
	if exist %INSTALL_PATH%\runtime\%%i (
		xcopy /E /R /Y dat %INSTALL_PATH%\runtime\%%i\dat
		copy %INSTALL_PATH%\runtime\%%i\ScsEnvTable %INSTALL_PATH%\runtime\%%i\ScsEnvTable.no_hilc
		copy %INSTALL_PATH%\runtime\%%i\ScsEnvTable.hilc %INSTALL_PATH%\runtime\%%i\ScsEnvTable
	)
)
