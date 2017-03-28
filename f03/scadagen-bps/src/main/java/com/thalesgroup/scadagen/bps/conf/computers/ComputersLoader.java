package com.thalesgroup.scadagen.bps.conf.computers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;

public class ComputersLoader {

	/** limit number of input */
    //private static final int INPUT_LIMIT = 20;

    /** This is a logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ComputersLoader.class);

    /** computer class discoverer */
    private ComputersDiscoverer discoverer_;

    /**
     * Create an computers loader.
     * @param discoverer the instance of {@link ComputersDiscoverer} able to find the computer.
     */
    public ComputersLoader(final ComputersDiscoverer discoverer) {
        discoverer_ = discoverer;
    }

    /**
     * Get map of valid instance of computers indexed by their id.
     * @param xmlRelativePath the xml relative path of the computers configuration file.
     * @return the map of valid instance of computers indexed by their id.
     */
    public Map<String, IComputer> loadComputers(final String xmlRelativePath) {

        final Map<String, IComputer> computersMap = new HashMap<String, IComputer>();

        final Set<Class< ? extends IComputer>> discoveredComputers = discoverer_.discoverComputers(xmlRelativePath);

        for (final Class< ? extends IComputer> computerClass : discoveredComputers) {

            // Instantiate the computer
            try {

                final IComputer computerInstance = computerClass.newInstance();

                // add the computer if valid
                addIfValid(computersMap, computerInstance);

            } catch (final InstantiationException e) {
                LOGGER.error("Cannot instantiate the computer for class [" + computerClass
                        + "]. A computer must only have a empty constructor.", e);
            } catch (final IllegalAccessException e) {
                LOGGER.error("Cannot instantiate the computer for class [" + computerClass + "].", e);
            }
        }

        if (discoveredComputers.isEmpty()) {
            LOGGER.info("No computer to load.");
        } else {
            LOGGER.info("[{}] computer(s) to load. [{}] valid, [{}] in error.",
                    new Object[]{discoveredComputers.size(), computersMap.size(),
                        discoveredComputers.size() - computersMap.size()});


            if (LOGGER.isDebugEnabled()) {
                final StringBuilder sb = new StringBuilder("Computer loaded:");
                for (final Entry<String, IComputer> computerEntry : computersMap.entrySet()) {
                    sb.append('\n');
                    sb.append(computerEntry.getKey());
                    sb.append(" : ");
                    sb.append(computerEntry.getValue().getClass().getCanonicalName());
                }

                LOGGER.debug(sb.toString());
            }

        }

        return computersMap;
    }

    private void addIfValid(final Map<String, IComputer> computersMap, final IComputer computerInstance) {

        // use exception because there is no performance purpose, it's more readable and it allow to write only one log
        try {

            checkComputerId(computerInstance);

            checkUnicity(computersMap, computerInstance);

            //checkInput(computerInstance);

            computersMap.put(computerInstance.getComputerId(), computerInstance);

        } catch (final HypervisorException e) {
            LOGGER.error("The computer [" + computerInstance.getClass() + "] is not valid, it will not be added. "
                    + e.getMessage());
        }

    }

    @SuppressWarnings("static-method")
    private void checkComputerId(final IComputer computerInstance)
        throws HypervisorException {

        if (computerInstance.getComputerId() == null) {
            throw new HypervisorException("Id is null.");
        }

        if ("".equals(computerInstance.getComputerId())) {
            throw new HypervisorException("Id is empty.");
        }
    }

    @SuppressWarnings("static-method")
    private void checkUnicity(final Map<String, IComputer> computersMap,
            final IComputer computerInstance) throws HypervisorException {
        if (computersMap.containsKey(computerInstance.getComputerId())) {
            throw new HypervisorException("A computer with the id ["
                    + computerInstance.getComputerId() + "] already exists.");
        }
    }

}
