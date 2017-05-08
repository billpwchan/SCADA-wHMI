#!/bin/sh

if [ -z "$IS_BUILD_ENV_SET" ]
then
	. $MSYS_HOME/home/admin/.profile
fi

cd $ROOTDIR

if [ ! -d log ]; then mkdir log; fi

outputFile=log/build_hilc_`date '+%Y%m%d_%H%M'`.out

echo -e "==> `date` <== START BUILDING HILC in `pwd` (`type make`)\n" | tee $outputFile

# clean
rm -rf $BINDIR

# Start the build
startDateCompil=`date +%s`
make | tee -a $outputFile 2>&1

endDateCompil=`date +%s`
durationCompil=`expr $(($endDateCompil - $startDateCompil))`

echo -e "\n*** BUILD TIME: ${durationCompil} seconds" | tee -a $outputFile