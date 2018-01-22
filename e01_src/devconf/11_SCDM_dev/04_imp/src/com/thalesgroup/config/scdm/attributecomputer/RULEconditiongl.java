package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : RULEconditiongl.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES                                                      */
/*  CREATION DATE : 2018/01/11                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2018.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class RULEconditiongl extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		   return "IF([.enable] AND [.condition1] AND [.condition2] AND [.condition3] AND [.condition4] AND [.condition5] AND [.condition6] AND [.condition7] AND [.condition8],1,0)";
    }
}
