#!/bin/sh
# mklibmanimator -- build animator library with motif from development libraries
#
command=`basename $0`
if [ "${AN_TARGET-unset}" = "unset"\
  -o "${BINDIR-unset}" = "unset"\
  -o "${ILVHOME-unset}" = "unset"\
  -o "${SYSTEMFAMILY-unset}" = "unset"\
  -o "${RWHOME-unset}" = "unset" ]
then
    echo "${command}: AN_TARGET, BINDIR, ILVHOME, RWHOME, SYSTEMFAMILY: unknown environment variables."
    exit 1
fi

libraryFile="libanimator.a"
libraryParts="${BINDIR}/libapiv8.a\
  ${BINDIR}/libapiv9cpp.a\
  ${BINDIR}/libkernel.a\
  ${BINDIR}/libengine.a\
  ${BINDIR}/libutils.a\
  ${BINDIR}/libilvfonts.a\
  ${ILVHOME}/lib/${SYSTEMFAMILY}/libilvgadgt.a\
  ${ILVHOME}/lib/${SYSTEMFAMILY}/libviews.a\
  ${ILVHOME}/lib/${SYSTEMFAMILY}/libmviews.a\
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

