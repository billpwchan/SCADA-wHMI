/**************************************************************/
/* Company         : TCS                              */
/* Filemane        : SigHILCMain.h                          */
/*------------------------------------------------------------*/
/* Purpose         : SIGHILC MAIN                         */
/*                                                            */
/**************************************************************/
/* Creation        :                                          */
/* Author          :                                          */
/*------------------------------------------------------------*/
/* Modification    :                                          */
/* Author          :                                          */
/* Action          :                                          */
/**************************************************************/
// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

#ifndef __SIGHILCMAIN_H__
#define __SIGHILCMAIN_H__

#include "asc.h"
#include "scs.h"
#include "dbm.h"

class SigHILCServer_i;

//------------------------------------------------------------
// Global variables
//------------------------------------------------------------

/// DbmServer interface
//DbmServer * _PtrDbmServer;

/// DbmPoller interface
//DbmPoller * _PtrDbmPoller;

/// Name of SigHILC server
const char SIGHILC_K_SERVER_NAME[] = "SigHILCServer";

InitHILCServer* _pU_InitHILCServer;  // pointers of instances
SigHILCServer_i* _pU_SigHILCServer_i; // server

//------------------------------------------------------------
/// SigHILCNoMoreMemory : Display error and exit program when      
///                      operator "new" cannot allocate enough    
///                      memory                                   
//------------------------------------------------------------
void SigHILCNoMoreMemory(void);

//------------------------------------------------------------
/// SigHILCExit : Free memory allocated in the initialisation part
//------------------------------------------------------------
void SigHILCExit(void);

//------------------------------------------------------------
/// parses arguments
//------------------------------------------------------------
ScsStatus GetArguments( int argc, char *  argv[], char ** vs_serverName, char ** vs_physicalEnv, unsigned long  & o_vuh_levelMask);

#endif // __SIGHILCMAIN_H__
