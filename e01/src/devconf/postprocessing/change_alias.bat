@ECHO OFF
@SETLOCAL
SET SITUATIONVIEW_DIR=D:\git\eal\hmi
PowerShell -ExecutionPolicy bypass -NoLogo -File "%~dp0\replace_strings.ps1" -mapping "%~dp0\replace_strings_changeAlias.csv"
@ENDLOCAL