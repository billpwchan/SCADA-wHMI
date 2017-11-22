@ECHO off

CALL LoadPath.bat

SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%
ECHO %CUR_PATH%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\Build.doxygen.%v_strdt%.log

SET doxygen_PATH=%DOXYGEN_HOME%\doxygen
SET graphviz_PATH=%DOXYGEN_HOME%\graphviz\bin
SET mscgen_PATH=%DOXYGEN_HOME%\mscgen\bin

ECHO doxygen_PATH=%doxygen_PATH%
ECHO graphviz_PATH=%graphviz_PATH%
ECHO mscgen_PATH=%mscgen_PATH%
SET DOT_PATH=%CUR_PATH%;%doxygen_PATH%;%graphviz_PATH%;%mscgen_PATH%
ECHO %DOT_PATH%

SET PATH=.
SET PATH=%PATH%;%DOT_PATH%;

ECHO PATH=%PATH%

%doxygen_PATH%/doxygen.exe whmi.doxy > %LOG_FILE%

PAUSE