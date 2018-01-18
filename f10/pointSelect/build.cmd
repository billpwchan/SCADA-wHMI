@ECHO OFF
@SETLOCAL

@SET BASE_HREF=.

@ECHO Building Application...
@CALL npm install
@CALL npm run packagr
@CD dist
@CALL npm pack
REM @CALL ng build --base-href=%BASE_HREF% --target=production --extract-css --aot --sourcemap --stats-json --output-hashing=none
@IF %ERRORLEVEL% NEQ 0 (
    @ECHO Failed in ng build
) ELSE (
    @ECHO Completed
    @ECHO `- Binary at "/dist/"
)
@ENDLOCAL
