#! /usr/bin/env jython
# Author: Katherine SHIH
# ===================================================
#   Script to Update Aggregate Objects
# ===================================================
#	public String createAggregate( String aggregateName,
#                                               String parent,
#                                               String aggregateComment,
#                                               Collection elementIDs,
#                                               boolean isGraphic ) throws AlreadyExistExcept
# ===================================================					
#	public void deleteAggregate(String aggregateID)			
# ===================================================					

import sys
import os
import traceback
from CfgInterface import CfgInterface
import java.util.Vector
from CfgInterface import printList
from CfgInterface import GetInstances
from Custom import stdPrint

I=CfgInterface()
a=[]

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
	ved=ved.replace(ved[0:3],"TMP")
	# INF=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	# INF = INF + "_"
else :
	stdPrint("Cannot read CDV name from config file! Exiting")
	sys.exit()

fso.close()


baseeqpt="SGEQPT8"

parent_id=""
filter_str=""

I.unlock(ved)
I.unlock(vdc)
CDV=I.GetConfiguration(vdc,1)
VED=I.GetConfiguration(ved,1)

count_err1=0
count1=0
count_err2=0

stdPrint("CDV is " + vdc)
stdPrint("WDS is " + ved)

if VED.getAttachedVDCName()!=vdc :
	stdPrint("VED " + ved + " is not attached to VDC " + vdc)
else:
	#AGGlist=I.Query(ved,parent_id+"//"+eqpt+filter_str)
	AGGlist=I.Query(ved,"//HMITags || //Tags || //ufes")
	AGGLib=[]
	AGGLib=GetInstances(CDV.listAggregates(baseeqpt,0),"",['ID'])
	
	for AGG in AGGlist:
		AGG_eqpt_code=AGG[0].split(":")[4]
		AGG_sys=AGG[0].split(":")[3]
		AGG_node=AGG[0].split(":")[5]
		
		AGG_parent=AGG[0].replace(":"+AGG_node,"")
		AGG_name="ADD_" + AGG_sys + "_" + AGG_eqpt_code + "_" + AGG_node
		
		I.unlock(vdc)
		e=0
		
		try:
			if AGGLib.count([':AA:'+AGG_name])!=0 :
				stdPrint("Delete Aggregate :AA:"+ AGG_name + "====="	)
				CDV.deleteAggregate(":AA:"+AGG_name)
				stdPrint("Delete Aggregate :AA:"+ AGG_name + " Succeed.")
		except:
			stdPrint("Delete of " + AGG_name + " failed")
			traceback.print_exc()
			e=1
			count_err1=count_err1+1
		
		#if e==0 :
		#	stdPrint("Delete Aggregate :AA:"+ AGG_name + " Succeed.")
		stdPrint("=====================================")
		
		e=0
		try:
			stdPrint("Create Aggregate "+AGG_name + "=========")
			VED.createAggregate(AGG_name,AGG_parent,ved,java.util.Vector(AGG),0)
		except:
			stdPrint("Create of " + AGG_name + " failed")
			traceback.print_exc()
			e=1
			count_err2=count_err2+1
		
		if e==0 :
			stdPrint("Create of Aggregate :AA:"+AGG_name + " Succeed.")
		stdPrint("=====================================")
			
		a=[]
		count1=count1+1
			
	#end for
	stdPrint("Delete Aggregate Finished with " + str(count_err1) + "/" + str(count1) + " errors.")
	stdPrint("Create Aggregate Finished with " + str(count_err2) + "/" + str(count1) + " errors.")

CDV.close()
VED.close()
I.unlock(vdc)
I.unlock(ved)