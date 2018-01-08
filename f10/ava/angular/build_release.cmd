@ECHO OFF
SETLOCAL

SET BASE_HREF=.
ECHO Running lint...
CALL npm run lint
IF %ERRORLEVEL% NEQ 0 (
    ECHO Failed in ng lint, please fix the problems before building
) ELSE (
	ECHO Building Application...
	CALL ng build --base-href=%BASE_HREF% --target=production --extract-css --aot --output-hashing=none
	IF %ERRORLEVEL% NEQ 0 (
		ECHO Failed in ng build
	) ELSE (
		ECHO Completed
		ECHO `- Binary at "/dist/"
	)
)
ENDLOCAL
