package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.GetChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsPollerAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.poller.SubscribeResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.poller.UnSubscribeResult;

public class UIDataGridDatabase implements UIDataGridDatabase_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDataGridDatabase.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, UIDataGridDatabase> instances = new HashMap<String, UIDataGridDatabase>();

	public static UIDataGridDatabase getInstance(String key) {
		if ( ! instances.containsKey(key) ) { instances.put(key, new UIDataGridDatabase()); }
		UIDataGridDatabase instance = instances.get(key);
		return instance;
	}
	private UIDataGridDatabase() {}
	
	private String strDataGrid = null;
	
	private String [] strDataGridColumnsLabels = null;
	
	private String [] strDataGridColumnsTypes = null;
	
	private String [] strDataGridColumnsFilters = null;
	
	private String [] strDataGridColumnsSourceTableIndexes = null;
	
	private Set<String> scsEnvIdSet = new HashSet<String>();
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();
	
	private Map<String, String> subscriptionMap = new HashMap<String, String>();
	
	private final String [] brctableFields = { "number", "brctype", "label", "inhibflag", "exestatus", "succns",
			"succdelay", "failns", "faildelay", "enveqp", "eqp", "cmdname", "cmdval", "cmdlabel", "cmdtype",
			"maxretry", "bpretcond", "bpinitcond", "sndbehavr" };

	/**
	 * The provider that holds the list of contacts in the database.
	 */
	private ListDataProvider<Equipment_i> dataProvider = new ListDataProvider<Equipment_i>();

	/**
	 * Add a new contact.
	 * 
	 * @param equipment
	 *            the contact to add.
	 */
	@Override
	public void addEquipment(Equipment_i equipment) {
		List<Equipment_i> equipments = dataProvider.getList();
		// Remove the contact first so we don't add a duplicate.
		equipments.remove(equipment);
		equipments.add(equipment);
	}
	
	public void clearEquipment() {
		List<Equipment_i> equipments = dataProvider.getList();
		equipments.clear();
	}
	
	public void setEquipment(Equipment_i equipment) {
		List<Equipment_i> equipments = dataProvider.getList();
		equipments.clear();
		equipments.add(equipment);
	}

	/**
	 * Add a display to the database. The current range of interest of the
	 * display will be populated with data.
	 * 
	 * @param display a {@Link HasData}.
	 */
	@Override
	public void addDataDisplay(HasData<Equipment_i> display) {
		dataProvider.addDataDisplay(display);
	}

	@Override
	public ListDataProvider<Equipment_i> getDataProvider() {
		return dataProvider;
	}

	/**
	 * Refresh all displays.
	 */
	@Override
	public void refreshDisplays() {
		dataProvider.refresh();
	}

	public void setScsEnv(String strDataGrid, String scsEnvIdsStr, String [] strDataGridColumnsLabels, String []strDataGridColumnsTypes, String[] strDataGridColumnsFilters, String[] strDataGridColumnsSourceTableIndexes) {
		final String function = "setScsEnv";
		
		logger.begin(className, function);
		
		if (scsEnvIdsStr != null) {
			logger.debug(className, function, "scsEnvIdsStr [{}]", scsEnvIdsStr);
			String [] tokens = scsEnvIdsStr.split(",");
			
			// Clear existing scsEnvIdSet
			scsEnvIdSet.clear();
			
			// Add new scsEnvIdSet
			for (String token: tokens) {
				scsEnvIdSet.add(token.trim());
			}
		}
		
		this.strDataGrid = strDataGrid;
		this.strDataGridColumnsLabels = strDataGridColumnsLabels;
		
		if (strDataGridColumnsLabels != null) {
			for (String label: strDataGridColumnsLabels) {
				logger.debug(className, function, "label=[{}]", label);
			}
		}
		
		this.strDataGridColumnsTypes = strDataGridColumnsTypes;
		if (strDataGridColumnsTypes != null) {
			for (String type: strDataGridColumnsTypes) {
				logger.debug(className, function, "type=[{}]", type);
			}
		}
		
		this.strDataGridColumnsFilters = strDataGridColumnsFilters;
		
		if (strDataGridColumnsFilters != null) {
			for (String filter: strDataGridColumnsFilters) {
				logger.debug(className, function, "filter=[{}]", filter);
			}
		}
		
		this.strDataGridColumnsSourceTableIndexes = strDataGridColumnsSourceTableIndexes;
		
		if (strDataGridColumnsSourceTableIndexes != null) {
			for (String index: strDataGridColumnsSourceTableIndexes) {
				logger.debug(className, function, "index=[{}]", index);
			}
		}
		
		logger.end(className, function);
	}
	
	public void connect() {
		final String function = "connect";
		
		logger.begin(className, function);

		// Clear data grid
		clearEquipment();
	
		if (strDataGrid != null && !scsEnvIdSet.isEmpty()) {
			if ( strDataGrid.equals("UIDataGridFomatterSOC") ) {
				
				for (String scsEnvId: scsEnvIdSet) {

					final String scsEnvId_ = scsEnvId;
					String clientKey = "strDataGrid" + "_" + scsEnvId;
					String dbaddress = ":ScadaSoft:ScsCtlGrc";
					
					rtdb.getChildren(clientKey, scsEnvId_, dbaddress, new GetChildrenResult() {

						@Override
						public void setGetChildrenResult(String clientKey, String[] instances, int errorCode,
								String errorMessage) {
							for (int i=0; i<instances.length; i++) {
						    	EquipmentBuilder_i builder = new EquipmentBuilder();
						    	
						    	String [] columnValues = new String [3];
						    	
						    	columnValues[0] = instances[i].substring(instances[i].lastIndexOf(":")+1);
						    	columnValues[1] = scsEnvId_;
						    	columnValues[2] = instances[i];
						    	boolean skip = false;
						    	
						    	if (strDataGridColumnsFilters != null) {		
						    		for (int j=0; j<strDataGridColumnsFilters.length; j++) {
						    			if (!strDataGridColumnsFilters[j].isEmpty() && !columnValues[j].matches(strDataGridColumnsFilters[j])) {
						    				skip = true;
						    				break;
						    			}
						    		}
						    	}
						    	if (skip) {
						    		continue;
						    	}
						    	Equipment_i equipment_i = builder
						    			.setValue(strDataGridColumnsLabels[0], columnValues[0])
						    			.setValue(strDataGridColumnsLabels[1], columnValues[1])
						    			.setValue(strDataGridColumnsLabels[2], columnValues[2])
						    			.build();
						    	addEquipment(equipment_i);

							}
						}
						
					});
					
//					db.addStaticRequest(api, clientKey, scsEnvId, dbaddress, new DatabaseEvent() {
//						
//						@Override
//						public void update(String key, String[] value) {
//							for (int i=0; i<value.length; i++) {
//						    	EquipmentBuilder_i builder = new EquipmentBuilder();
//						    	
//						    	String [] columnValues = new String [3];
//						    	
//						    	columnValues[0] = value[i].substring(value[i].lastIndexOf(":")+1);
//						    	columnValues[1] = scsEnvId_;
//						    	columnValues[2] = value[i];
//						    	boolean skip = false;
//						    	
//						    	if (strDataGridColumnsFilters != null) {		
//						    		for (int j=0; j<strDataGridColumnsFilters.length; j++) {
//						    			if (!strDataGridColumnsFilters[j].isEmpty() && !columnValues[j].matches(strDataGridColumnsFilters[j])) {
//						    				skip = true;
//						    				break;
//						    			}
//						    		}
//						    	}
//						    	if (skip) {
//						    		continue;
//						    	}
//						    	Equipment_i equipment_i = builder
//						    			.setValue(strDataGridColumnsLabels[0], columnValues[0])
//						    			.setValue(strDataGridColumnsLabels[1], columnValues[1])
//						    			.setValue(strDataGridColumnsLabels[2], columnValues[2])
//						    			.build();
//						    	addEquipment(equipment_i);
//
//							}
//						}
//						
//					});
				}

		    } else if ( strDataGrid.equals("UIDataGridFomatterSOCDetails") ) {
		    	
		    	// No SOC Detail until SOC Card is selected

		    }
		} else {
			logger.error(className, function, "strDataGrid or scsEnvId is null");
		}
		
		logger.end(className, function);
	}
	
	public void disconnect() {

	}
	
	// input dbaddress is db point address
	public void loadData(String clientKey, String scsEnvId, String dbaddress) {
		final String function = "loadData";
		
		logger.begin(className, function);
		
		clearEquipment();
		
		if ( strDataGrid.equals("UIDataGridFomatterSOCDetails") && 
				(strDataGridColumnsSourceTableIndexes != null) && 
				(strDataGridColumnsSourceTableIndexes.length > 0)) {
				
			//String brctablePath = dbaddress + ".brctable(0:$, label)";
			String [] indexHolder = new String[strDataGridColumnsSourceTableIndexes.length];
			
			int count = 0;
			int numberCol = -1;
			boolean numberColFound = false;
			for (String indexStr: strDataGridColumnsSourceTableIndexes) {
				int col = -1;
				if (indexStr != null) {
					try {
						// Expected format for index string \d+(:\d+)?
						// First token is the column index (0 based index)
						// Separator is colon (:)
						// Second token is the token number
						String [] tokens = indexStr.split(":");
						col = Integer.parseInt(tokens[0]);
						
						//TODO: Handle second token
						
					} catch (Exception e) {
						logger.warn(className, function, "[{}]", e.getMessage());
						continue;
					}
				}
				if (col >= 0 && col < brctableFields.length) {
					if (col == 0) {
						numberColFound = true;
						numberCol = col;
					}
					indexHolder[count++] = dbaddress + ".brctable(0:$, " + brctableFields[col] + ")";
				}
			}
			
			if (!numberColFound) {
				// Add number column at the end of the subscription list
				numberCol = count;
				count++;
			}

			String [] dbaddresses = new String [count];
			if (!numberColFound) {
				for (int i=0; i<count-1; i++) {
					dbaddresses[i] = indexHolder[i];
					logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
				}
				dbaddresses[numberCol] = dbaddress + ".brctable(0:$, " + brctableFields[0] + ")";
			} else {
				for (int i=0; i<count; i++) {
					dbaddresses[i] = indexHolder[i];
					logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
				}
			}
		
			logger.debug(className, function, "clientKey=[{}] scsEnvId=[{}]", clientKey, scsEnvId);

			// Use multiRead to read brctable		
			rtdb.multiReadValue(clientKey, scsEnvId, dbaddresses, new MultiReadResult() {

				@Override
				public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
					final String function = "setReadResult";
					int numberCol = 0;
					
					// Find number column
					boolean numberColFound = false;
					for (int col=0; col<strDataGridColumnsSourceTableIndexes.length; col++) {
						if (strDataGridColumnsSourceTableIndexes[col].compareTo("0") == 0) {
							logger.debug(className, function, "numberColFound=true at col=[{}]", col);
							numberColFound = true;
							numberCol = col;
							break;						
						}
					}
					if (!numberColFound) {
						numberCol = values.length;
						logger.debug(className, function, "numberColFound=false; set numberCol=[{}]", numberCol);
					}
					
					// Get number of steps from number col
					if (values != null && values.length > 0) {
						String [] numbers = values[numberCol].replaceAll("\"", "").split(",");
						int numSteps = 0;
						
						for (String num: numbers) {
							if (numSteps > 0 && num.compareTo("0")==0) {
								break;
							}
							numSteps++;
						}
					
						logger.debug(className, function, "numSteps=[{}] ", numSteps);
						logger.debug(className, function, "strDataGridColumnsLabels.length=[{}] ", strDataGridColumnsLabels.length);
						
						String [][] columnValues = new String [strDataGridColumnsLabels.length][numSteps];
						
						for (int col=0; col<strDataGridColumnsLabels.length; col++) {
							logger.debug(className, function, "col=[{}] value=[{}]", col, values[col]);
							
							String removedBracketStr = values[col].substring(1, values[col].length()-1);
							
							String unquotedStr = removedBracketStr.replaceAll("\"", "");
							
							logger.debug(className, function, "unquotedStr=[{}] ", unquotedStr);
							
							String [] splittedStr = unquotedStr.split(",");
							
							logger.debug(className, function, "splittedStr.length=[{}] ", splittedStr.length);
							
							for (int row=0; row<numSteps; row++) {
								columnValues[col][row] = splittedStr[row];
								logger.debug(className, function, "row=[{}] columnValues=[{}] ", row, splittedStr[row]);
							}
						}

						for (int row=0; row<numSteps; row++) {
							EquipmentBuilder_i builder = new EquipmentBuilder();
							
							for (int col=0; col<strDataGridColumnsLabels.length; col++) {
								if (strDataGridColumnsTypes[col].compareToIgnoreCase("Number") == 0) {
									builder = builder.setValue(strDataGridColumnsLabels[col], Integer.parseInt(columnValues[col][row]));
								} else {
									builder = builder.setValue(strDataGridColumnsLabels[col], columnValues[col][row]);
								}
							}
							Equipment_i equipment_i = builder.build();
							addEquipment(equipment_i);
						}
					}
				}
					
//			    	EquipmentBuilder_i builder = new EquipmentBuilder();
//			    	
//			    	String [] columnValues = new String [3];
//			    	
//			    	columnValues[0] = "";
//			    	columnValues[1] = "";
//			    	columnValues[2] = "";
//
//			    	Equipment_i equipment_i = builder
//			    			.setValue(strDataGridColumnsLabels[0], columnValues[0])
//			    			.setValue(strDataGridColumnsLabels[1], columnValues[1])
//			    			.setValue(strDataGridColumnsLabels[2], columnValues[2])
//			    			.build();
//			    	addEquipment(equipment_i);

				
			});
			
//			db.addStaticRequest(api, clientKey, scsEnvId_, brctablePath, new DatabaseEvent() {
//				
//				@Override
//				public void update(String key, String[] values) {
//					
//					for (String val: values) {
//						logger.debug(className, "update", "value = [{}]", val);
//					}
//					
//			    	EquipmentBuilder_i builder = new EquipmentBuilder();
//			    	
//			    	String [] columnValues = new String [3];
//			    	
//			    	columnValues[0] = "";
//			    	columnValues[1] = "";
//			    	columnValues[2] = "";
//
//			    	Equipment_i equipment_i = builder
//			    			.setValue(strDataGridColumnsLabels[0], columnValues[0])
//			    			.setValue(strDataGridColumnsLabels[1], columnValues[1])
//			    			.setValue(strDataGridColumnsLabels[2], columnValues[2])
//			    			.build();
//			    	addEquipment(equipment_i);
//
//				}
//				
//			});
		}
		
		logger.end(className, function);
	}

	
	private void subscribe(String key, String scsEnvId, String dbaddress) {
		final String function = "subscribe";
		
		logger.begin(className, function);
		
		String groupName = key;
		int periodMS = 0;
		String [] dataFields = new String [1];
		dataFields[0] = dbaddress;
	
		SubscribeResult subResult = new SubscribeResult() {

			@Override
			public void update() {
				final String className ="SubscribeResult";
				final String function ="update";

				String key_ = getKey();
				
				String subscriptionId = getSubUUID();
				logger.debug(className, function, "subUUID = [{}]", subscriptionId);
				
				subscriptionMap.put(key_, subscriptionId);
				
				String [] values = getValues();

				for (String value: values) {
					logger.debug(className, function, "value=[{}]", value);
				}
			}
		};
		WrapperScsPollerAccess poller = WrapperScsPollerAccess.getInstance();
		poller.subscribe(key, scsEnvId, groupName, dataFields, periodMS, subResult);

		logger.end(className, function);
	}

	
	private void unSubscribe(String key, String scsEnvId, String subscriptionId) {
		final String function = "unSubscribe";
		
		logger.begin(className, function);

		String groupName = key;

		UnSubscribeResult unsubResult = new UnSubscribeResult();
		
		WrapperScsPollerAccess poller = WrapperScsPollerAccess.getInstance();
		poller.unSubscribe(key, scsEnvId, groupName, subscriptionId, unsubResult);
		
		logger.end(className, function);
	}
	
}
