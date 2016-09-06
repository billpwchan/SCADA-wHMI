package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointType;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

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
			
			String clientKey = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
				for ( String dbaddress : this.addresses ) {
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
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

			Database database = Database.getInstance();
			
			String api = "multiReadValue";
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					Database database = Database.getInstance();
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
					String point = RTDB_Helper.getPoint(dbaddress);
					if ( null != point ) {
						PointType pointType = RTDB_Helper.getPointType(point);
						if ( pointType == PointType.dci ) {
							for ( String attribute : dynamicDciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.aci ) {
							for ( String attribute : dynamicAciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
							}
						} else if ( pointType == PointType.sci ) {
							for ( String attribute : dynamicSciAttibutes ) {
								dbaddressesArrayList.add(dbaddress+attribute);
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
			updateValue(true);
		}
		
		logger.begin(className, function);
	}
	
	@Override
	public void buildWidgets() {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		buildWidgets(this.addresses.length);
	
		logger.end(className, function);
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	void buildWidgets(int numOfWidgets) {
		final String function = "buildWidgets";
		
		logger.begin(className, function);
		
		logger.info(className, function, "numOfWidgets[{}]", numOfWidgets);
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			pageCounter = new PageCounter(numOfWidgets, 10);
			pageCounter.calc(pageIndex);
			
			updatePager();
			
			int numOfWidgetShow = pageCounter.pageRowCount;
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[numOfWidgetShow];
				txtAttributeValue	= new TextBox[numOfWidgetShow];
				txtAttibuteColor	= new InlineLabel[numOfWidgetShow];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				for( int i = 0 ; i < numOfWidgetShow ; ++i ) {
					
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

			} else {
				logger.info(className, function, "this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes);
			
		} else {
			logger.info(className, function, "points IS NULL");
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
					value = RTDB_Helper.removeDBStringWrapper(value);
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
			
			String point = RTDB_Helper.getPoint(address);
			PointType pointType = RTDB_Helper.getPointType(point);

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
		
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.debug(className, function, "PointName.value.toString()[{}] dbaddress[{}]", PointName.value.toString(), dbaddress);
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
			}
		}		
		
		logger.debug(className, function, "value[{}]", value);
		
		String valueTable = null;
		{
			String dbaddress = address + PointName.dalValueTable.toString();
			logger.debug(className, function, "PointName.value.toString()Table[{}] dbaddress[{}]", PointName.dalValueTable.toString(), dbaddress);
			if ( dbvalues.containsKey(dbaddress) ) {
				valueTable = dbvalues.get(dbaddress);
			} else {
				logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
			}					
		}
		
		logger.debug(className, function, "valueTable[{}]", valueTable);
		
		String label = null;
		{
			int valueCol = 0, labelCol = 1;
			
			logger.debug(className, function, "valueCol[{}] nameCol[{}]", valueCol, labelCol);
			
			for( int r = 0 ; r < 12 ; ++r ) {
				String v = RTDB_Helper.getArrayValues(valueTable, valueCol, r );
				logger.debug(className, function, "getvalue r[{}] v[{}] == valueTable[i][{}]", new Object[]{r, v, valueTable});
				if ( 0 == v.compareTo(value) ) {
					logger.debug(className, function, "getname r[{}] v[{}] == valueTable[i][{}]", new Object[]{r, v, valueTable});
					label = RTDB_Helper.getArrayValues(valueTable, labelCol, r );
					break;
				}
			}
		}
		
		logger.debug(className, function, "name[{}]", label);
		
		if ( null != label ) {
			label = RTDB_Helper.removeDBStringWrapper(label);
			txtAttributeValue[row].setText(label);	
		} else {
			value = RTDB_Helper.removeDBStringWrapper(value);
			txtAttributeValue[row].setText(value);	
		}
		
		String valueAlarmVector = null;
		String validity = null;
		String forcedStatus = null;
		{
			{
				String dbaddress = address + PointName.dalValueAlarmVector.toString();
				logger.debug(className, function, "PointName.value.toString() dalValueAlarmVector[{}] dbaddress[{}]", PointName.dalValueAlarmVector.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "row[{}] valueAlarmVector[{}] address[{}] PointName.dalValueTable.toString()[{}]"
					, new Object[]{row, valueAlarmVector, address, PointName.dalValueTable.toString()});
			
			logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.debug(className, function, "PointName.validity.toString()[{}] dbaddress[{}]", PointName.validity.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "validity[{}]", validity);
			
			{
				String dbaddress = address + PointName.dfoForcedStatus.toString();
				logger.debug(className, function, "strForcedStatus[{}] dbaddress[{}]", PointName.dfoForcedStatus.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "forcedStatus[{}]", forcedStatus);

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.debug(className, function, "strColorCSS[{}]", strColorCSS);
		
		logger.end(className, function);
	}

	private void updateAci(String address, int row) {
		final String function = "updateAci";
		
		logger.begin(className, function);
		
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.debug(className, function, "PointName.value.toString()[{}] dbaddress[{}]", PointName.value.toString(), dbaddress);
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
			}
		}		
		
		logger.debug(className, function, "value[{}]", value);
		
		value = RTDB_Helper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);	

		String valueAlarmVector = null;
		String validity = null;
		String forcedStatus = null;
		{
			{
				String dbaddress = address + PointName.aalValueAlarmVector.toString();
				logger.debug(className, function, "PointName.value.toString()AlarmVector[{}] dbaddress[{}]", PointName.aalValueAlarmVector.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.info(className, function, "PointName.validity.toString()[{}] dbaddress[{}]", PointName.validity.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "validity[{}]", validity);
			
			{
				String dbaddress = address + PointName.afoForcedStatus.toString();
				logger.info(className, function, "strForcedStatus[{}] dbaddress[{}]", PointName.afoForcedStatus.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.info(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "forcedStatus[{}]", forcedStatus);

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.debug(className, function, "strColorCSS[{}]", strColorCSS);
		
		logger.end(className, function);
	}
	
	void updateSci(String address, int row) {
		final String function = "updateSci";
		
		logger.begin(className, function);
		
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.debug(className, function, "PointName.value.toString()[{}] dbaddress[{}]", PointName.value.toString(), dbaddress);
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
			}
		}		
		
		logger.debug(className, function, "value[{}]", value);
		
		value = RTDB_Helper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);

		String valueAlarmVector = null;
		String validity = null;
		String forcedStatus = null;
		{
			{
				String dbaddress = address + PointName.salValueAlarmVector.toString();
				logger.debug(className, function, "PointName.value.toString()AlarmVector[{}] dbaddress[{}]", PointName.salValueAlarmVector.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "valueAlarmVector[{}]", valueAlarmVector);
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.debug(className, function, "PointName.validity.toString()["+PointName.validity.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!", dbaddress);
				}
			}
			
			logger.debug(className, function, "validity[{}]", validity);
			
			{
				String dbaddress = address + PointName.sfoForcedStatus.toString();
				logger.debug(className, function, "strForcedStatus[{}] dbaddress[{}]", PointName.sfoForcedStatus.toString(), dbaddress);
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.warn(className, function, "dbaddress[{}] VALUE NOT EXISTS!",  dbaddress);
				}
			}
			
			logger.debug(className, function, "forcedStatus[{}]", forcedStatus);

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
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
	@Override
	public void init(String xml) {
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
				// TODO Auto-generated method stub
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
		
		logger.begin(className, function);
	}
	

	@Override
	public ComplexPanel getMainPanel() {
		return basePanel;
	}
	
//	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
//		this.messageBoxEvent = messageBoxEvent;
	}
}
