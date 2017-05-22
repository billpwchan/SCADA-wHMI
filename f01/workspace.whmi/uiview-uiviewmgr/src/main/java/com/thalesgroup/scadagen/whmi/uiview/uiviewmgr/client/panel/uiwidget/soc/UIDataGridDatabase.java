package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIDataGridDatabase implements UIDataGridDatabase_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDataGridDatabase.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private static HashMap<String, UIDataGridDatabase> instances = new HashMap<String, UIDataGridDatabase>();

	public static UIDataGridDatabase getInstance(String key) {
		if ( ! instances.containsKey(key) ) { instances.put(key, new UIDataGridDatabase()); }
		UIDataGridDatabase instance = instances.get(key);
		return instance;
	}
	
	private String strDataGrid = null;
	
	private String [] strDataGridColumnsLabels = null;
	
	private String [] strDataGridColumnsTypes = null;
	
	private int [] intDataGridColumnsTranslations = null;
	
	private String strDataGridOptsXMLFile = null;
	
	private String [] scsEnvIds = null;
	
	private IDataGridDataSource dataSource = null;
	
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
	
	@Override
	public void updateEquipmentElement(int index, Equipment_i equipment) {
		List<Equipment_i> equipments = dataProvider.getList();
		if (equipments != null && index >= 0 && index < equipments.size()) {
			equipments.remove(index);
			equipments.add(index, equipment);
		}
	}
	
	public Equipment_i getEquipmentElement(int index) {
		List<Equipment_i> equipments = dataProvider.getList();
		if (equipments != null && index >= 0 && index < equipments.size()) {
			return equipments.get(index);
		}
		return null;
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

	public void setScsEnv(String strDataGrid, String scsEnvIdsStr, String [] strDataGridColumnsLabels, String []strDataGridColumnsTypes, int []intDataGridColumnsTranslations, String strDataGridOptsXMLFile) {
		final String function = "setScsEnv";
		
		logger.begin(className, function);
		
		if (scsEnvIdsStr != null) {
			String [] tokens = scsEnvIdsStr.split(",");
			scsEnvIds = new String [tokens.length];
			for (int i=0; i<tokens.length; i++) {
				scsEnvIds[i] = tokens[i].trim();
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
		
		this.intDataGridColumnsTranslations = intDataGridColumnsTranslations;
		if (intDataGridColumnsTranslations != null) {
			for (int translation: intDataGridColumnsTranslations) {
				logger.debug(className, function, "translation=[{}]", translation);
			}
		}
				
		this.strDataGridOptsXMLFile = strDataGridOptsXMLFile;
		
		logger.end(className, function);
	}
	
	public void connect() {
		final String function = "connect";
		
		logger.begin(className, function);

		// Clear data grid
		clearEquipment();

		if ( strDataGrid.equals("UIDataGridFormatterSOC") ) {
			
			dataSource = new SocCardList();

	    } else if (strDataGrid.equals("UIDataGridFormatterSOCDetails")) {

	    	dataSource = new SocCardDetail();
	    }
		
		dataSource.init(strDataGrid, scsEnvIds, strDataGridOptsXMLFile, this);
	    	
	    dataSource.connect();
		
		logger.end(className, function);
	}
	
	public void loadData(String scsEnvId, String dbaddress) {
		final String function = "loadData";
		
		logger.begin(className, function);
		
		clearEquipment();
		
		dataSource.loadData(scsEnvId, dbaddress);
		
		logger.end(className, function);
	}
	
	
	public void reloadColumnData(String columnLabel, String columnType, boolean enableTranslation) {
		dataSource.reloadColumnData(columnLabel, columnType, enableTranslation);
	}
	
	public void resetColumnData(String columnLabel, String columnType) {
		dataSource.resetColumnData(columnLabel, columnType);
	}
	
	@Override
	public int getColumnCount() {
		return strDataGridColumnsLabels.length;
	}
	
	@Override
	public String[] getColumnLabels() {
		return strDataGridColumnsLabels;
	}
	
	@Override
	public String[] getColumnTypes() {
		return strDataGridColumnsTypes;
	}
	
	@Override
	public int [] getColumnTranslation() {
		return intDataGridColumnsTranslations;
	}
	
	public void disconnect() {
		dataSource.disconnect();
	}

	@Override
	public void changeColumnFilter(Map<String, String> filterMap) {
		dataSource.changeColumnFilter(filterMap);	
	}

}
