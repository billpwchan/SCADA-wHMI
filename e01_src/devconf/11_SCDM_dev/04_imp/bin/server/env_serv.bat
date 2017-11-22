echo off

set JAVA_HOME=%JAVA_HOME%
set CLASSPATH_JDBC_ORA=%CLASSPATH_JDBC_ORA%

REM ##########################################
REM fichier de paramétrage principal
REM ##########################################

if "%JAVA_HOME%" == "" goto errorEnv
if "%CLASSPATH_JDBC_ORA%" == "" goto errorEnv

set CONFIGURATEUR_PROP=../parametrage/configurateurServer.prop
set LIB=../bin/lib

set LOCALCLASSPATH=%LIB%/configurateurRmi.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurCommon.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurServerUMCD.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/poi-3.13-20150929.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/poi-ooxml-3.13-20150929.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xmlbeans-2.6.0.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/poi-ooxml-schemas-3.13-20150929.jar
REM for debugging uncomment line below and comment line above
REM set LOCALCLASSPATH=%LOCALCLASSPATH%;%rootdir%/devconf/MC_workspace/dev/bin
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurServer.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%CLASSPATH_JDBC_ORA%
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jdom.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xml-apis.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xerces.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xalan.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/log4j.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/velocity-1.1.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/antlr-runtime.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/edtftpj.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;../parametrage/
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/dom4j.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jasperreports-3.7.2.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/iText-2.1.7.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/commons-digester-1.7.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/commons-collections-2.1.1.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/commons-logging-1.0.4.jar

goto end

:errorEnv
echo Environnement incorrect pour la commande :
echo    JAVA_HOME=%JAVA_HOME%
echo    CLASSPATH_JDBC_ORA=%CLASSPATH_JDBC_ORA%
echo Ces variables doivent contenir respectivement le chemin d'installation
echo de la jvm et les jars JDBC d'Oracle
exit 1

:end


