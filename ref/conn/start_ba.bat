@echo "===================="
@echo on

title BA_SCADA1_NODE1

set arg1=%1
set arg2=%2

set physenv=%arg2:~2,80%
set srvname=%arg1:~2,80%

set SCS_ARGS=-Dscadasoft.servername=%srvname% -Dscadasoft.physicalenv=%physenv%

set "JAVA_OPTS=-Xms300M -Xmx900M -XX:MaxPermSize=128M"
set JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.config.file=logging.properties -XX:OnError="taskkill -F -PID %%p " -XX:OnOutOfMemoryError="taskkill -F -PID %%p "

REM ping -n 10 -w 1000 1.2.3.4 > nul: 2>&1

rmdir /Q /S terracotta\client-logs

REM For debugging jna pb use:
REM   -Djna.debug_load.jna=true -Djna.debug_load=true

REM use local compilation to debug
REM %JAVA_HOME32%\bin\java.exe -agentlib:jdwp=transport=dt_socket,address=localhost:9019,server=y,suspend=n -server %JAVA_OPTS% -cp A:\dev\scs_source\hvc\scsba\target\classes;A:\dev\scs_source\hvc\scsapi\target\classes;A:\SCSTraining\devba\myba\classes;ba_common_lib/*;extra;extra/*;ba_lib/*;last_lib/*;. %SCS_ARGS% com.thalesgroup.scadasoft.myba.Main

%JAVA_HOME32%\bin\java.exe $$M100CONN1_DEBUG$$ %JAVA_OPTS% -cp ba_lib/*;ba_common_lib/*;last_lib/*;extra;extra/*;. %SCS_ARGS% com.thalesgroup.scadasoft.myba.Main