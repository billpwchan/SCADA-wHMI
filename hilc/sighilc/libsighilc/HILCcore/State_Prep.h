/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#ifndef __STATE_PREP_HILC_H__
#define __STATE_PREP_HILC_H__


#include "State.h"
#include "Session.h"
#include "scs.h"
#include <string>

class State_Prep :	public State
{
private:

public:
    State_Prep();
    virtual ~State_Prep();
    //Methode appellee lors de reception de notification sur la Session qui possede le State
    virtual ScsStatus actionOnNotify(Session *session);
    //Methode appellee apres le changement de state
    virtual ScsStatus actionOnInit(Session *session);
    // Methode appellee lorsque le Timer HILC arrive a echeance
    virtual ScsStatus actionOnTimerEnd(Session *session);
    //virtual ScsStatus actionOnCancel(Session *session);
    virtual string info();
};

#endif //__STATE_Prep_HILC_H__

