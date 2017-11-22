package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : SIO_TYPEreturncondgl.java                                         */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class DPC_COMreturncondgl extends AttributeComputer {
	
	/**
	 * The formula
	 */
	private static String formula_ = "IF( [.returnCond1] AND [.returnCond2] AND "      +
			"[.returnCond3] AND [.returnCond4] AND [.returnCond5] AND [.returnCond6] " +
			"AND [.returnCond7] AND [.returnCond8], 1, 0)£1";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return formula_;
    }
}
