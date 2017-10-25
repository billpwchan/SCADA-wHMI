package com.thalesgroup.config.scdm.attributecomputer; 
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : UCS_TYPEcurrexecstatus.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class UCS_TYPEcurrexecstatus extends AttributeComputer {

	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		String DO_pointName=walker.getCurrentNode().getAttribute("DO_pointname");
		String currexecstatus = "BITOR(BITAND(0,IF({.currExecStatus}<>{^:^:" + DO_pointName + ".execStatus},PUT({.currExecStatus},{.prevExecStatus}),0)),[^:^:" + DO_pointName + ".execStatus])";
		return currexecstatus;
	}
}