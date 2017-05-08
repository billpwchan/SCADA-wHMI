// ----------------------------------------------------------------------------
// COMPANY  : THALES-IS
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : JNASCSDef.h
// Date     : 2011/12/05
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
/// \file JNASCSDef.h
/// \brief Precompilations defines.
// ----------------------------------------------------------------------------
#ifndef __JNASCSDef__
#define __JNASCSDef__
// ----------------------------------------------------------------------------

#if defined(WIN32)
  #ifdef JNA_SCS_EXPORTS
    #define JNA_SCS_API __declspec(dllexport)
  #else
    #define JNA_SCS_API __declspec(dllimport)
  #endif
#else
  #define JNA_SCS_API
#endif
// ----------------------------------------------------------------------------
#if defined(WIN32)
    #ifndef WIN32_LEAN_AND_MEAN
    #define WIN32_LEAN_AND_MEAN
    #endif
    #include <windows.h>
#endif

struct SCSTimeField {
  long long int tv_sec;
  long long int tv_usec;
};
  
// ----------------------------------------------------------------------------
#endif /* ! __JNASCSDef__ */
