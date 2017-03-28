package com.thalesgroup.scadagen.bps.data;

import java.util.HashSet;
import java.util.Set;

import com.thalesgroup.hv.sdk.connector.notification.tools.operator.IOperator;

public class TransientEntityDataDescription extends EntityDataDescriptionAbstract {
	private final String type_;

	public TransientEntityDataDescription(String entityType, Set<IOperator> criteria) {
		super(criteria);
		type_ = entityType;
	}

	public TransientEntityDataDescription(String entityType) {
		super(new HashSet<IOperator>());
		type_ = entityType;
	}

	public String getType() {
		return type_;
	}
}
