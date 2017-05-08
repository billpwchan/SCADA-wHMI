/**************************************************************/
/* Company         : TCS                             */
/* Filemane        : SigHILCServer_i.h                         */
/*------------------------------------------------------------*/
/* Purpose         :                                          */
/*                                                            */
/**************************************************************/
/* Creation        : 27/04/2015                       */
/* Author          : SigHILC                                      */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions
#ifndef __SIGHILCSERVER_I_H__
#define __SIGHILCSERVER_I_H__

#include "scs.h"
#include "imgmsgtag.h"
#include "RdnServer.h"
#include "SigHILCServer.hh"
#include "SigHILCServer_soap.hh"
#include "SessionManager.h"


class CtlCmdCommand;


///Class SigHILCServer_i
class SigHILCServer_i : public virtual SigHILCServerSOAP, public RdnServer
{
public:

    //------------------------------------------------
    /// Constructor
    //------------------------------------------------
    SigHILCServer_i();
    //------------------------------------------------
    /// Destructor : Free memory allocated
    //------------------------------------------------
    virtual ~SigHILCServer_i();
    //------------------------------------------------
    /// Initialize internal pointers
    //------------------------------------------------
    virtual void Init();


    /// status
    //------------------------------------------------
    inline ScsStatus getStatus(void) const
    {
        return _vU_status;
    };

    //------------------------------------------------------------------------
    /// Save and restore methods for redundancy purpose (using SnapshotHelper)
    //------------------------------------------------------------------------
    virtual int preForegroundDump();
    virtual int postForegroundDump();
    virtual int preLoad();
    virtual int postLoad();


    //////////////////////////////////////////// HILC ////////////////////////////////////////////////////////////////////

    //------------------------------------------------
    /// HILC CBI Preparation requests
    //------------------------------------------------

    virtual ScsStatusIdl CmdPreparationRequest(
        const char * sOperatorName,
        const char * sWorkStationName,
        ::CORBA::UShort sCmdType,
        ::CORBA::Short sCmdValue,
		::CORBA::UShort sCmdValueDiv,
        const char * sEquipmentAlias,
        const char * sEquipmentType,
        const char * sCommandName)            
    throw (CORBA::SystemException );


    //------------------------------------------------
    /// HILC CBI Confirmation request
    //------------------------------------------------

    virtual ScsStatusIdl CmdConfirmRequest (
        const char * sOperatorName,
        const char * sWorkStationName,
        ::CORBA::UShort sCmdType,
        ::CORBA::Short sCmdValue,
		::CORBA::UShort sCmdValueDiv,
        const char * sEquipmentAlias,
        const char * sEquipmentType,
        const char * sCommandName )		 
    throw (CORBA::SystemException );


    //------------------------------------------------
    /// HILC CBI Cancel request
    //------------------------------------------------
    /* BHE : besoin pas encore exprime
      virtual ScsStatusIdl CmdCancelRequest (
          const char * sOperatorName,
          const char * sWorkStationName,
          ::CORBA::Short sCmdType,
          ::CORBA::Short sCmdValue,
          const char * sEquipmentAlias,
          const char * sEquipmentType,
          const char * sCommandName,
          const char * sRequesterId,
          ::SigHILCClientIdl_ptr pClient )		            // callback
        throw (CORBA::SystemException );
    */


private:

    SessionManager* m_SessionManager;


    /// Pending command manager
    //static SigCtlCmdManager _commandManager;
    /// Redundancy message tag provider
    static ImgMsgTag*    _tagProvider;

    /// Internal status
    ScsStatus       _vU_status;

    /// Internal pointers
};

#endif // __SIGHILCSERVER_I_H__
