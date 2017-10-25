@echo off
REM ##############################################################
REM
REM Script_CfgInterfaceMain.bat
REM
REM batch script to launch "CfgInterfaceMain.py" python file
REM
REM USAGE : Script_CfgInterfaceMain.bat
REM
REM launch the CfgInterface.py core that implement the functions
REM and wrap the java API of the configurator client.
REM
REM
REM ##############################################################

CD /d %~dp0

call setConfig.bat

REM ==============
REM set log pipe
REM ==============
set LOGFILE=PY_%~n0_%USERNAME%

set SCSHOME=%SCSHOME:/=\%
if NOT DEFINED BINDIR (
	for /f %%x in ('dir /b /ad %SCSHOME%\*msvc* 2^>nul:') do set BINDIR=%SCSHOME%\%%x
)
set PATH=%BINDIR%\bin;%PATH%
set SCSPATH=%~dp0..
scslimitlog -d -o -n 500000 -l 5 -p %LOGFILE%
set SCSPATH=
REM ==============
REM set log pipe end
REM ==============

CALL Script.bat -i   %~n0.py 1>\\.\pipe\%LOGFILE% 2>&1

pause



