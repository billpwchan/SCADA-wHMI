/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/

#ifndef __STATE_ERROR_HILC_H__
#define __STATE_ERROR_HILC_H__


#include "State.h"
#include "Session.h"
#include "scs.h"
#include <string>

class State_Error :	public State
{
private: 
	
public:
	State_Error();
	virtual ~State_Error();
  //Methode appellee lors de reception de notification sur la Session qui possede le State 
	virtual ScsStatus actionOnNotify(Session *session);
  //Methode appellee apres le changement de state
	virtual ScsStatus actionOnInit(Session *session);
	virtual string info();
};

#endif //__STATE_ERROR_HILC_H__

