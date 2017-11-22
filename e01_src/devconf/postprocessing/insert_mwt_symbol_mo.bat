@ECHO OFF
@SETLOCAL ENABLEDELAYEDEXPANSION

@REM ###########################################################################
@REM This script will insert the manual override (MO) rectangle into MWT symbols
@REM This script will scan all %SITUATIONVIEW_DIR%\symbols\*._symbol.xml,
@REM then obtains their specified highlight size through the use of the script
@REM defined in %SYM_TO_SVG_POINT_MGMT_MAP_LOGIC%
@REM ###########################################################################

CALL "%~dp0\sg_mwt_util_env.bat"

CALL :LOG Started

CALL :GET_TEMPFILE
@REM ECHO %TEMPFILE%

@REM ###########################################################################
SET SCRIPT_DIR=%~dp0
FOR /F %%F IN ('DIR /B "%SITUATIONVIEW_DIR%\symbols\*_symbol.xml"') DO (
	SET SYMBOL_NAME=%%F
	SET SYMBOL_PATH=%SITUATIONVIEW_DIR%\symbols\!SYMBOL_NAME!
	@REM ECHO !SYMBOL_PATH!
	
	@REM Get SVG Path
	SET SVG_MO=#ERROR#
	SET SVG_EMPTY=#ERROR#
	FOR /F %%A IN ('CALL "%SYM_TO_SVG_POINT_MGMT_MAP_LOGIC%" !SYMBOL_NAME! _State_MO') DO (
		SET SVG_MO=%%A
	)
	FOR /F %%A IN ('CALL "%SYM_TO_SVG_POINT_MGMT_MAP_LOGIC%" !SYMBOL_NAME! _State_Empty') DO (
		SET SVG_EMPTY=%%A
	)
	@REM ECHO !SVG_MO!
	@REM ECHO !SVG_EMPTY!
	
	IF "!SVG_MO!" == "#ERROR#" (
		CALL :LOG Failed to find MO SVG for: !SYMBOL_NAME!
	) ELSE IF "!SVG_EMPTY!" == "#ERROR#" (
		CALL :LOG Failed to find Empty SVG for: !SYMBOL_NAME!
	) ELSE (
		SET ID=!SYMBOL_NAME:.xml=!_!SVG_EMPTY:.svg=!
		CALL :GEN_GRAPHIC !ID! !SVG_EMPTY:.svg=! %TEMPFILE%
		SET SYMBOL_GRAPHIC=!SYMBOL_PATH:_symbol=_graphic!
		CALL "%PYTHON_BIN%" "%~dp0\insert_xml_node.py" -i "!SYMBOL_GRAPHIC!" -n root -t %TEMPFILE%
		IF %ERRORLEVEL% NEQ 0 (
			CALL :LOG Failed to insert xml node into !SYMBOL_GRAPHIC!
		) ELSE (
			CALL :LOG Inserted xml node into !SYMBOL_GRAPHIC!

			CALL :GEN_ANIMATION !SVG_EMPTY:.svg=! !SVG_MO:.svg=! %TEMPFILE%
			SET SYMBOL_ANIMATION=!SYMBOL_PATH:_symbol=_animation!
			CALL "%PYTHON_BIN%" "%~dp0\insert_xml_node.py" -i "!SYMBOL_ANIMATION!" -n animationRules -t %TEMPFILE%
			IF %ERRORLEVEL% NEQ 0 (
				CALL :LOG Failed to insert xml node into !SYMBOL_ANIMATION!
			) ELSE (
				CALL :LOG Inserted xml node into !SYMBOL_ANIMATION!
			)
		)
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

:GEN_GRAPHIC
	SETLOCAL
	SET ID=%1
	SET SVG=%2
	SET TEMPFILE=%3
	ECHO ^<svgGroup id="%ID%"^> > %TEMPFILE%
    ECHO      ^<svgRef brickId="%SVG%" id="inhibsynthesis.value_node"^>^</svgRef^> >> %TEMPFILE%
	ECHO ^</svgGroup^> >> %TEMPFILE%
	ENDLOCAL
	@EXIT /B 0

:GEN_ANIMATION
	SETLOCAL
	SET SVG_EMPTY=%1
	SET SVG_MO=%2
	SET TEMPFILE=%3
	ECHO ^<animationRule^> > %TEMPFILE%
	ECHO     ^<source xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns2:statusDataSourceType" name="inhibsynthesis"^>^</source^> >> %TEMPFILE%
	ECHO         ^<sourceType^>INT^</sourceType^> >> %TEMPFILE%
	ECHO         ^<svgRefAnimation defaultValue="%SVG_EMPTY%" nodeId="inhibsynthesis.value_node"^> >> %TEMPFILE%
	ECHO             ^<rangeBinding^> >> %TEMPFILE%
	ECHO                 ^<range from="1" to="2147483647" value="%SVG_MO%"^>^</range^> >> %TEMPFILE%
	ECHO             ^</rangeBinding^> >> %TEMPFILE%
	ECHO         ^</svgRefAnimation^> >> %TEMPFILE%
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
