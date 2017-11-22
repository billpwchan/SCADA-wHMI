echo off

rmdir /S /Q log
mkdir log

set LOG_FILE="log\outGenerateEpg.log"
set LOG_FILE_ERR="log\outGenerateEpg_err.log"
echo " " > %LOG_FILE_ERR%

REM TRACES des paramètres dans un fichier texte
REM -------------------------------------------
echo 0=%0 > %LOG_FILE%
echo 1=%1 >> %LOG_FILE%
echo 2=%2 >> %LOG_FILE%

REM ------------------------------------------------
if {%1} == {} goto :PRINT_USAGE

REM ----------- ENV for EPG ------------------------
set NOM_VDC=%2
set rep_dest=%1\%NOM_VDC%

if {%SCSHOME%} == {} goto :SCSHOME_NOT_SET
set PATH=%SCSHOME%\xp-msvc10\dll;%SCSHOME%\xp-msvc10\dll\rwav;%SCSHOME%\xp-msvc10\dll\ilog;%SCSHOME%\ACE_Wrappers\lib;%PATH%
set EPGBINHOME=%SCSHOME%\xp-msvc10\bin

set EPGCONFDIR=%1\..\resource\epg
set EPGROOT=%rep_dest%\epg

set EPGDAT=%EPGCONFDIR%\dat
set EPGSRC=%EPGROOT%\src
set EPGGEN=%EPGROOT%\gen

rmdir /S /Q %EPGGEN%
mkdir %EPGGEN%

REM ------------------------------------------------

echo EPGROOT    = %EPGROOT% >> %LOG_FILE%
echo EPGBINHOME = %EPGBINHOME% >> %LOG_FILE%
echo EPGDAT     = %EPGDAT% >> %LOG_FILE%
echo EPGSRC     = %EPGSRC% >> %LOG_FILE%
echo EPGGEN     = %EPGGEN% >> %LOG_FILE%

echo "======================================================" >> %LOG_FILE%
dir /B "%EPGSRC%\*.epg" >> %LOG_FILE%
echo "======================================================" >> %LOG_FILE%
rem 
FOR /F %%i IN ('dir /B "%EPGSRC%\*.epg"') DO start /b /wait %EPGBINHOME%\scsepg.exe -f %%i -i %EPGCONFDIR%\umcd_epg.ini 1>> %LOG_FILE% 2>> %LOG_FILE_ERR%

echo "======================================================" >> %LOG_FILE%
echo "%rep_dest%\endEpgGeneration" >> %LOG_FILE%
echo "======================================================" >> %LOG_FILE%
echo "fin" >  %rep_dest%\endEpgGeneration

if errorlevel 1 goto :ERREUR
echo "Generation of Equipment Panel successfully ended"
goto :END

REM --------------------- Sortie en erreur de scsepg ---------------------
:ERREUR
echo "Error during ScsEpg execution" >&2
exit 1

REM -------------------- Usage de la commande ---------------------------------
:PRINT_USAGE
	echo Usage:
	echo %0 ^<generation path^> ^<vdc generation name^>
    echo Example : %0 \\142.153.1.100\BD_ANIM\vdc_cible VDC_RE_4
goto :ERREUR

:SCSHOME_NOT_SET
echo "The environment variable SCSHOME is not set!"

:END
