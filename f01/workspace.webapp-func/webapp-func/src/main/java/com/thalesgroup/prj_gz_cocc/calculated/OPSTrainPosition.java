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
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.Coordinates;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.CoordinatesAttribute;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.IntAttribute;

public class OPSTrainPosition implements StatusComputer {

//	private static final ClientLogger s_logger = ClientLogger.getClientLogger();
	private static final Logger s_logger = LoggerFactory.getLogger(OPSTrainPosition.class.getName());

	/** name of the statuses to use */
    private final Set<String> statuses_ = Collections.singleton("trackno");
    
    /** name of the properties to use */
    private final Set<String> properties_ = Collections.emptySet();
   
    /** store the mapping between line and track/coordinates map */
    private final Map<String, Map<Integer, Coordinates>> lineMapping = new HashMap<String, Map<Integer, Coordinates>>();
    
    final static Integer __Default__ = -1;
    
    /**
     * Advanced constructor
     * @param fileName define the filename to use
     * @throws HypervisorException can't initialize the computer
     */
    public OPSTrainPosition() throws HypervisorException {
        final MarshallersPool pool = new MarshallersPool(Mapping.class.getPackage().getName(), "schemas/train_position_mapping.xsd");
        
        IConfigLoader configLoader = ServicesImplFactory.getInstance().getService(IConfigLoader.class);
        Map<String,String> projProperties = configLoader.getProjectConfigurationMap();
   
        Set<String> propKeySet = projProperties.keySet();
        if (propKeySet.isEmpty()) {
        	throw new HypervisorException("project properties not defined");
        }
        for (String key :propKeySet) {
        	if (key.startsWith("OPSTrainPosition.")) {
        		// Extract line info from key
        		String line = key.substring(key.indexOf('.')+1);
        		
        		// Get line mapping file from property
        		String mappingfile = projProperties.get(key);
        		
        		Map<Integer, Coordinates> mapping_ = new HashMap<Integer, Coordinates>();
        		Mapping mapping = pool.unmarshal(mappingfile, Mapping.class);
        		mapping_.put(__Default__,  new Coordinates(mapping.getDefaultPosition().getX(), mapping.getDefaultPosition().getY()));
        		for (TrackSectionCoordinates position : mapping.getTrackPosition()) {
                	mapping_.put(position.getId(), new Coordinates(position.getX(), position.getY()));
                }
        		if (getDefaultPosition(line) == null) {
        			throw new HypervisorException("__DEFAULT__ position is not defined in the " + mappingfile + " file");
        		}
        		lineMapping.put(line, mapping_);
        	}
        }
    }

    private com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.Coordinates getPosition(String line, final Integer trackno) {
    	Map<Integer, Coordinates> mapping = lineMapping.get(line);
    	if (mapping != null) {
    		return mapping.get(trackno);
    	}
    	return getDefaultPosition(line);
    }
    
    private Coordinates getDefaultPosition(String line) {
    	Map<Integer, Coordinates> mapping = lineMapping.get(line);
    	if (mapping != null) {
    		return mapping.get(__Default__);
    	}

    	return new Coordinates(__Default__,__Default__);
    }

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
            Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		s_logger.debug("*** OPSTrainPosition: compute *** " + entityId);
        final CoordinatesAttribute attribute = new CoordinatesAttribute();
        attribute.setAttributeClass(CoordinatesAttribute.class.getName());
        String line = "Default";
        
        if (lineMapping != null && !lineMapping.isEmpty()) {
        	// Extract line info from entityId
            int beginIdx = entityId.indexOf("--");
            if (beginIdx > 0 && beginIdx < entityId.length()-4) {
            	line = entityId.substring(beginIdx+2, beginIdx+5);
            }
        }
        
        IntAttribute trackAttribute = (IntAttribute) inputStatusByName.get(statuses_.iterator().next());
        if (trackAttribute != null) {

        	Coordinates position = getPosition(line, trackAttribute.getValue());
        	if (position != null) {
        		s_logger.debug("*** OPSTrainPosition: coordinates for " + trackAttribute.getValue() + " " + position.getX() + "," + position.getY());
        		attribute.setValue(position);
        	} else {
        		s_logger.debug("*** OPSTrainPosition: coordinates not found for " + trackAttribute.getValue());
            	attribute.setValue(getDefaultPosition(line));
            }

        } else {
        	attribute.setValue(getDefaultPosition(line));
        }
        attribute.setTimestamp(trackAttribute.getTimestamp());
        attribute.setValid(trackAttribute.isValid());
        return attribute;
	}

	@Override
    public String getComputerId() {
        return OPSTrainPosition.class.getName();
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
