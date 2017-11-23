package com.thalesgroup.scadagen.calculated.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thalesgroup.hv.common.HypervisorException;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.Coordinates;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.CoordinatesAttribute;
import com.thalesgroup.scadagen.calculated.util.Util;

public abstract class TrainPositionByTrackPointRange extends SCSStatusComputer implements SGSymbol_i {

	protected Logger logger					= null;
	
	protected String logPrefix				= null;

	protected final String mappingname		= ".valuemapping.";
	
	protected final String trackIDField		= "TrackID";
	protected final String kpEndField		= "Kp_End";
	protected final String kpBeginField		= "Kp_Begin";
	protected final String xEndField		= "x_End";
	protected final String yEndField		= "y_End";
	protected final String xBeginField		= "x_Begin";
	protected final String yBeginField		= "y_Begin";
	protected final String xOffsetConvField		= "x_Offset_Conv";
	protected final String yOffsetConvField		= "y_Offset_Conv";
	protected final String xOffsetRevField		= "x_Offset_Rev";
	protected final String yOffsetRevField		= "y_Offset_Rev";
	protected final String rotationField		= "rotation";
	
	protected final String defaultXPropName		= ".defaultX";
	protected final String defaultYPropName		= ".defaultY";
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
	
	protected Double defaultX = -100.0;
	
	protected Double defaultY = -100.0;
	
	protected Integer dirConventional = 1;
	
	protected Integer dirReverse = 2;
	
	protected String csvFile = "TrackPositionByTrackPointRange.csv";
	
	protected String delimiter = ",";
	
	protected Map<Integer, List<Map<String, Double>>> trackMapListMap = new HashMap<Integer, List<Map<String, Double>>>();


	@Override
	public String getComputerId() {
		return m_name;
	}
	
	@Override
	public void loadCnf() {
		
		logger = LoggerFactory.getLogger(TrainPositionByTrackPointRange.class.getName());
		
		logPrefix = m_name;

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
		
		// default x coordinate
		tmpstr = mappings.get(defaultXPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultX = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultX = 0.0;
				logger.warn("Error loading defaultX from config file. [{}]", e.getMessage());
			}
		}
		
		// default y coordinate
		tmpstr = mappings.get(defaultYPropName);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultY = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultY = 0.0;
				logger.warn("Error loading defaultY from config file. [{}]", e.getMessage());
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
		
		logger.debug("trackIDAttName[{}] kpAttName[{}] directionAttName[{}] csvFile[{}] delimiter[{}] defaultX[{}] defaultY[{}] dirConventional[{}] dirReverse[{}]",
				new Object[]{trackIDAttName, trackPointAttName, directionAttName, csvFile, delimiter, defaultX, defaultY, dirConventional, dirReverse});

		// Fields to Subscribe
		m_statusSet.add(trackIDAttName);
    	m_statusSet.add(trackPointAttName);
    	m_statusSet.add(directionAttName);
	}
	
	protected void loadTrackMapping() {
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
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
			Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		logger.debug("compute Begin");
		logger.debug("compute position from trackIDAttName[{}] trackPointAttName[{}] directionAttName[{}]", new Object[]{trackIDAttName, trackPointAttName, directionAttName});
		
		// Return attribute
		final CoordinatesAttribute position = new CoordinatesAttribute();
		
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
    	
    	Coordinates coordinate = computeCoordinates(trackID, trackPoint, direction);

    	position.setValue(coordinate);
    	position.setTimestamp(new Date());
    	position.setValid(true);

    	logger.debug("compute End");
		return position;
	}

	private Coordinates computeCoordinates(Integer trackID, Integer trackPoint, Integer direction) {
		
		Coordinates defaultCoordinate = new Coordinates(defaultX, defaultY);
		if (trackMapListMap == null) {
			logger.warn("Track mapping is null. Default coordinate [{}] is used", defaultCoordinate.toString());
			return defaultCoordinate;
		}
		
		// get list of map with trackID
		List<Map<String, Double>> trackListMap = trackMapListMap.get(trackID);
		if (trackListMap == null) {
			logger.warn("Unable to map trackID [{}] to any track point range. Default coordinate [{}] is used", new Object[] {trackID, defaultCoordinate});
			return defaultCoordinate;
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
			logger.warn("TrackID [{}] track point [{}] mapping not found. Default coordinate [{}] is used", new Object[] {trackID, trackPoint, defaultCoordinate});
			return defaultCoordinate;
		}
		
		// Calculate midpoint
		Double midKp = (kpEnd + kpBegin) / 2;
		
		// Get x, y coordinates
		Double x = defaultX;
		Double y = defaultY;
		Double xBegin = foundKpMap.get(xBeginField);
		if (xBegin == null) {
			logger.warn("xBegin column data not found");
			xBegin = 0.0;
		}
		Double xEnd = foundKpMap.get(xEndField);
		if (xEnd == null) {
			logger.warn("xEnd column data not found");
			xEnd = 0.0;
		}
		Double yBegin = foundKpMap.get(yBeginField);
		if (yBegin == null) {
			logger.warn("yBegin column data not found");
			yBegin = 0.0;
		}
		Double yEnd = foundKpMap.get(yEndField);
		if (yEnd == null) {
			logger.warn("yEnd column data not found");
			yEnd = 0.0;
		}
		Double xOffsetConv = foundKpMap.get(xOffsetConvField);
		if (xOffsetConv == null) {
			logger.warn("xOffsetConv column data not found");
			xOffsetConv = 0.0;
		}
		Double yOffsetConv = foundKpMap.get(yOffsetConvField);
		if (yOffsetConv == null) {
			logger.warn("yOffsetConv column data not found");
			yOffsetConv = 0.0;
		}
		Double xOffsetRev = foundKpMap.get(xOffsetRevField);
		if (xOffsetRev == null) {
			logger.warn("xOffsetRev column data not found");
			xOffsetRev = 0.0;
		}
		Double yOffsetRev = foundKpMap.get(yOffsetRevField);
		if (yOffsetRev == null) {
			logger.warn("yOffsetRev column data not found");
			yOffsetRev = 0.0;
		}
		Double kpRot = foundKpMap.get(rotationField);
		if (kpRot == null) {
			logger.warn("rotation column data not found");
			kpRot = 0.0;
		}
		
		if (trackPoint > midKp) {
			if (direction == dirConventional) {
				x = xBegin + xOffsetConv;
				y = yBegin + yOffsetConv;
			} else if (direction == dirReverse) {
				x = ((xEnd + xBegin) / 2) + xOffsetRev;
				y = ((yEnd + yBegin) / 2) + yOffsetRev;
			} else {
				x = (((xEnd + xBegin) / 2) + xEnd) / 2 + (100 * Math.sin(Math.toRadians(kpRot)));
				y = (((yEnd + yBegin) / 2) + yEnd) / 2 + (100 * Math.cos(Math.toRadians(kpRot)));
			}
		} else {
			if (direction == dirConventional) {
				x = ((xEnd + xBegin) / 2) + xOffsetConv;
				y = ((yEnd + yBegin) / 2) + yOffsetConv;
			} else if (direction == dirReverse) {
				x = xEnd + xOffsetRev;
				y = yEnd + yOffsetRev;
			} else {
				x = (((xEnd + xBegin) / 2) + xEnd) / 2 + (100 * Math.sin(Math.toRadians(kpRot)));
				y = (((yEnd + yBegin) / 2) + yEnd) / 2 + (100 * Math.cos(Math.toRadians(kpRot)));
			}
		}
		logger.debug("TrackID[{}] TrackPoint[{}] Direction[{}] mapped to coordinate x[{}] y[{}]", new Object[] {trackID, trackPoint, direction, x, y});
		
		return new Coordinates(x, y);
	}
}
