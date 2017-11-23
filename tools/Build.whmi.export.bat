@ECHO OFF

SET CUR_PATH=%~dp0
SET TOOLS_PATH=%CUR_PATH:~0,-1%

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

ECHO "v_strdt=%v_strdt%"

ECHO Start of Build.whmi.export...

CALL "%TOOLS_PATH%\LoadFolder.bat"

CALL %TOOLS_PATH%\Export.git.commit.bat %v_strdt%

CALL %TOOLS_PATH%\Build.workspace.bat %v_strdt%
CALL %TOOLS_PATH%\Export.workspace.binary.log.bat %v_strdt%

CALL %TOOLS_PATH%\Export.ref.bat %v_strdt%

CALL %TOOLS_PATH%\Export.e01.bat %v_strdt%
CALL %TOOLS_PATH%\Export.e02.bat %v_strdt%
CALL %TOOLS_PATH%\Export.p01.bat %v_strdt%

CALL %TOOLS_PATH%\Build.e03.bat %v_strdt%
CALL %TOOLS_PATH%\Export.e03.binary.log.bat %v_strdt%

CALL %TOOLS_PATH%\Build.connectors.all.bat %v_strdt%
CALL %TOOLS_PATH%\Export.connector.binary.log.bat %v_strdt%

CALL %TOOLS_PATH%\Build.f10.all.bat %v_strdt%
CALL %TOOLS_PATH%\Export.f10.binary.log.bat %v_strdt%

CALL %TOOLS_PATH%\Export.repo.bat %v_strdt%

ECHO End of Build.whmi.export.

REM PAUSE