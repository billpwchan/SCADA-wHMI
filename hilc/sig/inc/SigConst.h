#ifndef __SIGCONST_H__
#define __SIGCONST_H__
//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : mcs
//|
//| Purpose           : 
//|
//| Filename          : SigConst.h
//|
//| Creation Date     : 27/04/2000
//|
//| Author            : SUDRES Geraldine	
//|
//| History           : date, name, action
//| 			21/10/1999	Administration ClearCase
//| 		Automatic template insertion
//|
//+-----------------------------------------------------------------------

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions


/**@name Sig Private constants*/
//@{
/// Descriptive text

/// Maximum Number of fixed blocked (eqBLOC and eqBLOCNS)
//const long SIG_K_MAX_NB_FB = 800;
        
/// Max length of an alias (see Sig_t_AliasAddr)
//const long SIG_K_MAX_ADDR_LN = 300;
        
/// Max length of an alias (see structure Sig_t_Location)
//const long SIG_K_ALIAS_LOCATION_SIZE = 19 + 1;

/// Value for Orb timeout during initialization phase
const unsigned short SIG_K_ORB_TIME_OUT_DURING_INIT = 60000;

//@}

#endif // __SIGCONST_H__
