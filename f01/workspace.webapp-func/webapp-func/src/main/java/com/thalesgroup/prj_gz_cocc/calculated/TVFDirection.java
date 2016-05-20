package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class TVFDirection implements StatusComputer {

	private static final String STATUS = "status";
	private static final String DIRECTION = "direction";
	
	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		IntAttribute status = (IntAttribute) inputStatusByName.get(STATUS);
		IntAttribute direction = (IntAttribute) inputStatusByName.get(DIRECTION);
		Integer symbolId = 2; // Default Undefined direction
		if (status != null && direction != null) {
			if (status.getValue() == 1) {    // Started status
			    if (direction.getValue() == 0) {
			        symbolId = 0;   // Forward direction
			    } else if (direction.getValue() == 1) {
			        symbolId = 1;   // Reverse direction
			    }
			}
		}
		IntAttribute toReturn = new IntAttribute();
		toReturn.setAttributeClass(Integer.class.getName());
		toReturn.setTimestamp((status != null) ? status.getTimestamp() : new Date());
		toReturn.setValid((status != null) ? status.isValid() : true);
		toReturn.setValue(symbolId);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return TVFDirection.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(STATUS);
		result.add(DIRECTION);
		return result;
	}

}
