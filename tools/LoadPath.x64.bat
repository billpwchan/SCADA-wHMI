SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%

SET TOOLS_BASE=%CUR_PATH%
SET SOFTS_BASE=%CUR_PATH%/../../cots
SET SOURCE_BASE=%CUR_PATH%/..
SET SOURCE_BASE_F01=%SOURCE_BASE%/f01
SET SOURCE_BASE_F02=%SOURCE_BASE%/f02
SET SOURCE_BASE_F03=%SOURCE_BASE%/f03
SET REPO_BASE=%CUR_PATH%/../../cots

SET SOFTS_DIR=softs.x86_64

REM Absolute Path For 7 Zip Command Line
SET DEPOT_HOME=D:\Build.SCADAgen.output
SET SOURCE_BASE_HOME=D:\Build.SCADAgen\whmi

SET JAVA_HOME=%SOFTS_BASE%/%SOFTS_DIR%/jdk1.7.0_79_x86_64
SET MAVEN_HOME=%SOFTS_BASE%/%SOFTS_DIR%/apache-maven-3.3.9
SET CATALINA_HOME=%SOFTS_BASE%/%SOFTS_DIR%/apache-tomcat-8.0.30-x86_64
SET ECLIPSE_HOME=%SOFTS_BASE%/%SOFTS_DIR%/eclipse-jee-mars-R-win32-x86_64
SET CHROME_HOME=D:\Users\syau\PortableApps\GoogleChromePortable\GoogleChromePortable.exe
SET SEVEN_ZIP_HOME=%SOFTS_BASE%/%SOFTS_DIR%/PortableApps/7-ZipPortable/App/7-Zip64/7z.exe
SET DOXYGEN_HOME=%SOFTS_BASE%/%SOFTS_DIR%/doxygen

SET TAIL_EXE=%SOFTS_BASE%/%SOFTS_DIR%/Tail-4.2.12\Tail.exe

SET LOG_HOME=%CUR_PATH%

SET CATALINA_OPTS=-Djava.library.path=%CATALINA_HOME%\bin

SET JRE_HOME=%JAVA_HOME%/jre

SET M2_HOME=%MAVEN_HOME%

SET M2_REPO=%REPO_BASE%/.m2/repo

SET PATH=.
SET PATH=%PATH%;%JAVA_HOME%/bin;%MAVEN_HOME%/bin;

SET PATH=%PATH%;%SystemRoot%/system32;%SystemRoot%;%SystemRoot%/System32/Wbem;

SET CLASSPATH=.;%JAVA_HOME%/lib;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar;

REM ECHO %PATH%

ECHO M2_REPO [%M2_REPO%]
