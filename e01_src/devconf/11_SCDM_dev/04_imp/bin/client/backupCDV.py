#! /usr/bin/env jython
# ===================================================
#   Script to Restore/Compute/Check/Generated a CDV
# ===================================================
import sys
import os
import traceback
from CfgInterface import CfgInterface

retCode = 0

# ================================
#               Main
# ================================

# Check the arguments
# -----------------------
if len(sys.argv) < 2:
	print "Usage: " + sys.argv[0] + " <VDC Name>"
	retCode = 1
else:
	vdcName=sys.argv[1]

	print "-----------------------"
	print "- " + os.time.strftime("%X %x") + "- Connect to the server"
	CfgInt=CfgInterface()
	print "-----------------------"
	
	try:
		vdcRelease = vdcName + "_" + CfgInt.getCDVRelease(vdcName)
		list_backupCDV = CfgInt.listBackupCDV()
		if list_backupCDV.count([vdcRelease]) == 0 :
			try:						
				print "- " + os.time.strftime("%X %x") + "- Backup the CDV :" + vdcRelease
				CfgInt.backupCDV(vdcName,vdcRelease)
				print "-------------------------------"
			except:
				print "- Exception raised" 
				traceback.print_exc()
				print "--------------------------------"
				retCode = 1
		else :
			print "--------------------------------"
			print "- The CDV " + vdcRelease + " is backup already"
			print "--------------------------------"
	finally:
		print "- " + os.time.strftime("%X %x") + "- Close Connection to the server"
		CfgInt.Disconnect()
		print "-------------------------------"

# Return the completion status
# --------------------------------
sys.exit(retCode)		

# ===================================================	
	