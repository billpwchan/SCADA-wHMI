// ----------------------------------------------------------------------------
// COMPANY  : THALES-IS
// LANGUAGE : C++
// ----------------------------------------------------------------------------
// Copyright © THALES.
// All rights reserved.
// Unauthorized access, use, reproduction or distribution is prohibited.
// ----------------------------------------------------------------------------
// Module   : GWebHMI
// File     : SCSServerStateEventListener.h
// Date     : 2016/05/09
// ----------------------------------------------------------------------------
// HISTORY  :
// ----------------------------------------------------------------------------
#ifndef __JNASCSServerStateEventListener__
#define __JNASCSServerStateEventListener__
// ----------------------------------------------------------------------------
#include <scsclient/SCSServerStateEventListener.h>
// ----------------------------------------------------------------------------
#include <JNASCSDef.h>
#include <JNASCSConnectorAPI.h>

// ----------------------------------------------------------------------------
/// \file JNASCSServerStateEventListener.h
/// \brief JNA SCADAsoft server state events listener.
// ----------------------------------------------------------------------------
class JNA_SCS_API JNASCSServerStateEventListener : public SCSServerStateEventListener
{
public:
  /// \par Description:
  /// Creates a new listener for State events.
  JNASCSServerStateEventListener(ServerStateChangeFunc cb);
  
  virtual ~JNASCSServerStateEventListener();

  /// \par Description:
  /// Handles a Server state event.
  /// \param[in] event The State event.
  virtual void handleStateEvent(SCSServerStateEvent* event);

private:
  ServerStateChangeFunc m_callback;
};
// ----------------------------------------------------------------------------
#endif /* ! __JNASCSServerStateEventListener__ */
