package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class StationFasAlarm implements StatusComputer {
	private static final ClientLogger s_logger = ClientLogger.getClientLogger();

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
            String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName,
            Map<String, Object> inputPropertiesByName) {
		IntAttribute stationFasAlarmAttribute = new IntAttribute ();
		
		stationFasAlarmAttribute.setValue(0);
		stationFasAlarmAttribute.setValid(true);
		stationFasAlarmAttribute.setTimestamp(new Date());
		stationFasAlarmAttribute.setAttributeClass(IntAttribute.class.getName());
		
		s_logger.info("StationFasAlarm compute");
		
		AttributeClientAbstract<?> msgobj = inputStatusByName.get("fasmode");
		IntAttribute msg = (msgobj instanceof IntAttribute ? (IntAttribute)msgobj : null);
        if (msg != null) {
            if (msg.getValue() > 0) {
            	stationFasAlarmAttribute.setValue(1);
            	s_logger.info("StationFasAlarm setValue(1)");
            }
        }
		
		return stationFasAlarmAttribute;
	}

	@Override
	public String getComputerId() {
		return "com.thalesgroup.scadasoft.gwebhmi.main.client.calculated.StationFasAlarm";
	}

	@Override
	public Set<String> getInputProperties() {
		String [] inputPropArray = {};
		Set<String> inputPropSet = new HashSet<String> (Arrays.asList(inputPropArray));
		return inputPropSet;
	}

	@Override
	public Set<String> getInputStatuses() {
		String [] inputStatusesArray = {"fasmode"};
		Set<String> inputStatusesSet = new HashSet<String> (Arrays.asList(inputStatusesArray));
		return inputStatusesSet;
	}

}
