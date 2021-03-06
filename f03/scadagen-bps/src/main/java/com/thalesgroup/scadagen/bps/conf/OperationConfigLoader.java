package com.thalesgroup.scadagen.bps.conf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.scadagen.bps.conf.operation.Operation;
import com.thalesgroup.scadagen.bps.conf.operation.OperationConfig;

public class OperationConfigLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(OperationConfigLoader.class);
	private static OperationConfigLoader instance_;
	private MarshallersPool marshallersPool_;
	private Map<String, Long> resourceLastModifyMap_;
	private Map<String, Resource> resourceMap_;
	private Map<String, String> operationToResourceMap_;
	private Map<String, Operation> operationMap_;

	/** configuration object package name */
    private static final String CONF_PACKAGE_NAME = OperationConfig.class.getPackage().getName();

    /** configuration xsd relative path */
    private static final String COMMON_XSD_PATH = "xsd/common.xsd";
    private static final String XSD_RELATIVE_PATH = "xsd/operationAction.xsd";

    private static final String FILE_NAME_PATTERN = "classpath*:bpsActionConfig/*operation*.xml";

	public static synchronized OperationConfigLoader getInstance() throws HypervisorException {
		if (instance_ == null) {
			instance_ = new OperationConfigLoader();
		}
		return instance_;
	}

	protected OperationConfigLoader() throws HypervisorException {
		initializeMarshaller();
		if (marshallersPool_ != null) {
			initConfigurations();
		} else {
			LOGGER.error(
					"The configuration loader cannot be initialized. The marshaller pool is null. Check previous log for an error.");
		}
	}

	private void initializeMarshaller() throws HypervisorException {

		List<String> schemas = new ArrayList<String>();
		schemas.add(COMMON_XSD_PATH);
		schemas.add(XSD_RELATIVE_PATH);

		LOGGER.trace("Loaded xsd for the configuration [{}].", schemas);

		List<String> context = new ArrayList<String>();
		context.add(CONF_PACKAGE_NAME);
		LOGGER.trace("Context for the configuration [{}].", context);

		String buildContextPath = MarshallersPool.buildContextPath(context);

		marshallersPool_ = new MarshallersPool(buildContextPath, schemas);
	}

	private void initConfigurations() {
		resourceLastModifyMap_ = new HashMap<String, Long>();
		resourceMap_ = new HashMap<String, Resource>();
		operationToResourceMap_ = new HashMap<String, String>();
		operationMap_ = new HashMap<String, Operation>();
		
		LOGGER.debug("init HvOperationConfigLoader");

		ResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = pathResolver.getResources(FILE_NAME_PATTERN);
			
			if (LOGGER.isTraceEnabled()) {
				for (Resource res: resources) {
					LOGGER.trace("Found operation config file [{}]", res.getURL().getPath());
				}
			}

			for (Resource resource : resources) {
				LOGGER.trace("loading config file resource [{}]", resource.getFilename());
				OperationConfig config = null;

				try {
					config = read(resource);
				} catch (HypervisorConversionException e) {
					LOGGER.error("An error occurred while reading configuration files. [{}]", e);
				} catch (IOException e) {
					LOGGER.error("An error occurred while reading configuration files. [{}]", e);
				}

				if (config != null && config.getOperation() != null) {
					resourceLastModifyMap_.put(config.getOperationConfigName(),
							Long.valueOf(resource.lastModified()));
					resourceMap_.put(config.getOperationConfigName(), resource);

					List<Operation> operationList = config.getOperation();
					for (Operation operation : operationList) {
						operationMap_.put(operation.getOperationName(), operation);
						operationToResourceMap_.put(operation.getOperationName(),
								config.getOperationConfigName());
					}
				} else {
					LOGGER.error("operation config is null");
				}
			}
		} catch (IOException e) {
			LOGGER.error("An error occurred while looking for configuration files. [{}]", e);
		}
	}

	private OperationConfig read(Resource resource) throws HypervisorConversionException, IOException {
		OperationConfig conf = (OperationConfig) marshallersPool_.unmarshal(resource.getURL());

		return conf;
	}

	public Operation getOperation(String operationName) {

		if (operationMap_.containsKey(operationName)) {
			String configName = null;
			try {
				if (operationToResourceMap_.containsKey(operationName)) {
					configName = operationToResourceMap_.get(operationName);
				}
				if (configName != null && ((Resource) resourceMap_.get(configName)).lastModified() > 
						((Long) resourceLastModifyMap_.get(configName)).longValue()) {
					LOGGER.debug("The configuration [" + configName + "] need to be refresh due to a modification.");
					try {
						refreshConf(configName);
					} catch (Exception e) {
						LOGGER.error("Error while refreshing the configuration [" + configName
								+ "]. The old version will be used.", e);
					}
				}
			} catch (IOException e) {
				LOGGER.error("Error while getting the last modified time of the configuration [" + configName
						+ "] the config couldn't be up to date.", e);
			}
		} else {
			LOGGER.debug("The [" + operationName + "] is unknown, the configuration files will be reload.");
			initConfigurations();
		}
		return operationMap_.get(operationName);
	}

	private void refreshConf(String configurationName) throws HypervisorConversionException, IOException {
		Resource resource = (Resource) resourceMap_.get(configurationName);

		resourceMap_.remove(configurationName);
		resourceLastModifyMap_.remove(configurationName);
		for (String operationName: operationToResourceMap_.keySet()) {
			if (operationToResourceMap_.get(operationName).equals(configurationName)) {
				operationMap_.remove(operationName);
				operationToResourceMap_.remove(operationName);
			}
		}


		OperationConfig config = read(resource);
		if (config != null && config.getOperation() != null) {
			resourceLastModifyMap_.put(config.getOperationConfigName(),
					Long.valueOf(resource.lastModified()));
			resourceMap_.put(config.getOperationConfigName(), resource);

			List<Operation> operationList = config.getOperation();
			for (Operation operation : operationList) {
				operationMap_.put(operation.getOperationName(), operation);
				operationToResourceMap_.put(operation.getOperationName(),
						config.getOperationConfigName());
			}
		} else {
			LOGGER.error("operation config is null");
		}
	}
}
