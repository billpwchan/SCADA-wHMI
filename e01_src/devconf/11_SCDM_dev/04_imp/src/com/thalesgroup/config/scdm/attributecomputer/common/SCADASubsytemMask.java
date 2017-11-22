package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.utils.ExtendBitSet;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : SCADASubsytemMask.java                                            */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES TRANSPORTATION SYSTEM                                   */
/*  CREATION DATE : 12 janv. 07                                               */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Transportation System 1996-2006.                       */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/

public class SCADASubsytemMask extends AttributeComputer {

	@SuppressWarnings("rawtypes")
	public String compute(TreeWalker walker, ComputeContext context) {		
		// no subsytems defined, look the children mask
		ExtendBitSet maskResult = new ExtendBitSet();
		ExtendBitSet maskBit    = new ExtendBitSet();
		SAXElement node = walker.getCurrentNode();
		String value = node.getAttribute("SCADASubsystemPreMask");
		if (value != null) {
			maskResult.valueOf(value);
		}
		
		Iterator it = walker.getChildrenNodes();
		while( it.hasNext() ) {
			node = (SAXElement)it.next();
			value = node.getAttribute("SCADASubsystemMask");
			if( !NOT_COMPUTED_VALUE.equals(value) && value != null ) {
				maskBit.valueOf(value);
				maskResult.or(maskBit);
			}
		}

		return maskResult.toString();
	}

}

