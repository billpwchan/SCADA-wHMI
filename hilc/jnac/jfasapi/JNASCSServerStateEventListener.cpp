// ----------------------------------------------------------------------------
// COMPANY  : THALES-IS
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : JNASCSServerStateEventListener.cpp
// Date     : 2016/05/09
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------

#include <scsclient/SCSServerStateEvent.h>

// ----------------------------------------------------------------------------
#include <JNASCSConnector.h>
#include <JNASCSServerStateEventListener.h>
// -------------------------------------------------------------------------

// ----------------------------------------------------------------------------
JNASCSServerStateEventListener::JNASCSServerStateEventListener(ServerStateChangeFunc cb)
{
  m_callback = cb;
}
// ----------------------------------------------------------------------------
JNASCSServerStateEventListener::~JNASCSServerStateEventListener()
{
}
// ----------------------------------------------------------------------------
void JNASCSServerStateEventListener::handleStateEvent(SCSServerStateEvent* event)
{
  SCSAPITrace(SCS_LEVEL(1), "JNASCSServerStateEventListener_handleStateEvent -IN-");
  
  try {
    
    (*m_callback)(event->getEnvironmentName(), event->getServerName(), event->getState());

  } catch (...) {
    SCSAPITrace(SCS_LEVEL(0), "JNASCSServerStateEventListener_handleStateEvent error calling callback");
  }
  
  SCSAPITrace(SCS_LEVEL(1), "JNASCSServerStateEventListener_handleStateEvent -OUT-");
}
