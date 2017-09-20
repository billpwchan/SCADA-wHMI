@ECHO OFF

ECHO Git Branch=%1, CurrentDir=%cd%

FOR /F "usebackq tokens=1,2 delims==" %%i IN (`wmic os get LocalDateTime /VALUE 2^>NUL`) DO IF '.%%i.'=='.LocalDateTime.' SET ldt=%%j
SET strdt=%ldt%
SET ldt=%ldt:~0,4%-%ldt:~4,2%-%ldt:~6,2% %ldt:~8,2%:%ldt:~10,2%:%ldt:~12,2%.%ldt:~15,3%
SET strdt=%strdt:~0,4%_%strdt:~4,2%_%strdt:~6,2%_%strdt:~8,2%_%strdt:~10,2%_%strdt:~12,2%_%strdt:~15,3%

ECHO Remove exists workspace if exists
IF EXIST "..\whmi" ( MOVE "..\whmi" "..\whmi.%strdt%")

ECHO Checkout branch %1
CALL "..\cots\softs.x86_64\Git\bin\sh.exe" --login -i -c "/d/Build.SCADAgen/whmi_build_scripts/git_clone_checkout_whmi.sh %1"

ECHO Change Directory
CD "..\whmi\tools"

ECHO Branch=%1, CurrentDir=%cd%

ECHO Build SCADAgen WHMI
CALL Build.whmi.export.bat %1

ECHO End of build
PAUSE