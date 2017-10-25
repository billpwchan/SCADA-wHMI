package com.thalesgroup.config.scdm.attributecomputer;

import com.thalesis.config.business.attributecomputer.AttributeComputer;
import com.thalesis.config.business.attributecomputer.ComputeContext;
import com.thalesis.config.utils.TreeWalker;

public class DCI_SOE_TYPEvalue extends AttributeComputer{
	public String compute(TreeWalker walker, ComputeContext context) {		
		String ce_soe_value = "";
		
		if (walker.getChildrenNodesOfType("dco_type").hasNext()){
			ce_soe_value = "SCSDPC_DCIVALUE([.source],[.status],IF([dco.computedValue]=MOD({.value},4),{.value},{dco.computedValue}),[dfo.forcedValue])";			
		} else if (walker.getChildrenNodesOfType("dac_type").hasNext()){
			ce_soe_value = "SCSDPC_DCIVALUE([.source],[.status],IF([dac.acqValue]=MOD({.value},4),{.value},{dac.acqValue}),[dfo.forcedValue])";
		}
		return ce_soe_value;
	}
}
