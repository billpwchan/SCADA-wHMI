package com.thalesgroup.scadagen.bps.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.scadagen.bps.conf.bps.BpsConfig;
import com.thalesgroup.scadagen.bps.conf.common.DataSource;
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;
import com.thalesgroup.scadagen.bps.datasource.DataSourceAbstract;
import com.thalesgroup.scadagen.bps.datasource.SubscriptionDataSourceImpl;

public final class ConfLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfLoader.class);

	private static ConfLoader instance_;

	private MarshallersPool marshallersPool_;

	private Map<String, Long> lastModifyMap_;

	private Map<String, Resource> resourceMap_;

	private Map<String, BpsConfig> confMap_;

	private static final String FILE_NAME_PATTERN = "classpath*:bpsConfig/bps*.xml";


	public static synchronized ConfLoader getInstance() throws HypervisorException {
		if (instance_ == null) {
			instance_ = new ConfLoader();
		}
		return instance_;
	}

	protected ConfLoader() throws HypervisorException {

		initializeMarshaller();

		if (this.marshallersPool_ != null) {
			initConfigurations();
		} else {
			LOGGER.error(
					"The BPS configuration loader cannot be initialized. The marshaller pool is null. Check previous log for an error.");
		}
	}

	private void initializeMarshaller() throws HypervisorException {

		List<String> schemas = new ArrayList<String>();
		schemas.add("xsd/common.xsd");
		schemas.add("xsd/subscription-data-source-config.xsd");
		schemas.add("xsd/bps-config.xsd");

		LOGGER.trace("Loaded xsd for the bps configuration [{}].", schemas);

		List<String> context = new ArrayList<String>();
		context.add(SubscriptionDataSource.class.getPackage().getName());
		context.add(BpsConfig.class.getPackage().getName());
		LOGGER.trace("Context for the bps configuration [{}].", context);

		String buildContextPath = MarshallersPool.buildContextPath(context);

		this.marshallersPool_ = new MarshallersPool(buildContextPath, schemas);
	}

	private void initConfigurations() {
		this.lastModifyMap_ = new HashMap<String, Long>();
		this.resourceMap_ = new HashMap<String, Resource>();
		this.confMap_ = new HashMap<String, BpsConfig>();

		ResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = pathResolver.getResources(FILE_NAME_PATTERN);

			for (Resource resource : resources) {
				try {
					BpsConfig config = read(resource);
					LOGGER.trace("Read resource {}", resource.getFilename());

					this.confMap_.put(config.getBpsConfiguration().getName(), config);
					this.lastModifyMap_.put(config.getBpsConfiguration().getName(), Long.valueOf(resource.lastModified()));
					this.resourceMap_.put(config.getBpsConfiguration().getName(), resource);
				} catch (HypervisorConversionException e) {
					LOGGER.error("Error while reading the BPS configuration file ["
							+ resource.getFilename() + "].", e);
				}

			}
		} catch (IOException e) {
			LOGGER.error("An error occurred while looking for BPS configuration files.", e);
		}
	}

	private BpsConfig read(Resource resource) throws HypervisorConversionException, IOException {
		BpsConfig conf = (BpsConfig) this.marshallersPool_.unmarshal(resource.getURL());

		return conf;
	}
	
	public Set<String> getConfigNameSet() {	
		return lastModifyMap_.keySet();
	}

	public BpsConfig getConfiguration(String configName) {
		if (this.lastModifyMap_.containsKey(configName)) {
			try {
				if (((Resource) this.resourceMap_.get(configName))
						.lastModified() > ((Long) this.lastModifyMap_.get(configName)).longValue()) {
					LOGGER.debug("The BPS configuration [" + configName
							+ "] need to be refresh due to a modification.");

					try {
						refreshConf(configName);
					} catch (Exception e) {
						LOGGER.error("Error while refreshing the BPS configuration [" + configName
								+ "]. The old version will be used.", e);
					}

				}

			} catch (IOException e) {
				LOGGER.error("Error while getting the last modified time of the BPS configuration ["
						+ configName + "] the config couldn't be up to date.", e);
			}
		} else {
			LOGGER.debug("The BPS [" + configName + "] is unknown, the configuration files will be reload.");
			initConfigurations();
		}

		return (BpsConfig) this.confMap_.get(configName);
	}

	private void refreshConf(String configurationName) throws HypervisorConversionException, IOException {
		Resource resource = (Resource) this.resourceMap_.get(configurationName);

		this.resourceMap_.remove(configurationName);
		this.confMap_.remove(configurationName);
		this.lastModifyMap_.remove(configurationName);

		BpsConfig configuration = read(resource);

		this.confMap_.put(configuration.getBpsConfiguration().getName(), configuration);
		this.lastModifyMap_.put(configuration.getBpsConfiguration().getName(), Long.valueOf(resource.lastModified()));
		this.resourceMap_.put(configuration.getBpsConfiguration().getName(), resource);
	}

	public DataSourceAbstract<? extends DataSource> getDataSource(DataSource dataSourceConf)
			throws HypervisorException {
		DataSourceAbstract<? extends DataSource> dataSource = new SubscriptionDataSourceImpl();
		return dataSource;
	}
}
