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
	#INF=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	#INF = INF + "_"
else :
	stdPrint("Cannot read CDV name from config file! Exiting")
	sys.exit()

fso.close()

#stdPrint(vdc + " " + ved + " " + INF)

#stdPrint("Press [Enter] to continue with CDV(" +vdc+ "), WDS (" + ved + ") and suffix (" + INF + ") or [Ctrl+C] to end")
#if len(sys.stdin.read(1))<>0 :
#	sys.exit(0)
	
#####################################################
## Confrim with the CDV, VED name and suffix
#####################################################

	
#vdc="SIL"
#ved="AGG_TMS_A1H"
#INF="TMS_"

I.unlock(ved)
I.unlock(vdc)

CDV=I.GetConfiguration(vdc,1)
VED=I.GetConfiguration(ved)


basic_EQPT_attr=['ID','shortLabel','type','hdvFlag','resrvTimeout']
VE_attr=['ID','functTrans','deadband']
	
dci_type_attr=['ID', 'label', 'hmiOrder', 'userField1', 'Archive', 'reportType','computedMessage','timestampSelection']
aci_type_attr=['ID', 'label', 'hmiOrder', 'unit', 'Archive', 'reportType','computedMessage','NumDec']
sci_type_attr=['ID', 'label', 'hmiOrder']
dio_type_attr=['ID', 'label', 'hmiOrder', 'nature']
aio_type_attr=['ID', 'label', 'hmiOrder', 'unit', 'initCond1', 'initCond2', 'initCond3', 'initCond4', 'initCond5', 'returnCond1', 'returnCondTO']
sio_type_attr=['ID', 'label', 'hmiOrder']
dac_type_attr=['ID', 'nature', 'CFGPOLE']
dal_type_attr=['ID', 'DIAlmCEBehaviour', 'theme', 'ackAutomaton', 'valueAutomaton', 'inhibit','hidden']
dal_aut_type_attr=['ID', 'DIAlmCEBehaviour', 'theme', 'ackAutomaton', 'valueAutomaton', 'inhibit','hidden']
aal_type_attr=['ID', 'AIAlmCEBehaviour', 'theme', 'ackAutomaton', 'valueAutomaton','inhibit','hidden']
dov_type_attr=['ID', 'ctlvalue', 'CFGVECOORD', 'initCond1', 'initCond2', 'initCond3', 'initCond4', 'initCond5', 'returnCond1', 'returnCondTO', 'initCondTO']
ucs_type_attr=['ID','DI_pointname','DO_pointname','delay']
#--! reserve ,'initCond6', 'initCond7', 'initCond8' for list level configuration
MODE_attr=['ID', 'modeStatus']
UserFields_attr=['ID', 'ce_intf1']

Tag_attr=['ID', 'value','label']
HMITags_attr=['ID', 'label', 'type']
Symbol_attr=['ID', 'value', 'color', 'label']
Number_attr=['ID', 'value', 'color', 'label']
Text_attr=['ID', 'value', 'color', 'label']
ufes_attr=['ID', 'label']
ufe_attr=['ID', 'computedValue', 'value']

dco_type_attr=['ID', 'computedDate', 'computedStatus', 'computedValue']
sco_type_attr=['ID', 'computedDate', 'computedStatus', 'computedValue']
HSER_attr=['ID', 'type']
POST_attr=['ID', 'type']

UserFields_attr=['ID', 'ce_intf1', 'ce_intf2', 'intf1', 'intf2', 'ce_datef1', 'ce_datef2']

parent_id = ""
filter_str=""

EQPT_NAME="SGEQPT8"

elementlist=['ucs_type','dci_mod4_type','dal_aut_type','Number', 'Text', 'soe_only_type', 'dal_type32', 'ufe_date', 'ufe_float', 'ufe_string', 'ufe_int', 'ufes', 'Symbol', 'HMITags', 'Tag', 'MODE', 'HSER', 'POST', EQPT_NAME, 'dco_type', 'dci_cb_type', 'dci_soe_type', 'aci_cb_type', 'SCI_TYPE', 'dio_type', 'aio_type', 'sio_type', 'dac_type', 'dal_type', 'aal_type', 'dov_type', 'VE', 'dal_type12', 'sco_type', 'UserFields', 'PointUserFields', 'NodeUserFields']
attrlist=[ucs_type_attr,dci_type_attr,dal_aut_type_attr,Number_attr, Text_attr, dci_type_attr, dal_type_attr, ufe_attr, ufe_attr, ufe_attr, ufe_attr, ufes_attr, Symbol_attr, HMITags_attr, Tag_attr, MODE_attr, HSER_attr, POST_attr, basic_EQPT_attr, dco_type_attr, dci_type_attr, dci_type_attr, aci_type_attr, sci_type_attr, dio_type_attr, aio_type_attr, sio_type_attr, dac_type_attr, dal_type_attr, aal_type_attr, dov_type_attr,VE_attr,dal_type_attr,sco_type_attr,UserFields_attr,UserFields_attr,UserFields_attr]

e=0
count_totalerr=0
count_totalchg=0
count_err2=0
count1=0
count_change=0

stdPrint("CDV is " + vdc)
stdPrint("WDS is " + ved + "\n")

if VED.getAttachedVDCName()!=vdc :
	stdPrint("VED \"" + ved + "\" is not attached to VDC \"" + vdc + "\".")
else:
	al=0
	for element in elementlist:
		AGGlist=I.Query(ved,parent_id+"//"+element+filter_str)
		
		stdPrint(parent_id+"//"+element)
		stdPrint("[INFO] " + os.time.strftime("%x %X") + " - Start ELEMENT:" + element)
		GetTemp=[]
		GetTemp=GetInstances(CDV.listTemplateObjectsForClass(element),"",['ID'])
		count1=0
		count_err2=0
		count_change=0
		
		for AGG in AGGlist:
			#name of current node
			Class_name=AGG[0].split(":")[4]
			INF=AGG[0].split(":")[3]+"_"
			
			if len(AGG[0].split(":"))>5 :
				AGG_NAME=INF+Class_name+"_"+AGG[0].split(":")[5].split("-").pop()
			else :
				AGG_NAME=INF+Class_name

			if element.startswith('dal_type'):
				NODE_NAME=AGG[0].split(":")[6]
				if not NODE_NAME.endswith('al'):
					AGG_NAME=AGG_NAME+"_"+NODE_NAME
				
			# 5: pointname
			if element=='dov_type':
				AGG_NAME=AGG_NAME+"_"+AGG[0].split(":")[6].split("-").pop()
			
			if element=='VE':
				AGG_NAME =AGG[0].split(":")[6]
				
			if element=='Text' or element=='Number' or element=='Symbol' or element=='Tag'  or element=='ucs_type':
				AGG_NAME=AGG_NAME+"_"+AGG[0].split(":")[6]	
			
			if element=='ufe_int' or element=='ufe_string' or element=='ufe_date' or element=='ufe_float':
				temp_ufes=AGG[0].split(":")
				temp_ufe=temp_ufes.pop()
				AGG_NAME=INF+temp_ufes.pop()+"_"+temp_ufe
				
			#Alias of parent node
			AGG_parent=AGG[0]
			
			I.unlock(vdc)
			e=0
			
			try:
				#CDV.removeTemplateObject(element+":"+AGG_NAME)
				if GetTemp.count([element + ":" + AGG_NAME])==0:
					stdPrint("Template " +element+ ":"+AGG_NAME + " not exist!")
					stdPrint("Create Template " +element+ ":"+AGG_NAME + "=========")
					CDV.createTemplateObject(AGG_NAME,element, "")
				
			except:
				e=1
				stdPrint("Create Template " +element+ ":"+AGG_NAME + " failed")
				traceback.print_exc()
				count_err2=count_err2+1
			
			if e==0:
				# Get value from WDS and library
				src=GetInstances(VED.getNodeAttributes(AGG[0]),"",attrlist[al])[0]
				xmlString_par=CDV.getTemplateObject(element+":"+AGG_NAME)
				xmlString_org=xmlString_par
				dest=GetInstances(xmlString_par,"",attrlist[al])[0]
				# Replace value in library with value in WDS
				a=0
				for attrs in attrlist[al]:
					#CASE attributes not = ID/type
					if attrs=='ID':
						stdPrint('ID: ' + dest[a])
					if attrs!='ID' :
						if dest[a]!=src[a] :
							if attrs!='hdvFlag' or (attrs=='hdvFlag' and src[a]!='1'):
								# for pound sign, it need to be replaced with &#163; instead
								stdPrint('Replace Attribute [.' + attrs + '] from "' + dest[a].replace("&","&amp;").replace(">","&gt;").replace("<","&lt;").replace('"',"&quot;").replace("'","&apos;") + '" to "' + src[a].replace("&","&amp;").replace(">","&gt;").replace("<","&lt;").replace('"',"&quot;").replace("'","&apos;") + '"')
								xmlString_par=xmlString_par.replace(' ' + attrs + '="'+dest[a].replace("&","&amp;").replace(">","&gt;").replace("<","&lt;").replace('"',"&quot;").replace("'","&apos;")+'"', ' ' + attrs + '="'+src[a].replace("&","&amp;").replace(">","&gt;").replace("<","&lt;").replace('"',"&quot;").replace("'","&apos;")+'"')
						
					a=a+1
				
				
				if xmlString_org != xmlString_par :
					try:
						CDV.setTemplateObject(xmlString_par,0,1)
						CDV.updateTemplateObject(element+":"+AGG_NAME,vdc,0)
						count_change=count_change+1
						stdPrint("Modify of " + element+":"+AGG_NAME + " succeed")
					except:
						traceback.print_exc()
						count_err2=count_err2+1
						stdPrint("Modify of " + element+":"+AGG_NAME + " failed!!")
					
				else:
					stdPrint("No update on the Template " + element+":"+AGG_NAME)
			stdPrint("==================================================")
			count1=count1+1
		al=al+1
		#end for
		stdPrint("[INFO] " + os.time.strftime("%x %X") + " - End ELEMENT:" + element)
		stdPrint(">>>Create of Templates //" + element + " finished with " + str(count_err2) + " error, " + str(count_change) + " change out of " + str(count1) + " elements.\n")
		count_totalerr = count_totalerr + count_err2
		count_totalchg = count_totalchg + count_change

	stdPrint("\n### Total number of error(s): " + str(count_totalerr))
	stdPrint("### There are " + str(count_totalchg) + " change(s) made successfully.")

		
VED.close()
CDV.close()
I.unlock(vdc)
I.unlock(ved)

stdPrint("\n\n\nProgram halt.\n\n\n")
