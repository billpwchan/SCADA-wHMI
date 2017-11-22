@echo off
REM ##############################################################
REM
REM Script_generateAllCDV.bat
REM
REM Batch script SAMPLE to launch "generateAllCDV.py" python file
REM
REM USAGE generateAllCDV.py : generateAllCDV.py [cdvNameExcept]
REM
REM generate all CDV except cdvNameExcept
REM do not generate CDV already generated
REM
REM
REM ##############################################################

CD /d %~dp0

SET LOG=%~n0_%USERNAME%_%COMPUTERNAME%
 
CALL Script.bat generateAllCDV.py vdc_vide >..\log\%LOG%_stdout.log 2>..\log\%LOG%_stderr.log
