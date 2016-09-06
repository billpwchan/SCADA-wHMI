package com.thalesgroup.scadagen.bps.conf.actions;

import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorConversionException;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.hv.common.tools.XMLTool;
import com.thalesgroup.scadagen.bps.conf.actions.ActionPackageConfiguration;

public class ActionsConfigurationLoader {
	/** This is a logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ActionsConfigurationLoader.class);

    /** configuration object package name */
    private static final String CONF_PACKAGE_NAME = ActionPackageConfiguration.class.getPackage().getName();

    /** configuration xsd relative path */
    private static final String XSD_RELATIVE_PATH = "xsd/actions-config.xsd";

    /** configuration file reader */
    private MarshallersPool marshallersPool_;

    /**
     * Create the configuration loader.
     */
    public ActionsConfigurationLoader() {

        try {
            LOGGER.trace("Create marshaller pool.");
            marshallersPool_ = new MarshallersPool(CONF_PACKAGE_NAME, 1, XSD_RELATIVE_PATH);
            LOGGER.trace("Marshaller ok.");
        } catch (final HypervisorException e) {
            LOGGER.error("Error while starting the marshaller pool. "
                    + "The ActionsConfigurationLoader is not able to read the configuration. "
                    + "No action will be loaded.", e);
        }

    }

    /**
     * Read the configuration and return the configured package names.
     * @param xmlRelativePath the relative configuration xml path.
     * @return the {@link Set} of configured package name
     * or an empty {@link Set} if the file doesn't exist or if an error occurred.
     */
    public Set<String> readConfiguration(final String xmlRelativePath) {

        // set of class to return
        final Set<String> toReturn = new HashSet<String>();

        if (marshallersPool_ != null) {
            LOGGER.debug("Reading action configuration file [{}].", xmlRelativePath);

            // get URL of the configuration file, can be null if file doesn't exist
            Source xmlFile = null;

            try {
                xmlFile = XMLTool.loadXMLFile(xmlRelativePath);
            } catch (final HypervisorConversionException e) {
                // can happen if the functionality is deactivated.
                LOGGER.debug("No action configuration file was found, no action will be loaded.");
            }

            try {	
                // load only if the file was found
                if (xmlFile != null) {

                    // unmarshall the configuration file
                    final ActionPackageConfiguration configuration = marshallersPool_.unmarshal(xmlFile);

                    toReturn.addAll(configuration.getActionPackage());

                    LOGGER.debug("[{}] configurated package(s).", toReturn.size());

                }

            } catch (final HypervisorConversionException e) {
                LOGGER.error("Error while reading the action configuration file.", e);
            }

        } else {
            LOGGER.trace("Marshaller pool is null, not any action will be searched.");
        }

        return toReturn;
    }
}
