@echo off
set /p ORACLE_SID=Please enter oracle SID
set /p ORACLE_PW=Please enter oracle password for user system
set /p NEW_CFG_NAME=Please enter the name of New Configurator: 
set /p OLD_CFG_NAME=Please enter the name of Old Configurator: 
set /p CDV_NAME=Please enter the name of CDV: 
(
REM Merge the library
echo.INSERT INTO %NEW_CFG_NAME%.HISTORY_CURRENT_CHECK
echo.SELECT * FROM %OLD_CFG_NAME%.HISTORY_CURRENT_CHECK ;
echo.
echo.INSERT INTO %NEW_CFG_NAME%.HISTORY_CURRENT_EVENT
echo.SELECT * FROM %OLD_CFG_NAME%.HISTORY_CURRENT_EVENT;
echo.
echo.INSERT INTO %NEW_CFG_NAME%.RES1
echo.SELECT * FROM %OLD_CFG_NAME%.RES1
echo.WHERE ELEMENT_NAME ='IntegrationProperty' and PARENT_ID = ':R:%CDV_NAME%';
echo.
echo.COMMIT;
echo.EXIT;
) > %~dp0%~n0.sql

sqlplus system/%ORACLE_PW%@%ORACLE_SID% < %~dp0%~n0.sql

pause

