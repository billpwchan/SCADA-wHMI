/**************************************************************/
/* Company         : TCS                               */
/* Filemane        : SigHILCMain.cpp                           */
/*------------------------------------------------------------*/
/* Purpose         : SIG HILC MAIN                             */
/*                                                            */
/**************************************************************/
/* Creation        : 03/03/2005                               */
/* Author          : OJU                                      */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Instruction for generation tracability : please, do not remove
//static char const gconf_id[] = CCID;

#include "ggetopt.h"
#include "SnapshotHelper.h"   // redondance
#include "dbm.h"
#include "SigError.h"
#include "SigTraces.h"
#include "Traces.h"
#include "HILCdefs.h"

#include "InitHILCServer.h"
#include "SigHILCServer_i.h"
#include "SigHILCMain.h"
#include "redutil.h"

/**************************************************************/
/* Name            : SigHILCNoMoreMemory                       */
/* Purpose         : Display error and exit program when      */
/*                   operator "new" cannot allocate enough    */
/*                   memory                                   */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void SigHILCNoMoreMemory()
{
    SigErrMemory( SCS_FATAL );
    exit( -1 );
}


/**************************************************************/
/* Name            : printUsage                               */
/* Purpose         : print the usage message in case of error */
/*                   in the argument list                     */
/**************************************************************/
/* In parameters   : const char *processName : process name   */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void printUsage( const char* processName )
{
    cerr << "\nUsage:\t" << processName << " -e env [-n serverName] [-m module] [-l level] [-i snapdir]\n"
         << "Options:\n"
         << "\t-e  name of the SCADAsoft physical environment\n"
         << "\t-n  name of the server\n"
         << "\t-m  trace module (0-31)\n"
         << "\t-i  snapshot directory\n"
         << "\t-l  trace level (0-31) level 0-5 : standard levels\n"
         << "\t0 : highest level always set\n"
         << "\t1 : external calls to other processes\n"
         << "\t2 : external calls from other processes\n"
         << "\t3 : internal calls\n"
         << "\t4 : internal calls more detailed\n"
         << "\t5 : details\n"
         << endl;
}


/**************************************************************/
/* Name            : SigHILCExit                               */
/* Purpose         : Free memory allocated in the             */
/*                   initialisation part                      */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void SigHILCExit()
{
    if( _pU_InitHILCServer )
    {
        delete _pU_InitHILCServer;
        _pU_InitHILCServer = 0;
    }
    if( _pU_SigHILCServer_i )
    {
        DESTROY_SERVANT(SigHILCServer_i,_pU_SigHILCServer_i);
    }/* if( _pU_SigHILCServer_i ) */
}

/**************************************************************/
/* Name            : getArguments                             */
/* Purpose         : get the option letters from the argument */
/*                   list                                     */
/**************************************************************/
/* In parameters   : int argc     : Nb of arguments           */
/*                   char* argv[] : Arguments                 */
/* Out parameters  : char** vs_serverName   : Server name     */
/*                   char ** vs_physicalEnv : Environment     */
/*                   unsigned long& o_vuh_levelMask : mask    */
/* Return          : ScsValid or ScsError                     */
/* Context         :                                          */
/**************************************************************/
ScsStatus GetArguments( int argc, char *  argv[], char ** vs_serverName, char ** vs_physicalEnv,
                        unsigned long  & o_vuh_ModuleMask, unsigned long  & o_vuh_levelMask)
{
    //---------------------------------------------------------
    // Declaration of variables
    //---------------------------------------------------------
    int optChar = 0;  // Command line input character
    int optUsage = 0; // Flag to print usage and options of command
    int envFlag = 0;  // Flag set if compulsory info physical env is
    // provided on the command line
    int level = 0;    // Level of traces to be activated
    int module = 0;   // Level of module trace
    ScsStatus status = ScsValid; // Returned status
    bool eIsScanned = false;
	
    //---------------------------------------------------------
    // Initialise output results
    //---------------------------------------------------------
    o_vuh_ModuleMask = (1 << K_NUM_SIG);
    o_vuh_levelMask = 0;
    GetOpt getopt( argc, argv, ":hn:e:i:l:m:" );

	optChar=getopt();
    while( optChar != EOF )
    {
		unsigned short sizeOfArgs = strlen (getopt.optarg) + 1;
        switch( optChar )
        {
        case 'n':  // Cannot specify server name, takes default name
            break;

        case ':': // ???
            break;

        case 'h': // -h prints the list of possible options
            optUsage++;
            break;

        case 'i': // SCADAsoft snapshot given, implies starting as standby
            SnapshotHelper::instance()->setSnapshotDir( getopt.optarg );
            break;

        case '?':        // If unknown option
            optUsage++;
            break;

        case 'e':
            // this test just to avoid a (not probable) klocwork issue code MLK.MIGHT
            if (eIsScanned == false)
			{
				*vs_physicalEnv = new char[sizeOfArgs];
				if( *vs_physicalEnv == 0 )
				{
					SigErrMemory (SCS_FATAL);
					status = ScsError;
				}
				else
				{
					strcpy_s( *vs_physicalEnv, sizeOfArgs, getopt.optarg );
					envFlag++;
				}/* if( *vs_physicalEnv == 0 ) */	
                eIsScanned=true;				
			}
            break;

        case 'l':
            level = atoi(getopt.optarg);
            if( (level < 0) || (level > 31) )
            {
                optUsage++;
            }
            else
            {
                o_vuh_levelMask |= (1 << level);
            }/* if( (level < 0) || (level > 31) ) */
            break;

        case 'm':
            module = atoi(getopt.optarg);
            if( (module < 0) || (module > 31) )
            {
                optUsage++;
            }
            else
            {
                o_vuh_ModuleMask |= (1 << module);
            }/* module = atoi(getopt.optarg); */
            break;

        default:
            // No special processing
			break;
        } // End of switch

        if( status == ScsError)
            break;

		optChar=getopt();
    } // End of while

    //---------------------------------------------------------
    // Set the name of server to its default name
    //---------------------------------------------------------
    if( status.isValid() )
    {
		unsigned short sizeOfServerName = strlen (SIGHILC_K_SERVER_NAME) + 1;
        *vs_serverName = new char[sizeOfServerName];
        if( *vs_serverName == 0 )
        {
            // Allocation failed
            SigErrMemory( SCS_FATAL );
            status = ScsError;
        }
        else
        {
            strcpy_s (*vs_serverName, sizeOfServerName, SIGHILC_K_SERVER_NAME);
        }/* if( *vs_serverName == 0 ) */

    }/* if( status == ScsValid ) */

    if( status.isValid() )
    {
        if( optUsage || (!envFlag) )
        {
            printUsage(argv[0]);
            status = ScsError;
        }/* if( optUsage || (!envFlag) ) */
    }/* if( status == ScsValid ) */

    //---------------------------------------------------------
    // Force trace at level GENSTA to be activated always
    //---------------------------------------------------------
    o_vuh_levelMask = o_vuh_levelMask | K_CODE_LEV_GENSTA;

    return status;

}


/**************************************************************/
/* Name            : main                                     */
/* Purpose         : main function                            */
/**************************************************************/
/* In parameters   : int argc     : Nb of arguments           */
/*                   char* argv[] : Arguments                 */
/* Out parameters  :                                          */
/* Return          : ScsError if an error occures             */
/*                   0 otherwise                              */
/* Context         :                                          */
/**************************************************************/
int main(int argc, char * argv[])
{
    try// KlocWork impose a Catch-all handler in the main
    {
        //----------------------------------------------------------
        // variables declaration and initialisation
        //----------------------------------------------------------
        ScsStatus status = ScsError;   // Value returned from initialisation
        char* sServerName  = 0;        // Orbix server name
        char* sPhysicalEnv = 0;        // Physical environment name
        unsigned long ulModuleMask = 0; // Trace modules mask
        unsigned long ulLevelMask = 0; // Trace levels mask

        //---------------------------------------------------------
        // Gets the arguments (init sServerName, sPhysicalEnv)
        //---------------------------------------------------------
        status = GetArguments( argc, argv, &sServerName, &sPhysicalEnv, ulModuleMask, ulLevelMask );


        //-----------------------------------------------------------
        // Initialize SCADASOFT, all required variables
        // and SigHILC servers
        // Scadasoft::Initialize has to be called before calling
        // constructor of SigHILCServerSOAP
        //-----------------------------------------------------------
        if (status.isValid ())
        {

            cout << "   ###main::new InitHILCServer() " <<endl;
            _pU_InitHILCServer  = new InitHILCServer();

            if( _pU_InitHILCServer )
            {
                cout << "   ###main::_pU_InitHILCServer->Init()" <<endl;
                status = _pU_InitHILCServer->Init( sServerName, sPhysicalEnv, ulModuleMask, ulLevelMask);
            }

        }

        //--------------------------------------------------------------------------
        // Free allocated memory : Server name and Physical environment
        //--------------------------------------------------------------------------
        if( sServerName )
        {
            delete [] sServerName;
            sServerName = 0;
        }/* if( sServerName ) */
        if( sPhysicalEnv )
        {
            delete [] sPhysicalEnv;
            sPhysicalEnv = 0;
        }/* if( sPhysicalEnv ) */

        //-----------------------------------------------------------
        // Initialize SIGHILC server
        //-----------------------------------------------------------
        if( status.isValid() && _pU_InitHILCServer )
        {
            cout << "   ###main::CREATE_SERVANT" <<endl;
            CREATE_SERVANT(SigHILCServer_i*,SigHILCServer_i,_pU_SigHILCServer_i,new SigHILCServer_i());

            if( _pU_SigHILCServer_i )
            {
                cout << "   ###main::_pU_SigHILCServer_i->Init( )" <<endl;
                _pU_SigHILCServer_i->Init( );
                status = _pU_SigHILCServer_i->getStatus();
            }/* if( _pU_SigHILCServer_i ) */
        }

        //-----------------------------------------------------------
        // SetUP the server
        //-----------------------------------------------------------
        if( status.isValid() && _pU_InitHILCServer )
        {
            cout << "   ###main::_pU_InitHILCServer->SetUP()" <<endl;
            status = _pU_InitHILCServer->SetUP();
            /*
            // Set snaphot mode to text
              SnapshotHelper::instance()->setBinaryMode(false);
              // Load snapshot on standby server
              SnapshotHelper::instance()->load();
            */
            // Goes UP
            if( _pU_SigHILCServer_i )
            {
                cout << "   ###main:: _pU_SigHILCServer_i->goesUP()" <<endl;
                _pU_SigHILCServer_i->goesUP();
            }
        }/* SetUP */

        //-----------------------------------------------------------
        // Is OK
        //-----------------------------------------------------------
        if( !status.isValid() )
        {
            SigHILCExit();
            SigErrInit( SCS_FATAL );
            exit(-1);
        }/* if( !status.isValid() )  */
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Initialisation SCADASOFT and SIG HILC completed");
        //Display::Trace( "Initialisation SCADASOFT and SIG HILC completed", 0);


        // -------------------
        // SCADAsoft main loop
        // -------------------
        Scadasoft::MainLoop();

        SigHILCExit();
    }
    catch ( ... ) // Catch-all handler
    {
        // Handle unexpected exceptions
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Catch unexpected exceptions in Main");
    }

    return 0;
}



