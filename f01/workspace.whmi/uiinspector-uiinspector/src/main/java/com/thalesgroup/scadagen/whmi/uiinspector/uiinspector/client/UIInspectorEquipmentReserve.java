package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

public class UIInspectorEquipmentReserve implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorEquipmentReserve.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String [] dynamicAttibutes = {PointName.resrvReservedID.toString()};
	
	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.info(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.info(className, function, "this.parent[{}]", this.parent);
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

		// Read static
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + "inspector" + className + "_" + "static" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {

				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			logger.info(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.info(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}

			Database database = Database.getInstance();
			
			String api = "multiReadValue";
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					Database database = Database.getInstance();
					String clientKeyStatic = "multiReadValue" + "_" + "inspector" + className + "_" + "static" + "_" + parent;
					if ( clientKeyStatic.equals(key) ) {
						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
						HashMap<String, String> keyAndValue = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							keyAndValue.put(dbaddresses[i], dbvalues[i]);
						}

						updateValue(key, keyAndValue);
					}
				}
			});
			
			logger.end(className, function);
		}
		
		// Read dynamic
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + "inspector" + className + "_" + "dynamic" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {

						for ( String attribute : dynamicAttibutes ) {
							dbaddressesArrayList.add(dbaddress+attribute);
						}

					} else {
						logger.warn(className, function, "point IS NULL");
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			logger.info(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.info(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
			
			Database database = Database.getInstance();
			database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {

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
		logger.beginEnd(className, function);
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues				= new HashMap<String, String>();
	
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		logger.debug(className, function, "clientkey[{}]", clientKey);
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
		
		if ( "static".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( "dynamic".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesDynamic.put(clientKey, keyAndValue);
			
			updateValue(false);
		}
		
		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				HashMap<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			HashMap<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
			
			updateValueDynamic(clientKey, keyAndValue);
			
		}// End of keyAndValuesDynamic
		
		logger.end(className, function);
	}
	
	private void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);

		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + className + "_" + "static" + "_" + parent;
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
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		
		logger.begin(className, function);

		{
			String key = parent + PointName.resrvReservedID.toString();
			if ( dbvalues.containsKey(key) ) {
				String value = dbvalues.get(key);
				if ( null != value ) {
					value = RTDB_Helper.removeDBStringWrapper(value);
					eqtReserved = EquipmentReserve.isEquipmentReservation(value);
					
					if ( 0 == eqtReserved || 1 == eqtReserved ) {
						if ( null != equipmentReserveEvent ) equipmentReserveEvent.isAvaiable(true);
					} else {
						if ( null != equipmentReserveEvent ) equipmentReserveEvent.isAvaiable(false);
					}
					
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
	public void buildWidgets() {
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
	
	private EquipmentReserveEvent equipmentReserveEvent = null;
	public void setEquipmentReserveEvent(EquipmentReserveEvent equipmentReserveEvent) {
		this.equipmentReserveEvent = equipmentReserveEvent;
	}
}
