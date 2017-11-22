package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

/**
 * compute the RealCnt attribute of list : number of values in the list
 */
public class RealCnt {

	@SuppressWarnings("rawtypes")
	static public String compute(TreeWalker walker, ComputeContext context, String vectorName)
	{
		// get the values iterator
		Iterator venameIt = ValidatorFunctions.getNodeVectorIterator(walker , vectorName);
		int realCnt = 0;
		while (venameIt.hasNext()) {
			String value = (String)venameIt.next();
			if ("".equals(value)) break;
			realCnt++;
		}
		return Integer.toString(realCnt);
	}
}
