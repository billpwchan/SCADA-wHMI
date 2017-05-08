#include "State_Prep.h"
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

State_Prep::State_Prep()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Prep-ctor ");
}


State_Prep::~State_Prep()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Prep-dtor ");
}


ScsStatus  State_Prep::actionOnNotify(Session *session)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"State_Prep  actionOnNotify");
    ScsStatus status = ScsError;
    ScsStatus status2 = ScsError;

    HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
    string equip = session->getEqA();

    int statePrepCondValNumber = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_CONDVALNUMBER;
    int CurrentStepValueCondVal = -1;

    int statePrepErrorCondValNumber = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_ERROR_CONDVALNUMBER;
    int CurrentStepValueErrorCondVal = -1;

#ifdef USE_SAME_DCI	
    // dans les affaires où une DCI couvre la preparation et la confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_CURRENTSTEP_NUMBER);
#else
    // dans les affaires avec 1 DCI pour preparation et 1 pour confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_PREPSTATE_NUMBER);
#endif
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep:  Verif Change State Condition Start");

    // Vérification des conditions de transition au State suivant
    for (unsigned short i=1 ; i <= statePrepCondValNumber ; i++)
    {
        if (i == 1)
        {
            CurrentStepValueCondVal = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_CONDVAL1;
        }
        /* Pas encore defini
        else if (i == 2)
        {
            CurrentStepValueCondVal = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_CONDVAL2;
        }
        */
        status = m_updateData->verifEquipementAttributIntvalue(equip,CurrentStepValueAtt,CurrentStepValueCondVal);
        if (status.isValid())
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep:  Change State Condition OK ");
            session->setCurrentState(new State_WaitingConf());
            status = session->actionOnInit();
            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep  actionOnNotify: next state actionOnInit OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Prep  actionOnNotify: next state actionOnInit KO" );
            }
            delete this;
            return status;
        }
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Prep:  Change State Condition KO");

    // Vérification des conditions d'erreur du State Preparation
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep:  Verif Error State Condition Start");
    // Vérification des conditions d 'erreur du State

    //identifier les conditions d'erreur du  CurrentStepValue associe a ce state
    for (unsigned short i=1 ; i <= statePrepErrorCondValNumber ; i++)
    {
        if (i == 1)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_ERROR_CONDVAL1;
        }
        else if (i == 2)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_ERROR_CONDVAL2;
        }

        status = m_updateData->verifEquipementAttributIntvalue(equip,CurrentStepValueAtt,CurrentStepValueErrorCondVal);
        if (status.isValid() )
        {
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep: Error State Condition found : %d", CurrentStepValueErrorCondVal);
            session->setCurrentState(new State_Error());
            status = session->actionOnInit();
            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep  actionOnNotify: next state actionOnInit OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Prep actionOnNotify: next state actionOnInit KO" );
            }
            delete this;
            return status;
        }
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Prep No Error");

    return status;
}

ScsStatus State_Prep::actionOnInit(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Prep actionOninit");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep Send Preparation Command Start");

    // send command Preparation
    status = session->sendMyCommand(HILC_ConfManager::getInstance()->HILC_PREPARATION_STEP_NUMBER);

    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep Send Preparation Command OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Prep Send Preparation Command Failed");
    }

    return status;

}

ScsStatus State_Prep::actionOnTimerEnd(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Prep actionOnTimerEnd");
    // Changement de State vers le State Error_Timeout
    session->setCurrentState(new State_Error_TimeOut());
    status = session->actionOnInit();
    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Prep  actionOnTimerEnd: next state actionOnInit OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Prep  actionOnTimerEnd: next state actionOnInit KO" );
    }
    delete this;

    return status;
}

string  State_Prep::info()
{
    return "State_Prep";
}


