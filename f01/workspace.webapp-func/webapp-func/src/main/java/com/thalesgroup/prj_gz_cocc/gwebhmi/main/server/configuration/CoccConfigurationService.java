package com.thalesgroup.prj_gz_cocc.gwebhmi.main.server.configuration;

import java.util.Set;
import java.util.TreeSet;

import com.thalesgroup.hv.data_v1.service.configuration.AbstractServiceType;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.action.IConfigRequest;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.config.server.svc.ConfigurationService;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.mvp.rpc.action.IOperatorActionReturn;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.GetAreaIds;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.GetServiceIds;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.configuration.action.IdsResponse;

/**
 * Implementation of the ICoccConfigurationService interface
 */
public class CoccConfigurationService extends ConfigurationService implements ICoccConfigurationService {

    /** a reference to the configuration bean */
    private final IConfigLoader configuration_; 
    
    /**
     * Constructor 
     * @param configuration a reference to the configuration bean to get data
     */
    public CoccConfigurationService(final IConfigLoader configuration) {
        configuration_ = configuration;
    }
    
    /**
     * We override this method to add the support of our custom methods
     */
    @Override
    public IOperatorActionReturn manageConfigRequest(IConfigRequest action) {
        IOperatorActionReturn toReturn = null ;
        //handle our cases
        if(action instanceof GetAreaIds) {
            final IdsResponse response = new IdsResponse();
            response.setIds(getAreaIds());
            toReturn = response;
        } else if(action instanceof GetServiceIds){
            final IdsResponse response = new IdsResponse();
            response.setIds(getServiceIds());
            toReturn = response;
        } else {
            //keep the default behavior by default
            toReturn = super.manageConfigRequest(action);
        }
        
        return toReturn;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAreaIds() {
        return new TreeSet<String>(configuration_.getSharedConfiguration().getAreas());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getServiceIds() {
        return new TreeSet<String>(configuration_.getSharedConfiguration().getEntitiesAsMap(AbstractServiceType.class).keySet());
    }

}
