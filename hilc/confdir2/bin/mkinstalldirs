#!/bin/sh
# mkinstalldirs --- make directory hierarchy
# Author: Noah Friedman <friedman@prep.ai.mit.edu>
# Created: 1993-05-16
# Last modified: 1994-03-25
# Public domain

#echo "argument = $1"
errstatus=0
silent=0
#set -- `getopt "q" $*`
if [ $? != 0 ]
then
    echo Pb... 
    exit 1
fi
#while [ $1 != -- ]
#do
#    case $1 in
#     -q) silent=1;;
#    esac
#    shift
#done
#shift

for file in ${1+"$@"}
do
    set fnord `echo ":$file" | sed -ne 's/^:\//#/;s/^://;s/\// /g;s/^#/\//;p'`
    shift
    pathcomp=
    for d in ${1+"$@"}
    do
	pathcomp="$pathcomp$d"
	case "$pathcomp" in
	 -* ) pathcomp=./$pathcomp ;;
	esac

	if [ ! -d "$pathcomp" ]
	then
	    if [ $silent -ne 1 ]
	    then
		echo "mkdir $pathcomp" 1>&2
	    fi
	    mkdir "$pathcomp" || errstatus=$?
	fi
	pathcomp="$pathcomp/"
    done
done

#exit $errstatus

# mkinstalldirs ends here
