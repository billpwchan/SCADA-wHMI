@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

SET NOT_INTERACTIVE=1

SET PYTHON_BIN=C:\softs\Python27\python.exe

SET SITUATIONVIEW_DIR=C:\SCSTraining\devconf\genhmi\widgets\situationView

@REM Symbol Blinking
@REM Remove old blinking mechanism (i.e., +100, and use postfix _Blink)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\replace_strings.ps1" "%~dp0\replace_strings_removeSymbolBlink.csv"
@REM Added current blinking mechanism (i.e., use needack)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_symbol_blinking.ps1" -whitelist "%~dp0\modify_mwt_symbol_blinking_whitelist.csv" -blacklist "%~dp0/modify_mwt_symbol_blinking_point_level.csv"
@REM Add current per-point blinking mechansim (i.e., +100)
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_symbol_blinking_point_level.ps1" -whitelist "%~dp0\modify_mwt_symbol_blinking_point_level.csv"

@REM Symbol Highlight
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_symbol_highlight.ps1" -mapping "%~dp0\modify_mwt_symbol_highlight.csv"

@REM Symbol Point Management Presentation
CALL "%~dp0\insert_mwt_symbol_mo.bat"

@REM Schematics Zoom levels
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\modify_mwt_view_resolution.ps1" -whitelist "%~dp0\modify_mwt_view_resolution.csv"

@REM Remove unnecessary animations
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\remove_xml_node.ps1" -mapping "%~dp0\remove_xml_node.csv"

@REM Replace strings
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\replace_strings.ps1" -mapping "%~dp0\replace_strings_statusComputer.csv"
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\replace_strings.ps1" -mapping "%~dp0\replace_strings_patch.csv"

@REM Remove XML comments for easier diff-ing
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\remove_xml_comment.ps1" -whitelist "%~dp0\remove_xml_comment.csv"

@REM Replace manually created files
CALL "%~dp0\replace_files.bat"

@REM Transform rotated / flipped symbol in situation view
PowerShell -NoLogo -ExecutionPolicy bypass -File "%~dp0\transform_symbol_by_view.ps1" -whitelist "%~dp0\transform_symbol_by_view.csv"

@ENDLOCAL