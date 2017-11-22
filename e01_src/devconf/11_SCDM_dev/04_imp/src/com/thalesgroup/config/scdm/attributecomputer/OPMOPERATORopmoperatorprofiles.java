package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : OPMOPERATORopmoperatorprofiles.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class OPMOPERATORopmoperatorprofiles extends AttributeComputer {
	
	/** 
	 * name of the mandatory link to compute
	 */
	final static private String link = "link_OpmProfile";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		Logger.USER.debug("Compute node named : " + 
							"'" + walker.getCurrentName() + "'");
		
		String profileName = "";
		Iterator it = walker.getLinkedNodeIDs(link);
		int count = 0;
		while(it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elt = walker.getCurrentNode();
			if(count == 0) {
				profileName += elt.getAttribute("OpmProfileName");
			} else {
				profileName += "|" + elt.getAttribute("OpmProfileName");
			}
			count++;
		}
		
		if(count == 0) {
			Logger.USER.error("Can not compute the profile of the operator " +
					"because the operator has no link to a profile !!");
			return NOT_CALCULABLE;
		}
		
		// case more than one link set for the profile of the operator
		if(it.hasNext()) {
			Logger.USER.warn("Too many links for operator profile, operator must " +
					"have only one link to a profile");
			Logger.USER.warn("Get the first one found for computation ...");
		}
		
		return profileName;
    }
}
