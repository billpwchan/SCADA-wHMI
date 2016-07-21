package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : ACIsource.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class ACIsource extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		StringBuffer formula = new StringBuffer("SCSDPC_SOURCE([.initValue],");
		String args = computeArgs(walker,"Status");
		if (NOT_CALCULABLE.equals(args)) return NOT_CALCULABLE;
		formula.append(args);
		formula.append(')');
		return formula.toString();
	}
	
	@SuppressWarnings("rawtypes")
	static public String computeArgs(TreeWalker walker, String att) {
	    /*
	      Recherche de l'existance d'une aac/aco/afo/aes
	    */
	    String indicAac = null;
	    String indicAco = null;
	    String indicAfo = null;
	    String indicAes = null;
	    
	    Iterator childIter = walker.getChildrenNodes();
	    while (childIter.hasNext()) {
	    	SAXElement child = (SAXElement)childIter.next();
	    	String childName = child.getAttribute("nom_instance");
	    	if ("aac".equalsIgnoreCase(childName)) indicAac = childName ;
	    	else if ("aco".equalsIgnoreCase(childName)) indicAco = childName ;
	    	else if ("afo".equalsIgnoreCase(childName)) indicAfo = childName ;
	    	else if ("aes".equalsIgnoreCase(childName)) indicAes = childName ;
	    }
	    
	    StringBuffer formula = new StringBuffer();
	    if (null!=indicAac) {
	    	formula.append("[");
	    	formula.append(indicAac);
	    	formula.append(".acq");
	    	formula.append(att);
	    	formula.append("],");
	    } else if (null!=indicAco) {
	    	formula.append("[");
	    	formula.append(indicAco);
	    	formula.append(".computed");
	    	formula.append(att);
	    	formula.append("],");
	    } else {
	    	return NOT_CALCULABLE;
	    }
	    
	    if (null!=indicAfo) {
	    	formula.append("[");
	    	formula.append(indicAfo);
	    	formula.append(".forced");
	    	formula.append(att);
	    	formula.append("]");
	    } else {
	    	formula.append("0");
	    }
	    
	    if (null!=indicAes) {
	    	formula.append(",[");
	    	formula.append(indicAes);
	    	formula.append(".estimated");
	    	formula.append(att);
	    	formula.append("]");
	    }
	    return formula.toString();
    }
}
