set TARGET_DIR=D:\ISCS_1.5.0
copy binaries\5.3.1\debug\bin\*.dll %TARGET_DIR%\softs\QATARsoft\xp-msvc10\dll
copy binaries\5.3.1\debug\bin\*.exe %TARGET_DIR%\softs\QATARsoft\xp-msvc10\bin
copy binaries\5.3.1\debug\bin\*.pdb %TARGET_DIR%\softs\QATARsoft\xp-msvc10\bin
copy sighilc\util\*.xml %TARGET_DIR%\runtime\SRV2\dat
copy sighilc\datcnf\HilcAlarmExt.cnf %TARGET_DIR%\runtime\SRV2\dat
