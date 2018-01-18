package com.thalesgroup.config.scdm.attributecomputer; 
import java.util.Iterator;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : CMB_TYPEtrigger1.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class NORMALSTATEvaluelabel extends AttributeComputer {

	@SuppressWarnings("unchecked")
	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		
		return "IF([<alias>ScadaSoft.dpcTrigger]=1,INDEX({^:dal.valueTable},{.value},1)";
	}
}
