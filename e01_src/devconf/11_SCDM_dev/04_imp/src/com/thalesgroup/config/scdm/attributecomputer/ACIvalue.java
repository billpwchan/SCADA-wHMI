package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : ACIvalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright � THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class ACIvalue extends AttributeComputer {
	/**
	 * Surcharge de la m�thode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		StringBuffer formula = new StringBuffer("SCSDPC_ACIVALUE([.source],[.status],");
		String args = ACIsource.computeArgs(walker,"Value");
		if (NOT_CALCULABLE.equals(args)) return NOT_CALCULABLE;
		formula.append(args);
		formula.append(')');
		return formula.toString();
    }
}
