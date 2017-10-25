package com.thalesgroup.config.scdm.attributecomputer; 
import java.util.Iterator;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : TAGcomputedvalue.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2017/07/10                                                */
/*  LANGUAGE  : JAVA                                                          */
/*																				
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class TAGcomputedvalue extends AttributeComputer {

	@SuppressWarnings("unchecked")
	@Override
	public String compute(TreeWalker walker, ComputeContext context) {
		
		String tag_name = "";
		String di_name = "";
		String computedValue="";
		
		tag_name = walker.getCurrentNode().getAttribute("nom_instance");
		
		Iterator <SAXElement> it = walker.walkToParentNode().walkToParentNode().getChildrenNodes();
		while (it.hasNext()){
			di_name = it.next().getAttribute("nom_instance");
			if (di_name.endsWith("-" + tag_name)){
				computedValue = "[^:^:" + di_name + ".value]" ;
				break;
			}
		}
		return computedValue;
	}
	
} 
