// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : JNASCSConnector.cpp
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#include <string>
#include <vector>
// ----------------------------------------------------------------------------
#include <scsthread.h>
#include <scadaorb.h>
#include <asc.h>
#include <manager.h>
#include <dbm.h>

// ----------------------------------------------------------------------------

#include <scsclient/SCSServer.h>
#include <scsclient/SCSGenericServer.h>
#include <scsclient/SCSEventQueue.h>
#include <SCSSigHILCServerAcc.h>
#include <scsclient/SCSFactory.h>
#include <SCSXFactory.h>

// ----------------------------------------------------------------------------
#include <JNASCSConnector.h>

// ----------------------------------------------------------------------------
class SCSServerInfo : public SCSServer::StateListener {
public:
  
  SCSServerInfo(SCSServer* s, StateChangeFunc cb) : m_server(s)
    {
      if (cb != NULL) {
        m_callbackList.push_back(cb);
      }
    }
  
  virtual void changeState(SCSServer* srv) {
    
    std::vector<StateChangeFunc>::iterator it = m_callbackList.begin();
    
    while(it != m_callbackList.end()) {
      StateChangeFunc cb = *it;
      if (srv->getState() == SCSServer::Up) {
        (*cb)(SERVER_STATE_Up);
      } else {
        (*cb)(SERVER_STATE_Down);
      }
      it++;
    }
  }
  
  void addCallback(StateChangeFunc cb) {
    if (cb != NULL) {
      m_callbackList.push_back(cb);
      // call with initial state
      if (m_server->getState() == SCSServer::Up) {
        (*cb)(SERVER_STATE_Up);
      } else {
        (*cb)(SERVER_STATE_Down);
      }
    }
  }
  
  std::vector<StateChangeFunc> m_callbackList;
  SCSServer* m_server;
};

// ----------------------------------------------------------------------------
JNASCSConnector* JNASCSConnector::s_instance = NULL;
ScsLock JNASCSConnector::s_gate;

// ----------------------------------------------------------------------------
JNASCSConnector* JNASCSConnector::Instance()
{
  if (s_instance == NULL) {
    ScsScopedLock guard(&s_gate);
    s_instance = new JNASCSConnector();
  }
  return s_instance;
}

// ----------------------------------------------------------------------------
JNASCSConnector::JNASCSConnector()
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::JNASCSConnector");
  
  m_nextId = 0;
  m_statLoopThreadID = 0;
  m_setStateCB = NULL;
}
// ----------------------------------------------------------------------------
JNASCSConnector::~JNASCSConnector()
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::~JNASCSConnector");
}
// ----------------------------------------------------------------------------
int JNASCSConnector::disconnect(int srvHandle)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::disconnect() %d", srvHandle);
  ScsScopedLock guard(&s_gate);
  
  SCSServer* server = getServer(srvHandle);
  if (server != NULL) {
    server->disconnect();
    std::map<int, SCSServerInfo*>::iterator it1 = m_serverMap.find(srvHandle);
    server->removeStateListener(it1->second);
    delete it1->second;
    m_serverMap.erase(it1);
    std::map<SCSServer*, int>::iterator it2 = m_handleMap.find(server);
    m_handleMap.erase(it2);
    SCSResource::unref(server);
  }
  
  return 0;
}
// ----------------------------------------------------------------------------

// ----------------------------------------------------------------------------
SCSServer* JNASCSConnector::getServer(int srvHandle)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::getServer srvHandle %d ", srvHandle);
  ScsScopedLock guard(&s_gate);
  std::map<int, SCSServerInfo*>::iterator it = m_serverMap.find(srvHandle);
  if (it != m_serverMap.end()) {
    return it->second->m_server;
  }
  
  return NULL;
}

ScsThreadReturnType JNASCSConnector::EventLoop(void* )
{
  SCSManager::Instance()->mainloop();
  return 0;
}

ScsThreadReturnType JNASCSConnector::StatLoop(void * arg)
{
  int* uarg = (int*) arg;
  int delay = *uarg;
  delete uarg;
  while(1) {
    SCSEventQueue::Instance()->printStat();
    Scadasoft::Sleep(delay);
  }
  
  return 0;
}

int JNASCSConnector::initialize(const char *physicalEnv, const char *serverName, int statloop, ASCSetStateFunc cb)
{
  ScsScopedLock guard(&s_gate);
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::initialize %s@%s", serverName, physicalEnv);
  // Set SCSManager to use extended factory
  SCSFactory *factory = (SCSFactory *) new SCSXFactory();
  SCSManager::Instance()->setFactory(factory);
  SCSManager::Instance()->initAsServer(physicalEnv, serverName, this);
  
  if (cb != NULL) {
    m_setStateCB = cb;
  }
  // start thread for event loop
  ScsThread::Create(m_evtLoopThreadID, JNASCSConnector::EventLoop, this, 0, 0);
  
  // stat loop
  if (statloop > 0) {
    Scadasoft::SetTrace(1<<25, 1<<6);
    int* arg = new int;
    *arg = statloop;
    ScsThread::Create(m_statLoopThreadID, JNASCSConnector::StatLoop, arg, 0, 0);
  }
  return 0;
}

int JNASCSConnector::initialize(const char *physicalEnv, const char *serverName, int statloop)
{
  ScsScopedLock guard(&s_gate);
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::initialize %s@%s", serverName, physicalEnv);
  // Set SCSManager to use extended factory
  SCSFactory *factory = (SCSFactory *) new SCSXFactory();
  SCSManager::Instance()->setFactory(factory);
  SCSManager::Instance()->initAsServer(physicalEnv, serverName, this);
  // stat loop
  if (statloop > 0) {
    Scadasoft::SetTrace(1<<25, 1<<6);
    int* arg = new int;
    *arg = statloop;
    ScsThread::Create(m_statLoopThreadID, JNASCSConnector::StatLoop, arg, 0, 0);
  }
  return 0;
}

void JNASCSConnector::changeState(const char* newState)
{
  if (m_setStateCB != NULL) {
    (*m_setStateCB)(newState);
  }
}

int JNASCSConnector::exit()
{
  SCSManager::Instance()->stopLoop();
  
  // kill stat thread
  if (m_statLoopThreadID != 0) {
     ScsThread::Kill(m_statLoopThreadID);
  }
  
  // wait for end of event loop
  void *exitCode = NULL;
  ScsThread::WaitFor(m_evtLoopThreadID, exitCode);
  // stop ORB
  SCSManager::Instance()->exit();
 
  return 0;
}

int JNASCSConnector::connectHILCServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::connectHILCServer() %s@%s", remSrvName, remEnvName);
  ScsScopedLock guard(&s_gate);
  int id = -1;
  SCSServer* server = SCSManager::Instance()->getServer(remEnvName, remSrvName, SCSSigHILCServerAcc::scs_type);
  
  if (server != NULL) {
    // check if object is known
    std::map<SCSServer*, int>::iterator it = m_handleMap.find(server);
    if (it != m_handleMap.end()) {
      id = it->second;
      
      SCSServerInfo* si = NULL;
      std::map<int, SCSServerInfo*>::iterator it = m_serverMap.find(id);
      if (it != m_serverMap.end()) {
        si = it->second;
      }
      
      if (si != NULL) {
        si->addCallback(cb);
      }
      
      SCSAPITrace(SCS_LEVEL(2), "JNASCSConnector::connectHILCServer() %s@%s found with id %d", remSrvName, remEnvName, id);
    } else {
      // new item
      id = m_nextId;
      m_nextId++;
      m_handleMap[server] = id;
      server->connect(true);
      
      SCSServerInfo* si = new SCSServerInfo(server, cb);
      server->addStateListener(si);
      
      // call for initial state
      si->changeState(server);
      m_serverMap[id] = si;
      SCSResource::ref(server);
      SCSAPITrace(SCS_LEVEL(2), "JNASCSConnector::connectHILCServer() add %s@%s with id %d", remSrvName, remEnvName, id);
    }
   
  } else {
    SCSAPITrace(SCS_LEVEL(0), "Cannot connect to HILCServer %s@%s", remSrvName, remEnvName);
  }

  return id;
}

int JNASCSConnector::connectServer(const char* remEnvName, const char* remSrvName, StateChangeFunc cb)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::connectServer() %s@%s", remSrvName, remEnvName);
  ScsScopedLock guard(&s_gate);
  int id = -1;
  SCSServer* server = SCSManager::Instance()->getServer(remEnvName, remSrvName, SCSGenericServer::scs_type);
  
  if (server != NULL) {
    // check if object is known
    std::map<SCSServer*, int>::iterator it = m_handleMap.find(server);
    if (it != m_handleMap.end()) {
      id = it->second;
      
      SCSServerInfo* si = NULL;
      std::map<int, SCSServerInfo*>::iterator it = m_serverMap.find(id);
      if (it != m_serverMap.end()) {
        si = it->second;
      }
      
      if (si != NULL) {
        si->addCallback(cb);
      }
      
      SCSAPITrace(SCS_LEVEL(2), "JNASCSConnector::connectServer() %s@%s found with id %d", remSrvName, remEnvName, id);
    } else {
      // new item
      id = m_nextId;
      m_nextId++;
      m_handleMap[server] = id;
      server->connect(true);
      
      SCSServerInfo* si = new SCSServerInfo(server, cb);
      server->addStateListener(si);
      
      // call for initial state
      si->changeState(server);
      m_serverMap[id] = si;
      SCSResource::ref(server);
      SCSAPITrace(SCS_LEVEL(2), "JNASCSConnector::connectServer() add %s@%s with id %d", remSrvName, remEnvName, id);
    }
   
  } else {
    SCSAPITrace(SCS_LEVEL(0), "Cannot connect to Server %s@%s", remSrvName, remEnvName);
  }

  return id;
}

SCSSigHILCServerAcc* JNASCSConnector::getHILCServer(int srvHandle)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSConnector::getHILCServer srvHandle %d ", srvHandle);
  SCSServer* s = getServer(srvHandle);
  if (s != NULL && s->serverType() == SCSSigHILCServerAcc::scs_type) {
    return (SCSSigHILCServerAcc*) s;
  }
  
  return NULL;
}
