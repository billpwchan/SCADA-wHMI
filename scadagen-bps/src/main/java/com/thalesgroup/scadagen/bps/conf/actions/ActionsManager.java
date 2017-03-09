package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionsManager implements IActionsManager {

	 /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionsManager.class);
    
	/** action handler map */
    private Map<String, IAction> actionsMap_;

    /** default configuration path */
    public static final String DEFAULT_CONFIGURATION_FILE = "bpsActionConfig/actionHandlers.xml";
    
    private static ActionsManager instance_;

    /**
     * Constructs a {@link IAction} manager with the default
     * configuration file path {@link ComputersManager#DEFAULT_CONFIGURATION_FILE}.
     */
    public ActionsManager() {
        this(DEFAULT_CONFIGURATION_FILE);
    }

    /**
     * Constructs a {@link IAction} manager.
     * @param xmlRelativePath  the relative path of the configuration file.
     */
    public ActionsManager(final String xmlRelativePath) {
        final ActionsConfigurationLoader configurationLoader = new ActionsConfigurationLoader();
        final ActionsDiscoverer discoverer = new ActionsDiscoverer(configurationLoader);
        final ActionsLoader loader = new ActionsLoader(discoverer);
        actionsMap_ = loader.loadActions(xmlRelativePath);
    }
    
    /**
     * Get the unique instance.
     * @return the unique instance.
     */
    public static synchronized IActionsManager getInstance() {

        if (instance_ == null) {
            instance_ = new ActionsManager();
        }

        return instance_;

    }

    @Override
    public IAction getAction(final String actionId) {
        if (!actionsMap_.containsKey(actionId)) {
            LOGGER.error("The action ["
                + actionId + "] was not found, check the action declaration file ["
                + DEFAULT_CONFIGURATION_FILE + "].");
        }
        return actionsMap_.get(actionId);
    }

}
