package com.thalesgroup.scadagen.bps.data;

import java.util.Set;
import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;

public abstract class EntityDataDescriptionAbstract {
	private final Set<IOperator> criteria_;

	public EntityDataDescriptionAbstract(Set<IOperator> criteria) {
		criteria_ = criteria;
	}

	public Set<IOperator> getCriteria() {
		return criteria_;
	}
}
