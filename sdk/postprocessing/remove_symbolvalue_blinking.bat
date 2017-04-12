@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will modify the animationRule that make use of symbolvalue for
@REM blinking effect.
@REM This script will scan all %SITUATIONVIEW_DIR%\symbols\*._animation.xml.
@REM This script require the use of PowerShell
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started

CALL :GET_TEMPFILE
@REM ECHO %TEMPFILE%

@REM ###########################################################################
FOR /F %%F IN ('DIR /B "%SITUATIONVIEW_DIR%\symbols\*_animation.xml"') DO (
	SET ANIMATION_NAME=%%F
	SET ANIMATION_PATH=%SITUATIONVIEW_DIR%\symbols\!ANIMATION_NAME!
	@REM ECHO !ANIMATION_PATH!
	
	SET REMOVE_BLINK=1
	IF EXIST "%SYM_BLINK_BLACKLIST_LOGIC%" (
		CALL "%SYM_BLINK_BLACKLIST_LOGIC%" !ANIMATION_NAME!
		SET REMOVE_BLINK=!ERRORLEVEL!
	)
@REM	ECHO !ANIMATION_NAME!: !REMOVE_BLINK!

	IF "!REMOVE_BLINK!"=="1" (
		PowerShell -Command "get-content \"!ANIMATION_PATH!\" | %%{$_ -replace \"_Blink\"\"\",\"\"\"\"} | Out-File \"%TEMPFILE%\" -Encoding ASCII"
		MOVE /Y "%TEMPFILE%" "!ANIMATION_PATH!" >NUL 2>&1
		IF NOT %ERRORLEVEL% == 0 (
			CALL :LOG Failed to update: !ANIMATION_PATH!
			DEL "%TEMPFILE%"
		) ELSE (
			CALL :LOG Updated: !ANIMATION_PATH!
		)
	) ELSE (
		CALL :LOG Skipped: !ANIMATION_PATH!
	)
)
CALL :LOG Completed
CALL :PAUSE_IF_NOT_INTERACTIVE
@ENDLOCAL

EXIT /B 0

:GET_TEMPFILE
	SET TEMPFILE=%~n0_%RANDOM%.tmp
	IF EXIST "%TEMPFILE%" GOTO :GET_TEMPFILE
	@EXIT /B 0

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
