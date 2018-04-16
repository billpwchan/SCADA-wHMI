set WEBAPP=AUTOLOGOUTWEBAPP
set JARPATTERN=autologout*.jar

title %WEBAPP%

set curdirpath=%~dp0

FOR %%F in (%curdirpath:~0,-1%) do set rootdir=%%~dpF
FOR %%F in (%rootdir:~0,-1%) do set rootdir=%%~dpF
set rootdir=%rootdir:~0,-1%

REM softs pas au meme endroit en dev et sur machine cible
if exist %rootdir%\softs goto env_cible
REM en env de dev, softs est a cote de rootdir
set SOFTS_DIR=%rootdir%\..\..\softs
goto suite
:env_cible
set SOFTS_DIR=%rootdir%\softs

:suite
REM JAVA
set JAVA_HOME=%SOFTS_DIR%\jdk1.7.0_80_x64

REM PATH
set PATH=%PATH%;%JAVA_HOME%\bin

for /r %%f in (%JARPATTERN%) do (
	set JAR=%%f
	goto run
)

:notfounderror
echo %WEBAPP% jar file not found
goto exit

:run
java -jar %JAR%

:exit