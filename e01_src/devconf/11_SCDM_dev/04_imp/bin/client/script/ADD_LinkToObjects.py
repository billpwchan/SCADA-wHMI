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

#####################################################
## read CDV name, VED name and suffix from ref_file
#####################################################
ref_file="config_"+os.environ['USERNAME']+".txt"
if not os.path.exists(ref_file) :
	stdPrint(ref_file + " not exist!" )
	sys.exit()

fso=open(ref_file,"r")
read_str=fso.readline()

if len(read_str) > 0:
	vdc=read_str.split("=")[1].replace("\r","").replace("\n","")
	ved=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	
else :
	stdPrint("Cannot read CDV name from config file! Exiting...")
	sys.exit()

fso.close()

#vdc="DHLR1"
#ved="KS_11_HMISym"

fname='temp_link_object.csv'
fout=open(fname,'w')


I.unlock(ved)
I.unlock(vdc)
CDV=I.GetConfiguration(vdc,1)
VED=I.GetConfiguration(ved)

elementlist=['ucs_type','dci_mod4_type','dal_aut_type12','dal_aut_type32''dal_aut_type','Number','Text','soe_only_type','dal_type32','ufe_float','ufe_date','ufe_string','ufes','ufe_int','Symbol','HMITags','Tag','dci_cb_type', 'dci_soe_type', 'aci_cb_type', 'SCI_TYPE', 'dio_type', 'aio_type', 'sio_type', 'dac_type', 'dal_type','aal_type','dov_type','UserFields','dal_type12','dco_type', 'sco_type', 'UserFields', 'PointUserFields', 'NodeUserFields']
parent_id=""
filter_str=""

e=0
count_err1=0
count_err2=0
count1=0

stdPrint("CDV is " + vdc)
stdPrint("WDS is " + ved)

if VED.getAttachedVDCName()!=vdc :
	stdPrint("VED " + ved + " is not attached to VDC " + vdc)
else:
	al=0
	fout.writelines('"ID","link_object_is_from_object_type"'+'\n')
	
	for element in elementlist:
		AGGlist=I.Query(ved,parent_id+"//"+element+filter_str+ "[@link_object_is_from_object_type='']")
		stdPrint(element)
		GetTemp=[]
		GetTemp=GetInstances(CDV.listTemplateObjectsForClass(element),"",['ID','nom_instance'])
		
		for AGG in AGGlist:
			#name of current node
			Class_name=AGG[0].split(":")[5][:4]
			INF=AGG[0].split(":")[4]+"_"
			
			if len(AGG[0].split(":"))>6 :
				AGG_NAME=INF+Class_name+"_"+AGG[0].split(":")[6].split("-").pop()
			else :
				AGG_NAME=INF+Class_name
			# 5: pointname
			if element=='dov_type':
				AGG_NAME=AGG_NAME+"_"+AGG[0].split(":")[7].split("-").pop()
				
			# if (INF=='POW_' or INF=='POL_' or INF=='POD_' or INF=='OHL_' or INF=='UPS_' or INF+Class_name=='MCS_DUM_' or INF+Class_name=='MCS_P24S' or INF+Class_name=='MCS_P48S' or INF+Class_name=='MCS_P24T' or INF+Class_name=='MCS_P48T') and element.count('dal_type')==1 :
				# AGG_NAME=AGG_NAME+"_"+AGG[0].split(":")[7]
			if element.startswith('dal_type'):
				NODE_NAME=AGG[0].split(":")[6]
				if not NODE_NAME.endswith('al'):
					AGG_NAME=AGG_NAME+"_"+NODE_NAME
				
			if element=='Number' or element=='Text' or element=='Symbol' or element=='Tag' or element=='ucs_type':
				AGG_NAME=AGG_NAME+"_"+AGG[0].split(":")[7]
			
			if element=='ufe_int' or element=='ufe_string' or element=='ufe_date' or element=='ufe_float':
				temp_ufes=AGG[0].split(":")
				temp_ufe=temp_ufes.pop()
				AGG_NAME=INF+temp_ufes.pop()[:4]+"_"+temp_ufe
				
				
			#Alias of parent node
			AGG_parent=AGG[0]
			if element=='dov_type':
				if not AGG[0].split(":")[6].split("-").pop().startswith("SEL"): 
					fout.writelines(AGG[0]+","+element + ":" + AGG_NAME+"\n")
			else :
				if element=='dal_10_type' :
					if not AGG[0].split(":")[6].split("-").pop().startswith("SEL"): 
						fout.writelines(AGG[0]+","+element + ":" + AGG_NAME+"\n")
				else:
					fout.writelines(AGG[0]+","+element + ":" + AGG_NAME+"\n")
		#end for
		stdPrint("Create of links finished with " + str(count_err2) + "/" + str(count1) + " errors.")
	
	fout.flush()
	fout.close()
	stdPrint("Please wait while adding links!...")
	I.ImportCSV(ved,fname)
	stdPrint("Finished adding links.")
	


VED.close()
CDV.close()
I.unlock(vdc)
I.unlock(ved)

