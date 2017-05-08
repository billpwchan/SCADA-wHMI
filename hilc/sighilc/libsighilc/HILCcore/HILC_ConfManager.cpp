/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#include "HILC_ConfManager.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include <string>
#include <sstream>
#include <stddef.h>
#include <ecomlib/confmanager.h>
using namespace std;

// initialisation du pointeur de singleton
HILC_ConfManager *HILC_ConfManager::m_instance = NULL;

//Constructeur avec initialisation des attributs avec une valeur par defaut dans le constructeur demandée par Klocwork
HILC_ConfManager::HILC_ConfManager() :
    HILC_MAX_SESSIONS(-1), HILC_TIMER_DELAY( -1), HILC_MESSAGE_FORMAT_TYPE( -1), HILC_MESSAGE_TO_UPPER( -1), HILC_PREP_CMD_ID( -1), HILC_CONF_CMD_ID( -1),
    HILC_CLASS_NUMBER( -1), HILC_SCSTYPES_NUMBER( -1), HILC_ROOTS_NUMBER(-1), HILC_ATT_NUMBER( -1), HILC_NUMBER_OF_STEP( -1),
    HILC_ENTERSESSION_STEP_NUMBER( -1), HILC_PREPARATION_STEP_NUMBER( -1), HILC_CONFIRMATION_STEP_NUMBER( -1),
    HILC_SESSIONSTATE_NUMBER( -1), HILC_CURRENTSTEP_NUMBER( -1), HILC_PREPSTATE_NUMBER(-1), HILC_CONFSTATE_NUMBER(-1), HILC_K_ALA_EXTERN_01( -1), 
    HILC_INITSESSION_COND_VALUE( -1), HILC_FALLBACK_COND_VALUE( -1),
    HILC_STATE_ENTERSESSION_ATT1_CONDVALNUMBER( -1), HILC_STATE_ENTERSESSION_ATT1_CONDVAL1( -1), HILC_STATE_ENTERSESSION_ATT2_CONDVALNUMBER( -1),
    HILC_STATE_ENTERSESSION_ATT2_CONDVAL1( -1), HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVALNUMBER( -1), HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVAL1( -1),
    HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVALNUMBER( -1), HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVAL1( -1), HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVAL2( -1),
    HILC_STATE_PREP_ATT1_CONDVALNUMBER( -1), HILC_STATE_PREP_ATT1_CONDVAL1( -1), HILC_STATE_PREP_ATT2_CONDVALNUMBER( -1), HILC_STATE_PREP_ATT2_CONDVAL1( -1),
    HILC_STATE_PREP_ATT1_ERROR_CONDVALNUMBER( -1), HILC_STATE_PREP_ATT1_ERROR_CONDVAL1( -1), HILC_STATE_PREP_ATT1_ERROR_CONDVAL2( -1), HILC_STATE_PREP_ATT2_ERROR_CONDVALNUMBER( -1),
    HILC_STATE_PREP_ATT2_ERROR_CONDVAL1( -1), HILC_STATE_PREP_ATT2_ERROR_CONDVAL2( -1), HILC_STATE_WAITINGCONF_ATT1_CONDVALNUMBER( -1), HILC_STATE_WAITINGCONF_ATT2_CONDVALNUMBER( -1),
    HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVALNUMBER( -1), HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL1( -1), HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL2( -1),
    HILC_STATE_WAITINGCONF_ATT2_ERROR_CONDVALNUMBER( -1), HILC_STATE_WAITINGCONF_ATT2_ERROR_CONDVAL1( -1), HILC_STATE_CONF_ATT1_CONDVALNUMBER( -1), HILC_STATE_CONF_ATT1_CONDVAL1( -1),
    HILC_STATE_CONF_ATT2_CONDVALNUMBER( -1), HILC_STATE_CONF_ATT2_CONDVAL1( -1), HILC_STATE_CONF_ATT1_ERROR_CONDVALNUMBER( -1), HILC_STATE_CONF_ATT1_ERROR_CONDVAL1( -1),
    HILC_STATE_CONF_ATT1_ERROR_CONDVAL2( -1), HILC_STATE_CONF_ATT2_ERROR_CONDVALNUMBER( -1), HILC_STATE_CONF_ATT2_ERROR_CONDVAL1( -1), HILC_STATE_CONF_ATT2_ERROR_CONDVAL2( -1),
    HILC_STATE_CONF_ATT2_ERROR_CONDVAL3( -1)
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   HILC_ConfManager-ctor " );
    m_instance = NULL;
}


HILC_ConfManager::~HILC_ConfManager()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   HILC_ConfManager-dtor " );
}


HILC_ConfManager* HILC_ConfManager::getInstance()
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   HILC_ConfManager-getInstance " );

    if (m_instance == NULL)
    {
        m_instance = new HILC_ConfManager();
    }
    return m_instance;
}

//Initialisation
ScsStatus HILC_ConfManager::init(string hilcConf_Filename)
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_ConfManager-Init: ");
    ScsStatus status = ScsError;
    string keyName;
    bool xml_ok = false;
    // Init XML conf parser
    char* pBuffer = getenv("SCSPATH");
    if( NULL != pBuffer )
    {
        // Fichier de configuration du composant HILC
        const string HILCFILE_PATH = string( pBuffer ) + hilcConf_Filename;

        try
        {
            //lecture du fichier de conf
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::init readfile: %s ", HILCFILE_PATH.c_str() );
            xml_ok = ecomlib::ConfManager::instance()->readfile( HILCFILE_PATH.c_str() );
        }
        catch( ... )
        {
            xml_ok = false;
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::init readfile Error: %s ", HILCFILE_PATH.c_str() );
        }
    }

    // Read BDD_NODE
    keyName = "HILC_CONF.BDD_NODE.HILC_ROOTS_NUMBER";
    HILC_ROOTS_NUMBER = this->getConfValue(keyName, 1);

    for ( int i=1; i < HILC_ROOTS_NUMBER+1; ++i)
    {
        stringstream HILCRoots_i;
        HILCRoots_i << "HILC_CONF.BDD_NODE.HILC_ROOTS." << i ;

        keyName = HILCRoots_i.str();
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf root %d = %s", i, keyName.c_str() );

        string root_i =  this->getConfValue(keyName, "");
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf root %d = %s", i, root_i.c_str() );

        if( root_i.compare("") != 0)
        {
            HILC_Roots.push_back(root_i);
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init root %d = %s", i, HILC_Roots[i-1].c_str() );
        }
    }

    keyName = "HILC_CONF.BDD_NODE.HILC_DCI_NODE_NAME";
    HILC_DCI_NODE_NAME = this->getConfValue(keyName, ":dci");
    keyName = "HILC_CONF.BDD_NODE.HILC_SIO_NODE_NAME";
    HILC_SIO_NODE_NAME = this->getConfValue(keyName, ":sio");
    keyName = "HILC_CONF.BDD_NODE.HILC_DIO_NODE_NAME";
    HILC_DIO_NODE_NAME = this->getConfValue(keyName, ":dio");
    keyName = "HILC_CONF.BDD_NODE.HILC_AIO_NODE_NAME";
    HILC_AIO_NODE_NAME = this->getConfValue(keyName, ":aio");
    keyName = "HILC_CONF.BDD_NODE.HILC_TAG_ALIAS";
    HILC_TAG_ALIAS = this->getConfValue(keyName, "<alias>");
    keyName = "HILC_CONF.BDD_NODE.HILC_ATTR_UNIVNAME";
    HILC_ATTR_UNIVNAME = this->getConfValue(keyName, ".UNIVNAME");
    keyName = "HILC_CONF.BDD_NODE.HILC_ATTR_VALUE";
    HILC_ATTR_VALUE = this->getConfValue(keyName, ".value");

    // Read PARAM
    keyName = "HILC_CONF.PARAM.HILC_MAX_SESSIONS";
    HILC_MAX_SESSIONS = this->getConfValue(keyName, 10);	
    keyName = "HILC_CONF.PARAM.HILC_TIMER_DELAY";
    HILC_TIMER_DELAY = this->getConfValue(keyName, 100000);
#ifdef USE_SIO	
    keyName = "HILC_CONF.PARAM.HILC_MESSAGE_LOCALE";
    HILC_MESSAGE_LOCALE = this->getConfValue(keyName, "en_US");
    keyName = "HILC_CONF.PARAM.HILC_MESSAGE_FORMAT_TYPE";
    HILC_MESSAGE_FORMAT_TYPE = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.PARAM.HILC_MESSAGE_TO_UPPER";
    HILC_MESSAGE_TO_UPPER = this->getConfValue(keyName, 0);

    //Read Command ID
    keyName = "HILC_CONF.PARAM.HILC_PREP_CMD_ID";
    HILC_PREP_CMD_ID = this->getConfValue(keyName, 129);
    keyName = "HILC_CONF.PARAM.HILC_CONF_CMD_ID";
    HILC_CONF_CMD_ID = this->getConfValue(keyName, 126);
#endif	

    //Read Equipment Class configuration

    keyName = "HILC_CONF.PARAM.HILC_CLASS_NUMBER";
    HILC_CLASS_NUMBER = this->getConfValue(keyName, 5);

    for ( int i=1; i < HILC_CLASS_NUMBER+1; ++i)
    {
        stringstream HILCClass_i;
        HILCClass_i << "HILC_CONF.PARAM.HILC_CLASS." << i ;

        keyName = HILCClass_i.str();
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf Class %d = %s", i, keyName.c_str() );

        string equipmentType_i =  this->getConfValue(keyName, "");
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf equipmentType %d = %s", i, equipmentType_i.c_str() );

        if( equipmentType_i.compare("") != 0)
        {
            HILC_EquipmentTypes.push_back(equipmentType_i);
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init equipmentType %d = %s", i, HILC_EquipmentTypes[i-1].c_str() );
        }
    }

    keyName = "HILC_CONF.PARAM.HILC_SCSTYPES_NUMBER";
    HILC_SCSTYPES_NUMBER = this->getConfValue(keyName, 1);

    for ( int i=1; i < HILC_SCSTYPES_NUMBER+1; ++i)
    {
        stringstream HILCScsType_i;
        HILCScsType_i << "HILC_CONF.PARAM.HILC_SCSTYPES." << i ;

        keyName = HILCScsType_i.str();
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf scstype %d = %s", i, keyName.c_str() );

        string scsType_i =  this->getConfValue(keyName, "");
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init LoadConf scstype %d = %s", i, scsType_i.c_str() );

        if( scsType_i.compare("") != 0)
        {
            HILC_ScsTypes.push_back(scsType_i);
            SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::Init scstype %d = %s", i, HILC_ScsTypes[i-1].c_str() );
        }
    }
	
    //Read Configuration for HILC
    keyName = "HILC_CONF.PARAM.HILC_ATT_NUMBER";
    HILC_ATT_NUMBER = this->getConfValue(keyName, 0);
#ifdef USE_SESSION_EQPT	
    keyName = "HILC_CONF.PARAM.HILC_SESSIONSTATE";
    HILC_SESSIONSTATE = this->getConfValue(keyName, "");
#endif	
#ifdef USE_SAME_DCI
    keyName = "HILC_CONF.PARAM.HILC_CURRENTSTEP";
    HILC_CURRENTSTEP = this->getConfValue(keyName, "-Act");
#endif
    keyName = "HILC_CONF.PARAM.HILC_PREPSTATE";
    HILC_PREPSTATE = this->getConfValue(keyName, "-Ack");
    keyName = "HILC_CONF.PARAM.HILC_CONFSTATE";
    HILC_CONFSTATE = this->getConfValue(keyName, "-Ack");

#ifdef USE_SIO
#ifdef USE_SESSION_EQPT
    keyName = "HILC_CONF.PARAM.HILC_SIO_ENTERSESSION_NAME";
    HILC_SIO_ENTERSESSION_NAME = this->getConfValue(keyName, "");
#endif
    keyName = "HILC_CONF.PARAM.HILC_SIO_PREPARATION_NAME";
    HILC_SIO_PREPARATION_NAME = this->getConfValue(keyName, "");
    keyName = "HILC_CONF.PARAM.HILC_SIO_CONFIRMATION_NAME";
    HILC_SIO_CONFIRMATION_NAME = this->getConfValue(keyName, "");
#endif

#ifdef USE_SESSION_EQPT	
    keyName = "HILC_CONF.PARAM.HILC_DIO_ENTERSESSION_NAME";
    HILC_DIO_ENTERSESSION_NAME = this->getConfValue(keyName, "-");
#endif
    keyName = "HILC_CONF.PARAM.HILC_DIO_PREPARATION_NAME";
    HILC_DIO_PREPARATION_NAME = this->getConfValue(keyName, "-");
    keyName = "HILC_CONF.PARAM.HILC_DIO_CONFIRMATION_NAME";
    HILC_DIO_CONFIRMATION_NAME = this->getConfValue(keyName, "-");

#ifdef USE_SESSION_EQPT	
    keyName = "HILC_CONF.PARAM.HILC_AIO_ENTERSESSION_NAME";
    HILC_AIO_ENTERSESSION_NAME = this->getConfValue(keyName, "-");
#endif			
    keyName = "HILC_CONF.PARAM.HILC_AIO_PREPARATION_NAME";
    HILC_AIO_PREPARATION_NAME = this->getConfValue(keyName, "-");
    keyName = "HILC_CONF.PARAM.HILC_AIO_CONFIRMATION_NAME";
    HILC_AIO_CONFIRMATION_NAME = this->getConfValue(keyName, "-");

    //Parametres de la procedure HILC
    keyName = "HILC_CONF.PARAM.HILC_NUMBER_OF_STEP";
    HILC_NUMBER_OF_STEP = this->getConfValue(keyName, 0);

    keyName = "HILC_CONF.PARAM.HILC_ENTERSESSION_STEP_NUMBER";
    HILC_ENTERSESSION_STEP_NUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.PARAM.HILC_PREPARATION_STEP_NUMBER";
    HILC_PREPARATION_STEP_NUMBER = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.PARAM.HILC_CONFIRMATION_STEP_NUMBER";
    HILC_CONFIRMATION_STEP_NUMBER = this->getConfValue(keyName, 2);

#ifdef USE_SESSION_EQPT
    keyName = "HILC_CONF.PARAM.HILC_SESSIONSTATE_NUMBER";
    HILC_SESSIONSTATE_NUMBER = this->getConfValue(keyName, 0);
#endif	
#ifdef USE_SAME_DCI
    keyName = "HILC_CONF.PARAM.HILC_CURRENTSTEP_NUMBER";
    HILC_CURRENTSTEP_NUMBER = this->getConfValue(keyName, 1);
#endif
    keyName = "HILC_CONF.PARAM.HILC_PREPSTATE_NUMBER";
    HILC_PREPSTATE_NUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.PARAM.HILC_CONFSTATE_NUMBER";
    HILC_CONFSTATE_NUMBER = this->getConfValue(keyName, 1);

    //EXTERNAL ALARM
    keyName = "HILC_CONF.ALA_EXT.HILC_K_ALA_EXTERN_01";
    HILC_K_ALA_EXTERN_01 = this->getConfValue(keyName, 1);

    //STATE PATTERN CONFIGURATION
    keyName = "HILC_CONF.STATE_PATTERN.HILC_WAITING_STEP_NAME";
    HILC_WAITING_STEP_NAME = this->getConfValue(keyName, "State_WaitingConf");
#ifdef USE_SESSION_EQPT
    keyName = "HILC_CONF.STATE_PATTERN.HILC_INITSESSION_COND_VALUE";
    HILC_INITSESSION_COND_VALUE = this->getConfValue(keyName, 0);
#endif	
#ifdef USE_FALLBACK
    keyName = "HILC_CONF.STATE_PATTERN.HILC_FALLBACK_CMDNAME";
    HILC_FALLBACK_CMDNAME = this->getConfValue(keyName, "-FBMode");
    keyName = "HILC_CONF.STATE_PATTERN.HILC_FALLBACK_DCINAME";
    HILC_FALLBACK_DCINAME = this->getConfValue(keyName, "_2000-ModeFallBack");
    keyName = "HILC_CONF.STATE_PATTERN.HILC_FALLBACK_CLASS";
    HILC_FALLBACK_CLASS = this->getConfValue(keyName, "PMI");
    keyName = "HILC_CONF.STATE_PATTERN.HILC_FALLBACK_EQPT";
    HILC_FALLBACK_EQPT = this->getConfValue(keyName, "CBI_1");
    keyName = "HILC_CONF.STATE_PATTERN.HILC_FALLBACK_COND_VALUE";
    HILC_FALLBACK_COND_VALUE = this->getConfValue(keyName, 1);
#endif
#ifdef USE_SESSION_EQPT
    //State ENTERSESSION
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT1_CONDVALNUMBER";
    HILC_STATE_ENTERSESSION_ATT1_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT1_CONDVAL1";
    HILC_STATE_ENTERSESSION_ATT1_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT2_CONDVALNUMBER";
    HILC_STATE_ENTERSESSION_ATT2_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT2_CONDVAL1";
    HILC_STATE_ENTERSESSION_ATT2_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVALNUMBER";
    HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVAL1";
    HILC_STATE_ENTERSESSION_ATT1_ERROR_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVALNUMBER";
    HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_ENTERSESSION.HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVAL1";
    HILC_STATE_ENTERSESSION_ATT2_ERROR_CONDVAL1 = this->getConfValue(keyName, 0);
#endif	
    //State PREPARATION
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT1_CONDVALNUMBER";
    HILC_STATE_PREP_ATT1_CONDVALNUMBER = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT1_CONDVAL1";
    HILC_STATE_PREP_ATT1_CONDVAL1 = this->getConfValue(keyName, 2);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT2_CONDVALNUMBER";
    HILC_STATE_PREP_ATT2_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT1_ERROR_CONDVALNUMBER";
    HILC_STATE_PREP_ATT1_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 2);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT1_ERROR_CONDVAL1";
    HILC_STATE_PREP_ATT1_ERROR_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT1_ERROR_CONDVAL2";
    HILC_STATE_PREP_ATT1_ERROR_CONDVAL2 = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_PREP.HILC_STATE_PREP_ATT2_ERROR_CONDVALNUMBER";
    HILC_STATE_PREP_ATT2_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 0);

    //State WAITINGCONF
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT1_CONDVALNUMBER";
    HILC_STATE_WAITINGCONF_ATT1_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT2_CONDVALNUMBER";
    HILC_STATE_WAITINGCONF_ATT2_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVALNUMBER";
    HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 2);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL1";
    HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL2";
    HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL2 = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_WAITINGCONF.HILC_STATE_WAITINGCONF_ATT2_ERROR_CONDVALNUMBER";
    HILC_STATE_WAITINGCONF_ATT2_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 0);

    //State CONFIRMATION
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT1_CONDVALNUMBER";
    HILC_STATE_CONF_ATT1_CONDVALNUMBER = this->getConfValue(keyName, 1);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT1_CONDVAL1";
    HILC_STATE_CONF_ATT1_CONDVAL1 = this->getConfValue(keyName, 3);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT2_CONDVALNUMBER";
    HILC_STATE_CONF_ATT2_CONDVALNUMBER = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT1_ERROR_CONDVALNUMBER";
    HILC_STATE_CONF_ATT1_ERROR_CONDVALNUMBER = this->getConfValue(keyName, 2);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT1_ERROR_CONDVAL1";
    HILC_STATE_CONF_ATT1_ERROR_CONDVAL1 = this->getConfValue(keyName, 0);
    keyName = "HILC_CONF.STATE_PATTERN.STATE_CONF.HILC_STATE_CONF_ATT1_ERROR_CONDVAL2";
    HILC_STATE_CONF_ATT1_ERROR_CONDVAL2 = this->getConfValue(keyName, 1);

    if (xml_ok)
    {
        status = ScsValid;
    }

    return status;
}

// lecture des parametres de configurations  de type INT
int HILC_ConfManager::getConfValue(string keyName, int defaultValue) const
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_ConfManager-getConfValue: ");
    int result = defaultValue;
    try
    {
        //valeur du fichier de conf
        result = ecomlib::ConfManager::instance()->getIntKey( keyName );
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::getConfValue  %s = %d", keyName.c_str(), result );
    }
    catch( ecomlib::ConfManagerException& ex )
    {
        //valeur par defaut
        result = defaultValue;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::init Int Conf Key %s access error - set '%d' by default: %s", keyName.c_str(),defaultValue, ex.what() );
    }

    return result;
}
// lecture des parametres de configurations  de type STRING
string HILC_ConfManager::getConfValue(string keyName, string defaultValue) const
{
    SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "   HILC_ConfManager-getConfValue: ");
    string result = defaultValue;
    try
    {
        //valeur du fichier de conf
        result = ecomlib::ConfManager::instance()->getStringKey( keyName );
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "SessionManager::getConfValue  %s = %s", keyName.c_str(), result.c_str() );
    }
    catch( ecomlib::ConfManagerException& ex )
    {
        //valeur par defaut
        result = defaultValue;
        SigTrace( SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "SessionManager::init String Conf Key %s access error  - set '%s' by default: %s", keyName.c_str(),defaultValue.c_str(), ex.what() );
    }

    return result;
}
