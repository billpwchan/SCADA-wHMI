#ifndef __SESSION_HILC_H__
#define __SESSION_HILC_H__


#include "State.h"
#include "HILC_ConfManager.h"
#include "scs.h"
#include "scstimer.h"
#include <sstream>
#include <string>
#include <vector>
using namespace std;

// taille max des tableaux
#ifdef USE_SIO	
const int HILC_MESSAGE_BUFFER_SIZE = 1024;
#endif
const int VECTOR_MAX_SIZE = 10;

class Session
{
private:

// attributs principaux associes a la commande securisee
    string _operatorName;
    string _workStationName;
    unsigned short _cmdType;
    short _cmdValue;
	unsigned short _cmdValueDiv;
    string _equipmentAlias;
    string _equipmentType;
    string _commandName;
    string _groupName;
    int _id;

    string _HILC_Att_Path[VECTOR_MAX_SIZE+1];
    string _HILC_CMD_Path[VECTOR_MAX_SIZE+1];

#ifdef USE_SIO	
// Tailles des messages
    unsigned int _HILC_EnterSessionCmdSize;
    unsigned int _HILC_PrepCmdSize;
    unsigned int _HILC_ConfCmdSize;
    // Cancel is an option unsigned int _HILC_CancelCmdSize;


// Valeurs des commandes
    unsigned char  _HILC_EnterSession_String_Cmd_Value[HILC_MESSAGE_BUFFER_SIZE];
    unsigned char  _HILC_Prep_String_Cmd_Value[HILC_MESSAGE_BUFFER_SIZE];
    unsigned char  _HILC_Conf_String_Cmd_Value[HILC_MESSAGE_BUFFER_SIZE];
    // Cancel is an option unsigned char  _HILC_Cancel_String_Cmd_Value[HILC_MESSAGE_BUFFER_SIZE];
#endif

    short _HILC_Int_Cmd_Value[VECTOR_MAX_SIZE+1];
	float _HILC_Float_Cmd_Value[VECTOR_MAX_SIZE+1];

    State * _currentState;

    ScsTimer * _timerHILC;
    ScsTimer * _finalTimerHILC;

    /**
    * Copy and default constructor are private and not implemented.
    */
    Session( const Session& );
    Session();

    /**
     * Assignment operator is private and not implemented.
     */
    Session& operator=( const Session& );


public:
    //Session();
    Session(string operatorName, string workStationName, unsigned short cmdType, short cmdValue, unsigned short cmdValueDiv,string equipmentAlias, string equipmentType, string commandName, string groupName, int eqtNumber);
    void init();

    /*
    //getter and setter
    */
    int getId() const
    {
        return _id;
    };
    void setId(int newId)
    {
        _id=newId;
    };
    void setGpN(string  newGpN)
    {
        _groupName=newGpN;
    };
    void setHILC_Att_Path();
    ScsStatus setHILC_CMD_Path();
#ifdef USE_SIO
    ScsStatus setHILC_SIO_FullPath();
#endif
    ScsStatus setHILC_DIO_FullPath();
    ScsStatus setHILC_AIO_FullPath();
    ScsStatus setHILC_Cmd_Value();
#ifdef USE_SIO
    void setHILC_String_Cmd_Value();
#endif
    void setHILC_Int_Cmd_Value();
	void setHILC_Float_Cmd_Value();
    void setHILCTimer();
    void setFinalHILCTimer();

    State* getCurrentState();

    void setCurrentState(State * newState);


    string getOpN() const
    {
        return _operatorName;
    };
    string getGpN() const
    {
        return _groupName;
    };
    string getWS() const
    {
        return _workStationName;
    };
    string getEqA() const
    {
        return _equipmentAlias;
    };
    string getEqT() const
    {
        return _equipmentType;
    };
    string getCmdN() const
    {
        return _commandName;
    };
	/*
    string getSrc() const
    {
        return _source;
    };
	*/
	string buildEventLogSource() const;
    
	string getHILC_Att_Path(int i);
    string getHILC_CMD_Path(int i);
    unsigned char * getCmdValue( unsigned int & dstSize, int commandStepNumber);
    short getIntCmdValue(int commandStepNumber);
	float getFloatCmdValue(int commandStepNumber);

    int getCmdT() const
    {
        return _cmdType;
    };
    short getCmdV() const
    {
        return _cmdValue;
    };

    unsigned short getCmdValueDiv() const
    {
        return _cmdValueDiv;
    };

    ScsTimer * getTimerHILC()
    {
        return _timerHILC;
    };
    ScsTimer * getFinalTimerHILC()
    {
        return _finalTimerHILC;
    };

    ScsStatus actionOnNotify();
    ScsStatus actionOnInit();
    ScsStatus actionOnTimerEnd();
#ifdef USE_SIO	
    ScsStatus sendMyStringCommand(int commandStepNumber, string commandPath);
#endif	
    ScsStatus sendMyIntCommand(int commandStepNumber, string commandPath);
    ScsStatus sendMyFloatCommand(int commandStepNumber, string commandPath);
    ScsStatus sendMyCommand(int commandStepNumber);

    /*
    //construction affichage du message securises
    */
#ifdef USE_SIO
    static const void convertMessage(unsigned char * src , unsigned char * dst,  unsigned int lengthSrc );
    static const void formatMessage(unsigned char * src , unsigned char * dst,  unsigned int & lengthSrc );
    void TraceMessage(unsigned char * src, unsigned int lengthSrc ) const;
#endif

    void print();

    ~Session();
};

#endif //__SESSION_HILC_H__

