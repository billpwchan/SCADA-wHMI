package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.SocCardList_i.SocCardListParameter;
import com.thalesgroup.scadagen.wrapper.wrapper.client.GetChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class SocCardList implements IDataGridDataSource {

	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
	private int [] intDataGridColumnsTranslations = null;
	private Map<String, String> scsEnvIdMap = new HashMap<String, String>();
	private Map<String, Equipment_i> clientKeyToDataMap = new HashMap<String, Equipment_i>();
	private List<String> clientKeyList = new ArrayList<String>();
	private Map<String, String> runTimeFilters = new HashMap<String, String>();
	
	private String [] colLblVals = null;
	private String CheckCardOPM = null;
	private String CheckOPMAPI = null;
	private String CheckOPMFunction = null;
	private String CheckOPMLocation = null;
	private String CheckOPMScope = null;
	private String CheckOPMMode = null;
	
	private String colLblSOCCard = null;
	private String colLblScsEnvID = null;
	private String colLblAlias = null;
	
	
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();

	@Override
	public void init(String strDataGrid, String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase) {
		final String function = "init";
		logger.begin(function);
		
		strDataGrid_ = strDataGrid;
		scsEnvIds_ = scsEnvIds;
		optsXMLFile_ = strDataGridOptsXMLFile;
		dataGridDb_ = uiDataGridDatabase;
		
		readConfig();
		
		logger.end(function);
	}
	
	protected void readConfig() {
		final String function = "readConfig";
		logger.begin(function);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		String strColumnValueFilters = null;
		String strGrcPointAttribute = null;
		String strColLblVals = null;
		
		if (dataGridDb_ != null) {
			strDataGridColumnsLabels = dataGridDb_.getColumnLabels();
			strDataGridColumnsTypes = dataGridDb_.getColumnTypes();
			intDataGridColumnsTranslations = dataGridDb_.getColumnTranslation();
		}
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strColumnValueFilters = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColumnValueFilters.toString(), strHeader);
			strGrcPointAttribute = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.GrcPointAttributes.toString(), strHeader);
			
			strColLblVals = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColLblVals.toString(), strHeader);
			
			CheckCardOPM = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckCardOPM.toString(), strHeader);
			CheckOPMAPI = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckOPMAPI.toString(), strHeader);
			CheckOPMFunction = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckOPMFunction.toString(), strHeader);
			CheckOPMLocation = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckOPMLocation.toString(), strHeader);
			CheckOPMScope = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckOPMScope.toString(), strHeader);
			CheckOPMMode = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.CheckOPMMode.toString(), strHeader);
			
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
				if ( colLblsCountLimit != colLblsIdx ) { logger.warn(function, "colLblsCountLimit[{}] AND colLblsIdx[{}] IS NOT EQUAL IS NULL", colLblsCountLimit, colLblsIdx); }
			} else {
				logger.warn(function, "colLblVals IS NULL");
			}
			if ( null != colLblSOCCard && null != colLblScsEnvID && null != colLblAlias ) {
				colLblSOCCard = Translation.getWording(colLblSOCCard);
				colLblScsEnvID = Translation.getWording(colLblScsEnvID);
				colLblAlias = Translation.getWording(colLblAlias);
				
				logger.debug(function, "colLblSOCCard[{}] colLblScsEnvID[{}] colLblAlias[{}]", new Object[]{colLblSOCCard,colLblScsEnvID,colLblAlias});
			} else {
				logger.warn(function, "colLblSOCCard OR colLblScsEnvID OR colLblAlias IS NULL");
			}
		}
		logger.end(function);
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
		logger.begin(function);
		
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
		logger.end(function);
	}

	protected void updateSocCardList(String clientKey, String[] instances) {
		final String function = "updateSocCardList";
		logger.begin(function);
		
		
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
	    			// NO translation supported for SOCCard name
	    			columnValues[col] = instances[i].substring(instances[i].lastIndexOf(":")+1);
	    			builder.setValue(strDataGridColumnsLabels[col], instances[i].substring(instances[i].lastIndexOf(":")+1));
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase(colLblScsEnvID) == 0) {
	    			// NO translation supported for ScsEnvID name
	    			columnValues[col] = scsEnvId;
	    			builder.setValue(strDataGridColumnsLabels[col], scsEnvId);
	    		} else if (strDataGridColumnsLabels[col].compareToIgnoreCase(colLblAlias) == 0) {
	    			// NO translation supported for Alias
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

	    					logger.debug(function, "Read Grc point attribute address [{}]", address);
	    					addresses[cnt] = address;
	    					cnt++;
	    				}
	    				
	    				rtdb.multiReadValue(key, scsEnvId, addresses, new MultiReadResult() {

							@Override
							public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
								final String function = "updateSocCardList setReadResult";
								
								logger.begin(function);
								// Get row data from map								
								Equipment_i contact = clientKeyToDataMap.get(key);
								
								String EqpFunctionValue = null;
								String EqpLocationValue = null;
								
								if (contact != null) {
									int index = 0;
									
									for (int col=0; col<strDataGridColumnsLabels.length && index < values.length; col++) {
										logger.debug(function, "col[{}] label[{}]", col, strDataGridColumnsLabels[col]);
									
										if (!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblSOCCard) &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblScsEnvID) &&
											!strDataGridColumnsLabels[col].equalsIgnoreCase(colLblAlias)) {
											
											logger.debug(function, "set value for label[{}]", strDataGridColumnsLabels[col]);
											
											// Remove quotes from value string
											String unquotedStr = "";
											if (values[index] != null) {
												unquotedStr = values[index].replaceAll("\"", "");
												logger.debug(function, "column value before translation: [{}]", unquotedStr);
											}
											
											if(strDataGridColumnsLabels[col].equalsIgnoreCase(CheckOPMFunction)){
												EqpFunctionValue = unquotedStr;
												logger.debug(function, "Eqp Function Value is:[{}]", EqpFunctionValue);
											}
											if(strDataGridColumnsLabels[col].equalsIgnoreCase(CheckOPMLocation)){
												EqpLocationValue = unquotedStr;
												logger.debug(function, "Eqp Location Value is:[{}]", EqpLocationValue);
											}
											
																													
											if (strDataGridColumnsTypes[col].equalsIgnoreCase("String")) {
												
												
												if (intDataGridColumnsTranslations != null && intDataGridColumnsTranslations.length > col && intDataGridColumnsTranslations[col] == 1) {
													String translateKey = "&" + className + strDataGridColumnsLabels[col].replace(' ', '_') + "_" + unquotedStr;
													String translatedString = Translation.getWording(translateKey);
													logger.debug(function, "set translated string value [{}]", translatedString);
													contact.setStringValue(strDataGridColumnsLabels[col], translatedString);
												} else {
													logger.debug(function, "set string value [{}]", unquotedStr);
													contact.setStringValue(strDataGridColumnsLabels[col], unquotedStr);
												}
												index++;
											} else if (strDataGridColumnsTypes[col].equalsIgnoreCase("Number")) {
												logger.debug(function, "set number value [{}]", unquotedStr);

												// No translation supported for column showing number value
												contact.setNumberValue(strDataGridColumnsLabels[col], Integer.parseInt(unquotedStr));
												index++;
											} else if (strDataGridColumnsTypes[col].equalsIgnoreCase("Boolean")) {
												logger.debug(function, "set boolean value [{}]", unquotedStr);
												
												// No translation supported for column showing boolean value
												contact.setBooleanValue(strDataGridColumnsLabels[col], Boolean.parseBoolean(unquotedStr));
												index++;
											} else {
												logger.warn(function, "DataGrid [{}] column type [{}] not supported", strDataGrid_, strDataGridColumnsTypes[col]);
											}
										}
									}
								
									// Handle column filter option
							    	boolean skip = false;
							    	
							    	// Check OPM before checking filters
							    	if (CheckCardOPM != null && CheckCardOPM == "true"){
							    		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(CheckOPMAPI);
							    		boolean socCardOpm = uiOpm_i.checkAccess(EqpFunctionValue, EqpLocationValue, CheckOPMScope, CheckOPMMode);
							    		logger.debug(function, "SOC card OPM check result: [{}]", socCardOpm);
							    	
							    		if (!socCardOpm){
							    			skip = true;
							    		} else {
							    	    	clientKeyList.add(key);
							    		}
							    		
							    	}
							    	// Check filters
							    	if (strDataGridColumnsFilters != null) {		
							    		for (int col=0; col<strDataGridColumnsFilters.length; col++) {
							    			String colValue = contact.getValue(strDataGridColumnsLabels[col]);	    			
							    			if (!strDataGridColumnsFilters[col].isEmpty() && !colValue.matches(strDataGridColumnsFilters[col])) {
							    				logger.debug(function, "filter[{}]  value[{}] skipped", strDataGridColumnsFilters[col], colValue);
							    				skip = true;
							    				break;
							    			} else {
							    				logger.debug(function, "filter[{}]  value[{}] not skipped", strDataGridColumnsFilters[col], colValue);
							    			}
							    		}
							    	}
							    	if (!skip) {
							    		// Add row data to data grid
								    	dataGridDb_.addEquipment(contact);
								    	logger.debug(function, "addEquipment");
							    	}
									
									dataGridDb_.refreshDisplays();
								}
								logger.end(function);
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
		    	logger.debug(function, "addEquipment");
	    	}
		}
		logger.end(function);
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
	public void reloadColumnData(String[] columnLabels, String[] columnTypes, boolean[] enableTranslations) {
		
	}

	@Override
	public void changeColumnFilter(Map<String, String> filterMap) {
		final String function = "changeColumnFilter";
	
		// Clear datagrid
		dataGridDb_.getDataProvider().getList().clear();
		
		if (!filterMap.isEmpty()) {
			for (String filterKey: filterMap.keySet()) {
				String filterValue = filterMap.get(filterKey);
				if (filterValue != null && !filterValue.isEmpty()) {
					runTimeFilters.put(filterKey, filterValue);
					logger.debug(function, "runtime filter key[{}]  value[{}]", filterKey, filterMap.get(filterValue));
				} else {
					runTimeFilters.remove(filterKey);
				}
			}
		} else {
			logger.debug(function, "runtime filter clear all");
			runTimeFilters.clear();
		}
		
		for (String key: clientKeyList) {
			Equipment_i contact = clientKeyToDataMap.get(key);
	
			// Handle column filter option
	    	boolean skip = false;
		    	
	    	if (strDataGridColumnsFilters != null) {	
	    		logger.debug(function, "Check static column filter");
	    		for (int col=0; col<strDataGridColumnsFilters.length; col++) {
	    			String colValue = contact.getValue(strDataGridColumnsLabels[col]);	    			
	    			if (!strDataGridColumnsFilters[col].isEmpty() && !colValue.matches(strDataGridColumnsFilters[col])) {
	    				logger.debug(function, "filter[{}]  value[{}] skipped", strDataGridColumnsFilters[col], colValue);
	    				skip = true;
	    				break;
	    			}
	    		}
	    	}
	    	
	    	if (!skip) {
	    		logger.debug(function, "Check runtime column filter size[{}]", runTimeFilters.size());
	    		for (String filterColumn: runTimeFilters.keySet()) {    			
	    			String colValue = contact.getValue(filterColumn);	 
	    			logger.debug(function, "filter column [{}] data [{}]", filterColumn, colValue);
	    			if (!runTimeFilters.get(filterColumn).isEmpty() && colValue != null && !colValue.matches(runTimeFilters.get(filterColumn))) {
	    				logger.debug(function, "filter[{}]  value[{}] skipped", runTimeFilters.get(filterColumn), colValue);
	    				skip = true;
	    				break;
	    			} else {
	    				logger.debug(function, "filter[{}]  value[{}] not skipped", runTimeFilters.get(filterColumn), colValue);
	    			}
	    		}
	    		
	    		if (!skip) {
		    		// Add row data to data grid
			    	dataGridDb_.addEquipment(contact);
			    	logger.debug(function, "addEquipment");
	    		}
	    	}
		}
		
		dataGridDb_.refreshDisplays();
	}

}
