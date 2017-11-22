package com.thalesgroup.config.scdm.attributecomputer;

import java.util.Iterator;

import com.thalesgroup.config.scdm.attributecomputer.DCIsource;
import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

/******************************************************************************/
/*  FILE  : DCIvaluedate.java                                   */
/*----------------------------------------------------------------------------*/
/*  COMPANY  : THALES IS                                                      */
/*  CREATION DATE : 2005/02/09                                                */
/*  LANGUAGE  : JAVA                                                          */
/*............................................................................*/
/*  Copyright © THALES Information Systems 1996-2003.                         */
/*  All rights reserved.                                                      */
/*  Unauthorized access, use, reproduction or distribution is prohibited.     */
/******************************************************************************/
public class DCIvaluedate extends AttributeComputer {
	
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		StringBuffer formula = new StringBuffer("SCSDPC_DATE([.source],");
		String args = DCIsource.computeArgs(walker,"Date");
		
		boolean hasDumpDate = false;
		boolean hasTimestampSelection = false;
		boolean hasEquipmentDate = false;
		String ts = "";
		
		if (NOT_CALCULABLE.equals(args)) return NOT_CALCULABLE;
		//get time selection, attribute: timestampSelection
		if (null != walker.getCurrentNode().getAttribute("timestampSelection")){
			hasTimestampSelection = true;
			ts = walker.getCurrentNode().getAttribute("timestampSelection");
		}
		Iterator childDacIter = walker.getChildrenNodesOfType("dac_type");
		SAXElement childDac;
		if (childDacIter.hasNext()){
			childDac = (SAXElement)childDacIter.next();
			if (!childDac.getAttribute("dumpdate").equals(null)){
				hasDumpDate = true;
			}
			if (!childDac.getAttribute("equipmentDate").equals(null)){
				hasEquipmentDate = true;
			}
		}
		if (hasTimestampSelection) {
			if ("1".equals(ts)) {
					if (hasEquipmentDate) { 
						args = args.replace("[dac.acqDate]", "[dac.equipmentDate]");
					}
			} else if ("2".equals(ts)) {
					if (hasEquipmentDate && hasDumpDate) {
						args = args.replace("[dac.acqDate]", "IF(TO_UNIXTIME({dac.dumpdate})=TO_UNIXTIME({dac.equipmentDate}),[dac.acqDate],[dac.equipmentDate])");
					}
			}
		}
		formula.append(args);
		formula.append(')');
		return formula.toString();
    }
}
