package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.SocCardDetail_i.SocCardDetailParameter;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsPollerAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.poller.SubscribeResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.UUIDWrapper;

public class SocCardDetail implements IDataGridDataSource {
	
	private final String className = UIWidgetUtil.getClassSimpleName(SocCardDetail.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private class Cell {
		int row;
		int column;
		
		public Cell(int r, int c) {
			row = r;
			column = c;
		}
	}
	private Map<String, Set<Cell>> addressToCellMap = new HashMap<String, Set<Cell>>();
	
	private final String [] brctableFields = { "number", "brctype", "label", "inhibflag", "exestatus", "succns",
	"succdelay", "failns", "faildelay", "enveqp", "eqp", "cmdname", "cmdval", "cmdlabel", "cmdtype",
	"maxretry", "bpretcond", "bpinitcond", "sndbehavr" };
	
	private String scsEnvId_ = null;	
	private String optsXMLFile_ = null;
	private UIDataGridDatabase_i dataGrid_ = null;
	
	private String [] strDataGridColumnsLabels = null;
	private String [] strDataGridColumnsTypes = null;
	private String [] brcTableIndexes_ = null;
	private String [] extraRelativePoints_ = null;

	private final String separater = ",";
	private int extraCol = -1;
	
	private String [][] dataGridValues = null;
	
	private int subscriptionPeriod = 0;
	
	private String subscriptionId_ = "";
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();
	
	private WrapperScsPollerAccess poller = WrapperScsPollerAccess.getInstance();

	// Create unique clientKey
	private String clientKey = "SocCardDetail" + "_" + UUIDWrapper.getUUID();


	@Override
	public void init(String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase) {
		final String function = "init";	
		logger.begin(className, function);
		
		optsXMLFile_ = strDataGridOptsXMLFile;
		dataGrid_ = uiDataGridDatabase;
		
		readConfig();
		
		logger.end(className, function);
	}
	
	protected void readConfig() {
		final String function = "readConfig";	
		logger.begin(className, function);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";

		String strBrcTableIndexes = null;
		String strExtraRelativePoints = null;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {		
			strBrcTableIndexes = dictionariesCache.getStringValue(optsXMLFile_, SocCardDetailParameter.BrcTableIndexes.toString(), strHeader);
			strExtraRelativePoints = dictionariesCache.getStringValue(optsXMLFile_, SocCardDetailParameter.ExtraRelativePoints.toString(), strHeader);
		}
		
		if (strBrcTableIndexes != null) {
			String [] tokens = UIWidgetUtil.getStringArray(strBrcTableIndexes, separater);
			brcTableIndexes_ = new String [tokens.length];
			for (int i=0; i<tokens.length; i++) {
				brcTableIndexes_[i] = tokens[i].trim();
			}
		}
		
		if (strExtraRelativePoints != null) {
			String [] tokens = UIWidgetUtil.getStringArray(strExtraRelativePoints, separater);
			extraRelativePoints_ = new String [tokens.length];
			for (int i=0; i<tokens.length; i++) {
				extraRelativePoints_[i] = tokens[i].trim();
			}
		}
		
		if (dataGrid_ != null) {
			strDataGridColumnsLabels = dataGrid_.getColumnLabels();
			strDataGridColumnsTypes = dataGrid_.getColumnTypes();
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void connect() {
		
	}

	@Override
	public void loadData(String scsEnvId, String dbaddress) {		
		final String function = "loadData";	
		logger.begin(className, function);
		
		scsEnvId_ = scsEnvId;
		String [] indexHolder = new String[brcTableIndexes_.length];
		
		int count = 0;
		int numberCol = -1;
		int eqpCol = -1;
		boolean numberColFound = false;
		boolean eqpColFound = false;

		for (String indexStr: brcTableIndexes_) {
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
				} else if (col == 10) {
					eqpColFound = true;
					eqpCol = col;
				}
				indexHolder[count++] = "<alias>" + dbaddress + ".brctable(0:$, " + brctableFields[col] + ")";
			}
		}
		
		if (!numberColFound) {
			// Add number column at the end of the subscription list
			numberCol = count;
			count++;
		}
		if (!eqpColFound) {
			// Add eqp column at the end of the subscription list
			eqpCol = count;
			count++;
		}

		String [] dbaddresses = new String [count];
		if (!numberColFound && eqpColFound) {
			for (int i=0; i<count-1; i++) {
				dbaddresses[i] = indexHolder[i];
				logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			dbaddresses[numberCol] = "<alias>" + dbaddress + ".brctable(0:$, " + brctableFields[0] + ")";
			extraCol = count-1;
		} else if (numberColFound && !eqpColFound) {
			for (int i=0; i<count-1; i++) {
				dbaddresses[i] = indexHolder[i];
				logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			dbaddresses[eqpCol] = "<alias>" + dbaddress + ".brctable(0:$, " + brctableFields[10] + ")";
			extraCol = count-1;
		} else if (!numberColFound && !eqpColFound) {
			for (int i=0; i<count-2; i++) {
				dbaddresses[i] = indexHolder[i];
				logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			dbaddresses[numberCol] = "<alias>" + dbaddress + ".brctable(0:$, " + brctableFields[0] + ")";
			dbaddresses[eqpCol] = "<alias>" + dbaddress + ".brctable(0:$, " + brctableFields[10] + ")";
			extraCol = count-2;
		} else {
			for (int i=0; i<count; i++) {
				dbaddresses[i] = indexHolder[i];
				logger.debug(className, function, "dbaddresses[{}]=[{}]", i, dbaddresses[i]);
			}
			extraCol = count;
		}
	
		logger.debug(className, function, "clientKey=[{}] scsEnvId=[{}]", clientKey, scsEnvId_);
		
		// Use multiRead to read brctable		
		rtdb.multiReadValue(clientKey, scsEnvId_, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				updateDataGridBrcTableValues(values);
			}

		});
		
		logger.end(className, function);
	}
	
	protected void subscribe(String [] extraPointsAddresses) {
		final String function ="subscribe";	
		logger.begin(className, function);
		
		poller.subscribe(clientKey, scsEnvId_, clientKey, extraPointsAddresses, subscriptionPeriod, new SubscribeResult() {
			@Override
			public void update() {
				updateDataGridExtraPointsValues(getSubUUID(), getDbAddresses(), getValues());
			}
		});
		
		logger.end(className, function);
	}
	
	protected void unsubscribe() {
		final String function ="unsubscribe";	
		logger.begin(className, function);
		
		if (subscriptionId_ != null) {
			poller.unSubscribe(clientKey, scsEnvId_, clientKey, subscriptionId_, null);
		}
		
		logger.end(className, function);
	}

	@Override
	public void disconnect() {
		unsubscribe();
	}

	protected void updateDataGridExtraPointsValues(String subscriptionId, String[] addresses, String[] values) {
		final String function ="updateDataGridExtraPointsValues";		
		logger.begin(className, function);
	
		subscriptionId_ = subscriptionId;
		
		logger.debug(className, function, "subscriptionId = [{}]", subscriptionId_);
		
		Set<Integer> rows = new HashSet<Integer>(); 

		for (int i=0; i<values.length && i<addresses.length; i++) {
			logger.debug(className, function, "address=[{}]  value=[{}]", addresses[i], values[i]);
			
			Set<Cell> s = addressToCellMap.get(addresses[i]);
			if (s != null && !s.isEmpty()) {
				for (Cell c: s) {
					dataGridValues[c.row][c.column] = values[i];
					logger.debug(className, function, "update value to dataGridValues at row[{}] column[{}]", c.row, c.column);
					if (!rows.contains(c.row)) {
						rows.add(c.row);
					}
				}
			} else {
				logger.warn(className, function, "addressToCellMap entry not found for address [{}]", addresses[i]);
			}
		}
				
		for (Integer r: rows) {
			EquipmentBuilder_i builder = new EquipmentBuilder();
			
			for (int col=0; col<strDataGridColumnsLabels.length; col++) {
				if (strDataGridColumnsTypes[col].compareToIgnoreCase("String") == 0) {
					builder = builder.setValue(strDataGridColumnsLabels[col], dataGridValues[r][col]);
				} else if (strDataGridColumnsTypes[col].compareToIgnoreCase("Number") == 0) {
					builder = builder.setValue(strDataGridColumnsLabels[col], Integer.parseInt(dataGridValues[r][col]));
				} else if (strDataGridColumnsTypes[col].compareToIgnoreCase("Boolean") == 0) {
					builder = builder.setValue(strDataGridColumnsLabels[col], Boolean.parseBoolean(dataGridValues[r][col]));
				}
	    		logger.debug(className, function, "builder setValue [{}] [{}]", strDataGridColumnsLabels[col], dataGridValues[r][col]);
			}
			
			Equipment_i equipment_i = builder.build();
			dataGrid_.updateEquipmentElement(r, equipment_i);
		}
		dataGrid_.refreshDisplays();
		
		logger.end(className, function);
	}

	protected void updateDataGridBrcTableValues(String[] values) {
		final String function = "updateDataGridBrcTableValues";
		logger.begin(className, function);
		
		if (values != null && values.length > 0) {						
			int numberCol = 0;
			int eqpCol = 0;
			
			// Find number column
			boolean numberColFound = false;
			for (int col=0; col<brcTableIndexes_.length; col++) {
				if (brcTableIndexes_[col].compareTo("0") == 0) {
					logger.debug(className, function, "numberColFound=true at col=[{}]", col);
					numberColFound = true;
					numberCol = col;
					break;						
				}
			}
			
			// Find eqp column
			boolean eqpColFound = false;
			for (int col=0; col<brcTableIndexes_.length; col++) {
				if (brcTableIndexes_[col].compareTo("10") == 0) {
					logger.debug(className, function, "eqpColFound=true at col=[{}]", col);
					eqpColFound = true;
					eqpCol = col;
					break;						
				}
			}
			
			if (!numberColFound && eqpColFound) {
				numberCol = values.length;
				logger.debug(className, function, "numberColFound=false; set numberCol=[{}]", numberCol);
			} else if (numberColFound && !eqpColFound) {
				eqpCol = values.length;
				logger.debug(className, function, "eqpColFound=false; set eqpCol=[{}]", eqpCol);
			} else if (!numberColFound && !eqpColFound) {
				numberCol = values.length-1;
				eqpCol = values.length;
				logger.debug(className, function, "numberColFound=false; set numberCol=[{}]", numberCol);
				logger.debug(className, function, "eqpColFound=false; set eqpCol=[{}]", eqpCol);
			}
			
			// Get number of steps from number col					
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

			// Build two dimension value table column by column
			dataGridValues = new String [numSteps][strDataGridColumnsLabels.length];
			//String [][] tableValues = new String [strDataGridColumnsLabels.length][numSteps];
			
			for (int col=0; col<strDataGridColumnsLabels.length; col++) {
				if (col < values.length) {
					
					logger.debug(className, function, "col=[{}] value=[{}]", col, values[col]);
					
					String removedBracketStr = values[col].substring(1, values[col].length()-1);
					
					String unquotedStr = removedBracketStr.replaceAll("\"", "");
					
					logger.debug(className, function, "unquotedStr=[{}] ", unquotedStr);
					
					String [] splittedStr = unquotedStr.split(",");
					
					logger.debug(className, function, "splittedStr.length=[{}] ", splittedStr.length);
					
					for (int row=0; row<numSteps; row++) {
						dataGridValues[row][col] = splittedStr[row];
						logger.debug(className, function, "row=[{}] tableValues=[{}] ", row, splittedStr[row]);
					}
				} else {
					for (int row=0; row<numSteps; row++) {
						if (strDataGridColumnsTypes[col].compareToIgnoreCase("String") == 0) {
							dataGridValues[row][col] = "";
						} else if (strDataGridColumnsTypes[col].compareToIgnoreCase("Number") == 0) {
							dataGridValues[row][col] = "0";
						} else if (strDataGridColumnsTypes[col].compareToIgnoreCase("Boolean") == 0) {
							dataGridValues[row][col] = "false";
						} else {
							dataGridValues[row][col] = "";
						}
					}
				}
			}

			// Update data grid row by row
			for (int row=0; row<numSteps; row++) {
				EquipmentBuilder_i builder = new EquipmentBuilder();
				
				for (int col=0; col<strDataGridColumnsLabels.length; col++) {
					if (strDataGridColumnsTypes[col].compareToIgnoreCase("Number") == 0) {
						builder = builder.setValue(strDataGridColumnsLabels[col], Integer.parseInt(dataGridValues[row][col]));
					} else if (strDataGridColumnsTypes[col].compareToIgnoreCase("Boolean") == 0) {
						builder = builder.setValue(strDataGridColumnsLabels[col], Boolean.parseBoolean(dataGridValues[row][col]));
					} else {
						builder = builder.setValue(strDataGridColumnsLabels[col], dataGridValues[row][col]);
					}
					logger.debug(className, function, "builder setValue [{}] [{}]", strDataGridColumnsLabels[col], dataGridValues[row][col]);
					
					// Subscribe to extra relative points
					if (col == eqpCol && extraRelativePoints_ != null && extraRelativePoints_.length > 0) {
						String [] addresses = new String [extraRelativePoints_.length];
						String eqpPath = dataGridValues[row][col];

						if (eqpPath != null && !eqpPath.isEmpty()) {
							for (int i=0; i<extraRelativePoints_.length; i++) {
								addresses[i] = eqpPath + extraRelativePoints_[i];
								addAddressToCellMap(addresses[i], new Cell(row, extraCol+i));
								logger.debug(className, function, "Extra point address=[{}]", addresses[i]);
								logger.debug(className, function, "Cell row=[{}] col=[{}]", row, extraCol+i);
							}
							subscribe(addresses);
						}
					}
				}
				Equipment_i equipment_i = builder.build();
				dataGrid_.addEquipment(equipment_i);
			}
		}
		
		logger.end(className, function);
	}
	
	private void addAddressToCellMap(String address, Cell cell) {
		Set<Cell> s = addressToCellMap.get(address);
		if (s != null && !s.isEmpty()) {
			boolean alreadyExists = false;
			for (Cell c: s) {
				if (c.row == cell.row && c.column == cell.column) {
					alreadyExists = true;
					break;
				}
			}
			if (!alreadyExists) {
				s.add(cell);
			}
		} else {
			s = new HashSet<Cell>();
			s.add(cell);
			addressToCellMap.put(address, s);
		}
	}


}
