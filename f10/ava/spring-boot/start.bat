set WEBAPP=AVAWEBAPP

IF NOT EXIST %WEBAPP%.pid goto startspringboot
set /P logpid=<%WEBAPP%.pid
tasklist /FI "WINDOWTITLE eq %WEBAPP%" /FO LIST | findstr PID | findstr %logpid%
echo ERRORLEVEL %ERRORLEVEL%
IF %ERRORLEVEL%==0 goto alreadyrunning

:startspringboot
start springboot.bat

for /f "TOKENS=2" %%a in ('tasklist /FI "WINDOWTITLE eq %WEBAPP%" /FO LIST ^| findstr PID') do ( echo %%a > %WEBAPP%.pid )
goto exit

:alreadyrunning
echo Cannot start: application already running
pause

:exit