@ECHO OFF

call LoadPath.bat

IF [%1] == [] SET v_strdt=%strdt%
IF [%v_strdt%] == [] SET v_strdt=%1

SET LASTCOMPILATION_FILENAME=LastCompilation.java
SET LASTCOMPILATION_PATH=%SOURCE_BASE%/%sp_whmi%/uiview-uiviewmgr\src\main\java\com\thalesgroup\scadagen\whmi\uiview\uiviewmgr\client\

echo.%LASTCOMPILATION_PATH%\%LASTCOMPILATION_FILENAME%
(
	echo.package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;
	echo.public class LastCompilation {
	echo.	public static String get(^) {
	echo.		return "%v_strdt%";
	echo.	}
	echo.}
) > %LASTCOMPILATION_PATH%\%LASTCOMPILATION_FILENAME%