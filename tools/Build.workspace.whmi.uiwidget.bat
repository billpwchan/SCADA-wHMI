@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_uiwidget%.%v_strdt%.log

cd /d %SOURCE_BASE_F01%/%sp_uiwidget%

echo Starting build %sp_uiwidget% process and log to %LOG_FILE%...

echo Cleaning %sp_uiwidget% before build...
call mvn clean -V > %LOG_FILE%

echo Building %sp_uiwidget%...
REM call mvn clean war:exploded install >> %LOG_FILE%
call mvn clean gwt:clean install >> %LOG_FILE%

cd %TOOLS_PATH%
 
echo End of build %sp_uiwidget%

REM PAUSE