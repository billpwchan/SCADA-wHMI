#include "Session.h"
#include "State_Prep.h"
#include "HILC_UpdateData.h"
#include "Traces.h"
#include "SigTraces.h"
#include "HILCdefs.h"
#include <stddef.h>
#include <sstream>
#include <iostream>
#include <sstream>
#include <string>
#include <locale>
#include <vector>
#include <iomanip>

Session::Session(string operatorName,	string workStationName, unsigned short cmdType, short cmdValue, unsigned short cmdValueDiv, string equipmentAlias,  string equipmentType, string commandName, string groupName, int eqtNumber)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session-ctor");

    _operatorName=operatorName;
    _workStationName=workStationName;
    _cmdType=cmdType;
    _cmdValue=cmdValue;
	_cmdValueDiv=cmdValueDiv;
    _equipmentAlias=equipmentAlias;
    _equipmentType=equipmentType;
    _commandName=commandName;
    _currentState = new State_Prep();// Change for another initial State if Necessary
    _groupName=groupName;
    _id=eqtNumber;
#ifdef USE_SIO	
    _HILC_EnterSessionCmdSize = 0;
    _HILC_PrepCmdSize= 0;
    _HILC_ConfCmdSize= 0;
    // Cancel is option _HILC_CancelCmdSize= 0;
    const int iMessageMaxSize = HILC_MESSAGE_BUFFER_SIZE;
    memset( _HILC_EnterSession_String_Cmd_Value, 0, iMessageMaxSize );
    memset( _HILC_Prep_String_Cmd_Value, 0, iMessageMaxSize );
    memset( _HILC_Conf_String_Cmd_Value, 0, iMessageMaxSize );
#endif
    for( int i=0; i<VECTOR_MAX_SIZE; i++ )
    {
        _HILC_Int_Cmd_Value[i]=0;
    }
    _timerHILC = NULL;
    _finalTimerHILC = NULL;

}


void Session::init()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::init");
    //start ScsTimer
    this->setHILCTimer();

    //define path for dci  sio  dio
    this->setHILC_Att_Path();
    this->setHILC_CMD_Path();
    //this->setHILC_SIO_FullPath();
    //this->setHILC_DIO_FullPath();

    // construct the commands
    this->setHILC_Cmd_Value();
}

Session::~Session()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session-dtor ");
    if (_currentState)
    {
        delete _currentState;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session-dtor: delete currentState");
    }
    if (_timerHILC)
    {
        delete _timerHILC;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session-dtor: delete timerHILC");
    }
    if (_finalTimerHILC)
    {
        delete _finalTimerHILC;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session-dtor: delete _finalTimerHILC");
    }
}


State* Session::getCurrentState()
{
    return _currentState;
}

void Session::setCurrentState(State * newState)
{
    _currentState = newState;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " ################################Session on eqt: %d change state to : %s################",_id, _currentState->info().c_str() );
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");
}

ScsStatus Session::actionOnNotify()
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::actionOnNotify start ");
    status = _currentState->actionOnNotify(this);
    return status;
}

ScsStatus Session::actionOnInit()
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::actionOnInit Start " );
    status = _currentState->actionOnInit(this);
    return status;
}

ScsStatus Session::actionOnTimerEnd()
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::actionOnTimerEnd Start " );
    status = _currentState->actionOnTimerEnd(this);
    return status;
}

string Session::buildEventLogSource() const
{
	// build a 7 char source string for CtlCmdServer sendCommand event logging
	// REM : event logging will be effective only if the standard scadasoft scshooks are overwritten
	string source;
	if (this->getOpN().size()>3)
	{
		source = this->getOpN().substr(0, 3) + "@";
	}
	else
	{
		source = this->getOpN() + "@";
	}
	if (this->getWS().size()>3)
	{
		source = source + this->getWS().substr(this->getWS().size()-3, 3);
	}
	else
	{
		source = source + this->getWS();
	}	
	return source;
}

string Session::getHILC_Att_Path(int i)
{
    string result="";
    if( i <HILC_ConfManager::getInstance()->HILC_ATT_NUMBER)
    {
        result=_HILC_Att_Path[i];
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Att_Path:  %s", result.c_str() );
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Att_Path  Indice error ");
    }

    return result;
}

string Session::getHILC_CMD_Path(int i)
{
    string result="";

    switch(_cmdType)
    {
#ifdef USE_SIO
    case HILC_ConfManager::HILC_SIO_CMD_TYPE : //Enum

        result=_HILC_CMD_Path[i];
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_CMD_Path SIO:  %s", result.c_str() );
        break;
#endif
    case HILC_ConfManager::HILC_DIO_CMD_TYPE : //Enum

        result=_HILC_CMD_Path[i];
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_CMD_Path DIO:  %s", result.c_str() );
        break;

	case HILC_ConfManager::HILC_AIO_CMD_TYPE : //Enum

        result=_HILC_CMD_Path[i];
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_CMD_Path AIO:  %s", result.c_str() );
        break;

	default :
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " getHILC_CMD_Path  Unknown Command Type ");
		break; // just to be sure no code will be executed after, we can feel this unecessary but Klocwork don't like it

    }
    return result;
}

#ifdef USE_SIO
unsigned char * Session::getCmdValue(unsigned int & dstSize, int commandStepNumber)
{
    unsigned char * dst =NULL;
    dstSize = 0;
#ifdef USE_SESSION_EQPT
    if (commandStepNumber==HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER)
    {
        if( _HILC_EnterSessionCmdSize != 0)
        {
            dst = &_HILC_EnterSession_String_Cmd_Value[0];
            dstSize = _HILC_EnterSessionCmdSize;
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Cmd_Value : EnterSession cmd" );
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " getHILC_Cmd_Value  EnterSessionCmdSize error ");
        }
    }
    else
#endif
	if (commandStepNumber==HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER)
    {
        if( _HILC_PrepCmdSize != 0)
        {
            dst = &_HILC_Prep_String_Cmd_Value[0];
            dstSize = _HILC_PrepCmdSize;
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Cmd_Value : Prep cmd" );
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " getHILC_Cmd_Value  PrepCmdSize error ");
        }
    }
    else if (commandStepNumber==HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER)
    {
        if( _HILC_ConfCmdSize != 0)
        {
            dst = &_HILC_Conf_String_Cmd_Value[0];
            dstSize = _HILC_ConfCmdSize;
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Cmd_Value : Conf cmd" );
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " getHILC_Cmd_Value  ConfCmdSize error ");
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " getHILC_Cmd_Value  TypeCmd error ");
    }

    stringstream sCommandBuffer;
    for (unsigned int i=0; i<dstSize; i++)
    {
        sCommandBuffer << setfill('0') << setw(2) << hex << (unsigned int)dst[i] << " ";
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     Session getHILC_Cmd_Value:  %s", sCommandBuffer.str().c_str() );
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getHILC_Cmd_Value  size: %d",dstSize );

    return dst;
}
#endif

short Session::getIntCmdValue(int commandStepNumber)
{
    int result = -99;

    if ( commandStepNumber<=VECTOR_MAX_SIZE)
    {
        result = _HILC_Int_Cmd_Value[commandStepNumber];
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getIntCmdValue : %d",result );
    return result;
}

float Session::getFloatCmdValue(int commandStepNumber)
{
    float result = 1.0;

    if ( commandStepNumber<=VECTOR_MAX_SIZE)
    {
        result = _HILC_Float_Cmd_Value[commandStepNumber];
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " getFloatCmdValue : %f",result );
    return result;
}

void Session::setHILC_Att_Path()
{
    int numberOfAttr = HILC_ConfManager::getInstance()->HILC_ATT_NUMBER;
#ifdef USE_SESSION_EQPT							
    int sessionStateNumber = HILC_ConfManager::getInstance()->HILC_SESSIONSTATE_NUMBER;
#endif
#ifdef USE_SAME_DCI
    int currentStepNumber = HILC_ConfManager::getInstance()->HILC_CURRENTSTEP_NUMBER;
#else
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPSTATE_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFSTATE_NUMBER;
#endif
    if ( (numberOfAttr<=VECTOR_MAX_SIZE) &&  (numberOfAttr>0))
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_Att_Path");

        for (unsigned short i=0; i< numberOfAttr; i++)
        {
#ifdef USE_SESSION_EQPT							
            if (i == sessionStateNumber)
            {
                // 						        ":ied"          +   <equipement type>    +   "-Ses"             + <command Name> 		+   ".value"
                string SessionStateValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_SESSIONSTATE + _commandName + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: SessionStateValue : %s", SessionStateValueAtt.c_str() );

                _HILC_Att_Path[sessionStateNumber]=SessionStateValueAtt;
            }
            // dans les affaires où une DCI couvre la preparation et la confirmation
            else
#endif				
#ifdef USE_SAME_DCI
			if (i == currentStepNumber)
            {
                // 						        ":ied"          +   <equipement type>    +   "-Paso"             + <command Name> 		+   ".value"
                string CurrentStepValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_CURRENTSTEP + _commandName + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: CurrentStepValue : %s", CurrentStepValueAtt.c_str() );

                _HILC_Att_Path[currentStepNumber]=CurrentStepValueAtt;
            }
            // dans les affaires avec 1 DCI pour preparation et 1 pour confirmation
#else				
		/* BHE : code temporaire pour tester les AIO dans un environement TVS pas encore adapte 
			if ( _cmdType == HILC_ConfManager::HILC_AIO_CMD_TYPE )
			{
			if (i == prepStateNumber)
            {
                string prepStateValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + "-SpeedMeasure" + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: PrepStateValue : %s", prepStateValueAtt.c_str() );

                _HILC_Att_Path[prepStateNumber]=prepStateValueAtt;
            }
            else if (i == confStateNumber)
            {
                string confStateValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + "-SpeedMeasMAl" + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: ConfStateValue : %s", confStateValueAtt.c_str() );

                _HILC_Att_Path[confStateNumber]=confStateValueAtt;
            }				
			}
			else
			{ // BHE : seule section à garder au final !
		*/
			if (i == prepStateNumber)
            {
                string prepStateValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_PREPSTATE + _commandName + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: PrepStateValue : %s", prepStateValueAtt.c_str() );

                _HILC_Att_Path[prepStateNumber]=prepStateValueAtt;
            }
            else if (i == confStateNumber)
            {
                string confStateValueAtt = HILC_ConfManager::getInstance()->HILC_DCI_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_CONFSTATE + _commandName + HILC_ConfManager::getInstance()->HILC_ATTR_VALUE ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_Att_Path: ConfStateValue : %s", confStateValueAtt.c_str() );

                _HILC_Att_Path[confStateNumber]=confStateValueAtt;
            }
			// BHE code temporaire }
#endif			
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_Att_Path Error on HILC_ATT_NUMBER: %d", numberOfAttr);
    }
}

ScsStatus Session::setHILC_CMD_Path()
{
    ScsStatus status = ScsError;

    switch(_cmdType)
    {
#ifdef USE_SIO
    case HILC_ConfManager::HILC_SIO_CMD_TYPE : //Enum
        status = this->setHILC_SIO_FullPath();
        break;
#endif
    case HILC_ConfManager::HILC_DIO_CMD_TYPE : //Enum
        status = this->setHILC_DIO_FullPath();
        break;

    case HILC_ConfManager::HILC_AIO_CMD_TYPE : //Enum
        status = this->setHILC_AIO_FullPath();
        break;

    default :
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " setHILC_CMD_Path  Unknown Command Type ");
		break;
		
    }
    return status;
}
#ifdef USE_SIO		
ScsStatus Session::setHILC_SIO_FullPath()
{
    ScsStatus status = ScsError;

    int nbOfSteps = HILC_ConfManager::getInstance()->HILC_NUMBER_OF_STEP;
#ifdef USE_SESSION_EQPT										
    int enterSessionStateNumber = HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER;
#endif
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER;

    if ( (nbOfSteps<=VECTOR_MAX_SIZE) && (nbOfSteps>0) )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_SIO_Path");

        for (unsigned short i=0; i< nbOfSteps; i++)
        {
#ifdef USE_SESSION_EQPT										
            if (i == enterSessionStateNumber)
            {
                // 					              	<alias>		+     <equipmentAlias>   +    ":ict"     +   <equipement type>    +   "-Ini"       +     "Bloqueo"
                string EnterSessionSio = 	HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_SIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_SIO_ENTERSESSION_NAME + _commandName ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_SIO_Path: EnterSessionSio : %s", EnterSessionSio.c_str() );
                _HILC_CMD_Path[enterSessionStateNumber]=EnterSessionSio;
                status =ScsValid;
            }
            else
#endif				
			if (i == prepStateNumber)
            {
                // 						          <alias>		+     <equipmentAlias>   +      ":ict"       +   <equipement type>    +   "-Ses"     +     "Bloqueo"
                string PrepCmdSio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_SIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_SIO_PREPARATION_NAME + _commandName ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_SIO_Path: PrepCmdSio : %s", PrepCmdSio.c_str() );
                _HILC_CMD_Path[prepStateNumber]=PrepCmdSio;
                status =ScsValid;
            }
            else if (i == confStateNumber)
            {
                // 						          <alias>		+     <equipmentAlias>   +     ":ict"          +   <equipement type>    +   "-Pet"    +     "Bloqueo"
                string ConfCmdSio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_SIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_SIO_CONFIRMATION_NAME + _commandName ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_SIO_Path: ConfCmdSio : %s", ConfCmdSio.c_str() );
                _HILC_CMD_Path[confStateNumber]=ConfCmdSio;
                status =ScsValid;
            }
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_SIO_Path Error on HILC_NUMBER_OF_STEP: %d",nbOfSteps);
    }
    return status;
}
#endif

ScsStatus Session::setHILC_DIO_FullPath()
{
    ScsStatus status = ScsError;

    int nbOfSteps = HILC_ConfManager::getInstance()->HILC_NUMBER_OF_STEP;
#ifdef USE_SESSION_EQPT	
    int enterSessionStateNumber = HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER;
#endif
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER;

    if ( (nbOfSteps<=VECTOR_MAX_SIZE) && (nbOfSteps>0) )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_DIO_FullPath");

        for (unsigned short i=0; i< nbOfSteps; i++)
        {
#ifdef USE_SESSION_EQPT
            if (i == enterSessionStateNumber)
            {
                //                                                          <alias>		+     <equipmentAlias>   +                                  ":dio"   +   <equipement type> +                             "-"                             +"FerAnu"
                string EnterSessionDio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_DIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_DIO_ENTERSESSION_NAME + _commandName;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_DIO_FullPath: EnterSessionDio : %s", EnterSessionDio.c_str() );
                _HILC_CMD_Path[enterSessionStateNumber]=EnterSessionDio;
                status =ScsValid;
            }
            else
#endif
			if (i == prepStateNumber)
            {
                //                                                          <alias>		+     <equipmentAlias>   +                                  ":dio"   +   <equipement type> +                             "-"                             +"FerAnu"
                string PrepCmdDio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_DIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_DIO_PREPARATION_NAME + _commandName;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_DIO_FullPath: PrepCmdDio : %s", PrepCmdDio.c_str() );
                _HILC_CMD_Path[prepStateNumber]=PrepCmdDio;
                status =ScsValid;
            }
            else if (i == confStateNumber)
            {
                //                                                          <alias>		+     <equipmentAlias>   +                                  ":dio"   +   <equipement type> +                             "-"                            +   "FerAnu"
                string ConfCmdDio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_DIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_DIO_CONFIRMATION_NAME + _commandName ;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_DIO_FullPath: ConfCmdDio : %s", ConfCmdDio.c_str() );
                _HILC_CMD_Path[confStateNumber]=ConfCmdDio;
                status =ScsValid;
            }
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_DIO_FullPath Error on HILC_NUMBER_OF_STEP: %d",nbOfSteps);
    }
    return status;
}

ScsStatus Session::setHILC_AIO_FullPath()
{
    ScsStatus status = ScsError;

    int nbOfSteps = HILC_ConfManager::getInstance()->HILC_NUMBER_OF_STEP;
#ifdef USE_SESSION_EQPT
    int enterSessionStateNumber = HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER;
#endif
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER;

    if ( (nbOfSteps<=VECTOR_MAX_SIZE) && (nbOfSteps>0) )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_AIO_FullPath");

        for (unsigned short i=0; i< nbOfSteps; i++)
        {
#ifdef USE_SESSION_EQPT			
            if (i == enterSessionStateNumber)
            {
                string EnterSessionAio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_AIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_AIO_ENTERSESSION_NAME + _commandName;
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_AIO_FullPath: EnterSessionAio : %s", EnterSessionAio.c_str() );
                _HILC_CMD_Path[enterSessionStateNumber]=EnterSessionAio;
                status =ScsValid;
            }
            else
#endif				
			if (i == prepStateNumber)
            {
                string PrepCmdAio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_AIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_AIO_PREPARATION_NAME + _commandName;
				// BHE : code temporaire pour tester les AIO dans un environement TVS pas encore adapte
				// La conf doit aussi avoir temporairement HILC_AIO_PREPARATION_NAME = "-"
				// string PrepCmdAio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_AIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_AIO_PREPARATION_NAME + _commandName + "Sel";
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_AIO_FullPath: PrepCmdAio : %s", PrepCmdAio.c_str() );
                _HILC_CMD_Path[prepStateNumber]=PrepCmdAio;
                status =ScsValid;
            }
            else if (i == confStateNumber)
            {
                string ConfCmdAio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_AIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_AIO_CONFIRMATION_NAME + _commandName ;
				// BHE : code temporaire pour tester les AIO dans un environement TVS pas encore adapte
				// La conf doit aussi avoir temporairement HILC_AIO_CONFIRMATION_NAME = "-"
				// string ConfCmdAio = 		HILC_ConfManager::getInstance()->HILC_TAG_ALIAS + _equipmentAlias + HILC_ConfManager::getInstance()->HILC_AIO_NODE_NAME + _equipmentType + HILC_ConfManager::getInstance()->HILC_AIO_CONFIRMATION_NAME + _commandName + "Conf";
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     setHILC_AIO_FullPath: ConfCmdAio : %s", ConfCmdAio.c_str() );
                _HILC_CMD_Path[confStateNumber]=ConfCmdAio;
                status =ScsValid;
            }
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_AIO_FullPath Error on HILC_NUMBER_OF_STEP: %d",nbOfSteps);
    }
    return status;
}

ScsStatus Session::setHILC_Cmd_Value()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_Cmd_Value");

    ScsStatus status = ScsError;

    switch(_cmdType)
    {
#ifdef USE_SIO		
    case HILC_ConfManager::HILC_SIO_CMD_TYPE : //Enum
        this->setHILC_String_Cmd_Value();
        status = ScsValid;
        break;
#endif
    case HILC_ConfManager::HILC_DIO_CMD_TYPE : //Enum
        this->setHILC_Int_Cmd_Value();
        status = ScsValid;
        break;

    case HILC_ConfManager::HILC_AIO_CMD_TYPE : //Enum
		this->setHILC_Float_Cmd_Value();
        status = ScsValid;
        break;

    default :
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " setHILC_Cmd_Value  Unknown Command Type ");
		break;
    }

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_Cmd_Value End");
    return status;
}

#ifdef USE_SIO
void Session::setHILC_String_Cmd_Value()
{

    // ---------------------------------------------------------
    // build the message for string message command
    // add 0x0000 separator
    // add conversion to hexa
    // add conversion to big endian
    // ---------------------------------------------------------

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_String_Cmd_Value: build Message");

    // initialize
    const int iMessageMaxSize = HILC_MESSAGE_BUFFER_SIZE;

    unsigned char  temp_EnterSession_Cmd_Value[iMessageMaxSize];
    unsigned char  temp_Prep_Cmd_Value[iMessageMaxSize];
    unsigned char  temp_Conf_Cmd_Value[iMessageMaxSize];

    memset( _HILC_EnterSession_String_Cmd_Value, 0, iMessageMaxSize );
    memset( _HILC_Prep_String_Cmd_Value, 0, iMessageMaxSize );
    memset( _HILC_Conf_String_Cmd_Value, 0, iMessageMaxSize );
    // Cancel is option memset( _HILC_Cancel_String_Cmd_Value, 0, iMessageMaxSize ); // Cancel Command value

    memset( temp_EnterSession_Cmd_Value, 0, iMessageMaxSize );
    memset( temp_Prep_Cmd_Value, 0, iMessageMaxSize );
    memset( temp_Conf_Cmd_Value, 0, iMessageMaxSize );


    //Convert element used to compose the message
    unsigned int operatorNameSize = (unsigned int) _operatorName.size();
    unsigned int workStationNameSize = (unsigned int) _workStationName.size();

    //build EnterSession_Cmd Message
    this->convertMessage( (unsigned char * )_operatorName.c_str(), (unsigned char * ) &temp_EnterSession_Cmd_Value[_HILC_EnterSessionCmdSize],  operatorNameSize );
    // add  0000 separator with +2 increment
    _HILC_EnterSessionCmdSize = operatorNameSize*2+2;
    this->convertMessage( (unsigned char * )_workStationName.c_str(), (unsigned char * ) &temp_EnterSession_Cmd_Value[_HILC_EnterSessionCmdSize], workStationNameSize );
    // add  0000 separator with +2 increment
    _HILC_EnterSessionCmdSize += workStationNameSize*2+2;



    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_String_Cmd_Value: temp_EnterSession_Cmd_Value : ");
    this->TraceMessage( (unsigned char *) &temp_EnterSession_Cmd_Value[0],_HILC_EnterSessionCmdSize);



    if (2*_HILC_PrepCmdSize < iMessageMaxSize-2)
    {
		unsigned long int iCmdValue = _cmdValue;
		int formatMessageType = HILC_ConfManager::getInstance()->HILC_MESSAGE_FORMAT_TYPE;
		unsigned char prepCmdId = (unsigned char) HILC_ConfManager::getInstance()->HILC_PREP_CMD_ID;
		unsigned char confCmdId = (unsigned char) HILC_ConfManager::getInstance()->HILC_CONF_CMD_ID;
        //format message if necessary
        switch(formatMessageType)
        {
        case 0 :
            // NO Formatting of message
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_String_Cmd_Value: no need to format Message");

            memcpy( (char *) &_HILC_EnterSession_String_Cmd_Value[0], (char *) &temp_EnterSession_Cmd_Value[0], _HILC_EnterSessionCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:0: EnterSession_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_EnterSession_String_Cmd_Value[0],_HILC_EnterSessionCmdSize);

            //build Prep_Cmd Message and Conf_Cmd Message
            _HILC_PrepCmdSize = _HILC_EnterSessionCmdSize;
            _HILC_ConfCmdSize = _HILC_EnterSessionCmdSize;

            memcpy( (char *) &temp_Prep_Cmd_Value[0], (char *) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_PrepCmdSize);
            memcpy( (char *) &temp_Conf_Cmd_Value[0], (char *) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_ConfCmdSize);
            //Add CommandId and Command Type
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=prepCmdId;
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=iCmdValue;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=confCmdId;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=iCmdValue;

            memcpy( (char *) &_HILC_Prep_String_Cmd_Value[0], (char *) &temp_Prep_Cmd_Value[0], _HILC_PrepCmdSize);
            memcpy( (char *) &_HILC_Conf_String_Cmd_Value[0], (char *) &temp_Conf_Cmd_Value[0], _HILC_ConfCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:0: Prep_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_Prep_String_Cmd_Value[0],_HILC_PrepCmdSize);
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:0: Conf_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_Conf_String_Cmd_Value[0],_HILC_ConfCmdSize);


            break;

        case 1 :
            // Format message except  commandId and CommandType
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value: format Message");
            this->formatMessage( (unsigned char * ) &temp_EnterSession_Cmd_Value[0], (unsigned char * ) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_EnterSessionCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:1: EnterSession_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_EnterSession_String_Cmd_Value[0],_HILC_EnterSessionCmdSize);

            //build Prep_Cmd Message and Conf_Cmd Message
            _HILC_PrepCmdSize = _HILC_EnterSessionCmdSize;
            _HILC_ConfCmdSize = _HILC_EnterSessionCmdSize;

            memcpy( (char *) &temp_Prep_Cmd_Value[0], (char *) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_PrepCmdSize);
            memcpy( (char *) &temp_Conf_Cmd_Value[0], (char *) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_ConfCmdSize);
            //Add CommandId and Command Type
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=prepCmdId;
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=iCmdValue;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=confCmdId;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=iCmdValue;



            memcpy( (char *) &_HILC_Prep_String_Cmd_Value[0], (char *) &temp_Prep_Cmd_Value[0], _HILC_PrepCmdSize);
            memcpy( (char *) &_HILC_Conf_String_Cmd_Value[0], (char *) &temp_Conf_Cmd_Value[0], _HILC_ConfCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:1: Prep_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_Prep_String_Cmd_Value[0],_HILC_PrepCmdSize);
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:1: Conf_Cmd_Value : ");
            this->TraceMessage( (unsigned char *) &_HILC_Conf_String_Cmd_Value[0],_HILC_ConfCmdSize);


            break;

        case 2 :
            // Format all message including  commandId and CommandType

            //build Prep_Cmd Message and Conf_Cmd Message
            _HILC_PrepCmdSize = _HILC_EnterSessionCmdSize;
            _HILC_ConfCmdSize = _HILC_EnterSessionCmdSize;

            memcpy( (char *) &temp_Prep_Cmd_Value[0], (char *) &temp_EnterSession_Cmd_Value[0], _HILC_PrepCmdSize);
            memcpy( (char *) &temp_Conf_Cmd_Value[0], (char *) &temp_EnterSession_Cmd_Value[0], _HILC_ConfCmdSize);
            //Add CommandId and Command Type
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=prepCmdId;
            temp_Prep_Cmd_Value[_HILC_PrepCmdSize++]=iCmdValue;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=confCmdId;
            temp_Conf_Cmd_Value[_HILC_ConfCmdSize++]=iCmdValue;

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:2: EnterSession_Cmd_Value : ");
            this->formatMessage( (unsigned char * ) &temp_EnterSession_Cmd_Value[0], (unsigned char * ) &_HILC_EnterSession_String_Cmd_Value[0], _HILC_EnterSessionCmdSize);
            this->TraceMessage( (unsigned char *) &_HILC_EnterSession_String_Cmd_Value[0],_HILC_EnterSessionCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:2: Prep_Cmd_Value : ");
            this->formatMessage( (unsigned char * ) &temp_Prep_Cmd_Value[0], (unsigned char * ) &_HILC_Prep_String_Cmd_Value[0], _HILC_PrepCmdSize);
            this->TraceMessage( (unsigned char *) &_HILC_Prep_String_Cmd_Value[0],_HILC_PrepCmdSize);

            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "Session::setHILC_String_Cmd_Value:2: Conf_Cmd_Value : ");
            this->formatMessage( (unsigned char * ) &temp_Conf_Cmd_Value[0], (unsigned char * ) &_HILC_Conf_String_Cmd_Value[0], _HILC_ConfCmdSize);
            this->TraceMessage( (unsigned char *) &_HILC_Conf_String_Cmd_Value[0],_HILC_ConfCmdSize);

            break;

        default :
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_String_Cmd_Value: FormatMessageType Error ");
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::setHILC_String_Cmd_Value: Message Size Error");
    }

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_String_Cmd_Value: build Message End");
}
#endif

void Session::setHILC_Int_Cmd_Value()
{
    // ---------------------------------------------------------
    // build the message for int message command
    // ---------------------------------------------------------

    int nbOfSteps = HILC_ConfManager::getInstance()->HILC_NUMBER_OF_STEP;
#ifdef USE_SESSION_EQPT	
    int enterSessionStateNumber = HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER;
#endif					
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER;

    if ( (nbOfSteps<=VECTOR_MAX_SIZE) && (nbOfSteps>0) )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_Int_Cmd_Value: build Message");

        for (unsigned short i=0; i< nbOfSteps; i++)
        {
#ifdef USE_SESSION_EQPT			
            if (i == enterSessionStateNumber)
            {
                _HILC_Int_Cmd_Value[enterSessionStateNumber] = _cmdValue;
            }
            else
#endif				
			if (i == prepStateNumber)
            {
                _HILC_Int_Cmd_Value[prepStateNumber] = _cmdValue;
            }
            else if (i == confStateNumber)
            {
                _HILC_Int_Cmd_Value[confStateNumber] = _cmdValue;
            }
        }
    }
}

void Session::setHILC_Float_Cmd_Value()
{
    // ---------------------------------------------------------
    // build the message for int message command
    // ---------------------------------------------------------

    int nbOfSteps = HILC_ConfManager::getInstance()->HILC_NUMBER_OF_STEP;
#ifdef USE_SESSION_EQPT	
    int enterSessionStateNumber = HILC_ConfManager::getInstance()->HILC_ENTERSESSION_STEP_NUMBER;
#endif					
    int prepStateNumber = HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER;
    int confStateNumber = HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER;
	unsigned short div=1;
	
	if (_cmdValueDiv > 1)
	{
		div = _cmdValueDiv;
	}

    if ( (nbOfSteps<=VECTOR_MAX_SIZE) && (nbOfSteps>0) )
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::setHILC_Int_Cmd_Value: build Message");

        for (unsigned short i=0; i< nbOfSteps; i++)
        {
#ifdef USE_SESSION_EQPT			
            if (i == enterSessionStateNumber)
            {
                _HILC_Float_Cmd_Value[enterSessionStateNumber] = (float)_cmdValue / div;
            }
            else
#endif				
			if (i == prepStateNumber)
            {
                _HILC_Float_Cmd_Value[prepStateNumber] = (float)_cmdValue / div;
            }
            else if (i == confStateNumber)
            {
                _HILC_Float_Cmd_Value[confStateNumber] = (float)_cmdValue / div;
            }
        }
    }
}

#ifdef USE_SIO
const void Session::convertMessage(unsigned char * src , unsigned char * dst,  unsigned int lengthSrc )
{
    const int iMessageMaxSize = HILC_MESSAGE_BUFFER_SIZE;
    memset(dst, 0, iMessageMaxSize);
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::convertMessage: lengthSrc : %d", lengthSrc);

    int newlength=lengthSrc*2;
    if ( newlength < iMessageMaxSize)
    {
        // char to wchar convertion
        std::vector<wchar_t> WBuffer(newlength);
        std::locale loc(HILC_ConfManager::getInstance()->HILC_MESSAGE_LOCALE.c_str());
        //OS dependant
        std::use_facet< std::ctype<wchar_t> > (loc).widen( (char *) &src[0], (char *) &src[lengthSrc] , &WBuffer[0] );

        // wchar to unsigned char convertion + conversion to big endian

        unsigned short wideChar = 0;
        unsigned char * pWide = (unsigned char *) &wideChar;
        for( unsigned int i=0, j=0; i<lengthSrc; i++ )
        {
            wideChar = WBuffer[ i ];
            dst[j++] = pWide[1];
            dst[j++] = pWide[0];
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::convertMessage: Message size Error" );
    }

}
#endif

#ifdef USE_SIO
const void Session::formatMessage(unsigned char * src , unsigned char * dst,  unsigned int &lengthSrc )
{
    const int iMessageMaxSize = HILC_MESSAGE_BUFFER_SIZE;
    memset(dst, 0, iMessageMaxSize);
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::formatMessage: lengthSrc : %d", lengthSrc);
    unsigned int newLength = 2*lengthSrc;

    if ( newLength < iMessageMaxSize-2)
    {
        stringstream sBuffer;

        for (unsigned int i=0; i<lengthSrc; i++)
        {
            sBuffer << setfill('0') << setw(2) << hex << (unsigned int) src[i] ;
        }
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     sBuffer:  %s", sBuffer.str().c_str() );
        memcpy( (char *) &dst[0], (char *)sBuffer.str().c_str(), newLength);
        lengthSrc = newLength;
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session::formatMessage: lengthDst : %d", lengthSrc);
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::formatMessage: Message size Error" );
    }

}
#endif

#ifdef USE_SIO
void Session::TraceMessage(unsigned char * src,  unsigned int lengthSrc ) const
{
    stringstream sSrcCommandBuffer;

    for (unsigned int i=0; i<lengthSrc; i++)
    {
        sSrcCommandBuffer << setfill('0') << setw(2) << hex << (unsigned int) src[i] << " ";
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     sSrcCommandBuffer:  %s", sSrcCommandBuffer.str().c_str() );
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     sSrcCommandBufferSize:  %d", lengthSrc );
}
#endif

void Session::print()
{
    stringstream sBuffer;

    sBuffer 	<< " operatorName=" << _operatorName<< endl
    << " workStationName=" <<  _workStationName<< endl
    << " cmdType=" << _cmdType<< endl
    << " cmdValue=" << _cmdValue<< endl
    << " cmdValueDiv=" << _cmdValueDiv<< endl
    << " equipmentAlias=" << _equipmentAlias<< endl
    << " equipmentType=" << _equipmentType<< endl
    << " commandName=" << _commandName<< endl;
    string currentState = getCurrentState()->info();
    if ( !currentState.empty() )
    {
        sBuffer <<"currentState: " << currentState<< endl;
    }

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session::print:%s",sBuffer.str().c_str());
}

ScsStatus Session::sendMyCommand(int commandStepNumber)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session sendMyCommand");

    string commandPath = this->getHILC_CMD_Path(commandStepNumber);

    if (commandPath.size()>0)
    {
        switch(_cmdType)
        {
#ifdef USE_SIO			
        case HILC_ConfManager::HILC_SIO_CMD_TYPE : //Enum
            status = this->sendMyStringCommand( commandStepNumber, commandPath);
            break;
#endif
        case HILC_ConfManager::HILC_DIO_CMD_TYPE : //Enum
            status = this->sendMyIntCommand( commandStepNumber, commandPath);
            break;

		case HILC_ConfManager::HILC_AIO_CMD_TYPE : //Enum
			status = this->sendMyFloatCommand( commandStepNumber, commandPath);
			break;
			
        default :
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " sendMyCommand  Unknown Command Type ");
			break;
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), " sendMyCommand  null Command Path ");
    }
    return status;
}

#ifdef USE_SIO			
ScsStatus Session::sendMyStringCommand(int commandStepNumber, string commandPath)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session sendMyStringCommand : ");

    unsigned int uiMessageSize =0 ;

    unsigned char  * commandMessage = getCmdValue(uiMessageSize, commandStepNumber);

    if (commandMessage)
    {
        stringstream sCommandBuffer;
        for (unsigned int i=0; i<uiMessageSize; i++)
        {
            sCommandBuffer << setfill('0') << setw(2) << hex << (unsigned int)commandMessage[i] << " ";
        }
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     Session sendMyStringCommand: %s", sCommandBuffer.str().c_str() );
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "     with source: %s", this->buildEventLogSource().c_str() );
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");

        //Build and sendCommand
        if( uiMessageSize>0 )
        {
            HILC_UpdateData	* m_updateData=HILC_UpdateData::getInstance();
            status = m_updateData->sendCommand(commandPath, commandMessage, uiMessageSize, this->buildEventLogSource());
        }
        else
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session sendMyStringCommand: Command Message Size Error");
        }
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session sendMyStringCommand: Command  Error : NULL Buffer");
    }

    return status;
}
#endif

ScsStatus Session::sendMyIntCommand(int commandStepNumber, string commandPath)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session sendMyIntCommand : ");

    unsigned int uiMessageSize =0 ;

    short commandMessageValue = getIntCmdValue(commandStepNumber);

    //Build and sendCommand
    if (commandMessageValue!=-99)
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     Session sendMyIntCommand int:  %d", commandMessageValue );
		SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "     with source: %s", this->buildEventLogSource().c_str() );
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");


        HILC_UpdateData	* m_updateData=HILC_UpdateData::getInstance();
		
        status = m_updateData->sendCommand(commandPath, commandMessageValue, this->buildEventLogSource());
        // status = m_updateData->sendCommand(commandPath, commandMessage, uiMessageSize, this->getSrc());
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "Session sendMyIntCommand: Command Message Error");
    }
    return status;
}

ScsStatus Session::sendMyFloatCommand(int commandStepNumber, string commandPath)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "Session sendMyFloatCommand : ");

    unsigned int uiMessageSize =0 ;

    float commandMessageValue = getFloatCmdValue(commandStepNumber);

    //Build and sendCommand
	SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "     Session sendMyFloatCommand float:  %f", commandMessageValue );
	SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "     with source: %s", this->buildEventLogSource().c_str() );
	SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "###################################################################################################");

	HILC_UpdateData	* m_updateData=HILC_UpdateData::getInstance();
	
	status = m_updateData->sendCommand(commandPath, commandMessageValue, this->buildEventLogSource());
    return status;
}

void Session::setHILCTimer()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " #########################Session: setHILCTimer");
    _timerHILC = new ScsTimer(HILC_ConfManager::getInstance()->HILC_TIMER_DELAY, 0, HILC_UpdateData::timerCallback, (ScsAny)this);

}

void Session::setFinalHILCTimer()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), " #########################Session: setFinalHILCTimer");
    _finalTimerHILC = new ScsTimer(0, 0, HILC_UpdateData::finalTimerCallback, (ScsAny)this);
    //Timer call inorder  to avoid deadlock between Sighilc and dbmpoller
    //We want to delete session stop its Scstimer, unsubscribe equipememnt when the session reachs the final state (Pattern State)
}
