package com.thalesgroup.scadagen.bps.connector.operation;

import java.util.UUID;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.hv.sdk.connector.api.operation.requester.IOperationRequesterCallback;

public abstract interface IGenericOperationConnector {

	public abstract void setOnOperationResponseCallback(IOperationRequesterCallback paramIOperationRequesterCallback)
			throws HypervisorException;

	public abstract void requestOperation(AbstractOperationRequestType paramAbstractOperationRequestType)
			throws HypervisorException;

	public abstract void requestOperation(UUID paramUUID,
			AbstractOperationRequestType paramAbstractOperationRequestType) throws HypervisorException;

	public abstract IConnectorTools getTools();
}
