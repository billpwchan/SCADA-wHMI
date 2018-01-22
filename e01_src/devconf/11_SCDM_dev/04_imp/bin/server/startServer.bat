echo off
REM Chargement de l'environnement
REM -----------------------------
call env_serv

echo Lancement du serveur....
echo Vérifier les traces dans le fichier ../log/stdout.log

set JAVA_PARAM=-server -Xmx256m -Xms256m -ss1M -XX:SurvivorRatio=32
set JAVA_PARAM64=-server -Xmx4g -Xms4g -XX:PermSize=128m -XX:MaxPermSize=256m -XX:SurvivorRatio=32

echo OS processor type:

if "%PROCESSOR_ARCHITECTURE%"=="AMD64" goto 64BIT
echo 32-bit OS
goto END

:64BIT
echo 64-bit OS

	echo JAVA architecture type:
	"%JAVA_HOME%/bin/java" -version 2>&1 | find "64-Bit" >nul:
	if errorlevel 1 (
		echo 32-bit JAVA
	) else (
		echo 64-bit JAVA
		set JAVA_PARAM=%JAVA_PARAM64%
	)
:END

"%JAVA_HOME%/bin/java" %JAVA_PARAM% -Dsun.rmi.server.exceptionTrace=true -Djava.security.policy=../parametrage/factory.policy -cp %LOCALCLASSPATH% -Dapplication.config=%CONFIGURATEUR_PROP% com.thalesis.config.business.factory.FactoryServer > ../log/stdout.log 2>&1

