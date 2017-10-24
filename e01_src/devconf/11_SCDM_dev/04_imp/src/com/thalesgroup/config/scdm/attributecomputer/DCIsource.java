package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : DCIsource.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DCIsource extends AttributeComputer {
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
	      Recherche de l'existance d'une dac/dco/dfo/des
	    */
	    String indicDac = null;
	    String indicDco = null;
	    String indicDfo = null;
	    String indicDes = null;
	    
	    Iterator childIter = walker.getChildrenNodes();
	    while (childIter.hasNext()) {
	    	SAXElement child = (SAXElement)childIter.next();
	    	String childName = child.getAttribute("nom_instance");
	    	if ("dac".equalsIgnoreCase(childName)) indicDac = childName ;
	    	else if ("dco".equalsIgnoreCase(childName)) indicDco = childName ;
	    	else if ("dfo".equalsIgnoreCase(childName)) indicDfo = childName ;
	    	else if ("des".equalsIgnoreCase(childName)) indicDes = childName ;
	    }
	    
	    StringBuffer formula = new StringBuffer();
	    if (null!=indicDac) {
	    	formula.append("[");
	    	formula.append(indicDac);
	    	formula.append(".acq");
	    	formula.append(att);
	    	formula.append("],");
	    } else if (null!=indicDco) {
	    	formula.append("[");
	    	formula.append(indicDco);
	    	formula.append(".computed");
	    	formula.append(att);
	    	formula.append("],");
	    } else {
	    	return NOT_CALCULABLE;
	    }
	    
	    if (null!=indicDfo) {
	    	formula.append("[");
	    	formula.append(indicDfo);
	    	formula.append(".forced");
	    	formula.append(att);
	    	formula.append("]");
	    } else {
	    	formula.append("0");
	    }
	    
	    if (null!=indicDes) {
	    	formula.append(",[");
	    	formula.append(indicDes);
	    	formula.append(".estimated");
	    	formula.append(att);
	    	formula.append("]");
	    }
	    return formula.toString();
    }
}
