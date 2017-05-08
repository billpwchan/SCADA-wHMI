#!/bin/sh
# --------------------------------------------------------------- -*- Ksh -*-
#							GeniTools source file
# File:          mkdevdirs.sh
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
# mkdevdirs -- build a development tree directory
#
# Usage: mkdevdirs [options] [reference root dir]
# --help       Issue this usage message.
#
# --devdir=DIR Store Development tree base directory in DIR.
#              Default is '.'.
# --extract    Run co in order to extract files from RCS repositories.
#
# Options may be abbreviated to their first letter. Option equal sign may be
# replaced with a space.
#
# ---------------------------------------------------------------------------
#

#
# Set variables default
command=`basename $0 .sh`
rootdir=`pwd`
rootdir_defined=0
devdir=$rootdir
extract=0

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
Usage: mkdevdirs [options] [reference root dir]
--help       Issue this usage message.

--devdir=DIR Store Development tree base directory in DIR. Default is
             '.'.
--extract    Run co in order to extract files from RCS repositories.
             Default is do nothing.

Options may be abbreviated to their first letter. Option equal sign may be
replaced with a space.
EOF_USAGE
      exit 0 ;;

     -devdir | --devdir | --d | -d)
	ac_prev=devdir ;;
     -devdir=* | --devdir=* | --d=* | -d=*)
	devdir="$ac_optarg" ;;

     -extract | --extract | --e | -e)
	extract=1 ;;

     -*)
        { cat 1>&2 << EOF_INVALID_OPTION
$command: error: $ac_option: invalid option; use --help to show usage
EOF_INVALID_OPTION
           exit 1; } ;;

     *) if [ $rootdir_defined -ne 0 ]
	then
	    { cat 1>&2 << EOF_INVALID_ARGUMENT
$command: error: $ac_option: invalid argument
EOF_INVALID_ARGUMENT
	      exit 1; }
	fi
	rootdir="$ac_option"
	rootdir_defined=1 ;;
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
if [ ! -d "$rootdir" -o "$rootdir" = "$devdir" ]
then
    { cat 1>&2 << EOF_INVALID_DIRECTORY
$command: $rootdir: invalid directory
EOF_INVALID_DIRECTORY
      exit 1; }
fi

# Do job...
rootdir_prefix=`dirname $rootdir`
rootdir_tree=`find $rootdir -type d -print`
for root_entry in $rootdir_tree
do
    dev_entry=`echo $root_entry|sed -e "s|$rootdir_prefix|$devdir|g"`

    if [ "`basename $dev_entry`" = "RCS" ]
    then
	if [ -d $dev_entry -o -h $dev_entry ]
	then
	    echo "purging \"$dev_entry\"..."
	    rm -f $dev_entry
	fi
	echo "linking \"$dev_entry\""
	echo "with    \"$root_entry\"..."
	ln -s $root_entry $dev_entry
	if [ $extract -eq 1 ]
	then
	    (cd `dirname $dev_entry` ; co RCS/*,v)
	fi
    else
	if [ ! -d $dev_entry ]
	then
	    echo "creating \"$dev_entry\"..."
	    mkdir $dev_entry
	fi
    fi
done
