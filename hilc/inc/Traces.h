#ifndef __TRACES_H__
#define __TRACES_H__
//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : General definitions
//|
//| Purpose           : General definitions relative to traces levels for NEL, 
//|			commonly used by various CSCI
//|
//| Filename          : Traces.h
//|
//| Creation Date     : 15/11/2004
//|
//| Author            : Administration ClearCase
//|
//| History           : date, name, action
//+-----------------------------------------------------------------------

#include "scstrace.h"
#include "ant/trace.h"

/**@name Trace Level Definition */
const int k_TraceLevelGenSta                      = 0;
const int k_TraceLevelExtCall                     = 1;
const int k_TraceLevelCallByExt                   = 2;
const int k_TraceLevelIntCall                     = 3;
const int k_TraceLevelBasicCall                   = 4;
const int k_TraceLevelIntDet                      = 5;
const int k_TraceLevelIntFullDet                  = 6;

/**@name Regulation Trace Level Definition */
const int k_TraceLevelRegCycle                    = 7;
const int k_TraceLevelRegEvent                    = 8;
const int k_TraceLevelRegPerfs                    = 9;
const int k_TraceLevelRegTerminal                 = 10;
const int k_TraceLevelRegWay                      = 11;
const int k_TraceLevelRegFlow                     = 12;
const int k_TraceLevelRegEnergy                   = 13;
const int k_TraceLevelRegWaitingDso               = 14;
const int k_TraceLevelRegSpreadline               = 15;
const int k_TraceLevelRegRecuperation             = 16;

/**@name SAL Trace Level Definition */
const int k_TraceLevelSalCycle                    = 7;
const int k_TraceLevelSalConsole                  = 8;
const int k_TraceLevelSalSigDep                   = 9;
const int k_TraceLevelSalArrDepCtrl               = 10;
const int k_TraceLevelSalPmr                      = 11;

/**@name SIV Trace Level Definition */
const int k_TraceLevelSivCycle                    = 7;

/**@name SIG/LOC Trace Level Definition */
const int k_TraceLevelSigLocTrwdDynamicData       = 7;
const int k_TraceLevelSigLocTrwdConfigData        = 8;
const int k_TraceLevelSigLocTrainConfigData       = 9;
const int k_TraceLevelSigLocTcConfigData          = 10;

/**@name Trace level constants */
//@{
    /// General component state : Important traces allways printed
#define K_TRACE_LEV_GENSTA     SCS_LEVEL( k_TraceLevelGenSta )

    /// Call of external service of another component (ex : call of scs service)
#define K_TRACE_LEV_EXTCALL    SCS_LEVEL( k_TraceLevelExtCall )

    /// Call of component service by external component (start and end of CORBA server methods)
#define K_TRACE_LEV_CALLBYEXT  SCS_LEVEL( k_TraceLevelCallByExt )

    /// Call of component internal service (start and end of component main level methods)
#define K_TRACE_LEV_INTCALL    SCS_LEVEL( k_TraceLevelIntCall )

    /// Call of component basic internal service (start and end of component low level methods)
#define K_TRACE_LEV_BASINTCALL SCS_LEVEL( k_TraceLevelBasicCall ) 

    /// Internal component details (not too verbose internal functional information)
#define K_TRACE_LEV_INTDET     SCS_LEVEL( k_TraceLevelIntDet )

    /// Internal component full details (full verbose internal functional information)
#define K_TRACE_LEV_INTFULLDET SCS_LEVEL( k_TraceLevelIntFullDet )

//@}

/**@name Trace level codes */
//@{
    /// General component state
#define K_CODE_LEV_GENSTA     1 // level 0

    /// Call of external service of another component
#define K_CODE_LEV_EXTCALL    2 // level 1

    /// Call of component service by external component
#define K_CODE_LEV_CALLBYEXT  4 // level 2

    /// Call of component internal service
#define K_CODE_LEV_INTCALL    8 // level 3

    /// Call of component basic internal service
#define K_CODE_LEV_BASINTCALL 16 // level 4

    /// Internal component details
#define K_CODE_LEV_INTDET     32 // level 5
//@}


#endif //__TRACES_H__
