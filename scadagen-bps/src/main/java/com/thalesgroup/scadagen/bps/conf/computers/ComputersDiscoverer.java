package com.thalesgroup.scadagen.bps.conf.computers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.data.tools.finder.FinderSpringImpl;
import com.thalesgroup.hv.data.tools.finder.IFinder;

public class ComputersDiscoverer {

	/** This is a logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ComputersDiscoverer.class);

    /** configuration loader */
    private ComputersConfigurationLoader configurationLoader_;

    /**
     * Create a status computer discoverer.
     * @param configurationLoader the class able to read the configuration of computers.
     */
    public ComputersDiscoverer(final ComputersConfigurationLoader configurationLoader) {
        configurationLoader_ = configurationLoader;
    }

    /**
     * Get the computers class.
     * @param xmlRelativePath the configuration file relative path.
     * @return the {@link Set} of discovered implementation of {@link IComputer}.
     */
    public Set<Class< ? extends IComputer>> discoverComputers(final String xmlRelativePath) {
        LOGGER.debug("Looking for computers.");

        // to return
        final Set<Class< ? extends IComputer>> toReturn = new HashSet<Class< ? extends IComputer>>();

        // get the package names
        final Set<String> packageNames = configurationLoader_.readConfiguration(xmlRelativePath);

        // cannot be emtpy by xsd validation
        if (!packageNames.isEmpty()) {

            LOGGER.trace("Looking for computers in pacakge(s): " + packageNames);

            final Set<String> computerClassNameSet = findComputerNamesInPackages(packageNames);

            for (final String computerClassName : computerClassNameSet) {

                try {

                    final Class< ? extends IComputer> computerClass = getComputerClassFromName(computerClassName);

                    toReturn.add(computerClass);

                } catch (final HypervisorException e) {
                    LOGGER.error("Error while trying to load the class ["
                            + computerClassName + "] as an computer.", e);
                }

            }

        } else {
            LOGGER.debug("No package to look in.");
        }

        // add support of service loader to find the computer (using the classpath of the computer seems the only
        // working solution
        final ServiceLoader<IComputer> loader = ServiceLoader.load(IComputer.class,
            IComputer.class.getClassLoader());
        final Iterator<IComputer> iterator = loader.iterator();
        while (iterator.hasNext()) {
            boolean found = false;
            final Class< ? extends IComputer> candidate = iterator.next().getClass();
            for (final Class< ? extends IComputer> clazz : toReturn) {
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
     * Get an computer class from its name.
     * @param className the name of an computer class, not null, not empty
     * @return the corresponding class.
     * @throws HypervisorException if the class is not found or not an computer.
     */
    @SuppressWarnings({ "unchecked", "static-method" })
    private Class< ? extends IComputer> getComputerClassFromName(final String className) throws HypervisorException {

        try {
            // get class
            final Class< ? > candidate = Class.forName(className);

            if (IComputer.class.isAssignableFrom(candidate)) {
                return (Class< ? extends IComputer>) candidate;
            }

            throw new HypervisorException("The class [" + className + "] is not an computer.");

        } catch (final ClassNotFoundException e) {
            throw new HypervisorException("Cannot load the computer [" + className + "].", e);
        }

    }

    /**
     * findComputerNamesInPackages
     * @param packageNames not null set of package name (can be empty)
     * @return a set of computer names or an empty set.
     */
    @SuppressWarnings("static-method")
    private Set<String> findComputerNamesInPackages(final Set<String> packageNames) {

        // create class finder
        final IFinder finder = new FinderSpringImpl();

        // packageNames cannot be null
        final String[] packageArray = packageNames.toArray(new String[packageNames.size()]);
        final Set<String> concreteClassNames = finder.findConcreteClasses(packageArray, IComputer.class);

        return concreteClassNames;
    }

}
