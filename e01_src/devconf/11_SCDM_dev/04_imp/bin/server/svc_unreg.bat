@echo off
call %~dp0svc_env.bat %*

echo Remove Service %CONF_VERSION%
%~dp0runservice -remove %CONF_VERSION%

pause
