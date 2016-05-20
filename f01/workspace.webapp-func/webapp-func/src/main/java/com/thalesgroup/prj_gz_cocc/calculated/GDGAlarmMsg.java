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


public class GDGAlarmMsg implements StatusComputer {
	
	private static final Logger s_logger = LoggerFactory.getLogger(GDGAlarmMsg.class.getName());

	private static final String ALARMTEXT = "alarmText";
	private static final String SERVICEOWNERID = "serviceOwnerID";

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
	    MapStringByStringAttribute att = (MapStringByStringAttribute) inputStatusByName.get(ALARMTEXT);
		String alarmMsg = "Undefined";
		String key = "{P1}";
		String separater = "\\|";
		String outSeparater = ": ";
		Integer ptLabelIdx = 4;
		Integer ptValueLabelIdx = 5;
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
			str = properties.get("AlarmDataGridOutSeparater");
			if (str != null) {
				outSeparater = str;
			}
			str = properties.get("AlarmDataGridPointLabelIndex");
			if (str != null) {
				try {
					ptLabelIdx = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					s_logger.warn("Invalid value specified for property AlarmDataGridPointLabelIndex");
				}
			}
			str = properties.get("AlarmDataGridPointValueLabelIndex");
			if (str != null) {
				try {
					ptValueLabelIdx = Integer.valueOf(str);
				} catch (NumberFormatException e) {
					s_logger.warn("Invalid value specified for property AlarmDataGridPointValueLabelIndex");
				}
			}
    	}
		
		// Extract alarm text from "Point Label" and "Point ValueLabel" fields in event
		if (att != null) {
		    Map<String,String> map = att.getValue();
		    String rawMsg = map.get( key );
		    s_logger.debug("GDGAlarmMsg rawMsg=" + rawMsg);
		    if (rawMsg != null) {
		        content = rawMsg.split( separater );
		        Integer i = 0;
		        for (String a : content) {
		            s_logger.debug("GDGAlarmMsg content[" + i + "] = " + a);
		            i++;
		        }
		    
		        if (content.length > ptLabelIdx && content.length > ptValueLabelIdx) {
		            alarmMsg = content[ptLabelIdx] + outSeparater + content[ptValueLabelIdx];
		            s_logger.debug("GDGAlarmMsg alarmMsg = " + alarmMsg);
		        } else {
		            alarmMsg = rawMsg;
		        }
		    } else {
		    	s_logger.warn("GDGAlarmMsg att is null");
		    }
		} else {
		    s_logger.warn("GDGAlarmMsg att is null");
		}

		StringAttribute toReturn = new StringAttribute();
		toReturn.setAttributeClass(StringAttribute.class.getName());
		toReturn.setTimestamp((att != null) ? att.getTimestamp() : new Date());
		toReturn.setValid(att.isValid());
		toReturn.setValue(alarmMsg);
		
		return toReturn;
	}

	@Override
	public String getComputerId() {
		return GDGAlarmMsg.class.getName();
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
