######################################
# generateAllCDV.py
# 
# USAGE : generateAllCDV.py [-h] [cdvNameExcept]
# 
# [-h]		display this help and exit
# [cdvNameExcept]	cdvNameExcept has not to generate
#
#  generate all CDV except cdvNameExcept
#  do not generate CDV already generated
#  return 
#  0  if success
#  n  errors count in generateAllCDV
#
######################################

import os
import sys
import traceback
from CfgInterface import CfgInterface

def usage():
	print "USAGE : generateAllCDV.py [-h] [cdvNameExcept]"
	print 
	print "[-h]		display this help and exit"
	print "[cdvNameExcept]	cdvNameExcept has not to generate"
	print
	print " generate all CDV except cdvNameExcept"
	print " do not generate CDV already generated"
	print " return" 
	print " 0  if success"
	print " n  errors count in generateAllCDV"
	print
	sys.exit(retCode)

retCode = 0

listExceptCDV = None

if len (sys.argv[0:]) > 1:
	if ("-h" in sys.argv[1:]): 
		usage()
	listExceptCDV = sys.argv[1:] 

print "-" + os.time.strftime(" %x %X ") + "- Connect to the server"
cfgInterface = CfgInterface()
	
listCDV = cfgInterface.listCDV()

try:
	
	for cdvName in listCDV:
		
		if cdvName in listExceptCDV: continue
		
		try:
			isGenerateCDV = cfgInterface.isGenerateCDV(cdvName)
			if isGenerateCDV: continue
		except:
			traceback.print_exc()
			retCode = retCode + 1
			continue

		# execution
		print "-" + os.time.strftime(" %x %X ") + "- generateCDV " + cdvName + " in progress ... "
		try:
			print cfgInterface.generateCDV(cdvName)
			print "-" + os.time.strftime(" %x %X ") + "- Success in generateCDV " + cdvName
		except:
			traceback.print_exc()
			retCode = retCode + 1
			print "-" + os.time.strftime(" %x %X ") + "- Error in generateCDV " + cdvName
			continue

except:
	pass
finally:
	print "-" + os.time.strftime(" %x %X ") + "- Close Connection to the server"
	cfgInterface.Disconnect()

sys.exit(retCode)
