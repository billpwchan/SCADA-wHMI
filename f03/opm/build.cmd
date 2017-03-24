@ECHO OFF
@SETLOCAL

@SET BASE_HREF=/opm/
@SET OUTPUT_PATH=dist

@ECHO Building Application...
@CALL ng build --base-href=%BASE_HREF% --app=fas-opm --target=production --aot --sourcemap --stats-json --output-hashing=none --output-path="%OUTPUT_PATH%"
@IF %ERRORLEVEL% NEQ 0 (
    ECHO Failed in ng build
    GOTO :END
)

@ECHO Copying configuration files...
@XCOPY /Y /V /E /I src\app\config "%OUTPUT_PATH%\config"
@IF %ERRORLEVEL% NEQ 0 (
    ECHO Failed in copying configuration files
    GOTO :END
)

@ECHO Completed
@ECHO `- Binary at "%OUTPUT_PATH%"
@GOTO :END

:END
@ENDLOCAL