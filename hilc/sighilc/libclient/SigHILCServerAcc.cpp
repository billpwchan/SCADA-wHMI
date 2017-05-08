/**************************************************************/
/* Company         : TCS                              */
/* Filemane        : SigHILCServerAcc.cpp                   */
/*------------------------------------------------------------*/
/* Purpose         :                                          */
/*                                                            */
/**************************************************************/
/* Creation        : 28/04/2015                            */
/* Author          :                                     */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

#include "scadaorb.h"
#include "manager.h"
#include "SigTraces.h"
#include "ascconnection.h"
#include "SigHILCServerAcc.h"
#include "SigHILCServerproxy.hh"
//#include "SigHILCClient_i.h"
#include "redutil.h"
//Not used yet, comes from atserror.emf #include "atserror.h"
#include <sstream>

using namespace std;


//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Method     : SigHILCServerAcc()
//
// Desciption : constructor of SigHILCServerAcc class
//
// In         : i_vs_logicalEnv      SCADAsoft logical environment name
//              i_vU_returnCallback  command return callback function
//
// Error      : none
//
//---------------------------------------------------------------
SigHILCServerAcc::SigHILCServerAcc(
    string                       i_vs_logicalEnv)//,
    //SigHILCCommandReturnCallback  i_vU_returnCallback)
    : _pU_proxy(0), _vU_status(ScsValid), _pU_client(0)
{
  // stringstream sBuffer;
  // sBuffer << "LOGICAL_ENV=" << i_vs_logicalEnv.c_str();
  // cout<<"SigHILCServerAcc::SigHILCServerAcc"<< sBuffer.str()<<endl;

    CREATE_PROXY(SigHILCServer, i_vs_logicalEnv.c_str(), "SigHILCServer", _pU_proxy, _vU_status);

	/*
    if( i_vU_returnCallback )
    {
        CREATE_SERVANT( SigHILCClient_i*,SigHILCClient_i,_pU_client,new SigHILCClient_i(i_vs_logicalEnv.c_str(), i_vU_returnCallback) );
    }// if( i_vU_returnCallback )
	*/
}


//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Method     : ~SigHILCServerAcc()
//
// Desciption : destructor of SigHILCServerAcc class
//
// In         :
//
//
// Error      : none
//
//---------------------------------------------------------------
SigHILCServerAcc::~SigHILCServerAcc()
{
    if (_pU_proxy)
        delete _pU_proxy;
    _pU_proxy = 0;

	/* not used for the time being
    if (_pU_client)
        DESTROY_SERVANT(SigHILCClient_i,_pU_client);
	*/
}

//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Methode    : Trace
//
// Desciption : trace written mormally
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
void SigHILCServerAcc::Trace(string sTrace, int iLevel /*=5*/) const
{
    stringstream sBuffer;

    // buffer
    sBuffer << "###SIGHILC### " << sTrace;

    // trace
    SigTrace( SCS_LEVEL(iLevel), sBuffer.str().c_str() );
    //cout<<sBuffer.str().c_str()<<endl;
}

//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Methode    : TraceException
//
// Desciption : exception trace written mormally
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
void SigHILCServerAcc::TraceException(string sFunction, string sException) const
{
    stringstream sBuffer;

    // buffer
    sBuffer << "###SIGHILC### CORBA Exception : " << sException;

    // trace
    SigTrace( SCS_LEVEL(0), sBuffer.str().c_str() );
    //cerr<<sBuffer.str().c_str()<<endl;
}
//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Methode    : TraceS2
//
// Desciption : trace written when the function begins
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
void SigHILCServerAcc::TraceS2( string sFunctionName, string sFunctionParameter) const
{
    stringstream sBuffer;

    // function name
    sBuffer << "START " << sFunctionName;

    // function parameters
    if( sFunctionParameter.size()>0 )
        sBuffer << " ( " << sFunctionParameter << " )";

    // trace
    Trace( sBuffer.str(), 2 );
}

//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Methode    : TraceE2
//
// Desciption : trace written when the function ends
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : -
//
//---------------------------------------------------------------
void SigHILCServerAcc::TraceE2( string sFunctionName, bool bReturn) const
{
    stringstream sBuffer;

    // return type
    sBuffer << "END ";
    if( bReturn )
        sBuffer << "with SUCCESS";
    else
        sBuffer << "with ERROR";

    // function name
    sBuffer << "- " <<sFunctionName;

    // trace
    Trace( sBuffer.str(), 2 );
}



/******************************************************************************************/
/*******************************************HILC*******************************************/
/******************************************************************************************/


//------------------------------------------------------------
// HILC CBI Preparation
//------------------------------------------------------------

//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Method     : HILCCBIPreparationRequest
//
// Desciption : Reset request
//
// In         : i_sCommandName : command name
//
// Out        :
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
ScsUserStatus SigHILCServerAcc::HILCPreparationRequest	(
    string i_sOperatorName,
    string i_sWorkStationName,
    unsigned short  i_sCmdType,
    short  i_sCmdValue,
	unsigned short i_sCmdValueDiv,
    string i_sEquipmentAlias,
    string i_sEquipmentType,
    string i_sCommandName)
{

    // Build requester name
    stringstream sBuffer;
    sBuffer << "OperatorName=" << i_sOperatorName
    << " WorkStationName=" << i_sWorkStationName
    << " CmdType=" << i_sCmdType
    << " CmdValue=" << i_sCmdValue
    << " CmdValueDiv=" << i_sCmdValueDiv
    << " EquipmentAlias=" << i_sEquipmentAlias
    << " EquipmentType=" << i_sEquipmentType
    << " CommandName=" << i_sCommandName;

    this->TraceS2( "SigHILCServerAcc::HILCPreparationRequest", sBuffer.str() );

    //------------------------------------------------------------
    // declaration
    //------------------------------------------------------------
    ScsStatus vU_status = ScsValid;

	/* Not used for the time being
    SigHILCClientIdl_var tmpSigHILCClientIdl = NULL;

    if (_pU_client)
    {
        tmpSigHILCClientIdl = _pU_client->_this();
    }
	*/
    //------------------------------------------------------------
    // call the service
    //------------------------------------------------------------
    try
    {
        // Convert string to CORBA::String_Var
        CORBA::String_var operatorName = CORBA::string_dup(i_sOperatorName.c_str());
        CORBA::String_var workStationName = CORBA::string_dup(i_sWorkStationName.c_str());
        CORBA::String_var equipmentAlias = CORBA::string_dup(i_sEquipmentAlias.c_str());
        CORBA::String_var equipmentType = CORBA::string_dup(i_sEquipmentType.c_str());
        CORBA::String_var commandName = CORBA::string_dup(i_sCommandName.c_str());

        // Send data command to SigHILCServer_i
        vU_status = _pU_proxy->CmdPreparationRequest(operatorName, workStationName, i_sCmdType, i_sCmdValue, i_sCmdValueDiv, equipmentAlias, equipmentType, commandName);
    }
    catch( CORBA::SystemException &sysEx )
    {
        sBuffer.str("");
        sBuffer << sysEx;
        this->TraceException( "SigHILCServerAcc::HILCPreparationRequest", sBuffer.str());
    }

    this->TraceE2("SigHILCServerAcc::HILCPreparationRequest",( vU_status.isValid() ? true : false ) );

    string errorMes;

    if (!vU_status.isValid())
    {
        ostringstream output;
        output<<"Error: "<<vU_status.getMinor()<<endl;
        errorMes = output.str();
    }

    ScsUserStatus stringStatus(vU_status);
    stringStatus.setMessage(errorMes.c_str());

    return stringStatus;
}




//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Method     : HILCCbiConfirmRequest
//
// Desciption : HILC Confirmation request for CBI server
//
// In         : i_VitalCheckSumS1    : Vital checksum 1
//            : i_VitalCheckSumS2    : Vital checksum 2
//				      i_sCommandName : command name
//
// Out        :
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
ScsUserStatus SigHILCServerAcc::HILCConfirmRequest	(
    string i_sOperatorName,
    string i_sWorkStationName,
    unsigned short  i_sCmdType,
    short  i_sCmdValue,
	unsigned short i_sCmdValueDiv,
    string i_sEquipmentAlias,
    string i_sEquipmentType,
    string i_sCommandName)

{


    // Build requester name
    stringstream sBuffer;
    sBuffer << "OperatorName=" << i_sOperatorName
    << " WorkStationName=" << i_sWorkStationName
    << " CmdType=" << i_sCmdType
    << " CmdValue=" << i_sCmdValue
    << " CmdValueDiv=" << i_sCmdValueDiv
    << " EquipmentAlias=" << i_sEquipmentAlias
    << " EquipmentType=" << i_sEquipmentType
    << " CommandName=" << i_sCommandName;

    this->TraceS2( "SigHILCServerAcc::HILCConfirmRequest", sBuffer.str() );

    //------------------------------------------------------------
    // declaration
    //------------------------------------------------------------
    //build sequence
    ScsStatus vU_status = ScsValid;
	
	/* not used for the time being
    SigHILCClientIdl_var tmpSigHILCClientIdl = NULL;

    if (_pU_client)
    {
        tmpSigHILCClientIdl = _pU_client->_this();
    }
	*/
	
    //------------------------------------------------------------
    // call the service
    //------------------------------------------------------------
    try
    {
        // Convert string to CORBA::String_Var
        CORBA::String_var operatorName = CORBA::string_dup(i_sOperatorName.c_str());
        CORBA::String_var workStationName = CORBA::string_dup(i_sWorkStationName.c_str());
        CORBA::String_var equipmentAlias = CORBA::string_dup(i_sEquipmentAlias.c_str());
        CORBA::String_var equipmentType = CORBA::string_dup(i_sEquipmentType.c_str());
        CORBA::String_var commandName = CORBA::string_dup(i_sCommandName.c_str());

        // Send data command to SigHILCServer_i
        vU_status = _pU_proxy->CmdConfirmRequest(operatorName, workStationName, i_sCmdType, i_sCmdValue, i_sCmdValueDiv, equipmentAlias, equipmentType, commandName);
    }
    catch( CORBA::SystemException &sysEx )
    {
        sBuffer.str("");
        sBuffer << sysEx;
        this->TraceException( "SigHILCServerAcc::HILCConfirmRequest", sBuffer.str());
    }

    this->TraceE2("SigHILCServerAcc::HILCConfirmRequest",( vU_status.isValid() ? true : false ) );

    string errorMes;

    if (!vU_status.isValid())
    {
        ostringstream output;
        output<<"Error: "<<vU_status.getMinor()<<endl;
        errorMes = output.str();
    }

    ScsUserStatus stringStatus(vU_status);
    stringStatus.setMessage(errorMes.c_str());

    return stringStatus;
}


//---------------------------------------------------------------
// Class      : SigHILCServerAcc
// Method     : HILCCbiCancelRequest
//
// Desciption : HILC Cancel request for CBI server
//
// In         : i_VitalCheckSumS1    : Vital checksum 1
//            : i_VitalCheckSumS2    : Vital checksum 2
//				      i_sCommandName : command name
//              i_sRequester      : requester
//
// Out        :
//
// Error      : ScsValid or error
//
//---------------------------------------------------------------
/* BHE: besoin pas encore exprime
ScsUserStatus SigHILCServerAcc::HILCCancelRequest	(
    string i_sOperatorName,
    string i_sWorkStationName,
    short  i_sCmdType,
    short  i_sCmdValue,
    string i_sEquipmentAlias,
    string i_sEquipmentType,
    string i_sCommandName,
    string i_sRequesterId)

{


    // Build requester name
    stringstream sBuffer;
    sBuffer << "OperatorName=" << i_sOperatorName
    << " WorkStationName=" << i_sWorkStationName
    << " CmdType=" << i_sCmdType
    << " CmdValue=" << i_sCmdValue
    << " EquipmentAlias=" << i_sEquipmentAlias
    << " EquipmentType=" << i_sEquipmentType
    << " CommandName=" << i_sCommandName
    << " Requester=" << i_sRequesterId;

    this->TraceS2( "SigHILCServerAcc::HILCCancelRequest", sBuffer.str() );

    //------------------------------------------------------------
    // declaration
    //------------------------------------------------------------
    //build sequence
    ScsStatus vU_status = ScsValid;

    SigHILCClientIdl_var tmpSigHILCClientIdl = NULL;

    if (_pU_client)
    {
        tmpSigHILCClientIdl = _pU_client->_this();
    }

    //------------------------------------------------------------
    // call the service
    //------------------------------------------------------------
    try
    {
        // Convert string to CORBA::String_Var
        CORBA::String_var operatorName = CORBA::string_dup(i_sOperatorName.c_str());
        CORBA::String_var workStationName = CORBA::string_dup(i_sWorkStationName.c_str());
        CORBA::String_var equipmentAlias = CORBA::string_dup(i_sEquipmentAlias.c_str());
        CORBA::String_var equipmentType = CORBA::string_dup(i_sEquipmentType.c_str());
        CORBA::String_var commandName = CORBA::string_dup(i_sCommandName.c_str());
        CORBA::String_var requesterId = CORBA::string_dup(i_sRequesterId.c_str());


        // Send data command to SigHILCServer_i
        vU_status = _pU_proxy->CmdCancelRequest(operatorName, workStationName, i_sCmdType, i_sCmdValue, equipmentAlias, equipmentType, commandName, requesterId, tmpSigHILCClientIdl);
    }
    catch( CORBA::SystemException &sysEx )
    {
        sBuffer.str("");
        sBuffer << sysEx;
        this->TraceException( "SigHILCServerAcc::HILCCancelRequest", sBuffer.str());
    }

    this->TraceE2("SigHILCServerAcc::HILCCancelRequest",( vU_status.isValid() ? true : false ) );

    string errorMes;

    if (!vU_status.isValid())
    {
        ostringstream output;
        output<<"Error: "<<vU_status.getMinor()<<endl;
        errorMes = output.str();
    }

    ScsUserStatus stringStatus(vU_status);
    stringStatus.setMessage(errorMes.c_str());

    return stringStatus;
}
*/

