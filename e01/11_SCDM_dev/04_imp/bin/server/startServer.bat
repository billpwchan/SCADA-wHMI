echo off

REM Chargement de l'environnement
REM -----------------------------
call env_serv

echo Lancement du serveur....
echo Vérifier les traces dans le fichier ../log/stdout.log

set JAVA_PARAM=-server -mx1024M -ss1M -XX:SurvivorRatio=32 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=11044

"%JAVA_HOME%/bin/java" %JAVA_PARAM% -Dsun.rmi.server.exceptionTrace=true -Djava.security.policy=../parametrage/factory.policy -cp %LOCALCLASSPATH% -Dapplication.config=%CONFIGURATEUR_PROP% com.thalesis.config.business.factory.FactoryServer > ../log/stdout.log 2>&1


