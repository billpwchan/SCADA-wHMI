package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserveEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;

public class UIInspectorEquipmentReserve implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorEquipmentReserve.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String [] dynamicAttibutes = {PointName.resrvReservedID.toString()};
	
	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	private String tabName = null;
	@Override
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	@Override
	public void setRight(Map<String, Boolean> rights) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void applyRight() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean equipmentReserveHasScreen = false;
	@Override
	public void setEquipmentReserveHasScreen(boolean equipmentReserveHasScreen) {
		this.equipmentReserveHasScreen = equipmentReserveHasScreen;
	}
	
	private EquipmentReserveEvent equipmentReserveEvent = null;
	public void setEquipmentReserveEvent(EquipmentReserveEvent equipmentReserveEvent) {
		this.equipmentReserveEvent = equipmentReserveEvent;
	}
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.debug(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.debug(className, function, "this.parent[{}]", this.parent);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		logger.begin(className, function);
		
		this.addresses = addresses;
		
		logger.end(className, function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);
		
		// Read dynamic
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + "inspectorEquipmentReserve" + "_" + "dynamic" + "_" + parent;
			
			String[] parents = new String[]{parent};

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for(int x=0;x<parents.length;++x) {
					for(int y=0;y<dynamicAttibutes.length;++y) {
						dbaddressesArrayList.add(parents[x]+dynamicAttibutes[y]);
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
			
			database.subscribe(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.begin(className, function);
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		{
			String clientKey = "multiReadValue" + "_" + "inspectorEquipmentReserve" + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
		}
		logger.beginEnd(className, function);
	}
	
	private Map<String, Map<String, String>> keyAndValuesStatic		= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues				= new HashMap<String, String>();
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";
		logger.begin(className, function);
		logger.debug(className, function, "strClientKey[{}]", strClientKey);
		
		DataBaseClientKey clientKey = new DataBaseClientKey("_", strClientKey);
		
		for ( String key : keyAndValue.keySet() )
			dbvalues.put(key, keyAndValue.get(key));
		
		if ( clientKey.isStatic() ) {
			
			keyAndValuesStatic.put(strClientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( clientKey.isDynaimc() ) {
			
			keyAndValuesDynamic.put(strClientKey, keyAndValue);
			
			updateValue(false);
		}
		
		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		logger.begin(className, function);
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				Map<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			Map<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
			
			updateValueDynamic(clientKey, keyAndValue);
			
		}// End of keyAndValuesDynamic
		
		logger.end(className, function);
	}
	
	private void updateValueStatic(String clientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		logger.begin(className, function);

		String clientKeyStatic = "multiReadValue" + "_" + "inspectorEquipmentReserve" + "_" + "static" + "_" + parent;
		if ( clientKeyStatic.equals(clientKey) ) {
			
		}
		logger.end(className, function);
	}
	
	private int eqtReserved = 0;
	public int getEqtReservedValue() {
		final String function = "getEqtReservedValue";
		logger.beginEnd(className, function, "eqtReserved[{}]", eqtReserved);
		return eqtReserved;
	}
	
	private String previewValue = "";
	private void updateValueDynamic(String clientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(className, function);

		String key = parent + PointName.resrvReservedID.toString();
		if ( dbvalues.containsKey(key) ) {
			String value = dbvalues.get(key);
			if ( null != value ) {
				value = DatabaseHelper.removeDBStringWrapper(value);
				if ( ! value.equals(previewValue) ) {
					logger.debug(className, function, "previewValue[{}] == value[{}]", previewValue, value);
					eqtReserved = EquipmentReserve.isEquipmentReservation(value, equipmentReserveHasScreen, uiNameCard.getUiScreen());
					if ( null != equipmentReserveEvent ) equipmentReserveEvent.isAvaiable(eqtReserved);
					previewValue = value;
				}
			}
		}
		logger.end(className, function);
	}
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}
	@Override
	public ComplexPanel getMainPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildWidgets(int numOfPointForEachPage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDatabase(Database db) {
		database = db;
	}
}
