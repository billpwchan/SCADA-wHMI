package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

public class XIO_TYPEreturncondgl extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return "IF( [.returnCond1] AND [.returnCond2] AND [.returnCond3] AND [.returnCond4] AND [.returnCond5] AND [.returnCond6] AND [.returnCond7] AND [.returnCond8], 1, 0)£1";
    }
}
