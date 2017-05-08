#include "State_Conf.h"
#include "State_WaitingConf.h"
#include "State_Error.h"
#include "State_Error_TimeOut.h"
// BHE : besoin pas encore exprime sur Lusail Doha
//#include "State_Cancel.h"
#include "HILC_UpdateData.h"
#include "Session.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include <iostream>
using namespace std;

State_WaitingConf::State_WaitingConf()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_WaitingConf-ctor ");
}


State_WaitingConf::~State_WaitingConf()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_WaitingConf-dtor ");
}


ScsStatus  State_WaitingConf::actionOnNotify(Session *session)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"State_WaitingConf  actionOnNotify");
    ScsStatus status = ScsError;
    ScsStatus status2 = ScsError;

    HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
    string equip = session->getEqA();

    // Utilisation du ConfManger pour identifier les valeurs des conditions de changements d'etats
    // pour les modes degrades
    // Il n'est actuellement pas prevu dans le code une condition sur Notify qui ferait passer a l'etat "Conf"
    int stateWaitingConfErrorCondValNumber = HILC_ConfManager::getInstance()->HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVALNUMBER;
    int CurrentStepValueErrorCondVal = -1;
    //int CurrentStepValueErrorCondVal2 = HILC_ConfManager::getInstance()->HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL2;

#ifdef USE_SAME_DCI
    // dans les affaires où une DCI couvre la preparation et la confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_CURRENTSTEP_NUMBER);
#else
    // dans les affaires avec 1 DCI pour preparation et 1 pour confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_PREPSTATE_NUMBER);
#endif
    // Vérification des conditions d'erreur du State
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf:  Verif Error State Condition Start");

    //identifier les conditions d'erreur du  CurrentStepValue associe a ce state
    for (unsigned short i=1 ; i <= stateWaitingConfErrorCondValNumber ; i++)
    {
        if (i == 1)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL1;
        }
        else if (i == 2)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_WAITINGCONF_ATT1_ERROR_CONDVAL2;
        }

        status = m_updateData->verifEquipementAttributIntvalue(equip,CurrentStepValueAtt,CurrentStepValueErrorCondVal);
        if (status.isValid())
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf:  Error State Condition found: %d", CurrentStepValueErrorCondVal);
            session->setCurrentState(new State_Error());
            status = session->actionOnInit();
            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf  actionOnNotify: next state actionOnInit OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_WaitingConf actionOnNotify: next state actionOnInit KO" );
            }
            delete this;
            return status;
        }
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_WaitingConf No Error ");

    return status;
}

ScsStatus State_WaitingConf::actionOnInit(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_WaitingConf actionOninit");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf Waiting for Confirmation Command");

    // Don't send any command in this state because we are waiting operator confirmation

    return status;

}

ScsStatus State_WaitingConf::actionOnTimerEnd(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_WaitingConf actionOnTimerEnd");
    // Changement de State vers le State Error_Timeout
    session->setCurrentState(new State_Error_TimeOut());
    status = session->actionOnInit();
    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf  actionOnNotify: next state actionOnInit OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_WaitingConf  actionOnNotify: next state actionOnInit KO" );
    }
    delete this;

    return status;
}

string  State_WaitingConf::info()
{
    return "State_WaitingConf";
}

ScsStatus  State_WaitingConf::actionOnPreparationCommandFound(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"State_WaitingConf  actionOnPreparationCommandFound");

    //Preparation command found for the confirmation send by the operator
    //We can change state to send the next command
    session->setCurrentState(new State_Conf());
    status = session->actionOnInit();
    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_WaitingConf  actionOnNotify: next state actionOnInit OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_WaitingConf  actionOnNotify: next state actionOnInit KO" );
    }
    delete this;
    return status;
}

