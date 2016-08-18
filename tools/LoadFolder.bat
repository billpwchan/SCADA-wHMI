@echo off

SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%

SET sp_ver=
SET war_ver=1.2.5
SET TOMCAT_VER=-8.0.23

SET sp_wrapper=workspace.wrapper%sp_ver%
echo Folder Path for sp_wrapper is [%sp_wrapper%]

SET sp_integration=workspace.integration%sp_ver%
echo Folder Path for sp_integration is [%sp_integration%]

SET sp_uiwidget=workspace.whmi.uiwidget%sp_ver%
echo Folder Path for sp_uiwidget is [%sp_uiwidget%]

SET sp_whmi=workspace.whmi
echo Folder Path for sp_whmi is [%sp_whmi%]

SET sp_webapp=..\f02\workspace.webapp%sp_ver%
SET sp_webapp_name=workspace.webapp%sp_ver%
echo Folder Path for sp_webapp is [%sp_webapp%]
echo Folder Path for sp_webapp_name is [%sp_webapp_name%]

SET sp_webapp_func=workspace.webapp-func%sp_ver%
SET sp_webapp_func_name=workspace.webapp-func%sp_ver%
echo Folder Path for sp_webapp_func is [%sp_webapp_func%]
echo Folder Path for sp_webapp_func_name is [%sp_webapp_func_name%]

SET scstraning_loc=C:\BuildPlatform\SCSTraining\
echo Folder Path for scstraning_loc is [%scstraning_loc%]