package com.thalesgroup.config.scdm.attributecomputer; 
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : UCS_TYPEcurrentvaluedate.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class UCS_TYPEcurrentvaluedate extends AttributeComputer {

	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		String currentDate = "EVAL([.currentDIValue],TO_UNIXTIME(PUT({.currentValueDate},{.prevValueDate})))";
		return currentDate;
	}
}