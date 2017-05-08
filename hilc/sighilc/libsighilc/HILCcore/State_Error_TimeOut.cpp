#include "State_Error_TimeOut.h"
#include "Session.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include "SessionManager.h"
#include <iostream>
using namespace std;

State_Error_TimeOut::State_Error_TimeOut()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Error_TimeOut-ctor");
}


State_Error_TimeOut::~State_Error_TimeOut()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State_Error_TimeOut-dtor");
}


ScsStatus  State_Error_TimeOut::actionOnInit(Session *session)
{
    ScsStatus status = ScsError;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Error_TimeOut actionOninit");

    // send Alarm
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_ERROR ), "State_Error_TimeOut Send Alarm ");
    HILC_UpdateData * m_updateData = HILC_UpdateData::getInstance();
    //m_updateData->sendAlarmExt(HILC_K_ALA_EXTERN_01, session->getEqA().c_str(),0,ALMEXT_OBJ_STATE_ERROR);
    status = m_updateData->sendAlarmExt(session->getEqA().c_str());

    delete this;

    return status;
}

string  State_Error_TimeOut::info()
{
    return "State_Error_TimeOut";
}
