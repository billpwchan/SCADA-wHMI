// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : jscs
// File     : JNASCSConnector.h
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#ifndef __JNASCSConnector__
#define __JNASCSConnector__

#include <map>
// ----------------------------------------------------------------------------
#include <scsthread.h>
// ----------------------------------------------------------------------------
#include <JNASCSDef.h>
#include <JNASCSConnectorAPI.h>

#include <scsclient/SCSManager.h>

// ----------------------------------------------------------------------------
class SCSServer;
class SCSSigHILCServerAcc;

class SCSServerInfo;

// ----------------------------------------------------------------------------
/// \file JNASCSConnector.h
/// \brief Manager of connections to Scadasoft servers and its services.
// ----------------------------------------------------------------------------
class JNASCSConnector : public SCSManager::ASCStateListener 
{
  /// \par Description:
  /// Main class for managment of connections to Scadasoft server.
  /// Proposes means to establish connections or disconnections of Scadasoft services.
  /// It provides an accessor to retrieve the queued events sent by the server.
  /// This class has an unique instance (Singleton).
public:

  static JNASCSConnector* Instance();

  int initialize(const char *physicalEnv, const char *serverName, int statloop, ASCSetStateFunc cb);

  int initialize(const char *physicalEnv, const char *serverName, int statloop);

  int exit();

  virtual void changeState(const char* newState);
  
  int connectHILCServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb);

  int connectServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb);

  int disconnect(int srvHandle);

  SCSServer* getServer(int srvHandle);
  
  SCSSigHILCServerAcc* getHILCServer(int srvHandle);

    
private:
  JNASCSConnector();
  JNASCSConnector(const JNASCSConnector&) {}
    
  ~JNASCSConnector();
    
  static ScsThreadReturnType EventLoop(void *);
  ScsThread::ID m_evtLoopThreadID;
  static ScsThreadReturnType StatLoop(void *);
  ScsThread::ID m_statLoopThreadID;
  
  ASCSetStateFunc m_setStateCB;
  
  // server map
  int m_nextId;
  std::map<int, SCSServerInfo*> m_serverMap;
  std::map<SCSServer*, int> m_handleMap;

//   // DAC subscription map
//   int m_nextDacSubId;
//   std::map<int, JNASCSDacEventListener*> m_dacserverSubscriptionMap;
    
  static JNASCSConnector* s_instance;
  static ScsLock s_gate;
};
// ----------------------------------------------------------------------------
#endif /* ! __JNASCSConnector__ */
