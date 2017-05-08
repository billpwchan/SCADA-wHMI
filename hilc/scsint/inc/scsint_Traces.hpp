#ifndef __scsint_Traces
#define __scsint_Traces

#ifdef WIN32
#ifdef __cplusplus
#ifdef SCSINT_TRACES_EXPORTS
#define SCSINT_TRACES_API __declspec(dllexport)
#else
#define SCSINT_TRACES_API __declspec(dllimport)
#endif //SCSINT_TRACES_EXPORTS
#endif // __cplusplus
#else
//!WIN32
#define SCSINT_TRACES_API
#endif // WIN32


#include "Ats_types.h"
#include "Traces.h"

#include <stdarg.h>

//+===========================================================================
//+ Macro: ATS_Level(i)
//+
//+ Purpose  : To define various levels of tracing.
//+
//+===========================================================================
#define ATS_Level(i)    i,__LINE__,__FILE__

//+===========================================================================
//+ Macro : scsint_UserAtsTraces
//+
//+ Purpose  : Macro used by others module for the traces management
//+
//+===========================================================================
#define scsint_UserAtsTraces(i) \
scsint_SetModule(i); \
scsint_AtsTraces

//+===========================================================================
//+ Macro : AtsTrace
//+
//+ Purpose  : Macro used for all traces with level module K_NUM_ATS
//+
//+===========================================================================
#define AtsTrace scsint_UserAtsTraces(K_NUM_ATS)

extern "C" {
//+===========================================================================
//+ Function : scsint_SetUserTrace
//+
//+ Purpose  : This function sets the User Trace and module level for Scadasoft.
//+
//+===========================================================================
SCSINT_TRACES_API AtsUInt32 scsint_SetUserTrace(AtsInt32 ai_ModuleMask,
						AtsInt32 ai_LevelMask,
						AtsInt32 ai_flag_reset = 0);


//+===========================================================================
//+ Function : scsint_SetUserModule
//+
//+ Purpose  : This function sets the User module level for Scadasoft.
//+
//+===========================================================================
SCSINT_TRACES_API void scsint_SetModule(AtsInt32 ai_ModuleMask);

//+===========================================================================
//+ Function : scsint_AtsTraces
//+
//+ Purpose  : This function prints traces.
//+
//+===========================================================================
/* Not used (and Klocwork issue level 4 MISRA.FUNC.VARARG)
SCSINT_TRACES_API void scsint_AtsTraces(short        ai_level,
					short        ai_line,
					const char * ai_filename,
					const char * ai_format,
					...);
*/

//+===========================================================================
//+ Function : scsint_AtsTraces
//+
//+ Purpose  : This function prints traces.
//+
//+===========================================================================
/*
SCSINT_TRACES_API void scsint_AtsTracesVa(short        ai_level,
					  short        ai_line,
					  const char * ai_filename,
					  const char * ai_format,
					  va_list args);
*/
SCSINT_TRACES_API void scsint_SetLineNumPrinting(AtsInt32 ai_print);

}


#endif // __scsint_Traces
