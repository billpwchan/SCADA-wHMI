package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.Logger;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;

/******************************************************************************/
/*  FILE  : OPMPROFILEACCESSLINECommonMask.java                               */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 9 juil. 07                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class OPMPROFILEACCESSLINECommonMask {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	static public String compute(TreeWalker walker, ComputeContext context, String link) {
		Logger.USER.debug("Compute node named : " + 
				"'" + walker.getCurrentName() + "'");

		String mask = "";
		int count   = 0;
		Iterator it = walker.getLinkedNodeIDs(link);
		while(it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elt = walker.getCurrentNode();

			String category = elt.getAttribute("OpmCategory");
			// category == 0 means all actions 
			if("0".equals(category)) {
				if(count > 0) {
					Logger.USER.warn("You can not define all actions and specific " +
					"actions at same time");
					Logger.USER.warn("Decision : set mask for all actions ...");
				}
				// that means all actions => 0xFF or 1
				return "1";
			}

			mask += elt.getAttribute("OpmActionAbbrev");
			count++;
		}

		if(count == 0) {
			// that means no actions => 0x00 or 0
			return "0";
		}

		return mask;
	}

}
