package com.thalesgroup.scadagen.bps.data;

import java.util.Set;

import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;

public class ConfiguredEntityStatusesInstancesDataDescription extends ConfiguredEntityStatusesDataDescriptionAbstract {
	private Set<String> entityIds_;

	public ConfiguredEntityStatusesInstancesDataDescription(Set<String> entityIds, Set<String> propertiesName,
			Set<String> statusesName) {
		super(propertiesName, statusesName);
		entityIds_ = entityIds;
	}

	public ConfiguredEntityStatusesInstancesDataDescription(Set<String> entityIds, Set<String> propertiesName,
			Set<String> statusesName, Set<IOperator> criteria) {
		super(propertiesName, statusesName, criteria);
		entityIds_ = entityIds;
	}

	public Set<String> getEntityIds() {
		return entityIds_;
	}
}
