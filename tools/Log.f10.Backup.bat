IF NOT EXIST ".\logs" MKDIR .\logs
IF NOT EXIST ".\logs.backup" MKDIR .\logs.backup

DEL /Q .\logs.backup\build.f10*.log

MOVE /-y .\logs\build.f10*.log .\logs.backup\

MOVE /-y .\build.f10*.log .\logs\

REM MOVE /-y 

REM PAUSE