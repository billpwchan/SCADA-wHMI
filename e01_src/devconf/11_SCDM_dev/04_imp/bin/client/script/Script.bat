@echo off
REM ##############################################################
REM Script.bat
REM Batch to run jython file 
REM USAGE : Script.bat [option_jython] scriptfile.py [parameters]
REM [option_jython] -i inspect interactively after running script
REM return 0 if success
REM
REM ##############################################################
 
set RETCODE=
 
REM Check option_jython, scriptfile.py, parameters
REM --------------------------------------------------------------
set option_jython=%1
IF !%option_jython%! == !! GOTO USAGE
 
set scriptPy=%2
IF  !%option_jython:~0,2%! == !-i! (
  IF !%scriptPy%! == !! GOTO USAGE
) ELSE (
  set scriptPy=%option_jython%
  set option_jython=
)
 
set ext=%scriptPy:~-3%
IF /I !%ext%! NEQ !.py! (
  @ECHO.
  @ECHO %scriptPy% FILE IS NOT VALID
  @ECHO.
  GOTO USAGE
)
 
IF NOT EXIST %scriptPy% (
  @ECHO.
  @ECHO %scriptPy% FILE DOESN'T EXIST
  @ECHO.
  GOTO USAGE
)
 
set option_jython=
set scriptPy=
set ext=
REM --------------------------------------------------------------
 
REM Set the environment
REM ---------------------------------------
REM %~dp0=CONFIGURATEUR_HOME\script\
set CONFIGURATEUR_HOME=%~dp0
set CONFIGURATEUR_HOME=%CONFIGURATEUR_HOME:~0,-8%
 
cd /d %~dp0
 
if not defined CLIENTNAME set CLIENTNAME=Console
set IDENT=%CLIENTNAME%_%USERNAME%
 
REM set LIB=CONFIGURATEUR_HOME\lib
set LIB=./../lib
 
set LOCALCLASSPATH=%LIB%/configurateurRmi.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurCommon.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/configurateurClient.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jdom.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xml-apis.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xerces.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xalan.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/log4j.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/velocity-1.1.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/antlr-runtime.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/xwingml.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;%LIB%/jython.jar
set LOCALCLASSPATH=%LOCALCLASSPATH%;./../conf/
 
 
set OPTIONS=-Dapplication.home="%CONFIGURATEUR_HOME%" 
set OPTIONS=%OPTIONS% -Dapplication.config="%CONFIGURATEUR_HOME%\conf\configurateurClient.prop"
set OPTIONS=%OPTIONS% -Dlog4j.appender.A1.File="%~n0_%IDENT%.log"
 
"%JAVA_HOME%/bin/java"  -mx512m -ss5000000 -cp "%LOCALCLASSPATH%" %OPTIONS%  org.python.util.jython %*
set RETCODE=%ERRORLEVEL%
 
:OUTPUT
exit /b %RETCODE%
 
:USAGE
@ECHO.
@ECHO Batch to run jython file 
@ECHO.
@ECHO USAGE : %~n0 [option_jython] scriptfile.py [parameters] 
@ECHO.
@ECHO [option_jython] -i inspect interactively after running script
@ECHO.
set RETCODE=1
GOTO OUTPUT 