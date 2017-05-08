/**************************************************************/
/* Company         : THALES IS                                */
/* Filemane        : SigHILCServer_i.cpp                    */
/*------------------------------------------------------------*/
/* Purpose         :                                          */
/*                                                            */
/**************************************************************/
/* Creation        :                              */
/* Author          :                                     */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Instruction for generation tracability : please, do not remove
static char ccase_id[] = "@(#) $CCfile: -- $ $Revision: -- $ $Date: -- $ $Project: -- $" ;

#include <deque>
#include <sstream>

#include "scs.h"
#include "dbm.h"
#include "ctlcmd.h"
#include "ctlerror.h"
#include "RdnTraces.h"

#include "SigHILCServer_i.h"

//Not used yet #include "SigError.h"
#include "SigTraces.h"
#include "HILCdefs.h"

#include <iostream>

using namespace std;

/// Redundancy message tag provider
ImgMsgTag*    SigHILCServer_i::_tagProvider = 0;

//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : SigHILCServer_i()
//
// Description : constructor of SigHILCServer_i class
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
SigHILCServer_i::SigHILCServer_i() : _vU_status(ScsValid)/*, m_pSigHILCServer(0) - TO DO*/
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SigHILCServer_i::ctor");
    _tagProvider = new ImgMsgTag;
    m_SessionManager = SessionManager::getInstance();

}



//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : ~SigHILCServer_i()
//
// Description : destructor of SigHILCServer_i class
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
SigHILCServer_i::~SigHILCServer_i()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "SigHILCServer_i::dtor");
    m_SessionManager = 0;

    delete _tagProvider;
}


//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : Init()
//
// Description : Initialize internals pointers
//
// In         : pServers : pointer to the list of servers
//
// Error      : none
//
//---------------------------------------------------------------
void SigHILCServer_i::Init()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::Init");

    _vU_status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::Init_END");


}

// RDN Server
int SigHILCServer_i::preForegroundDump()
{
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " -> SigHILCServer_i::preForegroundDump()");
    //_commandManager.savePendingCommandList(SnapshotHelper::instance()->getSnapshotOutFile());
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " <- SigHILCServer_i::preForegroundDump()");
    return 0;
}

int SigHILCServer_i::postForegroundDump()
{
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " -> SigHILCServer_i::postForegroundDump() nothing to do...");

    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " <- SigHILCServer_i::postForegroundDump()");
    return 0;
}

int SigHILCServer_i::preLoad()
{
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " -> SigHILCServer_i::preLoad()");
    /*_commandManager.restorePendingCommandList(SnapshotHelper::instance()->getSnapshotInFile());
    unsigned short nbCommands = _commandManager.getListSize();
    ScsCtlMultiplePendingCmd* ctlMultiCommand = 0;
    ScsStatus status;
    for (unsigned short indexCmd = 0; indexCmd < nbCommands; indexCmd++)
      {
        ctlMultiCommand = (ScsCtlMultiplePendingCmd*)_commandManager.getCommand(indexCmd);
        if (ctlMultiCommand)
          {
      for (unsigned short indexSubCmd = 0; indexSubCmd < ctlMultiCommand->getNbSubCommand(); indexSubCmd++)
              {
          if (!ctlMultiCommand->IsSubCommandReceived(indexSubCmd))
                  {
    	  ScsCtlCommand* subCommand = ctlMultiCommand->getSubCommandVector(indexSubCmd);

    	  if (subCommand)
                      {
    	      status = m_pSigHILCServer->m_pCtlCmdServer->sendCommand(subCommand->getNbCommands(),
    									subCommand->getCommandList(),
    									subCommand->getRequester(),
    									ctlMultiCommand);
                      }
                  }
              }
          }
      }
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " <- SigHILCServer_i::preLoad() nbCommands=%d", nbCommands);*/
    return 0;
}

int SigHILCServer_i::postLoad()
{
    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " -> SigHILCServer_i::postLoad() nothing to do...");

    RdnTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " <- SigHILCServer_i::postLoad()");
    return 0;
}


//////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////HILC//////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

// HILC LC Preparation Request

//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : HILCCBIPreparationRequest
//
// Description :  Enter session for HILC command
//
// In         : lOperatorName  : operator name
//              lWorkStationname  : workstation name
//				      sCommandName    : command name
//              sOwnerId        : owner ID
//              pClient         : callback
//
//
// Out        :
//
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
ScsStatusIdl SigHILCServer_i::CmdPreparationRequest(
    const char * sOperatorName,
    const char * sWorkStationName,
    ::CORBA::UShort sCmdType,
    ::CORBA::Short sCmdValue,
	::CORBA::UShort sCmdValueDiv,
    const char * sEquipmentAlias,
    const char * sEquipmentType,
    const char * sCommandName )
throw (CORBA::SystemException)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   SigHILCServer_i::CmdPreparationRequest " );

    stringstream sBuffer;
    sBuffer 	<< " operatorName=" << sOperatorName << " workStationName=" << sWorkStationName << " cmdType=" << sCmdType  << " cmdValue=" << sCmdValue << " cmdValueDiv=" << sCmdValueDiv
    << " equipmentAlias=" << sEquipmentAlias << " equipmentType=" << sEquipmentType << " commandName=" << sCommandName ;

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdPreparationRequest:%s",sBuffer.str().c_str());

    // Declaration of variables
    ScsStatus status = ScsValid;
    ScsStatusIdl statusIdl;
    string op = sOperatorName;
    string ws = sWorkStationName;
    unsigned short ct = sCmdType;
    short cv = sCmdValue;
	unsigned short cvd = sCmdValueDiv;
    string eq = sEquipmentAlias;
    string et = sEquipmentType;
    string cn = sCommandName;

    m_SessionManager = SessionManager::getInstance();
    status = m_SessionManager->prepareCmd(op, ws, ct, cv, cvd, eq, et, cn);

    statusIdl.major = (CORBA::Short)status.getMajor();
    statusIdl.minor = (CORBA::Short)status.getMinor();
	
	// BHE test command return
	// ScsMessageTag tag;
	// pClient->commandReturn(tag, sCommandName, statusIdl, sBuffer.str().c_str());

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdPreparationRequest_END");


    return statusIdl;
}


// HILC CBI Confirmation Request

//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : HILCCbiConfirmRequest
//
// Description :  CBI Confirmationrequest
//
// In         : sCommandName    : command name
//              sOwnerId        : owner ID
//              pClient         : callback
//
//
// Out        :
//
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
ScsStatusIdl SigHILCServer_i::CmdConfirmRequest(
    const char * sOperatorName,
    const char * sWorkStationName,
    ::CORBA::UShort sCmdType,
    ::CORBA::Short sCmdValue,
	::CORBA::UShort sCmdValueDiv,
    const char * sEquipmentAlias,
    const char * sEquipmentType,
    const char * sCommandName )
throw (CORBA::SystemException)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   SigHILCServer_i::CmdConfirmRequest " );

    stringstream sBuffer;

    sBuffer 	<< " operatorName=" << sOperatorName	<< " workStationName=" << sWorkStationName << " cmdType=" << sCmdType << " cmdValue=" << sCmdValue
    << " equipmentAlias=" << sEquipmentAlias << " equipmentType=" << sEquipmentType << " commandName=" << sCommandName	;

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdConfirmRequest:%s",sBuffer.str().c_str());

    // Declaration of variables
    ScsStatus status = ScsValid;
    ScsStatusIdl statusIdl;
    string op = sOperatorName;
    string ws = sWorkStationName;
    unsigned short ct = sCmdType;
    short cv = sCmdValue;
	unsigned short cvd = sCmdValueDiv;
	
    string eq = sEquipmentAlias;
    string et = sEquipmentType;
    string cn = sCommandName;

    m_SessionManager = SessionManager::getInstance();
    status = m_SessionManager->confirmCmd(op, ws, ct, cv, cvd, eq, et, cn);

    statusIdl.major = (CORBA::Short)status.getMajor();
    statusIdl.minor = (CORBA::Short)status.getMinor();

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdConfirmRequest_END");

    return statusIdl;
}



// HILC CBI Cancel Request

//---------------------------------------------------------------
// Class      : SigHILCServer_i
// Method     : HILCCbiCancelRequest
//
// Description :  CBI Cancelrequest
//
// In         : sCommandName    : command name
//              sOwnerId        : owner ID
//              pClient         : callback
//
//
// Out        :
//
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
/* BHE : besoin pas encore exprime
ScsStatusIdl SigHILCServer_i::CmdCancelRequest(
      const char * sOperatorName,
      const char * sWorkStationName,
      ::CORBA::Short sCmdType,
      ::CORBA::Short sCmdValue,
      const char * sEquipmentAlias,
      const char * sEquipmentType,
      const char * sCommandName,
      const char * sRequesterId,
      ::SigHILCClientIdl_ptr pClient  )
  throw (CORBA::SystemException)
{
  SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   SigHILCServer_i::CmdCancelRequest " );

  stringstream sBuffer;

	sBuffer 	<< " operatorName=" << sOperatorName	<< " workStationName=" << sWorkStationName << " cmdType=" << sCmdType  << " cmdValue=" << sCmdValue
		<< " equipmentAlias=" << sEquipmentAlias << " equipmentType=" << sEquipmentType << " commandName=" << sCommandName	<< " OWNER=" << sRequesterId << " CLIENT(hex)=" << hex << pClient;

  SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdCancelRequest:%s",sBuffer.str().c_str());

  // Declaration of variables
  ScsStatus status = ScsValid;
  ScsStatusIdl statusIdl;
  string op = sOperatorName;
  string ws = sWorkStationName;
  unsigned short int ct = sCmdType;
  unsigned short int cv = sCmdValue;
  string eq = sEquipmentAlias;
  string et = sEquipmentType;
  string cn = sCommandName;
  string ri = sRequesterId;

  m_SessionManager = SessionManager::getInstance();
  status = m_SessionManager->cancelCmd(op, ws, ct, cv, eq, et, cn, ri);

  statusIdl.major = (CORBA::Short)status.getMajor();
  statusIdl.minor = (CORBA::Short)status.getMinor();

  SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::CmdCancelRequest_END");

  return statusIdl;
}
*/
