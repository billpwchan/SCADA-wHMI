@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

"%PYTHON_BIN%" "%~dp0\logic_sym_to_svg_point_mgmt_scadagen.py" -s %1 -p %2

@ENDLOCAL