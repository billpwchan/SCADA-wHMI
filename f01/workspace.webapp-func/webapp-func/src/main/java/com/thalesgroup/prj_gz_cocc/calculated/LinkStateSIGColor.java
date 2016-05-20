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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public class LinkStateSIGColor implements StatusComputer {

	private static final String IS_ENERGISED = "isEnergised";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		BooleanAttribute att = (BooleanAttribute) inputStatusByName.get(IS_ENERGISED);
		String color = "#00FF00";
		if (att != null) {
			if (att.getValue() == false) {
				color = "grey";
			}
		}
		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(String.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(color);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return LinkStateSIGColor.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(IS_ENERGISED);
		return result;
	}

}
