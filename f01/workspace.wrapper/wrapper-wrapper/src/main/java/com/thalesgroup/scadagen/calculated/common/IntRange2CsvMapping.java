package com.thalesgroup.scadagen.calculated.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.hypervisor.mwt.core.webapp.core.opm.client.dto.OperatorOpmInfo;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.AttributeClientAbstract;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.data.attribute.Coordinates;
import com.thalesgroup.scadagen.calculated.util.Util;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.server.factory.UILoggerFactory;

public class IntRange2CsvMapping extends SCSStatusComputer implements SGSymbol_i {
	
	protected UILogger_i logger						= null;
	
	protected String logPrefix					= null;

	protected final String mappingname			= ".valuemapping.";
	
	protected final int OT_COORDINATES			= 1;
	protected final int OT_DOUBLE				= 2;
	
	protected int ot							= OT_COORDINATES;
	
	protected final int INT_INDEX_TRACKID		= 1;
	protected final int INT_INDEX_DIR			= 2;
	protected final int INT_INDEX_BEGIN			= 3;
	protected final int INT_INDEX_END			= 4;
	
	protected final int INT_INDEX_X				= 5;
	protected final int INT_INDEX_Y				= 6;
	protected final int INT_INDEX_ANGLE			= 7;
	
	protected final String propNameDefaultX		= ".defaultX";
	protected final String propNameDefaultY		= ".defaultY";
	protected final String propNameDefaultAngle	= ".defaultAngle";
	
	protected final String propNameCsvFile		= ".csvFileName";
	protected final String propNameDelimiter	= ".delimiter";

	protected final String propNameTrack		= ".attNameTrack";
	protected final String propNameDirection	= ".attNameDirection";
	protected final String propNameTrackPoint	= ".attNameTrackPoint";
	
	protected String propNameIndexTrackId		= ".attNameTrackId";
	protected String propNameIndexDir			= ".attNameDir";
	protected String propNameIndexBegin			= ".attNameBegin";
	protected String propNameIndexEnd			= ".attNameEnd";
	protected String propNameIndexX				= ".attNameX";
	protected String propNameIndexY				= ".attNameY";
	protected String propNameIndexAngle			= ".attNameAngle";
	
	protected String attNameTrackId				= "trackID";
	protected String attNameTrackPoint			= "trackPoint";
	protected String attNameDirection			= "direction";
	
	protected static Map<String, String> mappings	= null;
	
	protected static Util util = new Util();
	
	protected String csvFile		= "IntRange2CsvMapping.csv";
	protected Double defaultX		= -100.0;
	protected Double defaultY		= -100.0;
	protected Double defaultAngle	= 0.0;
	protected String delimiter		= ",";
	
	protected int indexTrackId		= 0;
	protected int indexDir			= 1;
	protected int indexBegin		= 2;
	protected int indexEnd			= 3;
	protected int indexX			= 4;
	protected int indexY			= 5;
	protected int indexAngle		= 6;
	
	protected Map<Integer, String> lines = new HashMap<Integer, String>();
	protected Map<Integer, Map<Integer, Integer>> inputs = new HashMap<Integer, Map<Integer, Integer>>();
	protected Map<Integer, Map<Integer, Double>> outputs = new HashMap<Integer, Map<Integer, Double>>();

	@Override
	public String getComputerId() {
		return m_name;
	}
	
	@Override
	public AttributeClientAbstract<?> compute(OperatorOpmInfo operatorOpmInfo, String entityId,
			Map<String, AttributeClientAbstract<?>> inputStatusByName, Map<String, Object> inputPropertiesByName) {
		final String function = "compute";
		logger.debug("[{}] Begin", function);
		logger.debug("[{}] position from attNameTrackId[{}] attNameTrackPoint[{}] attNameDirection[{}]"
				, new Object[]{function, attNameTrackId, attNameTrackPoint, attNameDirection});
	
		// Reset log
		util.setPrefix(m_name);
		
		// default output
		Double x = defaultX, y = defaultY, angle = defaultAngle;
    	
    	// Load trackID value
    	final Integer dmTrackID = util.loadIntValue(inputStatusByName, attNameTrackId);
    	logger.debug("[{}] input attNameTrackId[{}] dmTrackID[{}]", new Object[]{function, attNameTrackId, dmTrackID});
    	
    	// Load track point value
    	final Integer dmTrackPoint = util.loadIntValue(inputStatusByName, attNameTrackPoint);
    	logger.debug("[{}] input attNameTrackPoint[{}] dmTrackPoint[{}]", new Object[]{function, attNameTrackPoint, dmTrackPoint});
    	
    	// Load direction value
    	final Integer dmDirection = util.loadIntValue(inputStatusByName, attNameDirection);
    	logger.debug("[{}] input attNameDirection[{}] dmDirection[{}]", new Object[]{function, attNameDirection, dmDirection});
    	
    	logger.debug("[{}] input dmTrackID[{}] dmTrackPoint[{}] dmDirection[{}]", new Object[]{function, dmTrackID, dmTrackPoint, dmDirection});
    	
    	if ( 
    			null != dmTrackID
    			&& null != dmTrackPoint
    			&& null != dmDirection ) {
    		
        	Map<Integer, Integer> input = null;
        	Map<Integer, Double> output = null;
        	
        	boolean found = false;
        	for ( int i = 0 ; i < inputs.size() ; ++i ) {
        		
        		input = inputs.get(i);
        		if ( null != input ) {
            		output = outputs.get(i);
            		if ( null != output ) {
            			
                		final Integer csvTackId		= input.get(INT_INDEX_TRACKID);
                		final Integer csvDir		= input.get(INT_INDEX_DIR);
                		final Integer csvBegin		= input.get(INT_INDEX_BEGIN);
                		final Integer csvEnd		= input.get(INT_INDEX_END);
                		logger.debug("[{}] input line[{}] csvTackId[{}] csvDir[{}] csvBegin[{}] csvEnd[{}]", new Object[]{function, i, csvTackId, csvDir, csvBegin, csvEnd});
            			
                		if (
                				null != csvTackId
                				&& null != csvDir
                				&& null != csvBegin
                				&& null != csvEnd
                		) {
                			if ( dmTrackID == csvTackId && csvDir == dmDirection ) {
                    			if ( csvBegin > csvEnd ) {
                    				if ( csvBegin >= dmTrackPoint && csvEnd < dmTrackPoint ) {
                    					found = true;
                    				}
                    			} else if ( csvBegin <= csvEnd ) {
                    				if ( csvBegin <= dmTrackPoint && csvEnd > dmTrackPoint ) {
                    					found = true;
                    				} // End of Begin End & TrackPoint Compare
                    			} // End of Begin > End Compare
                			} // End of TrackId & Direction Compare
                		} else {
                			// Invalid Input
                			logger.warn("[{}] loading line[{}] csvTackId[{}] csvDir[{}] csvBegin[{}] csvEnd[{}]"
                					, new Object[]{function, i, csvTackId, csvDir, csvBegin, csvEnd});
                		}
            		} // End of input valid checking
            		else {
            			// Invalid Input
            			logger.warn("[{}] loading line[{}] output IS NULL"
            					, new Object[]{function, i});
            		}
        		} // End of input valid checking
        		else {
        			// Invalid Input
        			logger.warn("[{}] loading line[{}] input IS NULL"
        					, new Object[]{function, i});
        		}
        		if ( found ) {
    				x = output.get(INT_INDEX_X);
    				y = output.get(INT_INDEX_Y);
    				angle = output.get(INT_INDEX_ANGLE);
        			break;
        		}
        	}
    	} // End of input valid checking
		else {
			// Invalid Input
			logger.warn("[{}] loading datamodel input, dmTrackID[{}] dmTrackPoint[{}] dmDirection[{}] value(s) IS NULL"
					, new Object[]{function, dmTrackID, dmTrackPoint, dmDirection});
		}
    	
    	AttributeClientAbstract<?> ret = null;
    	
    	if ( OT_COORDINATES == ot ) {
    		ret = util.getCoordinatesAttribute(new Coordinates(x, y), true, new Date());
    	} else {
    		ret = util.getDoubleAttribute(angle, true, new Date());
    	}

    	logger.debug("[{}] End", function);
		return ret;
	}
	
	@Override
	public void loadCnf() {
		final String function = "loadCnf";
		
		logger = UILoggerFactory.getInstance().get(this.getClass().getName());
		
		logPrefix = m_name;

		// Loading properties
		mappings = util.loadMapping(m_name);
		
		String tmpstr = null;
		
		// default x coordinate
		tmpstr = mappings.get(propNameDefaultX);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultX = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultX = 0.0;
				logger.warn("[{}] loading attribute propNameDefaultX[{}] from properties file, using default defaultX[{}]"
						, new Object[]{function, propNameDefaultX, defaultX});
			}
		}
		else {
			defaultX = 0.0;
			logger.debug("[{}] loading attribute propNameDefaultX[{}] from properties file, using default defaultX[{}]"
				, new Object[]{function, propNameDefaultX, defaultX});
		}
		
		// default y coordinate
		tmpstr = mappings.get(propNameDefaultY);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultY = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultY = 0.0;
				logger.warn("[{}] loading attribute propNameDefaultY[{}] from properties file, using default defaultY[{}]"
						, new Object[]{function, propNameDefaultY, defaultY});
			}
		}
		else {
			defaultY = 0.0;
			logger.debug("[{}] loading attribute propNameDefaultY[{}] from properties file, using default defaultY[{}]"
				, new Object[]{function, propNameDefaultY, defaultY});
		}
		
		// trackID attribute name
		tmpstr = mappings.get(propNameTrack);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			attNameTrackId = tmpstr;
		}
		else {
			logger.debug("[{}] loading attribute propNameTrack[{}] from properties file, using default attNameTrackId[{}]"
					, new Object[]{function, propNameTrack, attNameTrackId});
		}
		
		// trackPoint attribute name
		tmpstr = mappings.get(propNameTrackPoint);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			attNameTrackPoint = tmpstr;
		}
		else {
			logger.debug("[{}] loading attribute propNameTrackPoint[{}] from properties file, using default attNameTrackPoint[{}]"
					, new Object[]{function, propNameTrackPoint, attNameTrackPoint});
		}
		
		// direction attribute name
		tmpstr = mappings.get(propNameDirection);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			attNameDirection = tmpstr;
		}
		else {
			logger.debug("[{}] loading attribute propNameDirection[{}] from properties file, using default attNameDirection[{}]"
					, new Object[]{function, propNameDirection, attNameDirection});
		}
		
		// trackID to track point range csv file name
		tmpstr = mappings.get(propNameCsvFile);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			csvFile = tmpstr;
		}
		else {
			logger.debug("[{}] loading attribute propNameCsvFile[{}] from properties file, using default csvFile[{}]"
					, new Object[]{function, propNameCsvFile, csvFile});
		}
		
		// csv delimiter
		tmpstr = mappings.get(propNameDelimiter);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			delimiter = tmpstr;
		}
		else {
			logger.debug("[{}] loading attribute propNameDelimiter[{}] from properties file, using default delimiter[{}]"
					, new Object[]{function, propNameDelimiter, delimiter});
		}

		// default angle
		tmpstr = mappings.get(propNameDefaultAngle);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				defaultAngle = Double.parseDouble(tmpstr);
			} catch (Exception e) {
				defaultAngle = 0.0;
				logger.warn("[{}] loading attribute propNameDefaultAngle[{}] from properties file[{}], using default defaultAngle[{}]"
						, new Object[]{function, propNameDefaultAngle, csvFile, defaultAngle});
			}
		}
		else {
			defaultAngle = 0.0;
			logger.debug("[{}] loading attribute propNameDefaultAngle[{}] from properties file[{}], using default defaultAngle[{}]"
				, new Object[]{function, propNameDefaultAngle, csvFile, defaultAngle});
		}
		
		// indexTrackId value
		tmpstr = mappings.get(propNameIndexTrackId);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexTrackId = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexTrackId = 0;
				logger.warn("[{}] loading attribute propNameIndexTrackId[{}] from properties file[{}], using default indexTrackId[{}]"
						, new Object[]{function, propNameIndexTrackId, csvFile, indexTrackId});
			}
		}
		else {
			indexTrackId = 0;
			logger.debug("[{}] loading attribute propNameIndexTrackId[{}] from properties file[{}], using default indexTrackId[{}]"
				, new Object[]{function, propNameIndexTrackId, csvFile, indexTrackId});
		}
		
		// indexDir value
		tmpstr = mappings.get(propNameIndexDir);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexDir = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexDir = 0;
				logger.warn("[{}] loading attribute propNameIndexDir[{}] from properties file[{}], using default indexDir[{}]"
						, new Object[]{function, propNameIndexDir, csvFile, indexDir});
			}
		}
		else {
			indexDir = 0;
			logger.debug("[{}] loading attribute propNameIndexDir[{}] from properties file[{}], using default indexDir[{}]"
				, new Object[]{function, propNameIndexDir, csvFile, indexDir});
		}
		
		// indexBegin value
		tmpstr = mappings.get(propNameIndexBegin);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexBegin = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexBegin = 0;
				logger.warn("[{}] loading attribute propNameIndexBegin[{}] from properties file[{}], using default indexBegin[{}]"
						, new Object[]{function, propNameIndexBegin, csvFile, indexBegin});
			}
		}
		else {
			indexBegin = 0;
			logger.debug("[{}] loading attribute propNameIndexBegin[{}] from properties file[{}], using default indexBegin[{}]"
				, new Object[]{function, propNameIndexBegin, csvFile, indexBegin});
		}
		
		// indexEnd value
		tmpstr = mappings.get(propNameIndexEnd);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexEnd = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexEnd = 0;
				logger.warn("[{}] loading attribute propNameIndexEnd[{}] from properties file[{}], using default indexEnd[{}]"
						, new Object[]{function, propNameIndexEnd, csvFile, indexEnd});
			}
		}
		else {
			indexEnd = 0;
			logger.debug("[{}] loading attribute propNameIndexEnd[{}] from properties file[{}], using default indexEnd[{}]"
				, new Object[]{function, propNameIndexEnd, csvFile, indexEnd});
		}
		
		// indexX value
		tmpstr = mappings.get(propNameIndexX);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexX = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexX = 0;
				logger.warn("[{}] loading attribute propNameIndexX[{}] from properties file[{}], using default indexX[{}]"
						, new Object[]{function, propNameIndexX, csvFile, indexX});
			}
		}
		else {
			indexX = 0;
			logger.debug("[{}] loading attribute propNameIndexX[{}] from properties file[{}], using default indexX[{}]"
				, new Object[]{function, propNameIndexX, csvFile, indexX});
		}
		
		// indexY value
		tmpstr = mappings.get(propNameIndexY);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexY = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexY = 0;
				logger.warn("[{}] loading attribute propNameIndexY[{}] from properties file[{}], using default indexY[{}]"
						, new Object[]{function, propNameIndexY, csvFile, indexY});
			}
		}
		else {
			indexY = 0;
			logger.debug("[{}] loading attribute propNameIndexY[{}] from properties file[{}], using default indexY[{}]"
				, new Object[]{function, propNameIndexY, csvFile, indexY});
		}
		
		// indexAngle value
		tmpstr = mappings.get(propNameIndexAngle);
		if (tmpstr != null && tmpstr.length() > 0 ) {
			try {
				indexAngle = Integer.parseInt(tmpstr);
			} catch (Exception e) {
				indexAngle = 0;
				logger.warn("[{}] loading attribute propNameIndexAngle[{}] from properties file[{}], using indexAngle[{}]"
						, new Object[]{function, propNameIndexAngle, csvFile, indexAngle});
			}
		}
		else {
			indexAngle = 0;
			logger.debug("[{}] loading attribute propNameIndexAngle[{}] from properties file[{}], using indexAngle[{}]"
				, new Object[]{function, propNameIndexAngle, csvFile, indexAngle});
		}
		
		logger.debug("[{}] attNameTrackId[{}] attNameTrackPoint[{}] attNameDirection[{}]",
				new Object[]{function, attNameTrackId, attNameTrackPoint, attNameDirection});
		
		logger.debug("[{}] csvFile[{}] delimiter[{}]",
				new Object[]{function, csvFile, delimiter});
		
		logger.debug("[{}] defaultX[{}] defaultY[{}] defaultAngle[{}]",
				new Object[]{function, defaultX, defaultY, defaultAngle});
		
		logger.debug("[{}] indexTrackId[{}] indexDir[{}] indexBegin[{}] indexEnd[{}] indexX[{}] indexY[{}] indexAngle[{}]",
				new Object[]{function, indexTrackId, indexDir, indexBegin, indexEnd, indexX, indexY, indexAngle});

		// Fields to Subscribe
		m_statusSet.add(attNameTrackId);
    	m_statusSet.add(attNameTrackPoint);
    	m_statusSet.add(attNameDirection);
	}
	
	protected void loadCsvFile() {
		lines = util.readLineFromFile(csvFile);
	}
	
	private Integer convert2Integer(String [] strings, int index) {
		final String function = "convert2Integer";
		Integer ret = null;
		if ( null != strings ) {
			if ( strings.length > index ) {
				try {
					ret = Integer.parseInt(strings[index]);
				} catch (NumberFormatException e) {
					logger.error("[{}] Check the type conversion(String > Integer) on parse integer => strings({})[{}]"
							, new Object[]{function, index, strings[index]});
					logger.error("[{}] e[{}]", function, e.toString());
				}
			} else {
				logger.error("[{}] strings.length > index IS INVALID", function);
			}
		} else {
			logger.error("[{}] strings[] IS NULL", function);
		}
		return ret;
	}
	
	private Double convert2Double(String [] strings, int index) {
		final String function = "convert2Double";
		Double ret = null;
		if ( null != strings ) {
			if ( strings.length > index ) {
				try {
					ret = Double.parseDouble(strings[index]);
				} catch (NumberFormatException e) {
					logger.error("[{}] Check the type conversion(String > Double) on parse double => strings({})[{}]"
							, new Object[]{function, index, strings[index]});
					logger.error("[{}] e[{}]", function, e.toString());
				}
			} else {
				logger.error("[{}] strings.length > index IS INVALID", function);
			}
		} else {
			logger.error("[{}] strings[] IS NULL", function);
		}
		return ret;
	}
	
	protected void convertCsv2Map() {
		final String function = "convertCsv2Map";
		if ( null != lines ) {
			int counter = lines.size();
			for ( int lineNum = 0 ; lineNum < counter ; ++lineNum ) {
				Map<Integer, Integer> input = null;
				Map<Integer, Double> output = null;
				String line = lines.get(lineNum);
				if ( null != line ) {
					logger.debug("[{}] lineNum[{}] line[{}]", new Object[]{function, lineNum, line});
					
					if ( line.trim().length() > 0 ) {
						input = new HashMap<Integer, Integer>();
						output = new HashMap<Integer, Double>();
						String [] strings = line.split(delimiter);
						if ( null != strings ) {

							// Input TrackID
							input.put(INT_INDEX_TRACKID, convert2Integer(strings, indexTrackId));
							if ( null == input.get(INT_INDEX_TRACKID) ) {
								logger.warn("[{}] value of INT_INDEX_TRACKID[{}] at lineNum[{}] indexTrackId[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_TRACKID, indexTrackId});
							}
							
							//Input DIR
							input.put(INT_INDEX_DIR, convert2Integer(strings, indexDir));
							if ( null == input.get(INT_INDEX_DIR) ) {
								logger.warn("[{}] value of INT_INDEX_DIR[{}] at lineNum[{}] indexDir[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_DIR, indexDir});
							}
							
							//Input Begin
							input.put(INT_INDEX_BEGIN, convert2Integer(strings, indexBegin));
							if ( null == input.get(INT_INDEX_BEGIN) ) {
								logger.warn("[{}] value of INT_INDEX_BEGIN[{}] at lineNum[{}] indexBegin[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_BEGIN, indexBegin});
							}
							
							//Input End
							input.put(INT_INDEX_END, convert2Integer(strings, indexEnd));
							if ( null == input.get(INT_INDEX_END) ) {
								logger.warn("[{}] value of INT_INDEX_END[{}] at lineNum[{}] indexEnd[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_END, indexEnd});
							}

							// Output X
							output.put(INT_INDEX_X, convert2Double(strings, indexX));
							if ( null == output.get(INT_INDEX_X) ) {
								logger.warn("[{}] value of INT_INDEX_X[{}] at lineNum[{}] indexX[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_X, indexX});
							}

							// Output Y
							output.put(INT_INDEX_Y, convert2Double(strings, indexY));
							if ( null == output.get(INT_INDEX_Y) ) {
								logger.warn("[{}] value of INT_INDEX_Y[{}] at lineNum[{}] indexY[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_Y, indexY});
							}

							// Output Angle
							output.put(INT_INDEX_ANGLE, convert2Double(strings, indexAngle));
							if ( null == output.get(INT_INDEX_ANGLE) ) {
								logger.warn("[{}] value of INT_INDEX_ANGLE[{}] at lineNum[{}] indexAngle[{}] IS NULL"
										, new Object[]{function, lineNum, INT_INDEX_ANGLE, indexAngle});
							}
						}
						else {
							logger.warn("[{}] check the delimiter[{}], lineNum[{}] strings IS NULL"
									, new Object[]{function, delimiter, lineNum});
						}
					} else {
						logger.debug("[{}] lineNum[{}] line[{}] IS EMPTY, byPass loading", new Object[]{function, lineNum, line});
					}
				} else {
					logger.warn("[{}] check the csv context, lineNum[{}] IS NULL", function, lineNum);
				}
				
				inputs.put(lineNum, input);
				outputs.put(lineNum, output);
				
				if ( null == input ) 	logger.warn("[{}] input at lineNum[{}] IS NULL", function, lineNum);
				if ( null == output ) 	logger.warn("[{}] output at lineNum[{}] IS NULL", function, lineNum); 
			}
		}
		else {
			logger.debug("[{}] ByPass configuration loading", function);
		}

	}
}
