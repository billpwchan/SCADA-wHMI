@ECHO OFF

SET CUR_PATH=%~dp0
SET SCRIPT_PATH=%CUR_PATH:~0,-1%

SET WHMI_PATH="%SCRIPT_PATH%\..\whmi"

SET WHMI_TOOLS_PATH="%WHMI_PATH%\tools"

CD %WHMI_TOOLS_PATH%

CALL "%WHMI_TOOLS_PATH%\Build.workspace.bat"