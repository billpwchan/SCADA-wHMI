@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will replace the files saved under "%~dp0\replace", if any,
@REM into the %SITUATIONVIEW_DIR%
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started

@REM ###########################################################################
IF NOT EXIST "%~dp0\replace\" (
	CALL :LOG "%~dp0\replace\" does not exist
	GOTO :END
)

XCOPY /E /V /Y /F "%~dp0\replace" "%SITUATIONVIEW_DIR%"

:END
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
