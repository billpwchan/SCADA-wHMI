@ECHO OFF

call LoadPath.bat

SET _YYYY=%date:~10,4%
SET _MM=%date:~7,2%
SET _DD=%date:~4,2%
SET _time=%time::=.%
SET _folder="C:\1166B.source\%_YYYY%.%_MM%.%_DD%.%_time%"
SET _folderlogs=%_folder%\logs

ECHO _folder=%_folder% 
ECHO _folderlogs=%_folderlogs%

MD %_folder%
MD %_folder%\logs

SET _1166B="R:\1166B\whmi"
SET _F01_WEBAPPFUNC="%_1166B%\f01\workspace.webapp-func"
SET _F01_WHMI="%_1166B%\f01\workspace.whmi"
SET _F01_WHMI_UIWIDGET="%_1166B%\f01\workspace.whmi.uiwidget"
SET _F01_WRAPPER="%_1166B%\f01\workspace.wrapper"
SET _F01_FAS="%_1166B%\f01\workspace.fas"
SET _F02_WEBAPP="%_1166B%\f02\workspace.webapp\mywebapp"

SET _E02="%scstraning_loc%\scspaths\%_WEBAPP%\apache-tomcat%TOMCAT_VER%\conf\e02-resources-current"
SET _P01="%scstraning_loc%\scspaths\%_WEBAPP%\apache-tomcat%TOMCAT_VER%\conf\p01-func-current"

ECHO _F01_WEBAPPFUNC=%_F01_WEBAPPFUNC%
ECHO _F01_WHMI=%_F01_WHMI%
ECHO _F01_WHMI_UIWIDGET=%_F01_WHMI_UIWIDGET%
ECHO _F01_WRAPPER=%_F01_WRAPPER%
ECHO _F01_FAS=%_F01_FAS%
ECHO _F02_WEBAPP=%_F02_WEBAPP%

ECHO _E02=%_E02%
ECHO _P01=%_P01%


cd %_F01_WEBAPPFUNC%\webapp-func\target
for /r %%a in (myproject-webapp-func*.zip) do (
	COPY /B /Y /V %%~fa %_folder%\f01-webapp-func.zip
)

cd %_F02_WEBAPP%\target
for /r %%a in (mywebapp-*.war) do (
	COPY /B /Y /V %%~fa %_folder%\scadagen-f02-webapp-generic.war
)

REM COPY "%_F01_WEBAPPFUNC%\webapp-func\target\myproject-webapp-func*.zip" %_folder%\f01-webapp-func.zip

REM COPY "%_F02_WEBAPP%\target\*.war" %_folder%\scadagen-f02-webapp-generic.war

COPY "%_1166B%\tools\Build*.log" %_folder%\logs

CALL "%_1166B%\tools\Clean.workspace.bat"

SET _EXCLUDE=-mx0 "-xr!.metadata" "-xr!.settings" "-xr!.recommenders" "-xr!RemoteSystemsTempFiles"
SET _EXCLUDE2="-xr!mywebapp\src\main\gwt-unitCache"
SET _EXCLUDE3="-xr!workspace.webapp-func\webapp-func\myproject-webapp-func*.zip"
ECHO _EXCLUDE=%_EXCLUDE%

%SEVEN_ZIP_HOME% a %_folder%\f01 %_F01_WEBAPPFUNC% %_F01_WHMI% %_F01_WHMI_UIWIDGET% %_F01_WRAPPER% %_F01_FAS% %_EXCLUDE% %_EXCLUDE3%
%SEVEN_ZIP_HOME% a %_folder%\p01 %_P01% %_EXCLUDE%
%SEVEN_ZIP_HOME% a %_folder%\f02 %_F02_WEBAPP% %_EXCLUDE% %_EXCLUDE2%
%SEVEN_ZIP_HOME% a %_folder%\e02 %_E02% %_EXCLUDE%

ECHO END OF BACKUP