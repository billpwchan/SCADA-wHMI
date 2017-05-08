/**************************************************************/
/* Company         : TCS                             */
/* Filemane        : SigHILCServerAcc.h                        */
/*------------------------------------------------------------*/
/* Purpose         :                                          */
/*                                                            */
/**************************************************************/
/* Creation        : 28/04/2015                              */
/* Author          :                                      */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions
#ifndef __SIGHILCSERVERACC_H__
#define __SIGHILCSERVERACC_H__

#include <vector>
#include "scs.h"
#include "scsuserstatus.h"
//#include "SigHILCServer.hh"

using namespace std;

/// SigHILC command return callback
/* not used for the time being
typedef void (*SigHILCCommandReturnCallback)( const char *i_vs_environment,
        const char *i_vs_commandName,
        ScsStatus   i_vU_returnStatus,
        const char *i_errorMsg);
*/

class SigHILCServerProxy;
class SigHILCClient_i;

/// Class descriptive text
class SigHILCServerAcc
{
public:

    //------------------------------------------------
    /// Constructor
    //------------------------------------------------
	/*
    SigHILCServerAcc( string                       i_vs_logicalEnv,	        // Logical environment name
                      SigHILCCommandReturnCallback  i_vU_returnCallback = 0);// Command return callback
	*/
    SigHILCServerAcc( string                       i_vs_logicalEnv);	        // Logical environment name
	
    //------------------------------------------------
    /// Destructor :
    //------------------------------------------------
    ~SigHILCServerAcc();
    //------------------------------------------------
    /// Gets the status of the connection to the SIGHILC
    /// server process
    //------------------------------------------------
    inline ScsStatus getStatus() const
    {
        return _vU_status;
    };



    ////////////////////////////////////////////////////// Preparation Requests //////////////////////////////////////////////////////             CmdPreparationRequest

    ScsUserStatus HILCPreparationRequest (
        string i_sOperatorName,
        string i_sWorkStationName,
        unsigned short  i_sCmdType,
        short  i_sCmdValue,
		unsigned short i_sCmdValueDiv,
        string i_sEquipmentAlias,
        string i_sEquipmentType,
        string i_sCommandName);



    ////////////////////////////////////////////////////// Confirmation request  //////////////////////////////////////////////////////               CmdConfirmRequest

    ScsUserStatus HILCConfirmRequest (
        string i_sOperatorName,
        string i_sWorkStationName,
        unsigned short  i_sCmdType,
        short  i_sCmdValue,
		unsigned short i_sCmdValueDiv,
        string i_sEquipmentAlias,
        string i_sEquipmentType,
        string i_sCommandName);

    ////////////////////////////////////////////////////// Cancel request  //////////////////////////////////////////////////////               CmdCancelRequest

/* BHE : besoin pas encore exprime
    ScsUserStatus HILCCancelRequest (
        string i_sOperatorName,
        string i_sWorkStationName,
        short  i_sCmdType,
        short  i_sCmdValue,
        string i_sEquipmentAlias,
        string i_sEquipmentType,
        string i_sCommandName);
*/

private:

    /// Trace
    void Trace( string sTrace      /** buffer */,
                int iLevel=5       /** level of trace */ ) const;

    void TraceS2( string sFunctionName      /** name of the function */,
                  string sFunctionParameter /** parameter of the function */ ) const;

    void TraceE2( string sFunctionName      /** name of the function */,
                  bool bReturn              /** true if the ends with success */ ) const;

    void TraceException( string sFunction  /** function name */,
                         string sException /** exception explanation */ ) const;

    /// Private attribute members
    SigHILCServerProxy *_pU_proxy;

    /// Internal status (updated by the constructor)
    ScsStatus          _vU_status;

    /// Client pointer for callback
    SigHILCClient_i    *_pU_client;

    /**
    * Copy and default constructor are private and not implemented.
    */

    /**
     * Assignment operator is private and not implemented.
     */
    SigHILCServerAcc( const SigHILCServerAcc& );
    SigHILCServerAcc();

    /**
     * Assignment operator is private and not implemented.
     */
    SigHILCServerAcc& operator=( const SigHILCServerAcc& );


};


#endif // __SIGHILCSERVERACC_H__
