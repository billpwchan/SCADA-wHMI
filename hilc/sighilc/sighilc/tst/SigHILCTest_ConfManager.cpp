#include "SigHILCTest_ConfManager.h"
#include "Traces.h"
#include "SigTraces.h"
//BHE #include "SigError.h"
#include <string>
#include <stddef.h>
#include <sstream>
#include <ecomlib/confmanager.h>
using namespace std;

// initialisation du pointeur de singleton
SigHILCTest_ConfManager *SigHILCTest_ConfManager::m_instance = NULL;

SigHILCTest_ConfManager::SigHILCTest_ConfManager()
{
    SigTrace( K_TRACE_LEV_INTDET,"   SigHILCTest_ConfManager-ctor " );
    m_instance = NULL;

}


SigHILCTest_ConfManager::~SigHILCTest_ConfManager()
{
    SigTrace( K_TRACE_LEV_INTDET,"   SigHILCTest_ConfManager-dtor " );
}


SigHILCTest_ConfManager* SigHILCTest_ConfManager::getInstance()
{
    SigTrace( K_TRACE_LEV_INTDET, "   SigHILCTest_ConfManager-getInstance " );

    if (m_instance == NULL)
    {
        m_instance = new SigHILCTest_ConfManager();
    }

    return m_instance;
}

ScsStatus SigHILCTest_ConfManager::init(string hilcConf_Filename, int testNumber)
{
    SigTrace( K_TRACE_LEV_INTDET, "   SigHILCTest_ConfManager-Init: ");
    ScsStatus status = ScsError;
    stringstream keyName;
    bool xml_ok = false;

    // Init XML conf parser
    char* pBuffer = getenv("SCSPATH");
    if( NULL != pBuffer )
    {
        const string SIGHILCTEST_FILE_PATH = string( pBuffer ) + hilcConf_Filename;

        try
        {
            SigTrace( K_TRACE_LEV_INTDET, "SessionManager::init readfile: %s ", SIGHILCTEST_FILE_PATH.c_str() );
            xml_ok = ecomlib::ConfManager::instance()->readfile( SIGHILCTEST_FILE_PATH.c_str() );
        }
        catch( ... )
        {
            xml_ok = false;
        }
    }

    if ( testNumber != 0)
    {
        SigTrace( K_TRACE_LEV_INTDET, "SessionManager::init for test : %d ", testNumber );
		
        // Read CMD ARG : CORBA request arguments
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.EQPT";
        //cout << keyName.str()<<endl<<endl;
        SIGHILCTEST_EQPT = this->getConfValue(keyName.str(), "");
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.EQPT_TYPE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_EQPT_TYPE = this->getConfValue(keyName.str(), "");
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.CMD_NAME";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_CMD_NAME = this->getConfValue(keyName.str(), "");
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.CMD_TYPE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_CMD_TYPE = this->getConfValue(keyName.str(),2);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.CMD_VALUE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_CMD_VALUE = this->getConfValue(keyName.str(),1);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.CMD_VALUE_DIV";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_CMD_VALUE_DIV = this->getConfValue(keyName.str(),1);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.OP";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_OP = this->getConfValue(keyName.str(), "Test");
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.WS";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_WS = this->getConfValue(keyName.str(), "lslint02");
        //keyName.str("");
        //keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CMD_ARG.SRC";
        //cout << keyName.str()<<endl;
        //SIGHILCTEST_SRC = this->getConfValue(keyName.str(), "ts1");
		
		// Test parameters wich are not CORBA request arguments
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".TIMER_BEFORE_CMD";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_TIMER_BEFORE_CMD = this->getConfValue(keyName.str(),3);		
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".TIMER_BEFORE_CONF";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_TIMER_BEFORE_CONF = this->getConfValue(keyName.str(),3);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".SIGHILCTEST_ISACTIVE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_ISACTIVE = this->getConfValue(keyName.str(),1);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".ISSENDPREPACTIVE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_ISSENDPREPACTIVE = this->getConfValue(keyName.str(),1);
        cout << keyName.str() <<" = " << SIGHILCTEST_ISSENDPREPACTIVE <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".ISSENDCONFACTIVE";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_ISSENDCONFACTIVE = this->getConfValue(keyName.str(),1);
        cout << keyName.str() <<" = " << SIGHILCTEST_ISSENDCONFACTIVE <<endl;
        //keyName.str("");
        //keyName << "SIGHILCTEST_CONF.SIGHILCTEST_ISSENDCANCELACTIVE";
        //cout << keyName.str()<<endl;
        //SIGHILCTEST_ISSENDCANCELACTIVE = this->getConfValue(keyName.str(),0);
        
		keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".PREP_ACK_RESET";
        SIGHILCTEST_PREP_ACK_RESET = this->getConfValue(keyName.str(),0);
        cout << keyName.str() <<" = " << SIGHILCTEST_PREP_ACK_RESET <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CONF_ACK_RESET";
        SIGHILCTEST_CONF_ACK_RESET = this->getConfValue(keyName.str(),0);
        cout << keyName.str() <<" = " << SIGHILCTEST_CONF_ACK_RESET <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".PREP_ACK_VALUE";
        SIGHILCTEST_PREP_ACK_VALUE = this->getConfValue(keyName.str(),1);
        cout << keyName.str() <<" = " << SIGHILCTEST_PREP_ACK_VALUE <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CONF_ACK_VALUE";
        SIGHILCTEST_CONF_ACK_VALUE = this->getConfValue(keyName.str(),1);
        cout << keyName.str() <<" = " << SIGHILCTEST_CONF_ACK_VALUE <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".PREP_ACK_VALUE2";
        SIGHILCTEST_PREP_ACK_VALUE2 = this->getConfValue(keyName.str(),-1);
        cout << keyName.str() <<" = " << SIGHILCTEST_PREP_ACK_VALUE2 <<endl;
		
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".PREP_EXPECT_STATUS";
        SIGHILCTEST_PREP_EXPECT_STATUS = this->getConfValue(keyName.str(), "SUCCESS");
        cout << keyName.str() <<" = " << SIGHILCTEST_PREP_EXPECT_STATUS <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CONF_EXPECT_STATUS";
        SIGHILCTEST_CONF_EXPECT_STATUS = this->getConfValue(keyName.str(), "SUCCESS");
        cout << keyName.str() <<" = " << SIGHILCTEST_CONF_EXPECT_STATUS <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".PREP_EXPECT_STATUS_MINOR";
        SIGHILCTEST_PREP_EXPECT_STATUS_MINOR = this->getConfValue(keyName.str(), 0);
        cout << keyName.str() <<" = " << SIGHILCTEST_PREP_EXPECT_STATUS_MINOR <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".CONF_EXPECT_STATUS_MINOR";
        SIGHILCTEST_CONF_EXPECT_STATUS_MINOR = this->getConfValue(keyName.str(), 0);
        cout << keyName.str() <<" = " << SIGHILCTEST_CONF_EXPECT_STATUS_MINOR <<endl;
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_" << testNumber << ".EXPECT_TRACE_PATTERN";
        SIGHILCTEST_EXPECT_TRACE_PATTERN = this->getConfValue(keyName.str(), "");
        cout << keyName.str() <<" = " << SIGHILCTEST_EXPECT_TRACE_PATTERN <<endl;
		
    }
    else
    {
        // Read GLOBAL PARAM

        keyName.str("");
        keyName << "SIGHILCTEST_CONF.TEST_NUMBER";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_TEST_NUMBER = this->getConfValue(keyName.str(),1);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.FIRST_TEST";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_FIRST_TEST = this->getConfValue(keyName.str(),1);
        //keyName.str("");
        //keyName << "SIGHILCTEST_CONF.TIMER_BEFORE_CANCEL";
        //cout << keyName.str()<<endl;
        //SIGHILCTEST_TIMER_BEFORE_CANCEL = this->getConfValue(keyName.str(),3);
        keyName.str("");
        keyName << "SIGHILCTEST_CONF.REPEAT";
        //cout << keyName.str()<<endl;
        SIGHILCTEST_REPEAT = this->getConfValue(keyName.str(),1);
    }
    if (xml_ok)
    {
        status = ScsValid;
    }

    return status;
}


int SigHILCTest_ConfManager::getConfValue(string keyName, int defaultValue)
{
    SigTrace( K_TRACE_LEV_INTDET, "   SigHILCTest_ConfManager-getConfValue: ");
    int result = defaultValue;
    try
    {
        result = ecomlib::ConfManager::instance()->getIntKey( keyName );
        SigTrace( K_TRACE_LEV_INTDET, "SessionManager::getConfValue  %s = %d", keyName.c_str(), result );
    }
    catch( ecomlib::ConfManagerException& ex )
    {
        result = defaultValue;
        SigTrace( K_TRACE_LEV_INTDET, "SessionManager::init Int Conf Key %s access error - set '%d' by default: %s", keyName.c_str(),defaultValue, ex.what() );
    }

    return result;
}

string SigHILCTest_ConfManager::getConfValue(string keyName, string defaultValue)
{
    SigTrace( K_TRACE_LEV_INTDET, "   SigHILCTest_ConfManager-getConfValue: ");
    string result = defaultValue;
    try
    {
        result = ecomlib::ConfManager::instance()->getStringKey( keyName );
		// Allow to force empty string whereas default is not empty
		if (result.compare("isEmpty") == 0)
		{
			result = "";
		}
        SigTrace( K_TRACE_LEV_INTDET, "SessionManager::getConfValue  %s = %s", keyName.c_str(), result.c_str() );
    }
    catch( ecomlib::ConfManagerException& ex )
    {
        result = defaultValue;
        SigTrace( K_TRACE_LEV_INTDET, "SessionManager::init String Conf Key %s access error  - set '%s' by default: %s", keyName.c_str(),defaultValue.c_str(), ex.what() );
    }

    return result;
}
