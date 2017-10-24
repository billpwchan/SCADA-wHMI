package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : DCI_MOD4_TYPEvalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DCI_MOD4_TYPEvalue extends AttributeComputer{
	public String compute(TreeWalker walker, ComputeContext context) {		
		String ce_formula = "SCSDPC_DCIVALUE([.source],[.status],";
		
		if (walker.getChildrenNodesOfType("dco_type").hasNext()){
			ce_formula = ce_formula + "MOD([dco.computedValue],4),[dfo.forcedValue])";			
		} else if (walker.getChildrenNodesOfType("dac_type").hasNext()){
			ce_formula = ce_formula + "MOD([dac.acqValue],4),[dfo.forcedValue])";
		} else {
			ce_formula = NOT_CALCULABLE;
		}
		return ce_formula;
	}
}
