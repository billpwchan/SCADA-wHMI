#!/bin/sh
# --------------------------------------------------------------- -*- Ksh -*-
#							GeniTools source file
# File:          mktags.sh
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
# mktags - an Emacs TAGS file builder
#
# Usage: mktags [options] [base directory]
# Options:
# --help              Issue this usage message.
#
# --output=TAGSFILE Use TAGSFILE as output file. Default is
#                   $ROOTDIR/TAGS.
# --keep=REGEXP     Use REXEXP to select subdirectories to browse.
#                   Default is all.
# --suffixes=REGEXP Use REGEXP to select files to tag.
#                   Default selects C/C++ source and headers.
#
# Options may be abbreviated to their first letter. Option equal sign may be
# replaced with a space.
#
# File involved: mktags.sh  bourne shell script
#
# ---------------------------------------------------------------------------
# 

#
# Set variables default
command=`basename $0 .sh`
directory=$ROOTDIR
if [ -z "$directory" ]
then
    directory="."
fi
directory_defined=0
output="$directory/TAGS"
subdirs='.*'
suffixes='((\.(C|(c(c|pp|xx|\+\+)?)))|(\.(H|(h(h|pp|xx|\+\+)?))))$'

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
Usage: mktags [options] [base directory]
--help       Issue this usage message.

--output=TAGSFILE Use TAGSFILE as output file. Default is
                  $ROOTDIR/TAGS.
--keep=REGEXP     Use REXEXP to select subdirectories to browse.
                  Default is all.
--suffixes=REGEXP Use REGEXP to select files to tag.
                  Default selects C/C++ source and headers.

Options may be abbreviated to their first letter. Option equal sign may be
replaced with a space.
EOF_USAGE
      exit 0 ;;

     -keep | --keep | --k | -k)
	ac_prev=subdirs ;;
     -keep=* | --keep=* | --k=* | -k=*)
	subdirs="$ac_optarg" ;;

     -output | --output | --o | -o)
	ac_prev=output ;;
     -output=* | --output=* | --o=* | -o=*)
	output="$ac_optarg" ;;

     -suffixes | --suffixes | --s | -s)
	ac_prev=suffixes ;;
     -suffixes=* | --suffixes=* | --s=* | -s=*)
	suffixes="$ac_optarg" ;;

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
$command: $rootdir: invalid directory
EOF_INVALID_DIRECTORY
      exit 1; }
fi

# Do job...
rm -f $output
etags -C -a -o $output `find $directory -print | \
  egrep "$subdirs" | egrep "$suffixes"`
