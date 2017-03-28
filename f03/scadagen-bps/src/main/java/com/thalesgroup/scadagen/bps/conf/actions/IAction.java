package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.Map;

import com.thalesgroup.hv.data_v1.attribute.AbstractAttributeType;
import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;

public interface IAction {
	
	void execute(IGenericOperationConnector operationConnector, AbstractEntityStatusesType entity, Map<String, AbstractAttributeType> attributeMap, String actionConfigId);

	String getActionId();
}
