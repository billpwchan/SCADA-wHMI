@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
IF [%1] == [] (
	ECHO Usage: %~n0.bat ^<Symbol File Name^>
	GOTO :END
)

@REM ###########################################################################
CALL "%~dp0\sg_mwt_util_env.bat"

@REM ###########################################################################
SET SYMBOL_NAME=%1
SET SVG_NAME=%SYMBOL_NAME%

@REM ###########################################################################
IF EXIST "%SYM_TO_SVG_HIGHLIGHT_MAP_SPECIFIC_PATTERN%" (
	@REM Get the actual name of the specific handler
	FOR %%F IN ("%SYM_TO_SVG_HIGHLIGHT_MAP_SPECIFIC_PATTERN%") DO (
		SET SPECIFIC_HANDLER=%%F
	)
	@REM Call the specific handler
	FOR /F %%F IN ('CALL "!SPECIFIC_HANDLER!" %SYMBOL_NAME%') DO (
		SET SVG_RECT=%%F
	)
	ECHO !SVG_RECT!
	GOTO :END
) ELSE (
	@REM Remove the "_symbol.xml" postfix
	CALL :STRLEN _symbol.xml
	SET LEN=!ERRORLEVEL!
	CALL SET SVG_NAME=%%SVG_NAME:~0,-!LEN!%%

	@REM Append MWT SVG "_State_Selected.svg" postfix
	SET SVG_NAME=!SVG_NAME!_State_Selected.svg

	IF EXIST "%SITUATIONVIEW_DIR%\bricks\!SVG_NAME!" (
		FOR /F "tokens=1,2" %%A IN ('CALL "%PYTHON_BIN%" "%~dp0\get_mwt_svg_rect.py" -b -i "!SVG_PATH!"') DO (
			SET SVG_RECT_WIDTH=%%A
			SET SVG_RECT_HEIGHT=%%B
			ECHO !SVG_RECT_WIDTH!,!SVG_RECT_HEIGHT!
			GOTO :END
		)
	)
	GOTO :ERROR
)

@REM ###########################################################################
:STRLEN
	@SETLOCAL ENABLEDELAYEDEXPANSION
	@SET STR=%1
	@SET /A LEN=0
	:STRLEN_LOOP
		@IF NOT "!STR:~%LEN%!" == "" (
			@SET /A LEN+=1
			@GOTO :STRLEN_LOOP
		)
	@EXIT /B %LEN%
	@ENDLOCAL

@REM ###########################################################################
:ERROR
ECHO #ERROR#
:END
@ENDLOCAL