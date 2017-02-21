SET resource_source=C:\SCSTraining\scspaths\OCCENVWEB1\apache-tomcat-8.0.23\conf
SET resource_dest=%SOURCE_BASE_F01%\workspace.project-conf\project-conf\src\main

SET project_source=project
SET project_dest=project

SET e01_folder=e01-eqp-current
SET e02_folder=e02-resources-current
SET e03_folder=e03-data-model-current
SET e04_folder=e04-system-conf-current
SET p01_folder=p01-func-current
SET f01_folder=f01
SET f02_folder=f02

REM Symbol Status Computer Properties
SET SOURCE=%resource_source%\%e01_folder%\projectConf 
SET DESC=%resource_dest%\projectConf\%project_dest%
XCOPY /I /Y %SOURCE% %DESC%

REM Situation View
SET SOURCE=%resource_source%\%e01_folder%\situationView
SET DESC=%resource_dest%\widgets\%project_dest%\situationView
XCOPY /I /Y %SOURCE% %DESC%

REM CSS for the Panel
SET SOURCE=%resource_source%\%e02_folder%\%project_source%\css
SET DESC=%resource_dest%\resources\css\%project_dest%
XCOPY /I /Y %SOURCE% %DESC%

REM Image for the GDG Panel
SET SOURCE=%resource_source%\%e02_folder%\%project_source%\img\gdg
SET DESC=%resource_dest%\resources\Images\%project_dest%\gdg
XCOPY /I /Y %SOURCE% %DESC%

REM Translation Properties
SET SOURCE=%resource_source%\%p01_folder%\locale\%project_source%
SET DESC=%resource_dest%\locale\%project_dest%
XCOPY /I /Y %SOURCE% %DESC%

REM UIWidgetGeneric Configuration
SET SOURCE=%resource_source%\%p01_folder%\%project_source%\config
SET DESC=%resource_dest%\widgets\%project_dest%\config\UIWidgetGeneric
XCOPY /I /Y %SOURCE% %DESC%

REM JavaScripts
SET SOURCE=%resource_source%\%p01_folder%\%project_source%\js
SET DESC=%resource_dest%\resources\js\%project_dest%
XCOPY /I /Y %SOURCE% %DESC%

REM GDG Status Computer
SET SOURCE=%resource_source%\%p01_folder%\projectConf
SET DESC=%resource_dest%\projectConf\%project_dest%
XCOPY /I /Y %SOURCE% %DESC%

REM GDG definlation
SET SOURCE=%resource_source%\%p01_folder%\widgets\genericDataGrid
SET DESC=%resource_dest%\widgets\%project_dest%\genericDataGrid
XCOPY /I /Y %SOURCE% %DESC%

CALL LoadPath.bat
CD ..\f01\workspace.project-conf\project-conf
mvn clean install