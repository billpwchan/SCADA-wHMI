package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.SocCardList_i.SocCardListParameter;
import com.thalesgroup.scadagen.wrapper.wrapper.client.GetChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;

public class SocCardList implements IDataGridDataSource {

	private final String className = UIWidgetUtil.getClassSimpleName(SocCardList.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strDataGrid_ = "";
	private String [] scsEnvIds_ = null;
	private String optsXMLFile_ = null;
	private UIDataGridDatabase_i dataGridDb_ = null;
	private String separater = ",";
	private String [] strDataGridColumnsFilters = null;
	private String [] strGrcPointAttributes = null;
	private final String grcPathRoot = "ScadaSoft";
	private String [] strDataGridColumnsLabels = null;
	private String [] strDataGridColumnsTypes = null;
	private Map<String, String> scsEnvIdMap = new HashMap<String, String>();
	private Map<String, Integer> clientKeyToRowMap = new HashMap<String, Integer>();
	
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
		
		if (dataGridDb_ != null) {
			strDataGridColumnsLabels = dataGridDb_.getColumnLabels();
			strDataGridColumnsTypes = dataGridDb_.getColumnTypes();
		}
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strColumnValueFilters = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColumnValueFilters.toString(), strHeader);
			strGrcPointAttribute = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.GrcPointAttributes.toString(), strHeader);
			if (strColumnValueFilters != null) {
				strDataGridColumnsFilters = UIWidgetUtil.getStringArray(strColumnValueFilters, separater);
			}
			if (strGrcPointAttribute != null) {
				strGrcPointAttributes = UIWidgetUtil.getStringArray(strGrcPointAttribute, separater);
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
			final String grcAddress = ":ScadaSoft:ScsCtlGrc";
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

	    	String [] columnValues = new String [strDataGridColumnsLabels.length];
	    	for (int col=0; col<strDataGridColumnsLabels.length; col++) {
	    		// Set column values according to pre-defined labels (SOCCard, ScsEnvID, Alias)
	    		if (strDataGridColumnsLabels[col].compareToIgnoreCase("SOCCard") == 0) {
	    			columnValues[col] = instances[i].substring(instances[i].lastIndexOf(":")+1);
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase("ScsEnvID") == 0) {
	    			columnValues[col] = scsEnvId;
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase("Alias") == 0) {
	    			// Temporary handling. Remove all ":" and leading "ScadaSoft"
	    			String alias = instances[i].replace(":", "");
	    			if (alias.startsWith(grcPathRoot)) {
	    				alias = alias.substring(grcPathRoot.length());
	    			}
	    			columnValues[col] = alias;
	    			
	    			//TODO: Handle db full path to alias
	    			
	    			
	    			// Read Grc point attribute (e.g. label)
	    			if (strGrcPointAttributes.length > 0) {
	    				String [] addresses = new String [strGrcPointAttributes.length];
	    				int cnt = 0;
	    				// Add columns
	    				for (String att: strGrcPointAttributes) {
	    					String address = "<alias>" + alias + "." + att;

	    					logger.debug(className, function, "Read Grc point attribute address [{}]", address);
	    					addresses[cnt] = address;
	    					cnt++;
	    				}
	    				
	    				String key = clientKey + "_" + Integer.toString(existingRows + i);
	    				
	    				logger.debug(className, function, "clientKeyToRowMap key[{}]  row[{}]", key, existingRows + i);
	    				clientKeyToRowMap.put(key, existingRows + i);
	    				
	    				rtdb.multiReadValue(key, scsEnvId, addresses, new MultiReadResult() {

							@Override
							public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
								final String function = "updateSocCardList setReadResult";
								
								logger.begin(className, function);
								// Get row number from map
								Integer intObj = clientKeyToRowMap.get(key);
								logger.debug(className, function, "clientKey [{}] row [{}]", key, intObj);
								if (intObj != null) {
									int row = intObj;
									int index = 0;
									
									for (int col=0; col<strDataGridColumnsLabels.length && index < values.length; col++) {
										logger.debug(className, function, "col[{}] label[{}]", col, strDataGridColumnsLabels[col]);
									
										if (!strDataGridColumnsLabels[col].equalsIgnoreCase("SOCCard") &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase("ScsEnvID") &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase("Alias")) {
											
											logger.debug(className, function, "set value for label[{}]", strDataGridColumnsLabels[col]);
											
											// Remove quotes from value string
											String unquotedStr = "";
											if (values[col] != null) {
												unquotedStr = values[col].replace("\"", "");
											}
																													
											List<Equipment_i> list = dataGridDb_.getDataProvider().getList();
											Equipment_i contact = list.get(row);
											if (strDataGridColumnsTypes[col].equalsIgnoreCase("String")) {
												logger.debug(className, function, "set string value [{}]", unquotedStr);
												contact.setStringValue(strDataGridColumnsLabels[col], unquotedStr);
												index++;
											} else if (strDataGridColumnsTypes[col].equalsIgnoreCase("Integer")) {
												logger.debug(className, function, "set number value [{}]", unquotedStr);
												contact.setNumberValue(strDataGridColumnsLabels[col], Integer.parseInt(unquotedStr));
												index++;
											} else {
												logger.warn(className, function, "DataGrid [{}] column type [{}] not supported", strDataGrid_, strDataGridColumnsTypes[col]);
											}
										}
									}
									
									dataGridDb_.refreshDisplays();
								}
								logger.end(className, function);
							}   					
	    				});
	    			}
	    		}
	    	}

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
	    	
	    	// Build row data
	    	EquipmentBuilder_i builder = new EquipmentBuilder();
	    	for (int col=0; col<strDataGridColumnsLabels.length; col++) {
	    		builder = builder.setValue(strDataGridColumnsLabels[col], columnValues[col]);
	    		logger.debug(className, function, "builder setValue [{}] [{}]", strDataGridColumnsLabels[col], columnValues[col]);
	    	}
	    	Equipment_i equipment_i = builder.build();
	    	
	    	// Add row data to data grid
	    	dataGridDb_.addEquipment(equipment_i);
	    	logger.debug(className, function, "addEquipment");
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
