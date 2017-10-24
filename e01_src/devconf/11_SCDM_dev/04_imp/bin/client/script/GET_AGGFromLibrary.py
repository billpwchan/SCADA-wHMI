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
from com.thalesis.config.utils import GZIPHelper
from CfgInterface import CfgInterface
from CfgInterface import printList
from CfgInterface import GetInstances
from Custom import stdPrint

I=CfgInterface()

#stdPrint("Press [Enter] to continue with CDV(" +vdc+ "), WDS (" + ved + ") and suffix (" + sys + ") or [Ctrl+C] to end")
#if len(sys.stdin.readline())<>0 :
#	sys.exit(0)
	
#####################################################
## Confrim with the CDV, VED name and suffix
#####################################################

vdc=sys.argv[1]
subsys=sys.argv[2]
ved=sys.argv[3]


fname=sys.argv[0].replace('.py','')+'.csv'
fout=open(fname,'w')

CDV=I.GetConfiguration(vdc,0)

EQPT_NAME='SGEQPT8'

######################################################
#@@ Get List of Agggregate to be created
######################################################
listAGG=[]
listAGG=GetInstances(CDV.listTemplateObjectsForClass(EQPT_NAME),'',['ID'])

######################################################
#@@ Create CSV for import those Aggregate
######################################################
fout.writelines('"ID","ELEMENT_NAME","__AGGREGATE__","link_object_is_from_object_type"\n')
fout.writelines('":R:A:'+subsys+'","BasicNode"\n')

######################################################
#@@ Add library Objects only if it is linked in CDV
######################################################
for n in listAGG:
	if (n[0].replace(EQPT_NAME + ':',''))[0:len(subsys)]==subsys:
		if len(GetInstances(GZIPHelper.unzipToString((CDV.getObjectsLinkedTo(n[0]))),"",['ID'])) > 0 :
			tempAGG=n[0][len(EQPT_NAME)+1:]
			fout.writelines('":R:A:'+subsys+':'+tempAGG[len(subsys)+1:]+'","' + EQPT_NAME + '",'+tempAGG+','+EQPT_NAME+':'+tempAGG+'\n')
	
fout.flush()
fout.close()
			
######################################################
#@@ Get List of Agggregate to be created
######################################################
listVE_list=[]
listVE_list=GetInstances(CDV.listTemplateObjectsForClass('VE'),'',['ID'])
has_VE=0

fname_ve=sys.argv[0].replace('.py','')+'_VE.csv'
fout_ve=open(fname_ve,'w')

fout_ve.writelines('ID,ELEMENT_NAME,name,label,address,deadband,length,type,protocol,address1,SubscriptionPeriod,RefreshPeriod,link_object_is_from_object_type\n')
fout_ve.writelines(':R:A:POLE,POLE,POLE,POLE,,,,\n')
fout_ve.writelines(':R:A:POLE:CTRT,CTRT,CTRT,CTRT,,,,,ctrt,0\n')
fout_ve.writelines(':R:A:POLE:CTRT:'+subsys+',GRP_VE,'+subsys+',,,,,,,,0,0\n')

for ve in listVE_list:
	if ve[0][len('VE')+1:len('VE')+1+len(subsys)]==subsys:
		if len(GetInstances(GZIPHelper.unzipToString((CDV.getObjectsLinkedTo(ve[0]))),"",['ID'])) > 0 :
			ve_type="AEIV"
			tempVE=ve[0][len('VE')+1:]
			if tempVE.count("_AO_")>0:
				ve_type="AEOV"
			fout_ve.writelines(':R:A:POLE:CTRT:'+subsys+':'+tempVE+',VE,'+tempVE+','+tempVE+',temp,,4,'+ve_type+',,,,,'+ve[0]+'\n')
			has_VE=1

fout_ve.close()

I.createVED(ved,'Library objects from ' + vdc + ' version '+I.getCDVRelease(vdc)+' of '+subsys+' system ',vdc)
stdPrint("Please wait while importing...")
I.ImportCSV(ved,fname)
if has_VE==1:
	I.ImportCSV(ved,fname_ve)
stdPrint("Finished loading from library.")
	

CDV.close()
I.unlock(vdc)
I.unlock(ved)

