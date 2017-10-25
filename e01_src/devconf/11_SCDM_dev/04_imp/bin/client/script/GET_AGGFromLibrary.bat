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

SET /p CDV="Please specify the CDV library you want to get: "
SET /p SYS="Please specify the System you want to get from library: "
SET /p WDS="Please specify the name of WDS you want to store the Aggregate: "
set Parm= %CDV% %SYS% %WDS%

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


Call Script.bat -i %%~n0.py% %%Parm% 1>\\.\pipe\%LOGFILE% 2>&1

pause



