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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.MapStringByStringAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;


public class GDGEquipment implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(GDGEquipment.class.getName());

	private static final String ALARMTEXT = "alarmText";
	private static final String SERVICEOWNERID = "serviceOwnerID";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
	    MapStringByStringAttribute att = (MapStringByStringAttribute) inputStatusByName.get(ALARMTEXT);
		String equipment = "Undefined";
		String key = "{P1}";
		String separater = "\\|";
		Integer eqpLabelIdx = 2;
		Integer eqpShortLabelIdx = 3;
		String [] content = {};
		
		IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
		Map<String,String> properties = configLoader.getProjectConfigurationMap();
		if (properties != null) {
			String str = properties.get("AlarmDataGridKey");
			if (str != null) {
				key = str;
			}
			str = properties.get("AlarmDataGridSeparater");
			if (str != null) {
				separater = str;
			}
			str = properties.get("AlarmDataGridEqpLabelIndex");
			if (str != null) {
				try {
					eqpLabelIdx = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					s_logger.warn("Invalid value specified for property AlarmDataGridEqpLabelIndex");
				}
			}
			str = properties.get("AlarmDataGridEqpShortLabelIndex");
			if (str != null) {
				try {
					eqpShortLabelIdx = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					s_logger.warn("Invalid value specified for property AlarmDataGridEqpShortLabelIndex");
				}
			}
    	}
		
		// Extract equipment from "Equipment Short Label" and "Equipment Label" fields in event
		if (att != null) {
		    Map<String,String> map = att.getValue();
		    String rawMsg = map.get( key );
		    s_logger.debug("GDGEquipment rawMsg=" + rawMsg);
		    if (rawMsg != null) {
		        content = rawMsg.split( separater );
		        Integer i = 0;
		        for (String a : content) {
		            s_logger.debug("GDGEquipment content[" + i + "] = " + a);
		            i++;
		        }
		    
		        if (content.length > eqpLabelIdx && content.length > eqpShortLabelIdx) {
		        	equipment = content[eqpShortLabelIdx] + " " + content[eqpLabelIdx];
		        	s_logger.debug("GDGEquipment equipment = " + equipment);
		        } else {
		        	equipment = rawMsg;
		        }

		    } else {
		    	s_logger.warn("GDGEquipment att is null");
		    }

		} else {
		    s_logger.warn("GDGEquipment att is null");
		}

		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(StringAttribute.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(equipment);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return GDGEquipment.class.getName();
	}

	@Override
	public Set<String> getInputProperties() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getInputStatuses() {
		final Set<String> result = new HashSet<String>();
		result.add(ALARMTEXT);
		result.add(SERVICEOWNERID);
		return result;
	}

}
