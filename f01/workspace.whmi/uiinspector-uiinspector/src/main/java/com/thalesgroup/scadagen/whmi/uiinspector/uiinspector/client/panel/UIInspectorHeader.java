package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.MessageBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTabClickEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.EquipmentReserve;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIInspectorHeader implements UIInspectorTab_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorHeader.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.shortLabel.toString(), PointName.geographicalCat.toString()};

	// Dynamic Attribute List
	private final String dynamicAttibutes[]	= new String[] {PointName.isControlable.toString(), PointName.resrvReservedID.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	private Database database	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "";
		
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.info(className, function, "this.scsEnvId[{}]", this.scsEnvId);
		logger.info(className, function, "this.parent[{}]", this.parent);
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
				
				String clientKey = "multiReadValue" + "_" + "inspectorheader" + "_" + "static" + "_" + parent;
				
				String[] parents = new String[]{parent};

				String[] dbaddresses = null;
				{
					ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
					for(int x=0;x<parents.length;++x) {
						for(int y=0;y<staticAttibutes.length;++y) {
							dbaddressesArrayList.add(parents[x]+staticAttibutes[y]);
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
						{
							String clientKey_multiReadValue_inspectorheader_static = "multiReadValue" + "_" + "inspectorheader" + "_" + "static" + "_" + parent;
							if ( 0 == clientKey_multiReadValue_inspectorheader_static.compareTo(key) ) {
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

				String clientKey = "multiReadValue" + "_" + "inspectorheader" + "_" + "dynamic" + "_" + parent;
				
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

				logger.info(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.info(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}

				database.subscribe(clientKey, dbaddresses, new DatabaseEvent() {
					
					@Override
					public void update(String key, String[] value) {
						{
							String clientKey_multiReadValue_inspector_dynamic = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
							if ( 0 == clientKey_multiReadValue_inspector_dynamic.compareTo(key) ) {
								String [] dbaddresses	= database.getKeyAndAddress(key);
								String [] dbvalues		= database.getKeyAndValues(key);
								HashMap<String, String> dynamicvalues = new HashMap<String, String>();
								for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
									dynamicvalues.put(dbaddresses[i], dbvalues[i]);
								}
								updateValue(key, dynamicvalues);
							}			
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
			String clientKey = "multiReadValue" + "_" + "inspectorheader" + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
		}
		{
			String clientKey = "multiReadValue" + "_" + "inspector" + "_" + "dynamic" + "_" + parent;
			database.unSubscribe(clientKey);
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
	
		if ( 0 == "static".compareTo(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( 0 == "dynamic".compareTo(clientKey.split("_")[2]) ) {
			
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

		// Equipment Description
		String shortLabelValue = DatabaseHelper.getAttributeValue(parent, PointName.shortLabel.toString(), dbvalues);
		shortLabelValue = DatabaseHelper.removeDBStringWrapper(shortLabelValue);
		if ( null != shortLabelValue ) txtAttributeStatus[0].setText(shortLabelValue);

		String geographicalCatValue = DatabaseHelper.getAttributeValue(parent, PointName.geographicalCat.toString(), dbvalues);
		geographicalCatValue = DatabaseHelper.removeDBStringWrapper(geographicalCatValue);
		if ( null != geographicalCatValue ) {
			String wordPrefix = "scsalarmList_area_location";
			String wordkey = wordPrefix + geographicalCatValue;
			geographicalCatValue = Translation.getWording(wordkey);
			txtAttributeStatus[1].setText(geographicalCatValue);
		}
		
		logger.begin(className, function);
	}
	
	private final String strNotReserved = "Not Reserved";
	private final String strReserved = "Reserved";
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueDynamic";
		
		logger.begin(className, function);

		
		String resrvReservedIDValue = DatabaseHelper.getAttributeValue(parent, PointName.resrvReservedID.toString(), dbvalues);
		resrvReservedIDValue = DatabaseHelper.removeDBStringWrapper(resrvReservedIDValue);
		if ( null != resrvReservedIDValue ) {
			int eqtReserved = EquipmentReserve.isEquipmentReservation(resrvReservedIDValue);
			String strEqtReserved = strNotReserved;
			if ( 2 == eqtReserved ) {
				strEqtReserved = strReserved;
			}
			txtAttributeStatus[3].setText(strEqtReserved);
		} else {
			txtAttributeStatus[3].setText(strNotReserved+" / "+strReserved);
		}

		final String strYes = "Yes";
		final String strNo = "No";
		String isControlableValue = DatabaseHelper.getAttributeValue(parent, PointName.isControlable.toString(), dbvalues);
		isControlableValue = DatabaseHelper.removeDBStringWrapper(isControlableValue);
		if ( null != isControlableValue ) txtAttributeStatus[2].setText((isControlableValue.equals("0")?strNo:strYes));
		
		logger.end(className, function);

	}
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel vpCtrls = null;
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strHeadersLabel [] = new String[] { "Equipment Description", "Location", "Control Right","Control Right Reserved","Handover Right" };
		String strHeadersStatus [] = new String[] { "-", "-", "Yes / No","Not Reserved / Not", "Central / Station" };
		
		FlexTable flexTableHeader = new FlexTable();
		flexTableHeader.addStyleName("project-gwt-flextable-header");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			InlineLabel inlineLabel = new InlineLabel(strHeadersLabel[i]);
			inlineLabel.getElement().getStyle().setPadding(10, Unit.PX);	
			inlineLabel.addStyleName("project-gwt-inlinelabel-headerlabel");
			flexTableHeader.setWidget(i, 0, inlineLabel);
			txtAttributeStatus[i] = new TextBox();
			txtAttributeStatus[i].setText(strHeadersStatus[i]);
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
