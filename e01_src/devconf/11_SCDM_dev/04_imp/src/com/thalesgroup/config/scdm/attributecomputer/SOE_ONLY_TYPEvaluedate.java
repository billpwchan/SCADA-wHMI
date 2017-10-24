package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesgroup.config.scdm.attributecomputer.DCIvaluedate;

/******************************************************************************/
/*  FILE  : SOE_ONLY_TYPEvaluedate.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
/* 	
 * 	SOE timestamp (where dac.acqValue ranged 4~7) is wrttien on dac.equipmentDate    
 *  Background Scan timestamp (where dac.acqValue ranged 0~3) is written on dac.acqValue and Daccom 
 *  write "dumpdate" on equipmentDate
 */

public class SOE_ONLY_TYPEvaluedate extends DCIvaluedate{

}
