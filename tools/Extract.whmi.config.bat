@echo off

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LOG_FILE=%LOG_HOME%\build.%sp_webapp%.%v_strdt%.log

SET DESTINATION_BASE=%TOOLS_BASE%\..\config.extracted

RMDIR /S /Q %DESTINATION_BASE%\e02
RMDIR /S /Q %DESTINATION_BASE%\e01
RMDIR /S /Q %DESTINATION_BASE%\p01

IF NOT EXIST "%DESTINATION_BASE%\e02" MKDIR %DESTINATION_BASE%\e02
IF NOT EXIST "%DESTINATION_BASE%\e01" MKDIR %DESTINATION_BASE%\e01
IF NOT EXIST "%DESTINATION_BASE%\p01" MKDIR %DESTINATION_BASE%\p01

REM SET CONFIG_BASE=%scstraning_loc%\scspaths\WEBAPP\apache-tomcat%TOMCAT_VER%

SET CONFIG_BASE="R:\1166B\configShouldBe"

REM e02 SVG Background
SET e02_svg_background=conf\hypervisor-configuration\widgets\situationView\views\images
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_svg_background%" MKDIR %DESTINATION_BASE%\e02\%e02_svg_background%
COPY /Y "%CONFIG_BASE%\%e02_svg_background%\*.svg" %DESTINATION_BASE%\e02\%e02_svg_background%

REM e02 Symbols (png)
SET e02_symbols_png=conf\hypervisor-configuration\widgets\situationView\bricks
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_symbols_png%" MKDIR %DESTINATION_BASE%\e02\%e02_symbols_png%
COPY /Y "%CONFIG_BASE%\%e02_symbols_png%\*.svg" %DESTINATION_BASE%\e02\%e02_symbols_png%

REM e01 Situation Views
SET e01_situation_views=conf\hypervisor-configuration\widgets\situationView\views
IF NOT EXIST "%DESTINATION_BASE%\e01\%e01_situation_views%" MKDIR %DESTINATION_BASE%\e01\%e01_situation_views%
COPY /Y "%CONFIG_BASE%\%e01_situation_views%\*.xml" %DESTINATION_BASE%\e01\%e01_situation_views%

SET e02_situation_views_symbols=conf\hypervisor-configuration\widgets\situationView\symbols
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_situation_views_symbols%" MKDIR %DESTINATION_BASE%\e02\%e02_situation_views_symbols%
COPY /Y "%CONFIG_BASE%\%e02_situation_views_symbols%\*.xml" %DESTINATION_BASE%\e02\%e02_situation_views_symbols%

SET e02_situation_views_brick_definitions=conf\hypervisor-configuration\widgets\situationView\
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_situation_views_brick_definitions%" MKDIR %DESTINATION_BASE%\e02\%e02_situation_views_brick_definitions%
COPY /Y "%CONFIG_BASE%\%e02_situation_views_brick_definitions%\brick_definitions.xml" %DESTINATION_BASE%\e02\%e02_situation_views_brick_definitions%

REM p01 GenericDataGrid
SET p01_genericdatagrid=conf\hypervisor-configuration\widgets\genericDataGrid
IF NOT EXIST "%DESTINATION_BASE%\p01\%p01_genericdatagrid%" MKDIR %DESTINATION_BASE%\p01\%p01_genericdatagrid%
COPY /Y "%CONFIG_BASE%\%p01_genericdatagrid%\*.xml" %DESTINATION_BASE%\p01\%p01_genericdatagrid%

REM p01 GenericWidget and GenericLayout
SET p01_generic_widget_layout=conf\project\resources\config
IF NOT EXIST "%DESTINATION_BASE%\p01\%p01_generic_widget_layout%" MKDIR %DESTINATION_BASE%\p01\%p01_generic_widget_layout%
XCOPY /Y /S /I "%CONFIG_BASE%\%p01_generic_widget_layout%\*" %DESTINATION_BASE%\p01\%p01_generic_widget_layout%

REM p01 Locale
SET p01_locale=conf\hypervisor-configuration\locale\project
IF NOT EXIST "%DESTINATION_BASE%\p01\%p01_locale%" MKDIR %DESTINATION_BASE%\p01\%p01_locale%
COPY /Y "%CONFIG_BASE%\%p01_locale%\*.properties" %DESTINATION_BASE%\p01\%p01_locale%
 
REM e02 css (project)
SET e02_css=conf\project\resources\css
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_css%" MKDIR %DESTINATION_BASE%\e02\%e02_css%
COPY /Y "%CONFIG_BASE%\%e02_css%\*.css" %DESTINATION_BASE%\e02\%e02_css%

REM e02 css (mwt override)
SET e02_css_mwt=conf\hypervisor-configuration\resources\css
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_css_mwt%" MKDIR %DESTINATION_BASE%\e02\%e02_css_mwt%
COPY /Y "%CONFIG_BASE%\%e02_css_mwt%\*.css" %DESTINATION_BASE%\e02\%e02_css_mwt%

REM e02 img
SET e02_img=conf\project\resources\img
IF NOT EXIST "%DESTINATION_BASE%\e02\%e02_img%" MKDIR %DESTINATION_BASE%\e02\%e02_img%
XCOPY /Y /S /I "%CONFIG_BASE%\%e02_img%\*" %DESTINATION_BASE%\e02\%e02_img%