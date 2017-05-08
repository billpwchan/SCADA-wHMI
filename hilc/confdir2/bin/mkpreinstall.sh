#!/bin/sh
# mkinstall -- install Animator 96 release
#
command=`basename $0`
baseReleaseDir="/animref/A96"
releaseDir="animator"
userSourceDir="/an96/v9/source/a96"
srcDir="source"
configFile="config"
configMakefile="config.mak"
buildFile="buildrelease"
instructionFile="instructions"

set -- `getopt "d:u:" $*`
if [ $? != 0 ]
then
    exit 1
fi
while [ $1 != -- ]
do
    case $1 in
     -d) baseReleaseDir=$2
	 shift;;
     -u) userSourceDir=$2
	 shift;;
    esac
    shift
done
shift

if [ $# -ne 2 ]
then
    cat <<EOF
usage: $command <targetDir> <releaseStamp>

options:                 default value is:
  -d <baseReleaseDir>      ($baseReleaseDir)
  -u <userSourceDir>       ($userSourceDir)

note:
  <targetDir>    MUST be a single directory name
  <releaseStamp> is a version symbol
EOF
    exit 1
else
    targetDir=$1
    releaseStamp=$2
fi

tarDir="${baseReleaseDir}/${targetDir}/${releaseDir}"
tarContents="./${releaseStamp}"
tarFile="${releaseStamp}.tar"

# --------------------------------------------------------------------
echo "creating release directories..."
rm -rf ${baseReleaseDir}/${targetDir}/source
workingDir=`pwd`
sedCommand="s|/RCS||;s|${userSourceDir}|${baseReleaseDir}/${targetDir}/${srcDir}|;p"
directoryList=`find $userSourceDir -name RCS -print`
for sourceDirectory in $directoryList
do
    echo "extracting sources files from ${sourceDirectory}..."
    targetDirectory=`echo $sourceDirectory|sed -ne "$sedCommand"`
    mkinstalldirs -q $targetDirectory
    cd $targetDirectory
    co -q $sourceDirectory/*
done
cd $workingDir

# --------------------------------------------------------------------
echo "adapting configuration script..."
chmod 666 ${baseReleaseDir}/${targetDir}/${srcDir}/${configFile}
regularExpr='setenv[ \t]\{1,\}ROOTDIR.*'
replacement="setenv ROOTDIR ${baseReleaseDir}/${targetDir}/${srcDir}"
sed -ne "s|${regularExpr}|${replacement}|;p" \
  ${baseReleaseDir}/${targetDir}/${srcDir}/${configFile} \
  > ${baseReleaseDir}/${targetDir}/${srcDir}/tmp

mv ${baseReleaseDir}/${targetDir}/${srcDir}/tmp \
  ${baseReleaseDir}/${targetDir}/${srcDir}/${configFile}
chmod 444 ${baseReleaseDir}/${targetDir}/${srcDir}/${configFile}

chmod 666 ${baseReleaseDir}/${targetDir}/${srcDir}/${configMakefile}
regularExpr='[ \t]\{1,\}-g[0-3]*'
replacement=" "
sed -ne "s|${regularExpr}|${replacement}|;p" \
  ${baseReleaseDir}/${targetDir}/${srcDir}/${configMakefile} \
  > ${baseReleaseDir}/${targetDir}/${srcDir}/tmp

mv ${baseReleaseDir}/${targetDir}/${srcDir}/tmp \
  ${baseReleaseDir}/${targetDir}/${srcDir}/${configMakefile}
chmod 444 ${baseReleaseDir}/${targetDir}/${srcDir}/${configMakefile}

# --------------------------------------------------------------------
echo "creating release builder script..."
cat > ${baseReleaseDir}/${targetDir}/${srcDir}/${buildFile} <<EOF
#!/bin/sh
# $buildFile -- build Animator 96 release
#
command=`basename \$0`
if [ "\${ANSYSTEM-unset}" = "unset"\\
  -o "\${ROOTDIR-unset}" = "unset"\\
  -o "\${ILVHOME-unset}" = "unset"\\
  -o "\${RWHOME-unset}" = "unset" ]
then
    echo "${command}: ANSYSTEM, ROOTDIR, ILVHOME, RWHOME: unknown environment variables."
    exit 1
fi

baseReleaseDir="$baseReleaseDir"
releaseDir="$releaseDir"
targetDir="$targetDir"
releaseStamp="$releaseStamp"

libraryDir="lib/\${ANSYSTEM}"
libraryFile="libanimator.a"
libraryParts="\${ROOTDIR}/\${ANSYSTEM}/libapiv8.a\\
  \${ROOTDIR}/\${ANSYSTEM}/libapiv9cpp.a\\
  \${ROOTDIR}/\${ANSYSTEM}/libkernel.a\\
  \${ROOTDIR}/\${ANSYSTEM}/libengine.a\\
  \${ROOTDIR}/\${ANSYSTEM}/libutils.a\\
  \${ROOTDIR}/\${ANSYSTEM}/libilvfonts.a\\
  \${ILVHOME}/lib/\${ANSYSTEM}/libilvgadgt.a\\
  \${ILVHOME}/lib/\${ANSYSTEM}/libviews.a\\
  \${ILVHOME}/lib/\${ANSYSTEM}/libxviews.a\\
  \${RWHOME}/\${ANSYSTEM}/lib/librwtool.a"

binaryDir="bin/\${ANSYSTEM}"
binaryFile="\${ROOTDIR}/\${ANSYSTEM}/antest"

apiHeadersDir="include"
apiHeadersDirectories="\${ROOTDIR}/a96lib/apiv8\\
  \${ROOTDIR}/a96lib/apiv9cpp"

dataDir="data"
dataDirectories="\$ROOTDIR/a96lib/data"

workingDir=`pwd`
trap 'cd \$workingDir ; exit 1' 1 2 3 15

# display status
cat <<EOF_MSG

Release builder for Animator 96
-------------------------------

 System release: \$ANSYSTEM
 Directories:
  Installation:  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}
  Source:        \$ROOTDIR
  Ilog Views:    \$ILVHOME
  Tools.h++:     \$RWHOME

WARNING: be sure that all binary files must have been generated
         before using this script.

[RETURN] to continue release building, ^C to exit

EOF_MSG
read dummy

# --------------------------------------------------------------------
echo "making release directory ..."
rm -rf \${baseReleaseDir}/\${targetDir}/\${releaseDir}
mkinstalldirs -q\
  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${binaryDir}\\
  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${libraryDir}\\
  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${apiHeadersDir}\\
  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${dataDir}

# --------------------------------------------------------------------
echo "creating release library..."
cd \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${libraryDir}
for subLibrary in \$libraryParts
do
    echo "extracting objects from \${subLibrary}..."
    objList="\`ar t \$subLibrary | grep '\\.o'\`"
    ar x \$subLibrary \$objList

    echo "adding them to \${libraryFile}..."
    ar cqs \$libraryFile \$objList

    rm -f \$objList
done
cd \$workingDir

# --------------------------------------------------------------------
echo "creating release library test program..."
cp \$binaryFile \
  \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${binaryDir}

# --------------------------------------------------------------------
echo "creating release API Headers ..."
for apiDirectory in \$apiHeadersDirectories
do
    cp \${apiDirectory}/*.h \
      \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${apiHeadersDir}
done

# --------------------------------------------------------------------
echo "creating release datas..."
for dataDirectory in \$dataDirectories
do
    cp -r \${dataDirectory}/* \
      \${baseReleaseDir}/\${targetDir}/\${releaseDir}/\${releaseStamp}/\${dataDir}
done

EOF
chmod u+x ${baseReleaseDir}/${targetDir}/${srcDir}/${buildFile}

# --------------------------------------------------------------------
cat <<EOF > ${baseReleaseDir}/${targetDir}/${srcDir}/${instructionFile}
INSTRUCTIONS:

Invoke following commands to compile new releases:

1- cd ${baseReleaseDir}/${targetDir}/${srcDir}
2- source config
3- cd a96lib
4- gmake depend all
5- cd .. ; $buildFile

DON'T FORGET TO INSTALL SAMPLES DIRECTORY!!!

Then, to create a tar release:

1- cd ${tarDir}
2- tar cf $tarFile $tarContents

EOF

# --------------------------------------------------------------------
cat << EOF
--------------------------------------------------
Release sources installed.

Following instructions are stored in file:

${baseReleaseDir}/${targetDir}/${srcDir}/${instructionFile}

--------------------------------------------------
EOF

cat ${baseReleaseDir}/${targetDir}/${srcDir}/${instructionFile}

# --------------------------------------------------------------------
# End of mkrelease
