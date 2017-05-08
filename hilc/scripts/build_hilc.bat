@echo off

IF NOT DEFINED MSYS_HOME (
	echo MSYS_HOME is not defined, exit !
	pause
	exit
)

echo %MSYS_HOME%\bin\sh build_hilc.sh is running...

%MSYS_HOME%\bin\sh build_hilc.sh > NUL 2>&1
