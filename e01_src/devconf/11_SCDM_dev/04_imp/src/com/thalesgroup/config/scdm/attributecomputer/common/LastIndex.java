package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.SAXElement;
import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.utils.VectorUtility;
import com.thalesis.config.business.attributecomputer.CommonAttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;


/**
 * Compute the size of a list
 */
public class LastIndex {

	static public String compute(TreeWalker walker, ComputeContext context, String vectorName)
	{
		// go to the vector element
		walker.walkToChildNode(VectorUtility.getVectorName(vectorName));
		// get the vector element
		SAXElement vector = walker.getCurrentNode();
		// return its length
		String length = null;
		if (null!=vector) {
			length = vector.getAttribute("maxLength");
		}
		return (null == length || "".equals(length)) ?  CommonAttributeComputer.NOT_CALCULABLE : length;
	}
}
