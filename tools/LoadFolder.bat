@echo off

SET CUR_PATH=%~dp0
SET CUR_PATH=%CUR_PATH:~0,-1%

SET sp_ver=.v123
SET war_ver=1.2.3

SET sp_wrapper=workspace.wrapper%sp_ver%
echo Folder Path for sp_wrapper is [%sp_wrapper%]

SET sp_whmi=workspace.whmi
echo Folder Path for sp_whmi is [%sp_whmi%]

SET sp_webapp=workspace.webapp%sp_ver%
echo Folder Path for sp_webapp is [%sp_webapp%]

SET scstraning_loc=C:\V1_2_3\scstraining\
echo Folder Path for scstraning_loc is [%scstraning_loc%]