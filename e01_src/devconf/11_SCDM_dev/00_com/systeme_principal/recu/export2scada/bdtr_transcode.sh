#!/bin/sh
#

# define environment
# ==================
export cible=$2
export root_vdc=`dirname $cible`

export ficout=$root_vdc/bdtr_transcode.log

echo "`date`: 0=$0" > $ficout
echo "`date`: 1=$1" >> $ficout
echo "`date`: 2=$2" >> $ficout
echo "`date`: 3=$3" >> $ficout


#
# Copy the epg data
# =================
cp -rfp $root_vdc/../../resource/epg $root_vdc

# Semaphore creation for the client
# ===================================
touch $root_vdc/endExtraction

# Attente de la fin de copie des images
# =====================================
semFile=$root_vdc/visu/extractImageEnded
typeset -i timeCount=0
until [ -f $semFile -o $timeCount -gt 2 ]
do
	echo "`date`: Waiting for image copy ($semFile)..." >> $ficout
	sleep 10
	timeCount=${timeCount}+1
done
if [ ! -f $semFile ]
then
	echo "Stopping generation : images not copied." >&2
	exit 1
fi

# Attente de la fin de creation des epgs
# ======================================
semFile=$root_vdc/endEpgGeneration
typeset -i timeCount=0
until [ -f $semFile -o $timeCount -gt 2 ]
do
	echo "`date`: Waiting for epg generation ($semFile)..." >> $ficout
	sleep 10
	timeCount=${timeCount}+1
done
if [ ! -f $semFile ]
then
	echo "Stopping generation : epg not generated." >&2
	exit 1
fi


# Send the output to the export report
# ------------------------------------
cat $ficout

exit 0
