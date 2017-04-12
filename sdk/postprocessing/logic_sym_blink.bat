@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
IF [%1] == [] (
	ECHO Usage: %~n0.bat ^<Symbol Animation File Name^>
	GOTO :END
)

@REM ###########################################################################
CALL "%~dp0\sg_mwt_util_env.bat"

@REM ###########################################################################
SET ANIMATION_NAME=%1
@REM ECHO %ANIMATION_NAME%

@REM ###########################################################################
@REM Blacklist items
SET BLACKLIST_LEN=3
SET BLACKLIST[1]=_ActiveText_
SET BLACKLIST[2]=_ActiveBackdrop_
SET BLACKLIST[3]=_ActiveNumber_

@REM ###########################################################################
FOR /L %%I IN (1, 1, %BLACKLIST_LEN%) DO (
	SET BLACKLIST_ITEM=!BLACKLIST[%%I]!
@REM	ECHO !BLACKLIST_ITEM!

	CALL :STRCMP !BLACKLIST_ITEM! %ANIMATION_NAME%
	SET MATCHED=!ERRORLEVEL!

	IF NOT "!MATCHED!"=="0" (
		@ENDLOCAL
		EXIT /B 0
	)
)
@ENDLOCAL
EXIT /B 1

@REM ###########################################################################
:STRCMP
	@SETLOCAL ENABLEDELAYEDEXPANSION
	@SET SEARCH_TEXT=%1
	@SET IN_TEXT=%2
	@CALL SET MATCHED=%%IN_TEXT:%SEARCH_TEXT%=%%
	@IF NOT x%MATCHED%==x%IN_TEXT% (
		@EXIT /B 1
	) ELSE (
		@EXIT /B 0
	)
	@ENDLOCAL

@REM ###########################################################################

:END
@ENDLOCAL