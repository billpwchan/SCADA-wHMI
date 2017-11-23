@echo off
set /p ORACLE_SID=Please enter the Oracle SID:
set /p ORACLE_PW=Please enter the Oracle password for user system:
set /p SCHEMA_NAME=Please enter the name of Configurator: 
set /p CDV_NAME=Please enter the CDV name that you want: 
(
REM Get the table name of CDV
echo.column TABLE_NAME new_value QUERY_TABLE_NAME
echo.SELECT TABLE_NAME 
echo.FROM %SCHEMA_NAME%.RESOURCE_DIRECTORY_TAB
echo.WHERE NAME='%CDV_NAME%';
echo.
REM Get the table name of CDV_library
echo.column TABLE_NAME new_value QUERY_TABLE_LI_NAME
echo.SELECT TABLE_NAME 
echo.FROM %SCHEMA_NAME%.RESOURCE_DIRECTORY_TAB
echo.WHERE NAME='%CDV_NAME%_LI';
echo.
REM Merge the library
echo.INSERT INTO %SCHEMA_NAME%.^&^&QUERY_TABLE_LI_NAME
echo.SELECT * FROM %SCHEMA_NAME%.RES5
echo.WHERE ELEMENT_ID NOT IN ^(
echo.	SELECT ELEMENT_ID FROM %SCHEMA_NAME%.^&^&QUERY_TABLE_LI_NAME^);
echo.

echo.EXIT;
) > %~dp0%~n0.sql

sqlplus system/%ORACLE_PW%@%ORACLE_SID% < %~dp0%~n0.sql

pause