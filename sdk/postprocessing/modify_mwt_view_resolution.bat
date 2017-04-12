@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will modify the resolution of the MWT view xml according to the
@REM HMI display area size, the MWT view xml's maxExtent, and the desired zoom
@REM levels.
@REM ###########################################################################
@REM Note that if "displayPanZoomBar" attribute was not defined or set to "false"
@REM in MWT view xml, this script will automatically set to zoom level to 1x only
@REM ###########################################################################
@REM Note that 1x zoom in this script means the MWT view will fit into the HMI
@REM display area
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started
@REM ###########################################################################
SET SCRIPT_DIR=%~dp0
FOR %%F IN ("%SITUATIONVIEW_DIR%\views\*.xml") DO (
	CALL "%PYTHON_BIN%" "%SCRIPT_DIR%\modify_mwt_view_resolution.py" -i "%%F" -z %SCHEMATIC_ZOOM_LEVELS% -W %SCHEMATIC_DISPLAY_WIDTH_PX% -H %SCHEMATIC_DISPLAY_HEIGHT_PX%
	IF %ERRORLEVEL% NEQ 0 (
		CALL :LOG Failed to modify: %%F ^(%SCHEMATIC_ZOOM_LEVELS%^)
	) ELSE (
		CALL :LOG Modified %%F ^(%SCHEMATIC_ZOOM_LEVELS%^)
	)
)
CALL :LOG Completed
CALL :PAUSE_IF_NOT_INTERACTIVE
@ENDLOCAL

EXIT /B 0

:LOG
	@SETLOCAL
	@SET TAG=[%DATE:~10,4%-%DATE:~4,2%-%DATE:~7,2% %TIME:~0,2%:%TIME:~3,2%:%TIME:~6,2% %~n0]
	@CALL :TEE %TAG% %*
	@ENDLOCAL
	@EXIT /B 0

:TEE
	@SETLOCAL
	@SET LOGFILE=%~n0.log
	@ECHO %*
	@ECHO %* >> "%LOGFILE%"
	@ENDLOCAL
	@EXIT /B 0

:PAUSE_IF_NOT_INTERACTIVE
	@SETLOCAL
	@IF DEFINED NOT_INTERACTIVE @EXIT /B 0
	@ECHO %CMDCMDLINE% | FINDSTR /L %COMSPEC% >NUL 2>&1
	@IF NOT %ERRORLEVEL% == 0 PAUSE
	@ENDLOCAL
	@EXIT /B 0
