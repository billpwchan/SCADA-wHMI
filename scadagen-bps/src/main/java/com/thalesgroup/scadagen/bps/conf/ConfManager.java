package com.thalesgroup.scadagen.bps.conf;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.common.DataSource;
import com.thalesgroup.scadagen.bps.connector.operation.IGenericOperationConnector;
import com.thalesgroup.scadagen.bps.connector.subscription.IGenericSubscriptionConnector;
import com.thalesgroup.scadagen.bps.datasource.DataSourceAbstract;
import com.thalesgroup.scadagen.bps.datasource.SubscriptionDataSourceImpl;

public class ConfManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfManager.class);

	private SCADAgenBPS bps_;

	//private BpsConfig config_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IGenericOperationConnector operationConnector_;

	private IConnectorTools iConnectorTools_;
	
	private HvOperationConfigLoader hvOperationConfigLoader_;
	
	private ConfLoader configLoader_;

	public ConfManager(SCADAgenBPS bps,
			IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector,
			IConnectorTools iConnectorTools) throws HypervisorException {
		bps_ = bps;
		subscriptionConnector_ = subscriptionConnector;
		operationConnector_ = operationConnector;
		iConnectorTools_ = iConnectorTools;
	}
	
	public void loadConfig() throws HypervisorException {
		
		hvOperationConfigLoader_ = HvOperationConfigLoader.getInstance();
		
		configLoader_ = ConfLoader.getInstance();
	}

	private BpsConfig getConfig(String configurationId) throws HypervisorException {
		LOGGER.trace("Get BPS configuration [{}].", configurationId);

		 BpsConfig cfg = ConfLoader.getInstance().getConfiguration(configurationId);

		 if (cfg == null) {
			 throw new HypervisorException("Cannot get the configuration for the id [" + configurationId + "].");
		 }

		return cfg;
	}

	public DataSourceAbstract<? extends DataSource> getDataSource(String configId) throws HypervisorException {
		BpsConfig config = getConfig(configId);
		Set<DataSource> dataSourceConf = new HashSet(config.getSubject().getDataSource());

		DataSourceAbstract<? extends DataSource> dataSource = new SubscriptionDataSourceImpl();

		if (dataSource != null) {
			dataSource.setBPS(bps_);
			dataSource.setSubscriptionConnector(subscriptionConnector_);
			dataSource.setOperationConnector(operationConnector_);
			dataSource.setConnectorTools(iConnectorTools_);
	
			dataSource.initProtected(config, dataSourceConf);
		}

		return dataSource;
	}
	
	public Set<DataSourceAbstract<? extends DataSource>> getDataSources() throws HypervisorException {
		Set<DataSourceAbstract<? extends DataSource>> dataSources = new HashSet();
		for (String configName: configLoader_.getConfigNameSet()) {
			dataSources.add(getDataSource(configName));
		}
		return dataSources;
	}
}
