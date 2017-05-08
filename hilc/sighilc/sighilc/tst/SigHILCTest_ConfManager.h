#ifndef __SIGHILCTESTCONFMANAGER_HILC_H__
#define __SIGHILCTESTCONFMANAGER_HILC_H__


#include "scs.h"
#include "asc.h"
#include <sstream>
#include <string>
#include <ecomlib/confmanager.h>


using namespace std;

class SigHILCTest_ConfManager
{
private:
    static SigHILCTest_ConfManager* m_instance;
    SigHILCTest_ConfManager();

public:

    static SigHILCTest_ConfManager* getInstance();
    ScsStatus init(string hilcConf_Filename, int testNumber);
    ~SigHILCTest_ConfManager();
    string getConfValue(string keyName, string defaultValue);
    int getConfValue(string keyName, int defaultValue);

// XML CONFIGURATION

    string SIGHILCTEST_EQPT;
    string SIGHILCTEST_EQPT_TYPE;
    string SIGHILCTEST_CMD_NAME;
    unsigned short SIGHILCTEST_CMD_TYPE;
    short SIGHILCTEST_CMD_VALUE;
	unsigned short SIGHILCTEST_CMD_VALUE_DIV;
    string SIGHILCTEST_OP;
    string SIGHILCTEST_WS;
    //string SIGHILCTEST_SRC;
    int SIGHILCTEST_TEST_NUMBER;
    int SIGHILCTEST_FIRST_TEST;
    int SIGHILCTEST_TIMER_BEFORE_CONF;
    int SIGHILCTEST_TIMER_BEFORE_CMD;
    //int SIGHILCTEST_TIMER_BEFORE_CANCEL;
    int SIGHILCTEST_ISACTIVE;
    int SIGHILCTEST_ISSENDPREPACTIVE;
    int SIGHILCTEST_ISSENDCONFACTIVE;
    //int SIGHILCTEST_ISSENDCANCELACTIVE;
    int SIGHILCTEST_REPEAT;

	int SIGHILCTEST_PREP_ACK_RESET;
	int SIGHILCTEST_CONF_ACK_RESET;
	int SIGHILCTEST_PREP_ACK_VALUE;
	int SIGHILCTEST_PREP_ACK_VALUE2;
	int SIGHILCTEST_CONF_ACK_VALUE;

	string SIGHILCTEST_PREP_EXPECT_STATUS;
	int SIGHILCTEST_PREP_EXPECT_STATUS_MINOR;
	string SIGHILCTEST_CONF_EXPECT_STATUS;
	int SIGHILCTEST_CONF_EXPECT_STATUS_MINOR;

	string SIGHILCTEST_EXPECT_TRACE_PATTERN;
};

#endif //__SIGHILCTESTCONFMANAGER_HILC_H__
