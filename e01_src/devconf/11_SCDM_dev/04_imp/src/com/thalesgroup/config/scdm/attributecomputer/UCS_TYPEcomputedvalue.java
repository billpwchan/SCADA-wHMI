package com.thalesgroup.config.scdm.attributecomputer; 

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : UCS_TYPEcomputedvalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class UCS_TYPEcomputedvalue extends AttributeComputer {

	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		// TODO Auto-generated method stub
		String pointname = "";
		pointname = walker.getCurrentNode().getAttribute("DI_pointname");
		
		if ( pointname.equals("")) {
			return "";
		}
		String computedValue="";
		computedValue = computedValue + "IF(";
		computedValue = computedValue + "IF(({.currExecStatus}=2) OR (({.prevExecStatus}=2) AND (SIGNED_TIMEDIFF({.CtlCompletionDate},NOW())< {.delay})),0,1) AND "; 
		computedValue = computedValue + "({^:^:" + pointname + ".status}=1) AND "; 
		computedValue = computedValue + "({^:^:" + pointname + ".previousStatus}=1) AND "; 
		computedValue = computedValue + "({.currentDIValue}<>0) AND "; 
		computedValue = computedValue + "IF(({.previousDIValue}=0) AND (SIGNED_TIMEDIFF([.prevValueDate],NOW())>{.delay}),0,1),{.currentDIValue},0)";
		
		return computedValue;
	}
	
} 
