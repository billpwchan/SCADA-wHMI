/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#include "State_Final.h"
#include "State_Conf.h"
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

State_Conf::State_Conf()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Conf-ctor ");
}


State_Conf::~State_Conf()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Conf-dtor ");
}

//Méthode appellee lors de reception de notification sur la Session qui possede le State
ScsStatus  State_Conf::actionOnNotify(Session *session)
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"State_Conf  actionOnNotify");
    ScsStatus status = ScsError;
    ScsStatus status2 = ScsError;

    HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
    string equip = session->getEqA();
    // Utilisation du ConfManger pour identifier les valeurs des conditions de changements d'etats
    // pour le cas nominal et les modes degrades
    int stateConfCondValNumber = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_CONDVALNUMBER;
    int CurrentStepValueCondVal = -1;

    int stateConfErrorCondValNumber = HILC_ConfManager::getInstance()->HILC_STATE_PREP_ATT1_ERROR_CONDVALNUMBER;
    int CurrentStepValueErrorCondVal = -1;

#ifdef USE_SAME_DCI
    // dans les affaires où une DCI couvre la preparation et la confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_CURRENTSTEP_NUMBER);
#else
    // dans les affaires avec 1 DCI pour preparation et 1 pour confirmation
    string CurrentStepValueAtt = session->getHILC_Att_Path(HILC_ConfManager::getInstance()->HILC_CONFSTATE_NUMBER);
#endif
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf:  Verif Change State Condition Start");

    // Vérification des conditions de transition au State suivant
    for (unsigned short i=1 ; i <= stateConfCondValNumber ; i++)
    {
        if (i == 1)
        {
            CurrentStepValueCondVal = HILC_ConfManager::getInstance()->HILC_STATE_CONF_ATT1_CONDVAL1;
        }
        /* Pas encore defini
        else if (i == 2)
        {
            CurrentStepValueCondVal = HILC_ConfManager::getInstance()->HILC_STATE_CONF_ATT1_CONDVAL2;
        }
        */
        status = m_updateData->verifEquipementAttributIntvalue(equip,CurrentStepValueAtt,CurrentStepValueCondVal);
        if (status.isValid())
        {
            // La procedure d'envoi de commande se finalise
            // Changement de State vers le State Final
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf:  Change State Condition OK ");
            session->setCurrentState(new State_Final());
            status = session->actionOnInit();
            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf  actionOnNotify: next state actionOnInit OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Conf  actionOnNotify: next state actionOnInit KO" );
            }
            delete this;
            return status;
        }
    }
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Conf:  Change State Condition KO");

    // Vérification des conditions d'erreur du State
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf:  Verif Error State Condition Start");

    //identifier les conditions d'erreur du  CurrentStepValue associe a ce state
    for (unsigned short i=1 ; i <= stateConfErrorCondValNumber ; i++)
    {
        if (i == 1)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_CONF_ATT1_ERROR_CONDVAL1;
        }
        else if (i == 2)
        {
            CurrentStepValueErrorCondVal = HILC_ConfManager::getInstance()->HILC_STATE_CONF_ATT1_ERROR_CONDVAL2;
        }

        status = m_updateData->verifEquipementAttributIntvalue(equip,CurrentStepValueAtt,CurrentStepValueErrorCondVal);
        if (status.isValid()  )
        {
            // Changement de State vers le State Error
            SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf:  Error State Condition found: %d", CurrentStepValueErrorCondVal);
            session->setCurrentState(new State_Error());
            status = session->actionOnInit();
            if (status.isValid())
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf  actionOnNotify: next state actionOnInit OK");
            }
            else
            {
                SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Conf actionOnNotify: next state actionOnInit KO" );
            }
            delete this;
            return status;
        }
    }
    status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Conf No Error");

    return status;
}

//Méthode appellee apres le changement de state
ScsStatus State_Conf::actionOnInit(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Conf actionOninit");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf Send Confirmation Command Start");

    // send command Confirmation
    status = session->sendMyCommand(HILC_ConfManager::getInstance()->HILC_CONFIRMATION_STEP_NUMBER);

    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf Send Confirmation Command OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Conf Send Confirmation Command Failed");
    }

    return status;

}

// Méthode appellee lorsque le Timer HILC arrive a échéance
ScsStatus State_Conf::actionOnTimerEnd(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Conf actionOnTimerEnd");

    session->setCurrentState(new State_Error_TimeOut());
    status = session->actionOnInit();
    if (status.isValid())
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Conf  actionOnNotify: next state actionOnInit OK");
    }
    else
    {
        SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Conf  actionOnNotify: next state actionOnInit KO" );
    }
    delete this;

    return status;
}

string  State_Conf::info()
{
    return "State_Conf";
}


