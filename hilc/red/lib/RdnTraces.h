#ifndef __RDNTRACES_H__
#define __RDNTRACES_H__
//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : rdn
//|
//| Purpose           : Redundancy Traces levels
//|
//| Filename          : RdnTraces.h
//|
//| Creation Date     : 12 mar 2002
//|
//| Author            : MENCHI Jean-Christophe
//|
//| History           : date, name, action
//|
//+-----------------------------------------------------------------------

// Comments with // are C++ comments
// Comments with /// or //@ are DOC++ instructions

// Instruction for generation tracability : please, do not remove
#ifndef WIN32
#pragma VERSIONID "$CCfile: -- $ $Revision: -- $ $Date: -- $ $Project: -- $"
#endif

#include "Traces.h"
#include "CSC.h"

/// RDN identification number for trace component
#define RDN_K_CSC_NUM K_NUM_RDN

/// Trace macro for RDN component
#define RdnTrace ScsUserTraceFunction (RDN_K_CSC_NUM)

/**@name Trace level constants */
//@{
/// Trace always displayed 
#define RDN_K_LEVEL_GENSTA     K_TRACE_LEV_GENSTA

/// Trace for call of external services offered by other components
#define RDN_K_LEVEL_EXTCALL    K_TRACE_LEV_EXTCALL

/// Trace for call of RDN services by other components
#define RDN_K_LEVEL_CALLBYEXT  K_TRACE_LEV_CALLBYEXT

/// Trace for call of internal services
#define RDN_K_LEVEL_INTCALL    K_TRACE_LEV_INTCALL

/// Trace for call of basic internal services
#define RDN_K_LEVEL_BASINTCALL K_TRACE_LEV_BASINTCALL

/// Trace for details
#define RDN_K_LEVEL_INTDET     K_TRACE_LEV_INTDET
//@}

/**@name Trace level codes */
//@{
/// General component state
#define RDN_K_CODE_LEV_GENSTA     K_CODE_LEV_GENSTA

/// Call of external services offered by other components
#define RDN_K_CODE_LEV_EXTCALL    K_CODE_LEV_EXTCALL

/// Call of component service by other components
#define RDN_K_CODE_LEV_CALLBYEXT  K_CODE_LEV_CALLBYEXT

/// Call of component internal service
#define RDN_K_CODE_LEV_INTCALL    K_CODE_LEV_INTCALL

/// Call of component basic internal service
#define RDN_K_CODE_LEV_BASINTCALL K_CODE_LEV_BASINTCALL

/// Internal component details
#define RDN_K_CODE_LEV_INTDET     K_CODE_LEV_INTDET
//@}


#endif // __RDNTRACES_H__
