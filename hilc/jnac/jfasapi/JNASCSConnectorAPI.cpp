// ----------------------------------------------------------------------------
// COMPANY  : TTSHK
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright (C) THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : jscs
// File     : JNASCSConnectorAPI.cpp
// Date     : 2017/03/13
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------

#include <JNASCSConnectorAPI.h>
#include <JNASCSConnector.h>
#include <JNASCSServerStateEventListener.h>
// -------------------------------------------------------------------------

int JNA_SCSInitialize(const char *physicalEnv, const char *serverName, int statloop, ASCSetStateFunc cb)
{
  return JNASCSConnector::Instance()->initialize(physicalEnv, serverName, statloop, cb);
}

int JNA_SCSInit(const char *physicalEnv, const char *serverName, int statloop)
{
  return JNASCSConnector::Instance()->initialize(physicalEnv, serverName, statloop);
}

const char* JNA_SubscribeToServerState(const char* remEnvName, const char* remSrvName, ServerStateChangeFunc cb)
{
  JNASCSServerStateEventListener* l = new JNASCSServerStateEventListener(cb);
  return SCSManager::Instance()->addStateListener(remEnvName, remSrvName, l);
}

int JNA_SCSUpdateLoop()
{
  bool run = SCSManager::Instance()->updateEventLoop();
  if (run) {
    return 1;
  }
  return 0;
}

int JNA_SCSStopLoop()
{
  SCSManager::Instance()->stopLoop();
  return 1;
}

void JNA_SetTrace(int moduleMask, int levelMask)
{
  Scadasoft::SetTrace(moduleMask, levelMask);
}

int JNA_GetTraceForModule(int module)
{
  int i;
  int levelMask = 0;
  // module >= 32 are user module
  for(i = 0; i < 32; ++i) {
    if (Scadasoft::IsModuleEnable(module, i)) {
      levelMask |= 1<<i;
    }
  }
  return levelMask;
}

int JNA_SCSExit()
{
  return JNASCSConnector::Instance()->exit();
}
void JNA_Exit(int code)
{
  _exit(code);
}

void JNA_FreeCharArray(char** tab, int count)
{
    if (tab != NULL) {
        int i;
        for(i = 0; i < count; ++i) {
            if (tab[i] != NULL) {
                free(tab[i]);
            }
        }
        free(tab);
    }
}
void JNA_FreeMem(void* m)
{
    if (m != NULL) {
        free(m);
    }
}

int JNA_ConnectToHILCServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb)
{
  return JNASCSConnector::Instance()->connectHILCServer(remEnvName, remSrvName, cb);
}
  
int JNA_ConnectToServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb)
{
  return JNASCSConnector::Instance()->connectServer(remEnvName, remSrvName, cb);
}

int JNA_Disconnect(int srvHandle)
{
  return JNASCSConnector::Instance()->disconnect(srvHandle);
}
