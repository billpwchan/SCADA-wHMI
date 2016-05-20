package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public class PlatformDestination implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(PlatformDestination.class.getName());

	private static final String DESTINATION = "destination";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		IntAttribute att = (IntAttribute) inputStatusByName.get(DESTINATION);
		String stationStr = "XXXX";

		// Extract MCS string from entityId
		int index = entityId.indexOf("--")+2;
		String mcs = entityId.substring(index, entityId.indexOf("--",index));
		
		s_logger.debug("PlatformDestination entityId=" + entityId + " mcs=" + mcs);
		
		if (att != null) {
			if (att.getValue() > 0) {
				stationStr = mcs + String.format("S%02d", att.getValue());
			}
		}
		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(String.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(stationStr);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return PlatformDestination.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(DESTINATION);
		return result;
	}

}
