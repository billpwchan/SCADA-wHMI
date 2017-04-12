@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will modify the highlight rectangle size of MWT symbols
@REM This script will scan all %SITUATIONVIEW_DIR%\symbols\*._symbol.xml,
@REM then obtains their specified highlight size through the use of the script
@REM defined in %SYM_TO_SVG_HIGHLIGHT_MAP_LOGIC%
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started
@REM ###########################################################################
FOR /F %%F IN ('DIR /B "%SITUATIONVIEW_DIR%\symbols\*_symbol.xml"') DO (
	SET SYMBOL_NAME=%%F
	SET SYMBOL_PATH=%SITUATIONVIEW_DIR%\symbols\!SYMBOL_NAME!
	@REM ECHO !SYMBOL_PATH!
	
	@REM Get SVG Path
	SET SVG_WIDTH=#ERROR#
	SET SVG_HEIGHT=#ERROR#
	FOR /F "tokens=1,2 delims=," %%A IN ('CALL "%SYM_TO_SVG_HIGHLIGHT_MAP_LOGIC%" !SYMBOL_NAME!') DO (
		SET SVG_WIDTH=%%A
		SET SVG_HEIGHT=%%B
	)
	@REM ECHO !SVG_WIDTH!,!SVG_HEIGHT!
	
	IF NOT "!SVG_WIDTH!"=="#ERROR#" (
		IF NOT "!SVG_HEIGHT!"=="#ERROR#" (
			@REM Modify the symbol highlight
			CALL "%PYTHON_BIN%" "%~dp0\modify_mwt_symbol_highlight.py" -i "!SYMBOL_PATH!" -W !SVG_WIDTH! -H !SVG_HEIGHT!
			IF %ERRORLEVEL% NEQ 0 (
				CALL :LOG Failed to modify: !SYMBOL_PATH! ^(W: !SVG_WIDTH!, H: !SVG_HEIGHT!^)
			) ELSE (
				CALL :LOG Modified !SYMBOL_PATH!: ^(W: !SVG_WIDTH!, H: !SVG_HEIGHT!^)
			)
		) ELSE (
			CALL :LOG !SYMBOL_PATH! Not Updated.
		)
	) ELSE (
		CALL :LOG !SYMBOL_PATH! Not Updated.
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
