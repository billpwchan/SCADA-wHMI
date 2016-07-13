@echo off
REM #################
REM Batch to run BackupCDV.py
REM #################

REM Set the environment
REM -----------------------------

REM -----------------------
REM call env_serv from server side
call ..\..\..\..\server\bin\env_serv.bat
echo %JAVA_HOME%
REM -----------------------

setlocal enableextensions

set HOME=%~dp0

rem  cd /d %~dp0..\..\..\..\client\
set CONFIGURATEUR_HOME=%~dp0..\..\..\..\client\
set CONFIGURATEUR_HOME=%CONFIGURATEUR_HOME:~0,-1%
rem cd %CONFIGURATEUR_HOME%
set log=.\..\..\..\..\server\log\%~n0.log
rem set LIB=./lib
set LIB=%CONFIGURATEUR_HOME%\lib

set LOCALCLASSPATH=%LIB%/configurateurRmi.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurCommon.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurClient.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jdom.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xml-apis.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xerces.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xalan.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/log4j.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/velocity-1.1.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/antlr-runtime.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xwingml.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jython.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;./conf/

set OPTIONS=-Dapplication.home="%CONFIGURATEUR_HOME%" 
set OPTIONS=%OPTIONS% -Dapplication.config="%CONFIGURATEUR_HOME%\conf\configurateurClient.prop"
set OPTIONS=%OPTIONS% -Dlog4j.appender.A1.File=%log%

"%JAVA_HOME%/bin/java"  -cp "%LOCALCLASSPATH%" %OPTIONS%  org.python.util.jython %HOME%%~n0.py %*
rem "%JAVA_HOME%/bin/java"  %OPTIONS%  org.python.util.jython %HOME%%~n0.py %*
set RETCODE=%ERRORLEVEL%
rem echo Exit Code is %RETCODE%

rem cd %HOME%
rem exit /b %RETCODE%
endlocal
