#! /usr/bin/env jython
# ===================================================
#   CfgInterface.py
#
#     Configurator client API Wrapper
# ===================================================
import sys
import os
import traceback
import xml.sax
import java.util.Iterator
import java.util.Collection
import atexit
import socket
from com.thalesis.config.client.gui.util import XwingMLSingleton
from com.thalesis.config.utils import GZIPHelper
from com.thalesis.config.client.gui.util.importdata import ImportDataManager

retCode = 0

# =========================================
#    Procedure to manipulate Xml Result
# =========================================
class GetInstancesHandler(xml.sax.handler.ContentHandler):
    def __init__(self,result,tag,columns):
        self.result = result
        self.tag = tag
        self.columns = columns
        pass
    def startElement(self, name, attrs):
        ok=1
        if (self.tag == "") | (self.tag == name):
            if self.columns == "":
                self.result.append(str(attrs.items()))
            elif self.columns == []:
                row={}
                row['qname']=name
                for col in attrs.keys():
                    row[col]=str(attrs.getValue(col))
                self.result.append(row)
            else:
                for col in self.columns:
                    if (col != "qname") & (attrs.has_key(col) == 0) :
                        ok = 0
                if (ok):
                    row=[]
                    for col in self.columns:
                        if col == "qname" :
                            row.append(name)
                        else:
                            row.append(str(attrs.getValue(col)))
                    self.result.append(row)
        pass

def GetInstances(xmlReport,tag="",columns=[]):
    result = []
    xml.sax.parseString(xmlReport,GetInstancesHandler(result,tag,columns))
    return result

def GetKeyAndValue(xmlReport,key,value):
    columns=[]
    columns.append(key)
    columns.append(value)
    result = GetInstances(xmlReport,"",columns)
    dicResult={}
    for keyAndValue in result:
        dicResult[keyAndValue[0]]=keyAndValue[1]
    return dicResult

def printList(list):
    for row in list:
        print str(row)

# =========================================
#    Disconnect object before exit
# =========================================
ObjectList=[]

def disconnect():
    print "Exit handler nbObjects:" + str(len(ObjectList))
    while len(ObjectList)>0:
        obj=ObjectList[0]
        obj.Disconnect()
    
atexit.register(disconnect)

# =========================================
#    Interface wrapper
# =========================================
class CfgInterface:    
    def __init__(self):
        self.ved=None
        self.r=XwingMLSingleton.getInstance()
        self.Connect()
        
    def __del__(self):
        self.Disconnect()
        
    def Connect(self):
        ObjectList.append(self)
        if (self.r.myFactory == None):
            identifier=os.getlogin() + "_" + socket.gethostname()
            try:
                self.r.RMIFactoryConnect(identifier,"Administration")
            except:        
                # even if autentification failed, the RMI access is available
                if (self.r.myFactory == None):
                    raise
                    

    def Disconnect(self):
        e=None
        try:
            ObjectList.remove(self)
        except:        
            e=sys.exc_info()
        
        if (self.r.myFactory != None):            
            if self.ved != None:
                try:
                    self.ved.close()
                    self.ved=None
                except:
                    e=sys.exc_info()
                
            try:
                self.r.myFactory.closeClientConnection()
            except:
                e=sys.exc_info()
                
        self.r.myFactory = None
    
    # =========================================
    #    Procedure to Get a configuration (WDS/CDV)
    # =========================================
    def GetConfiguration(self, cfgName, writable=0):
        cfg=None
        if self.listCDV().contains(cfgName):
            if (writable):
                cfg = self.r.myFactory.getWritableVDC(cfgName)
            else:
                cfg = self.r.myFactory.getReadableVDC(cfgName)
        elif self.listVED().contains(cfgName):
            if (writable):
                vdcName=self.GetAttachedVDCName(cfgName)
                self.ved=self.r.myFactory.getWritableVED(cfgName,vdcName)
                cfg = self.ved
            else:
                cfg = self.r.myFactory.getReadableVED(cfgName)                    
        
        if cfg == None:
            raise "[ERROR] Configuration " + cfgName + " not found"
            
        return cfg
    

    # =========================================
    #    Procedure to Query
    # =========================================
    def Query(self,cfgName,query,columns=['ID']):
        result=[]
        
        try:
            cfg = self.GetConfiguration(cfgName)
            result = GetInstances(GZIPHelper.unzipToString(cfg.query(query,1,1)),"", columns)
            result.sort()
            
        except:
            print "Query - Exception raised"  + query
            traceback.print_exc()
        
        return result
    
    # =========================================
    #    Procedure to import
    # =========================================
    def ImportCSV(self,vedname,pathName,importNodeBeforeFlag=0):
        retCode=0

        try:
            
            fileList=java.util.Vector()            
            if os.path.isdir(pathName):
                for file in os.listdir(pathName):
                    fileList.add(os.path.join(pathName,file))                    
            else:
                fileList.add(pathName)
            
            nodeList=[]
            if importNodeBeforeFlag == 1:
                for file in fileList:
                    csvFile=open(file,"r")
                    cnt=0
                    for line in csvFile.readlines() :
                        id=line.split(',')[0].replace('"','') 
                        if cnt == 0:
                            if id != "ID":
                                break
                        else:
                            parentpath=id.split(':')
                            parentpath.pop()
                            parentpath=':'.join(parentpath)
                            try:
                                nodeList.index(parentpath)
                            except:
                                nodeList.append(parentpath)
                            
                        cnt=cnt+1
                        
                    csvFile.close()
                    
                if len(nodeList)>0:
                    print "CfgInterface::ImportCSV - IMPORT nodes to the WDS:" + str(nodeList)
                    self.addNodes(vedname,nodeList,0,1)
            
            vdcName=self.GetAttachedVDCName(vedname)
            
            e=None
            
            ved=self.GetConfiguration(vedname,1)

            try:
                dataMgt=ImportDataManager()            
            
                        
                for file in fileList:
                    print "CfgInterface::ImportCSV - IMPORT file:" + file + " in VED:" + vedname + " linked to VDC:" + vdcName
                    print dataMgt.importData(ved,java.io.File(file),1,1)

            except:
                e=sys.exc_info()
                
            ved.close()
                        
            if (e!=None):
                raise(e[2].dumpStack())
            
        except:
            traceback.print_exc()
            retCode = 1
                            
        return retCode    
        
    # =========================================
    #    Procedure to get the VDC linked to a VED
    # =========================================
    def GetAttachedVDCName(self,vedname):
        vdcName=""
        
        ved = self.r.myFactory.getReadableVED(vedname)
        vdcName=ved.getAttachedVDCName()

        return vdcName                

    # =========================================
    #   Validate a WDS
    # =========================================
    def validateVED(self,vedname):
        e=None
        result=""
        
        ved = self.GetConfiguration(vedname, 1)
        try:
            ved.validate()
        except:
            e=sys.exc_info()
            
        ved.close()
        
        if (e!=None):
            raise(e[2].dumpStack())

        return result        

    # =========================================
    #   Integrate a WDS
    # =========================================
    def integrateVED(self,vedname):
        e=None
        result=[]        
        
        vdcName = self.GetAttachedVDCName(vedname)
        vdc = self.r.myFactory.getWritableVDC(vdcName)
            
        try:
            xmlResult=GZIPHelper.unzipToString(vdc.integrateVED(vedname,1))
            columns=['ID']
            columns.append(vedname)
            columns.append(vdcName)
            result=GetInstances(xmlResult,"diffelement",columns)                
        except:
            e=sys.exc_info()
            
        vdc.close()
            
        if (e!=None):
            raise(e[2].dumpStack())

        return result        
        
    # =========================================
    #    listValidatedVED
    # =========================================
    def listValidatedVED(self):
        return self.r.myFactory.listValidatedVED()            
        
    # =========================================
    #    listVED
    # =========================================
    def listVED(self):
        return self.r.myFactory.listVED()                

    # =========================================
    #    list
    # =========================================
    def list(self):
        print "====CDV====="
        for cfgName in self.listCDV():
            locked=""
            try:
                self.GetConfiguration(cfgName,1).close()
                locked="available"
            except:
                locked="locked"

            print " - " + cfgName + " " + locked
            
        print "====WDS====="
        for cfgName in self.listVED():
            locked=""
            try:
                self.GetConfiguration(cfgName,1).close()
                locked="available"
            except:
                locked="locked"

            print " - " + cfgName + " " + locked

    # =========================================
    #    createVED
    # =========================================
    def createVED(self,vedName,description,vdcName):
        self.r.myFactory.createVED(vedName,description,vdcName)
        self.attachedVED2VDC(vedName,vdcName) 
        return 

    # =========================================
    #    attachedVED2VDC
    # =========================================
    def attachedVED2VDC(self,vedName,vdcName):
        ved = self.r.myFactory.getWritableVED(vedName, vdcName)            
        ved.close()
        return 

    # =========================================
    #    deleteVED
    # =========================================
    def deleteVED(self,vedName):
        return self.r.myFactory.deleteVED(vedName)                

    # =========================================
    #    createCDV
    # =========================================
    def createCDV(self,vdcName,vdcRef,clone=0):
        self.r.myFactory.createVDC(vdcName,"",vdcRef,clone)
        self.unlock(vdcName)
        return

    # =========================================
    #    unlockCDV
    # =========================================
    def unlockCDV(self,vdcName):
        self.unlock(vdcName)
        return
        
    def unlock(self,cfgName):
        cfg = self.GetConfiguration(cfgName)
        cfg.close()
        return

    # =========================================
    #    deleteCDV
    # =========================================
    def deleteCDV(self,vdcName):
        return self.r.myFactory.deleteVDC(vdcName)                

    # =========================================
    #    getVEDProperties
    # =========================================
    def getVEDProperties(self,vedName):
        xmlResult=self.r.myFactory.getVEDProperties(vedName)
        result=GetKeyAndValue(xmlResult,"name","value")
        return result                

    # =========================================
    #    listCDV
    # =========================================
    def listCDV(self):
        return self.r.myFactory.listVDC()                

    # =========================================
    #    listGeneratedCDV
    # =========================================
    def listGeneratedCDV(self):
        xmlResult=self.r.myFactory.listGeneratedVDC()
        return GetInstances(xmlResult,"",['name','dateDeGeneration'])


    # =========================================
    #    getCDVProperties
    # =========================================
    def getCDVProperties(self,vdcName):
        xmlResult=self.r.myFactory.getVDCProperties(vdcName)    
        result=GetKeyAndValue(xmlResult,"name","value")
        return result                            

    # =========================================
    #    getCDVRelease
    # =========================================
    def getCDVRelease(self,vdcName):
        return self.getCDVProperties(vdcName)["revision number"]                            

    # =========================================
    #    getCDVState
    # =========================================
    def getCDVState(self,vdcName):
        return self.getCDVProperties(vdcName)["control"]                            

    # =========================================
    #    diffVEDs
    # =========================================
    def diffVEDs(self,ved1Name,ved2Name,classList=[]):
        xmlResult=self.r.myFactory.diffVEDs(ved1Name,ved2Name,java.util.Vector(classList))
        result=GZIPHelper.unzipToString(xmlResult)
        columns=['ID']
        columns.append(ved1Name)
        columns.append(ved2Name)
        return GetInstances(result,"",columns)

    # =========================================
    #    diffVEDfromVDC
    # =========================================
    def diffVEDfromVDC(self,vedName,classList=[]):
        vdcName=self.GetAttachedVDCName(vedName)
        xmlResult=self.r.myFactory.diffVEDfromVDC(vedName,vdcName,java.util.Vector(classList))
        result=GZIPHelper.unzipToString(xmlResult)
        columns=['ID']
        columns.append(vedName)
        columns.append(vdcName)
        return GetInstances(result,"",columns)

    # =========================================
    #    diffVDCs
    # =========================================
    def diffVDCs(self,vdcName1,vdcName2,classList=[]):
        xmlResult=self.r.myFactory.diffVDCs(vdcName1,vdcName2,java.util.Vector(classList),0)
        result=GZIPHelper.unzipToString(xmlResult)
        columns=['ID']
        columns.append(vdcName1)
        columns.append(vdcName2)
        return GetInstances(result,"",columns)


    # =========================================
    #    diffDetails
    # =========================================
    def diffDetails(self,cfgName1,cfgName2,diffPoint,ignoreComputed=1):
        diffNode=[]
        columns=['attribut']
        columns.append(cfgName1)
        columns.append(cfgName2)
        value2=diffPoint.pop()
        value1=diffPoint.pop()
        node=diffPoint.pop()
        cfg=self.GetConfiguration(cfgName1)
        try:
            xmlResult=GZIPHelper.unzipToString(self.r.myFactory.diffNode(node,cfgName1,cfgName2))
            for diffAttr in GetInstances(xmlResult,"",columns):
                value2=diffAttr.pop()
                value1=diffAttr.pop()
                attr=diffAttr.pop()
                ignore=0
                if ignoreComputed:
                    xmlResult=GZIPHelper.unzipToString(cfg.getZippedTree(node,1))
                    className=GetInstances(xmlResult,"",['qname'])[0][0]
                    ignore=cfg.listDerivedAttributes(className).contains(attr)
                if ((ignore==0) & (value1 != value2)):
                    diffNode.append([node + "." + attr,value1,value2])
        except:
            diffNode.append([node,value1,value2])
                
        return diffNode
                
    # =========================================
    #    diffVDCsDetails
    # =========================================
    def diffVDCsDetails(self,vdcName1,vdcName2,classList=[],ignoreComputed=1):
        instances = self.diffVDCs(vdcName1,vdcName2,classList)
        result=[]
        for diffPoint in instances:
            result.extend(self.diffDetails(vdcName1,vdcName2,diffPoint,ignoreComputed))

        result.sort()
        
        return result
        
    # =========================================
    #    diffVEDfromVDCDetails
    # =========================================
    def diffVEDfromVDCDetails(self,vedName,classList=[],ignoreComputed=1):
        vdcName=self.GetAttachedVDCName(vedName)
        instances = self.diffVEDfromVDC(vedName,classList)
        result=[]
        for diffPoint in instances:
            result.extend(self.diffDetails(vedName,vdcName,diffPoint,ignoreComputed))
            
        result.sort()

        return result

    # =========================================
    #    computeCDV
    # =========================================
    def computeCDV(self,vdcName):
        e=None
        result=""
        vdc = self.r.myFactory.getWritableVDC(vdcName)
        
        try:
            result=vdc.computeAttributes()
        except:
            e=sys.exc_info()
            
        vdc.close()

        if (e!=None):
            raise(e[2].dumpStack())

        return result

    # =========================================
    #    checkCDV
    # =========================================
    def checkCDV(self,vdcName):
        e=None
        result=[]
        vdc = self.r.myFactory.getWritableVDC(vdcName)
        
        try:
            result=GetInstances(vdc.check(0),'message',['object_id','attribute','error_level','error_msg'])
        except:
            e=sys.exc_info()
            
        vdc.close()

        if (e!=None):
            raise(e[2].dumpStack())
            
        return result

    # =========================================
    #    generateCDV
    # =========================================
    def generateCDV(self,vdcName):
        e=None
        result=[]
        vdc = self.r.myFactory.getWritableVDC(vdcName)
        
        try:
            vdcRelease=vdcName+"_"+self.getCDVRelease(vdcName)
            try:
                self.r.myFactory.deleteGeneratedVDC(vdcRelease)
            except:
                e=None
                
            result=GZIPHelper.unzipToString(vdc.generate())
            result=GetInstances(result,'message',['operation','error_level','error_msg'])
            
        except:
            e=sys.exc_info()
            
        vdc.close()
        
        if (e!=None):
            raise(e[2].dumpStack())
            
        return result

    # =========================================
    #    restoreCDV
    # =========================================
    def restoreCDV(self,vdcName):
        self.r.myFactory.restoreVDC(vdcName,vdcName)    

    # =========================================
    #    backupCDV
    # =========================================
    def backupCDV(self,vdcName,backupName='',comment=''):
        if backupName == '':
            backupName=vdcName + "_" + self.getCDVRelease(vdcName)
            
        if comment == '':
            comment=backupName
            
        self.r.myFactory.backupVDC(vdcName,backupName,comment)    

    # =========================================
    #    deleteCDVBackup
    # =========================================
    def deleteCDVBackup(self,vdcName):
        self.r.myFactory.deleteVDCBackup(vdcName)

    # =========================================
    #    getBackupCDV
    # =========================================
    def listBackupCDV(self):
        xmlResult=self.r.myFactory.getAllVDCBackups()
        return GetInstances(xmlResult,"",["text"])

    # =========================================
    #    getChildren
    # =========================================
    def getChildren(self,cfgName,node=""):
        nodeList=[]
        e=None
        
        cfg = self.GetConfiguration(cfgName)
        if node == "":
            node = cfg.getRootID()
        try:
            xmlResult=GZIPHelper.unzipToString(cfg.getZippedTree(node,2))
            for child in GetInstances(xmlResult,"",['ID','ELEMENT_STATUS']):
                if child[0] != node :
                    nodeList.append(child[0])
        except:
            e=sys.exc_info()
            
        cfg.close()
            
        if (e!=None):
            raise(e[2].dumpStack())
        
        return nodeList
        
    # =========================================
    #    getClassName
    # =========================================
    def getClassName(self,cfgName,node=""):
        className=[]
        e=None
        cfg = self.GetConfiguration(cfgName)
        if node == "":
            node = cfg.getRootID()
        try:
            xmlResult=GZIPHelper.unzipToString(cfg.getZippedTree(node,1))
            className=GetInstances(xmlResult,"",['qname'])[0][0]
        except:
            e=sys.exc_info()            
            
        cfg.close()

        if (e!=None):
            raise(e[2].dumpStack())
        
        return className
        
    # =========================================
    #    getNode
    # =========================================
    def getNode(self,cfgName,node=""):
        nodeValues=[]
        e=None
        cfg = self.GetConfiguration(cfgName)
        if node == "":
            node = cfg.getRootID()
        try:
            xmlResult=cfg.getFullNodeAttributes(node)
            nodeValues=GetInstances(xmlResult)[0]
        except:
            e=sys.exc_info()
        cfg.close()

        if (e!=None):
            raise(e[2].dumpStack())
        
        return nodeValues
    
    # =========================================
    #    copyNode
    # =========================================
    def copyNode(self,vedName,nodeSrc,nodeTarget):
        e=None
        ved = self.GetConfiguration(vedName, 1)
        
        try:
            path=nodeTarget.split(':')
            instName=path.pop()
            ved.copyNode(instName,":".join(path),nodeSrc,vedName,0,0)
        except:
            e=sys.exc_info()
            
        ved.close()

        if (e!=None):
            raise(e[2].dumpStack())
        
    # =========================================
    #    addNodes
    # =========================================
    def addNodes(self,vedName,nodeList, recursive=0, skelchildren=1):
        e=None
        ved = self.GetConfiguration(vedName, 1)
        
        try:
            for node in nodeList:
                if node.__class__ == [].__class__:
                    node=node.pop()
                
                print "CfgInterface::addNodes " + node
                try:
                    xmlResult=GZIPHelper.unzipToString(ved.getZippedTree(node,1))
                    child=GetInstances(xmlResult,"",['ID','ELEMENT_STATUS'])[0]
                    if child[1] == "SKEL":
                        raise "SKEL"
                    print "CfgInterface::addNodes " + node + " already included."
                except:    
                    try:
                        ved.editNode(node,recursive)
                        if skelchildren == 0 :
                            xmlResult=GZIPHelper.unzipToString(ved.getZippedTree(node,2))
                            for child in GetInstances(xmlResult,"",['ID','ELEMENT_STATUS']):
                                if child[1] == "SKEL":
                                    ved.deleteNode(child[0])
                    except:    
                        print "CfgInterface::addNodes " + node + " Failed"
                        traceback.print_exc()

        except:
            e=sys.exc_info()
            
        ved.close()

        if (e!=None):
            raise(e[2].dumpStack())

    # =========================================
    #    deleteNodes
    # =========================================
    def deleteNodes(self,vedName,nodeList):
        e=None
        ved = self.GetConfiguration(vedName, 1)
        
        try:
            for node in nodeList:
                if node != []:
                    print "CfgInterface::deleteNodes " + str(node)
                    
                    if node.__class__ == [].__class__:
                        node=node.pop()
                        
                        
                    #include the parent
                    parentpath=node.split(':')
                    parentpath.pop()
                    parentpath=":".join(parentpath)
                                        
                    try:
                        xmlResult=GZIPHelper.unzipToString(ved.getZippedTree(parentpath,1))
                        for child in GetInstances(xmlResult,"",['ID','ELEMENT_STATUS']):
                            if child[1] == "SKEL":
                                ved.editNode(parentpath,0)
                    except:                    
                        ved.editNode(parentpath,0)
                        
                    try:
                        # delete the node
                        ved.deleteNode(node)
                    except:                    
                        print "CfgInterface::deleteNodes " + node + " failed."
                        traceback.print_exc()
        except:
            e=sys.exc_info()
            
        ved.close()
        
        if (e!=None):
            raise(e[2].dumpStack())


    # =========================================
    #    getIntegratedWDS
    # =========================================
    def getIntegratedWDS(self,cdvName):
        return GetInstances(self.r.myFactory.getVDCIntegrationProperties(cdvName),"child",["text"])

if __name__ == '__main__':
    def help():
        print "HELP:"
        print "I is an instance of CfgInterface() that wrap some Configurator API"
        print ""
        print "  - I.Connect() connect to the configurator"
        print "  - I.Disconnect() disconnect from the configurator"
        print ""
        print "  - I.listVED() get the list of WDS"
        print "  - I.listValidatedVED() get the list of validated WDS"
        print "  - I.createVED(vedName,description,vdcName) create a WDS"
        print "  - I.deleteVED(vedName) delete a WDS"
        print "  - I.getVEDProperties(vedName) get the properties of a WDS"
        print "  - I.validateVED(vedName) validate a WDS"
        print "  - I.integrateVED(vedName) integrate a WDS in its attached CDV"
        print "  - I.diffVEDs(ved1Name,ved2Name,classList=[],ignoreComputed=1) compare two VEDs"
        print "  - I.diffVEDfromVDC(vedName,classList=[],ignoreComputed=1) compare a WDS to its attached CDV"
        print "  - I.diffVEDfromVDCDetails(vedName,classList=[],ignoreComputed=1) compare a WDS to its attached CDV"
        print "  - I.addNodes(vedName,nodelist,recursive=0,skelchildren=1) add nodes from a WDS"
        print "  - I.deleteNodes(vedName,nodelist) remove nodes from a WDS"
        print "  - I.copyNode(vedName,nodeSrc,nodeTarget) copy node inside a WDS"
        print ""
        print "  - I.listCDV() get the list of CDV"
        print "  - I.listGeneratedCDV() get the list of generated CDV"
        print "  - I.listBackupCDV() get the list of backup CDV"
        print "  - I.getCDVProperties(vdcName) get the properties of a CDV"
        print "  - I.getCDVRelease(vdcName) get the release number of a CDV"
        print "  - I.createCDV(vdcName,vdcRefName,clone=0) create a CDV"
        print "  - I.restoreCDV(vdcName) create a CDV from a backup"
        print "  - I.backupCDV(vdcName,,backupName='',comment='') backup a CDV"
        print "  - I.computeCDV(vdcName) compute a CDV"
        print "  - I.checkCDV(vdcName) check  a CDV"
        print "  - I.generateCDV(vdcName) generate a CDV"
        print "  - I.diffVDCs(vdcName1,vdcName2,classList=[],ignoreComputed=1) compare 2 CDVs"
        print "  - I.diffVDCsDetails(vdcName1,vdcName2,classList=[],ignoreComputed=1) compare 2 CDVs"
        print "  - I.getIntegratedWDS(vdcName) get list of integrated WDS in a CDV"
        print ""
        print "  - I.Query(cfgName,query,columns=['ID']) query a CDV/WDS"
        print "  - I.ImportCSV(vedname,csvfile,importNodeBeforeFlag=0) import CSV files into a WDS"
        print "  - I.unlock(cfgname) try to remove lock"
        print "  - I.list() list CDV and WDS"
        print ""
        print "help() print again this."
    
    I=CfgInterface()
    help()
    
# ===================================================    
    