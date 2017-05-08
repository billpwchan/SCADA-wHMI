#ifndef __REDUTIL_H__
#define __REDUTIL_H__
//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : pcc
//|
//| Purpose           : 
//|
//| Filename          : redutil.h
//|
//| Creation Date     : 12/11/2001
//|
//| Author            : JC Menchi	
//|
//| History           : date, name, action
//| 			21/10/1999	Administration ClearCase
//| 		Automatic template insertion
//|
//+-----------------------------------------------------------------------

#define CREATE_PROXY(CLASSNAME, ENVNAME, SERVERNAME, PROXYVAR, STATUSVAR)    \
  STATUSVAR = CLASSNAME##Proxy::Create##CLASSNAME##Proxy(ENVNAME, SERVERNAME, PROXYVAR);

#include "scsversion.h"
#if SCS_INTEGER_VERSION < 5000000L
#define CREATE_SERVANT(SMART_PTR, SERVANT_CLS, ptr, servant) \
   (ptr) = (servant);
   
#define DESTROY_SERVANT(SERVANT_CLS,ptr) \
   delete (ptr);

#else

#include "scadaorb.h"
#define CREATE_SERVANT(SMART_PTR, SERVANT_CLS, ptr, servant) \
   activate<SMART_PTR,SERVANT_CLS>((ptr),(servant));

#define DESTROY_SERVANT(SERVANT_CLS, ptr) \
  deactivatePtr<SERVANT_CLS>((ptr)); delete (ptr);

#endif



 
#endif // __REDUTIL_H__
