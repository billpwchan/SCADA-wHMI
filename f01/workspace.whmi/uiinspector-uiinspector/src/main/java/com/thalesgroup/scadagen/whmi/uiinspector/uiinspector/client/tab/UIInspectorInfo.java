package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.page.PageCounter;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.alarm.AckAlarm;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;

public class UIInspectorInfo implements UIInspectorTab_i {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	public String strCSSStatusGreen		= null;
	public String strCSSStatusRed		= null;
	public String strCSSStatusBlue		= null;
	public String strCSSStatusGrey		= null;
	
	// Static Attribute List
	private final String staticAciAttibutes [] = new String[] {PointName.label.toString(), PointName.hmiOrder.toString()};
	private final String staticDciAttibutes [] = new String[] {PointName.label.toString(), PointName.hmiOrder.toString(), PointName.dalValueTableLabel.toString(), PointName.dalValueTableValue.toString()};
	private final String staticSciAttibutes [] = new String[] {PointName.label.toString(), PointName.hmiOrder.toString()};
	
	// Dynamic Attribute List
	private final String dynamicAciAttibutes [] = new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.aalValueAlarmVector.toString(), PointName.afoForcedStatus.toString()};
	private final String dynamicDciAttibutes [] = new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.dalValueAlarmVector.toString(), PointName.dfoForcedStatus.toString()};
	private final String dynamicSciAttibutes [] = new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.salValueAlarmVector.toString(), PointName.sfoForcedStatus.toString()};
	
	// Dynamic Attribute List ComputedMessage
	private final String dynamicAciAttibutesCM [] = new String[] {PointName.computedMessage.toString(), PointName.validity.toString(), PointName.aalValueAlarmVector.toString(), PointName.afoForcedStatus.toString()};
	private final String dynamicDciAttibutesCM [] = new String[] {PointName.computedMessage.toString(), PointName.validity.toString(), PointName.dalValueAlarmVector.toString(), PointName.dfoForcedStatus.toString()};
	private final String dynamicSciAttibutesCM [] = new String[] {PointName.computedMessage.toString(), PointName.validity.toString(), PointName.salValueAlarmVector.toString(), PointName.sfoForcedStatus.toString()};
	
	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	// This option is used to set whether to get label from value table mapping or directly from computedMessage
	private Boolean aciValLabelFromComputedMessage = false;
	private Boolean dciValLabelFromComputedMessage = false;
	private Boolean sciValLabelFromComputedMessage = false;
	private Map<String, Boolean>mapIsValLabelFromComputedMessage = new HashMap<String, Boolean>();
	
	private static final String DICTIONARY_CACHE_NAME = UIInspector_i.strUIInspector;
	private static final String DICTIONARY_FILE_NAME = "inspectorpanel.info.properties";
	private static final String CONFIG_PREFIX = "inspectorpanel.info.";
	
	private static final String VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX = CONFIG_PREFIX+"valLabelFromComputedMessage.";
	
	private final String INSPECTOR = UIInspectorInfo_i.INSPECTOR;
	
	public static final String STR_EMPTY                = Database_i.STR_EMPTY;
	public static final String STR_UNDERSCORE           = Database_i.STR_UNDERSCORE;
	public static final String STR_COLON                = Database_i.STR_COLON;
	public static final String STR_COMMA                = Database_i.STR_COMMA;
	
	public static final String STR_UP					= "▲";
	public static final String STR_SPLITER				= " / ";
	public static final String STR_DOWN					= "▼";
	public static final String STR_PAGER_DEFAULT		= "1 / 1";
	
	private static final String STR_ATTRIBUTE_LABEL		= "ATTRIBUTE_LABEL_";
	private static final String STR_ATTRIBUTE_STATUS	= "ATTRIBUTE_STATUS_";
	private static final String STR_R					= "R";
	
	private static final String STR_ACK_PAGE			= "Ack. Page";
	
	private String strCssPrefix;
	
	private String tabName = null;
	@Override
	public void setTabName(String tabName) { 
		this.tabName = tabName;
		this.strCssPrefix = "project-inspector-"+tabName+"-";
	}
	
	private Map<String, Map<String, String>> attributesList = new HashMap<String, Map<String, String>>();
	@Override
	public void setAttribute(String type, String key, String value) {
		final String function = "setAttribute";
		logger.begin(function);
		if ( null == attributesList.get(type) ) attributesList.put(type, new HashMap<String, String>());
		attributesList.get(type).put(key, value);
		logger.end(function);
	}
	
	private Button btnAckCurPage = null;
	
	private Map<String, Boolean> rights = null;
	@Override
	public void setRight(Map<String, Boolean> rights) {
		final String function = "setRight";
		if ( null == rights ) {
			logger.warn(function, "rights IS NULL");
		}		
		if ( null == this.rights ) this.rights = new HashMap<String, Boolean>();
		for ( Entry<String, Boolean> right : rights.entrySet() ) {
			String key = right.getKey();
			boolean value = right.getValue();
			this.rights.put(key, value);
		}
	}

	@Override
	public void applyRight() {
		final String function = "applyRight";
		String rightname = "ackalarm";
		if ( rights.containsKey(rightname) && null != rights.get(rightname) ) {
			boolean right = rights.get(rightname);
			logger.debug(function, "Checking rightname[{}] right[{}]", rightname, right);
			if ( ! right ) {
				logger.warn(function, "rightname[{}] right IS INSUFFICIENT RIGHT", rightname);		
				btnAckCurPage.setVisible(false);
			}
		} else {
			logger.warn(function, "rightname[{}] right IS INVALID", rightname);
		}
	}
	
	private boolean equipmentReserveHasScreen = false;
	@Override
	public void setEquipmentReserveHasScreen(boolean equipmentReserveHasScreen) {
		this.equipmentReserveHasScreen = equipmentReserveHasScreen;
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
		logger.begin(function);
		
		this.addresses = addresses;
		
		logger.end(function);
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);

		ReadLabelProperties();

		// Read static
		{
			logger.begin(function);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR+tabName);
			clientKey.setStability(Stability.STATIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);
			
			String strClientKey = clientKey.getClientKey();

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = DatabaseHelper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						if ( pointType == PointType.dci ) {
							for ( String attribute : staticDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.aci ) {
							for ( String attribute : staticAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.sci ) {
							for ( String attribute : staticSciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else {
							logger.warn(function, "dbaddress IS UNKNOW TYPE");
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			if ( logger.isDebugEnabled() ) {
				logger.debug(function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}
			
			String strApi = clientKey.getApi().toString();
			
			database.addStaticRequest(strApi, strClientKey, scsEnvId, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(INSPECTOR+tabName);
					clientKey.setStability(Stability.STATIC);
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(scsEnvId);
					clientKey.setAdress(parent);
					
					String strClientKey = clientKey.toClientKey();

					if ( 0 == strClientKey.compareTo(key) ) {
						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
						Map<String, String> keyAndValue = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							keyAndValue.put(dbaddresses[i], dbvalues[i]);
						}

						updateValue(key, keyAndValue);
					}
				}
			});
			
			logger.end(function);
		}
		
		// Read dynamic
		{
			logger.begin(function);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(INSPECTOR + tabName);
			clientKey.setStability(Stability.DYNAMIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(scsEnvId);
			clientKey.setAdress(parent);

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = DatabaseHelper.getPoint(dbaddress);
					
					if ( null != point ) {
						PointType pointType = DatabaseHelper.getPointType(point);
						Boolean useCompMsg = isValLabelFromComputedMessage(dbaddress, pointType);
						if ( pointType == PointType.dci ) {
							if (useCompMsg) {
								for ( String attribute : dynamicDciAttibutesCM ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							} else {
								for ( String attribute : dynamicDciAttibutes ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							}
						} else if ( pointType == PointType.aci ) {
							if (useCompMsg) {
								for ( String attribute : dynamicAciAttibutesCM ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							} else {
								for ( String attribute : dynamicAciAttibutes ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							}
						} else if ( pointType == PointType.sci ) {
							if (useCompMsg) {
								for ( String attribute : dynamicSciAttibutesCM ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							} else {
								for ( String attribute : dynamicSciAttibutes ) {
									dbaddressesArrayList.add(dbaddress+attribute);
								}
							}
						} else {
							logger.warn(function, "dbaddress IS UNKNOW TYPE");
						}
					} else {
						logger.warn(function, "point IS NULL");
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			
			String strClientKey = clientKey.toString();
			if ( logger.isDebugEnabled() ) {
				logger.debug(function, "strClientKey[{}] scsEnvId[{}]", strClientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}

			database.subscribe(strClientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.begin(function);
		}
		logger.end(function);
	}

	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);

		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.DYNAMIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		database.unSubscribe(strClientKey);

		logger.end(function);
	}
	
	private void updatePager() {
		final String function = "updatePager";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
				
		if ( pageCounter.hasPreview || pageCounter.hasNext ) {
			btnUp.setEnabled(pageCounter.hasPreview);
			lblPageNum.setText(String.valueOf(pageIndex+1) + STR_SPLITER + String.valueOf(pageCounter.pageCount));
			btnDown.setEnabled(pageCounter.hasNext);
		} else {
			btnUp.setVisible(false);
			lblPageNum.setVisible(false);
			btnDown.setVisible(false);
		}
		
		logger.end(function);
	}
	
	private void updateLayout() {
		final String function = "updateLayout";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
		
		int pageSize = pageCounter.pageSize;
		
		int stopper = pageCounter.pageRowCount;
		
		logger.debug(function
				, "pageIndex[{}] pageSize[{}], stopper[{}]"
				, new Object[]{pageIndex, pageSize, stopper});
		
		for ( int i = 0 ; i < pageSize ; i++ ) {
			boolean visible = true;
			if ( i >= stopper ) {
				visible = false;
			}
			lblAttibuteLabel[i]		.setVisible(visible);
			txtAttributeValue[i]	.setVisible(visible);
			txtAttibuteColor[i]		.setVisible(visible);
		}

		logger.end(function);
	}
	
	private void onButton(Button btn) {
		final String function = "onButton";
		logger.begin(function);
		
		if (  btn == btnUp || btn == btnDown ) {
			if ( btn == btnUp) {
				--pageIndex;
			} else if ( btn == btnDown ) {
				++pageIndex;
			}
			updatePager();
			updateLayout();
			updateValue(true);
		}
		
		logger.begin(function);
	}
	
	@Override
	public void buildWidgets(int numOfPointEachPage) {
		final String function = "buildWidgets";
		logger.begin(function);
		
		buildWidgets(this.addresses.length, numOfPointEachPage);
	
		logger.end(function);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	void buildWidgets(int numOfWidgets, int numOfPointEachPage) {
		final String function = "buildWidgets";
		logger.begin(function);
		
		logger.debug(function, "numOfWidgets[{}] numOfPointEachPage[{}]", numOfWidgets, numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			int pageSize = pageCounter.pageSize;
			
			logger.debug(function, "pageSize[{}]", pageSize);
			
//			if ( DatabaseHelper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[pageSize];
				txtAttributeValue	= new TextBox[pageSize];
				txtAttibuteColor	= new InlineLabel[pageSize];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.addStyleName(strCssPrefix+"flextable");
				
				for( int index = 0 ; index < pageSize ; ++index ) {
					logger.debug(function, "index[{}]", index);
						
					lblAttibuteLabel[index] = new InlineLabel();
					lblAttibuteLabel[index].addStyleName(strCssPrefix+"inlinelabel-label");
					lblAttibuteLabel[index].setText(STR_ATTRIBUTE_LABEL+(index+1)+STR_COLON);
					flexTableAttibutes.setWidget(index+1, 0, lblAttibuteLabel[index]);
					DOM.getParent(lblAttibuteLabel[index].getElement()).setClassName(strCssPrefix+"inlinelabel-label-parent");
					
					txtAttributeValue[index] = new TextBox();
					txtAttributeValue[index].addStyleName(strCssPrefix+"textbox-value");
					txtAttributeValue[index].setText(STR_ATTRIBUTE_STATUS+(index+1));
					txtAttributeValue[index].setReadOnly(true);
					txtAttributeValue[index].setMaxLength(16);
					flexTableAttibutes.setWidget(index+1, 1, txtAttributeValue[index]);
					DOM.getParent(txtAttributeValue[index].getElement()).setClassName(strCssPrefix+"textbox-value-parent");
					
					txtAttibuteColor[index] = new InlineLabel();
					txtAttibuteColor[index].addStyleName(strCssPrefix+"textbox-color");
					txtAttibuteColor[index].setStyleName(strCSSStatusGrey);
					txtAttibuteColor[index].setText(STR_R);
					flexTableAttibutes.setWidget(index+1, 2, txtAttibuteColor[index]);
					DOM.getParent(txtAttibuteColor[index].getElement()).setClassName(strCssPrefix+"textbox-color-parent");
				}

				flexTableAttibutes.getColumnFormatter().addStyleName(0, strCssPrefix+"flextable-inspectorlabel-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(1, strCssPrefix+"flextable-inspectorvalue-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(2, strCssPrefix+"flextable-inspectorstatus-col");

//			} else {
//				logger.debug(function, "this.addresses IS INVALID");
//			}
			
			vpCtrls.add(flexTableAttibutes);
			
			updatePager();
			updateLayout();
			
		} else {
			logger.warn(function, "points IS NULL");
		}
		
		logger.end(function);
	}
	
	private Map<String, Map<String, String>> keyAndValuesStatic     = new LinkedHashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, Map<String, String>>();
	private Map<String, String> dbvalues = new HashMap<String, String>();
	@Override
	public void updateValue(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValue";
		logger.begin(function);
		
		logger.trace(function, "strClientKey[{}]", strClientKey);
		
		DataBaseClientKey clientKey = new DataBaseClientKey(STR_UNDERSCORE, strClientKey);
		
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
	
	private void updateValueStatic(String key, Map<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.debug(function, "strClientKey[{}]", key);	
		
		DataBaseClientKey clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(INSPECTOR + tabName);
		clientKey.setStability(Stability.STATIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(scsEnvId);
		clientKey.setAdress(parent);
		
		String strClientKey = clientKey.toClientKey();
		
		logger.debug(function, "strClientKey[{}]", strClientKey);
		
		if ( strClientKey.equalsIgnoreCase(key) ) {
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				String address = this.addresses[x];
				String dbaddress = address + PointName.label.toString();
				if ( dbvalues.containsKey(dbaddress) ) {
					String value = dbvalues.get(dbaddress);
					value = DatabaseHelper.removeDBStringWrapper(value);
					lblAttibuteLabel[y].setText(value);
				}
			}
		}
		
		logger.end(function);
	}
	
	private void updateValueDynamic(String strClientKey, Map<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.trace(function, "strClientKey[{}]", strClientKey);
		
		for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
			String address = this.addresses[x];
			
			logger.trace(function, "address[{}]", address);
			
			String point = DatabaseHelper.getPoint(address);
			PointType pointType = DatabaseHelper.getPointType(point);

			if ( PointType.dci == pointType ) {
				updateDci(address, y);
			} else if ( PointType.aci == pointType ){
				updateAci(address, y);
			} else if ( PointType.sci == pointType ) {
				updateSci(address, y);
			}

		}
		logger.end(function);
	}
	
	public String getColorCSS(String alarmVector, String validity, String forcedStatus) {
		final String function = "getColorCSS";
		logger.begin(function);
		logger.trace(function, "alarmVector[{}] validity[{}] forcedStatus[{}]", new Object[]{alarmVector, validity, forcedStatus});
		
		String colorCSS	= strCSSStatusGrey;
		
		if ( DatabaseHelper.isForced(forcedStatus) ) {
			colorCSS = strCSSStatusBlue;
		} else if ( ! DatabaseHelper.isValid(validity)) {
			colorCSS = strCSSStatusGrey;
		} else if ( DatabaseHelper.isAlarm(alarmVector) ) {
			colorCSS = strCSSStatusRed;
		} else {
			colorCSS = strCSSStatusGreen;
		}
		
		logger.trace(function, "colorCode[{}]", colorCSS);
		logger.end(function);
		return colorCSS;
	}
	
	private void updateDci(String address, int row) {
		final String function = "updateDci";
		logger.begin(function);
		
		String label = null;
		String value = "";
		
		if (isValLabelFromComputedMessage(address, PointType.dci)) {
			value = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (value != null) {
				label = TranslationMgr.getInstance().getTranslation(value);
				logger.trace(function, "computedMessage[{}] translated to label[{}]", value, label);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
			logger.trace(function, "value[{}]", value);
	
			final String dalValueTableLabel = DatabaseHelper.getAttributeValue(address, PointName.dalValueTableLabel.toString(), dbvalues);
			logger.trace(function, "dalValueTableLabel[{}]", dalValueTableLabel);
			final String labels [] = DatabaseHelper.getValueTableColumn(dalValueTableLabel);
			
			final String dalValueTableValue = DatabaseHelper.getAttributeValue(address, PointName.dalValueTableValue.toString(), dbvalues);
			logger.trace(function, "dalValueTableValue[{}]", dalValueTableValue);
			final String values [] = DatabaseHelper.getValueTableColumn(dalValueTableValue);
			
			for(int r=0;r<labels.length;++r){
				if(0==value.compareTo(values[r])){
					label = labels[r];
					break;
				}
			}
		}
		
		logger.trace(function, "label[{}]", label);
		
		if ( null != label ) {
			label = DatabaseHelper.removeDBStringWrapper(label);
			txtAttributeValue[row].setText(label);	
		} else {
			value = DatabaseHelper.removeDBStringWrapper(value);
			txtAttributeValue[row].setText(value);	
		}
		
		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.dalValueAlarmVector.toString(), dbvalues);
		logger.trace(function, "dalValueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.trace(function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.dfoForcedStatus.toString(), dbvalues);
		logger.trace(function, "dfoForcedStatus[{}]", forcedStatus);
		
		String strColorCSS = getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.trace(function, "strColorCSS[{}]", strColorCSS);
		logger.end(function);
	}

	private void updateAci(String address, int row) {
		final String function = "updateAci";
		logger.begin(function);
		
		String value = STR_EMPTY;
		
		if (isValLabelFromComputedMessage(address, PointType.aci)) {
			String compMsg = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (compMsg != null) {
				value = TranslationMgr.getInstance().getTranslation(compMsg);
				logger.trace(function, "computedMessage[{}] translated to value label[{}]", compMsg, value);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
		
			logger.trace(function, "value[{}]", value);
		}
		value = DatabaseHelper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);
		
		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.aalValueAlarmVector.toString(), dbvalues);
		logger.trace(function, "valueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.trace(function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.afoForcedStatus.toString(), dbvalues);
		logger.trace(function, "forcedStatus[{}]", forcedStatus);
		
		String strColorCSS = getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.trace(function, "strColorCSS[{}]", strColorCSS);
		logger.end(function);
	}
	
	void updateSci(String address, int row) {
		final String function = "updateSci";
		logger.begin(function);
		
		String value = "";
		
		if (isValLabelFromComputedMessage(address, PointType.sci)) {
			String compMsg = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (compMsg != null) {
				value = TranslationMgr.getInstance().getTranslation(compMsg);
				logger.trace(function, "computedMessage[{}] translated to value label[{}]", compMsg, value);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
			logger.trace(function, "value[{}]", value);
		}
		value = DatabaseHelper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);

		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.salValueAlarmVector.toString(), dbvalues);
		logger.trace(function, "valueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.trace(function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.sfoForcedStatus.toString(), dbvalues);
		logger.trace(function, "forcedStatus[{}]", forcedStatus);
		
		String strColorCSS = getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.trace(function, "strColorCSS[{}]", strColorCSS);
		logger.end(function);
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttributeValue[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel vpCtrls = null;
	private DockLayoutPanel basePanel = null;
	private Panel rootPanel = null;
	@Override
	public void init() {
		final String function = "init";
		logger.begin(function);
		
		strCSSStatusGreen		= strCssPrefix + "inlinelabel-status-green";
		strCSSStatusRed			= strCssPrefix + "inlinelabel-status-red";
		strCSSStatusBlue		= strCssPrefix + "inlinelabel-status-blue";
		strCSSStatusGrey		= strCssPrefix + "inlinelabel-status-grey";
		
		vpCtrls = new VerticalPanel();
		vpCtrls.addStyleName(strCssPrefix+"panel-vpCtrls");
		
		btnUp = new Button();
		btnUp.addStyleName(strCssPrefix+"button-up");
		btnUp.setText(STR_UP);
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				onButton((Button)event.getSource());

			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName(strCssPrefix+"label-pagenum");
		lblPageNum.setText(STR_PAGER_DEFAULT);
		
		btnDown = new Button();
		btnDown.addStyleName(strCssPrefix+"button-down");
		btnDown.setText(STR_DOWN);
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButton((Button)event.getSource());
			}
		});	
		
		btnAckCurPage = new Button();
		btnAckCurPage.addStyleName(strCssPrefix+"button-ackpage");
		btnAckCurPage.setText(STR_ACK_PAGE);
		btnAckCurPage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ackPageAlarms();
			}
		});	
		
		HorizontalPanel pageBar = new HorizontalPanel();
		pageBar.addStyleName(strCssPrefix+"panel-pageBar");

		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnUp);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(lblPageNum);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnDown);
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.addStyleName(strCssPrefix+"panel-bottomBar");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnAckCurPage);
		
		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName(strCssPrefix+"panel-base");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);

		// Auto close handle
		rootPanel = new FocusPanel();
		rootPanel.add(basePanel);
		((FocusPanel)rootPanel).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ( null != event ) {
					if ( null != uiInspectorTabClickEvent ) uiInspectorTabClickEvent.onClick(); 
				} else {
					logger.warn(function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName(strCssPrefix+"panel-root");
		
		logger.end(function);
	}
	
	private void ackPageAlarms() {
		final String function = "ackPageAlarms";
		logger.begin(function);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		List<String> alarmList = new ArrayList<String>();
		for ( int x = rowBegin ; x < rowEnd ; ++x) {
			String address = this.addresses[x];
			
			logger.debug(function, "address[{}]", address);
			
			String point = DatabaseHelper.getPoint(address);
			PointType pointType = DatabaseHelper.getPointType(point);

			String valueAlarmVectorPoint = null;
			if ( PointType.dci == pointType ) {
				valueAlarmVectorPoint = PointName.dalValueAlarmVector.toString();
			} else if ( PointType.aci == pointType ){
				valueAlarmVectorPoint = PointName.aalValueAlarmVector.toString();
			} else if ( PointType.sci == pointType ) {
				valueAlarmVectorPoint = PointName.salValueAlarmVector.toString();
			}
			logger.debug(function, "pointType[{}] valueAlarmVectorPoint[{}]", pointType, valueAlarmVectorPoint);

			String valueAlarmVector = DatabaseHelper.getAttributeValue(address, valueAlarmVectorPoint, dbvalues);
			logger.debug(function, "valueAlarmVector[{}]", valueAlarmVector);
			
			String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
			logger.debug(function, "validity[{}]", validity);
			
			boolean bIsValid	= DatabaseHelper.isValid(validity);
			
			boolean bIsNeedAck	= DatabaseHelper.isNeedAck(valueAlarmVector);
			
			logger.debug(function, "address[{}] bIsValid[{}] bIsNeedAck[{}]", new Object[]{address, bIsValid, bIsNeedAck});

			if ( bIsValid && bIsNeedAck ) {
				logger.debug(function, "address[{}] added to alarm list", address);
				alarmList.add(address);
			}
		}

		if ( 0 != alarmList.size() ) {
			new AckAlarm(scsEnvId).ack(alarmList.toArray(new String[0]));
		} else {
			logger.warn(function, "alarmList.size IS ZERO");
		}

		logger.end(function);
	}

	@Override
	public Panel getMainPanel() {
		return rootPanel;
	}
	

	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		// TODO Auto-generated method stub
	}
	
	private UIInspectorTabClickEvent uiInspectorTabClickEvent = null;
	@Override
	public void setUIInspectorTabClickEvent(UIInspectorTabClickEvent uiInspectorTabClickEvent) {
		this.uiInspectorTabClickEvent = uiInspectorTabClickEvent;
	}
	
	@Override
	public void setDatabase(Database db) {
		database = db;
	}
	
	Boolean isValLabelFromComputedMessage(String dbaddress, PointType pointType) {
		final String function = "isValLabelFromComputedMessage";
		Boolean ret = false;
		
		if (pointType == PointType.aci) {
			ret = aciValLabelFromComputedMessage;
		} else if (pointType == PointType.dci) {
			ret = dciValLabelFromComputedMessage;
		} else if (pointType == PointType.sci) {
			ret = sciValLabelFromComputedMessage;
		}
		
		for (String pattern: mapIsValLabelFromComputedMessage.keySet()) {
			if (dbaddress.matches(pattern)) {
				ret = mapIsValLabelFromComputedMessage.get(pattern);
				logger.debug(function, "dbaddress [{}] matches pattern[{}]", dbaddress, pattern);
				break;
			} else {
				logger.debug(function, "dbaddress [{}] does not match pattern[{}]", dbaddress, pattern);
			}
		}
		
		return ret;
	}
	
	private void ReadLabelProperties() {
		final String function = "ReadLabelProperties";
		logger.begin(function);
		
		// Read default property for getting dynamic attribute value label
		String valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + "aciDefault";
		aciValLabelFromComputedMessage = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, false);
		
		valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + "dciDefault";
		dciValLabelFromComputedMessage = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, false);
		
		valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + "sciDefault";
		sciValLabelFromComputedMessage = ReadProp.readBoolean(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, false);

		// Read number of specific property for getting dynamic attribute value label based on dbaddress
		valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + "size";
		
		int nameCount = ReadProp.readInt(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, 0);
		logger.debug(function+" " + valueKey + "=" + Integer.toString(nameCount) );
		if (nameCount > 0) {
			for (int i=0; i<nameCount; i++) {
				valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + Integer.toString(i);
				
				// Read specific property for getting dynamic attribute value label based on dbaddress
				String propStr = ReadProp.readString(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, "");
				logger.debug(function+" " + valueKey + "=" + propStr.toString() );

				String [] propArray = propStr.split(STR_COMMA);
				if (propArray.length == 2) {
					String pattern = propArray[0].trim();
					String valueStr = propArray[1].trim();
					
					if (valueStr.compareToIgnoreCase("true") == 0) {
						mapIsValLabelFromComputedMessage.put(pattern, true);
					} else if (valueStr.compareToIgnoreCase("false") == 0) {
						mapIsValLabelFromComputedMessage.put(pattern, false);
					} else {
						logger.warn(function+"Invalid property found in " + DICTIONARY_FILE_NAME);
					}
				} else {
					logger.warn(function+"Invalid property found in " + DICTIONARY_FILE_NAME);
				}
			}
		}
		logger.end(function);
	}
}
