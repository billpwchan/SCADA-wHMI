#include "State_Final.h"
#include "Session.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include "SessionManager.h"
#include <iostream>
using namespace std;

State_Final::State_Final()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Final-ctor ");
}


State_Final::~State_Final()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State_Final-dtor ");
}


ScsStatus  State_Final::actionOnNotify(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ),"State_Final  actionOnNotify");
    // Don't send any command in this last state
    return status;

}

ScsStatus State_Final::actionOnInit(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Final actionOninit");
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Final Delete State Session and dbmPoller subscription ");

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Final: Set Timer for dbmpoller unsubscription");
    //Ask the session to start its final timer
    //the goal is  to avoid deadlock between Sighilc and dbmpoller
    //We want to delete session stop its Scstimer, unsubscribe equipememnt
    session->setFinalHILCTimer();
    status = session->getFinalTimerHILC()->getStatus();

    delete this;


    return status;

}

string  State_Final::info()
{
    return "State_Final";
}
