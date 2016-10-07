package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.Set;

import com.thalesgroup.hv.data_v1.entity.AbstractEntityStatusesType;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.data.EntityDataDescriptionAbstract;

public interface IAction {
	
	void execute(IGenericOperationConnector operationConnector, Set<EntityDataDescriptionAbstract> desc, String actionConfigId, AbstractEntityStatusesType entity);

	String getActionId();
}
