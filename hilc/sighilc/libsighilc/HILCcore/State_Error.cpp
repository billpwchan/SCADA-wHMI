#include "State_Error.h"
#include "Session.h"
#include "Traces.h"
#include "SigTraces.h"
#include "SessionManager.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include <iostream>
using namespace std;

State_Error::State_Error()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   State_Error-ctor ");
}


State_Error::~State_Error()
{

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "   State_Error-dtor ");
}


ScsStatus  State_Error::actionOnNotify(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_FUNC ), "State_Error  actionOnNotify");
    // Don't send any command in this error  state
    return status;
}

ScsStatus State_Error::actionOnInit(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Error actionOninit");

    // send Alarm
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Error Send Alarm");

    HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();

    status = m_updateData->sendAlarmExt(session->getEqA().c_str());

    //Delete State Session and dbmPoller subscription
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Error Delete State Session and dbmPoller subscription");

    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Error: Set Timer for dbmpoller unsubscription");
    //Ask the session to start its final timer
    //the goal is  to avoid deadlock between Sighilc and dbmpoller
    //We want to delete session stop its Scstimer, unsubscribe equipememnt
    session->setFinalHILCTimer();

    delete this;

    return status;
}

string  State_Error::info()
{
    return "State_Error";
}
