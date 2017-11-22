package com.thalesgroup.config.scdm.attributecomputer;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : SOE_ONLY_TYPEvalueformatteddate.java                                   */
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

public class SOE_ONLY_TYPEvalueformatteddate extends AttributeComputer{
	public String compute(TreeWalker walker, ComputeContext context) {		
		String ce_soe_date = "";
		walker.getCurrentNode();
		ce_soe_date = "TIME_FORMAT([.valueDate],\"%d/%m/%Y %H:%M:%S.\",\"%03d\")";			
		
		return ce_soe_date;
	}
}
