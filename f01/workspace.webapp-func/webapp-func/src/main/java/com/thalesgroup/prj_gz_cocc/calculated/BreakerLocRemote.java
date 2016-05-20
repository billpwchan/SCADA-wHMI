package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.BooleanAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class BreakerLocRemote implements StatusComputer {

	private static final String CONTROLLOCATION = "controllocation";
	private static final String OCCPOWMODE = "occpowmode";
	
	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		IntAttribute ctrlLoc = (IntAttribute) inputStatusByName.get(CONTROLLOCATION);
		BooleanAttribute occpow = (BooleanAttribute) inputStatusByName.get(OCCPOWMODE);
		Integer symbolId = 2; // Dummy symbol
		if (ctrlLoc != null) {
			if (ctrlLoc.getValue() == 1) {
				symbolId = 1; // Local symbol
			} else if (occpow != null) {
			    if (ctrlLoc.getValue() == 2 && occpow.getValue() == false) {
			        symbolId = 0; // Remote symbol
			    }
			}
		}
		IntAttribute toReturn = new IntAttribute();
		toReturn.setAttributeClass(Integer.class.getName());
		toReturn.setTimestamp((ctrlLoc != null) ? ctrlLoc.getTimestamp() : new Date());
		toReturn.setValid((ctrlLoc != null) ? ctrlLoc.isValid() : true);
		toReturn.setValue(symbolId);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return BreakerLocRemote.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(CONTROLLOCATION);
		result.add(OCCPOWMODE);
		return result;
	}

}
