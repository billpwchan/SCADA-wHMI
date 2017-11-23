#! /usr/bin/env jython
# ===================================================
#   Script to Restore/Compute/Check/Generated a CDV
# ===================================================
import sys
import os
import traceback
from CfgInterface import CfgInterface
from Custom import stdPrint

retCode = 0

# ================================
#               Main
# ================================

# Check the arguments
# -----------------------
I=CfgInterface()

temp=str(I.r.myFactory.getAllVEDBackups())

WDS_list=I.listVED()

for n in WDS_list:
    if n[0:4]=='AGG_' and temp.count(str(n))==0:
		stdPrint("Backup WDS: " + str(n))
		I.r.myFactory.backupVED(str(n),str(n),"")




stdPrint("WDS Backup is done!")