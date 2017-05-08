#ifndef __CSC_H__
#define __CSC_H__
//+-----------------------------------------------------------------------
//|Company       : SYSECA
//|CSCI          : pcc
//|
//| Purpose           : Constants for traces and errors handled by ScadaSoft
//|
//| Filename          : CSC.h
//|
//| Creation Date     : 15/11/2004
//|
//| Author            : Administration ClearCase
//|
//| History           : date, name, action
//|
//+-----------------------------------------------------------------------

/**@name CSC identification numbers */
//@{
/// to be used to set ScadaSoft traces
typedef enum {
  K_NUM_ALA = 0,
  K_NUM_ATS,		// 1
  K_NUM_SIG,		// 2
  K_NUM_RDN,		// 3
  K_NUM_CGE,		// 4
  K_NUM_TMC_ENC,	// 5
  K_NUM_TMC_DIA,	// 6
  K_NUM_TMC_REC,	// 7
  K_NUM_TMC_REG,	// 8
  K_NUM_TMC_SAL,	// 9
  K_NUM_TMC_SPV,	// 10
  K_NUM_TMC_SUP,	// 11
  K_NUM_TMC_TRK,	// 12
  K_NUM_CEV,     	// 13
  K_NUM_TMC_SIV,	// 14
  K_NUM_TMC_EQT,	// 15
  K_NUM_DSS,        // 16
  K_NUM_SFO,         // 17
  K_NUM_SAR,		// 18 
  K_NUM_CSA,		// 19		         
  K_NUM_RES,		// 20
  K_NUM_ARC,		// 21
  K_NUM_TMC_EXT,	// 22
  K_NUM_TMC_PEX,	// 23
  K_NUM_TMC_PEX_VISU,	// 24
  K_NUM_IMP,            // 25
  K_NUM_TMC_DRV,  	// 26
  K_NUM_TMC_DRV_SUP,    // 27
  K_NUM_TMC_PEX_TOOLS,  // 28
  K_NUM_VISU_MODS,      // 29
  K_NUM_VISU_SCRS,      // 30
  K_NUM_TMC_OIL_MONITORING //31

  // KLL/LB/JPU 03/06/2005: Supression des modules du NEL inutilisés. A confirmer.
/*   K_NUM_CCT, */
/*   K_NUM_CLK, */
/*   K_NUM_ECS, */
/*   K_NUM_PAS, */
/*   K_NUM_PIS, */
/*   K_NUM_POW, */
/*   K_NUM_RAD, */
/*   K_NUM_SMC, */
/*   K_NUM_TEL, */
/*   K_NUM_TIM, */
/*   K_NUM_BMF, */
/*   K_NUM_SYS, */
/*   K_NUM_MAI, */
/*   K_NUM_ADM, */
/*   K_NUM_DEP, */
/*   K_NUM_HIS, */
/*   K_NUM_DSS, */
/*   K_NUM_PMS, */
/*   K_NUM_HMI, */
/*   K_NUM_TRS, */
/*   K_NUM_TPL, */
/*   K_NUM_HDV, */
/*   K_NUM_REA, */
/*   K_NUM_SCS, */
/*   K_NUM_ARC, */
} CSC_t_IdNumber;
//@}

const long K_NUM_CEV_QUA = K_NUM_CEV;
const long K_NUM_CEV_LTV = K_NUM_CEV;
const long K_NUM_CEV_PSV = K_NUM_CEV;
const long K_NUM_CEV_SUP = K_NUM_CEV;

/**@name CSC error base numbers */
//@{
/// to be used to set ScadaSoft errors
const long K_ERRBAS_ALA = 4000;
const long K_ERRBAS_ATS = 4200;
const long K_ERRBAS_CCT = 4400;
const long K_ERRBAS_CLK = 4600;
const long K_ERRBAS_ECS = 4800;
const long K_ERRBAS_PAS = 5000;
const long K_ERRBAS_PIS = 5200;
const long K_ERRBAS_POW = 5400;
const long K_ERRBAS_RAD = 5600;
const long K_ERRBAS_SIG = 5800;
const long K_ERRBAS_SMC = 6000;
const long K_ERRBAS_TEL = 6200;
const long K_ERRBAS_TIM = 6400;
const long K_ERRBAS_BMF = 6600;
const long K_ERRBAS_SYS = 6800;
const long K_ERRBAS_MAI = 7000;
const long K_ERRBAS_ADM = 7200;
const long K_ERRBAS_DEP = 7400;
const long K_ERRBAS_SAR = 7600;
const long K_ERRBAS_DSS = 7800;
const long K_ERRBAS_PMS = 8000;
const long K_ERRBAS_HMI = 8200;
const long K_ERRBAS_TRS = 8400;
const long K_ERRBAS_TPL = 8600;
const long K_ERRBAS_HDV = 8800;
const long K_ERRBAS_REA = 9000;
const long K_ERRBAS_RDN = 9200;
const long K_ERRBAS_SCS = 9400;
const long K_ERRBAS_ARC = 9600;
const long K_ERRBAS_CGE = 9800;

const long K_ERRBAS_WIDTH = 200;
//@}

const int k_TraceUserModule       = 32;
const int k_TraceModuleReg        = K_NUM_TMC_REG + k_TraceUserModule;
const int k_TraceModuleSal        = K_NUM_TMC_SAL + k_TraceUserModule;
const int k_TraceModuleSiv        = K_NUM_TMC_SIV + k_TraceUserModule;
const int k_TraceModuleSig        = K_NUM_SIG + k_TraceUserModule;

#endif // __CSC_H__
