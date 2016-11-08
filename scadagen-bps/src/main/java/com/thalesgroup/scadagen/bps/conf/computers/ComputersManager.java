package com.thalesgroup.scadagen.bps.conf.computers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputersManager {

	 /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputersManager.class);
    
	/** computer handler map */
    private Map<String, IComputer> computersMap_;

    /** default configuration path */
    public static final String DEFAULT_CONFIGURATION_FILE = "bpsConfig/computers.xml";
    
    private static ComputersManager instance_;

    /**
     * Constructs a {@link Icomputer} manager with the default
     * configuration file path {@link ComputersManager#DEFAULT_CONFIGURATION_FILE}.
     */
    public ComputersManager() {
        this(DEFAULT_CONFIGURATION_FILE);
    }

    /**
     * Constructs a {@link Icomputer} manager.
     * @param xmlRelativePath  the relative path of the configuration file.
     */
    public ComputersManager(final String xmlRelativePath) {
        final ComputersConfigurationLoader configurationLoader = new ComputersConfigurationLoader();
        final ComputersDiscoverer discoverer = new ComputersDiscoverer(configurationLoader);
        final ComputersLoader loader = new ComputersLoader(discoverer);
        computersMap_ = loader.loadComputers(xmlRelativePath);
    }
    
    /**
     * Get the unique instance.
     * @return the unique instance.
     */
    public static synchronized ComputersManager getInstance() {

        if (instance_ == null) {
            instance_ = new ComputersManager();
        }

        return instance_;

    }

    public IComputer getComputer(final String computerId) {
        if (!computersMap_.containsKey(computerId)) {
            LOGGER.error("The computer ["
                + computerId + "] was not found, check the computer declaration file ["
                + DEFAULT_CONFIGURATION_FILE + "].");
        }
        return computersMap_.get(computerId);
    }

}
