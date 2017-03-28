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
import com.thalesgroup.scadagen.bps.conf.subscription_data_source.SubscriptionDataSource;

public final class BpsConfLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(BpsConfLoader.class);

	private static BpsConfLoader instance_;

	private MarshallersPool marshallersPool_;

	private Map<String, Long> lastModifyMap_;

	private Map<String, Resource> resourceMap_;

	private Map<String, BpsConfig> confMap_;

	private static final String FILE_NAME_PATTERN = "classpath*:bpsConfig/bps*.xml";
	
//	private Map<String,String> hveqp2scsdbMap_ = new HashMap<String, String>();
//	
//	private Map<String,String> hveqp2scstypeMap_ = new HashMap<String, String>();


	public static synchronized BpsConfLoader getInstance() throws HypervisorException {
		if (instance_ == null) {
			instance_ = new BpsConfLoader();
		}
		return instance_;
	}

	protected BpsConfLoader() throws HypervisorException {

		initializeMarshaller();

		if (marshallersPool_ != null) {
			initConfigurations();
		} else {
			LOGGER.error(
					"The BPS configuration loader cannot be initialized. The marshaller pool is null. Check previous log for an error.");
		}
	}

	private void initializeMarshaller() throws HypervisorException {

		List<String> schemas = new ArrayList<String>();
		schemas.add("xsd/common.xsd");
		schemas.add("xsd/subscriptionDataSourceConfig.xsd");
		schemas.add("xsd/bpsConfig.xsd");

		LOGGER.trace("Loaded xsd for the bps configuration [{}].", schemas);

		List<String> context = new ArrayList<String>();
		context.add(SubscriptionDataSource.class.getPackage().getName());
		context.add(BpsConfig.class.getPackage().getName());
		LOGGER.trace("Context for the bps configuration [{}].", context);

		String buildContextPath = MarshallersPool.buildContextPath(context);

		marshallersPool_ = new MarshallersPool(buildContextPath, schemas);
	}

	private void initConfigurations() {
		lastModifyMap_ = new HashMap<String, Long>();
		resourceMap_ = new HashMap<String, Resource>();
		confMap_ = new HashMap<String, BpsConfig>();

		ResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = pathResolver.getResources(FILE_NAME_PATTERN);

			for (Resource resource : resources) {
				try {
					BpsConfig config = read(resource);
					LOGGER.trace("Read resource [{}]", resource.getFilename());

					confMap_.put(config.getBpsConfiguration().getName(), config);
					lastModifyMap_.put(config.getBpsConfiguration().getName(), Long.valueOf(resource.lastModified()));
					resourceMap_.put(config.getBpsConfiguration().getName(), resource);
				} catch (HypervisorConversionException e) {
					LOGGER.error("Error while reading the BPS configuration file ["
							+ resource.getFilename() + "].", e);
				}

			}
			
//			List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
//			mapList.add(hveqp2scsdbMap_);
//			mapList.add(hveqp2scstypeMap_);
//
//			readMapFromCsv("classpath:bpsConfig/hveqp2scsdb.csv", ",", mapList);
			
		} catch (IOException e) {
			LOGGER.error("An error occurred while looking for BPS configuration files.", e);
//		} catch (HypervisorException e) {
//			LOGGER.error("An error occurred while looking for BPS configuration files.", e);
		}
	}

	private BpsConfig read(Resource resource) throws HypervisorConversionException, IOException {
		BpsConfig conf = (BpsConfig) marshallersPool_.unmarshal(resource.getURL());

		return conf;
	}
	
	public Set<String> getConfigNameSet() {	
		return lastModifyMap_.keySet();
	}

	public BpsConfig getConfiguration(String configName) {
		if (lastModifyMap_.containsKey(configName)) {
			try {
				if (((Resource) resourceMap_.get(configName))
						.lastModified() > ((Long) lastModifyMap_.get(configName)).longValue()) {
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

		return (BpsConfig) confMap_.get(configName);
	}

	private void refreshConf(String configurationName) throws HypervisorConversionException, IOException {
		Resource resource = (Resource) resourceMap_.get(configurationName);

		resourceMap_.remove(configurationName);
		confMap_.remove(configurationName);
		lastModifyMap_.remove(configurationName);

		BpsConfig configuration = read(resource);

		confMap_.put(configuration.getBpsConfiguration().getName(), configuration);
		lastModifyMap_.put(configuration.getBpsConfiguration().getName(), Long.valueOf(resource.lastModified()));
		resourceMap_.put(configuration.getBpsConfiguration().getName(), resource);
	}

//	public DataSourceAbstract<? extends DataSource> getDataSource()
//			throws HypervisorException {
//		DataSourceAbstract<? extends DataSource> dataSource = new SubscriptionDataSourceImpl();
//		return dataSource;
//	}
	
//	public Map<String,String> getHveqp2scsdbMap() {
//		return hveqp2scsdbMap_;
//	}
//	
//	public Map<String,String> getHveqp2scstypeMap() {
//		return hveqp2scstypeMap_;
//	}
	
//	protected void readMapFromCsv(String csvFile, String delimiter, List<Map<String,String>> mapList) throws HypervisorException {
//		try {
//			ResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
//			Resource res = pathResolver.getResource(csvFile);
//
//			Scanner scanner = new Scanner(res.getFile());
//			if (scanner != null) {
//				if (delimiter != null && !delimiter.isEmpty()) {
//					scanner.useDelimiter(delimiter);
//				}
//				
//				while (scanner.hasNextLine()) {
//					String line = scanner.nextLine();
//					LOGGER.trace("csv line=[{}]", line);
//
//					String [] tokens = line.trim().split(delimiter);
//
//					String key = tokens[0].trim();
//					for (int i=1; i<tokens.length; i++) {
//						String value = tokens[i].trim();
//						LOGGER.trace("csv key=[{}] value=[{}]", key, value);
//						
//						if (key != null && value != null) {
//							mapList.get(i-1).put(key, value);
//						}
//					}
//
//				}
//			}
//		} catch (FileNotFoundException e) {
//			throw new HypervisorException(e);
//		} catch (Exception e) {
//			throw new HypervisorException(e);
//		}
//	}
}
