// ----------------------------------------------------------------------------
// COMPANY  : THALES
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : fasclient
// File     : SCSSigHILCServerAcc.cpp
// Date     : 2017/03/15
// ----------------------------------------------------------------------------
#include <scsthread.h>
#include <SCSSigHILCServerAcc.h>
// ----------------------------------------------------------------------------
class SCSSigHILCServerAcc::SCSSigHILCServerAccImpl {
public:
  SCSSigHILCServerAccImpl() : m_srv(NULL), m_srvLock("SCSSigHILCServerLock") {}
  ~SCSSigHILCServerAccImpl() {
    if (m_srv != NULL) {
      delete m_srv;
      m_srv = NULL;
    }
    m_srvLock.unlock();
  }
  SigHILCServerAcc* m_srv;
  ScsLock m_srvLock;
};
// ----------------------------------------------------------------------------
SCSSigHILCServerAcc::SCSSigHILCServerAcc(const char* envname, const char* servername)
:SCSServer(envname, servername)
,m_impl(new SCSSigHILCServerAccImpl())
{
}
// ----------------------------------------------------------------------------
SCSSigHILCServerAcc::~SCSSigHILCServerAcc()
{
  disconnect();
  if (m_impl != NULL) {
    m_impl->m_srvLock.lock();
    delete m_impl;
    m_impl = NULL;
  }
}
// ----------------------------------------------------------------------------
bool
SCSSigHILCServerAcc::connect(bool direct)
{
  SCSAPITrace(SCS_LEVEL(1), "SCSSigHILCServerAcc::connect %s in env %s", getServerName(), getEnvironmentName());
  ScsScopedLock guard(&m_impl->m_srvLock);
  if (m_impl->m_srv == NULL) {
    SCSServer::State s = subscribeState();
    changeState();
  } else {
    SCSAPITrace(SCS_LEVEL(2), "SCSSigHILCServerAcc::connect already connected to %s in env %s", getServerName(), getEnvironmentName());
  }
  
  ScsStatus s = ScsError;
  if (m_impl->m_srv != NULL) {
    s = m_impl->m_srv->getStatus();
  }
  
  if (s == ScsValid) {
    SCSAPITrace(SCS_LEVEL(2), "SCSSigHILCServerAcc::connect succeeded to %s in env %s", getServerName(), getEnvironmentName());
    return true;  
  }
  SCSAPITrace(SCS_LEVEL(0), "SCSSigHILCServerAcc::connect failed to %s in env %s", getServerName(), getEnvironmentName());
  return false;
}
// ----------------------------------------------------------------------------
bool
SCSSigHILCServerAcc::disconnect()
{
  SCSAPITrace(SCS_LEVEL(1), "SCSSigHILCServerAcc::disconnect ");
  ScsScopedLock guard(&m_impl->m_srvLock);
  if (m_impl->m_srv != NULL) {
    unsubscribeState();
    delete m_impl->m_srv;
    m_impl->m_srv = NULL;
  }
  return true;
}
// ----------------------------------------------------------------------------
void
SCSSigHILCServerAcc::changeState()
{
  ScsScopedLock guard(&m_impl->m_srvLock);
  if (getState() == SCSServer::Down) {
    SCSAPITrace(SCS_LEVEL(2), "SCSSigHILCServerAcc::changeState %s@%s is Down", getServerName(), getEnvironmentName());
    if (m_impl->m_srv != NULL) {
      delete m_impl->m_srv;
      m_impl->m_srv = NULL;
    }
  } else if (getState() == SCSServer::Up) {
    SCSAPITrace(SCS_LEVEL(2), "SCSSigHILCServerAcc::changeState %s@%s is Up", getServerName(), getEnvironmentName());
    if (m_impl->m_srv == NULL) {
      m_impl->m_srv = new SigHILCServerAcc(getEnvironmentName());
    }
  }
}
// ----------------------------------------------------------------------------
int 
SCSSigHILCServerAcc::HILCPreparationRequest (
        const char* i_sOperatorName,
        const char* i_sWorkStationName,
        unsigned int  i_sCmdType,
        int  i_sCmdValue,
		    unsigned int i_sCmdValueDiv,
        const char* i_sEquipmentAlias,
        const char* i_sEquipmentType,
        const char* i_sCommandName)
{
  SCSAPITrace(SCS_LEVEL(1), "SCSSigHILCServerAcc::HILCPreparationRequest ");
  ScsScopedLock guard(&m_impl->m_srvLock);
  if (m_impl->m_srv == NULL) {
    SCSAPITrace(SCS_LEVEL(0), "SCSSigHILCServerAcc::HILCPreparationRequest m_impl->m_srv is null");
    return 1;
  }
  ScsStatus s = m_impl->m_srv->HILCPreparationRequest(string(i_sOperatorName),
                      string(i_sWorkStationName),
                      i_sCmdType,
                      i_sCmdValue,
                      i_sCmdValueDiv,
                      string(i_sEquipmentAlias),
                      string(i_sEquipmentType),
                      string(i_sCommandName));
  if (s != ScsValid)
    return s.getMinor();

  return 0;
}
// ----------------------------------------------------------------------------
int 
SCSSigHILCServerAcc::HILCConfirmRequest (
        const char* i_sOperatorName,
        const char* i_sWorkStationName,
        unsigned int  i_sCmdType,
        int  i_sCmdValue,
		    unsigned int i_sCmdValueDiv,
        const char* i_sEquipmentAlias,
        const char* i_sEquipmentType,
        const char* i_sCommandName)
{
  SCSAPITrace(SCS_LEVEL(1), "SCSSigHILCServerAcc::HILCConfirmRequest ");
  ScsScopedLock guard(&m_impl->m_srvLock);
  if (m_impl->m_srv == NULL) {
    SCSAPITrace(SCS_LEVEL(0), "SCSSigHILCServerAcc::HILCConfirmRequest m_impl->m_srv is null");
    return 1;
  }  
  ScsStatus s = m_impl->m_srv->HILCConfirmRequest(i_sOperatorName,
                      i_sWorkStationName,
                      i_sCmdType,
                      i_sCmdValue,
                      i_sCmdValueDiv,
                      i_sEquipmentAlias,
                      i_sEquipmentType,
                      i_sCommandName);
  if (s != ScsValid)
    return s.getMinor();

  return 0;
}
// ----------------------------------------------------------------------------
