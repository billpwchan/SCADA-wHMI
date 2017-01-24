@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

for /F "usebackq tokens=1,2 delims==" %%i in (`wmic os get LocalDateTime /VALUE 2^>NUL`) do if '.%%i.'=='.LocalDateTime.' set ldt=%%j
set strdt=%ldt%
set ldt=%ldt:~0,4%-%ldt:~4,2%-%ldt:~6,2% %ldt:~8,2%:%ldt:~10,2%:%ldt:~12,2%.%ldt:~15,3%
set strdt=%strdt:~0,4%_%strdt:~4,2%_%strdt:~6,2%_%strdt:~8,2%_%strdt:~10,2%_%strdt:~12,2%_%strdt:~15,3%

ECHO Current Local date is [%ldt%]
ECHO Current Local date string is [%strdt%]

CALL "%TOOLS_PATH%\LoadFolder.bat"

:CheckOS
IF EXIST "%PROGRAMFILES(X86)%" (GOTO 64BIT) ELSE (GOTO 32BIT)

:64BIT
ECHO Loading 64-bit Configuration...
CALL "%TOOLS_PATH%\LoadPath.x64.bat"
GOTO END

:32BIT
ECHO Loading 32-bit Configuration...
CALL "%TOOLS_PATH%\LoadPath.x86.bat"
GOTO END

:END