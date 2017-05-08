#!/bin/sh

machine=`hostname`

rsh -l root $machine "chown root $1 ; chmod a+s $1"
