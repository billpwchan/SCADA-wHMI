package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : SIO_TYPEinitcondgl.java                                           */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class DPC_COMinitcondgl extends AttributeComputer {
	
	/**
	 * The formula
	 */
	private static String formula_ = "IF( [.initCond1] AND [.initCond2] AND "  +
			"[.initCond3] AND [.initCond4] AND [.initCond5] AND [.initCond6] " +
			"AND [.initCond7] AND [.initCond8], 1, 0)£1";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return formula_;
    }
}
