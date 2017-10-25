package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : COMMANDextract_data.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class COMMANDextract_data extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {
		String result = "";
		Iterator it = walker.getLinkedNodeIDs("link_data");
		if (it.hasNext()) {
			String currentLinkId = (String)it.next();
			walker.goToNode(currentLinkId);
			SAXElement elm = walker.getCurrentNode();
			if (null != elm) {
				String dataName = elm.getAttribute("ID");
				String dataTag = elm.getTagName();
				String dataType;
				if (dataTag.equals("sio_type")) {
					dataType = "2";
				}
				else if (dataTag.equals("aio_type")) {
					dataType = "1";
				}
				else {
					dataType = "0";
				}
				if (null != dataName)
					result = new String(dataName + "~" + dataType);
			}
		}
		return result;
    }
}
