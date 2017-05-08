/**************************************************************/
/* Company         : TCS                                */
/* Filemane        : InitHILCServer.h                         */
/*------------------------------------------------------------*/
/* Purpose         : class which containes SIG HILC PCC server */
/*                   instances pointers                       */
/*                                                            */
/**************************************************************/
/* Creation        : 30/04/2015	                              */
/* Author          :                                      */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

#ifndef __INITIHILCSERVER_H__
#define __INITIHILCSERVER_H__

class CtlCmdServer;
class DbmServer;
class DbmPoller;
class AscServer;
class AscManager;
class ScsAlmExtern;
class HILC_ConfManager;
class HILCServer_i;
class HILCClientIdl;
class SessionManager;
class HILC_UpdateData;


const std::string ALARMFILE_NAME = "/dat/HilcAlarmExt.cnf";		//External Alarm File
const std::string HILCFILE_NAME = "/dat/HilcConfFile.xml";		// HILC Conf File

#define HILC_K_CODE_LEV_DB_CONFIG 64

///Class InitHILCServer
class InitHILCServer
{

 public:

  //------------------------------------------------------------
  /// Constructor : do nothing
  //------------------------------------------------------------
  InitHILCServer();
  //------------------------------------------------------------
  /// Destructor : Free memory allocated
  //------------------------------------------------------------
  virtual ~InitHILCServer();
  //------------------------------------------------------------
  /// initializes all
  //------------------------------------------------------------
  ScsStatus Init( std::string   sServerName  /** server name */,
		  std::string   sPhysicalEnv         /** physical environment name */,
		  unsigned long ulModuleMask         /** module mask */,
		  unsigned long ulLevelMask          /** trace mask */ );
  //------------------------------------------------------------
  /// setUP the process
  //------------------------------------------------------------
  ScsStatus SetUP();

  //------------------------------------------------------------
  /// pointers are allocated
  //------------------------------------------------------------
  const bool AreAllocated();
  //------------------------------------------------------------
  /// pointers
  //------------------------------------------------------------
  AscManager*     m_pAscManager;               /// manager
  DbmServer*      m_pDbmServer;                /// DbmServer interface
  DbmPoller*      m_pDbmPoller;                /// DbmPoller interface
  AscServer*      m_pAscServer;                /// AscServer interface
  CtlCmdServer*   m_pCtlCmdServer;             /// CtlCmd interface (with callback)
  CtlCmdServer*   m_pCtlCmdServerWithoutCB;    /// CtlCmd interface (without callback)
  ScsAlmExtern* m_AlmExtern;
  //HILCServer_i * m_pHILCServer_i;
  
  SessionManager*  m_SessionManager;
  HILC_UpdateData * m_updateData;
  HILC_ConfManager * m_confManager;
  
  //void HILCClientCallback(std::string origin, std::string message, ScsStatus status, HILCClientIdl * pClient);

 private:

  //------------------------------------------------------------
  // server name & physical env & trace mask
  //------------------------------------------------------------
  std::string    m_sServerName;
  std::string    m_sPhysicalEnv;
  unsigned long  m_ulModuleMask;
  unsigned long  m_ulLevelMask;
  //------------------------------------------------------------
  // to keep control
  //------------------------------------------------------------
  InitHILCServer(const InitHILCServer&);
  InitHILCServer& operator=(const InitHILCServer&);

  //------------------------------------------------------------
  /// free memory
  //------------------------------------------------------------
  void FreeMemory();
  
  // -----------------------------------------------
  /// Command return callback called by CTLCMD server
  // -----------------------------------------------
  static void commandReturnCallback
  (
	const char * i_vs_environment,
	const char * i_vs_subCommandName,
	ScsStatus i_vU_returnStatus,
	long i_vi_nbInfo,
	long * i_pi_valStatus,
	char ** i_vs_textStatus,
	void * i_pv_userArg
  );
	
  //------------------------------------------------------------
  /// initializes SCADASOFT
  //------------------------------------------------------------
  bool m_bSetORBTimeout;   // true if ORB time out is set
  ScsStatus InitScadasoft();
  ScsStatus EndInitScadasoft();
  //------------------------------------------------------------
  /// initialize HILC Server
  //------------------------------------------------------------
  ScsStatus Init();
  ScsStatus InitHILCAutomaton();


};

#endif //__INITIHILCSERVER_H__
