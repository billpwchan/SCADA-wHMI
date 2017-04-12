@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
IF [%1] == [] GOTO :USAGE
IF [%2] == [] GOTO :USAGE

@REM ###########################################################################
CALL "%~dp0\sg_mwt_util_env.bat"

@REM ###########################################################################
SET SYMBOL_NAME=%1
SET SYMBOL_POSTFIX=%2
SET SVG_NAME=%SYMBOL_NAME%

@REM ###########################################################################
IF EXIST "%SYM_TO_SVG_POINT_MGMT_MAP_SPECIFIC_PATTERN%" (
	@REM Get the actual name of the specific handler
	FOR %%F IN ("%SYM_TO_SVG_POINT_MGMT_MAP_SPECIFIC_PATTERN%") DO (
		SET SPECIFIC_HANDLER=%%F
	)
	@REM Call the specific handler
	FOR /F %%F IN ('CALL "!SPECIFIC_HANDLER!" %SYMBOL_NAME% %SYMBOL_POSTFIX%') DO (
		SET SVG_NAME=%%F
	)
) ELSE (
	@REM Remove the "_symbol.xml" postfix
	CALL :STRLEN _symbol.xml
	SET LEN=!ERRORLEVEL!
	CALL SET SVG_NAME=%%SVG_NAME:~0,-!LEN!%%

	@REM Append MWT SVG postfix
	SET SVG_NAME=!SVG_NAME!%SYMBOL_POSTFIX%.svg
)

IF EXIST "%SITUATIONVIEW_DIR%\bricks\!SVG_NAME!" (
	ECHO !SVG_NAME!
	GOTO :END
)
GOTO :ERROR

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
:USAGE:
	ECHO Usage: %~n0.bat ^<Symbol File Name^> ^<Postfix of the SVG File^>
	GOTO :END
:ERROR
	ECHO #ERROR#
	GOTO :END
:END
@ENDLOCAL