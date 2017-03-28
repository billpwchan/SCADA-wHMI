package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;

public class ActionsLoader {

	/** limit number of input */
    //private static final int INPUT_LIMIT = 20;

    /** This is a logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ActionsLoader.class);

    /** action class discoverer */
    private ActionsDiscoverer discoverer_;

    /**
     * Create an actions loader.
     * @param discoverer the instance of {@link ActionsDiscoverer} able to find the action.
     */
    public ActionsLoader(final ActionsDiscoverer discoverer) {
        discoverer_ = discoverer;
    }

    /**
     * Get map of valid instance of actions indexed by their id.
     * @param xmlRelativePath the xml relative path of the actions configuration file.
     * @return the map of valid instance of actions indexed by their id.
     */
    public Map<String, IAction> loadActions(final String xmlRelativePath) {

        final Map<String, IAction> actionsMap = new HashMap<String, IAction>();

        final Set<Class< ? extends IAction>> discoveredActions = discoverer_.discoverActions(xmlRelativePath);

        for (final Class< ? extends IAction> actionClass : discoveredActions) {

            // Instantiate the action
            try {

                final IAction actionInstance = actionClass.newInstance();

                // add the action if valid
                addIfValid(actionsMap, actionInstance);

            } catch (final InstantiationException e) {
                LOGGER.error("Cannot instantiate the action for class [" + actionClass
                        + "]. A action must only have a empty constructor.", e);
            } catch (final IllegalAccessException e) {
                LOGGER.error("Cannot instantiate the action for class [" + actionClass + "].", e);
            }
        }

        if (discoveredActions.isEmpty()) {
            LOGGER.info("No action to load.");
        } else {
            LOGGER.info("[{}] action(s) to load. [{}] valid, [{}] in error.",
                    new Object[]{discoveredActions.size(), actionsMap.size(),
                        discoveredActions.size() - actionsMap.size()});


            if (LOGGER.isDebugEnabled()) {
                final StringBuilder sb = new StringBuilder("Action loaded:");
                for (final Entry<String, IAction> actionEntry : actionsMap.entrySet()) {
                    sb.append('\n');
                    sb.append(actionEntry.getKey());
                    sb.append(" : ");
                    sb.append(actionEntry.getValue().getClass().getCanonicalName());
                }

                LOGGER.debug(sb.toString());
            }

        }

        return actionsMap;
    }

    private void addIfValid(final Map<String, IAction> actionsMap, final IAction actionInstance) {

        // use exception because there is no performance purpose, it's more readable and it allow to write only one log
        try {

            checkComputerId(actionInstance);

            checkUnicity(actionsMap, actionInstance);

            //checkInput(actionInstance);

            actionsMap.put(actionInstance.getActionId(), actionInstance);

        } catch (final HypervisorException e) {
            LOGGER.error("The action [" + actionInstance.getClass() + "] is not valid, it will not be added. "
                    + e.getMessage());
        }

    }

    @SuppressWarnings("static-method")
    private void checkComputerId(final IAction actionInstance)
        throws HypervisorException {

        if (actionInstance.getActionId() == null) {
            throw new HypervisorException("Id is null.");
        }

        if ("".equals(actionInstance.getActionId())) {
            throw new HypervisorException("Id is empty.");
        }
    }

    @SuppressWarnings("static-method")
    private void checkUnicity(final Map<String, IAction> actionsMap,
            final IAction actionInstance) throws HypervisorException {
        if (actionsMap.containsKey(actionInstance.getActionId())) {
            throw new HypervisorException("A action with the id ["
                    + actionInstance.getActionId() + "] already exists.");
        }
    }

}
