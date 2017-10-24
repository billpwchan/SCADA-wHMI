package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

import java.util.Iterator;


/******************************************************************************/
/*  FILE  : EQ_TYPEiscontrolable.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class EQ_TYPEiscontrolable extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		Iterator childIt = walker.getChildrenNodes();
		while (childIt.hasNext()) {
			SAXElement childElt = (SAXElement)childIt.next();
			String classID = childElt.getAttribute("class_id");
			if (null!=classID && !"".equals(classID)) {
				if ("108".equals(classID) || "105".equals(classID) || "109".equals(classID)) return "true";
			}
		}
		return "false";
    }
}
