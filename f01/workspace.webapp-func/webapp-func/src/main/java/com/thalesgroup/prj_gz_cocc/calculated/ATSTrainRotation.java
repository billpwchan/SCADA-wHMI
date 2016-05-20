package com.thalesgroup.prj_gz_cocc.calculated;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.prj_gz_cocc.train.Mapping;
import com.thalesgroup.prj_gz_cocc.train.TrackSectionCoordinates;
import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hv.common.tools.MarshallersPool;
import com.thalesgroup.hypervisor.mwt.core.util.config.loader.IConfigLoader;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.computers.StatusComputer;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.data.server.rpc.implementation.ServicesImplFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.DoubleAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class ATSTrainRotation implements StatusComputer {

//	private static final ClientLogger s_logger = ClientLogger.getClientLogger();
	private static final Logger s_logger = LoggerFactory.getLogger(ATSTrainRotation.class.getName());

	/** name of the statuses to use */
    private final Set<String> statuses_ = Collections.singleton("trackno");
    
    /** name of the properties to use */
    private final Set<String> properties_ = Collections.emptySet();
    
    /** store the mapping between line and track/rotation map */
    private final Map<String, Map<Integer, Double>> lineMapping = new HashMap<String, Map<Integer, Double>>();
    
    final static Integer __Default__ = -1;
    
    public ATSTrainRotation() throws HypervisorException {
    	final MarshallersPool pool = new MarshallersPool(Mapping.class.getPackage().getName(), "schemas/train_position_mapping.xsd");
    	IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
        Map<String,String> projProperties = configLoader.getProjectConfigurationMap();
   
        Set<String> propKeySet = projProperties.keySet();
        if (propKeySet.isEmpty()) {
        	throw new HypervisorException("project properties not defined");
        }
        for (String key :propKeySet) {
        	if (key.startsWith("ATSTrainPosition.")) {
        		// Extract line info from key
        		String line = key.substring(key.indexOf('.')+1);
        		
        		// Get line mapping file from property
        		String mappingfile = projProperties.get(key);
        		
        		Map<Integer, Double> mapping_ = new HashMap<Integer, Double>();
        		Mapping mapping = pool.unmarshal(mappingfile, Mapping.class);
        		mapping_.put(__Default__,  mapping.getDefaultPosition().getRot());
        		for (TrackSectionCoordinates position : mapping.getTrackPosition()) {
                	mapping_.put(position.getId(), position.getRot());
                }
        		if (getDefaultRotation(line) == null) {
        			throw new HypervisorException("__DEFAULT__ rotation is not defined in the " + mappingfile + " file");
        		}
        		lineMapping.put(line, mapping_);
        	}
        }
    }

    private Double getRotation(String line, final Integer trackno) {
    	Map<Integer, Double> mapping = lineMapping.get(line);
    	if (mapping != null) {
    		return mapping.get(trackno);
    	}
    	return getDefaultRotation(line);
    }
    
    private Double getDefaultRotation(String line) {
    	Map<Integer, Double> mapping = lineMapping.get(line);
    	if (mapping != null) {
    		return mapping.get(__Default__);
    	}

    	return 0.0;
    }

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		s_logger.debug("*** ATSTrainRotation: compute *** " + entityId);
        final DoubleAttribute attribute = new DoubleAttribute();
        attribute.setAttributeClass(DoubleAttribute.class.getName());
        String line = "Default";
        
        if (lineMapping != null && !lineMapping.isEmpty()) {
        	// Extract line info from entityId
            int beginIdx = entityId.indexOf("--");
            if (beginIdx > 0 && beginIdx < entityId.length()-4) {
            	line = entityId.substring(beginIdx+2, beginIdx+5);
            }
        }
        
        IntAttribute trackAttribute = (IntAttribute) inputStatusByName.get(statuses_.iterator().next());
        if (trackAttribute != null ) { 
        	Double rotation = getRotation(line, trackAttribute.getValue());
        	if (rotation != null) {
        		s_logger.debug("*** ATSTrainRotation: rotation for " + trackAttribute.getValue() + " " + rotation.toString());
        		attribute.setValue(rotation);
        	} else {
        		s_logger.debug("*** ATSTrainRotation: rotation not found for " + trackAttribute.getValue());
            	attribute.setValue(getDefaultRotation(line));
            }
        } else {
        	attribute.setValue(getDefaultRotation(line));
        }
        attribute.setTimestamp(trackAttribute.getTimestamp());
        attribute.setValid(trackAttribute.isValid());
        return attribute;
	}

	@Override
    public String getComputerId() {
        return ATSTrainRotation.class.getName();
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
