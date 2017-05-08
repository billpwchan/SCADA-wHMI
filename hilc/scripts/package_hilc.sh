#!/bin/sh

if [ -z "$IS_BUILD_ENV_SET" ]
then
	. $MSYS_HOME/home/admin/.profile
fi

cd $ROOTDIR

tmpDirKit=kit/tmp

# clean
rm -rf kit
mkdir -p $tmpDirKit

# create tree
mkdir -p $tmpDirKit/QATARsoft/$SYSTEM
mkdir -p $tmpDirKit/QATARsoft/$SYSTEM/bin
mkdir -p $tmpDirKit/QATARsoft/$SYSTEM/dll

mkdir -p $tmpDirKit/dat

# copy files
cp binaries/$SCS_VERSION/$MODE_DEBUG/bin/*.exe $tmpDirKit/QATARsoft/$SYSTEM/bin
cp binaries/$SCS_VERSION/$MODE_DEBUG/bin/*.dll $tmpDirKit/QATARsoft/$SYSTEM/dll

cp sighilc/util/*.xml $tmpDirKit/dat
cp sighilc/datcnf/HilcAlarmExt.cnf $tmpDirKit/dat

cp scripts/deploy-hilc.bat $tmpDirKit

 # create kit
version=`grep -A3 "<artifactId>occ-package" ../scada/maven-project/packaging/pom.xml | awk -F "version>" {'print $2'} | awk -F "<" {'print $1'}`
version=`echo $version | sed 's/ //g'`
cd $tmpDirKit
"$SEVENZIPHOME"/7z.exe a -tzip ../hilc-package-$version.zip *
cd -

# clean
cd kit ; rm -rf tmp ; cd -
