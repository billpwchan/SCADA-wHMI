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


public class GDGSubsystem implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(GDGSubsystem.class.getName());

	private static final String SOURCEID = "sourceID";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		StringAttribute att = (StringAttribute) inputStatusByName.get(SOURCEID);
		String subsystemStr = "subsystemUndefined";

		
		
		// Extract subsystem string from sourceID
		if (att != null) {
		    String sourceID = att.getValue();
		    
		    s_logger.debug("GDGSubsystem sourceID =" + sourceID);
		    
		    if (sourceID.length() > 6) {
		        String s1 = sourceID.substring( 3, 6 );
		        if (s1.compareToIgnoreCase( "bas" ) == 0 || s1.compareToIgnoreCase( "ecs" ) == 0) {     
		            subsystemStr = "BAS";
		        } else if (s1.compareToIgnoreCase( "fas" ) == 0) {
                    subsystemStr = "FAS";
                } else if (s1.compareToIgnoreCase( "pow" ) == 0) {
                    subsystemStr = "PSCADA";
                } else if (s1.compareToIgnoreCase( "psd" ) == 0) {
                    subsystemStr = "PSD";
                } else if (s1.compareToIgnoreCase( "sig" ) == 0) {
                    subsystemStr = "SIG";
                } else if (s1.compareToIgnoreCase( "afc" ) == 0) {
                    subsystemStr = "AFC";
                } else if (s1.compareToIgnoreCase( "fep" ) == 0) {
                	subsystemStr = "MCS";
                }
		    }		    
		} else {
		    s_logger.debug("GDGSubsystem att is null");
		}

		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(StringAttribute.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(subsystemStr);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return GDGSubsystem.class.getName();
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
