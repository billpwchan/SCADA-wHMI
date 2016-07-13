#!/usr/bin/env jython
#
#
import sys
import os
import traceback
#from CfgInterface import CfgInterface
retCode = 0

try: 
	from CfgInterface import CfgInterface	

	# ================================
	#               Main
	# ================================

	# Check the arguments
	# -----------------------
	if len(sys.argv) < 1:
		print "Usage: " + sys.argv[0] + " <VDC Name>"
		retCode = 1
	else:
		vdcName=sys.argv[1]

		print "-----------------------"
		print "- " + os.time.strftime("%X %x") + "- Connect to the server"
		print "- CDV name :" + vdcName
		CfgInt=CfgInterface()
		print "-----------------------"

		try:
			vdcRelease = vdcName + "_" + CfgInt.getCDVRelease(vdcName)
			
			try:
				print "- " + os.time.strftime("%X %x") + "- remove CDV backup " + vdcName + " to avoid conflict."
				CfgInt.deleteCDVBackup(vdcName)
			except:
				print "- " + vdcName + " do not exist."

			print "- " + os.time.strftime("%X %x") + "- Backup 1st CDV : " + vdcName
			
			CfgInt.backupCDV(vdcName,vdcName)
			list_backupCDV = CfgInt.listBackupCDV()
			if list_backupCDV.count([vdcRelease]) == 0 :				
				try:
					print "- " + os.time.strftime("%X %x") + "- Backup 2nd CDV : " + vdcRelease
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
			CfgInt.Disconnect();
			print "-------------------------------"

except:
	print "Fail to import CFGInterface!"
# Return the completion status
# --------------------------------
finally:
	print "Done save cdv"
	sys.exit( retCode )
