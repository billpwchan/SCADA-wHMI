package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorPage_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;

public class UIInspectorInfo implements UIInspectorPage_i {
	
	private final Logger logger = Logger.getLogger(UIInspectorInfo.class.getName());
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorInfo.class.getName());
	private final String logPrefix = "["+className+"] ";
	
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
		this.parent = parent;
		this.scsEnvId = scsEnvId;
		logger.log(Level.FINE, logPrefix+"setConnection this.parent["+this.parent+"]");
		logger.log(Level.FINE, logPrefix+"setConnection this.scsEnvId["+this.scsEnvId+"]");
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		logger.log(Level.FINE, logPrefix+"setAddresses Begin");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, logPrefix+"setAddresses End");
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}

	@Override
	public void connect() {
		logger.log(Level.FINE, logPrefix+"connect Begin");

		// Read static
		{
			logger.log(Level.FINE, logPrefix+"multiReadValue Begin");
			
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
							logger.log(Level.SEVERE, logPrefix+"connectInspector"+tagname+" dbaddress IS UNKNOW TYPE");
						}
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			logger.log(Level.FINE, logPrefix+"multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, logPrefix+"multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
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
			
			logger.log(Level.FINE, logPrefix+"multiReadValue End");
		}
		
		// Read dynamic
		{
			logger.log(Level.FINE, logPrefix+"multiReadValue Begin");
			
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
							logger.log(Level.SEVERE, logPrefix+"connectInspector"+tagname+" dbaddress IS UNKNOW TYPE");
						}
					} else {
						logger.log(Level.SEVERE, logPrefix+"connectInspector"+tagname+" point IS NULL");
					}
				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}
			
			logger.log(Level.FINE, logPrefix+"multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
			for(int i = 0; i < dbaddresses.length; ++i ) {
				logger.log(Level.FINE, logPrefix+"multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
			}
			
			Database database = Database.getInstance();
			database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {

				@Override
				public void update(String key, String[] value) {
					// TODO Auto-generated method stub
					
				}
				
			});
		
			logger.log(Level.FINE, logPrefix+"multiReadValue End");
		}
		
		logger.log(Level.FINE, logPrefix+"connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, logPrefix+"disconnect Begin");
		
		logger.log(Level.FINE, logPrefix+"disconnect End");
	}
	
	private void updatePager() {
		
		logger.log(Level.FINE, logPrefix+"updatePager Begin");
		
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
		
		logger.log(Level.FINE, logPrefix+"updatePager End");
	}
	
	private void onButton(Button btn) {
		
		logger.log(Level.FINE, logPrefix+"onButton Begin");
		
		if (  btn == btnUp || btn == btnDown ) {
			if ( btn == btnUp) {
				--pageIndex;
			} else if ( btn == btnDown ) {
				++pageIndex;
			}
			updatePager();
			updateValue(true);
		}
		
		logger.log(Level.FINE, logPrefix+"onButton End");
	}
	
	@Override
	public void buildWidgets() {
		
		logger.log(Level.FINE, logPrefix+"buildWidgets Begin");
		
		buildWidgets(this.addresses.length);
	
		logger.log(Level.FINE, logPrefix+"buildWidgets End");
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, logPrefix+"buildWidgets Begin");
		
		logger.log(Level.FINE, logPrefix+"buildWidgets numOfWidgets["+numOfWidgets+"]");
		
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
					
					logger.log(Level.FINE, logPrefix+"buildWidgets i["+i+"]");
						
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
				logger.log(Level.FINE, logPrefix+"buildWidgets this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes);
			
		} else {
			logger.log(Level.FINE, logPrefix+"buildWidgets points IS NULL");
		}
		
		logger.log(Level.FINE, logPrefix+"buildWidgets End");
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {

		logger.log(Level.FINE, logPrefix+"updateValue Begin");
		logger.log(Level.FINE, logPrefix+"updateValue clientkey["+clientKey+"]");
		
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

		logger.log(Level.FINE, logPrefix+"updateValue End");
	}
	
	private void updateValue(boolean withStatic) {
		
		logger.log(Level.FINE, logPrefix+"updateValue Begin");
		
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
		
		logger.log(Level.FINE, logPrefix+"updateValue End");
	}
	
	private void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		logger.log(Level.FINE, logPrefix+"updateValueStatic Begin");
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.log(Level.FINE, logPrefix+"updateValue Begin");
		logger.log(Level.FINE, logPrefix+"updateValue clientkey["+clientKey+"]");
		
		String clientKeyStatic = "multiReadValue" + "_" + "inspector" + tagname + "_" + "static" + "_" + parent;
		
		logger.log(Level.FINE, logPrefix+"updateValue clientKeyStatic["+clientKeyStatic+"]");
		
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
		
		logger.log(Level.FINE, logPrefix+"updateValueStatic End");
	}
	
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, logPrefix+"updateValueDynamic Begin");
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;

		logger.log(Level.FINE, logPrefix+"updateValue Begin");
		logger.log(Level.FINE, logPrefix+"updateValue clientkey["+clientKey+"]");
		
		for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
			String address = this.addresses[x];
			
			logger.log(Level.FINE, logPrefix+"updateValue address["+address+"]");
			
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

		logger.log(Level.FINE, logPrefix+"updateValueDynamic End");
	}
	
	private void updateDci(String address, int row) {
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.log(Level.FINE, logPrefix+"updateDci PointName.value.toString()["+PointName.value.toString()+"] dbaddress["+dbaddress+"]");
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.log(Level.SEVERE, logPrefix+"updateDci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}
		}		
		
		logger.log(Level.FINE, logPrefix+"updateDci value["+value+"]");
		
		String valueTable = null;
		{
			String dbaddress = address + PointName.dalValueTable.toString();
			logger.log(Level.FINE, logPrefix+"updateDci PointName.value.toString()Table["+PointName.dalValueTable.toString()+"] dbaddress["+dbaddress+"]");
			if ( dbvalues.containsKey(dbaddress) ) {
				valueTable = dbvalues.get(dbaddress);
			} else {
				logger.log(Level.SEVERE, logPrefix+"updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}					
		}
		
		logger.log(Level.FINE, logPrefix+"updateDci valueTable["+valueTable+"]");
		
		String label = null;
		{
			int valueCol = 0, labelCol = 1;
			
			logger.log(Level.FINE, logPrefix+"updateDci valueCol["+valueCol+"] nameCol["+labelCol+"]");
			
			for( int r = 0 ; r < 12 ; ++r ) {
				String v = RTDB_Helper.getArrayValues(valueTable, valueCol, r );
				logger.log(Level.FINE, logPrefix+"updateDci getvalue r["+r+"] v["+v+"] == valueTable[i]["+valueTable+"]");
				if ( 0 == v.compareTo(value) ) {
					logger.log(Level.FINE, logPrefix+"updateDci getname r["+r+"] v["+v+"] == valueTable[i]["+valueTable+"]");
					label = RTDB_Helper.getArrayValues(valueTable, labelCol, r );
					break;
				}
			}
		}
		
		logger.log(Level.FINE, logPrefix+"updateDci name["+label+"]");
		
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
				logger.log(Level.FINE, logPrefix+"updateDci PointName.value.toString() dalValueAlarmVector["+PointName.dalValueAlarmVector.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateDci row["+row+"] valueAlarmVector["+valueAlarmVector+"] address["+address+"] PointName.dalValueTable.toString()["+PointName.dalValueTable.toString()+"]");
			
			logger.log(Level.FINE, logPrefix+"updateDci valueAlarmVector["+valueAlarmVector+"]");
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.log(Level.FINE, logPrefix+"updateValue PointName.validity.toString()["+PointName.validity.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateDci validity["+validity+"]");
			
			{
				String dbaddress = address + PointName.dfoForcedStatus.toString();
				logger.log(Level.FINE, logPrefix+"updateDci strForcedStatus["+PointName.dfoForcedStatus.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateDci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateDci forcedStatus["+forcedStatus+"]");

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.log(Level.FINE, logPrefix+"updateDci strColorCSS["+strColorCSS+"]");
	}

	private void updateAci(String address, int row) {
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.log(Level.FINE, logPrefix+"updateAci PointName.value.toString()["+PointName.value.toString()+"] dbaddress["+dbaddress+"]");
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.log(Level.SEVERE, logPrefix+"updateAci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}
		}		
		
		logger.log(Level.FINE, logPrefix+"updateAci value["+value+"]");
		
		value = RTDB_Helper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);	

		String valueAlarmVector = null;
		String validity = null;
		String forcedStatus = null;
		{
			{
				String dbaddress = address + PointName.aalValueAlarmVector.toString();
				logger.log(Level.FINE, logPrefix+"updateAci PointName.value.toString()AlarmVector["+PointName.aalValueAlarmVector.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateAci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateAci valueAlarmVector["+valueAlarmVector+"]");
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.log(Level.FINE, logPrefix+"updateAci PointName.validity.toString()["+PointName.validity.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateAci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateAci validity["+validity+"]");
			
			{
				String dbaddress = address + PointName.afoForcedStatus.toString();
				logger.log(Level.FINE, logPrefix+"updateAci strForcedStatus["+PointName.afoForcedStatus.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateAci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateAci forcedStatus["+forcedStatus+"]");

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.log(Level.FINE, logPrefix+"updateAci strColorCSS["+strColorCSS+"]");
	}
	
	void updateSci(String address, int row) {
		String value = null;
		{
			String dbaddress = address + PointName.value.toString();
			logger.log(Level.FINE, logPrefix+"updateSci PointName.value.toString()["+PointName.value.toString()+"] dbaddress["+dbaddress+"]");
			if ( dbvalues.containsKey(dbaddress) ) {
				value = dbvalues.get(dbaddress);
			} else {
				logger.log(Level.SEVERE, logPrefix+"updateSci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}
		}		
		
		logger.log(Level.FINE, logPrefix+"updateSci value["+value+"]");
		
		value = RTDB_Helper.removeDBStringWrapper(value);
		txtAttributeValue[row].setText(value);	

		String valueAlarmVector = null;
		String validity = null;
		String forcedStatus = null;
		{
			{
				String dbaddress = address + PointName.salValueAlarmVector.toString();
				logger.log(Level.FINE, logPrefix+"updateSci PointName.value.toString()AlarmVector["+PointName.salValueAlarmVector.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					valueAlarmVector = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateSci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateSci valueAlarmVector["+valueAlarmVector+"]");
			
			{
				String dbaddress = address + PointName.validity.toString();
				logger.log(Level.FINE, logPrefix+"updateSci PointName.validity.toString()["+PointName.validity.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					validity = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateSci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateSci validity["+validity+"]");
			
			{
				String dbaddress = address + PointName.sfoForcedStatus.toString();
				logger.log(Level.FINE, logPrefix+"updateSci strForcedStatus["+PointName.sfoForcedStatus.toString()+"] dbaddress["+dbaddress+"]");
				if ( dbvalues.containsKey(dbaddress) ) {
					forcedStatus = dbvalues.get(dbaddress);
				} else {
					logger.log(Level.SEVERE, logPrefix+"updateSci dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
				}
			}
			
			logger.log(Level.FINE, logPrefix+"updateSci forcedStatus["+forcedStatus+"]");

		}
		
		String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
		txtAttibuteColor[row].setStyleName(strColorCSS);
		
		logger.log(Level.FINE, logPrefix+"updateSci strColorCSS["+strColorCSS+"]");
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
		
		logger.log(Level.FINE, logPrefix+"init Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
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
		
		logger.log(Level.FINE, logPrefix+"init End");
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
