package com.thalesgroup.scadagen.bps.connector.operation;

import java.util.Map;
import java.util.UUID;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.DateUtils;
import com.thalesgroup.hv.data_v1.operation.AbstractOperationRequestType;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.hv.sdk.connector.api.operation.requester.IOperationRequesterCallback;

public class GenericOperationConnector implements IGenericOperationConnector {
	
	private Connector connector_;
	
	public GenericOperationConnector(Connector connector) {
		connector_ = connector;
	}

	@Override
	public void setOnOperationResponseCallback(IOperationRequesterCallback operationCallBack)
			throws HypervisorException {
		connector_.setOnOperationResponseCallback(operationCallBack);
	}

	@Override
	public void requestOperation(AbstractOperationRequestType operationRequest)
			throws HypervisorException {
		connector_.requestOperation(operationRequest);
	}

	@Override
	public void requestOperation(UUID correlationID, AbstractOperationRequestType operationRequest)
			throws HypervisorException {
		connector_.requestOperation(correlationID, operationRequest);
	}

	@Override
	public IConnectorTools getTools() {
		return connector_;
	}
	
	public AbstractOperationRequestType createOperation(String javaClassName, 
			Map<String, String> operationParam) throws HypervisorException
	{ 
		AbstractOperationRequestType operationRequest = null;
		try {
			operationRequest = getTools().getOperationHelper().createOperationRequest(javaClassName, operationParam);
			
			if (operationRequest.getId() == null) {
				operationRequest.setId(UUID.randomUUID().toString());
			}

			long currentTimeStamp = DateUtils.getCurrentTimestamp();	
			operationRequest.setTimestamp(currentTimeStamp);
			
			if (operationRequest.getOperatorID() == null) {
				operationRequest.setOperatorID("scsconnector");
			}
			
		} catch (HypervisorConversionException e) {
			throw new HypervisorException("Error creating operation request");
		}
		
		return operationRequest;
	}

}
