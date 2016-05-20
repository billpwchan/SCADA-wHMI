package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.StringAttribute;

public class OPSTrainColor implements StatusComputer {

//	private static final ClientLogger s_logger = ClientLogger.getClientLogger();
	private static final Logger s_logger = LoggerFactory.getLogger(OPSTrainColor.class.getName());

	/** name of the statuses to use */
	private final Set<String> statuses_ = Collections.singleton("trackno");
    
    /** name of the properties to use */
    private final Set<String> properties_ = Collections.emptySet();
    
    final static Integer __Default__ = -1;
    
    /**
     * Advanced constructor
     * @param fileName define the filename to use
     * @throws HypervisorException can't initialize the computer
     */
    public OPSTrainColor() throws HypervisorException {
    }

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		s_logger.debug("*** OPSTrainColor: compute *** " + entityId);
        final StringAttribute attribute = new StringAttribute();
        attribute.setAttributeClass(StringAttribute.class.getName());
        String color = "#D2D2D2";	// Set default color
        
        IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
        if (configLoader != null) {
	        Map<String,String> projProperties = configLoader.getProjectConfigurationMap();
	        String line = "Default";
	
	        // Extract line info from entityId
	        int beginIdx = entityId.indexOf("--");
	        if (beginIdx > 0 && beginIdx < entityId.length()-4) {
	        	line = entityId.substring(beginIdx+2, beginIdx+5);
	        }

	        String key = "OPSTrainColor." + line + ".fillColor";
        	s_logger.debug("*** OPSTrainColor: key=" + key);
        	if (projProperties != null && projProperties.get(key) != null) {
        		color = projProperties.get(key);
        	}

        } else {
        	s_logger.debug("*** OPSTrainColor: configLoader=null");
        }

        s_logger.debug("*** OPSTrainColor: color=" + color);
        attribute.setValue(color);
        attribute.setTimestamp(new Date());
        attribute.setValid(true);
        return attribute;
	}

	@Override
    public String getComputerId() {
        return OPSTrainColor.class.getName();
    }

    @Override
    public Set<String> getInputStatuses() {
        return statuses_;
    }

    @Override
    public Set<String> getInputProperties() {
        return properties_;
    }

}
