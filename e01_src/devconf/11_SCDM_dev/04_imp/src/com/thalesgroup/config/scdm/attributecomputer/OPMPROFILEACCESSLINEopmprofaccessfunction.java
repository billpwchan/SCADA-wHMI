package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : OPMPROFILEACCESSLINEopmprofaccessfunction.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class OPMPROFILEACCESSLINEopmprofaccessfunction extends AttributeComputer {
	/** 
	 * name of the mandatory link to compute
	 */
	final static private String link = "link_OpmFunction";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		Logger.USER.debug("Compute node named : " + 
							"'" + walker.getCurrentName() + "'");
		
		String functionCategory;
		Iterator it = walker.getLinkedNodeIDs(link);
		if(it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elt = walker.getCurrentNode();
			functionCategory = elt.getAttribute("OpmCategory");
		} else {
			Logger.USER.error("Can not compute the function of the profile access " +
					"because the profile access has no link to a function !!");
			return NOT_CALCULABLE;
		}
		
		// case more than one link set for the profile of the operator
		if(it.hasNext()) {
			Logger.USER.warn("Too many links for function of the profile access, " +
					"profile access must have only one link to a function");
			Logger.USER.warn("Get the first one found for computation ...");
		}
		
		// particular case, if catefory == 0 that means all functions => *
		if("0".equals(functionCategory)) {
			return "F*";
		} else {
			switch(functionCategory.length()) {
				case 1 : return "F00" + functionCategory;
				case 2 : return "F0"  + functionCategory;
				case 3 : return "F"   + functionCategory;
				default :
					Logger.USER.error("Can not compute because the attribute " +
							"'category' of the linked function must have a value " +
							"between 1 and 999");
					return NOT_CALCULABLE;
			}
		}
    }
}
