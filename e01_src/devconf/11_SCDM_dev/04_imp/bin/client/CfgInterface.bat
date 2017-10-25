@echo off
REM #################
REM Batch to run CfgInterface.py
REM #################

rem set JAVA_HOME=D:/MetaConf/scsconf/generator/bin/jdk1.6.0_31

REM Set the environment
REM -----------------------------
cd /d %~dp0
set CONFIGURATEUR_HOME=%~dp0
set CONFIGURATEUR_HOME=%CONFIGURATEUR_HOME:~0,-1%

set LIB=./lib

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
set OPTIONS=%OPTIONS% -Dlog4j.appender.A1.File="%~n0.log"

"%JAVA_HOME%/bin/java" -mx512m -ss5000000 -cp "%LOCALCLASSPATH%" %OPTIONS%  org.python.util.jython -i %~n0.py %*
set RETCODE=%ERRORLEVEL%

rem exit /b %RETCODE%

