#include <stdarg.h>
#include <stdio.h>
#include "Ats_types.h"
//BHE On arrete ici de tirer des liens #include "tmc_cr.h"
//BHE On arrete ici de tirer des liens #include "formattedTime.h"

#ifndef _STUB_SCS_INTERFACE

#include "scs.h"

#else // !_STUB_SCS_INTERFACE

#include <iostream>

static AtsInt32 Ats_Trace_Module = 0;  // Level of module set by scsint_SetUserTrace
static AtsInt32 Ats_Trace_Level  = 0;  // Level of traces set by scsint_SetUserTrace
static AtsInt32 Ats_Module_Used  = 0;  // Level of module set by scsint_SetModule
static AtsInt32 Ats_Print_Line_Num = AtsTRUE; // Flag to enable/disable line number printing (tstxxx)

#endif // _STUB_SCS_INTERFACE

// Please keep this includes after "scs.h" because of macro and symbols definitions
#include "scsint_Traces.hpp"
//BHE On arrete ici de tirer des liens #include "scsint_ErrorLog.hpp"

const int CR_K_ERROR = 3147; // as TMC_CR_K_ERROR on tmc_cr.h which we don't use (pure ATS)
const int CR_K_NORMAL = 0; // as TMC_CR_K_NORMAL on tmc_cr.h which we don't use (pure ATS)

//+===========================================================================
//+ Function : scsint_SetUserTrace
//+
//+ Purpose  : This function sets the User Trace and module level for Scadasoft.
//+
//+===========================================================================
AtsUInt32 scsint_SetUserTrace(AtsInt32 ai_ModuleMask, AtsInt32 ai_LevelMask, AtsInt32 ai_flag_reset)
{
//=========================
#ifndef _STUB_SCS_INTERFACE
//=========================
  ScsStatus scsStatus;
  
  scsStatus = Scadasoft::SetUserTrace(ai_ModuleMask, ai_LevelMask);
  if (!scsStatus.isValid())
    {
      /* BHE on utilisera pas ATSerror_log : ca tire d'autres trucs tmc, restons en là
      ATSerror_log(ATS_LOG_LINE_FILE, TMC_CR_K_BADCOM, ATS_LOG_K_SEVFATAL,
		   "scsint_SetUserTrace", "ScadaSoft SetUserTrace FAILED !! (error = %d)", 
		   scsStatus.getMinor());
      */    	
      std::cerr << "scsint_SetUserTrace: User Trace not set ! ScadaSoft SetUserTrace FAILED !! error = "
                << scsStatus.getMinor();
      return CR_K_ERROR;
    }
  else 
    {
      std::cout << "scsint_SetUserTrace: Trace set for modules :";
    }

  
//===
#else 
//===

  if (ai_flag_reset)
    {
      Ats_Trace_Module = ai_ModuleMask;
      Ats_Trace_Level  = ai_LevelMask;
    }
  else {
    Ats_Trace_Module |= ai_ModuleMask;
    Ats_Trace_Level  |= ai_LevelMask;
  }
  std::cout << "scsint_SetUserTrace: Stub Trace set for modules :";

//====
#endif
//====

  int i = 0;
  int j = 0;
  for(i=0; i<31; i++)
    {
      if ((ai_ModuleMask & (1<<i))!=0)
	{ std::cout <<  " " << i; }
    }
  std::cout << " at levels:";
  for(j=0; j<31; j++)
    {
      if ((ai_LevelMask & (1<<j))!=0)
	{ std::cout << " " << j; }
    }
  std::cout << std::endl;
  
  return CR_K_NORMAL;
} // end of scsint_SetUserTrace

//+===========================================================================
//+ Function : scsint_SetUserModule
//+
//+ Purpose  : This function sets the User module level for Scadasoft.
//+
//+===========================================================================
void scsint_SetModule(AtsInt32 ai_ModuleMask)
 {
   
//=========================
#ifndef _STUB_SCS_INTERFACE
//=========================
   // Set our module.  (+32 for user levels)
   Scadasoft::SetModule(ai_ModuleMask+32);
  
//===
#else 
 //===
   Ats_Module_Used = ai_ModuleMask;
 //====
#endif
 //====
 
 }  // end of scsint_SetUserModule


//+===========================================================================
//+ Function : scsint_AtsTraces
//+
//+ Purpose  : This function prints traces.
//+
//+ Notes : This function is a copy of the internal implementation of the 
//+ Scadasoft function ScsUserTraceFunction(int i).  The reason is to be able
//+ to stub the trace function without recompiling the calling functions.
//+
//+===========================================================================
/* Not used (and Klocwork issue level 4 MISRA.FUNC.VARARG)
void scsint_AtsTraces(short        ai_level,
                      short        ai_line,  
		      const char * ai_filename,
                      const char * ai_format,
                      ...)
{
  va_list args;
  va_start( args, ai_format );
  scsint_AtsTracesVa( ai_level, ai_line, ai_filename, ai_format, args );
  va_end( args );
}  // end of scsint_AtsTraces
*/

//+===========================================================================
//+ Function : scsint_AtsTraces
//+
//+ Purpose  : This function prints traces.
//+
//+ Notes : This function is a copy of the internal implementation of the 
//+ Scadasoft function ScsUserTraceFunction(int i).  The reason is to be able
//+ to stub the trace function without recompiling the calling functions.
//+
//+===========================================================================
/*
void scsint_AtsTracesVa(short        ai_level,
			short        ai_line,  
			const char * ai_filename,
			const char * ai_format,
			va_list      args)
{

//=========================
#ifndef _STUB_SCS_INTERFACE
//=========================

  // Copied directly from ScsPrintTrace from scstrace.h
  if (Scadasoft::IsEnable(ai_level))
    {
      AntMessageBody  message;
      message[0] = '\0';
      if (vsprintf(message, ai_format, args) > AntMessageMaxSize)
        {
	  return;
        }
      AntPrintTrace(ai_level, ai_line, ai_filename, message);
    }

//===
#else 
//===
  int module = Ats_Trace_Module & (1<<Ats_Module_Used);
  int level  = Ats_Trace_Level  & (1<<ai_level);

  if ((module) && (level)) {
     char  message[1000];
     message[0] = '\0';
     if (vsprintf(message, ai_format, args) > 1000)
       {
       return;
       }
     std::string basefilename(ai_filename);
#ifdef WIN32
     // transform backslashes to slashes
     std::string::size_type pos = basefilename.find('\\');
     while ( std::string::npos != pos ) {
       basefilename.at(pos) = '/';
       pos = basefilename.find('\\',pos);
     }
#endif
     
     std::string::size_type startpos = basefilename.find("ats_source");
     if( std::string::npos != startpos ) {
        basefilename.erase(0,startpos);
     }
     
     std::cout << "STUB Trace from (" << basefilename.c_str() ;
     if(Ats_Print_Line_Num) {
       std::cout << ":" << ai_line;
     }
     std::cout << ") :- " << std::endl;
     std::cout << "    " << message << std::endl;
  }

  Ats_Module_Used = 0;

//====
#endif
//====

  return;
  
}  // end of scsint_AtsTraces
*/

//+===========================================================================
//+ Function : scsint_SetLineNumPrinting
//+
//+ Purpose  : This function sets the flag allowing not to print line numbers
//+
//+===========================================================================
void scsint_SetLineNumPrinting(AtsInt32 ai_print)
{
//=========================
#ifdef _STUB_SCS_INTERFACE
//=========================
  Ats_Print_Line_Num = ai_print;
//===
#endif 
//===
}



