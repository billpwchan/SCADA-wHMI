#!/bin/sh
command=`basename $0 .sh`
command_dir=`dirname $0`

# AWK Command
if [ "`uname`" = "SunOS" ]
then
    awk_command=nawk
else
    awk_command=awk
fi

# Predefined values for options
copy=
copy_defined=0
libraries=
libraries_defined=0
output=libanimator.a
prefix=.
target_dir=$TMPDIR
strip=0
purge=0

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
Usage: $command [options] [directory]
--help            Issue this usage message.

--copy=FILES      Copy files to target library.
--libraries=LIBS  Use LIBRARIES to build output (order is significant).
--output=LIB      Use LIB as output library.
--prefix=DIR      Use DIR as a prefix path for libraries.
--strip           Strip extracted objects from libraries.
--target_dir=DIR  Use DIR as target directory.
--purge           Purge librairy output librairy before.

Options may be abbreviated to their first letter. Option equal sign may be
replaced with a space.
EOF_USAGE
        exit 0 ;;

     -copy | --copy | --c | -c)
	ac_prev=copy ;;
     -copy=* | --copy=* | --c=* | -c=*)
	copy="$ac_optarg"
 	copy_defined=1 ;;

     -libraries | --libraries | --l | -l)
	ac_prev=libraries ;;
     -libraries=* | --libraries=* | --l=* | -l=*)
	libraries="$ac_optarg"
 	libraries_defined=1 ;;

     -output | --output | --o | -o)
	ac_prev=output ;;
     -output=* | --output=* | --o=* | -o=*)
	output="$ac_optarg" ;;

     -prefix | --prefix | --p | -p)
	ac_prev=prefix ;;
     -prefix=* | --prefix=* | --p=* | -p=*)
	prefix="$ac_optarg" ;;

     -strip | --strip | -s | --s)
        strip=1 ;;

     -target_dir | --target_dir | --t | -t)
	ac_prev=target_dir ;;
     -target_dir=* | --target_dir=* | --t=* | -t=*)
	target_dir="$ac_optarg" ;;

     -purge | --purge | -p | --p)
        purge=1 ;;
     -*)
        { cat 1>&2 << EOF_INVALID_OPTION
$command: error: $ac_option: invalid option; use --help to show usage
EOF_INVALID_OPTION
           exit 1; } ;;
     *) if [ $libraries_defined -ne 0 -o $copy_defined -ne 0 ]
		then
	    	{ cat 1>&2 << EOF_INVALID_ARGUMENT
$command: error: $ac_option: invalid argument
EOF_INVALID_ARGUMENT
	      exit 1; }
		fi
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

echo "$command: purging previous target directory..."
rm -rf $target_dir
mkdir $target_dir
cd $target_dir

echo "$command: creating $output..."
if [ $purge -ne 0 ]
then
    echo "$command: removing previous librairy..."
    rm -f $output
fi

for sub_library in $libraries
do
    echo "$command: extracting objects from $sub_library..."
    obj_list="`ar t \$sub_library | grep '\.o'`"
    ar x $sub_library $obj_list

    if [ $strip -ne 0 ]
    then
        echo "$command: stripping objects..."
        strip $obj_list
    fi

    echo "$command: adding them to $output..."
    if [ ! -f $output ]
    then
	ar cqs $output $obj_list
    else
	ar ru $output $obj_list
    fi

    rm -f $obj_list
done

if [ $copy_defined -ne 0 ]
then
    echo "$command: copying files..."
    cp $copy $target_dir
fi

