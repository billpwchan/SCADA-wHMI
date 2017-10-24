echo off

REM Chargement de l'environnement
REM -----------------------------
call env_serv

echo Lancement du serveur....
echo Vérifier les traces dans le fichier ../log/stdout.log

echo Detecting OS processor type

if "%PROCESSOR_ARCHITECTURE%"=="AMD64" goto 64BIT
echo 32-bit OS
	set JAVA_PARAM=-server -Xmx256m -Xms256m -ss1M -XX:SurvivorRatio=32
goto END
:64BIT
echo 64-bit OS
	rem set JAVA_PARAM=-server -Xmx4096M -Xms4096m -ss1M -XX:SurvivorRatio=32 
	set JAVA_PARAM=-server -Xmx4g -Xms4g -XX:PermSize=128m -XX:MaxPermSize=256m -XX:SurvivorRatio=32 
:END

"%JAVA_HOME%/bin/java" %JAVA_PARAM% -Dsun.rmi.server.exceptionTrace=true -Djava.security.policy=../parametrage/factory.policy -cp %LOCALCLASSPATH% -Dapplication.config=%CONFIGURATEUR_PROP% com.thalesis.config.business.factory.FactoryServer > ../log/stdout.log 2>&1

