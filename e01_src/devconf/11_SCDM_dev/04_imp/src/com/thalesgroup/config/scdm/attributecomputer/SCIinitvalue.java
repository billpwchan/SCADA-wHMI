package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.exchange.extract.ExtractKeys;

/******************************************************************************/
/*  FILE  : SCIinitvalue.java                                                 */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class SCIinitvalue extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		   String initValue_init = walker.getCurrentNode().getAttribute("initValue_init");
		   if (null==initValue_init) initValue_init = "";
		   
		   String result = "SCSDPC_SCIINIT([<alias>ScadaSoft.dpcTrigger],{.initValue})";
		   if (!"".equals(initValue_init)) {
			   result += ExtractKeys.FORMULA_SEPARATOR + initValue_init ;
		   }
		   return result;
    }
}
