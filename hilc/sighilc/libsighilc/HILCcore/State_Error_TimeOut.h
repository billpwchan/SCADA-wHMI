/******************************************************************************/
/* Thales SFI                                                                 */
/*CSCI          : HILC                                                        */
/******************************************************************************/
#ifndef __STATE_ERROR_TIMEOUT_HILC_H__
#define __STATE_ERROR_TIMEOUT_HILC_H__


#include "State.h"
#include "Session.h"
#include "scs.h"
#include <string>

class State_Error_TimeOut :	public State
{
private: 
	
public:
	State_Error_TimeOut();
	virtual ~State_Error_TimeOut();
  //Methode appellee apres le changement de state
	virtual ScsStatus actionOnInit(Session *session);	
	virtual string info();
};

#endif //__STATE_ERROR_TIMEOUT_HILC_H__

