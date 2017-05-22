@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

CALL %TOOLS_PATH%\LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

SET _folder="%DEPOT_HOME%\%v_strdt%"

SET _folderlogs=%_folder%\logs

ECHO _folder=%_folder% 
ECHO _folderlogs=%_folderlogs%

MD %_folder%
MD %_folder%\logs

SET _F01_WEBAPPFUNC="%SOURCE_BASE_HOME%\f01\workspace.webapp-func"
SET _F01_WHMI_UIWIDGET="%SOURCE_BASE_HOME%\f01\workspace.whmi.uiwidget"
SET _F01_WRAPPER="%SOURCE_BASE_HOME%\f01\workspace.wrapper"
SET _F01_FAS="%SOURCE_BASE_HOME%\f01\workspace.fas"
SET _F02_WEBAPP="%SOURCE_BASE_HOME%\f02\workspace.webapp\mywebapp"

SET _E02="D:\Build.SCADAgen\whmi\e02"
SET _P01="D:\Build.SCADAgen\whmi\p01"

SET _PACKAGE_SCADAGEN="com\thalesgroup\scadagen"
SET _REPO_SCADAGEN="%M2_REPO%\%_PACKAGE_SCADAGEN%"

ECHO _REPO_SCADAGEN=%_REPO_SCADAGEN%

ECHO _F01_WEBAPPFUNC=%_F01_WEBAPPFUNC%
ECHO _F01_WHMI_UIWIDGET=%_F01_WHMI_UIWIDGET%
ECHO _F01_WRAPPER=%_F01_WRAPPER%
ECHO _F01_FAS=%_F01_FAS%
ECHO _F02_WEBAPP=%_F02_WEBAPP%

ECHO _E02=%_E02%
ECHO _P01=%_P01%


REM Repo
SET _REPO_DEPO="%_folder%\repo"
SET _REPO_DEPO_SCADAGEN="%_REPO_DEPO%\%_PACKAGE_SCADAGEN%"

ECHO _REPO_DEPO_SCADAGEN=%_REPO_DEPO_SCADAGEN%


cd %_F01_WEBAPPFUNC%\webapp-func\target
for /r %%a in (myproject-webapp-func*.zip) do (
	COPY /B /Y /V %%~fa %_folder%\f01-webapp-func.zip
)

cd %_F02_WEBAPP%\target
for /r %%a in (mywebapp-*.war) do (
	COPY /B /Y /V %%~fa %_folder%\scadagen-f02-webapp-generic.war
)

REM Export Logs

COPY "%TOOLS_BASE%\Build*.log" %_folder%\logs

REM Export Repo

MD %_REPO_DEPO_SCADAGEN%

XCOPY %_REPO_SCADAGEN% %_REPO_DEPO_SCADAGEN% /s/h/e/k/f/c

Export Export E01, P01

SET _EXCLUDE=-mx0 "-xr!.metadata" "-xr!.settings" "-xr!.recommenders" "-xr!RemoteSystemsTempFiles"
SET _EXCLUDE2="-xr!mywebapp\src\main\gwt-unitCache"
SET _EXCLUDE3="-xr!workspace.webapp-func\webapp-func\myproject-webapp-func*.zip"
ECHO _EXCLUDE=%_EXCLUDE%

%SEVEN_ZIP_HOME% a %_folder%\p01 %_P01% %_EXCLUDE%
%SEVEN_ZIP_HOME% a %_folder%\e02 %_E02% %_EXCLUDE%
%SEVEN_ZIP_HOME% a %_folder%\repo %_REPO_DEPO% %_EXCLUDE%

ECHO END OF BACKUP