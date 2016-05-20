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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;


public class GDGScadaSubsystem implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(GDGScadaSubsystem.class.getName());

	private static final String SERVICEOWNERID = "serviceOwnerID";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		StringAttribute att = (StringAttribute) inputStatusByName.get(SERVICEOWNERID);
		String lineStr = "Undefined";

		if (att != null) {
			String serviceOwnerIDStr = att.getValue();
			if (serviceOwnerIDStr != null) {
				lineStr = serviceOwnerIDStr;
			}
		} else {
		    s_logger.debug("serviceOwnerID att is null");
		}
		
		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(StringAttribute.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(lineStr);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return GDGScadaSubsystem.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(SERVICEOWNERID);
		return result;
	}

}
