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
@REM e.g. SYMBOL_NAME = C1166B_ECT_TVF1_Left_symbol.xml
SET SYMBOL_NAME=%1
@REM e.g. SVG_NAME = C1166B_ECT_TVF1_Left_symbol.xml
SET SVG_NAME=%SYMBOL_NAME%

@REM ###########################################################################
@REM e.g. SYS_NAME = ECT
SET SYS_NAME=%SVG_NAME:~7,3%
@REM e.g., SVG_NAME = TVF1_Left_symbol.xml
SET SVG_NAME=%SVG_NAME:~11%

@REM e.g. SVG_NAME = TVF1_L_symbol.xml
@REM _Up_ -> _U_, _Down_ -> _D_, _Left_ -> _L_, _Right_ -> _R_, _Horizontal_ -> _H_, _Vertical_ -> _V_
SET SVG_NAME=%SVG_NAME:_Up_=_U_%
SET SVG_NAME=%SVG_NAME:_Down_=_D_%
SET SVG_NAME=%SVG_NAME:_Left_=_L_%
SET SVG_NAME=%SVG_NAME:_Right_=_R_%
SET SVG_NAME=%SVG_NAME:_Horizontal_=_H_%
SET SVG_NAME=%SVG_NAME:_Vertical_=_V_%

@REM ###########################################################################
@REM Remove the "_symbol.xml" postfix
@REM e.g. SVG_NAME = TVF1_L
CALL :STRLEN _symbol.xml
SET LEN=!ERRORLEVEL!
CALL SET SVG_NAME=%%SVG_NAME:~0,-!LEN!%%

@REM ###########################################################################
SET SVG_NAME_FULL=!SYS_NAME!_!SVG_NAME!
IF "!SVG_NAME_FULL!" == "ECT_CALM_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_MFD__ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_TTVF_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_SUMM_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_SUMM_S1_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_SUMM_S2_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_TEMP_ActiveNumber" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "ECT_TES__ActiveNumber" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "SIG_TRAN_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "SIG_SUMM_ActiveText" (
	ECHO 200,100
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "DSS_ACTN_ActiveText" (
	ECHO 0,0
	GOTO :END
) ELSE IF "!SVG_NAME_FULL!" == "DSS_EMOD" (
	ECHO 0,0
	GOTO :END
) ELSE (
	@REM ###########################################################################
	@REM Append MWT SVG postfix
	@REM e.g. SVG_NAME = TVF1_L_State_Selected.svg
	SET SVG_NAME=%SVG_NAME%_State_Selected.svg
	SET SVG_PATH=%SITUATIONVIEW_DIR%\bricks\!SVG_NAME!
	IF EXIST "!SVG_PATH!" GOTO :PARSE

	@REM e.g. SVG_NAME = ECT_TVF1_L_State_Selected.svg
	SET SVG_NAME=%SYS_NAME%_%SVG_NAME%_State_Selected.svg
	SET SVG_PATH=%SITUATIONVIEW_DIR%\bricks\!SVG_NAME!
	IF EXIST "!SVG_PATH!" GOTO :PARSE
	GOTO :ERROR

:PARSE
	@REM Get SVG Rect
	SET SVG_RECT_WIDTH=UNDEFINED
	SET SVG_RECT_HEIGHT=UNDEFINED
	FOR /F "tokens=1,2" %%A IN ('CALL "%PYTHON_BIN%" "%~dp0\get_mwt_svg_rect.py" -b -i "!SVG_PATH!"') DO (
		SET SVG_RECT_WIDTH=%%A
		SET SVG_RECT_HEIGHT=%%B
		ECHO !SVG_RECT_WIDTH!,!SVG_RECT_HEIGHT!
		GOTO :END
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