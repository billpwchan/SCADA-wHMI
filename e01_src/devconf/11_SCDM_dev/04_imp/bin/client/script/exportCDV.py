######################################
# exportCDV.py
# 
# USAGE : exportCDV.py cdvName exportName
#
# cdvName     name of the CDV
# exportName  name of the export system
#
# export a CDV 
# return 
# 0 if success
# 1 if incorrect parameters
# 2 if cdvName doesn't exist
# 3 if exportName doesn't exist
# 4 if error in exportCDV
#
######################################

import os
import sys
from CfgInterface import CfgInterface

print "-" + os.time.strftime(" %x %X ") + "- Connect to the server"
cfgInterface = CfgInterface()

retCode = 4

######################################

listCDV = cfgInterface.listCDV()
listImportExportTargetSystems = cfgInterface.listImportExportTargetSystems()

try:
	if len (sys.argv[0:]) != 3:
		print
		print "Error syntax => USAGE : exportCDV.py cdvName exportName"
		print "with cdvName in :"
		print listCDV
		print "with exportName in :"
		print listImportExportTargetSystems
		print 
		retCode = 1
		raise
	
	cdvName 	= str(sys.argv[1])
	exportName 	= str(sys.argv[2])
	
	if not cdvName in listCDV:
		print
		print "Error : cdvName ", cdvName, " doesn't exist"
		print "CDV list :"
		print listCDV
		print 
		retCode = 2
		raise
	
	if not exportName in listImportExportTargetSystems:
		print
		print "Error : exportName ", exportName, " doesn't exist"
		print "exportName list :"
		print listImportExportTargetSystems
		print 
		retCode = 3
		raise
	
	try:
		# execution
		print cdvName, " export in progress ..."
		query=None
		bExportReplace=False
		print cfgInterface.exportCDV(cdvName,exportName,query,bExportReplace)
		print "Success in exportCDV ", cdvName
		retCode = 0
	except:
		import traceback
		traceback.print_exc()
		print
		print "Error in exportCDV ", cdvName
		print
		retCode = 4
		raise

except:
	pass
finally:
	print "-" + os.time.strftime(" %x %X ") + "- Close Connection to the server"
	cfgInterface.Disconnect()

sys.exit(retCode)
