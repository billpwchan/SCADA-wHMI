package com.thalesgroup.config.scdm.attributecomputer.common;
import com.thalesis.config.utils.Logger;
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

public class EQ_TYPEresrvaction1 extends AttributeComputer {
	
	public String compute(TreeWalker walker, ComputeContext context) {
		
		//Get Attribute "resrvTimeout"
		String resrv_timeout=walker.getCurrentNode().getAttribute("resrvTimeout");
		if (resrv_timeout.equals(null) || "".equals(resrv_timeout)){
			resrv_timeout="60";
		} else {
			//Logger.BUSINESS.error("OK");
		}
		return "IF((TO_UNIXTIME([.resrvCheckTime])-TO_UNIXTIME([.resrvReservedTime]))>=" + resrv_timeout + ",StrLen(PUT(\"\",{.resrvReservedID})),(TO_UNIXTIME({.resrvCheckTime})-TO_UNIXTIME({.resrvReservedTime})))";
		
	}
}
