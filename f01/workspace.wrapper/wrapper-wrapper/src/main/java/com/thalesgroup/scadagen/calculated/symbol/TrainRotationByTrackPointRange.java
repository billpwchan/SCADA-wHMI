package com.thalesgroup.scadagen.calculated.symbol;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.DoubleAttribute;
import com.thalesgroup.scadagen.calculated.common.SCSStatusComputer;
import com.thalesgroup.scadagen.calculated.common.SGSymbol_i;
import com.thalesgroup.scadagen.calculated.util.Util;

public class TrainRotationByTrackPointRange extends SCSStatusComputer implements SGSymbol_i {

	private final Logger logger = LoggerFactory.getLogger(TrainRotationByTrackPointRange.class.getName());

	protected final String mappingname		= ".valuemapping.";
	
	protected final String trackIDField		= "TrackID";
	protected final String kpEndField		= "Kp_End";
	protected final String kpBeginField		= "Kp_Begin";
	protected final String rotationField	= "rotation";
	
	protected final String defaultRotPropName	= ".defaultRotation";
	protected final String dirConventPropName	= ".dirConventional";
	protected final String dirReversePropName	= ".dirReverse";
	protected final String trackIDPropName		= ".trackIDAttName";
	protected final String trackPointPropName	= ".trackPointAttName";
	protected final String directionPropName	= ".directionAttName";
	protected final String csvFilePropName		= ".csvFileName";
	protected final String delimiterPropName	= ".delimiter";
	
	protected String trackIDAttName			= "trackID";
	protected String trackPointAttName		= "trackPoint";
	protected String directionAttName		= "direction";
	
	protected static Map<String, String> mappings	= null;
	
	protected static Util util = new Util();
	
	protected Double defaultRotation = 0.0;
	
	protected Integer dirConventional = 1;
	
	protected Integer dirReverse = 2;
	
	protected String csvFile = "TrainRotationByTrackPointRange.csv";
	
	protected String delimiter = ",";
	
	protected Map<Integer, List<Map<String, Double>>> trackMapListMap = new HashMap<Integer, List<Map<String, Double>>>();
	
	public TrainRotationByTrackPointRange() {
    	
    	m_name = this.getClass().getSimpleName();
    	
    	logger.debug("getComputerId[{}]", getComputerId());

        loadCnf();
        
        try {
        	util.readMapFromCsv(csvFile, delimiter, trackMapListMap);
        	if (logger.isDebugEnabled()) {
        		if (trackMapListMap != null) {
		        	for (Integer key : trackMapListMap.keySet()) {
		        		List<Map<String, Double>> list = trackMapListMap.get(key);
		        		if (list != null) {
		        			for (Map<String, Double>map : list) {
		        				String str = "";
		        				for (String mapKey: map.keySet()) {
		        					str += map.get(mapKey) + ",";
		        				}
		        				logger.debug(str);
		        			}
		        		}
		        	}
        		} else {
        			logger.debug("trackMapListMap is null");
        		}
        	}
        } catch (HypervisorException e) {
        	logger.error("Error reading config csv file [{}] [{}]", csvFile, e);
        }
    }

	@Override
	public String getComputerId() {
		return m_name;
	}
	
	@Override
	public void loadCnf() {

		// Loading properties
		mappings = util.loadMapping(m_name);
		
		// trackID attribute name
		String tmpstr = mappings.get(trackIDPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			trackIDAttName = tmpstr;
		}
		
		// trackPoint attribute name
		tmpstr = mappings.get(trackPointPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			trackPointAttName = tmpstr;
		}
		
		// direction attribute name
		tmpstr = mappings.get(directionPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			directionAttName = tmpstr;
		}
		
		// trackID to track point range csv file name
		tmpstr = mappings.get(csvFilePropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			csvFile = tmpstr;
		}
		
		// csv delimiter
		tmpstr = mappings.get(delimiterPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			delimiter = tmpstr;
		}
		
		// default rotation
		tmpstr = mappings.get(defaultRotPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultRotation = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultRotation = 0.0;
				logger.warn("Error loading default rotation from config file. [{}]", e.getMessage());
			}
		}
		
		// default conventional direction value
		tmpstr = mappings.get(dirConventPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				dirConventional = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				dirConventional = 1;
				logger.warn("Error loading conventional direction value from config file. [{}]", e.getMessage());
			}
		}
		
		// default reverse direction value
		tmpstr = mappings.get(dirReversePropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				dirReverse = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				dirReverse = 2;
				logger.warn("Error loading reverse direction value from config file. [{}]", e.getMessage());
			}
		}
		
		logger.debug("trackIDAttName[{}] kpAttName[{}] directionAttName[{}] csvFile[{}] delimiter[{}] defaultRotation[{}] dirConventional[{}] dirReverse[{}]",
				new Object[]{trackIDAttName, trackPointAttName, directionAttName, csvFile, delimiter, defaultRotation, dirConventional, dirReverse});

		// Fields to Subscribe
		m_statusSet.add(trackIDAttName);
    	m_statusSet.add(trackPointAttName);
    	m_statusSet.add(directionAttName);
	}

	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
			Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		logger.debug("compute Begin");
		logger.debug("compute trackIDAttName[{}] trackPointAttName[{}] directionAttName[{}]", new Object[]{trackIDAttName, trackPointAttName, directionAttName});
		
		// Return attribute
		final DoubleAttribute rotation = new DoubleAttribute();
		
		// Reset log
		util.setPrefix(m_name);
    	
    	// Load trackID value
    	Integer trackID = util.loadIntValue(inputStatusByName, trackIDAttName);
    	logger.debug("input trackID[{}]", trackIDAttName);
    	
    	// Load track point value
    	Integer trackPoint = util.loadIntValue(inputStatusByName, trackPointAttName);
    	logger.debug("input trackPoint[{}]", trackPointAttName);
    	
    	// Load direction value
    	Integer direction = util.loadIntValue(inputStatusByName, directionAttName);
    	logger.debug("input direction[{}]", directionAttName);
    	
    	Double rot = computeRotation(trackID, trackPoint, direction);

    	rotation.setValue(rot);
    	rotation.setTimestamp(new Date());
    	rotation.setValid(true);

    	logger.debug("compute End");
		return rotation;
	}

	private Double computeRotation(Integer trackID, Integer trackPoint, Integer direction) {
		
		Double defaultRotation = 0.0;
		if (trackMapListMap == null) {
			logger.warn("Track mapping is null. Default rotation [{}] is used", defaultRotation.toString());
			return defaultRotation;
		}
		
		// get list of map with trackID
		List<Map<String, Double>> trackListMap = trackMapListMap.get(trackID);
		if (trackListMap == null) {
			logger.warn("Unable to map trackID [{}] to any track point. Default rotation [{}] is used", new Object[] {trackID, defaultRotation});
			return defaultRotation;
		}
		
		Map<String, Double>foundKpMap = null;
		Double kpEnd = 0.0;
		Double kpBegin = 0.0;
		// iterate list of map to get track point map
		for (Map<String, Double>kpMap : trackListMap) {
			kpEnd = kpMap.get(kpEndField);
			kpBegin = kpMap.get(kpBeginField);
			if (kpEnd == null || kpBegin == null) {
				continue;
			}
			
			if (direction == dirConventional) {			
				if (trackPoint > kpEnd && trackPoint <= kpBegin) {
					foundKpMap = kpMap;
					break;
				}
			} else {
				if (trackPoint >= kpEnd && trackPoint < kpBegin) {
					foundKpMap = kpMap;
					break;
				}
			}
		}
		if (foundKpMap == null) {
			logger.warn("TrackID [{}] track point [{}] mapping not found. Default rotation [{}] is used", new Object[] {trackID, trackPoint, defaultRotation});
			return defaultRotation;
		}
		
		// Calculate midpoint
		Double midKp = (kpEnd + kpBegin) / 2;
		
		// Get rotation
		Double rot = 0.0;
		Double kpRot = foundKpMap.get(rotationField);
		if (kpRot == null) {
			logger.warn("rotation column data not found");
			kpRot = 0.0;
		}
		if (trackPoint > midKp) {
			if (direction == dirConventional) {
				rot = kpRot;
			} else if (direction == dirReverse) {
				rot = (kpRot + 180) % 360;
			} else {
				rot = 0.0;
			}
		} else {
			if (direction == dirConventional) {
				rot = kpRot;
			} else if (direction == dirReverse) {
				rot = (kpRot + 180) % 360;
			} else {
				rot = 0.0;
			}
		}
		logger.debug("TrackID[{}] TrackPoint[{}] Direction[{}] mapped to rotation[{}]", new Object[] {trackID, trackPoint, direction, rot});
		
		return rot;
	}
}
