@ECHO OFF

call LoadPath.bat

echo delete war file...

set PATH_FOLDER="%scstraning_loc%\scspaths\%_WEBAPP%\apache-tomcat%TOMCAT_VER%\webapps\scadagen-f02-webapp-generic"
set PATH_WAR="%PATH_FOLDER%.war"

echo DELETE 

del /s /f /q %PATH_WAR%

echo remove war folder...

echo DELETE  %PATH_FOLDER%

rmdir /s /q %PATH_FOLDER%

echo copy war file

set PATH_RELEASE="%SOURCE_BASE%\%sp_webapp%\mywebapp\target\mywebapp-%war_ver%.war"

echo FROM %PATH_RELEASE%
echo TO   %PATH_WAR%

copy /B /Y /V %PATH_RELEASE% %PATH_WAR%

REM PAUSE