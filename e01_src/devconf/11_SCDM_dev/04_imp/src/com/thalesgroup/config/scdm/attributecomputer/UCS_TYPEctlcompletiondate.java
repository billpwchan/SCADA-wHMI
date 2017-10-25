package com.thalesgroup.config.scdm.attributecomputer; 
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : UCS_TYPEctlcompletiondate.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class UCS_TYPEctlcompletiondate extends AttributeComputer {

	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		
		// TODO Auto-generated method stub
		String ctlCompletionDate = "IF(([.currExecStatus]=2) OR ({.prevExecStatus}=2),NOW(),{.CtlCompletionDate})";
		
		return ctlCompletionDate;
	}
	
}
