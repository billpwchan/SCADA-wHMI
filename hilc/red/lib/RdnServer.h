//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : rdn
//|
//| Purpose           : 
//|
//| Filename          : RdnServer.h
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

#ifndef RDNSERVER_H
#define RDNSERVER_H

#include <asc.h>

#include "RED_api.h"

class RdnServer;

//-------------------------------------------------------------
// State manager (for server stop : RDNServer::down)
//-------------------------------------------------------------
class RED_API RdnStateMgr : public AscStateMgr {
public:
  RdnStateMgr(RdnServer* rdn);
  
  virtual ~RdnStateMgr()
  { if (m_vsState != NULL) delete [] m_vsState;}
  
  virtual const char * getState()
  { return m_vsState;}
  
  virtual void setState (char const * i_vsState);
  
private:
  char*      m_vsState;
  RdnServer* m_rdnserver;
  
  // instance assign not expect : declare private operator = 
  // This avoid Klocwork Warning CL.FFM.ASSIGN
  RdnStateMgr& operator=(const RdnStateMgr&) { return *this; }
  // instance assign not expect : declare private copy contructor
  // This avoid Klocwork Warning CL.FFM.COPY
  RdnStateMgr(const RdnStateMgr& src) : m_vsState(NULL), m_rdnserver(NULL) { /* do not create copies */ }
};

/**
   RdnServer: base class for redundant server.

   This class handle the interface with the Scadasoft redundancy. Each server
   must subclass RdnServer and redefine at least the preLoad and preForegroundDump
   methods.
   
  */
class RED_API RdnServer {
public:

  /// Constructor
  RdnServer();

  /// Destructor
  virtual ~RdnServer();

  /**
     Call this method when the server is ready to be
     seen UP by the ascmanager.

     This method creates an AscServer, so you should
     not create another one.
     
     WARNING !!! Call this method set the state defined in the AscStateMgr
     if AscStateMgr = NULL; the default state is SCS_UP
     else RdnServer notify ascmanager with the state defined in AscStateMgr (for example TmcSupServer with SCS_UP_WAITING_CEVSUP)
   */
  void goesUP(AscStateMgr* stmgr = NULL);

   /**
     Call this method when the server is ready to notify
     the ascmanager of a new state.

     WARNING !!! This method doesn't create an AscServer.
     Correct sequence is :
     Call goesUP first to create AscServer with a defined state
     Call changeState to modify the server state
   */
  void changeState(const char* state);

  /**
     Acces to the Scadasoft AscServer.

     This object exists only after a call to RdnServer::goesUP
   */
  AscServer* getAscServer() const
  { return m_ascServer; }
  
  /**
     Return true if the physical environment is in standby mode.
   */
  bool isStandby() const;

  /**
     Method called when the physical environment change to ONLINE state.
   */
  virtual void changeToOnline();

  /**
     Method called when the server receive a SCSDOWN message.

     After a call to scskill or scsshutdown
   */
  virtual void down();

  /**
     Return true if the physical environment is part of
     a redundant logical environment.
   */
  bool isEnvRedundant() const;

  //=============================
  // snapshot methods
  //=============================

  /** Method called before the generic part of the snapshot.

      This method should be redefined by the user, to read
      the specific part. It is called before the OlsList
      snapshot
   */
  virtual int preLoad() = 0;
 
  /** Method called after the generic part of the snapshot.
      
      This method is called after the loading of the OlsList.
   */
  virtual int postLoad() { return 0; }

  /** Method called before the generic part of the snapshot.
      @see RdnServer::preLoad
   */
  virtual int preForegroundDump() = 0;

  /** Method called after the generic part of the snapshot.
      @see RdnServer::postLoad
   */
  virtual int postForegroundDump() { return 0; }
  
  /** Method called after the generic part of the snapshot.
      @see RdnServer::postLoad
   */
  virtual int restorePendingCommands() { return 0; }

private:
  AscServer* m_ascServer;
  
  // instance assign not expect : declare private operator = 
  // This avoid Klocwork Warning CL.FFM.ASSIGN
  RdnServer& operator=(const RdnServer&) { return *this; }
  // instance assign not expect : declare private copy contructor
  // This avoid Klocwork Warning CL.FFM.COPY
  RdnServer(const RdnServer& src) : m_ascServer(NULL) { /* do not create copies */ }
};

#endif
