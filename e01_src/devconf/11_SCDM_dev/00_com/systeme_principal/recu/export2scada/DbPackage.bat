@echo off
set cdv_name=%1
set rep_dest=%2
set rep_dest=%rep_dest:/=\%
set loc_locale=%rep_dest%.\..\..\..\..\..\Locale

for /f %%t in ('wmic os get LocalDateTime ^| findstr ^[0-9]') do (
	set ts=%%t
)
set DBDATE=%ts:~2,2%%ts:~4,2%%ts:~6,2%
SET PKGNAME=DB_%DBDATE%_%cdv_name%
SET HV_PKGNAME=HV_%DBDATE%_%cdv_name%
set PACKAGE_LOG=%rep_dest%\DbPackage_%DBDATE%.log

::====Locale
if exist %loc_locale%\alm_cn.po (
	echo.Converting mapping file >> %PACKAGE_LOG%
	tail -c +4 %loc_locale%\alm_cn.po | msgfmt -c -o %loc_locale%\alm_cn.mo - >> %PACKAGE_LOG%
	echo.Start copy Locale >> %PACKAGE_LOG%
	xcopy /c /y /e %loc_locale%\* %rep_dest%\%PKGNAME%\Locale\ >> %PACKAGE_LOG%
)

mkdir %rep_dest%\%PKGNAME% >> %PACKAGE_LOG%
mkdir %rep_dest%\%HV_PKGNAME% >> %PACKAGE_LOG%

for /f %%i in ('dir /ad /b %rep_dest%\Database\xml_*') do (
	for /f "tokens=2* delims=_" %%e in ('echo %%i') do (
		rem e.g. xml+SCADASUBSYSTEM name = "xml_OCCENVCONN", %%e will be OCCENVCONN
		echo Environment Name: %%e  >> %PACKAGE_LOG%
		SETLOCAL EnableDelayedExpansion
		set ENVNAME=%%e
		set /a SUFFIX=!ENVNAME:~3,10!
		ENDLOCAL
		
		echo SUFFIX %SUFFIX% >> %PACKAGE_LOG%
		mkdir %rep_dest%\%PKGNAME%\%%e >> %PACKAGE_LOG%
		::====Database
		echo ########################### >> %PACKAGE_LOG%
		echo Start moving Database >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		mkdir %rep_dest%\%PKGNAME%\%%e\Database >> %PACKAGE_LOG%
		if exist %rep_dest%\Database\xml_%%e (
			xcopy /C /Y /E /I %rep_dest%\Database\xml_%%e %rep_dest%\%PKGNAME%\%%e\Database\xml >> %PACKAGE_LOG%
			rmdir /S /Q %rep_dest%\Database\xml_%%e	 >> %PACKAGE_LOG%
		)
		::====Ev Files
		echo ########################### >> %PACKAGE_LOG%
		echo Start copy dac >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		mkdir %rep_dest%\%PKGNAME%\%%e\dac >> %PACKAGE_LOG%
		
		if exist %rep_dest%\dac_%%e (
		if exist %rep_dest%\dac_%%e\*.dat (
			for /f %%d in ('dir /a /b %rep_dest%\dac_%%e\*.dat') do (
				:: Get line number
				SETLOCAL EnableDelayedExpansion
				set /a tempfilelen=0
				for /f "tokens=1,*" %%l in (%rep_dest%\dac_%%e\%%d) do (
					set /a tempfilelen=!tempfilelen!+1
				)
				echo Evariable Filename = %%d  >> %PACKAGE_LOG%
				:: If line number < 31, empty EV file -> get the EV file from corresponding station
				if !tempfilelen! lss 31 (
					:: Get the station name from the EV filename
					for /f "tokens=1 delims=_" %%g in ('echo %%d') do (
						echo filelen lss 31 >> %PACKAGE_LOG%
						echo Copying %rep_dest%\dac_%%g%SUFFIX%\%%d to %rep_dest%\%PKGNAME%\%%e\dac\ >> %PACKAGE_LOG%
						xcopy /c /y %rep_dest%\dac_%%g%SUFFIX%\%%d %rep_dest%\%PKGNAME%\%%e\dac\ >> %PACKAGE_LOG%
					)
				) 
				:: If line number > 31, non-empty EV file -> get the EV file from its environment
				if !tempfilelen! gtr 30 (
					echo Copying %rep_dest%\dac_%%e\%%d to %rep_dest%\%PKGNAME%\%%e\dac\ >> %PACKAGE_LOG%
					xcopy /c /y %rep_dest%\dac_%%e\%%d %rep_dest%\%PKGNAME%\%%e\dac\ >> %PACKAGE_LOG%
				)
				ENDLOCAL
			)
		)
		) else (
			echo ##Folder %rep_dest%\dac_%%e not exist! >> %PACKAGE_LOG%
		)
		if exist %rep_dest%\dac_%%e\%%e\ScsDacCtrt.cfg (
			xcopy /c /y %rep_dest%\dac_%%e\%%e\ScsDacCtrt.cfg %rep_dest%\%PKGNAME%\%%e\dac\ >> %PACKAGE_LOG%
		) else (
			echo ##File %rep_dest%\dac_%%e\%%e\ScsDacCtrt.cfg not found! >> %PACKAGE_LOG%
		)
		
		rem sed "s/Protocol=mbt/Protocol=mbt.x/ig" %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg > %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg_sed
		rem dos2unix -q %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg_sed >> %PACKAGE_LOG%
		rem xcopy /c /y %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg_sed %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg >> %PACKAGE_LOG%
		rem del %rep_dest%\%PKGNAME%\%%e\dac\ScsDacCtrt.cfg_sed >> %PACKAGE_LOG%
		
		::====Archive
		echo ########################### >> %PACKAGE_LOG%
		echo Start copy Archive >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		mkdir %rep_dest%\%PKGNAME%\%%e\Archives >> %PACKAGE_LOG%
		for /f %%b in ('findstr /m DbAddress %rep_dest%\Archives\DB_%%e\*.cfg 2^>nul:') do (
			mkdir %rep_dest%\%PKGNAME%\%%e\Archives\%%~nb >> %PACKAGE_LOG%
			echo Copying %%b to %rep_dest%\%PKGNAME%\%%e\Archives\%%~nb\ >> %PACKAGE_LOG%
			xcopy /C /Y %%b %rep_dest%\%PKGNAME%\%%e\Archives\%%~nb\ >> %PACKAGE_LOG%
		)
		echo Archive moved >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		::====Archive_CSVs
		echo Start copy Archive_csv >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		mkdir %rep_dest%\%PKGNAME%\%%e\Archive_CSVs
		for /f %%g in ('findstr /m aci %rep_dest%\Archive_CSVs\*.csv 2^>nul:') do (
			echo Copying %%g to %rep_dest%\%PKGNAME%\%%e\Archive_CSVs\ >> %PACKAGE_LOG%
			xcopy /c /y %%g %rep_dest%\%PKGNAME%\%%e\Archive_CSVs\ >> %PACKAGE_LOG%
		)
		echo Archive_CSVs moved >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		
		::====HV2SCS
		echo Start copy HV2SCS >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		mkdir %rep_dest%\%HV_PKGNAME%\hvscs\%%e >> %PACKAGE_LOG%
		if exist %rep_dest%\hvscs\%%e\*.xml (
			echo Copying %rep_dest%\hvscs\%%e\*.xml to %rep_dest%\%HV_PKGNAME%\%%e\hvscs\ >> %PACKAGE_LOG%
			xcopy /c /y %rep_dest%\hvscs\%%e\*.xml %rep_dest%\%HV_PKGNAME%\hvscs\%%e\ >> %PACKAGE_LOG%
		)
		if exist %rep_dest%\Hypervisor_%%e\systemConfiguration\instances\*.xml (
			echo Copying %rep_dest%\Hypervisor_%%e\systemConfiguration\instances\*.xml to %rep_dest%\%HV_PKGNAME%\Hypervisor\ >> %PACKAGE_LOG%
			xcopy /c /y %rep_dest%\Hypervisor_%%e\systemConfiguration\instances\*.xml %rep_dest%\%HV_PKGNAME%\Hypervisor\systemConfiguration\instances\  >> %PACKAGE_LOG%
			echo Copying %rep_dest%\Hypervisor_%%e\systemConfiguration\mapping\*.xml to %rep_dest%\%HV_PKGNAME%\Hypervisor\ >> %PACKAGE_LOG%
			xcopy /c /y %rep_dest%\Hypervisor_%%e\systemConfiguration\mapping\*.xml %rep_dest%\%HV_PKGNAME%\Hypervisor\systemConfiguration\mapping\  >> %PACKAGE_LOG%
		)
		if exist %rep_dest%\Hypervisor\systemConfiguration\instances\areas.xml (
			echo Copying %rep_dest%\Hypervisor\systemConfiguration\instances\areas.xml to %rep_dest%\%HV_PKGNAME%\Hypervisor\ >> %PACKAGE_LOG%
			xcopy /c /y %rep_dest%\Hypervisor\systemConfiguration\instances\areas.xml %rep_dest%\%HV_PKGNAME%\Hypervisor\systemConfiguration\instances\ >> %PACKAGE_LOG%
		)
		if exist %rep_dest%\Hypervisor\systemConfiguration\instances\equipments.xml (
			echo Copying %rep_dest%\Hypervisor\systemConfiguration\instances\equipments.xml to %rep_dest%\%HV_PKGNAME%\Hypervisor\ >> %PACKAGE_LOG%
			xcopy /c /y %rep_dest%\Hypervisor\systemConfiguration\instances\equipments.xml %rep_dest%\%HV_PKGNAME%\Hypervisor\systemConfiguration\instances\ >> %PACKAGE_LOG%
		)
		
		echo HV2SCS moved >> %PACKAGE_LOG%
		echo ########################### >> %PACKAGE_LOG%
		rem #######################
		rem #EPG to be implemented
		rem #######################
		
		
		REM echo starting with tcl script >> %PACKAGE_LOG%
		REM echo %~dp0.\IOGenCaches.tcl >> %rep_dest%\%PKGNAME%\IOGenCaches.log
		REM echo parameter 1 %%eENV >> %rep_dest%\%PKGNAME%\IOGenCaches.log
		REM echo parameter 2 %cdv_name% >> %rep_dest%\%PKGNAME%\IOGenCaches.log
		REM echo parameter 3 %rep_dest%\%PKGNAME%\%%e >> %rep_dest%\%PKGNAME%\IOGenCaches.log
		REM tclsh %~dp0.\IOGenCaches.tcl %%eENV %cdv_name% %rep_dest%\%PKGNAME%\%%e %rep_dest%\%PKGNAME%\%%e >> %rep_dest%\%PKGNAME%\IOGenCaches.log
		
		rem %rep_dest%\%PKGNAME%\IOGenCaches.log
	)
)
7z a -r -ttar %rep_dest%\%HV_PKGNAME%.tar %rep_dest%\%HV_PKGNAME% >> %PACKAGE_LOG%
7z a -r -tgzip %rep_dest%\%HV_PKGNAME%.tar.gz %rep_dest%\%HV_PKGNAME%.tar >> %PACKAGE_LOG%

7z a -r -ttar %rep_dest%\%PKGNAME%.tar %rep_dest%\%PKGNAME% >> %PACKAGE_LOG%
7z a -r -tgzip %rep_dest%\%PKGNAME%.tar.gz %rep_dest%\%PKGNAME%.tar >> %PACKAGE_LOG%

rem copy /V /Y /Z %rep_dest%\%PKGNAME%.tar.gz Z:\ >> %PACKAGE_LOG%

if exist %rep_dest%\%PKGNAME%.tar (
	echo ########################### >> %PACKAGE_LOG%
	echo Remove tar file after gzip >> %PACKAGE_LOG%
	echo ########################### >> %PACKAGE_LOG%
	del %rep_dest%\%PKGNAME%.tar >> %PACKAGE_LOG%
)
if exist %rep_dest%\%PKGNAME% (
	echo ########################### >> %PACKAGE_LOG%
	echo.Remove package folder after zip >> %PACKAGE_LOG%
	echo ########################### >> %PACKAGE_LOG%
	RMDIR /S /Q %rep_dest%\%PKGNAME% >> %PACKAGE_LOG%
)
if exist %rep_dest%\%HV_PKGNAME%.tar (
	echo ########################### >> %PACKAGE_LOG%
	echo Remove system config tar file after gzip >> %PACKAGE_LOG%
	echo ########################### >> %PACKAGE_LOG%
	del %rep_dest%\%HV_PKGNAME%.tar >> %PACKAGE_LOG%
)
if exist %rep_dest%\%HV_PKGNAME% (
	echo ########################### >> %PACKAGE_LOG%
	echo.Remove system config package folder after zip >> %PACKAGE_LOG%
	echo ########################### >> %PACKAGE_LOG%
	RMDIR /S /Q %rep_dest%\%HV_PKGNAME% >> %PACKAGE_LOG%
)

echo DbPackage is done

exit 0