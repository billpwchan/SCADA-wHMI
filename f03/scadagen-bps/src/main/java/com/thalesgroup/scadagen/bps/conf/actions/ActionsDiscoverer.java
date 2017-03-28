package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.tools.finder.FinderSpringImpl;
import com.thalesgroup.hv.data.tools.finder.IFinder;

public class ActionsDiscoverer {

	/** This is a logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ActionsDiscoverer.class);

    /** configuration loader */
    private ActionsConfigurationLoader configurationLoader_;

    /**
     * Create a status action discoverer.
     * @param configurationLoader the class able to read the configuration of actions.
     */
    public ActionsDiscoverer(final ActionsConfigurationLoader configurationLoader) {
        configurationLoader_ = configurationLoader;
    }

    /**
     * Get the actions class.
     * @param xmlRelativePath the configuration file relative path.
     * @return the {@link Set} of discovered implementation of {@link IAction}.
     */
    public Set<Class< ? extends IAction>> discoverActions(final String xmlRelativePath) {
        LOGGER.debug("Looking for actions.");

        // to return
        final Set<Class< ? extends IAction>> toReturn = new HashSet<Class< ? extends IAction>>();

        // get the package names
        final Set<String> packageNames = configurationLoader_.readConfiguration(xmlRelativePath);

        // cannot be emtpy by xsd validation
        if (!packageNames.isEmpty()) {

            LOGGER.trace("Looking for actions in pacakge(s): " + packageNames);

            final Set<String> actionClassNameSet = findActionNamesInPackages(packageNames);

            for (final String actionClassName : actionClassNameSet) {

                try {

                    final Class< ? extends IAction> actionClass = getActionClassFromName(actionClassName);

                    toReturn.add(actionClass);

                } catch (final HypervisorException e) {
                    LOGGER.error("Error while trying to load the class ["
                            + actionClassName + "] as an action.", e);
                }

            }

        } else {
            LOGGER.debug("No package to look in.");
        }

        // add support of service loader to find the action (using the classpath of the action seems the only
        // working solution
        final ServiceLoader<IAction> loader = ServiceLoader.load(IAction.class,
            IAction.class.getClassLoader());
        final Iterator<IAction> iterator = loader.iterator();
        while (iterator.hasNext()) {
            boolean found = false;
            final Class< ? extends IAction> candidate = iterator.next().getClass();
            for (final Class< ? extends IAction> clazz : toReturn) {
                //use class name comparison as we need to check if the classes are not found through different
                //class loaders as two techniques are used
                if (clazz.getName().equals(candidate.getName())) {
                    found = true;
                    break;
                }
            }
            //avoid duplicate classes
            if (!found) {
                toReturn.add(candidate);
            }
        }
        return toReturn;
    }

    /**
     * Get an action class from its name.
     * @param className the name of an action class, not null, not empty
     * @return the corresponding class.
     * @throws HypervisorException if the class is not found or not an action.
     */
    @SuppressWarnings({ "unchecked", "static-method" })
    private Class< ? extends IAction> getActionClassFromName(final String className) throws HypervisorException {

        try {
            // get class
            final Class< ? > candidate = Class.forName(className);

            if (IAction.class.isAssignableFrom(candidate)) {
                return (Class< ? extends IAction>) candidate;
            }

            throw new HypervisorException("The class [" + className + "] is not an action.");

        } catch (final ClassNotFoundException e) {
            throw new HypervisorException("Cannot load the action [" + className + "].", e);
        }

    }

    /**
     * findActionNamesInPackages
     * @param packageNames not null set of package name (can be empty)
     * @return a set of action names or an empty set.
     */
    @SuppressWarnings("static-method")
    private Set<String> findActionNamesInPackages(final Set<String> packageNames) {

        // create class finder
        final IFinder finder = new FinderSpringImpl();

        // packageNames cannot be null
        final String[] packageArray = packageNames.toArray(new String[packageNames.size()]);
        final Set<String> concreteClassNames = finder.findConcreteClasses(packageArray, IAction.class);

        return concreteClassNames;
    }

}
