package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.Set;

import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;

public abstract class ConfiguredEntityStatusesDataDescriptionAbstract extends EntityDataDescriptionAbstract {
	private final Set<String> statusesName_;
	private final Set<String> propertiesName_;

	public ConfiguredEntityStatusesDataDescriptionAbstract(Set<String> propertiesName, Set<String> statusesName) {
		super(new HashSet<IOperator>());
		propertiesName_ = propertiesName;
		statusesName_ = statusesName;
	}

	public ConfiguredEntityStatusesDataDescriptionAbstract(Set<String> propertiesName, Set<String> statusesName,
			Set<IOperator> criteria) {
		super(criteria);
		propertiesName_ = propertiesName;
		statusesName_ = statusesName;
	}

	public Set<String> getStatusesName() {
		return statusesName_;
	}

	public Set<String> getPropertiesName() {
		return propertiesName_;
	}
}
