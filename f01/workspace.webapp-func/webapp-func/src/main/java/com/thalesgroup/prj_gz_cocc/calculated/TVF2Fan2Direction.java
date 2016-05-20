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

public class TVF2Fan2Direction implements StatusComputer {

	private static final String STATUS2 = "status2";
	private static final String DIRECTION2 = "direction2";
	
	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		IntAttribute status2 = (IntAttribute) inputStatusByName.get(STATUS2);
		IntAttribute direction2 = (IntAttribute) inputStatusByName.get(DIRECTION2);
		Integer symbolId = 2; // Default Undefined direction
		if (status2 != null && direction2 != null) {
			if (status2.getValue() == 1) {    // Started status
			    if (direction2.getValue() == 0) {
			        symbolId = 0;   // Forward direction
			    } else if (direction2.getValue() == 1) {
			        symbolId = 1;   // Reverse direction
			    }
			}
		}
		IntAttribute toReturn = new IntAttribute();
		toReturn.setAttributeClass(Integer.class.getName());
		toReturn.setTimestamp((status2 != null) ? status2.getTimestamp() : new Date());
		toReturn.setValid((status2 != null) ? status2.isValid() : true);
		toReturn.setValue(symbolId);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return TVF2Fan2Direction.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(STATUS2);
		result.add(DIRECTION2);
		return result;
	}

}
