package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.ReadProp;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIInspectorInfo implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorInfo.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String tagname			= "info";
	
	public final String strCSSStatusGreen		= "project-gwt-inlinelabel-inspector"+tagname+"status-green";
	public final String strCSSStatusRed			= "project-gwt-inlinelabel-inspector"+tagname+"status-red";
	public final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector"+tagname+"status-blue";
	public final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector"+tagname+"status-grey";
	
	// Static Attribute List
	private final String staticAciAttibutes [] = new String[] {PointName.label.toString(), PointName.aalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticDciAttibutes [] = new String[] {PointName.label.toString(), PointName.dalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticSciAttibutes [] = new String[] {PointName.label.toString(), PointName.salValueTable.toString(), PointName.hmiOrder.toString()};
	
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
	private static final String VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX = "inspectorpanel.info.valLabelFromComputedMessage.";
	
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
		
		ReadLabelProperties();
		
		logger.begin(className, function);

		// Read static
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;

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
							logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			logger.info(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.info(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
			}
			
			String api = "multiReadValue";
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
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
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;

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
							logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
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
		logger.begin(className, function);
		{
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
		}
		logger.end(className, function);
	}
	
	private void updatePager() {
		final String function = "updatePager";
		
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
				
		if ( pageCounter.hasPreview || pageCounter.hasNext ) {
			btnUp.setEnabled(pageCounter.hasPreview);
			lblPageNum.setText(String.valueOf(pageIndex+1) + " / " + String.valueOf(pageCounter.pageCount));
			btnDown.setEnabled(pageCounter.hasNext);
		} else {
			btnUp.setVisible(false);
			lblPageNum.setVisible(false);
			btnDown.setVisible(false);
		}
		
		logger.end(className, function);
	}
	
	private void updateLayout() {
		final String function = "updateLayout";
		
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		int pageSize = pageCounter.pageSize;
		
		int stopper = pageCounter.pageRowCount;
		
		logger.info(className, function
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

		logger.end(className, function);
	}
	
	private void onButton(Button btn) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
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
		
		logger.begin(className, function);
	}
	
	@Override
	public void buildWidgets(int numOfPointEachPage) {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		buildWidgets(this.addresses.length, numOfPointEachPage);
	
		logger.end(className, function);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	void buildWidgets(int numOfWidgets, int numOfPointEachPage) {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		logger.info(className, function, "numOfWidgets[{}]", numOfWidgets);
		logger.info(className, function, "numOfPointEachPage[{}]", numOfPointEachPage);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, numOfPointEachPage);
			pageCounter.calc(pageIndex);
			
			int pageSize = pageCounter.pageSize;
			
			logger.info(className, function, "pageSize[{}]", pageSize);
			
//			if ( DatabaseHelper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[pageSize];
				txtAttributeValue	= new TextBox[pageSize];
				txtAttibuteColor	= new InlineLabel[pageSize];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				for( int i = 0 ; i < pageSize ; ++i ) {
					
					logger.info(className, function, "i[{}]", i);
						
					lblAttibuteLabel[i] = new InlineLabel();
					lblAttibuteLabel[i].setWidth("100%");
					lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-label");
					lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
					flexTableAttibutes.setWidget(i+1, 0, lblAttibuteLabel[i]);
					txtAttributeValue[i] = new TextBox();
					txtAttributeValue[i].setWidth("95%");
					txtAttributeValue[i].setText("ATTRIBUTE_STATUS_"+(i+1));
					txtAttributeValue[i].addStyleName("project-gwt-textbox-inspector-"+tagname+"-value");
					txtAttributeValue[i].setReadOnly(true);
					txtAttributeValue[i].setMaxLength(16);
					flexTableAttibutes.setWidget(i+1, 1, txtAttributeValue[i]);
					txtAttibuteColor[i] = new InlineLabel();
					txtAttibuteColor[i].setText("R");
					txtAttibuteColor[i].setStyleName(strCSSStatusGrey);
					flexTableAttibutes.setWidget(i+1, 2, txtAttibuteColor[i]);
				}

				flexTableAttibutes.getColumnFormatter().addStyleName(0, "project-gwt-flextable-inspectorlabel-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(1, "project-gwt-flextable-inspectorvalue-col");
				flexTableAttibutes.getColumnFormatter().addStyleName(2, "project-gwt-flextable-inspectorstatus-col");

//			} else {
//				logger.info(className, function, "this.addresses IS INVALID");
//			}
			
			vpCtrls.add(flexTableAttibutes);
			
			updatePager();
			updateLayout();
			
		} else {
			logger.warn(className, function, "points IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
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
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.info(className, function, "clientkey[{}]", clientKey);
		
		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		logger.info(className, function, "clientKeyStatic[{}]", clientKeyStatic);
		
		if ( clientKeyStatic.equals(clientKey) ) {
			
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
		
		logger.end(className, function);
	}
	
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.debug(className, function, "clientkey[{}]", clientKey);
		
		for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
			String address = this.addresses[x];
			
			logger.debug(className, function, "address[{}]", address);
			
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

		logger.end(className, function);
	}
	
	private void updateDci(String address, int row) {
		final String function = "updateDci";
		
		logger.begin(className, function);
		
		String label = null;
		String value = "";
		
		if (isValLabelFromComputedMessage(address, PointType.dci)) {
			value = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (value != null) {
				label = Translation.getDBMessage(value);
				logger.debug(className, function, "computedMessage[{}] translated to label[{}]", value, label);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
			logger.debug(className, function, "value[{}]", value);
		
			String valueTable = DatabaseHelper.getAttributeValue(address, PointName.dalValueTable.toString(), dbvalues);
			logger.debug(className, function, "valueTable[{}]", valueTable);
				
			{
				int valueCol = 0, labelCol = 1;
				
				logger.debug(className, function, "valueCol[{}] nameCol[{}]", valueCol, labelCol);
				
				for( int r = 0 ; r < 12 ; ++r ) {
					String v = DatabaseHelper.getArrayValues(valueTable, valueCol, r );
					logger.debug(className, function, "getvalue r[{}] v[{}] == valueTable[i][{}]", new Object[]{r, v, valueTable});
					if ( 0 == v.compareTo(value) ) {
						logger.debug(className, function, "getname r[{}] v[{}] == valueTable[i][{}]", new Object[]{r, v, valueTable});
						label = DatabaseHelper.getArrayValues(valueTable, labelCol, r );
						break;
					}
				}
			}
		}
		
		logger.debug(className, function, "name[{}]", label);
		
		if ( null != label ) {
			label = DatabaseHelper.removeDBStringWrapper(label);
			txtAttributeValue[row].setText(label);	
		} else {
			value = DatabaseHelper.removeDBStringWrapper(value);
			txtAttributeValue[row].setText(value);	
		}
		
		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.dalValueAlarmVector.toString(), dbvalues);
		logger.debug(className, function, "dalValueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.debug(className, function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.dfoForcedStatus.toString(), dbvalues);
		logger.debug(className, function, "dfoForcedStatus[{}]", forcedStatus);
		
		String strColorCSS = DatabaseHelper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.debug(className, function, "strColorCSS[{}]", strColorCSS);
		
		logger.end(className, function);
	}

	private void updateAci(String address, int row) {
		final String function = "updateAci";
		
		logger.begin(className, function);
		
		String value = "";
		
		if (isValLabelFromComputedMessage(address, PointType.aci)) {
			String compMsg = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (compMsg != null) {
				value = Translation.getDBMessage(compMsg);
				logger.debug(className, function, "computedMessage[{}] translated to value label[{}]", compMsg, value);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
		
			logger.debug(className, function, "value[{}]", value);
		}
		value = DatabaseHelper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);
		
		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.aalValueAlarmVector.toString(), dbvalues);
		logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.debug(className, function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.afoForcedStatus.toString(), dbvalues);
		logger.debug(className, function, "forcedStatus[{}]", forcedStatus);
		
		String strColorCSS = DatabaseHelper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.debug(className, function, "strColorCSS[{}]", strColorCSS);
		
		logger.end(className, function);
	}
	
	void updateSci(String address, int row) {
		final String function = "updateSci";
		
		logger.begin(className, function);
		
		String value = "";
		
		if (isValLabelFromComputedMessage(address, PointType.sci)) {
			String compMsg = DatabaseHelper.getAttributeValue(address, PointName.computedMessage.toString(), dbvalues);
			if (compMsg != null) {
				value = Translation.getDBMessage(compMsg);
				logger.debug(className, function, "computedMessage[{}] translated to value label[{}]", compMsg, value);
			}
		} else {
			value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
			logger.debug(className, function, "value[{}]", value);
		}
		value = DatabaseHelper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);

		String valueAlarmVector = DatabaseHelper.getAttributeValue(address, PointName.salValueAlarmVector.toString(), dbvalues);
		logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
		
		String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
		logger.debug(className, function, "validity[{}]", validity);
		
		String forcedStatus = DatabaseHelper.getAttributeValue(address, PointName.sfoForcedStatus.toString(), dbvalues);
		logger.debug(className, function, "forcedStatus[{}]", forcedStatus);
		
		String strColorCSS = DatabaseHelper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.debug(className, function, "strColorCSS[{}]", strColorCSS);
		
		logger.end(className, function);
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
		
		logger.begin(className, function);
		
		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				onButton((Button)event.getSource());

			}
		});
		
		lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onButton((Button)event.getSource());
			}
		});	
		
		Button btnAckCurPage = new Button();
		btnAckCurPage.addStyleName("project-gwt-button-inspector-"+tagname+"-ackpage");
		btnAckCurPage.setText("Ack. Page");
		btnAckCurPage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ackPageAlarms();
			}
		});	
		
		HorizontalPanel pageBar = new HorizontalPanel();

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
		bottomBar.addStyleName("project-gwt-panel-inspector-"+tagname+"-bottom");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnAckCurPage);
		
		basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-"+tagname+"-inspector");
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
					logger.info(className, function, "event IS NULL");
				}
			}
		});
		rootPanel.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		logger.begin(className, function);
	}
	
	private void ackPageAlarms() {
		final String function = "ackPageAlarms";
		
		logger.begin(className, function);
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		ArrayList<String> alarmList = new ArrayList<String>();
		for ( int x = rowBegin ; x < rowEnd ; ++x) {
			String address = this.addresses[x];
			
			logger.debug(className, function, "address[{}]", address);
			
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
			logger.debug(className, function, "pointType[{}] valueAlarmVectorPoint[{}]", pointType, valueAlarmVectorPoint);

			String valueAlarmVector = DatabaseHelper.getAttributeValue(address, valueAlarmVectorPoint, dbvalues);
			logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
			
			String validity = DatabaseHelper.getAttributeValue(address, PointName.validity.toString(), dbvalues);
			logger.debug(className, function, "validity[{}]", validity);
			
			boolean bIsValid	= DatabaseHelper.isValid(validity);
			
			boolean bIsAlarm	= DatabaseHelper.isAlarm(valueAlarmVector);
			
			boolean bIsNeedAck	= DatabaseHelper.isNeedAck(valueAlarmVector);
			
			logger.info(className, function, "address[{}] bIsValid[{}] bIsAlarm[{}] bIsNeedAck[{}]", new Object[]{address, bIsValid, bIsAlarm, bIsNeedAck});

			if ( bIsValid && bIsAlarm && bIsNeedAck ) {
				
				logger.info(className, function, "address[{}] added to alarm list", address);
				alarmList.add(address);
			
			}
		}
		
		String [] alarmIds = alarmList.toArray(new String[0]);
		
		if ( logger.isInfoEnabled() ) {
			for ( String alarmId : alarmIds ) {
				logger.info(className, function, "alarmId[{}]", alarmId);
			}
		}
		
		logger.warn(className, function, "ackAlarms page disabled!");
		
//		String key = "ackalarms";
//		String comment = "";
//		int inUserId = 0;
//		AlmMgr almMgr = AlmMgr.getInstance("AlmMgr");
//		almMgr.ackAlarms(key, scsEnvId, alarmIds, comment, inUserId);

		logger.end(className, function);
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
				logger.debug(className, function, "dbaddress [{}] matches pattern[{}]", dbaddress, pattern);
				break;
			} else {
				logger.debug(className, function, "dbaddress [{}] does not match pattern[{}]", dbaddress, pattern);
			}
		}
		
		return ret;
	}
	
	private void ReadLabelProperties() {
		logger.debug("UIInspectorInfo ReadLabelProperties");
		
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
		logger.debug("ReadLabelProperties " + valueKey + "=" + Integer.toString(nameCount) );
		if (nameCount > 0) {
			for (int i=0; i<nameCount; i++) {
				valueKey = VAL_LABEL_FROM_COMPUTED_MESSAGE_PREFIX + Integer.toString(i);
				
				// Read specific property for getting dynamic attribute value label based on dbaddress
				String propStr = ReadProp.readString(DICTIONARY_CACHE_NAME, DICTIONARY_FILE_NAME, valueKey, "");
				logger.trace("ReadLabelProperties " + valueKey + "=" + propStr.toString() );

				String [] propArray = propStr.split(",");
				if (propArray.length == 2) {
					String pattern = propArray[0].trim();
					String valueStr = propArray[1].trim();
					
					if (valueStr.compareToIgnoreCase("true") == 0) {
						mapIsValLabelFromComputedMessage.put(pattern, true);
					} else if (valueStr.compareToIgnoreCase("false") == 0) {
						mapIsValLabelFromComputedMessage.put(pattern, false);
					} else {
						logger.warn("Invalid property found in " + DICTIONARY_FILE_NAME);
					}
				} else {
					logger.warn("Invalid property found in " + DICTIONARY_FILE_NAME);
				}
			}
		}
	}
}
