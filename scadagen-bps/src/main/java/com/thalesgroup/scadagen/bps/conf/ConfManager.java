package com.thalesgroup.scadagen.bps.conf;

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

public class ConfManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfManager.class);

	private SCADAgenBPS bps_;

	private BpsConfig config_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IGenericOperationConnector operationConnector_;

	private IConnectorTools iConnectorTools_;

	public ConfManager(SCADAgenBPS bps,
			IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector,
			IConnectorTools iConnectorTools) throws HypervisorException {
		bps_ = bps;
		subscriptionConnector_ = subscriptionConnector;
		operationConnector_ = operationConnector;
		iConnectorTools_ = iConnectorTools;
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
		config_ = getConfig(configId);
		DataSource dataSourceConf = config_.getSubject().getDataSource();

		DataSourceAbstract<? extends DataSource> dataSource = ConfLoader.getInstance().getDataSource(dataSourceConf);

		dataSource.setBPS(bps_);
		dataSource.setSubscriptionConnector(subscriptionConnector_);
		dataSource.setOperationConnector(operationConnector_);
		dataSource.setConnectorTools(iConnectorTools_);

		dataSource.initProtected(config_, dataSourceConf);

		return dataSource;
	}
}
