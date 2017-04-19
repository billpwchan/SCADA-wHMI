package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.hom.Hom;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.hom.HomEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.reserve.EquipmentReserveEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab.UIInspectorHeader_i.AttributeName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIInspectorHeader implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorHeader.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	final private String INSPECTOR = "inspector";
	
	private String tabName = null;
	@Override
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	
	private Map<String, Map<String, String>> attributesList = new HashMap<String, Map<String, String>>();
	@Override
	public void setAttribute(String type, String key, String value) {
		final String function = "setAttribute";
		logger.begin(className, function);
		logger.debug(className, function, "key[{}] value[{}]", key, value);
		if ( null == attributesList.get(type) ) attributesList.put(type, new HashMap<String, String>());
		attributesList.get(type).put(key, value);
		logger.end(className, function);
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

	private HomEvent homEvent = null;
	public void setHomEvent(HomEvent homEvent) {
		this.homEvent = homEvent;
	}
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.debug(className, function, "this.scsEnvId[{}] this.parent[{}]", this.scsEnvId, this.parent);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		logger.beginEnd(className, function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(className, function);

		{
			final String functionEmb = function + " multiReadValue";
			logger.begin(className, functionEmb);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(className);
			clientKey.setStability(Stability.STATIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.toClientKey();

			String[] parents = new String[]{parent};

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				Map<String, String> attributes =  attributesList.get(UIPanelInspector_i.strStaticAttibutes);
				for(int x=0;x<parents.length;++x) {
					for ( Map.Entry<String, String> entry : attributes.entrySet() ) {
						dbaddressesArrayList.add(parents[x]+entry.getValue());
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}

			String strApi = clientKey.getApi().toString();
			
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					{
						
						DataBaseClientKey clientKey = new DataBaseClientKey();
						clientKey.setAPI(API.multiReadValue);
						clientKey.setWidget(className);
						clientKey.setStability(Stability.STATIC);
						clientKey.setScreen(uiNameCard.getUiScreen());
						clientKey.setEnv(scsEnvId);
						clientKey.setAdress(parent);
						
						String strClientKey = clientKey.toClientKey();

						if ( 0 == strClientKey.compareTo(key) ) {
							String [] dbaddresses	= database.getKeyAndAddress(key);
							String [] dbvalues		= database.getKeyAndValues(key);
							HashMap<String, String> keyAndValue = new HashMap<String, String>();
							for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
								keyAndValue.put(dbaddresses[i], dbvalues[i]);
							}
							updateValue(key, keyAndValue);
						}			
					}
				}
			});
			
			logger.end(className, function);
		}
		
		{
			final String functionEmb = function + " multiReadValue";
			logger.begin(className, functionEmb);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(className);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.toClientKey();

			String[] parents = new String[]{parent};
			
			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				Map<String, String> attributes =  attributesList.get(UIPanelInspector_i.strDynamicAttibutes);
				for(int x=0;x<parents.length;++x) {
					for ( Map.Entry<String, String> entry : attributes.entrySet() ) {
						dbaddressesArrayList.add(parents[x]+entry.getValue());
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}

			logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}

			database.subscribe(strClientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(INSPECTOR);
					clientKey.setStability(Stability.DYNAMIC);
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(scsEnvId);
					clientKey.setAdress(parent);
					
					String strClientKeyDynamic = clientKey.toClientKey();
					
					if ( 0 == strClientKeyDynamic.compareTo(key) ) {

						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
						Map<String, String> dynamicvalues = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							dynamicvalues.put(dbaddresses[i], dbvalues[i]);
						}
						updateValue(key, dynamicvalues);
					}
				}
			});
			
			logger.end(className, functionEmb);
		}
	
		logger.end(className, function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		{
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(className);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();
			database.unSubscribe(strClientKey);
		}
		{
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();
			database.unSubscribe(strClientKey);
		}
		logger.end(className, function);
	}
	
	@Override
	public void buildWidgets(int numOfPointForEachPage) {
		final String function = "buildWidgets";
		logger.begin(className, function);
		
		buildWidgets(this.addresses.length);
	
		logger.end(className, function);
	}
	
	private TextBox txtAttributeStatus[] = null;
	void buildWidgets(int numOfWidgets, int numOfPointForEachPage) {

	}
	
	private Map<String, Map<String, String>> keyAndValuesStatic		= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues = new HashMap<String, String>();
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";
		logger.begin(className, function);
		logger.debug(className, function, "strClientKey[{}]", strClientKey);
		
		DataBaseClientKey clientKey = new DataBaseClientKey("_", strClientKey);
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
	
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
	
	private void updateValueStatic(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);
		
		Map<String, String> attributes = attributesList.get(UIPanelInspector_i.strStaticAttibutes);
		
		// Equipment Description
		{
			String shortLabel = attributes.get(AttributeName.ShortLabelAttribute.toString());
			String shortLabelValue = DatabaseHelper.getAttributeValue(parent, shortLabel, dbvalues);
			shortLabelValue = DatabaseHelper.removeDBStringWrapper(shortLabelValue);
			if ( null != shortLabelValue ) {
				shortLabelValue = Translation.getWording(shortLabelValue);
				txtAttributeStatus[0].setText(shortLabelValue);
			}
		}
		
		// GeographicalCat
		{
			String geographicalCat = attributes.get(AttributeName.LocationAttribute.toString());
			String geographicalCatValue = DatabaseHelper.getAttributeValue(parent, geographicalCat, dbvalues);
			geographicalCatValue = DatabaseHelper.removeDBStringWrapper(geographicalCatValue);
			if ( null != geographicalCatValue ) {
				geographicalCatValue = Translation.getWording(strLocationPrefix + geographicalCatValue);
				txtAttributeStatus[1].setText(geographicalCatValue);
			}
		}
		
		logger.begin(className, function);
	}
	
	private int eqtReserved = 0;
	public int getEqtReservedValue() {
		logger.beginEnd(className, "getEqtReservedValue", "eqtReserved[{}]", eqtReserved);
		return eqtReserved;
	}
	
	private String resrvReservedPreviewValue = null;
	private String hdvFlagPreviewValue = "";

	private void updateValueDynamic(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(className, function);
		
		Map<String, String> attributes = attributesList.get(UIPanelInspector_i.strDynamicAttibutes);

		{
			String isControlableValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.IsControlableAttribute.toString()), dbvalues);
			isControlableValue = DatabaseHelper.removeDBStringWrapper(isControlableValue);
			logger.debug(className, function, "isControlableValue[{}]", isControlableValue);
			if ( null != isControlableValue ) {
				isControlableValue = controlRightLabels.get(isControlableValue);
				logger.debug(className, function, "isControlableValue[{}]", isControlableValue);
				isControlableValue = Translation.getWording(isControlableValue);
				txtAttributeStatus[2].setText(isControlableValue);
			}
		}
		{		
			String resrvReservedIDValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.ResrvReservedIDAttribute.toString()), dbvalues);
			resrvReservedIDValue = DatabaseHelper.removeDBStringWrapper(resrvReservedIDValue);
			logger.debug(className, function, "resrvReservedIDValue[{}]", resrvReservedIDValue);

			logger.debug(className, function, "resrvReservedPreviewValue[{}] == resrvReservedIDValue[{}]", resrvReservedPreviewValue, resrvReservedIDValue);
			if ( null == resrvReservedPreviewValue || (null != resrvReservedIDValue && ! resrvReservedIDValue.equals(resrvReservedPreviewValue)) ) { 
				
				eqtReserved = EquipmentReserve.isEquipmentReservation(resrvReservedIDValue, equipmentReserveHasScreen, uiNameCard.getUiScreen());
				logger.debug(className, function, "eqtReserved[{}]", eqtReserved);
				
				if ( null != equipmentReserveEvent ) equipmentReserveEvent.isAvaiable(eqtReserved);
				resrvReservedPreviewValue = resrvReservedIDValue;

				String strEqtReservedLabel = null;
				strEqtReservedLabel = controlRightReservedLabels.get(eqtReserved);
				logger.debug(className, function, "strEqtReservedLabel[{}]", strEqtReservedLabel);
				strEqtReservedLabel = Translation.getWording(strEqtReservedLabel);
				txtAttributeStatus[3].setText(strEqtReservedLabel);
			}
		}
		{
			String hdvFlagValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.HdvFlagAttribute.toString()), dbvalues);
			hdvFlagValue = DatabaseHelper.removeDBStringWrapper(hdvFlagValue);
			logger.debug(className, function, "hdvFlagValue[{}]", hdvFlagValue);
			
			if ( null != hdvFlagValue ) {
				if ( ! hdvFlagValue.equals(hdvFlagPreviewValue) ) {
					logger.debug(className, function, "hdvFlagPreviewValue[{}] == hdvFlagValue[{}]", hdvFlagPreviewValue, hdvFlagValue);
					int eqtHom = Hom.isHom(hdvFlagValue);
					if ( null != homEvent ) homEvent.isAvaiable(eqtHom);
					hdvFlagPreviewValue = hdvFlagValue;
				}
				String hdvFlagDisplayValue = handoverRightLabels.get(hdvFlagValue);
				logger.debug(className, function, "hdvFlagDisplayValue[{}]", hdvFlagDisplayValue);
				hdvFlagDisplayValue = Translation.getWording(hdvFlagDisplayValue);
				txtAttributeStatus[4].setText(hdvFlagDisplayValue);
			}
		}
		
		logger.end(className, function);
	}
	
	private String strEquipmentDescriptionLabel = "";
	private String strEquipmentDescriptionInitValue = "";
	private void loadConfigurationShortLabel(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationShortLabel";
		logger.begin(className, function);
		
		String keyShortLabelAttribute = prefix+AttributeName.ShortLabelAttribute.toString();
		String shortLabelAttribute = ReadProp.readString(dictionariesCacheName, fileName, keyShortLabelAttribute, "");
		logger.debug(className, function, "shortLabelAttribute[{}]", shortLabelAttribute);
		setAttribute(UIPanelInspector_i.strStaticAttibutes, AttributeName.ShortLabelAttribute.toString(), shortLabelAttribute);
		
		strEquipmentDescriptionLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix+"ShortLabel", "");
		logger.debug(className, function, "strEquipmentDescription[{}]", strEquipmentDescriptionLabel);
		
		strEquipmentDescriptionInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix+"ShortLabelInitValue", "");
		logger.debug(className, function, "strEquipmentDescriptionInitValue[{}]", strEquipmentDescriptionInitValue);
		
		logger.end(className, function);
	}
	
	private String strLocationLabel = "";
	private String strLocationPrefix = "";
	private String strLocationInitValue = "";
	private void loadConfigurationLocation(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationLocation";
		logger.begin(className, function);
		
		String locationAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.LocationAttribute.toString(), "");
		logger.debug(className, function, "locationAttribute[{}]", locationAttribute);
		setAttribute(UIPanelInspector_i.strStaticAttibutes, AttributeName.LocationAttribute.toString(), locationAttribute);
		
		strLocationLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationLabel", "");
		logger.debug(className, function, "strLocationLabel[{}]", strLocationLabel);
		
		strLocationPrefix = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationPrefix", "");
		logger.debug(className, function, "strLocationPrefix[{}]", strLocationPrefix);
		
		strLocationInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationInitValue", "");
		logger.debug(className, function, "strLocationInitValue[{}]", strLocationInitValue);
		
		logger.end(className, function);
	}
	
	private String strControlRightLabel = "";
	private String strControlRightInitValue = "";
	
	private Map<String, String> controlRightLabels = null;
	private void loadConfigurationIsControlable(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationIsControlable";
		logger.begin(className, function);
		
		String isControlableAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.IsControlableAttribute.toString(), "");
		logger.debug(className, function, "isControlableAttribute[{}]", isControlableAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.IsControlableAttribute.toString(), isControlableAttribute);
		
		String prefix2 = prefix + "IsControlable"+UIPanelInspector_i.strDot;
		
		strControlRightLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"IsControlableLabel", "");
		logger.debug(className, function, "strControlRightLabel[{}]", strControlRightLabel);
		
		strControlRightInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(className, function, "strControlRightInitValue[{}]", strControlRightInitValue);

		int numOfLabel = ReadProp.readInt(dictionariesCacheName, fileName, prefix2+"NumOfLabel", 0);
		logger.debug(className, function, "numOfLabel[{}]", numOfLabel);

		controlRightLabels = new HashMap<String, String>();
		for ( int i = 0 ; i < numOfLabel ; i++ ) {
			String controlRightValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+i, "");
			logger.debug(className, function, "i[{}] controlRightValue[{}]", i, controlRightValue);
			
			controlRightLabels.put(String.valueOf(i), controlRightValue);
		}
		
		logger.end(className, function);
	}
	
	
	private String strControlRightReservedLabel = "";
	private String strControlRightReservedInitValue = "";
	
	private Map<Integer, String> controlRightReservedLabels = null;
	private void loadConfigurationResrvReservedID(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationResrvReservedID";
		logger.begin(className, function);
		
		String keyResrvReservedIDAttribute = prefix+AttributeName.ResrvReservedIDAttribute.toString();
		String resrvReservedIDAttribute = ReadProp.readString(dictionariesCacheName, fileName, keyResrvReservedIDAttribute, "");
		logger.debug(className, function, "resrvReservedIDAttribute[{}]", resrvReservedIDAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.ResrvReservedIDAttribute.toString(), resrvReservedIDAttribute);
		
		
		String prefix2 = prefix + "ResrvReserved"+UIPanelInspector_i.strDot;
		
		strControlRightReservedLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"ResrvReservedLabel", "");
		logger.debug(className, function, "strControlRightReservedLabel[{}]", strControlRightReservedLabel);
		
		strControlRightReservedInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(className, function, "strControlRightReservedInitValue[{}]", strControlRightReservedInitValue);

		int numOfLabel = ReadProp.readInt(dictionariesCacheName, fileName, prefix2+"NumOfLabel", 0);
		logger.debug(className, function, "numOfLabel[{}]", numOfLabel);

		controlRightReservedLabels = new HashMap<Integer, String>();
		for ( int i = 0 ; i < numOfLabel ; i++ ) {
			String controlRightReservedValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+i, "");
			logger.debug(className, function, "i[{}] controlRightReservedValue[{}]", i, controlRightReservedValue);
			
			controlRightReservedLabels.put(i, controlRightReservedValue);
		}
		
		logger.end(className, function);
	}
	
	private String strHandoverRightLabel = "";
	private String strHandoverRightInitValue = "";
	
	private Map<String, String> handoverRightLabels = null;
	private void loadConfigurationHdvFlag(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationHdvFlag";
		logger.begin(className, function);
		
		String hdvFlagAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.HdvFlagAttribute.toString(), "");
		logger.debug(className, function, "hdvFlagAttribute[{}]", hdvFlagAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.HdvFlagAttribute.toString(), hdvFlagAttribute);
		
		String prefix2 = prefix + "HdvFlag"+UIPanelInspector_i.strDot;
		
		strHandoverRightLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightLabel", "");
		logger.debug(className, function, "strHandoverRightLabel[{}]", strHandoverRightLabel);
		
		strHandoverRightInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(className, function, "strHandoverRightInitValue[{}]", strHandoverRightInitValue);

		int numOfLabel = ReadProp.readInt(dictionariesCacheName, fileName, prefix2+"NumOfLabel", 0);
		logger.debug(className, function, "numOfLabel[{}]", numOfLabel);

		handoverRightLabels = new HashMap<String, String>();
		for ( int i = 0 ; i < numOfLabel ; i++ ) {
			String handoverRightValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+i, "");
			logger.debug(className, function, "handoverRightValue[{}]", handoverRightValue);
			
			handoverRightLabels.put(String.valueOf(i), handoverRightValue);
		}
		
		logger.end(className, function);
	}
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private final String strUIInspectorHeader = className;
	private final String dictionariesCacheName = UIInspector_i.strUIInspector;
	private final String dictionariesCacheName_fileName = strUIInspectorHeader+UIPanelInspector_i.strConfigExtension;
	private final String dictionariesCacheName_prefix = strUIInspectorHeader+UIPanelInspector_i.strDot;
	
	private VerticalPanel vpCtrls = null;
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		loadConfigurationShortLabel(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationLocation(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		loadConfigurationIsControlable(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationResrvReservedID(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationHdvFlag(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		String strHeadersLabel [] = new String[] { strEquipmentDescriptionLabel, strLocationLabel, strControlRightLabel, strControlRightReservedLabel, strHandoverRightLabel };
		String strHeadersStatus [] = new String[] { strEquipmentDescriptionInitValue, strLocationInitValue, strControlRightInitValue, strControlRightReservedInitValue, strHandoverRightInitValue };
		
		FlexTable flexTableHeader = new FlexTable();
		flexTableHeader.addStyleName("project-gwt-flextable-header");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			String strHeadersLabelDisplay = Translation.getWording(strHeadersLabel[i]);
			InlineLabel inlineLabel = new InlineLabel(strHeadersLabelDisplay);
			inlineLabel.getElement().getStyle().setPadding(10, Unit.PX);
			inlineLabel.addStyleName("project-gwt-inlinelabel-headerlabel");
			flexTableHeader.setWidget(i, 0, inlineLabel);
			txtAttributeStatus[i] = new TextBox();
			String strHeadersStatusDisplay = Translation.getWording(strHeadersStatus[i]);
			txtAttributeStatus[i].setText(strHeadersStatusDisplay);
			txtAttributeStatus[i].setMaxLength(16);
			txtAttributeStatus[i].setReadOnly(true);
			txtAttributeStatus[i].addStyleName("project-gwt-textbox-headervalue");
			flexTableHeader.setWidget(i, 2, txtAttributeStatus[i]);
		}
		
		vpCtrls = new VerticalPanel();
		vpCtrls.addStyleName("project-gwt-panel-header");
		vpCtrls.add(flexTableHeader);
		
		logger.end(className, function);
	}
	
	@Override
	public ComplexPanel getMainPanel() {
		return vpCtrls;
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
