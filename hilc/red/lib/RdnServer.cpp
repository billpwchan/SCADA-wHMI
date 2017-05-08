//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : rdn
//|
//| Purpose           : 
//|
//| Filename          : RdnServer.cpp
//|
//| Creation Date     : 14/03/2002
//|
//| Author            : MENCHI Jean-Christophe
//|
//| History           : date, name, action
//|
//+-----------------------------------------------------------------------

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// SCADAsoft Includes
#include <asc.h>

// Local Includes
#include "SnapshotHelper.h"
#include "RdnServer.h"

//==============================================================================+
// RdnServer CLASS IMPLEMENTATION
//==============================================================================+
//------------------------------------------------------------------------------+
// RdnServer Constructor
//------------------------------------------------------------------------------+
RdnServer::RdnServer() : m_ascServer(NULL)
{
  SnapshotHelper::instance()->setRdnServer(this);
}

//------------------------------------------------------------------------------+
// RdnServer Destructor
//------------------------------------------------------------------------------+
RdnServer::~RdnServer()
{
  if (m_ascServer != NULL)
    delete m_ascServer;
}

//------------------------------------------------------------------------------+
// RdnServer goesUP
//------------------------------------------------------------------------------+
void RdnServer::goesUP(AscStateMgr* stmgr)
{
    if (stmgr != NULL) {
      m_ascServer = new AscServer(stmgr);
    } else {
      m_ascServer = new AscServer(new RdnStateMgr(this));
    }
    SnapshotHelper::instance()->setAscServer(m_ascServer);
}

//------------------------------------------------------------------------------+
// RdnServer changeState
//------------------------------------------------------------------------------+
void RdnServer::changeState(const char* state)
{
    if (m_ascServer && state)
        m_ascServer->setState(state);
}
//------------------------------------------------------------------------------+
// RdnServer isStandby
//------------------------------------------------------------------------------+
bool RdnServer::isStandby() const
{
  return SnapshotHelper::instance()->isStandby();
}
//------------------------------------------------------------------------------+
// RdnServer isEnvRedundant
//------------------------------------------------------------------------------+
bool RdnServer::isEnvRedundant() const
{
  return SnapshotHelper::instance()->isEnvRedundant();
}

void RdnServer::changeToOnline()
{
    
}

void RdnServer::down()
{
    
}

//==============================================================================+
// RdnStateMgr CLASS IMPLEMENTATION
//==============================================================================+
//------------------------------------------------------------------------------+
// RdnStateMgr Constructor
//------------------------------------------------------------------------------+
RdnStateMgr::RdnStateMgr(RdnServer* rdn) 
  : m_rdnserver(rdn) 
{
  unsigned short stateSize = strlen(SCS_UP) + 1;
  m_vsState = new char[stateSize];
  strcpy_s(m_vsState, stateSize, SCS_UP);
}
//------------------------------------------------------------------------------+
// RdnStateMgr setState
//------------------------------------------------------------------------------+
void RdnStateMgr::setState(char const * i_vsState) {
  // Copy i_vsState
  if (m_vsState != NULL) {
    delete [] m_vsState;
    m_vsState = NULL;
  }

  if (i_vsState != NULL) {
	unsigned short stateSize = strlen(i_vsState) + 1;
    m_vsState = new char[stateSize];
    strcpy_s(m_vsState, stateSize, i_vsState);
    
    // if server is becoming SCS_DOWN
    if (strcmp(i_vsState, SCS_DOWN) == 0) {
      m_rdnserver->down();
      Scadasoft::Exit();
    }
  }
}
