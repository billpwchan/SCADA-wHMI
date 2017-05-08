#!/bin/sh
# mkrelease --- make Animator 96 release
#
command=`basename $0`

if [ "${ROOTDIR-unset}" = "unset" ]
then
    echo "${command}: ROOTDIR: unknown environment variable"
    exit 1
fi

if [ $# -ne 2 ]
then
    echo "usage: $command <release symbol> <next base release version>"
    exit 2
else
    releaseSymbol=$1
    releaseVersion=$2
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
	echo "${command}: aborting: $directory contains locked files"
	exit 3
    fi
done

echo "working..."
for directory in $directoryList
do
    cd $directory
    rcs -sRel -n${releaseSymbol}: RCS/*
    co -l RCS/*
    ci -u -m"previous release stamped with '$releaseSymbol'"\
      -f$releaseVersion RCS/*
done

cd $workingDir
