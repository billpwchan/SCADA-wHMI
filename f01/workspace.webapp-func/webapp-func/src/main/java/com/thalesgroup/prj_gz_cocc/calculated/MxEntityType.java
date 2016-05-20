package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;


public class MxEntityType implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(MxEntityType.class.getName());

	private static final String SOURCEID = "sourceID";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		StringAttribute att = (StringAttribute) inputStatusByName.get(SOURCEID);
		String entityTypeStr = "Undefined";

		IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		
		// Get equipment type from config using sourceID
		if (att != null) {
		    String sourceID = att.getValue();
		    
		    s_logger.debug("MxEntityType sourceID =" + sourceID);
		    
		    String type = "";
		    entityTypeStr = ((type = configLoader.getSharedConfiguration().getEntity(sourceID).getClass().getName())== null ? "Undefined" : type);
		    
		    s_logger.debug("MxEntityType entityTypeStr =" + entityTypeStr);
		    
		} else {
		    s_logger.debug("MxEntityType att is null");
		}

		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(String.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(entityTypeStr);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return MxEntityType.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(SOURCEID);
		return result;
	}

}
