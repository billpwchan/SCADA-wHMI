/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#ifndef __STATE_HILC_H__
#define __STATE_HILC_H__


#include <string>
#include "scs.h"
using namespace std;
class Session;
class State
{
public:
    //Methode appellee lors de reception de notification sur la Session qui possede le State
    virtual ScsStatus actionOnNotify(Session *session);
    //Methode appellee apres le changement de state
    virtual ScsStatus actionOnInit(Session *session);
    // Methode appellee lorsque le Timer HILC arrive a echeance
    virtual ScsStatus actionOnTimerEnd(Session *session);
    //Methode appellee lorsque la Session identifie une commande de preparation associee a la commande de confirmation qu'elle vient de recevoir
    virtual ScsStatus actionOnPreparationCommandFound(Session *session);

    //virtual ScsStatus actionOnCancel(Session *session);
    //A redefinir dans les classes concrete pour retourner le nom de la classe
    virtual string info();
    virtual ~State();
};

#endif //__STATE_HILC_H__


