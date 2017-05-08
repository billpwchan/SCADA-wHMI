#include "State.h"
#include "Session.h"
#include "Traces.h"
#include "SigTraces.h"
//Not used yet #include "SigError.h"
#include "HILCdefs.h"
#include <iostream>
using namespace std;

//Methode appellee lors de reception de notification sur la Session qui possede ce State
ScsStatus State::actionOnNotify(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State actionOnNotify");
    return status;
}

//Methode appellee apres le changement de state
ScsStatus State::actionOnInit(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State actionOninit");
    return status;
}

//Methode appellee lorsque la Session identifie une commande de preparation associee a la commande de confirmation qu'elle vient de recevoir
ScsStatus State::actionOnPreparationCommandFound(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State actionOnConfirmCommandFound");
    return status;
}

// Methode appellee lorsque le Timer HILC arrive a echeance
ScsStatus State::actionOnTimerEnd(Session *session)
{
    ScsStatus status = ScsValid;
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ), "State actionOnTimerEnd");
    return status;
}

State::~State()
{
    SigTrace(SCS_LEVEL( HILC_TRACE_LEVEL_DETAIL ),"   State-dtor ");
}
string State::info()
{
    return "";
}
