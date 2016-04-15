@echo off

call LoadFolder.bat

echo delete war file...

echo DELETE "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp.war"

del /s /f /q "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp.war"

echo remove war folder...

echo DELETE  "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp"

rmdir /s /q "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp"

echo copy war file

echo FROM "%SOURCE_BASE%\%sp_webapp%\mywebapp\target\mywebapp-%war_ver%.war"
echo TO   "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp.war"

copy /B /Y /V "%SOURCE_BASE%\%sp_webapp%\mywebapp\target\mywebapp-%war_ver%.war" "%scstraning_loc%\scspaths\WEBAPP\apache-tomcat-8.0.23\webapps\mywebapp.war"

REM PAUSE