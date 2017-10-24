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
	# INF=fso.readline().split("=")[1].replace("\r","").replace("\n","")
	# INF = INF + "_"
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
#ved="TMP_ESI_A3_2C"
#INF="ESI_"
ved=ved.replace(ved[0:3],"TMP")

eqpt="SGEQPT8"

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
	AGGlist=I.Query(ved,parent_id+"//"+eqpt+filter_str)
	AGGLib=[]
	AGGLib=GetInstances(CDV.listAggregates(eqpt,0),"",['ID'])
	
	for AGG in AGGlist:
	#	AGG=AGGlist[0]
		AGG_str=AGG[0].split(":").pop()
		AGG_parent=AGG[0]
		INF=AGG[0].split(":")[3]+"_"
		#AGG_parent=AGG[0].split(":")
		#AGG_str=AGG_parent.pop()
		#AGG_parent=":".join(AGG_parent)
		#stdPrint(AGG_str)
		#stdPrint(AGG_parent)
		nodelist = I.Query(ved,parent_id+"//"+eqpt+"[@nom_instance='"+AGG_str+"']/*",['ID','UNIVNAME'])
		#stdPrint("//"+eqpt+"[@nom_instance='"+AGG_str+"']/*")

		for n in nodelist:
			a.append(n[0])
			
		I.unlock(vdc)
		e=0
		
		try:
			if AGGLib.count([':AA:'+INF+AGG_str])!=0 :
				stdPrint("Delete Aggregate :AA:"+INF+AGG_str + "====="	)
				CDV.deleteAggregate(":AA:"+INF+AGG_str)
				stdPrint("Delete Aggregate :AA:"+INF+AGG_str + " Succeed.")
		except:
			stdPrint("Delete of " + INF + AGG_str + " failed")
			traceback.print_exc()
			e=1
			count_err1=count_err1+1
		
		#if e==0 :
		#	stdPrint("Delete Aggregate :AA:"+INF+AGG_str + " Succeed.")
		stdPrint("=====================================")
		
		e=0
		try:
			stdPrint("Create Aggregate "+INF+AGG_str + "=========")
			VED.createAggregate(INF+AGG_str,AGG_parent,ved,java.util.Vector(a),0)
		except:
			stdPrint("Create of " + INF+AGG_str + " failed")
			traceback.print_exc()
			e=1
			count_err2=count_err2+1
		
		if e==0 :
			stdPrint("Create of Aggregate :AA:"+INF+AGG_str + " Succeed.")
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