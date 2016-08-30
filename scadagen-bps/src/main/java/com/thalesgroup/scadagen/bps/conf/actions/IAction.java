package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.Set;

import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public interface IAction {
	
	void execute(IGenericOperationConnector operationConnector, String actionConfigId, Set<AbstractEntityStatusesType> entities);

	String getActionId();
}
