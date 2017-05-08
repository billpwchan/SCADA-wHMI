#!/bin/sh
# --------------------------------------------------------------- -*- Ksh -*-
#							GeniTools source file
# File:          mkmf.sh
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
# mkmf -- a Makefile generator
#
# mkmf generates a Makefile based on source files found in current
# directory, from a template (selected with --library option),
# replacing special template tags %DATE%, %ID%, %TARGET% and %OBJECTS%
# respectively with current date, username, command line target and
# objects list deducted from source parsed. Dependencies are generated
# by preprocessor invocation and result parsing, then appended to
# generated Makefile.
#
# Usage: mkmf [options]
# Options:
# --help                      Issue this usage message.
# 
# --cppflags=CPPFLAGS         Extra flags needed for preprocessing.
# --datadir=DIR               Use DIR to find Makefile templates.
#                             Default is mkmf directory.
# --excludes=CPPSEARCHPATHES  Header search pathes as used by CPP
#                             (useful to compile but excluded from
#                              dependencies list). Default is
#                             "-I/usr/include/sys".
# --filetrace=FILE            Use FILE to keep trace of CPP results.
# --includes=CPPSEARCHPATHES  Header search pathes as used by CPP
#                             (useful to compile and included in
#                              dependencies list). Default is
#                             "-I/usr/include".
# --library                   Generate library vs executable Makefile
#                             (default).
# --output=MAKEFILE           Use MAKEFILE as output file. Default is
#                             Makefile.
# --preprocessor=CPP          Use CPP as preprocessor invocation.
#                             Default is "cc -E".
# --target=TARGET             Use TARGET as Makefile target
#                             name. Default is "a.out"
# --srcextradir=CPPSEARCHPATHES Source search pathes used to find more
#                             source file to include in dependency list
#
# Options may be abbreviated to their first letter. Option equal sign may be
# replaced with a space.
#
# The --library option select which Makefile template will be used
# (mkmf.prg for executable, mkmf.lib for library).
#
# Example of command line invocation:
#
# mkmf -p "CC -E" -i "-I../myincludedir1 -I../myincludedir2" -e
# "-I/usr/include -I/usr/include/sys" -l -t libmylibrary.a
#
# File involved: mkmf       bourne shell script
#                mkmf.awk   awk script
#                mkmf.prg   executable template Makefile
#                mkmf.lib   library template Makefile
#
# ---------------------------------------------------------------------------
#
command=`basename $0 .scs.sh`
command_dir=`dirname $0`

# Regexp to extract preprocessor dependency lines
egrep_regexp='^#(line)?[ \t]?[0-9][0-9]*'

# Command to extract dependencies from dependencylines
if [ "`uname`" = "SunOS" ]
then
    awk_command=nawk
else
    awk_command=awk
fi
awk_command="$awk_command -f $command_dir/$command.awk"

# Predefined values for options
if [ "$SYSTEMFAMILY" = "win32" ]
then	
    preprocessor="cl.exe /E"
else
    preprocessor="cc -E"
    includes="-I/usr/include"
    excludes="-I/usr/include/sys"
fi
cppflags=
filetrace=
datadir=`dirname $0`
library=0
target_defined=0
target="a.out"
output="Makefile"
srcextradir=

#echo "preprocessor = $preprocessor"

# Parse command line. No arguments defaults to --help option
if [ $# -eq 0 ]
then
    set x "--help"
    shift
fi

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
Usage: $command [options]
Options:
--help                      Issue this usage message.

--cppflags=CPPFLAGS         Extra flags needed for preprocessing.
--datadir=DIR               Use DIR to find Makefile templates.
                            Default is mkmf directory.
--excludes=CPPSEARCHPATHES  Header search pathes as used by CPP
                            (useful to compile but excluded from
                             dependencies list). Default is
                            "$excludes".
--filetrace=FILE            Use FILE to keep trace of CPP results.
--includes=CPPSEARCHPATHES  Header search pathes as used by CPP
                            (useful to compile and included in
                             dependencies list). Default is
                            "$includes".
--library                   Generate library vs executable Makefile
                            (default).
--output=MAKEFILE           Use MAKEFILE as output file. Default is
                            "$output".
--preprocessor=CPP          Use CPP as preprocessor invocation.
                            Default is "cc -E".
--target=TARGET             Use TARGET as Makefile target
                            name. Default is "$target"
--srcextradir=CPPSEARCHPATHES Source search pathes used to find more
                             source files to include in dependency list.

Options may be abbreviated to their first letter. Option equal sign
may be replaced with a space.
EOF_USAGE
	exit 0 ;;

     -target | --target | --t | -t)
	ac_prev=target ;;
     -target=* | --target=* | --t=* | -t=*)
	target="$ac_optarg" ;;

     -output | --output | --o | -o)
	ac_prev=output ;;
     -output=* | --output=* | --o=* | -o=*)
	output="$ac_optarg" ;;

     -preprocessor | --preprocessor | --p | -p)
	ac_prev=preprocessor ;;
     -preprocessor=* | --preprocessor=* | --p=* | -p=*)
	preprocessor="$ac_optarg" ;;

     -cppflags | --cppflags | --c | -c)
	ac_prev=cppflags ;;
     -cppflags=* | --cppflags=* | --c=* | -c=*)
	cppflags="$ac_optarg" ;;

     -includes | --includes | --i | -i)
	ac_prev=includes ;;
     -includes=* | --includes=* | --i=* | -i=*)
	includes="$ac_optarg" ;;

     -excludes | --excludes | --e | -e)
	ac_prev=excludes ;;
     -excludes=* | --excludes=* | --e=* | -e=*)
	excludes="$ac_optarg" ;;

     -filetrace | --filetrace | --f | -f)
	ac_prev=filetrace ;;
     -filetrace=* | --filetrace=* | --f=* | -f=*)
	filetrace="$ac_optarg" ;;

     -library | --library | -l | --l)
        library=1 ;;

     -datadir | --datadir | --d | -d)
	ac_prev=datadir ;;
     -datadir=* | --datadir=* | --d=* | -d=*)
        datadir="$ac_optarg" ;;

     -srcextradir | --srcextradir | --s | -s)
    ac_prev=srcextradir ;;
     -srcextradir=* | --srcextradir=* | --s=* | -s=*)
        srcextradir="ac_optarg" ;;

     -*)
	{ cat 1>&2 << EOF_INVALID_OPTION
$command: error: $ac_option: invalid option; use --help to show usage
EOF_INVALID_OPTION
          exit 1; } ;;

     *) if [ $target_defined -ne 0 ]
	then
	    { cat 1>&2 << EOF_INVALID_ARGUMENT
$command: error: $ac_option: invalid argument
EOF_INVALID_ARGUMENT
	      exit 1; }
	fi
	target="$ac_option"
	target_defined=1 ;;
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

# Check debug output file
if [ -n "$filetrace" ]
then
    if [ -d "$filetrace" ]
    then
	{ cat 1>&2 << EOF_FILETRACE
$command: error: $filetrace: not a file
EOF_FILETRACE
	  exit 1; }
    else
	rm -f $filetrace
    fi
fi

#echo "includes = $includes"

echo "$command: invocation parameters:" | \
  tee -a $filetrace > /dev/null
for parameter
do
    echo "\"$parameter\"" | tee -a $filetrace > /dev/null
done

# Check template used
makefile_template="$datadir/$command"
if [ $library -ne 0 ]
then
    if [ "$SYSTEMFAMILY" = "win32" ]
    then 
	makefile_template="$makefile_template.msvc6.lib"
    else
        makefile_template="$makefile_template.lib"
    fi	 
else
    makefile_template="$makefile_template.$SYSTEM.prg"
fi
if [ ! -r "$makefile_template" ]
then
    { cat 1>&2 << EOF_TEMPLATE_NOT_FOUND
$command: error: $makefile_template: not found
EOF_TEMPLATE_NOT_FOUND
      exit 1; }
else
    echo "$command: using template \"$makefile_template\"" | \
      tee -a $filetrace > /dev/null
fi
    

# Do the job...
tmpdir=$TMPDIR
if [ -z "$tmpdir" ]
then
    tmpdir=/tmp
fi
tmp_makefile="$tmpdir/mkmf$$.mak"
echo "$command: using tmp_makefile \"$tmp_makefile\"" | \
  tee -a $filetrace > /dev/null

tmp_cpp_output="$tmpdir/mkmf$$.tmp"
echo "$command: using tmp_cpp_output \"$tmp_cpp_output\"" | \
  tee -a $filetrace > /dev/null

trap "rm -f $tmp_makefile $tmp_cpp_output" 1 2 3 6 15

cat $makefile_template > $tmp_makefile

object_list=
retcode=0

# Replace "/* " by ":" at the end of each directory of srcextradir
extradirVPath=`echo -e "${srcextradir}" | sed "s|/\* *|:|g"`
# MRE : on parse tous les cpp contenus dans le bindir
bindirectory=`dirname $output`
for file in * $bindirectory/* $srcextradir
do
    # Define file species
    case $file in
     *.c) extension='.c'
	  dependency_regexp='.*\\.h'
	  ;;
     *.C) extension='.C'
	  dependency_regexp='.*\\.h(h|pp|xx)?'
	  ;;
     *.cc) extension='.cc'
	   dependency_regexp='.*\\.h(h|pp|xx)?'
	   ;;
     *.cpp) extension='.cpp'
	    dependency_regexp='.*\\.h(h|pp|xx)?'
	    ;;
     *.cxx) extension='.cxx'
	    dependency_regexp='.*\\.h(h|pp|xx)?'
	    ;;
     *)	continue
	;;
     esac

     source=$file

     if [ "$SYSTEMFAMILY" = "win32" ]
     then
        object=`basename $source $extension`.obj
        if [ "$SCS_USE_CPATH" = "no" ]
        then
            object=$object
            source=$file
        else
	object=`cpath $object`
	source=`cpath $file`
        fi
     else
        object=`basename $source $extension`.o
     fi	
     if [ -z "$object_list" ]
     then
	 object_list=$object
     else
	 object_list="$object_list $object"
     fi

     # Run preprocessor
     echo "==== Parsing $source ====" | tee -a $filetrace > /dev/null
     #echo $preprocessor $cppflags $includes $excludes $source
     sh -c "$preprocessor $cppflags $includes $excludes $source > $tmp_cpp_output"
     retcode=$?
     if [ $retcode -ne 0 ]
     then
         cat 1>&2 << EOF_CPP_ERROR
$command: error: $source cannot be preprocessed.
EOF_CPP_ERROR
	 echo "==== Stopped in $source ====" | tee -a $filetrace > /dev/null
	 break
     fi

     #  Parse result
     cat $tmp_cpp_output | tee -a $filetrace | \
       egrep "$egrep_regexp" 2>/dev/null | \
       $awk_command \
       EXCLUDED_DIRS="$excludes" \
       DEPENDENCY_REGEXP="$dependency_regexp" \
       OBJECT="$object" >> $tmp_makefile
     echo >> $tmp_makefile
done

if [ $retcode -eq 0 ]
then
    if [ x"$OSTYPE" != "xmsys" ]; then
      sed -e "s|%TARGET%|$target|g;\
        s|%OBJECTS%|$object_list|g;\
        s|%DATE%|`date`|g;\
        s|%ID%|`whoami`|g;\
        s|%VPATH%|$extradirVPath|g" $tmp_makefile > $output
    else
      sed -e "s|%TARGET%|$target|g;\
        s|%OBJECTS%|$object_list|g;\
        s|%DATE%|`date`|g;\
        s|%ID%|$USER|g;\
        s|%VPATH%|$extradirVPath|g" $tmp_makefile > $output
    fi
fi
rm -f $tmp_makefile $tmp_cpp_output

exit $retcode

