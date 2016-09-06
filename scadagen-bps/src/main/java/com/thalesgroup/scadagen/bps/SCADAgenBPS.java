package com.thalesgroup.scadagen.bps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.conf.ConfManager;
import com.thalesgroup.scadagen.bps.conf.HvOperationConfigLoader;
import com.thalesgroup.scadagen.bps.conf.actions.ActionsManager;
import com.thalesgroup.scadagen.bps.conf.actions.IActionsManager;
import com.thalesgroup.scadagen.bps.connector.operation.GenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.GenericSubscriptionConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;
import com.thalesgroup.scadagen.bps.data.ConfiguredEntityStatusesManager;
import com.thalesgroup.scadagen.bps.data.EntityManagerAbstract;
import com.thalesgroup.scadagen.bps.data.TransientEntityManager;
import com.thalesgroup.scadagen.bps.datasource.DataSourceAbstract;

public class SCADAgenBPS {
	private static final Logger LOGGER = LoggerFactory.getLogger(SCADAgenBPS.class);

	private IGenericOperationConnector operationConnector_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IConnectorTools connectorTools_;
	
	private ConfManager confManager_;
	
	IActionsManager actionsManager_ = null;
	
	HvOperationConfigLoader hvOperationConfigLoader_ = null;

	public SCADAgenBPS(Connector connector) {
		operationConnector_ = new GenericOperationConnector(connector);
		subscriptionConnector_ = new GenericSubscriptionConnector(connector);
		connectorTools_ = connector;
	}

	public void init() throws BPSException {
		LOGGER.debug("BPS: loading configuration");
		
		actionsManager_ = ActionsManager.getInstance();

		try {

			confManager_ = new ConfManager(this, getSubscriptionConnector(), getOperationConnector(), getTools());	
			
			if (confManager_ != null) {
				confManager_.loadConfig();
			}

		} catch (HypervisorException e) {
			throw new BPSException(e);
		}
	}

	private IGenericOperationConnector getOperationConnector() {
		return operationConnector_;
	}

	public IGenericSubscriptionConnector getSubscriptionConnector() {
		return subscriptionConnector_;
	}

	public IConnectorTools getTools() {
		return connectorTools_;
	}

	public void start() throws BPSException {
		LOGGER.trace("start: BPS");

		try {
			if (confManager_ == null) {
				throw new BPSException("Error starting BPS. ConfManager is not initialized");
			}
			for (DataSourceAbstract<?> dataSource : confManager_.getDataSources()) {
				dataSource.startSubscriptionProtected();
			}
		} catch (HypervisorException e) {
			throw new BPSException(e);
		}
	}

	public void stop() throws BPSException {
		LOGGER.trace("stop: BPS");

		try {
			if (confManager_ == null) {
				throw new BPSException("Error stopping BPS. ConfManager is not initialized");
			}
			for (DataSourceAbstract<?> dataSource : confManager_.getDataSources()) {
				dataSource.stopProtectedSubscription();
			}
		} catch (HypervisorException e) {
			throw new BPSException(e);
		}
	}

	public EntityManagerAbstract<?> createCfgEntitiesManager() {
		ConfiguredEntityStatusesManager manager = new ConfiguredEntityStatusesManager(subscriptionConnector_, operationConnector_);
		return manager;
	}

	public EntityManagerAbstract<?> createTransientEntitiesManager() {
		TransientEntityManager manager = new TransientEntityManager(subscriptionConnector_, operationConnector_);
		return manager;
	}
}
