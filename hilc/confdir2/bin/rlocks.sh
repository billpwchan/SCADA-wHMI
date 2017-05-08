#!/bin/sh
# --------------------------------------------------------------- -*- Ksh -*-
#							GeniTools source file
# File:          rlocks.sh
# Author:        Dominique FAURE
# Maintainer:    
# Created:       December 1996
# Version:       
# Last Modified: 
# Keywords:      
# ---------------------------------------------------------------------------
#  Copyright (C) 1996 by GENIGRAPH.
#  All Rights Reserved.
#
#  N O T I C E
#
#  THIS MATERIAL IS CONSIDERED A TRADE SECRET BY GENIGRAPH.
#  UNAUTHORIZED ACCESS, USE, REPRODUCTION OR DISTRIBUTION IS PROHIBITED.
# ---------------------------------------------------------------------------
#
# rlocks -- show files locked under RCS in directories
#
# Usage: rlocks [options] [directory]
# Options:
# --help        Issue this usage message.
#
# --lock=USER   Output file locked by USER specified only
#
# --banner      No banner output. Default is yes.
# --repository  Repository name output rather than file name (default).
# --user        No user locking output. Default is yes.
# --version     No version number output. Default is yes.
#
# Default directory is '.'
#
# Options may be abbreviated to their first letter. Option equal sign may be
# replaced with a space.
#
# ---------------------------------------------------------------------------

#
# Set variables default
if [ "`uname`" = "SunOS" ]
then
    awkCommand=nawk
else
    awkCommand=awk
fi
awkProg=$0.awk
command=`basename $0 .sh`
bannerFlag=1
repositoryFlag=0
fileFlag=1
versionFlag=1
userFlag=1
user=""
directory="."
directory_defined=0

# Parse command line
ac_prev=
for ac_option
do
    # If the previous option needs an argument, assign it.
    if [ -n "$ac_prev" ]
    then
	eval "$ac_prev=\$ac_option"
	ac_prev=
	continue
    fi
    # Parse options
    case "$ac_option" in
     -*=*) ac_optarg=`echo "$ac_option" | sed 's/[-_a-zA-Z0-9]*=//'` ;;
     *) ac_optarg= ;;
    esac
    case "$ac_option" in
     -help | --help | --h | -h)
	cat << EOF_USAGE
Usage: rlocks [options] [directory]
--help        Issue this usage message.

--lock=USER   Output file locked by USER specified only

--banner      No banner output. Default is yes.
--repository  Repository name output rather than file name (default).
--user        No user locking output. Default is yes.
--version     No version number output. Default is yes.

Default directory is '.'

Options may be abbreviated to their first letter. Option equal sign may be
replaced with a space.
EOF_USAGE
        exit 0 ;;

     -banner | --banner | --b | -b)
	bannerFlag=0;;

     -lock | --lock | --l | -l)
	ac_prev=user ;;
     -lock=* | --lock=* | --l=* | -l=*)
	user="$ac_optarg" ;;

     -repository | --repository | --r | -r)
	repositoryFlag=1
	fileFlag=0 ;;

     -version | --version | --v | -v)
	versionFlag=0 ;;

     -user | --user | --u | -u)
	userFlag=0 ;;

     -*)
        { cat 1>&2 << EOF_INVALID_OPTION
$command: error: $ac_option: invalid option; use --help to show usage
EOF_INVALID_OPTION
           exit 1; } ;;
     *) if [ $directory_defined -ne 0 ]
	then
	    { cat 1>&2 << EOF_INVALID_ARGUMENT
$command: error: $ac_option: invalid argument
EOF_INVALID_ARGUMENT
	      exit 1; }
	fi
	directory="$ac_option"
	directory_defined=1 ;;
    esac
done

# Check option validity
if [ -n "$ac_prev" ]
then
    { cat 1>&2 << EOF_MISSING_ARGUMENT
$command: error: missing argument to --$ac_prev
EOF_MISSING_ARGUMENT
      exit 1; }
fi

# Check directory
if [ ! -d "$directory" ]
then
    { cat 1>&2 << EOF_INVALID_DIRECTORY
$command: $directory: invalid directory
EOF_INVALID_DIRECTORY
      exit 1; }
fi

# Execute query
#rlog -L -h `find $directory -name RCS -print | sed -ne 's/$/\/\*,v/;p'` | $awkCommand -f $awkProg \
#  bannerFlag=$bannerFlag \
#  repositoryFlag=$repositoryFlag \
#  fileFlag=$fileFlag \
#  versionFlag=$versionFlag \
#  userFlag=$userFlag \
#  $user
filename=/tmp/rlocks$$
for i in `find $directory -name RCS -print | sed -ne 's/$/\/\*,v/;p'`; do
  rlog -L -h $i >>$filename
done
cat $filename | $awkCommand -f $awkProg \
    bannerFlag=$bannerFlag \
    repositoryFlag=$repositoryFlag \
    fileFlag=$fileFlag \
    versionFlag=$versionFlag \
    userFlag=$userFlag \
    $user
rm $filename
