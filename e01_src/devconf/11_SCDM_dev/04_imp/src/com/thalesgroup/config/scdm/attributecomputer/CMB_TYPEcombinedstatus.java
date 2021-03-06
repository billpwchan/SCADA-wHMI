package com.thalesgroup.config.scdm.attributecomputer; 
import java.util.Iterator;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : CMB_TYPEcombinedstatus.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright � THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class CMB_TYPEcombinedstatus extends AttributeComputer {

	@SuppressWarnings("unchecked")
	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		String point1="";
		String point2="";
		SAXElement combineList;
		Iterator<SAXElement> iCombineList = walker.getChildrenNodesOfType("cmb_type_VECTOR_combineList");
		if (iCombineList.hasNext()){
			combineList = iCombineList.next();
			point1=combineList.getAttribute("v0");
			point2=combineList.getAttribute("v1");
		} else {
			return "";
		}
		String combinedStatus="SCSDPC_ACQSTATUS( [^:^:" + point1 + ".status], [^:^:" + point2 + ".status])";
		return combinedStatus;
	}
}
