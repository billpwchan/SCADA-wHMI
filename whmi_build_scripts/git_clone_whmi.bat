@echo off

SET CUR_PATH=%~dp0
SET SCRIPT_PATH=%CUR_PATH:~0,-1%

for /F "usebackq tokens=1,2 delims==" %%i in (`wmic os get LocalDateTime /VALUE 2^>NUL`) do if '.%%i.'=='.LocalDateTime.' set ldt=%%j
set strdt=%ldt%
set ldt=%ldt:~0,4%-%ldt:~4,2%-%ldt:~6,2% %ldt:~8,2%:%ldt:~10,2%:%ldt:~12,2%.%ldt:~15,3%
set strdt=%strdt:~0,4%_%strdt:~4,2%_%strdt:~6,2%_%strdt:~8,2%_%strdt:~10,2%_%strdt:~12,2%_%strdt:~15,3%

SET GIT_HOME="%SCRIPT_PATH%\..\cots\softs.x86_64\Git"
SET GIT_BIN="%GIT_HOME%\bin"

SET REPO_URL="http://titanium.hk.thales:7990/scm/scadagen/whmi.git"

SET REPO_DIR="%SCRIPT_PATH%\..\whmi"

cd %GIT_BIN%
git clone %REPO_URL% %REPO_DIR%
