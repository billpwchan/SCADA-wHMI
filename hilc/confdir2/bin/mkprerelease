#!/bin/sh
# mkprerelease --- make Animator 96 release
#
command=`basename $0`

if [ "${ROOTDIR-unset}" = "unset" ]
then
    echo "${command}: ROOTDIR: unknown environment variable"
    exit 1
fi

echo "building directory list..."
directoryList=`find $ROOTDIR -name RCS -print | sed -ne 's|/RCS||;p'`

echo "verifying file locks..."
workingDir=`pwd`
for directory in $directoryList
do
    cd $directory
    rlocks
    if [ $? -ne 0 ]
    then
	echo " <!> $directory contains locked files"
    fi
done

echo "working..."
for directory in $directoryList
do
    cd $directory
    co RCS/*
done

cd $workingDir
