package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : OPMPROFILEACCESSLINEopmprofaccesslocation.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class OPMPROFILEACCESSLINEopmprofaccesslocation extends AttributeComputer {
	/** 
	 * name of the mandatory link to compute
	 */
	final static private String link = "link_OpmLocation";
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		Logger.USER.debug("Compute node named : " + 
							"'" + walker.getCurrentName() + "'");
		
		String locationCategory;
		Iterator it = walker.getLinkedNodeIDs(link);
		if(it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elt = walker.getCurrentNode();
			locationCategory = elt.getAttribute("OpmCategory");
		} else {
			Logger.USER.error("Can not compute the location of the profile access " +
					"because the profile access has no link to a location !!");
			return NOT_CALCULABLE;
		}
		
		// case more than one link set for the profile of the operator
		if(it.hasNext()) {
			Logger.USER.warn("Too many links for location of the profile access, " +
					"profile access must have only one link to a location");
			Logger.USER.warn("Get the first one found for computation ...");
		}
		
		// particular case, if catefory == 0 that means all functions => *
		if("0".equals(locationCategory)) {
			return "L*";
		} else {
			switch(locationCategory.length()) {
				case 1 : return "L00" + locationCategory;
				case 2 : return "L0"  + locationCategory;
				case 3 : return "L"   + locationCategory;
				default :
					Logger.USER.error("Can not compute because the attribute " +
							"'category' of the linked location must have a value " +
							"between 1 and 999");
					return NOT_CALCULABLE;
			}
		}
	}
}
