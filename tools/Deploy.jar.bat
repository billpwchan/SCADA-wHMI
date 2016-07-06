@ECHO OFF

call LoadPath.bat

echo delete war file...

set PATH_FOLDER="%scstraning_loc%\scspaths\WEBAPP\apache-tomcat%TOMCAT_VER%\conf\scadagen-f01-webapp-func"
set PATH_FILES="%PATH_FOLDER%\*"

echo DELETE %PATH_FILES%

del /s /f /q %PATH_FILES%

set PATH_SOURCE="R:\1166B\whmi\f01\workspace.webapp-func\webapp-func\target"

cd %PATH_SOURCE%

for /r %%a in (myproject-webapp-func*.zip) do (
	%SEVEN_ZIP_HOME% e %%~fa -o%PATH_FOLDER% -y
)

cd %CUR_PATH%