package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.configuration;

import java.util.Set;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.server.svc.IConfigurationService;

/**
 * Specific COCC configuration interface to add access to some system configuration information
 */
public interface ICoccConfigurationService extends IConfigurationService {

    /**
     * get the areas from the configuration
     * @return the collection of the area identifiers
     */
    Set<String> getAreaIds();
    
    /**
     * get the services from the configuration
     * @return the collection of the service identifiers
     */
    Set<String> getServiceIds();
    
}
