#! /usr/bin/env jython
# Author: Katherine SHIH
# ===================================================
#   Script to config Template Objects links
# ===================================================

#******************************************
#ELEMENT:
#******************************************
#dci_cb_type/ dci_soe_type
#	|-> (dfo)
#	|-> dac_type
#	|-> dal_type
#		|-> dal_type.valueTable
#aci_cb_type
#	|-> (aac), (afo), (aal_type.valueTable)
#	|-> aal_type
#	|-> aal_type.valueLimit
#dio_type
#	|-> dio_type.valueTable
#	|-> dov_type
#aio_type
#SCI_type
#	|-> (sac), (sfo)
#sio_type
#******************************************

import sys
import os
import traceback
import java.util.Vector
from CfgInterface import CfgInterface
from CfgInterface import printList
from CfgInterface import GetInstances
from Custom import stdPrint

I=CfgInterface()

ved=sys.argv[1]
fname='ERASE_temp_link_object.csv'
fout=open(fname,'w')

I.unlock(ved)
VED=I.GetConfiguration(ved)


elementlist=['ucs_type','dci_mod4_type','Number','Text','soe_only_type','dal_type12','dal_type32','ufe_float','ufe_date','ufe_string','ufe_int','ufes','HMITags','Tag','SGEQPT8','dci_cb_type', 'dci_soe_type', 'aci_cb_type', 'SCI_TYPE', 'dio_type', 'aio_type', 'sio_type', 'dac_type', 'dal_type','aal_type','dov_type','aal_type','dco_type','VE','Symbol']


e=0
count_err1=0
count_err2=0
count1=0

if 0==1 :
	stdPrint("VED " + ved + " is not attached to VDC " + vdc)
else:
	al=0
	fout.writelines('"ID","link_object_is_from_object_type"'+'\n')
	
	for element in elementlist:
		AGGlist=I.Query(ved,"//"+element+"[@link_object_is_from_object_type!='']")
		stdPrint(element)
		
		for AGG in AGGlist:
			#Alias of parent node
			AGG_parent=AGG[0]
			if element=='dov_type':
				if not AGG[0].split(":")[5].split("-").pop().startswith("SEL"): 
					fout.writelines(AGG[0]+",__ERASE__\n")
			else:
				fout.writelines(AGG[0]+",__ERASE__\n")
		#end for
		stdPrint("Create of links finished with " + str(count_err2) + "/" + str(count1) + " errors.")
	
	fout.flush()
	fout.close()
	I.ImportCSV(ved,fname)
	
VED.close()
I.unlock(ved)

stdPrint("Links are erased!")