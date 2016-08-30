package com.thalesgroup.scadagen.bps.connector.operation;

import java.util.UUID;

import com.thalesgroup.hv.data_v1.operation.AbstractOperationResponseType;

public abstract interface IGenericOperationResponse {
	public abstract void onOperationResponse(UUID paramUUID, AbstractOperationResponseType paramAbstractOperationResponseType);
}
