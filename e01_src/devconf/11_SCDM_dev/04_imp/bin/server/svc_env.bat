@echo off

for /f %%a in ("%~dp0") do (
	set CONF_VERSION=%%a
	call set CONF_VERSION=%%CONF_VERSION:\server\bin\=%%
	for /f %%z in ('call echo %%CONF_VERSION%%') do set CONF_VERSION=%%~nxz
)
set SVCNAME=%CONF_VERSION%
