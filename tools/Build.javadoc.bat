@ECHO OFF

CALL LoadPath.bat

SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%
ECHO %CUR_PATH%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Build.javadoc.%v_strdt%.log

SET PATH_DES=%CUR_PATH%\..\javadoc
SET PATH_SRC=%SOURCE_BASE_HOME%\f01\workspace.wrapper\wrapper-wrapper\src\main\java\

ECHO PATH_DES=%PATH_DES%
ECHO PATH_SRC=%PATH_SRC%

javadoc -d %PATH_DES% -sourcepath %PATH_SRC% -subpackages com.thalesgroup.scadagen.wrapper.wrapper.client -subpackages com.thalesgroup.scadagen.wrapper.wrapper.server > %LOG_FILE%

PAUSE