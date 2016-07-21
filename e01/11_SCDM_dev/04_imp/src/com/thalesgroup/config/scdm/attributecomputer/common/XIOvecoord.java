package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/******************************************************************************/
/*  FILE  : XIOvecoord.java                                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2006/12/18                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright � THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class XIOvecoord extends AttributeComputer {
	/**
	 * Surcharge de la m�thode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		// get the current element
		SAXElement element = walker.getCurrentNode();
		// get the valut for the command	
		String ctlvalue = element.getAttribute("ctlvalue");
        if (ctlvalue == null) {
          ctlvalue = "";
        }
		String polename = "";
		String vename = "";
		
		Iterator elemLinkIt = ValidatorFunctions.getNodeLinkIterator(walker,"ve");
		if (!elemLinkIt.hasNext())
		{
			// we try to compute the vename using the IO univname
			// get the current instance name (DIO or AIO or SIO)
			String classID = element.getAttribute("class_id");
			String name = "seo";
			if ("110".equalsIgnoreCase(classID)) {
				//DOV : we move to the dio point to be at the same level
				walker.walkToParentNode();
				name = "deo";
			} else if ("108".equalsIgnoreCase(classID)) {
				// AIO
				name = "aeo";
			}
            SAXElement xio_elem = walker.getCurrentNode();
            String ni = xio_elem.getAttribute("nom_instance"); // xio name
            vename = name + walker.walkToParentNode().getCurrentNode().getAttribute("UNIVNAME") + "_"; // equipment name
            vename += ni.substring(3);
            
		} else {
			walker.goToNode((String)elemLinkIt.next());
			SAXElement ev_elem = walker.getCurrentNode();		
			if (ev_elem == null || Boolean.TRUE.equals(walker.isCurrentNodeSkeleton()))
			{
				return NOT_CALCULABLE;
			}
			vename = ev_elem.getAttribute("name");	
	
			SAXElement pole_elem = walker.walkToFirstAncestorNodeOfType("POLE").getCurrentNode();
			if (pole_elem == null || Boolean.TRUE.equals(walker.isCurrentNodeSkeleton()))
			{
				return NOT_CALCULABLE;
			}
			polename = pole_elem.getAttribute("name");
		}
		
		return polename + "," + vename + "," + ctlvalue;
    }
}