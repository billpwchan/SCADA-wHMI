#! /bin/ksh

hostname=`hostname`
licserver=`hostname`

if [ "$SYSTEM" = "OSF1V4.0" ]; then
    username=`who am i | cut -f1 -d" "`
    licserver="bonsai"
    export RTAPROOT=/opt/rtap/A.07.00
    echo rsh $licserver -l $username "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $1"
    rsh $licserver -l $username "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $1"
elif [ "$SYSTEM" = "OSF1V4.0-cxx65" ]; then
    username=`who am i | cut -f1 -d" "`
    licserver="bonsai"
    export RTAPROOT=/opt/rtap/A.07.00
    echo rsh $licserver -l $username "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $1"
    rsh $licserver -l $username "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $1"
else
    licserver="cactus"
    username=`whoami`
    export RTAPROOT=/opt/rtap/A.08.20

    cp -p $1 $HOME/tmpRtap/
    echo remsh $licserver -l $username -n "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $HOME/tmpRtap/`basename $1`"
    remsh $licserver -l $username -n "export RTAPROOT=$RTAPROOT ; $RTAPROOT/bin/RtapUnlockExe $HOME/tmpRtap/`basename $1"
    cp -p $HOME/tmpRtap/`basename $1` $1
    rm -f $HOME/tmpRtap/`basename $1`
fi

