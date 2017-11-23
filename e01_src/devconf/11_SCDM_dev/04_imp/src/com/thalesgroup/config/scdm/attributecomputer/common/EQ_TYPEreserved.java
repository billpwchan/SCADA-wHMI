package com.thalesgroup.config.scdm.attributecomputer.common;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : EQ_TYPEreserved.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class EQ_TYPEreserved extends AttributeComputer {
	
	public String compute(TreeWalker walker, ComputeContext context) {
		String ce_reserved = "IF([:ScadaSoft.dpcTrigger]=1,0,{.reserved})";	
		return ce_reserved;
	}
}
