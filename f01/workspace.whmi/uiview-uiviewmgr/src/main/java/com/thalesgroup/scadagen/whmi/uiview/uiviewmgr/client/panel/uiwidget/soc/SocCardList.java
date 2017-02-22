package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.SocCardList_i.SocCardListParameter;
import com.thalesgroup.scadagen.wrapper.wrapper.client.GetChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.UUIDWrapper;

public class SocCardList implements IDataGridDataSource {

	private final String className = UIWidgetUtil.getClassSimpleName(SocCardList.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String [] scsEnvIds_ = null;
	private String optsXMLFile_ = null;
	private UIDataGridDatabase_i dataGridDb_ = null;
	private String separater = ",";
	private String [] strDataGridColumnsFilters = null;
	private final String grcPathRoot = "ScadaSoft";
	private String [] strDataGridColumnsLabels = null;
	private Map<String, String> scsEnvIdMap = new HashMap<String, String>();
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();

	@Override
	public void init(String[] scsEnvIds, String strDataGridOptsXMLFile, UIDataGridDatabase uiDataGridDatabase) {
		final String function = "init";
		logger.begin(className, function);
		
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
		
		if (dataGridDb_ != null) {
			strDataGridColumnsLabels = dataGridDb_.getColumnLabels();
		}
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strColumnValueFilters = dictionariesCache.getStringValue(optsXMLFile_, SocCardListParameter.ColumnValueFilters.toString(), strHeader);
			if (strColumnValueFilters != null) {
				strDataGridColumnsFilters = UIWidgetUtil.getStringArray(strColumnValueFilters, separater);
			}
		}
		logger.end(className, function);
	}
	
	private String genClientKey(String scsEnvId) {
		String clientKey = className + UUIDWrapper.getUUID();
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
	public void reloadColumnData(String columnLabel, String columnType) {
		
	}

}
