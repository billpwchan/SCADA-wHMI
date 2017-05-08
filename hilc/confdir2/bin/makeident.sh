#! /bin/sh

# Init
echo "Initializing SCADAsoft identification..."
ident_rep=$ROOTDIR/src/ident/version

# Récupération du numéro de version
version=`sh $CONFDIR/bin/findversion.ksh`

# Récupération du niveau de patch et du commentaire associé
cd $ident_rep

if [ "$SCSUSERS" = "" ]
then
    echo "Using /nassvr1/users/scadadev/ccase as path for ccaseLabels.txt"
    patch=`grep "${version}patch" /nassvr1/users/scadadev/ccase/ccaseLabels.txt | grep -v "animator" | sort -u | tail -1`
else
    echo "using $SCSUSERS as path for ccaseLabels.txt"
    patch=`grep "${version}patch" ${SCSUSERS}/scadadev/ccase/ccaseLabels.txt | grep -v "animator" | sort -u | tail -1`
fi
patch_level=`echo "$patch" | cut -f1`

patch_comment=""
if [ "$patch_level" = "" ]
then
    patch_level="No patch"
else
    label_name=`echo "$patch" | tr -d "\r" | cut -f2`
    if [ "$SYSTEMFAMILY" = "win32" ]
    then
	label_name=`echo "$label_name" | sed 's/@\/vobs\//@\\\/'`
    else
	label_name=`echo "$label_name" | sed 's/@\\\/@\/vobs\//'`
    fi
    patch_comment=`cleartool desc -fmt "%Nc" $label_name`
fi

# Ecriture du fichier de version
file=$ident_rep/version.cpp

# Ecriture du commentaire associé au patch en retirant les éventuels 'newline'
cat << EOF | tr -s "\n" " " > $file
static char const SCS_patchComment[] = "\$ScsPatchComment: $patch_comment $";
EOF

cat >> $file << EOF

static char const SCS_patchLevel[]   = "\$ScsPatchLevel:   $patch_level $";
static char const SCS_copyright[]    = "\$ScsCopyright:    Copyright © Thales Transportation Systems 1996-2006. All rights reserved. $";
static char const SCS_version[]      = "\$ScsVersion:      SCADAsoft Version $version $";

#include <stdio.h>
// Just to force some code optimizers to not remove the symbols above
int
ScsPrintVersionDummy(void)
{
    char buffertmp[1000];
    sprintf(buffertmp, "# %s\n", SCS_version);
    sprintf(buffertmp, "# %s\n", SCS_copyright);
    sprintf(buffertmp, "# %s\n", SCS_patchLevel);
    sprintf(buffertmp, "# %s\n", SCS_patchComment);
    return 0;
}

static int __ScsDummy = ScsPrintVersionDummy();
EOF

# Compilation
cd $ident_rep
if [ "$SYSTEMFAMILY" = "win32" ]
then
    MAKE=make
else
    MAKE=gmake
fi
$MAKE clean >/dev/null 2>&1
$MAKE > /dev/null 2>&1

if [ $? -eq 0 ]
then
    if [ "$patch_level" = "No patch" ]
    then
	echo "Initialization OK : $version (no patch)"
    else
	echo "Initialization OK : $patch_level [$patch_comment]"
    fi
else
    echo "***** Initialization ERROR : $patch_level *****"
fi
echo
