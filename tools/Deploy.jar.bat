@ECHO OFF

call LoadPath.bat

echo delete war file...

set PATH_FOLDER="%scstraning_loc%\scspaths\%_WEBAPP%\apache-tomcat%TOMCAT_VER%\conf\scadagen-f01-webapp-func"
set PATH_FILES="%PATH_FOLDER%\*"

echo DELETE %PATH_FILES%

del /s /f /q %PATH_FILES%

set PATH_SOURCE="%SOURCE_BASE_F01%\workspace.webapp-func\webapp-func\target"

cd %PATH_SOURCE%

for /r %%a in (myproject-webapp-func*.zip) do (
	%PATH_7Z_BIN% e %%~fa -o%PATH_FOLDER% -y
)

cd %CUR_PATH%