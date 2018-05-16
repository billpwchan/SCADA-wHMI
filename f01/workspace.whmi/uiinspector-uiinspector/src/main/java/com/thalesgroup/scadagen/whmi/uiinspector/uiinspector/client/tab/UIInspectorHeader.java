package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIInspectorHeader implements UIInspectorTab_i {

	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	final private String INSPECTOR = UIInspectorHeader_i.INSPECTOR;
	
	private String strCssPrefix;
	
	@Override
	public void setTabName(String tabName) { 
		this.strCssPrefix = "project-inspector-"+tabName+"-";
	}
	
	private Map<String, Map<String, String>> attributesList = new HashMap<String, Map<String, String>>();
	@Override
	public void setAttribute(String type, String key, String value) {
		final String function = "setAttribute";
		logger.begin(function);
		logger.debug(function, "key[{}] value[{}]", key, value);
		if ( null == attributesList.get(type) ) attributesList.put(type, new HashMap<String, String>());
		attributesList.get(type).put(key, value);
		logger.end(function);
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
		logger.debug(function, "this.parent[{}] this.scsEnvId[{}]", this.parent, this.scsEnvId);
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		final String function = "setAddresses";
		logger.beginEnd(function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);

		{
			final String functionEmb = function + " multiReadValue";
			logger.begin(functionEmb);
			
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
				logger.debug(function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
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
			
			logger.end(function);
		}
		
		{
			final String functionEmb = function + " multiReadValue";
			logger.begin(functionEmb);
			
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

			logger.debug(function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.debug(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
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
			
			logger.end(functionEmb);
		}
	
		logger.end(function);
	}
	
	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
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
		logger.end(function);
	}
	
	@Override
	public void buildWidgets(int numOfPointForEachPage) {
		final String function = "buildWidgets";
		logger.begin(function);
		
		buildWidgets(this.addresses.length);
	
		logger.end(function);
	}
	
	private TextBox txtAttributeStatus[] = null;
	private InlineLabel lblAttributeStatus[] = null;
	void buildWidgets(int numOfWidgets, int numOfPointForEachPage) {

	}
	
	private Map<String, Map<String, String>> keyAndValuesStatic		= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues = new HashMap<String, String>();
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";
		logger.begin(function);
		logger.trace(function, "strClientKey[{}]", strClientKey);
		
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

		logger.end(function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		logger.begin(function);
		
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
		
		logger.end(function);
	}
	
	private void updateValueStatic(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(function);
		
		Map<String, String> attributes = attributesList.get(UIPanelInspector_i.strStaticAttibutes);
		
		// Equipment Description
		{
			String shortLabel = attributes.get(AttributeName.ShortLabelAttribute.toString());
			String shortLabelValue = DatabaseHelper.getAttributeValue(parent, shortLabel, dbvalues);
			shortLabelValue = DatabaseHelper.removeDBStringWrapper(shortLabelValue);
			if ( null != shortLabelValue ) {
				shortLabelValue = Translation.getDBMessage(shortLabelValue);
				txtAttributeStatus[0].setText(shortLabelValue);
			}
		}
		
		// GeographicalCat
		{
			String geographicalCat = attributes.get(AttributeName.LocationAttribute.toString());
			String geographicalCatValue = DatabaseHelper.getAttributeValue(parent, geographicalCat, dbvalues);
			geographicalCatValue = DatabaseHelper.removeDBStringWrapper(geographicalCatValue);
			if ( null != geographicalCatValue ) {
				geographicalCatValue = Translation.getDBMessage(strLocationPrefix + geographicalCatValue);
				txtAttributeStatus[1].setText(geographicalCatValue);
			}
		}
		
		logger.end(function);
	}
	
	private int eqtReserved = 0;
	public int getEqtReservedValue() {
		logger.beginEnd("getEqtReservedValue", "eqtReserved[{}]", eqtReserved);
		return eqtReserved;
	}
	
	private boolean widgetHasCSS(Widget widget, String css ) {
		final String function = "widgetHasCSS";
		logger.begin(function);
		boolean result = false;
		String cssExist = widget.getStyleName();
		String cssExists [] = cssExist.split("\\s+");
		for ( int i = 0 ; i < cssExists.length ; ++i ) {
			if ( cssExists[i].equals(css) ) {
				result=true;
				break;
			}
		}
		return result;
	}
	
	private void widgetRemoveCSS(Widget widget, String css) {
		final String function = "widgetRemoveCSS";
		logger.begin(function);
		logger.trace(function, "widget[{}] css[{}]", widget, css);
		if ( widgetHasCSS(widget, css) ) {
			widget.removeStyleName(css);
		}
		logger.end(function);
	}
	
	private void widgetAddCSS(Widget widget, String css) {
		final String function = "widgetAddCSS";
		logger.begin(function);
		logger.trace(function, "widget[{}] css[{}]", widget, css);
		if ( ! widgetHasCSS(widget, css) ) {
			widget.addStyleName(css);
		}
		logger.end(function);
	}
	
	private String resrvReservedPreviewValue = null;
	private String hdvFlagPreviewValue = "";

	private void updateValueDynamic(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(function);
		
		Map<String, String> attributes = attributesList.get(UIPanelInspector_i.strDynamicAttibutes);

		{
			String isControlableValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.IsControlableAttribute.toString()), dbvalues);
			isControlableValue = DatabaseHelper.removeDBStringWrapper(isControlableValue);
			logger.trace(function, "isControlableValue[{}]", isControlableValue);
			if ( null != isControlableValue ) {
				isControlableValue = controlRightLabels.get(isControlableValue);
				logger.trace(function, "isControlableValue[{}]", isControlableValue);
				isControlableValue = Translation.getDBMessage(isControlableValue);
				txtAttributeStatus[2].setText(isControlableValue);
			}
		}
		{		
			String resrvReservedIDValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.ResrvReservedIDAttribute.toString()), dbvalues);
			resrvReservedIDValue = DatabaseHelper.removeDBStringWrapper(resrvReservedIDValue);
			logger.trace(function, "resrvReservedIDValue[{}]", resrvReservedIDValue);

			logger.trace(function, "resrvReservedPreviewValue[{}] == resrvReservedIDValue[{}]", resrvReservedPreviewValue, resrvReservedIDValue);
			if ( null == resrvReservedPreviewValue || (null != resrvReservedIDValue && ! resrvReservedIDValue.equals(resrvReservedPreviewValue)) ) { 
				
				eqtReserved = EquipmentReserve.isEquipmentReservation(resrvReservedIDValue, equipmentReserveHasScreen, uiNameCard.getUiScreen());
				logger.trace(function, "eqtReserved[{}]", eqtReserved);
				
				if ( null != equipmentReserveEvent ) equipmentReserveEvent.isAvaiable(eqtReserved);
				resrvReservedPreviewValue = resrvReservedIDValue;

				String strEqtReservedLabel = null;
				strEqtReservedLabel = controlRightReservedLabels.get(eqtReserved);
				logger.trace(function, "strEqtReservedLabel[{}]", strEqtReservedLabel);
				strEqtReservedLabel = Translation.getDBMessage(strEqtReservedLabel);
				txtAttributeStatus[3].setText(strEqtReservedLabel);
			}
		}
		{
			String hdvFlagValue = DatabaseHelper.getAttributeValue(parent, attributes.get(AttributeName.HdvFlagAttribute.toString()), dbvalues);
			hdvFlagValue = DatabaseHelper.removeDBStringWrapper(hdvFlagValue);
			logger.trace(function, "hdvFlagValue[{}]", hdvFlagValue);
			
			if ( null != hdvFlagValue ) {
				if ( ! hdvFlagValue.equals(hdvFlagPreviewValue) ) {
					logger.trace(function, "hdvFlagPreviewValue[{}] == hdvFlagValue[{}]", hdvFlagPreviewValue, hdvFlagValue);
					int eqtHom = Hom.isHom(hdvFlagValue);
					if ( null != homEvent ) homEvent.isAvaiable(eqtHom);
					hdvFlagPreviewValue = hdvFlagValue;
				}
				
				if ( null == handoverRightLabels ) handoverRightLabels = new HashMap<String, String>();
				
				if ( ! handoverRightLabels.containsKey(hdvFlagValue) ) {
					logger.trace(function, "hdvFlagPreviewValue[{}] == hdvFlagValue[{}]", hdvFlagPreviewValue, hdvFlagValue);
					String attribite = dictionariesCacheName_prefix + "HdvFlag"+UIPanelInspector_i.strDot+hdvFlagValue;
					
					logger.trace(function, "Reading dictionariesCacheName_fileName[{}] attribite[{}]", dictionariesCacheName_fileName, attribite);
					handoverRightLabels.put(hdvFlagValue, ReadProp.readString(dictionariesCacheName, dictionariesCacheName_fileName, attribite, ""));
				}
				logger.trace(function, "handoverRightLabels.get({}) =[{}]", hdvFlagValue, handoverRightLabels.get(hdvFlagValue));
				
				String hdvFlagDisplayValue = handoverRightLabels.get(hdvFlagValue);
				logger.trace(function, "hdvFlagDisplayValue[{}]", hdvFlagDisplayValue);
				hdvFlagDisplayValue = Translation.getDBMessage(hdvFlagDisplayValue);
				txtAttributeStatus[4].setText(hdvFlagDisplayValue);
				
				logger.trace(function, "hdvFlagValue[{}] strHandoverRightHiddenHOMValue[{}]", new Object[]{hdvFlagValue, strHandoverRightHiddenHOMValue});
				
				logger.trace(function, "strHandoverRightLabelCSSShow[{}] strHandoverRightLabelCSSHidden[{}] strHandoverRightValueCSSShow[{}] strHandoverRightValueCSSHidden[{}]"
						, new Object[]{strHandoverRightLabelCSSShow, strHandoverRightLabelCSSHidden, strHandoverRightValueCSSShow, strHandoverRightValueCSSHidden});
				
				boolean isHOMHiddenValue = false;
				if ( null != strHandoverRightHiddenHOMValues ) {
					for ( int i = 0 ; i < strHandoverRightHiddenHOMValues.length ; ++i ) {
						logger.trace(function, "hdvFlagValue[{}] strHandoverRightHiddenHOMValues({})[{}]", new Object[]{hdvFlagValue, i, strHandoverRightHiddenHOMValues[i]});
						if ( null != strHandoverRightHiddenHOMValues[i] && hdvFlagValue.equals(strHandoverRightHiddenHOMValues[i]) ) {
							isHOMHiddenValue = true;
							break;
						}
					}
				} else {
					logger.warn(function, "strHandoverRightByPassValues IS NULL");
				}
				
				
				logger.trace(function, "isHOMHiddenValue[{}]", isHOMHiddenValue);
				
				if ( isHOMHiddenValue ) {
					
					// Hidden
					if ( ! widgetHasCSS(lblAttributeStatus[4], strHandoverRightLabelCSSHidden) ) {
						widgetAddCSS(lblAttributeStatus[4], strHandoverRightLabelCSSHidden);
						widgetRemoveCSS(lblAttributeStatus[4], strHandoverRightLabelCSSShow);
					}
					
					if ( ! widgetHasCSS(txtAttributeStatus[4], strHandoverRightValueCSSHidden) ) {
						widgetAddCSS(txtAttributeStatus[4], strHandoverRightValueCSSHidden);
						widgetRemoveCSS(txtAttributeStatus[4], strHandoverRightValueCSSShow);
					}
					
				} else {
					// Visible
					
					if ( ! widgetHasCSS(lblAttributeStatus[4], strHandoverRightLabelCSSShow) ) {
						widgetAddCSS(lblAttributeStatus[4], strHandoverRightLabelCSSShow);
						widgetRemoveCSS(lblAttributeStatus[4], strHandoverRightLabelCSSHidden);
					}
					
					if ( ! widgetHasCSS(txtAttributeStatus[4], strHandoverRightValueCSSShow) ) {
						widgetAddCSS(txtAttributeStatus[4], strHandoverRightValueCSSShow);
						widgetRemoveCSS(txtAttributeStatus[4], strHandoverRightValueCSSHidden);
					}
				}
			}
		}
		
		logger.end(function);
	}
	
	private String strEquipmentDescriptionLabel = "";
	private String strEquipmentDescriptionInitValue = "";
	private void loadConfigurationShortLabel(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationShortLabel";
		logger.begin(function);
		
		String keyShortLabelAttribute = prefix+AttributeName.ShortLabelAttribute.toString();
		String shortLabelAttribute = ReadProp.readString(dictionariesCacheName, fileName, keyShortLabelAttribute, "");
		logger.debug(function, "shortLabelAttribute[{}]", shortLabelAttribute);
		setAttribute(UIPanelInspector_i.strStaticAttibutes, AttributeName.ShortLabelAttribute.toString(), shortLabelAttribute);
		
		strEquipmentDescriptionLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix+"ShortLabel", "");
		logger.debug(function, "strEquipmentDescription[{}]", strEquipmentDescriptionLabel);
		
		strEquipmentDescriptionInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix+"ShortLabelInitValue", "");
		logger.debug(function, "strEquipmentDescriptionInitValue[{}]", strEquipmentDescriptionInitValue);
		
		logger.end(function);
	}
	
	private String strLocationLabel = "";
	private String strLocationPrefix = "";
	private String strLocationInitValue = "";
	private void loadConfigurationLocation(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationLocation";
		logger.begin(function);
		
		String locationAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.LocationAttribute.toString(), "");
		logger.debug(function, "locationAttribute[{}]", locationAttribute);
		setAttribute(UIPanelInspector_i.strStaticAttibutes, AttributeName.LocationAttribute.toString(), locationAttribute);
		
		strLocationLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationLabel", "");
		logger.debug(function, "strLocationLabel[{}]", strLocationLabel);
		
		strLocationPrefix = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationPrefix", "");
		logger.debug(function, "strLocationPrefix[{}]", strLocationPrefix);
		
		strLocationInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix+"LocationInitValue", "");
		logger.debug(function, "strLocationInitValue[{}]", strLocationInitValue);
		
		logger.end(function);
	}
	
	private String strControlRightLabel = "";
	private String strControlRightInitValue = "";
	
	private Map<String, String> controlRightLabels = null;
	private void loadConfigurationIsControlable(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationIsControlable";
		logger.begin(function);
		
		String isControlableAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.IsControlableAttribute.toString(), "");
		logger.debug(function, "isControlableAttribute[{}]", isControlableAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.IsControlableAttribute.toString(), isControlableAttribute);
		
		String prefix2 = prefix + "IsControlable"+UIPanelInspector_i.strDot;
		
		strControlRightLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"IsControlableLabel", "");
		logger.debug(function, "strControlRightLabel[{}]", strControlRightLabel);
		
		strControlRightInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(function, "strControlRightInitValue[{}]", strControlRightInitValue);

		int numOfLabel = ReadProp.readInt(dictionariesCacheName, fileName, prefix2+"NumOfLabel", 0);
		logger.debug(function, "numOfLabel[{}]", numOfLabel);

		controlRightLabels = new HashMap<String, String>();
		for ( int i = 0 ; i < numOfLabel ; i++ ) {
			String controlRightValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+i, "");
			logger.debug(function, "i[{}] controlRightValue[{}]", i, controlRightValue);
			
			controlRightLabels.put(String.valueOf(i), controlRightValue);
		}
		
		logger.end(function);
	}
	
	
	private String strControlRightReservedLabel = "";
	private String strControlRightReservedInitValue = "";
	
	private Map<Integer, String> controlRightReservedLabels = null;
	private void loadConfigurationResrvReservedID(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationResrvReservedID";
		logger.begin(function);
		
		String keyResrvReservedIDAttribute = prefix+AttributeName.ResrvReservedIDAttribute.toString();
		String resrvReservedIDAttribute = ReadProp.readString(dictionariesCacheName, fileName, keyResrvReservedIDAttribute, "");
		logger.debug(function, "resrvReservedIDAttribute[{}]", resrvReservedIDAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.ResrvReservedIDAttribute.toString(), resrvReservedIDAttribute);
		
		
		String prefix2 = prefix + "ResrvReserved"+UIPanelInspector_i.strDot;
		
		strControlRightReservedLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"ResrvReservedLabel", "");
		logger.debug(function, "strControlRightReservedLabel[{}]", strControlRightReservedLabel);
		
		strControlRightReservedInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(function, "strControlRightReservedInitValue[{}]", strControlRightReservedInitValue);

		int numOfLabel = ReadProp.readInt(dictionariesCacheName, fileName, prefix2+"NumOfLabel", 0);
		logger.debug(function, "numOfLabel[{}]", numOfLabel);

		controlRightReservedLabels = new HashMap<Integer, String>();
		for ( int i = 0 ; i < numOfLabel ; i++ ) {
			String controlRightReservedValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+i, "");
			logger.debug(function, "i[{}] controlRightReservedValue[{}]", i, controlRightReservedValue);
			
			controlRightReservedLabels.put(i, controlRightReservedValue);
		}
		
		logger.end(function);
	}
	
	private String strHandoverRightLabel = "";
	private String strHandoverRightInitValue = "";
	
	private String strHandoverRightHiddenHOMValue = "";
	
	private String strHandoverRightHiddenHOMValues [] = null;
	
	private String strHandoverRightLabelCSSShow = null;
	private String strHandoverRightLabelCSSHidden = null;
	private String strHandoverRightValueCSSShow = null;
	private String strHandoverRightValueCSSHidden = null;
	
	private Map<String, String> handoverRightLabels = null;
	private void loadConfigurationHdvFlag(String dictionariesCacheName, String fileName, String prefix) {
		final String function = "loadConfigurationHdvFlag";
		logger.begin(function);
		
		String hdvFlagAttribute = ReadProp.readString(dictionariesCacheName, fileName, prefix+AttributeName.HdvFlagAttribute.toString(), "");
		logger.debug(function, "hdvFlagAttribute[{}]", hdvFlagAttribute);
		setAttribute(UIPanelInspector_i.strDynamicAttibutes, AttributeName.HdvFlagAttribute.toString(), hdvFlagAttribute);
		
		String prefix2 = prefix + "HdvFlag"+UIPanelInspector_i.strDot;
		
		strHandoverRightLabel = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightLabel", "");
		logger.debug(function, "strHandoverRightLabel[{}]", strHandoverRightLabel);
		
		strHandoverRightInitValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"Init", "");
		logger.debug(function, "strHandoverRightInitValue[{}]", strHandoverRightInitValue);
		
		strHandoverRightHiddenHOMValue = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightHiddenHOMValues", "");
		logger.debug(function, "strHandoverRightHiddenHOMValue[{}]", strHandoverRightHiddenHOMValue);
		
		strHandoverRightHiddenHOMValues = UIWidgetUtil.getStringArray(strHandoverRightHiddenHOMValue, ",");

		strHandoverRightLabelCSSShow = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightLabelCSSShow", "");
		logger.debug(function, "strHandoverRightLabelCSSShow[{}]", strHandoverRightLabelCSSShow);
		
		strHandoverRightLabelCSSHidden = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightLabelCSSHidden", "");
		logger.debug(function, "strHandoverRightLabelCSSHidden[{}]", strHandoverRightLabelCSSHidden);
		
		strHandoverRightValueCSSShow = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightValueCSSShow", "");
		logger.debug(function, "strHandoverRightValueCSSShow[{}]", strHandoverRightValueCSSShow);
		
		strHandoverRightValueCSSHidden = ReadProp.readString(dictionariesCacheName, fileName, prefix2+"HandoverRightValueCSSHidden", "");
		logger.debug(function, "strHandoverRightValueCSSHidden[{}]", strHandoverRightValueCSSHidden);

		logger.end(function);
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
		logger.begin(function);
		
		loadConfigurationShortLabel(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationLocation(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		loadConfigurationIsControlable(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationResrvReservedID(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		loadConfigurationHdvFlag(dictionariesCacheName, dictionariesCacheName_fileName, dictionariesCacheName_prefix);
		
		String strHeadersLabel [] = new String[] { strEquipmentDescriptionLabel, strLocationLabel, strControlRightLabel, strControlRightReservedLabel, strHandoverRightLabel };
		String strHeadersStatus [] = new String[] { strEquipmentDescriptionInitValue, strLocationInitValue, strControlRightInitValue, strControlRightReservedInitValue, strHandoverRightInitValue };
		
		FlexTable flexTableHeader = new FlexTable();
		flexTableHeader.addStyleName(strCssPrefix+"flextable-header");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		lblAttributeStatus = new InlineLabel[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			String strHeadersLabelDisplay = Translation.getDBMessage(strHeadersLabel[i]);
			lblAttributeStatus[i] = new InlineLabel(strHeadersLabelDisplay);
			lblAttributeStatus[i].getElement().getStyle().setPadding(10, Unit.PX);
			lblAttributeStatus[i].addStyleName(strCssPrefix+"inlinelabel-label-"+i);
			flexTableHeader.setWidget(i, 0, lblAttributeStatus[i]);
			DOM.getParent(lblAttributeStatus[i].getElement()).setClassName(strCssPrefix+"inlinelabel-label-parent-"+i);
			
			txtAttributeStatus[i] = new TextBox();
			String strHeadersStatusDisplay = Translation.getDBMessage(strHeadersStatus[i]);
			txtAttributeStatus[i].setText(strHeadersStatusDisplay);
			txtAttributeStatus[i].setMaxLength(16);
			txtAttributeStatus[i].setReadOnly(true);
			txtAttributeStatus[i].addStyleName(strCssPrefix+"textbox-value-"+i);
			flexTableHeader.setWidget(i, 2, txtAttributeStatus[i]);
			DOM.getParent(txtAttributeStatus[i].getElement()).setClassName(strCssPrefix+"textbox-value-parent-"+i);
		}
		
		vpCtrls = new VerticalPanel();
		flexTableHeader.addStyleName(strCssPrefix+"panel-vpCtrlsr");
		vpCtrls.add(flexTableHeader);
		
		logger.end(function);
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
