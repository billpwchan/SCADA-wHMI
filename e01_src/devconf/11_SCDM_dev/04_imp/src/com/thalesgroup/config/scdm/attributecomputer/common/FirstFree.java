package com.thalesgroup.config.scdm.attributecomputer.common;

import java.util.Iterator;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.business.constraints.validator.ValidatorFunctions;

public class FirstFree {

	@SuppressWarnings("rawtypes")
	static public String compute(TreeWalker walker, ComputeContext context, String vectorName)
	{
		// get the values iterator
		Iterator venameIt = ValidatorFunctions.getNodeVectorIterator(walker , vectorName);
		int firstFree = 0;
		while (venameIt.hasNext()) {
			String value = (String)venameIt.next();
			if ("".equals(value)) break;
			firstFree++;
		}
		return Integer.toString(firstFree);
	}
}
