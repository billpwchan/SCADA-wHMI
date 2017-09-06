package com.thalesgroup.scadasoft.calculated.symbol;

import java.util.Map;
import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;

public class SCADAgenSymbolHighlight implements StatusComputer {

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo,
			String entityId,
			Map<String, AttributeClientAbstract<?>> inputStatusByName,
			Map<String, Object> inputPropertiesByName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getComputerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInputStatuses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getInputProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
