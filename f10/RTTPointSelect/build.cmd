@ECHO OFF
@SETLOCAL

@SET BASE_HREF=/rtt/

@ECHO Building Application...
@CALL ng build --base-href=%BASE_HREF% --target=production --extract-css --aot --sourcemap --stats-json --output-hashing=none
@IF %ERRORLEVEL% NEQ 0 (
    @ECHO Failed in ng build
) ELSE (
    @ECHO Completed
    @ECHO `- Binary at "/dist/"
)
@ENDLOCAL
