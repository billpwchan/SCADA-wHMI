#! /bin/ksh
awk 'BEGIN {FS="\""} /^#define[ \t]+SCADASoftVersion[ \t]+\".*\"/ {print $2}' $ROOTDIR/src/inc/scsversion.h
