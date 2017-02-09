@echo off

SET CUR_PATH=%~dp0
SET SCRIPT_PATH=%CUR_PATH:~0,-1%

SET REPO_WHMI="%SCRIPT_PATH%\..\cots\.m2\repo\com\thalesgroup\scadagen"

rmdir /s /q "%REPO_WHMI%"