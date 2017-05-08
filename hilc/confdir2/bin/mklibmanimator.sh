#!/bin/sh
# mkmlibmanimator -- build animator library with motif from development libraries
#
command=`basename $0`
if [ "${ANSYSTEM-unset}" = "unset"\
  -o "${ROOTDIR-unset}" = "unset"\
  -o "${ILVHOME-unset}" = "unset"\
  -o "${RWHOME-unset}" = "unset" ]
then
    echo "${command}: ANSYSTEM, ROOTDIR, ILVHOME, RWHOME: unknown environment variables."
    exit 1
fi

libraryFile="libmanimator.a"
libraryParts="${ROOTDIR}/${ANSYSTEM}/libapiv8.a\
  ${ROOTDIR}/${ANSYSTEM}/libapiv9cpp.a\
  ${ROOTDIR}/${ANSYSTEM}/libkernel.a\
  ${ROOTDIR}/${ANSYSTEM}/libengine.a\
  ${ROOTDIR}/${ANSYSTEM}/libutils.a\
  ${ROOTDIR}/${ANSYSTEM}/libilvfonts.a\
  ${ILVHOME}/lib/${ANSYSTEM}/libilvgadgt.a\
  ${ILVHOME}/lib/${ANSYSTEM}/libviews.a\
  ${ILVHOME}/lib/${ANSYSTEM}/libmviews.a\
  ${RWHOME}/${ANSYSTEM}/lib/librwtool.a"

echo "deleting old release library..."
rm -f $libraryFile

echo "creating release library..."
for subLibrary in $libraryParts
do
    echo "extracting objects from ${subLibrary}..."
    objList="`ar t \$subLibrary | grep '\.o'`"
    ar x $subLibrary $objList

    echo "adding them to ${libraryFile}..."
    ar cqs $libraryFile $objList

    rm -f $objList
done

