REM Author: Katherine SHIH

for /f "tokens=1,2 delims== " %%i in (config_%USERNAME%.txt) do (
	if %%i==vdc set old_vdc=%%j
	if %%i==ved set old_ved=%%j
	rem if %%i==sys set old_sys=%%j
)

SET /p VDC="Please specify the CDV you want to update (default is "%old_vdc%")"
SET /p VED="Please specify the WDS contains library definition (default is "%old_ved%")"
rem SET /p SYS="Please specify the suffix of the interface system (default is "%old_sys%")"

IF "%VDC%"=="" (
	set VDC_arg=%old_vdc%
) else (
	set VDC_arg=%VDC%
)
echo vdc=%VDC_arg%> config_%USERNAME%.txt

IF "%VED%"=="" (
	set VED_arg=%old_ved%
) else (
	set VED_arg=%VED%
)
echo ved=%VED_arg%>> config_%USERNAME%.txt

REM IF "%SYS%"=="" (
	REM set SYS_arg=%old_sys%
REM ) else (
	REM set SYS_arg=%SYS%
REM )
REM echo sys=%SYS_arg%>> config_%USERNAME%.txt