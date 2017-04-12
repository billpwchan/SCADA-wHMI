@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will insert the animation rule for blinking upon equipment is
@REM in alarm state.
@REM This script will scan all %SITUATIONVIEW_DIR%\symbols\*_animation.xml
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started

CALL :GET_TEMPFILE
@REM ECHO %TEMPFILE%

@REM ###########################################################################
SET SCRIPT_DIR=%~dp0
FOR /F %%F IN ('DIR /B "%SITUATIONVIEW_DIR%\symbols\*_animation.xml"') DO (
	SET ANIMATION_NAME=%%F
	SET ANIMATION_PATH=%SITUATIONVIEW_DIR%\symbols\!ANIMATION_NAME!
@REM	ECHO !ANIMATION_PATH!

	SET ADD_BLINK=1
	IF EXIST "%SYM_BLINK_BLACKLIST_LOGIC%" (
		CALL "%SYM_BLINK_BLACKLIST_LOGIC%" !ANIMATION_NAME!
		SET ADD_BLINK=!ERRORLEVEL!
	)
@REM	ECHO !ANIMATION_NAME!: !ADD_BLINK!

	IF "!ADD_BLINK!"=="1" (
		CALL :GEN_ANIMATION %TEMPFILE%
		CALL "%PYTHON_BIN%" "%~dp0\insert_xml_node.py" -i "!ANIMATION_PATH!" -n animationRules -t %TEMPFILE%
		IF %ERRORLEVEL% NEQ 0 (
			CALL :LOG Failed to insert xml node into !ANIMATION_PATH!
		) ELSE (
			CALL :LOG Inserted xml node into !ANIMATION_PATH!
		)
	) ELSE (
			CALL :LOG Skipped !ANIMATION_PATH!
	)
)
DEL %TEMPFILE%
CALL :LOG Completed
CALL :PAUSE_IF_NOT_INTERACTIVE
@ENDLOCAL

EXIT /B 0

:GET_TEMPFILE
	SET TEMPFILE=%~n0_%RANDOM%.tmp
	IF EXIST "%TEMPFILE%" GOTO :GET_TEMPFILE
	@EXIT /B 0

:GEN_ANIMATION
	SETLOCAL
	SET TEMPFILE=%1
	ECHO ^<animationRule^> > %TEMPFILE%
	ECHO     ^<source xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns2:statusDataSourceType" name="needack"^>^</source^> >> %TEMPFILE%
	ECHO     ^<sourceType^>INT^</sourceType^> >> %TEMPFILE%
	ECHO     ^<blinkAnimation defaultValue="false"^> >> %TEMPFILE%
	ECHO         ^<rangeBinding^> >> %TEMPFILE%
	ECHO             ^<range from="1" to="2147483647" value="true"^>^</range^> >> %TEMPFILE%
	ECHO         ^</rangeBinding^> >> %TEMPFILE%
	ECHO     ^</blinkAnimation^> >> %TEMPFILE%
	ECHO ^</animationRule^> >> %TEMPFILE%
	ENDLOCAL
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
