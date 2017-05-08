/**************************************************************/
/* Company   : TCS                                    */
/* Filemane  : InitHILCServer.cpp                              */
/*------------------------------------------------------------*/
/* Purpose   : implementation of class which contains the     */
/*             instance pointers of all servers               */
/*                                                            */
/**************************************************************/
/* Creation  : 30/04/2015                                   */
/* Author    :                                             */
/*------------------------------------------------------------*/
/* Modification  :                                            */
/* Author        :                                            */
/* Action        :                                            */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Instruction for generation tracability : please, do not remove
static char ccase_id[] = "@(#) $CCfile: -- $ $Revision: -- $ $Date: -- $ $Project: -- $" ;

#include "scadaorb.h"
#include "SnapshotHelper.h"   // redondance
#include "asc.h"
#include "dbm.h"
#include "ctlcmd.h"
#include "SigConst.h"
#include "SigTraces.h"
#include "SigError.h"
#include "scsint_Traces.hpp"
#include "HILCdefs.h"

#include "InitHILCServer.h"
#include "HILC_ConfManager.h"
#include "SessionManager.h"

#include "scsalmext.h"
#include <sstream>
#include <iostream>
#include <stddef.h>

#ifdef WIN32
#include <direct.h> //_chdir
#endif

using namespace std;

//---------------------------------------------------------------
// Class    : InitHILCServer
// Methode  : InitHILCServer
//
// Desciption : Constructor, do nothing
//
// Preconditions : -
//
// PostContions  : -
//
// Error   : -
//
//---------------------------------------------------------------
InitHILCServer::InitHILCServer()
    : m_pDbmServer(0),
    m_pAscServer(0),
    m_pAscManager(0),
    m_pCtlCmdServer(0),
    m_pCtlCmdServerWithoutCB(0),
    m_ulModuleMask(0),
    m_AlmExtern(0),
    m_pDbmPoller(0),
    m_ulLevelMask(0)
{
    m_bSetORBTimeout = false;
    m_SessionManager = NULL;
    m_updateData = NULL;
    m_confManager = NULL;
}

//--------------------------------------------------------------
// Class  : InitHILCServer
// Method   : ~InitHILCServer
//
// Desciption : Destructor
//
// Preconditions : -
//
// PostContions  : -
//
// Error   : -
//
//---------------------------------------------------------------
InitHILCServer::~InitHILCServer()
{

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::dtor");
    m_pDbmServer= 0;

    FreeMemory();
}

//--------------------------------------------------------------
// Class    : InitHILCServer
// Method   : AreAllocated
//
// Desciption : tests if the pointers are allocated
//
// Preconditions : -
//
// PostContions  : -
//
// Error   : -
//
//---------------------------------------------------------------
const bool InitHILCServer::AreAllocated()
{
    bool bReturn = false;

    if( m_pDbmServer && m_pAscServer && m_pAscManager && m_pCtlCmdServer && m_pCtlCmdServerWithoutCB &&
            m_AlmExtern)
        bReturn = true;
    else
    {
        std::stringstream sBuffer;
        if( !m_pAscManager )
            sBuffer << "AscManager";
        if( !m_pDbmServer )
            sBuffer << ", DbmServer";
        if( !m_pAscServer )
            sBuffer << ", AscServer";
        if( !m_pCtlCmdServer )
            sBuffer << ", CtlCmdServer";
        if( !m_pCtlCmdServerWithoutCB )
            sBuffer << ", CtlCmdServer (without callback)";

        if( !m_AlmExtern)
            sBuffer << ", ScsAlmExtern";

        sBuffer << " is (are) not allocated";
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "%s",sBuffer.str().c_str());
        //Display::Trace( sBuffer.str() , 0);

    }/* if */

    return bReturn;
}

//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : FreeMemory
//
// Desciption : free memory
//
// Preconditions : -
//
// PostContions  : -
//
// Error   : -
//
//---------------------------------------------------------------
void InitHILCServer::FreeMemory()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"InitHILCServer::FreeMemory" );
    if( m_AlmExtern)
    {
        delete m_AlmExtern;
        m_AlmExtern = 0;
    }

    if( m_pAscServer )
    {
        delete m_pAscServer;
        m_pAscServer = 0;
    }/*  */

    if( m_pCtlCmdServer )
    {
        delete m_pCtlCmdServer;
        m_pCtlCmdServer = 0;
    }/* if( _pU_ctlCmdServer ) */

    if( m_pCtlCmdServerWithoutCB )
    {
        delete m_pCtlCmdServerWithoutCB;
        m_pCtlCmdServerWithoutCB = 0;
    }/*if( m_pCtlCmdServerWithoutCB )*/

    if( m_pDbmServer )
    {
        delete m_pDbmServer;
        m_pDbmServer= 0;
    }/* if( _pU_dbmServer ) */

    if( m_pAscManager )
    {
        delete m_pAscManager;
        m_pAscManager= 0;
    }/* if( m_pAscManager ) */
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"InitHILCServer::FreeMemory_end" );
}

//---------------------------------------------------------------
// Class      : HILCServer_i
// Method     : commandReturnCallback()
//
// Description : command return callback function (called by CTLCMD server)
//
// In         : i_vs_environment  : name of the logical environment in which the CTLCMD server is running
//              i_vs_commandName  : name of the command
//              i_vU_returnStatus : result status of the command
//              i_vi_nbInfo       : information count of valStatus and textStatus list
//              i_pi_valStatus    : condition value list issued from retCondTable or initCondTable
//              i_vs_textStatus   : condition text list issued from retCondTable or initCondTable
//              i_pv_userArg      : optional user argument
//
// Error      : none
//
//---------------------------------------------------------------
void InitHILCServer::commandReturnCallback(const char *i_vs_environment,
        const char *i_vs_subCommandName,
        ScsStatus   i_vU_returnStatus,
        long        i_vi_nbInfo,
        long       *i_pi_valStatus,
        char*      *i_vs_textStatus,
        void       *i_pv_userArg)
{
    stringstream sBuffer;
    sBuffer << " Env=" << i_vs_environment
    << " Command=" << i_vs_subCommandName
    << " Status=" << i_vU_returnStatus.getMinor()
    << " NbInfoStatus=" << i_vi_nbInfo
    << " UserArg(hex)=" << hex << i_pv_userArg;
    //Display::TraceS2( "HILCServer_i::commandReturnCallback", sBuffer.str() );
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "HILCServer_i::commandReturnCallback:%s",sBuffer.str().c_str());

    // TO DO - HILC commande en cours

}

//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : InitScadasoft
//
// Desciption : Initialize server name, physical
//              environment, type, Scadasoft server, DBM
//              and DBM server
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : ScsValid or ScsError
//
//---------------------------------------------------------------
ScsStatus InitHILCServer::InitScadasoft()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::InitScadasoft");
    //Display::TraceS4( "InitHILCServer::InitScadasoft","");
    //---------------------------------------------------------
    // Declares and initializes variables
    //---------------------------------------------------------
    ScsStatus status   = ScsError;                               // Returned status
    std::stringstream sBuffer;                                   // Error display string

    //---------------------------------------------------------
    // Initialize SCADAsoft with physical environment and server names
    //---------------------------------------------------------
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::InitScadasoft Call Scadasoft::Initialize");
    status = Scadasoft::Initialize( m_sPhysicalEnv.c_str(), m_sServerName.c_str() );
    if( !status.isValid() )
    {
        sBuffer.str("");
        sBuffer << "Scadasoft::Initialize (" << m_sPhysicalEnv << "," << m_sServerName << ")";
        SigErrScadasoft( SCS_FATAL, status.getMinor(), sBuffer.str().c_str() );
    }/* if( !status.isValid() ) */

    //---------------------------------------------------------
    // Determine StartUp status
    //---------------------------------------------------------
    // for now we always start as HOT, everything is in the RtapDB
    if( SnapshotHelper::instance()->isInitFromSnap() )
    {
        //Display::Trace("SigCtlPcc is Standby", 0);
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILC is standby");
        cout << "###InitHILCServer::InitScadasoft:         SigHILC is standby" <<endl;
    }
    else
    {
        // Display::Trace("SigCtlPcc is Online", 0);
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILC is online");
        cout << "###InitHILCServer::InitScadasoft:         SigHILC is online" <<endl;
    }

    //---------------------------------------------------------
    // setup current directory used to generate core file
    //---------------------------------------------------------
    sBuffer.str("");
    char* pBuffer= getenv( "HOME" );
    int iChdir = -1;

    if( pBuffer!=NULL )
    {
        sBuffer << string(pBuffer) << "/bin/cores/" << Scadasoft::GetServerName();
#ifndef WIN32
        iChdir = chdir( sBuffer.str().c_str() );
#else
        iChdir = _chdir( sBuffer.str().c_str() );
#endif

    }/* if( pBuffer!=NULL ) */

    if( iChdir!=0 )
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Cannot setup current directory used to generate core file");

    //---------------------------------------------------------
    // Activate traces for SigCtl component
    //---------------------------------------------------------
    if( status.isValid() )
    {
        scsint_SetUserTrace( m_ulModuleMask, m_ulLevelMask );
    }/* if( status.isValid() ) */

    //---------------------------------------------------------
    // Increase ORB timeout for remote calls during initialization phase
    //---------------------------------------------------------
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::InitScadasoft SetORBTimeout");
    if( status.isValid () )
    {
        status = Scadasoft::SetORBTimeout( SIG_K_ORB_TIME_OUT_DURING_INIT );
        sBuffer.str("");
        sBuffer << "Scadasoft::SetORBTimeout(" << SIG_K_ORB_TIME_OUT_DURING_INIT << " ms)";
        if( !status.isValid() )
            SigErrScadasoft( SCS_WARNING, status.getMinor(), sBuffer.str().c_str() );
        else
            m_bSetORBTimeout = true;
    }/* if( status.isValid() ) */

    //---------------------------------------------------------
    // InitialiZe AscManager
    //---------------------------------------------------------
    if( status.isValid() )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),  "   ###InitHILCServer::InitScadasoft AscManager start");

        m_pAscManager = new AscManager();
        status = m_pAscManager->getStatus();
        if( !status.isValid() )
            SigErrScadasoft( SCS_FATAL, status.getMinor(), "AscManager::getStatus()");
    }/* AscManager */

    //---------------------------------------------------------
    // InitialiZe DBM server
    //---------------------------------------------------------
    if( status.isValid() )
    {

        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),  "   ###InitHILCServer::InitScadasoft DbmServer start");

        m_pDbmServer = new DbmServer( m_sPhysicalEnv.c_str() );

        if( m_pDbmServer )
            status = m_pDbmServer->getStatus();

        if( !status.isValid() )
            SigErrScadasoft( SCS_FATAL, status.getMinor(), "DbmServer::getStatus()");
    }/* if( status.isValid() ) */

    //---------------------------------------------------------
    // InitialiZe DBM poller
    //---------------------------------------------------------
    if( status.isValid() )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),  "   ###InitHILCServer::InitScadasoft DbmPoller start");
        m_pDbmPoller = new DbmPoller ( m_sPhysicalEnv.c_str() );
        if ( m_pDbmPoller )
            status = m_pDbmPoller->getStatus ();
        if ( !status.isValid() )
            SigErrScadasoft( SCS_FATAL, status.getMinor(), "DbmPoller::getStatus()");
    }/* if( status.isValid() ) */


    //--------------------------------------------------------------------------
    // InitialiZe CtlCmd
    //--------------------------------------------------------------------------
    if( status.isValid() )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),   "   ###InitHILCServer::InitScadasoft CtlCmdServer start");
        m_pCtlCmdServer = new CtlCmdServer( m_sPhysicalEnv.c_str(), InitHILCServer::commandReturnCallback );
        if( m_pCtlCmdServer )
            status  = m_pCtlCmdServer->getStatus();
        if( !status.isValid() )
            SigErrScadasoft( SCS_FATAL, status.getMinor(), "CtlCmdServer::getStatus()");

        m_pCtlCmdServerWithoutCB = new CtlCmdServer( m_sPhysicalEnv.c_str() );
        if( m_pCtlCmdServerWithoutCB )
            status  = m_pCtlCmdServerWithoutCB->getStatus();
        if( !status.isValid() )
            SigErrScadasoft( SCS_FATAL, status.getMinor(), "CtlCmdServer::getStatus() (without callback)");

    }

    if(status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),   "   ###InitHILCServer::InitScadasoft ScsAlmExtern start");
        char* pBuffer = getenv("SCSPATH");
        if( NULL != pBuffer )
        {
            const string ALARMFILE_PATH = string( pBuffer ) + ALARMFILE_NAME;
            m_AlmExtern = new ScsAlmExtern(m_sPhysicalEnv.c_str(), ALARMFILE_PATH.c_str());
        }
        if(m_AlmExtern) status = m_AlmExtern->getStatus();
        if(!status.isValid()) SigErrScadasoft(SCS_FATAL, status.getMinor(), "ScsAlmExtern::getStatus()");
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SigHILCServer_i::Init:end");

    return status;
}


//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : EndInitScadasoft
//
// Desciption : End the Initialize scadosoft process
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : ScsValid or ScsError
//
//---------------------------------------------------------------
ScsStatus InitHILCServer::EndInitScadasoft()
{
    ScsStatus status   = ScsError; // Returned status
    //--------------------------------------------------------------------------
    // Initialise ASC
    //--------------------------------------------------------------------------
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "new ASC server");
    m_pAscServer = new AscServer();

    if( !m_pAscServer )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "AscServer bad alloc");
        //Display::Trace("AscServer bad alloc");
        status = ScsError;
    }/* if( !m_pAscServer ) */

    //--------------------------------------------------------------------------
    // Put back the default ORB timeout for remote calls
    //--------------------------------------------------------------------------
    if( m_bSetORBTimeout )
    {
        status = Scadasoft::UnSetORBTimeout();
        if( !status.isValid() )
        {
			std::stringstream sBuffer;     // Error display string
            sBuffer.str("");
            sBuffer << "Scadasoft::UnSetORBTimeout()";
            SigErrScadasoft( SCS_WARNING, status.getMinor(), sBuffer.str().c_str() );
        }/* if( !status.isValid() ) */
    }/* bSetORBTimeout */

    return status;
}

//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : InitSigCtlCBTC
//
// Desciption : Initialize other interfaces
//
// Preconditions :
//
//
// PostContions  : -
//
// Error         : ScsValid or ScsError
//
//---------------------------------------------------------------
ScsStatus InitHILCServer::InitHILCAutomaton()
{
    //---------------------------------------------------------
    // Declares and initializes variables
    //---------------------------------------------------------
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   InitHILCServer::InitHILCAutomaton ");

    m_confManager=HILC_ConfManager::getInstance();
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   InitHILCServer::HILC_ConfManager->Init() ");
    status = m_confManager->init(HILCFILE_NAME);
    if( status.isValid() )
    {
        m_updateData=HILC_UpdateData::getInstance();

        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   InitHILCServer::HILC_UpdateData->Init() ");
        status = m_updateData->init(m_pDbmServer, m_pDbmPoller, m_pCtlCmdServerWithoutCB, m_AlmExtern);
        if( status.isValid() )
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   InitHILCServer::SessionManager->Init() ");
            m_SessionManager=SessionManager::getInstance();
            status = m_SessionManager->init();

            if( status.isValid() )
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   InitHILCServer::InitSessionManager OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   InitHILCServer::InitHILCServer:  Init SessionManager failed");
            }

        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   InitHILCServer::InitHILCServer: Init HILC_UpdateData failed");
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "   InitHILCServer::InitHILCServer: Init ConfManager failed");
    }
    return status;
}



//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : Init
//
// Desciption : Initialize server name, physical
//              environment, type, Scadasoft server, DBM
//              server and SigCtlServer as well as
//              allocate memory for the objectsof the
//              class SigCtlServer
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : ScsValid or ScsError
//
//---------------------------------------------------------------
ScsStatus InitHILCServer::Init( std::string   sServerName,
                                std::string   sPhysicalEnv,
                                unsigned long ulModuleMask,
                                unsigned long ulLevelMask)
{
    //---------------------------------------------------------
    // Declares and initializes variables
    //---------------------------------------------------------

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::Init");

    ScsStatus status   = ScsError; // Returned status
    m_sServerName  = sServerName;
    m_sPhysicalEnv = sPhysicalEnv;
    m_ulModuleMask = ulModuleMask;
    m_ulLevelMask  = ulLevelMask;

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "ServerName = %s   PhysicalEnv = %s", m_sServerName.c_str(), m_sPhysicalEnv.c_str() );

    if( sServerName.size()>0 && sPhysicalEnv.size()>0 )
    {
        //---------------------------------------------------------
        // initialize SCADASOFT
        //---------------------------------------------------------
        status = InitScadasoft();
        //---------------------------------------------------------
        // initialize HILC
        //---------------------------------------------------------

        if( status.isValid() )
        {
            status = InitHILCAutomaton();
        }
    }/* size */
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "InitHILCServer::Init_END");
    //  Display::TraceE2( "InitHILCServer::Init",( status.isValid() ? true : false ) );
    return status;

}


//--------------------------------------------------------------
// Class  : InitHILCServer
// Method : SetUP
//
// Desciption : End the Initialize scadosoft process
//
// Preconditions : -
//
// PostContions  : -
//
// Error         : ScsValid or ScsError
//
//---------------------------------------------------------------
ScsStatus InitHILCServer::SetUP()
{
    return EndInitScadasoft();
}


/*void InitHILCServer::HILCClientCallback(string origin, string message, ScsStatus status, HILCClientIdl * pClient)
{
	m_pHILCServer_i->HILCClientCallback(origin, message, status, pClient);
}*/
