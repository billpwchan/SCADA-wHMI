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

CALL Script.bat -i CfgInterfaceMain.py
