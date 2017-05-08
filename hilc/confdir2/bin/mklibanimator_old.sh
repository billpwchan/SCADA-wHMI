#!/bin/sh
# mklibmanimator -- build animator library with motif from development libraries
#
command=`basename $0`
if [ "${AN_TARGET-unset}" = "unset"\
  -o "${ROOTDIR-unset}" = "unset"\
  -o "${ILVHOME-unset}" = "unset"\
  -o "${RWHOME-unset}" = "unset" ]
then
    echo "${command}: AN_TARGET, ROOTDIR, ILVHOME, RWHOME: unknown environment variables."
    exit 1
fi

libraryFile="libanimator.a"
libraryParts="${ROOTDIR}/${AN_TARGET}/libapiv8.a\
  ${ROOTDIR}/${AN_TARGET}/libapiv9cpp.a\
  ${ROOTDIR}/${AN_TARGET}/libkernel.a\
  ${ROOTDIR}/${AN_TARGET}/libengine.a\
  ${ROOTDIR}/${AN_TARGET}/libutils.a\
  ${ROOTDIR}/${AN_TARGET}/libilvfonts.a\
  ${ILVHOME}/lib/${AN_TARGET}/libilvgadgt.a\
  ${ILVHOME}/lib/${AN_TARGET}/libviews.a\
  ${ILVHOME}/lib/${AN_TARGET}/libmviews.a\
  ${RWHOME}/${AN_TARGET}/lib/librwtool.a"

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

