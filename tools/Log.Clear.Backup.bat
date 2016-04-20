IF NOT EXIST ".\logs" MKDIR .\logs
IF NOT EXIST ".\logs.backup" MKDIR .\logs.backup

DEL /Q .\logs.backup\*.log

MOVE /-y .\logs\*.log .\logs.backup\

MOVE /-y .\*.log .\logs\

MOVE /-y 

REM PAUSE