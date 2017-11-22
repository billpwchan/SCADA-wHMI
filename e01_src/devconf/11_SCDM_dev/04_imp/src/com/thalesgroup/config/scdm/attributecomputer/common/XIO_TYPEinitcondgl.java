package com.thalesgroup.config.scdm.attributecomputer.common;

import com.thalesis.config.utils.TreeWalker;
import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;

public class XIO_TYPEinitcondgl extends AttributeComputer {
	/**
	 * Surcharge de la méthode de calcul
	 */
	public String compute(TreeWalker walker, ComputeContext context) {
		return "IF( [.initCond1] AND [.initCond2] AND [.initCond3] AND [.initCond4] AND [.initCond5] AND [.initCond6] AND [.initCond7] AND [.initCond8], 1, 0)£1";
    }
}
