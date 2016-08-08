package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.RTDB_Helper.PointName;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspectorPage_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIInspectorHeader implements UIInspectorPage_i {
	
	private final Logger logger = Logger.getLogger(UIInspectorInfo.class.getName());

	// Static Attribute List
	private final String staticAttibutes[]	= new String[] {PointName.shortLabel.toString(), PointName.geographicalCat.toString()};

	// Dynamic Attribute List
	private final String dynamicAttibutes[]	= new String[] {PointName.isControlable.toString()};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String scsEnvId, String parent) {
		this.scsEnvId = scsEnvId;
		this.parent = parent;
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		logger.log(Level.FINE, "setConnection this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String[] addresses) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		logger.log(Level.FINE, "setAddresses End");
	}
	
	@Override
	public String[] getAddresses() {
		return this.addresses;
	}
	
	private Database database = Database.getInstance();
	
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");

			{
				logger.log(Level.FINE, "multiReadValue Begin");
				
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
				
				logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
				}

				String api = "multiReadValue";
				database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
					
					@Override
					public void update(String key, String[] value) {
						{
							Database database = Database.getInstance();
							
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
				
				logger.log(Level.FINE, "multiReadValue End");
			}
			
			{
				
				logger.log(Level.FINE, "multiReadValue Begin");

				String clientKey = "multiReadValue" + "_" + "inspectorheader" + "_" + "dynamic" + "_" + parent;
				String[] dbaddresses = null;
				{
					ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
					for(int y=0;y<dynamicAttibutes.length;++y) {
						dbaddressesArrayList.add(parent+dynamicAttibutes[y]);
					}
					dbaddresses = dbaddressesArrayList.toArray(new String[0]);
				}

				logger.log(Level.FINE, "multiReadValue key["+clientKey+"] scsEnvId["+scsEnvId+"]");
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.log(Level.FINE, "multiReadValue dbaddresses("+i+")["+dbaddresses[i]+"]");
				}

				database.addDynamicRequest(clientKey, dbaddresses, new DatabaseEvent() {
					
					@Override
					public void update(String key, String[] value) {
						{
							Database database = Database.getInstance();
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
				
				logger.log(Level.FINE, "multiReadValue End");
			}
		
		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		logger.log(Level.FINE, "disconnect End");
	}
	
	@Override
	public void buildWidgets() {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		buildWidgets(this.addresses.length);
	
		logger.log(Level.FINE, "buildWidgets End");
	}
	
	private TextBox txtAttributeStatus[] = null;
	void buildWidgets(int numOfWidgets) {

	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {

		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
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

		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValue(boolean withStatic) {
		
		logger.log(Level.FINE, "updateValue Begin");
		
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
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	private void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValueStatic Begin");

		// Equipment Description
		{
			String key = parent + PointName.shortLabel.toString();
			if ( dbvalues.containsKey(key) ) {
				String value = dbvalues.get(key);
				value = RTDB_Helper.removeDBStringWrapper(value);
				if ( null != value ) txtAttributeStatus[0].setText(value);
			}			
		}
		
		{
			String key = parent + PointName.geographicalCat.toString();
			if ( dbvalues.containsKey(key) ) {
				String value = dbvalues.get(key);
				value = RTDB_Helper.removeDBStringWrapper(value);
				if ( null != value ) {
					String wordPrefix = "scsalarmList_area_location";
					String wordkey = wordPrefix + value;
					value = Translation.getWording(wordkey);
					logger.log(Level.SEVERE, "Translation.getHVWording wordPrefix["+wordPrefix+"] value["+value+"] => wordkey["+wordkey+"] => value["+value+"]");
					txtAttributeStatus[1].setText(value);
				}
			}
		}
		
		logger.log(Level.FINE, "updateValueStatic End");
	}
	
	private void updateValueDynamic(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValueDynamic Begin");
		
		{
			String key = parent + PointName.isControlable.toString();
			if ( dbvalues.containsKey(key) ) {
				String value = dbvalues.get(key);
				value = RTDB_Helper.removeDBStringWrapper(value);
				if ( null != value ) txtAttributeStatus[2].setText((value.equals("0")?"No":"Yes"));
			}
		}
		
		logger.log(Level.FINE, "updateValueDynamic End");

	}
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private VerticalPanel vpCtrls = null;
	@Override
	public void init(String xml) {
		
		logger.log(Level.FINE, "init Begin");
		
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
		
		logger.log(Level.FINE, "init End");
	}
	
	@Override
	public ComplexPanel getMainPanel() {
		return vpCtrls;
	}

//	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
//		this.messageBoxEvent = messageBoxEvent;
	}

}
