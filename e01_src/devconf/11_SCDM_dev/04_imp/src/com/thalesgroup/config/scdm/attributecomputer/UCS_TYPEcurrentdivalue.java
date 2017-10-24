package com.thalesgroup.config.scdm.attributecomputer; 

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;


/******************************************************************************/
/*  FILE  : UCS_TYPEcurrentdivalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class UCS_TYPEcurrentdivalue extends AttributeComputer {

	public String compute(TreeWalker walker, ComputeContext context) {
		String currentDIValue="";
		String DI_pointName=walker.getCurrentNode().getAttribute("DI_pointname");
		currentDIValue="EVAL_INT(MOD(MOD([^:^:" + DI_pointName + ".value],4),3),IF({.currentDIValue}<>MOD(MOD({^:^:" + DI_pointName + ".value},4),3),PUT({.currentDIValue},{.previousDIValue}),0))";
		return currentDIValue;
	}
	
}