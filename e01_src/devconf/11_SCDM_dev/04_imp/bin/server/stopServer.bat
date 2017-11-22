echo off

REM Chargement de l'environnement
REM -----------------------------
call env_serv

echo Arrêt du serveur, vérification des clients connectés....

"%JAVA_HOME%/bin/java" -Djava.security.policy=../parametrage/factory.policy -Dapplication.config=%CONFIGURATEUR_PROP% -cp %LOCALCLASSPATH% com.thalesis.config.admintools.ConfiguratorServerAdmin quit

