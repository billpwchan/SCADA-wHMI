package com.thalesgroup.scadagen.bps.data;

import java.util.Set;

import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;

public class ConfiguredEntityStatusesTypeDataDescription extends ConfiguredEntityStatusesDataDescriptionAbstract {
	private final String entityType_;

	public ConfiguredEntityStatusesTypeDataDescription(String entityType, Set<String> propertiesName,
			Set<String> statusesName) {
		super(propertiesName, statusesName);
		entityType_ = entityType;
	}

	public ConfiguredEntityStatusesTypeDataDescription(String entityType, Set<String> propertiesName,
			Set<String> statusesName, Set<IOperator> criteria) {
		super(propertiesName, statusesName, criteria);
		entityType_ = entityType;
	}

	public String getEntityType() {
		return entityType_;
	}
}
