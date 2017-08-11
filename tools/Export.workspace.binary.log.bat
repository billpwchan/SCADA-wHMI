@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of workspace (F01, F02)

SET _folder="%DEPOT_HOME%\%v_strdt%"
ECHO _folder=%_folder% 
MD %_folder%

SET _folderlogs=%_folder%\logs
ECHO _folderlogs=%_folderlogs%
MD %_folderlogs%

SET _F01_WEBAPPFUNC="%SOURCE_BASE_HOME%\f01\workspace.webapp-func"
SET _F01_WHMI_UIWIDGET="%SOURCE_BASE_HOME%\f01\workspace.whmi.uiwidget"
SET _F01_WRAPPER="%SOURCE_BASE_HOME%\f01\workspace.wrapper"
SET _F01_FAS="%SOURCE_BASE_HOME%\f01\workspace.fas"
SET _F02_WEBAPP="%SOURCE_BASE_HOME%\f02\workspace.webapp\mywebapp"

ECHO _F01_WEBAPPFUNC=%_F01_WEBAPPFUNC%
ECHO _F01_WHMI_UIWIDGET=%_F01_WHMI_UIWIDGET%
ECHO _F01_WRAPPER=%_F01_WRAPPER%
ECHO _F01_FAS=%_F01_FAS%
ECHO _F02_WEBAPP=%_F02_WEBAPP%

cd %_F01_WEBAPPFUNC%\webapp-func\target
for /r %%a in (myproject-webapp-func*.zip) do (
	COPY /B /Y /V %%~fa %_folder%\f01-webapp-func.zip
)

cd %_F02_WEBAPP%\target
for /r %%a in (mywebapp-*.war) do (
	COPY /B /Y /V %%~fa %_folder%\scadagen-f02-webapp-generic.war
)

REM Export Logs

COPY "%TOOLS_BASE%\Build.workspace.*.log" %_folder%\logs

ECHO END OF workspace (F01, F02)