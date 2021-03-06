call:%*
exit/b

:EXTRACT_E
ECHO. EXTRACT_E
SET "PATH_IN=%~1"
SET "PATH_OUT=%~2"

ECHO. PATH_IN=%PATH_IN%
ECHO. PATH_OUT=%PATH_OUT%

ECHO. Extract
%PATH_7Z_BIN% e -aoa %PATH_IN% -o%PATH_OUT%
goto :eof

:EXTRACT_X
ECHO. EXTRACT

SET "PATH_IN=%~1"
SET "PATH_OUT=%~2"

ECHO. PATH_IN=%PATH_IN%
ECHO. PATH_OUT=%PATH_OUT%

ECHO. Extract
%PATH_7Z_BIN% x -aoa %PATH_IN% -o%PATH_OUT%
goto :eof

:TAR_GZIP
SET "PATH_IN=%~2"
SET "PATH_OUT=%~1"
SET "EXCLUDE_LIST=%~3"
SET EXTENSION_TAR=.tar
SET EXTENSION_GZIP=%EXTENSION_TAR%.gz
SET PATH_OUT_TAR=%PATH_OUT%%EXTENSION_TAR%
SET PATH_OUT_GRZIP=%PATH_OUT%%EXTENSION_GZIP%

ECHO. PATH_IN=%PATH_IN%
ECHO. PATH_OUT=%PATH_OUT%
ECHO. PATH_OUT_TAR=%PATH_OUT_TAR%
ECHO. PATH_OUT_GRZIP=%PATH_OUT_GRZIP%
%PATH_7Z_BIN% a -ttar %PATH_OUT_TAR% %PATH_IN% %EXCLUDE_LIST%
%PATH_7Z_BIN% a -tgzip %PATH_OUT_GRZIP% %PATH_OUT_TAR%
IF EXIST %PATH_OUT_TAR% ( DEL /s /q %PATH_OUT_TAR% )
exit /b

:ZIP
SET "PATH_IN=%~2"
SET "PATH_OUT=%~1"
SET "EXCLUDE_LIST=%~3"
SET EXTENSION_ZIP=.zip
SET PATH_OUT_ZIP=%PATH_OUT%%EXTENSION_ZIP%

ECHO. PATH_IN=%PATH_IN%
ECHO. PATH_OUT=%PATH_OUT%
ECHO. PATH_OUT_ZIP=%PATH_OUT_ZIP%
%PATH_7Z_BIN% a -tzip %PATH_OUT_ZIP% %PATH_IN% %EXCLUDE_LIST%
exit /b