package com.thalesgroup.scadagen.bps.conf;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.configuration.NotificationEntityBuilder;
import com.thalesgroup.hv.sdk.connector.Connector;
import com.thalesgroup.hv.sdk.connector.IConnectorTools;
import com.thalesgroup.scadagen.bps.SCADAgenBPS;
import com.thalesgroup.scadagen.bps.conf.binding.builder.AttributeBuilder;
import com.thalesgroup.scadagen.bps.conf.binding.engine.BindingLoader;
import com.thalesgroup.scadagen.bps.conf.binding.engine.Hv2ScsBindingEngine;
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
	
	private Connector connector_;

	private IGenericSubscriptionConnector subscriptionConnector_;

	private IGenericOperationConnector operationConnector_;

	private IConnectorTools iConnectorTools_;
	
	private OperationConfigLoader hvOperationConfigLoader_;
	
	private BpsConfLoader configLoader_;
	
	private static Hv2ScsBindingEngine bindingEngine_;
	
	private static Hv2ScsLoader scs2hvLoader_;

	public ConfManager(SCADAgenBPS bps, Connector connector,
			IGenericSubscriptionConnector subscriptionConnector, IGenericOperationConnector operationConnector,
			IConnectorTools iConnectorTools) throws HypervisorException {
		bps_ = bps;
		connector_ = connector;
		subscriptionConnector_ = subscriptionConnector;
		operationConnector_ = operationConnector;
		iConnectorTools_ = iConnectorTools;
	}
	
	public void loadConfig() throws HypervisorException {
		
		// load scs2hv
		scs2hvLoader_ = new Hv2ScsLoader("bpsConfig/hv2scs.xml");
		scs2hvLoader_.loadConfiguration();
		
		// load bindings
        final BindingLoader loader = new BindingLoader(connector_) {
            @Override
            protected String resolveBinding(String entityIdentifier) {
            	String type = scs2hvLoader_.getScsEqpOfHVInstancesMap().get(entityIdentifier);
            	if (type == null) {
            		type = entityIdentifier;
            	}
            	return type;
            }
        };
        bindingEngine_ = null;
        try {
	        loader.readBinding("bpsConfig/bindingHv2Scs.xml");
	        final NotificationEntityBuilder builder = new NotificationEntityBuilder(connector_.getSystemConfiguration(), connector_.getDataHelper());
	        bindingEngine_ = new Hv2ScsBindingEngine(builder, loader, connector_.getDataHelper(), new AttributeBuilder(connector_.getDataHelper()));
        } catch (HypervisorConversionException e) {
        	LOGGER.warn("SCADAgen BA - no bindings defined");
        }
        
		hvOperationConfigLoader_ = OperationConfigLoader.getInstance();
		
		configLoader_ = BpsConfLoader.getInstance();
	}
	
	public static Hv2ScsBindingEngine getBindingEngine() {
		return bindingEngine_;
	}
	
	public static Hv2ScsLoader getScs2HvLoader() {
		return scs2hvLoader_;
	}

	private BpsConfig getConfig(String configurationId) throws HypervisorException {
		LOGGER.trace("Get BPS configuration [{}].", configurationId);

		BpsConfig cfg = BpsConfLoader.getInstance().getConfiguration(configurationId);

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
