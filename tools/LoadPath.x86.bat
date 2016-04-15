SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%

SET SOFTS_DIR=softs

SET JAVA_HOME=%CUR_PATH%/%SOFTS_DIR%/jdk1.7.0_71
SET MAVEN_HOME=%CUR_PATH%/%SOFTS_DIR%/apache-maven-3.3.9
SET CATALINA_HOME=%CUR_PATH%/%SOFTS_DIR%/Tomcat-8.0
SET ECLIPSE_HOME=%CUR_PATH%/%SOFTS_DIR%/eclipse-jee-mars-1-win32

SET TAIL_EXE=%CUR_PATH%\softs\Tail-4.2.12\Tail.exe

SET LOG_HOME=%CUR_PATH%/log

SET CATALINA_OPTS="-Djava.library.path=%CATALINA_HOME%\bin"
REM -Xmx768m                                                          
SET JRE_HOME=%JAVA_HOME%/jre

SET M2_HOME=%MAVEN_HOME%

SET M2_REPO=%CUR_PATH%/.m2/repo

SET MAVEN_OPTS=-Xmx768m

SET PATH=.
SET PATH=%PATH%;%JAVA_HOME%/bin;%MAVEN_HOME%/bin;

SET PATH=%PATH%;%SystemRoot%/system32;%SystemRoot%;%SystemRoot%/System32/Wbem;

SET CLASSPATH=.;%JAVA_HOME%/lib;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar;

ECHO %PATH%
