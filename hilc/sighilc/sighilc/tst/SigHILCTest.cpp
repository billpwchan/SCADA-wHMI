/**************************************************************/
/* Company         : TCS                            */
/* Filemane        : SigHILCTest.cpp                           */
/*------------------------------------------------------------*/
/* Purpose         : SIG HILC TEST                             */
/*                                                            */
/**************************************************************/
/* Creation        : 28/04/2015                             */
/* Author          :                                       */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Instruction for generation tracability : please, do not remove
static char ccase_id[] = "@(#) $CCfile: -- $ $Revision: -- $ $Date: -- $ $Project: -- $" ;

#include "scadaorb.h"
#include "scsthread.h"
#include "asc.h"
#include "scs.h"
#include "ggetopt.h"
#include "SigHILCServer.hh"
#include "SigHILCServerAcc.h"
#include "SigHILCTest_ConfManager.h"
#include <signal.h>
#include "SigTraces.h"
#include "Traces.h"
#include <sstream>

#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif

#include <deque>
#include <typeinfo>

//BHE #include "Common.h"
#include <cstdlib>
#include <iostream>
#include <fstream>

using namespace std;
const std::string SIGHILCTEST_FILE_NAME = "/dat/SigHILCTestConfFile.xml";		// SigHILCTest Conf File
const std::string SIGHILCTEST_LOG = "/log/SigHILCTestLog";		// SigHILCTest Log File
const std::string SIGHILCTEST_REACCONF = "/DacSim/DCReac.conf";		// SigHILCTest reac Conf File
const std::string SIGHILCTEST_PATTERNFILE = "/log/PatternFile.txt";		// file of pattern to use with grep -qzf to check traces

typedef enum
{
    SIGHILC_K_HMI = 0	// Service called by HMI
} SigHILC_t_Caller;

SigHILCServerAcc *pSigHILCServer = 0;
//string sSource("ts1");
const char * sRemoteEnv = 0;
SigHILC_t_Caller tCaller = (SigHILC_t_Caller)0;
deque<string> m_dequeMessage;
bool m_bAuto;

unsigned short int sUnBlockingCmdType = 0;
unsigned short int sBlockingCmdType = 1;

int test_mode = 0;
string test_operator = "";
string test_workstation = "";
int test_cmdType = 0;
int test_cmdValue = 0;
string test_eqptAlias = "";
string test_eqptType = "";
string test_cmdName = "";
string test_requestId = "";
int test_cmdStep = 0;
int test_number = 1;
stringstream logThis;
stringstream reacConfStrStream;
//stringstream patternFileStream;

void writeLog( stringstream &text )
{

    //cout << text.str() << endl << endl;

    char* pBuffer = getenv("SCSPATH");
    if( NULL != pBuffer )
    {
        const string SIGHILCTEST_LOG_PATH = string( pBuffer ) + SIGHILCTEST_LOG;
        std::ofstream log_file(SIGHILCTEST_LOG_PATH.c_str(), std::ofstream::out);
        log_file << text.str() << std::endl;
        log_file.close();
        //text.str("");
    }
}

void writeReacConf( stringstream &text )
{
    char* pBuffer = getenv("SCSPATH");
    if( NULL != pBuffer )
    {
        const string SIGHILCTEST_REACCONF_PATH = string( pBuffer ) + SIGHILCTEST_REACCONF;
        std::ofstream log_file(SIGHILCTEST_REACCONF_PATH.c_str(), std::ofstream::out | std::ofstream::trunc);
		// Serait interessant d'avoir une seule ligne, mais le code suivant ne suffit pas : log_file.seekp(0);
        log_file << text.str() << std::endl;
        log_file.close();
    }
}

void writePatternFile( stringstream &text )
{
    char* pBuffer = getenv("SCSPATH");
    if( NULL != pBuffer )
    {
        const string SIGHILCTEST_PATTERNFILE_PATH = string( pBuffer ) + SIGHILCTEST_PATTERNFILE;
		std::string item;
        std::ofstream log_file(SIGHILCTEST_PATTERNFILE_PATH.c_str(), std::ofstream::out | std::ofstream::app);
		//std::cout << "writePatternFile of: " << text.str() << endl;
		while (std::getline(text, item, '+'))
		{
			log_file << item << std::endl;
			//std::cout << "writePatternFile item: " << item << endl;
		}
        log_file.close();
    }
}

/**************************************************************/
/* Name            : commandReturnCallback                    */
/* Purpose         :                                          */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
/* not used yet
void commandReturnCallback(const char *i_vs_environment,
                           const char *i_vs_commandName,
                           ScsStatus   i_vU_returnStatus,
                           const char *i_errorMsg)
{
	__debugbreak();
    std ::stringstream sBuffer;
    sBuffer << "Command return callback [" << i_vs_environment
    << "]: command name=\"" << i_vs_commandName
    << "\" status minor=" << i_vU_returnStatus.getMinor()
    << "\" error string=" << i_errorMsg;
    if( m_bAuto )
        m_dequeMessage.push_back( sBuffer.str() );
    else
        cout << endl << flush <<sBuffer.str() << endl;
}
*/

/**************************************************************/
/* Name            : printUsage                               */
/* Purpose         : print the usage message in case of error */
/*                   in the argument list                     */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void printUsage(const char *sProcessName, const char *sDefaultLogEnvName, const char *sDefaultEnvName)
{
    cerr << endl << "Usage:  " << sProcessName << " [-e environment] [-u user] [-E remote_log_env]"
         << endl ;
	cerr << "Default environment name is " << sDefaultEnvName << endl;
    exit(-1);
}

/**************************************************************/
/* Name            : getArguments                             */
/* Purpose         : get the option letters from the argument */
/*                   list                                     */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void getArguments(int &pArgc, char **&pArgv)
{
    int        optChar;
    int        optFlag = 0;
    int        envFlag = 0;
    char      *physicalEnv;
    ScsStatus  status = ScsValid;

    // -------------------------------------------
    // Construct default physical environment name
    // -------------------------------------------
    string defaultEnvName = getenv("SCSENV");
    string defaultLogEnvName = getenv("SCSENV");
    size_t size = 10;
    char *hostname = new char[size];

    if( gethostname(hostname, size) != -1 )
    {
        defaultEnvName += "_";
        defaultEnvName += hostname;
    }/* if */

    physicalEnv = (char*) defaultEnvName.c_str();

    unsigned long levelMask = K_CODE_LEV_GENSTA & K_CODE_LEV_EXTCALL & K_CODE_LEV_CALLBYEXT & K_CODE_LEV_INTCALL & K_CODE_LEV_BASINTCALL & K_CODE_LEV_INTDET;
    unsigned long moduleMask = (1 << K_NUM_SIG);
    status = Scadasoft::SetUserTrace (moduleMask, levelMask);

    if (!status.isValid())
    {
        cout << "Error during trace init --> EXIT"<<endl;
        exit(-1);
    }

    GetOpt getopt(pArgc, pArgv, ":he:u:E:");
    while ((optChar = getopt()) != EOF)
    {
        switch (optChar)
        {
        case 'h':
            printUsage(pArgv[0], defaultLogEnvName.data(), defaultEnvName.data());
            break;
        case 'e':
            envFlag++;
            physicalEnv = strdup(getopt.optarg);
            break;
        case 'u':
            sscanf(getopt.optarg, "%20d", &tCaller);
            break;
        case 'E':
            sRemoteEnv = strdup (getopt.optarg);
            break;
        case ':':
            break;
        case '?':
            printUsage(pArgv[0], defaultLogEnvName.data(), defaultEnvName.data());
            break;
        }/* switch */
    }/* while */

    // ----------------------------------------
    // One of the required option was not found
    // ----------------------------------------
    if( optFlag != 0 )
    {
        printUsage(pArgv[0],defaultLogEnvName.data(),  defaultEnvName.data());
    }/* if( optFlag != 0 ) */

    // ---------------------------------------------------------------
    // Initialize SCADAsoft with physical environment and server names
    // ---------------------------------------------------------------
	cout << "physical environment : " << physicalEnv << endl;
    // Calls ScadaORB_2_3::Initialize(), which calls ScadaORB_2_3::RegisterNamingContext().
    // See "src/scs/scadaorb_2_3.cpp".
    status = Scadasoft::Initialize(physicalEnv, "SigHILCTest");
	
    if( status.isError() )
    {
        exit(-1);
    }/* if( status.isError() )  */

    // MSE Comment
    // if (envFlag)
    // delete physicalEnv;
}


/**************************************************************/
/* Name            : threadFunction                           */
/* Purpose         :                                          */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
ScsThreadReturnType threadFunction(void *)
{
    Scadasoft::MainLoop();

    return (ScsThreadReturnType)NULL;
}


/**************************************************************/
/* Name            : SignalHandler                            */
/* Purpose         :                                          */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
void SignalHandler(int iSignal)
{
    cout << "--SIGNAL---------------------------------" << endl;
    cerr << "Signal " << iSignal << " received. Exiting..." << endl;
    exit(-1);
}

/***************************************************************/
/*                            HILC                             */
/***************************************************************/

// CBI

/**************************************************************/
/* Name            : CBI_HILC_Preparation    (Prep request)       */
/* Purpose         :                                          */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         :                                          */
/**************************************************************/
/*
void CBI_HILC_Preparation( SigHILCServerAcc* pSigHILCServer, string sAlias, string commandName, string  equipType, unsigned short int cmdType, unsigned short int sBlockingCmdType)
{
    ScsStatus status = ScsError;

    if( pSigHILCServer && (sAlias.size()>0) )
    {

        // Command type (1 : SIO_CMD_TYPE
        //               2 : DIO_CMD_TYPE)
        unsigned short int sCmdType = cmdType;

        // Command value (0 : unblocking signal ou unblocking switch command ,
        //               1 : blocking signal, blocking route ou sub route release command)

        unsigned short int sCmdValue = sBlockingCmdType;

        string sEquipmentAlias   = sAlias;
        string sEquipmentType   = equipType;


        string sCommandName = commandName;
        string sOperatorName = "dev";
        string sWorkStationName = "PMS1_SIMU";

        status = pSigHILCServer->HILCPreparationRequest( sOperatorName, sWorkStationName, sCmdType, sCmdValue, sEquipmentAlias, sEquipmentType, sCommandName, sSource);

        if( status.isValid() )
            cout << "The command \"CBI_HILC_Preparation\" was sent with SUCCESS" << endl;
        else
            cout << "The command \"CBI_HILC_Preparation\" raised an ERROR "<< endl;
    }
}
*/
///////////////////////////////////// CONFIRMATION ////////////////////////////////////////


/*****************************************************************/
/* Name            : CBI_HILC_Confirm    (Confirm request)       */
/* Purpose         :                                             */
/*****************************************************************/
/* In parameters   :                                             */
/* Out parameters  :                                             */
/* Return          :                                             */
/* Context         :                                             */
/*****************************************************************/
/*
void CBI_HILC_Confirm( SigHILCServerAcc* pSigHILCServer, string sAlias, string commandName ,string  equipType, unsigned short int cmdType, unsigned short int sBlockingCmdType)
{
    ScsStatus status = ScsError;

    if( pSigHILCServer && (sAlias.size()>0) )
    {

        // Command type (1 : SIO_CMD_TYPE
        //               2 : DIO_CMD_TYPE)
        unsigned short int sCmdType = cmdType;

        // Command value (0 : unblocking signal ou unblocking switch command ,
        //               1 : blocking signal, blocking route ou sub route release command)
        unsigned short int sCmdValue = sBlockingCmdType;


        string sEquipmentAlias   = sAlias;
        string sEquipmentType   = equipType;


        string sCommandName = commandName;
        string sOperatorName = "dev";
        string sWorkStationName = "PMS1_SIMU";

        status = pSigHILCServer->HILCConfirmRequest( sOperatorName, sWorkStationName, sCmdType, sCmdValue, sEquipmentAlias, sEquipmentType, sCommandName, sSource);

        if( status.isValid() )
            cout << "The command \"CBI_HILC_Confirm\" was sent with SUCCESS" << endl;
        else
            cout << "The command \"CBI_HILC_Confirm\" raised an ERROR "<< endl;
    }
}
*/
///////////////////////////////////// CANCEL ////////////////////////////////////////


/*****************************************************************/
/* Name            : CBI_HILC_Cancel    (Cancel request)       */
/* Purpose         :                                             */
/*****************************************************************/
/* In parameters   :                                             */
/* Out parameters  :                                             */
/* Return          :                                             */
/* Context         :                                             */
/*****************************************************************/
/* BHE : besoin pas encore exprime sur Lusail Doha
void CBI_HILC_Cancel( SigHILCServerAcc* pSigHILCServer, string sAlias, string commandName ,string  equipType, unsigned short int cmdType, unsigned short int sBlockingCmdType)
{
  ScsStatus status = ScsError;

    if( pSigHILCServer && (sAlias.size()>0) )
    {

     // Command type (1 : SIO_CMD_TYPE
      //               2 : DIO_CMD_TYPE)
     unsigned short int sCmdType = cmdType;

     // Command value (0 : unblocking signal ou unblocking switch command ,
      //               1 : blocking signal, blocking route ou sub route release command)
      unsigned short int sCmdValue = sBlockingCmdType;


      string sEquipmentAlias   = sAlias;
      string sEquipmentType   = equipType;


      string sCommandName = commandName;
      string sOperatorName = "dev";
      string sWorkStationName = "PMS1_SIMU";

      status = pSigHILCServer->HILCCancelRequest( sOperatorName, sWorkStationName, sCmdType, sCmdValue, sEquipmentAlias, sEquipmentType, sCommandName, sSource);

      if( status.isValid() )
        cout << "The command \"CBI_HILC_Cancel\" was sent with SUCCESS" << endl;
      else
        cout << "The command \"CBI_HILC_Cancel\" raised an ERROR "<< endl;
    }
}
*/

void sendAllCommand(SigHILCServerAcc* pSigHILCServer, int commandStep)
{
    ScsStatus status = ScsError;
    int testNumber;
    int firstTest;
    int timerBeforeConf = 8;
    int timerBeforeCmd = 1;
    //int timerBeforeCancel = 3;
    int isActive = 1 ;
    int isSendPrepActive = 1 ;
    int isSendConfActive = 1 ;
    int isSendCancelActive = 0;
    int repeat = 1;
    // ----------------------------------------------------------
    // Init xml conf
    // ----------------------------------------------------------
    SigHILCTest_ConfManager* m_testConfManager = SigHILCTest_ConfManager::getInstance();
    string eqpt ;
    string eqptType;
    string cmdName;
	string expectedPrepStatus, expectedConfStatus;
	int expectedPrepStatusMinor, expectedConfStatusMinor;
    unsigned short cmdType;
    short cmdValue;
	unsigned short cmdValueDiv;
    string op;
    string ws;
	int HilcSelReacReset, HilcExeReacReset;
	int AckSelValue, AckSelValue2, AckExeValue;
	unsigned int nbOfTestsOk = 0;

    status = m_testConfManager->init(SIGHILCTEST_FILE_NAME, 0);
    if( !status.isValid() )
    {
        cerr << "Error on SigHILCTest_ConfManager" << endl;
        exit(-1);
    }/* if( !status.isValid() ) */

    testNumber = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_TEST_NUMBER;
    firstTest = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_FIRST_TEST;
    //timerBeforeCancel = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_TIMER_BEFORE_CANCEL;
    repeat = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_REPEAT;

    logThis << "Number of TEST : " << testNumber <<endl;
    //writeLog(logThis.str());
    //logThis.str("");
    writeLog(logThis);
	// To clear the pattern file (which is then used in append mode)
	std::ofstream pattern_file(string(getenv("SCSPATH"))+SIGHILCTEST_PATTERNFILE, std::ofstream::out | std::ofstream::trunc);
	pattern_file.close();
	
    for (int currentCount=0; currentCount<repeat; ++currentCount)
    {
        cout << "TEST_COUNT" << currentCount << " :" << endl;
        for (int currentTestNumber=firstTest; currentTestNumber<(firstTest+testNumber); ++currentTestNumber)
        {
            status = m_testConfManager->init(SIGHILCTEST_FILE_NAME, currentTestNumber);
            if( !status.isValid() )
            {
                cerr << "Error on SigHILCTest_ConfManager" << endl;
                exit(-1);
            }/* if( !status.isValid() ) */


            isActive = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_ISACTIVE;
            if(isActive==1)
            {
				stringstream patternFileStream;
				bool testOK = true;
                //load value from xml file
                eqpt = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_EQPT;
                eqptType = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_EQPT_TYPE;
                cmdName = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CMD_NAME;
                cmdType = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CMD_TYPE;
                cmdValue = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CMD_VALUE;
				cmdValueDiv = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CMD_VALUE_DIV;
                op = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_OP;
                ws = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_WS;
                //src = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_SRC;
                timerBeforeCmd = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_TIMER_BEFORE_CMD;
                //timerBeforeCancel = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_TIMER_BEFORE_CANCEL;
			    timerBeforeConf = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_TIMER_BEFORE_CONF;
				
				HilcSelReacReset = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_PREP_ACK_RESET;
				HilcExeReacReset = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CONF_ACK_RESET;
				AckSelValue = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_PREP_ACK_VALUE;
				AckSelValue2 = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_PREP_ACK_VALUE2;
				AckExeValue = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CONF_ACK_VALUE;
				reacConfStrStream << "HilcSelReacReset " << HilcSelReacReset << " HilcExeReacReset " << HilcExeReacReset
						<< " AckSelValue " << AckSelValue << " AckExeValue " << AckExeValue
						<< " AckSelValue2 " << AckSelValue2 << endl;
				writeReacConf(reacConfStrStream);
				patternFileStream << SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_EXPECT_TRACE_PATTERN;
				writePatternFile(patternFileStream);
				
                cout << "TEST_" << currentTestNumber << " :" << endl;

                logThis << "#####################################" << endl;
                logThis << "TEST_" << currentTestNumber << " :" << endl;
                logThis << "Command Description : " << endl;
                logThis << "Equipment Type: " << eqptType << endl;
                logThis << "Equipment: " << eqpt << endl;
                logThis << "Command Name: " << cmdName << endl;
                logThis << "Command Type: " << cmdType << endl;
                logThis << "Command Value: " << cmdValue << endl;
                logThis << "Command Value Divisor: " << cmdValueDiv << endl;
                logThis << "Operator: " << op << endl;
                logThis << "Workstation: " << ws << endl;

                writeLog(logThis);
				
                //Send Prep Command
                isSendPrepActive = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_ISSENDPREPACTIVE;
                if(isSendPrepActive==1)
                {
					cout << "TEST_" << currentTestNumber << ": wait " << timerBeforeCmd << " seconds before prepare sent " << endl;
#ifdef _WIN32
					Sleep(timerBeforeCmd*1000); // Windows Sleep in milliseconds
#else
					sleep(timerBeforeCmd); // Unix sleep in seconds (usleep in microseconds)
#endif
                    status = pSigHILCServer->HILCPreparationRequest( op, ws, cmdType, cmdValue, cmdValueDiv, eqpt, eqptType, cmdName);

					expectedPrepStatus = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_PREP_EXPECT_STATUS;
					expectedPrepStatusMinor = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_PREP_EXPECT_STATUS_MINOR;
					
					string checkOk = "-> NOT EXPECTED RESULT !";
                    if( status.isValid() )
                    {
						if (expectedPrepStatus.compare("SUCCESS") == 0)
						{
							checkOk = "-> expected result";
						}
						else testOK=false;
                        cout << "TEST_" << currentTestNumber << ": Prep send SUCCESS " << checkOk << endl;
                        logThis << "Test " << currentTestNumber <<" : The command \"CBI_HILC_Prep\" was send with SUCCESS "
								<< checkOk << endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);
                    }
                    else
                    {
						if ( (expectedPrepStatus.compare("ERROR") == 0) && (expectedPrepStatusMinor == status.getMinor()) )
						{
							checkOk = "-> expected result";
						}
						else testOK=false;
                        cout << "TEST_" << currentTestNumber << ": Prep send ERROR minor code:" << status.getMinor()
						     << " expected minor code:" << expectedPrepStatusMinor << " : " << checkOk << endl;
                        logThis << "Test " << currentTestNumber <<" : The command \"CBI_HILC_Prep\" raised an ERROR "
								<< checkOk << endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);
                    }
/* BHE : besoin pas encore exprime sur Lusail Doha
                    isSendCancelActive = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_ISSENDCANCELACTIVE;
                    if ( isSendCancelActive == 1 )
                    {
                        //Wait few seconds before sending Cancel
                        logThis << "Wait few seconds before sending Cancel"<<endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);

#ifdef _WIN32
                        Sleep(timerBeforeCancel*1000); // Windows Sleep in milliseconds
#else
                        sleep(timerBeforeCancel); // Unix sleep in seconds (usleep in microseconds)
#endif

                        //Send Cancel Command
                                      status = pSigHILCServer->HILCCancelRequest( op, ws, cmdType, cmdValue, eqpt, eqptType, cmdName, src);
                                      if( status.isValid() )
                                      {
                                        cout << "TEST_" << currentTestNumber << ": Cancel send SUCCESS" << endl;
                                        logThis << "Test " << currentTestNumber <<" : The command \"CBI_HILC_Cancel\" was send with SUCCESS" << endl;
                                        //writeLog(logThis.str());
                                        //logThis.str("");
                                        writeLog(logThis);
                                      }
                                      else
                                      {
                                        cout << "TEST_" << currentTestNumber << ": Cancel send ERROR" << endl;
                                        logThis << "Test " << currentTestNumber <<" : The command \"CBI_HILC_Cancel\" raised an ERROR "<< endl;
                                        //writeLog(logThis.str());
                                        //logThis.str("");
                                        writeLog(logThis);
                                      }
                    }
                    else
                    {
                        logThis << "No need to cancel"<< endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);
                    }
*/
                }
                else
                {
                    logThis << "INACTIVE PREP STEP "<<endl;
                    //writeLog(logThis.str());
                    //logThis.str("");
                    writeLog(logThis);
                }

                //Send Conf Command
                isSendConfActive = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_ISSENDCONFACTIVE;
                if(isSendConfActive==1)
                {
					cout << "TEST_" << currentTestNumber << ": wait " << timerBeforeConf << " seconds before confirm sent " << endl;
#ifdef _WIN32
					Sleep(timerBeforeConf*1000); // Windows Sleep in milliseconds
#else
					sleep(timerBeforeConf); // Unix sleep in seconds (usleep in microseconds)
#endif
                    status = pSigHILCServer->HILCConfirmRequest( op, ws, cmdType, cmdValue, cmdValueDiv, eqpt, eqptType, cmdName);
					
					expectedConfStatus = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CONF_EXPECT_STATUS;
					expectedConfStatusMinor = SigHILCTest_ConfManager::getInstance()->SIGHILCTEST_CONF_EXPECT_STATUS_MINOR;
					
					string checkOk = "-> NOT EXPECTED RESULT !";
                    if( status.isValid() )
                    {
						if (expectedConfStatus.compare("SUCCESS") == 0)
						{
							checkOk = "-> expected result";
						}						
						else testOK=false;
                        cout << "TEST_" << currentTestNumber << ": Conf send SUCCESS " << checkOk << endl;
                        logThis << "Test " << currentTestNumber <<" : The confirm command was send with SUCCESS "
								<< checkOk << endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);
                    }
                    else
                    {
						if ((expectedConfStatus.compare("ERROR") == 0) && (expectedConfStatusMinor == status.getMinor()))
						{
							checkOk = "-> expected result";
						}
						else testOK=false;
                        cout << "TEST_" << currentTestNumber << ": Conf send ERROR minor code:" << status.getMinor()
						     << " expected minor code:" << expectedConfStatusMinor << " : " << checkOk << endl;
                        logThis << "Test " << currentTestNumber <<" : The confirm command raised an ERROR "
								<< checkOk << endl;
                        //writeLog(logThis.str());
                        //logThis.str("");
                        writeLog(logThis);
                    }
                }
                else
                {
                    logThis << "INACTIVE CONF STEP "<<endl;
                    //writeLog(logThis.str());
                    //logThis.str("");
                    writeLog(logThis);
                }
				if (testOK == true) nbOfTestsOk++;
            }
            else
            {
                logThis << "INACTIVE TEST "<<endl;
                //writeLog(logThis.str());
                //logThis.str("");
                writeLog(logThis);
            }
        }
		cout << nbOfTestsOk << " tests OK on a total of " << testNumber << " tests" << endl;
    }
}

////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////
/// MAIN ///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////

/**************************************************************/
/* Name            : main                                     */
/* Purpose         : main function                            */
/**************************************************************/
/* In parameters   :                                          */
/* Out parameters  :                                          */
/* Return          :                                          */
/* Context         : OK                                       */
/**************************************************************/
int main(int argc, char **argv)
{
    // --------------
    // variables
    // --------------

    string sChoice;
    string sAlias;

    // --------------
    // Initialisation
    // --------------

    cout << "argc = " << argc << endl;
    for(int i = 0; i < argc; i++)
    {
        cout << "argv[" << i << "] = " << argv[i] << endl;
    }
    getArguments(argc, argv);
    //AscServer* pAscServer = new AscServer();

    // ---------------
    // Signal handling
    // ---------------
    signal(2, SignalHandler);
    signal(11, SignalHandler);
    signal(15, SignalHandler);

    // ----------------------------
    // Creation of interface object
    // ----------------------------
    if( sRemoteEnv == 0 )
        sRemoteEnv = Scadasoft::GetLogicalEnvironment();

    cout << "remote environment : " << sRemoteEnv << endl;

    //pSigHILCServer = new SigHILCServerAcc(sRemoteEnv, commandReturnCallback);
	pSigHILCServer = new SigHILCServerAcc(sRemoteEnv);

    ScsStatus status = pSigHILCServer->getStatus();

    if( !status.isValid() )
    {
        cerr << "Error on SigHILCServerAcc::SigHILCServerAcc(" <<  sRemoteEnv << ")" << endl;
        exit(-1);
    }/* if( !status.isValid() ) */

    // ----------------------------------------------------------
    // Run mainloop in another thread
    // ----------------------------------------------------------
    ScsThread::ID threadId;
    ScsThread::Create(threadId, threadFunction, (void *)0);

    cout << "#############################"  << endl;
    cout << "RUN ALL TEST  "  << endl;
    cout << "#############################"  << endl;

    sendAllCommand(pSigHILCServer, test_cmdStep);

    cout << "delete SigHILCServerAcc "<< endl;
    if( pSigHILCServer )
        delete pSigHILCServer;
	
    // Unregister from naming service.
    // Calls ScadaORB_2_3::Exit(), which calls ScadaORB_2_3::Killit(), which
    // calls CosNaming::NamingContext_var::unbind().
	Scadasoft::Exit();

    // Release SCADAsoft resources.
    Scadasoft::Finalize();
	
    cout << "Exit Process "<< endl;
    exit(0);

    return 0;
}
