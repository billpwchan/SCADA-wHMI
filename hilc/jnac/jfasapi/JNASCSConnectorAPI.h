// ----------------------------------------------------------------------------
// COMPANY  : TTSHK
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright (C) THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : JSCS
// File     : JNASCSConnectorAPI.h
// Date     : 2017/03/13
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#ifndef __JNASCSConnectorAPI__
#define __JNASCSConnectorAPI__
// ----------------------------------------------------------------------------
#include <JNASCSDef.h>

// ----------------------------------------------------------------------------
/// \file JNASCSConnectorAPI.h
/// \brief JNA API for initialization and connection to Scadasoft server and its services.
  
extern "C" {

  typedef void (*ASCSetStateFunc)(const char*);
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_SCSInitialize(const char* physicalEnv, const char* serverName)
  /// \par Description:
  /// JNA API for initialization of a new SCADAsoft server.
  /// \param[in] physicalEnv The physical environment name (-e option).
  /// \param[in] serverName The server name (-n option).
  /// \return 0 if OK, -1 in case of error. 
  JNA_SCS_API int JNA_SCSInitialize(const char *physicalEnv, const char *serverName, int statloop, ASCSetStateFunc cb);
  
  JNA_SCS_API int JNA_SCSExit();
  JNA_SCS_API void JNA_Exit(int code);

  JNA_SCS_API void JNA_FreeCharArray(char** tab, int count);
  JNA_SCS_API void JNA_FreeMem(void* m);

  JNA_SCS_API int JNA_SCSInit(const char *physicalEnv, const char *serverName, int statloop);
 
  JNA_SCS_API int JNA_SCSUpdateLoop();
  JNA_SCS_API int JNA_SCSStopLoop();
  JNA_SCS_API void JNA_SetTrace(int moduleMask, int levelMask);
  JNA_SCS_API int JNA_GetTraceForModule(int module);
  
  typedef void (*ServerStateChangeFunc)(const char* envname, const char* srvname, const char* newState);
  JNA_SCS_API const char* JNA_SubscribeToServerState(const char* remEnvName, const char* remSrvName, ServerStateChangeFunc cb);
  
  // State change return new state
  /// The service is not initialized.
  #define SERVER_STATE_NotInitialized 0
  /// The service is connected.
  #define SERVER_STATE_Up 1
  /// The service is disconnected.
  #define SERVER_STATE_Down 2

  typedef void (*StateChangeFunc)(int);
  
 
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_ConnectToHILCServer(const char* remEnvName, const char* remSrvName)
  /// \par Description:
  /// JNA API for establishment of a connection to a HILC Server.
  /// \param[in] remEnvName The HILC environment name.
  /// \param[in] remSrvName The HILC service name.
  /// \return handle to HILCServer or -1 in case of error. 
  JNA_SCS_API int JNA_ConnectToHILCServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb);
  
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_ConnectToServer(const char* remEnvName, const char* remSrvName)
  /// \par Description:
  /// JNA API for establishment of a connection to any Server to monitor its state.
  /// \param[in] remEnvName The DbmServer environment name.
  /// \param[in] remSrvName The DbmServer service name.
  /// \return handle to Server or -1 in case of error. 
  JNA_SCS_API int JNA_ConnectToServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb);
  
  // ----------------------------------------------------------------------------
  /// \fn JNA_SCS_DECL int JNA_Connector_Disconnect(int srvHandle)
  /// \par Description:
  /// JNA API for removing a connection to a Scadasoft service.
  /// \param[in] srvHandle The Scadasoft server Id.
  /// \return The operation result. 
  JNA_SCS_API int JNA_Disconnect(int srvHandle);
  // ----------------------------------------------------------------------------
}

#endif /* ! __JNASCSConnectorAPI__ */
