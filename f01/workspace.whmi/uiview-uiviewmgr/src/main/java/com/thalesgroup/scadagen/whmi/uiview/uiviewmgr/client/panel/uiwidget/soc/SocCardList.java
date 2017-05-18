package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.SocCardList_i.SocCardListParameter;
import com.thalesgroup.scadagen.wrapper.wrapper.client.GetChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class SocCardList implements IDataGridDataSource {

	private final String className = UIWidgetUtil.getClassSimpleName(SocCardList.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String grcPathRoot = "ScadaSoft";
	private final String grcPathAlias = ":ScadaSoft:ScsCtlGrc";
	
	private String strDataGrid_ = "";
	private String [] scsEnvIds_ = null;
	private String optsXMLFile_ = null;
	private UIDataGridDatabase_i dataGridDb_ = null;
	private String separater = ",";
	private String [] strDataGridColumnsFilters = null;
	private String [] strGrcPointAttributes = null;
	private String [] strDataGridColumnsLabels = null;
	private String [] strDataGridColumnsTypes = null;
	private Map<String, String> scsEnvIdMap = new HashMap<String, String>();
	private Map<String, Equipment_i> clientKeyToDataMap = new HashMap<String, Equipment_i>();
	
	private String [] colLblVals = null;
	
	private String colLblSOCCard = null;
	private String colLblScsEnvID = null;
	private String colLblAlias = null;
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();

	@Override
	public void init(String strDataGrid, String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase) {
		final String function = "init";
		logger.begin(className, function);
		
		strDataGrid_ = strDataGrid;
		scsEnvIds_ = scsEnvIds;
		optsXMLFile_ = strDataGridOptsXMLFile;
		dataGridDb_ = uiDataGridDatabase;
		
		readConfig();
		
		logger.end(className, function);
	}
	
	protected void readConfig() {
		final String function = "readConfig";
		logger.begin(className, function);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		String strColumnValueFilters = null;
		String strGrcPointAttribute = null;
		String strColLblVals = null;
		
		if (dataGridDb_ != null) {
			strDataGridColumnsLabels = dataGridDb_.getColumnLabels();
			strDataGridColumnsTypes = dataGridDb_.getColumnTypes();
		}
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strColumnValueFilters = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColumnValueFilters.toString(), strHeader);
			strGrcPointAttribute = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.GrcPointAttributes.toString(), strHeader);
			
			strColLblVals = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColLblVals.toString(), strHeader);
			
			if (strColumnValueFilters != null) {
				strDataGridColumnsFilters = UIWidgetUtil.getStringArray(strColumnValueFilters, separater);
			}
			if (strGrcPointAttribute != null) {
				strGrcPointAttributes = UIWidgetUtil.getStringArray(strGrcPointAttribute, separater);
			}
			
			if (strColLblVals != null) { colLblVals = UIWidgetUtil.getStringArray(strColLblVals, separater); }
			
			final int colLblsCountLimit = 3;
			int colLblsIdx = 0;
			if ( null != colLblVals) {
				if ( colLblVals.length > colLblsIdx ) colLblSOCCard = colLblVals[colLblsIdx++];
				if ( colLblVals.length > colLblsIdx ) colLblScsEnvID = colLblVals[colLblsIdx++];
				if ( colLblVals.length > colLblsIdx ) colLblAlias = colLblVals[colLblsIdx++];
				if ( colLblsCountLimit != colLblsIdx ) { logger.warn(className, function, "colLblsCountLimit[{}] AND colLblsIdx[{}] IS NOT EQUAL IS NULL", colLblsCountLimit, colLblsIdx); }
			} else {
				logger.warn(className, function, "colLblVals IS NULL");
			}
			if ( null != colLblSOCCard && null != colLblScsEnvID && null != colLblAlias ) {
				colLblSOCCard = Translation.getWording(colLblSOCCard);
				colLblScsEnvID = Translation.getWording(colLblScsEnvID);
				colLblAlias = Translation.getWording(colLblAlias);
				
				logger.debug(className, function, "colLblSOCCard[{}] colLblScsEnvID[{}] colLblAlias[{}]", new Object[]{colLblSOCCard,colLblScsEnvID,colLblAlias});
			} else {
				logger.warn(className, function, "colLblSOCCard OR colLblScsEnvID OR colLblAlias IS NULL");
			}
		}
		logger.end(className, function);
	}
	
	private String genClientKey(String scsEnvId) {
		String clientKey = className + "_" + strDataGrid_ + "_" + scsEnvId;
		scsEnvIdMap.put(clientKey, scsEnvId);
		return clientKey;
	}
	
	private String getScsEnvIdFromMap(String key) {
		return scsEnvIdMap.get(key);
	}
	
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		for (String scsEnvId: scsEnvIds_) {
			final String grcAddress = grcPathAlias;
			final String clientKey = genClientKey(scsEnvId);

			rtdb.getChildren(clientKey, scsEnvId, grcAddress, new GetChildrenResult() {
	
				@Override
				public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
					updateSocCardList(clientKey, instances);
				}
			});
		}
		logger.end(className, function);
	}

	protected void updateSocCardList(String clientKey, String[] instances) {
		final String function = "updateSocCardList";
		logger.begin(className, function);
		
		String scsEnvId = getScsEnvIdFromMap(clientKey);
		
		int existingRows = dataGridDb_.getDataProvider().getList().size();
		
		for (int i=0; i<instances.length; i++) {
			
			String key = clientKey + "_" + Integer.toString(existingRows + i);
			
			// Build row data
	    	EquipmentBuilder_i builder = new EquipmentBuilder();
	    	Equipment_i equipment_i = null;

	    	String [] columnValues = new String [strDataGridColumnsLabels.length];
	    	for (int col=0; col<strDataGridColumnsLabels.length; col++) {
	    		// Set column values according to pre-defined labels (SOCCard, ScsEnvID, Alias)
	    		if (strDataGridColumnsLabels[col].compareToIgnoreCase(colLblSOCCard) == 0) {
	    			columnValues[col] = instances[i].substring(instances[i].lastIndexOf(":")+1);
	    			builder.setValue(strDataGridColumnsLabels[col], instances[i].substring(instances[i].lastIndexOf(":")+1));
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase(colLblScsEnvID) == 0) {
	    			columnValues[col] = scsEnvId;
	    			builder.setValue(strDataGridColumnsLabels[col], scsEnvId);
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase(colLblAlias) == 0) {
	    			// Temporary handling. Remove all ":" and leading "ScadaSoft"
	    			String alias = instances[i].replace(":", "");
	    			if (alias.startsWith(grcPathRoot)) {
	    				alias = alias.substring(grcPathRoot.length());
	    			}
	    			columnValues[col] = alias;
	    			builder.setValue(strDataGridColumnsLabels[col], alias);
	    			
	    			//TODO: Handle db full path to alias
	    			
	    			
	    			// Read Grc point attribute (e.g. label)
	    			if (strGrcPointAttributes.length > 0) {
	    				String [] addresses = new String [strGrcPointAttributes.length];
	    				int cnt = 0;
	    				// Add columns
	    				for (String att: strGrcPointAttributes) {
	    					String address = null;
	    					
	    					// Search for '.' to determine if att contains node path
	    					int idx = att.indexOf('.');
	    					if (idx < 0) {
	    						// att NOT contains node path
	    						// append '.' and att to address
	    						address = "<alias>" + alias + "." + att;
	    					} else if (idx == 0) {
	    						// att NOT contains node path
	    						// append att to address
	    						address = "<alias>" + alias + att;
	    					} else {
	    						// att contains node path
	    						if (att.startsWith(":")) {
	    							address = "<alias>" + alias + att;
	    						} else {
	    							address = "<alias>" + alias + ":" + att;
	    						}
	    					}

	    					logger.debug(className, function, "Read Grc point attribute address [{}]", address);
	    					addresses[cnt] = address;
	    					cnt++;
	    				}
	    				
	    				rtdb.multiReadValue(key, scsEnvId, addresses, new MultiReadResult() {

							@Override
							public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
								final String function = "updateSocCardList setReadResult";
								
								logger.begin(className, function);
								// Get row data from map								
								Equipment_i contact = clientKeyToDataMap.get(key);
								if (contact != null) {
									int index = 0;
									
									for (int col=0; col<strDataGridColumnsLabels.length && index < values.length; col++) {
										logger.debug(className, function, "col[{}] label[{}]", col, strDataGridColumnsLabels[col]);
									
										if (!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblSOCCard) &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblScsEnvID) &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblAlias)) {
											
											logger.debug(className, function, "set value for label[{}]", strDataGridColumnsLabels[col]);
											
											// Remove quotes from value string
											String unquotedStr = "";
											if (values[index] != null) {
												unquotedStr = values[index].replaceAll("\"", "");
											}
																													
											if (strDataGridColumnsTypes[col].equalsIgnoreCase("String")) {
												logger.debug(className, function, "set string value [{}]", unquotedStr);
												contact.setStringValue(strDataGridColumnsLabels[col], unquotedStr);
												index++;
											} else if (strDataGridColumnsTypes[col].equalsIgnoreCase("Number")) {
												logger.debug(className, function, "set number value [{}]", unquotedStr);
												contact.setNumberValue(strDataGridColumnsLabels[col], Integer.parseInt(unquotedStr));
												index++;
											} else if (strDataGridColumnsTypes[col].equalsIgnoreCase("Boolean")) {
												logger.debug(className, function, "set boolean value [{}]", unquotedStr);
												contact.setBooleanValue(strDataGridColumnsLabels[col], Boolean.parseBoolean(unquotedStr));
												index++;
											} else {
												logger.warn(className, function, "DataGrid [{}] column type [{}] not supported", strDataGrid_, strDataGridColumnsTypes[col]);
											}
										}
									}
								
									// Handle column filter option
							    	boolean skip = false;
							    	
							    	if (strDataGridColumnsFilters != null) {		
							    		for (int col=0; col<strDataGridColumnsFilters.length; col++) {
							    			String colValue = contact.getValue(strDataGridColumnsLabels[col]);	    			
							    			if (!strDataGridColumnsFilters[col].isEmpty() && !colValue.matches(strDataGridColumnsFilters[col])) {
							    				logger.debug(className, function, "filter[{}]  value[{}] skipped", strDataGridColumnsFilters[col], colValue);
							    				skip = true;
							    				break;
							    			} else {
							    				logger.debug(className, function, "filter[{}]  value[{}] not skipped", strDataGridColumnsFilters[col], colValue);
							    			}
							    		}
							    	}
							    	if (!skip) {
							    		// Add row data to data grid
								    	dataGridDb_.addEquipment(contact);
								    	logger.debug(className, function, "addEquipment");
							    	}
									
									dataGridDb_.refreshDisplays();
								}
								logger.end(className, function);
							}   					
	    				});
	    			}
	    		}	
	    	}
	    	equipment_i = builder.build();
	    	clientKeyToDataMap.put(key, equipment_i);
	    	
	    	if (strGrcPointAttributes.length < 1) {

		    	// Handle column filter option
		    	boolean skip = false;
		    	
		    	if (strDataGridColumnsFilters != null) {		
		    		for (int col=0; col<strDataGridColumnsFilters.length; col++) {
		    			if (!strDataGridColumnsFilters[col].isEmpty() && !columnValues[col].matches(strDataGridColumnsFilters[col])) {
		    				skip = true;
		    				break;
		    			}
		    		}
		    	}
		    	if (skip) {
		    		continue;
		    	}
	    	
		    	// Add row data to data grid
		    	dataGridDb_.addEquipment(equipment_i);
		    	logger.debug(className, function, "addEquipment");
	    	}
		}
		logger.end(className, function);
	}

	@Override
	public void loadData(String scsEnvId, String dbaddress) {

	}

	@Override
	public void disconnect() {
		
	}

	@Override
	public void resetColumnData(String columnLabel, String columnType) {
		
	}
	
	@Override
	public void reloadColumnData(String columnLabel, String columnType, boolean enableTranslation) {
		
	}

}
