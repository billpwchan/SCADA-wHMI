#!/bin/sh
# rcsapply --- apply a command to a RCS tree
#
command=`basename $0`
if [ $# -ne 2 ]
then
    echo "usage: $command <baseDirectory> <subCommand>"
    echo "subCommand must be quoted if needing parameters"
    exit 1
fi
baseDirectory=$1
if [ "$baseDirectory" = "." ]
then
    baseDirectory=`pwd`
fi
subCommand=$2

echo "building directory list..."
directoryList=`find $baseDirectory -name RCS -print | sed -ne 's|/RCS||;p'`
echo "working..."
for directory in $directoryList
do
    echo "processing $directory"
    cd $directory
    $subCommand
done
