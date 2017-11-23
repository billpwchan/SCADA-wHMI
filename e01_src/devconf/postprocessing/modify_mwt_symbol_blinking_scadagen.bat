@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM Remove old blinking mechanism (i.e., +100, and use postfix _Blink)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\replace_strings.ps1" "%~dp0\replace_strings_removeSymbolBlink.csv"
@REM Added current blinking mechanism (i.e., use needack)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_symbol_blinking.ps1" -whitelist "%~dp0\modify_mwt_symbol_blinking_whitelist.csv" -blacklist "%~dp0/modify_mwt_symbol_blinking_point_level.csv"
@REM Add current per-point blinking mechansim (i.e., +100)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_symbol_blinking_point_level.ps1" -whitelist "%~dp0\modify_mwt_symbol_blinking_point_level.csv"

@ENDLOCAL
