package com.thalesgroup.scadagen.bps.connector.operation;

import java.util.UUID;

import com.thalesgroup.hv.data_v1.operation.AbstractOperationResponseType;
import com.thalesgroup.hv.sdk.connector.api.operation.requester.IOperationRequesterCallback;

/**
 *
 *
 */
public class VoidOperationCallback implements IOperationRequesterCallback {

	/**
	 * 
	 */
	public VoidOperationCallback() {
	}

	/**
	 * 
	 */
	@Override
	public void onOperationResponseReception(UUID uuid, AbstractOperationResponseType list) {
	}

}
