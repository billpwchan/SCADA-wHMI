@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_str_log=%v_str_log%
IF [%v_str_log%] == [] SET v_str_log=%1

REM start %TAIL_EXE% %v_str_log%
