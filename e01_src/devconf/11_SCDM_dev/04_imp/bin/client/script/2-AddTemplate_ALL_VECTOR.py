#! /usr/bin/env jython
# Author: Katherine SHIH
# ===================================================
#   Script to Update Template Objects
# ===================================================
#	public String createTemplateObject(String templateObjectName,
#                                       String templateObjectType,
#										**String prototypeUID
#                                       --String parent,		<--removed
#                                       --boolean isGraphic		<--removed)
# ===================================================					
#	public void removeTemplateObject(String templateObjectID)			
# ===================================================				
#   public void updateTemplateObject ( String templateObjectID, String sourceVDCName, **boolean isFromPrototype)	
# ===================================================				
#   public void setTemplateObject (String templateObject, **boolean isFromPrototype, boolean clearEmptyField)
# ===================================================				
#	void addItemInDomainValue(String domainValueID,
#                              String itemName,
#                              String itemValue)
# ===================================================					
#	void delItemInDomainValue(String itemID)
# ===================================================					

#=========================
#1. get source Table
# dal: format, label, severity, state, value
#=========================
#2. get Template Table
#=========================
#3. set Template Table
#=========================

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
	stdPrint(ref_file + " not exist!") 
	sys.exit()

fso=open(ref_file,"r")
read_str=fso.readline()

if len(read_str) > 0:
	vdc=read_str.split("=")[1].replace("\r","").replace("\n","")
	ved=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	# INF=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	# INF = INF + "_"
else :
	stdPrint("Cannot read CDV name from config file! Exiting...")
	sys.exit()

fso.close()

#stdPrint(vdc + " " + ved + " " + INF )

#stdPrint("Press [Enter] to continue with CDV(" +vdc+ "), WDS (" + ved + ") and suffix (" + INF + ") or [Ctrl+C] to end")
#if len(sys.stdin.read(1))<>0 :
#	sys.exit(0)
	
#####################################################
## Confrim with the CDV, VED name and suffix
#####################################################


I.unlock(ved)
I.unlock(vdc)
CDV=I.GetConfiguration(vdc,1) 	
VED=I.GetConfiguration(ved)

dal_type_VECTOR=['format', 'label', 'severity', 'state', 'value']
dio_type_VECTOR=['dovname','label','value']

cmb_type_VECTOR=['v0','v1']
aal_type_VECTOR=['v0','v1','v2','v3','v4']
dac_type_VECTOR=['v0','v1','v2','v3']
#dov_type_VECTOR=['v0','v1','v2','v3']

elementlist=['dal_type', 'dio_type','aal_type','dac_type','dal_type12','dal_type32','dal_aut_type','dal_aut_type12','dal_aut_type32','cmb_type']
attrlist=[dal_type_VECTOR, dio_type_VECTOR, aal_type_VECTOR, dac_type_VECTOR,dal_type_VECTOR,dal_type_VECTOR,dal_type_VECTOR,dal_type_VECTOR,dal_type_VECTOR,cmb_type_VECTOR]
vectorlist=['V_valueTable_','V_valueTable_','V_valueLimits','V_combineList','V_valueTable_','V_valueTable_','V_valueTable_','V_valueTable_','V_valueTable_','V_combineList']

maxLength=8


# use aal_type as element name for aal_type_VECTOR_valueLimits modification
# use dac_type as element name for dac_type_VECTOR_combineList modification

e=0
count_totalerr=0
count_totalchg=0
count_err2=0
count1=0
count_change=0

stdPrint("CDV is " + vdc )
stdPrint("WDS is " + ved + "\n")

if VED.getAttachedVDCName()!=vdc :
	stdPrint("VED \"" + ved + "\" is not attached to VDC \"" + vdc + "\".")
else:
	al=0
	for element in elementlist:
		AGGlist=I.Query(ved,"//"+element)
		GetTemp=[]
		GetTemp=GetInstances(CDV.listTemplateObjectsForClass(element),"",['ID'])
		count_err2 = 0
		count1 = 0
		count_change=0
		
		for AGG in AGGlist:
			#name of current node
			Class_name=AGG[0].split(":")[4]
			INF=AGG[0].split(":")[3]+"_"
			
			# 5: pointname
			AGG_NAME=INF+Class_name+"_"+AGG[0].split(":")[5].split("-").pop()
			
			if element.startswith('dal_type'):
				NODE_NAME=AGG[0].split(":")[6]
				if not NODE_NAME.endswith('al'):
					AGG_NAME=AGG_NAME+"_"+NODE_NAME
			
			#Alias of parent node
			AGG_parent=AGG[0]
			
			I.unlock(vdc)
			
			stdPrint("Get Template Object " + element + ":" + AGG_NAME + "*****")
			if element=='aal_type' or element=='dac_type':
				try:
					tab=GetInstances(VED.getNodeAttributes(AGG[0]+":"+vectorlist[al]),"",attrlist[al])[0]
					xmlString=CDV.getTemplateObject(element+":"+AGG_NAME+":"+vectorlist[al])
					xmlString_org=xmlString
					dest=GetInstances(xmlString,"",attrlist[al])[0]
					for i in range(0 , len(attrlist[al])):
						xmlString=xmlString.replace(' '+attrlist[al][i]+'="'+dest[i].replace("&","&amp;")+'"',' '+attrlist[al][i]+'="'+tab[i].replace("&","&amp;")+'"')
						if dest[i]!=tab[i]:
							stdPrint("Replace " + attrlist[al][i] + "=" + dest[i].replace("&","&amp;") + " to " + tab[i].replace("&","&amp;"))
					if xmlString_org!=xmlString :
						CDV.setTemplateObject(xmlString,0,1)
						CDV.updateTemplateObject(element+":"+AGG_NAME,vdc,0)
						count_change=count_change+1
						stdPrint("Modify of " + element+":"+AGG_NAME + " succeed")
				except:
					traceback.print_exc()
					count_err2=count_err2+1
					stdPrint("Modify of " + element+":"+AGG_NAME + " failed!!")
						
				else:
					stdPrint("No Update on Template object " + element+":"+AGG_NAME)
			
			else:
				if element=='dal_type12':
					maxLength=12
				if element=='dal_type32':
					maxLength=32
					
				try:
					for v in attrlist[al]:
						title_arr=['ID']
						for v_len in range(0,maxLength):
							title_arr.append('v' + str(v_len))
							
						tab=GetInstances(VED.getNodeAttributes(AGG[0]+":"+vectorlist[al]+v),"",title_arr)[0]
						xmlString=CDV.getTemplateObject(element+":"+AGG_NAME+":"+vectorlist[al]+v)
						xmlString_org=xmlString
						dest=GetInstances(xmlString,"",title_arr)[0]
						for i in range(0 , maxLength):
							if dest[i+1].replace("&","&amp;") != tab[i+1].replace("&","&amp;") :
								stdPrint("replace " + v + " v" + str(i) + ': ' + dest[i+1].replace("&","&amp;") + " to " + tab[i+1].replace("&","&amp;"))
								xmlString=xmlString.replace(' v'+str(i)+'="'+dest[i+1].replace("&","&amp;")+'"',' v'+str(i)+'="'+tab[i+1].replace("&","&amp;")+'"')
							
						if xmlString_org!=xmlString:
							CDV.setTemplateObject(xmlString,0,1)
							CDV.updateTemplateObject(element+":"+AGG_NAME,vdc,0)
							stdPrint("Modify of " + element+":"+AGG_NAME + " succeed")
							count_change=count_change+1
						else:
							stdPrint("No Update on Template object " + element+":"+AGG_NAME)
				except:
					traceback.print_exc()
					count_err2=count_err2+1
					stdPrint("Modify of " + element+":"+AGG_NAME + " failed!!")
			#stdPrint("Modify of " + element+":"+AGG_NAME + " succeed")
			stdPrint("==================================================")
			count1=count1+1
		al=al+1
		stdPrint(">>>Create of Templates //" + element + " finished with " + str(count_err2) + " error, " + str(count_change) + " change out of " + str(count1) + " elements.\n")
		count_totalerr = count_totalerr + count_err2
		count_totalchg = count_totalchg + count_change

#sys.stdout.write "Number of change(s) made: " + str(count_change)	
stdPrint("\n### Total number of error(s): " + str(count_totalerr))
stdPrint("### There are " + str(count_totalchg) + " change(s) made successfully.")
		
VED.close()
CDV.close()
I.unlock(vdc)
I.unlock(ved)

stdPrint("\n\n\nProgram halt.\n\n\n")
