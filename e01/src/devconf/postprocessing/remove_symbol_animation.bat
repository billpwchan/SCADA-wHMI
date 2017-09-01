@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will remove the animationRule specified
@REM This script will scan all %SITUATIONVIEW_DIR%\symbols\*._animation.xml.
@REM ###########################################################################

@REM ###########################################################################
IF [%1] == [] (
	ECHO Usage: %~n0.bat ^<Animation Rule Name^>
	GOTO :END
)

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started

SET ANIMATIONRULE_NAME=%1

@REM ###########################################################################
FOR /F %%F IN ('DIR /B "%SITUATIONVIEW_DIR%\symbols\*_animation.xml"') DO (
	SET ANIMATION_NAME=%%F
	SET ANIMATION_PATH=%SITUATIONVIEW_DIR%\symbols\!ANIMATION_NAME!
	@REM ECHO !ANIMATION_PATH!
	
	"%PYTHON_BIN%" "%~dp0\remove_symbol_animation.py" -i "!ANIMATION_PATH!" -n %ANIMATIONRULE_NAME%
	IF NOT %ERRORLEVEL% == 0 (
		CALL :LOG Failed to remove animation rule: %ANIMATIONRULE_NAME% from !ANIMATION_PATH!
		DEL "%TEMPFILE%"
	) ELSE (
		CALL :LOG Removed %ANIMATIONRULE_NAME% from !ANIMATION_PATH!
	)
)
CALL :LOG Completed
CALL :PAUSE_IF_NOT_INTERACTIVE
@ENDLOCAL

:END
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
