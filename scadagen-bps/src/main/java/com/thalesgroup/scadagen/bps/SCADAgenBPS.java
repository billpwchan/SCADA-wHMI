package com.thalesgroup.scadagen.bps;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.conf.ConfLoader;
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

	private Set<DataSourceAbstract<?>> dataSourceSet_;

	private IGenericOperationConnector operationConnector_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IConnectorTools connectorTools_;
	
	ConfLoader configLoader_ = null;
	
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
		
		dataSourceSet_ = new HashSet<DataSourceAbstract<?>>();

		try {
			hvOperationConfigLoader_ = HvOperationConfigLoader.getInstance();
			
			configLoader_ = ConfLoader.getInstance();

			ConfManager confManager = new ConfManager(this, getSubscriptionConnector(), getOperationConnector(), getTools());	
			
			for (String configId: configLoader_.getConfigNameSet()) {
				DataSourceAbstract<?> dataSource = confManager.getDataSource(configId);
				if (dataSource != null) {
					LOGGER.trace("Add dataSource {}", configId);
					dataSourceSet_.add(dataSource);
				}
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
			for (DataSourceAbstract<?> dataSource : dataSourceSet_) {
				dataSource.startProtected();
			}
		} catch (HypervisorException e) {
			throw new BPSException(e);
		}
	}

	public void stop() throws BPSException {
		LOGGER.trace("stop: BPS");

		try {
			for (DataSourceAbstract<?> dataSource : dataSourceSet_) {
				dataSource.stopProtected();
			}
		} catch (HypervisorException e) {
			throw new BPSException(e);
		}
	}

//	public DataSourceAbstract<?> getDataSource() {
//		return dataSource_;
//	}

//	public void updateData(GDGData data) {
//		
//	}
//
//	public String getConfigurationId() {
//		return configId_;
//	}

	public EntityManagerAbstract<?> createCfgEntitiesManager() {
		ConfiguredEntityStatusesManager manager = new ConfiguredEntityStatusesManager(subscriptionConnector_, operationConnector_);
		return manager;
	}

	public EntityManagerAbstract<?> createTransientEntitiesManager() {
		TransientEntityManager manager = new TransientEntityManager(subscriptionConnector_, operationConnector_);
		return manager;
	}
}
