package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : GRC_COMMANDextract_data.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class GRC_COMMANDextract_data extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		String result = NOT_CALCULABLE;
		Iterator it = walker.getLinkedNodeIDs("link_grc");
		if (it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elm = walker.getCurrentNode();
			if (null != elm) {
				String grcName = elm.getAttribute("UNIVNAME");
				if (null == grcName || "".equals(grcName) ) return NOT_CALCULABLE;
				if (NOT_COMPUTED_VALUE.equals(grcName)) return null;
				result = new String("<alias>" + grcName);
			}
		}
		return result;
    }
}
