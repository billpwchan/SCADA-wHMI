package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorInfo implements UIInspectorTab_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorInfo.class.getName());
	
	private String tagname			= "info";
	
	public final String strCSSStatusGreen		= "project-gwt-inlinelabel-inspector"+tagname+"status-green";
	public final String strCSSStatusRed			= "project-gwt-inlinelabel-inspector"+tagname+"status-red";
	public final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector"+tagname+"status-blue";
	public final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector"+tagname+"status-grey";

	// Static Attribute
	private final String strLabel				= ".label";
	private final String strValueTable			= ":dal.valueTable";
	private final String strHmiOrder			= ".hmiOrder";
	
	// Dynamic Attribute
	private final String strValue				= ".value";
	private final String strValidity			= ".validity"; // 0=invalid, 1=valid
	private final String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
	private final String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

	// Static Attribute List
	private String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};

	// Dynamic Attribute List
	private String dynamicAttibutes [] = new String[] {strValue, strValidity, strValueAlarmVector, strForcedStatus};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses, String period) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
	}

	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		
		
		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		
		logger.log(Level.FINE, "disconnect End");
	}
	
	private void updatePager() {
		
		logger.log(Level.FINE, "updatePager Begin");
		
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
		
		logger.log(Level.FINE, "updatePager End");
	}
	
	private void onButton(Button btn) {
		
		logger.log(Level.FINE, "onButton Begin");
		
		if (  btn == btnUp || btn == btnDown ) {
			if ( btn == btnUp) {
				--pageIndex;
			} else if ( btn == btnDown ) {
				++pageIndex;
			}
			updatePager();
			updateValue(true);
		}
		
		logger.log(Level.FINE, "onButton End");
	}
	
	@Override
	public void buildWidgets() {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		buildWidgets(this.addresses.length);
	
		logger.log(Level.FINE, "buildWidgets End");
	}
	
	private int pageIndex = 0;
	private PageCounter pageCounter = null;
	void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		logger.log(Level.FINE, "buildWidgets numOfWidgets["+numOfWidgets+"]");
		
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
					
					logger.log(Level.FINE, "buildWidgets i["+i+"]");
						
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
				logger.log(Level.FINE, "buildWidgets this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes);
			
		} else {
			logger.log(Level.FINE, "buildWidgets points IS NULL");
		}
		
		logger.log(Level.FINE, "buildWidgets End");
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
		
		pageCounter.calc(pageIndex);
		
		int rowBegin	= pageCounter.pageRowBegin;
		int rowEnd		= pageCounter.pageRowEnd;
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				logger.log(Level.FINE, "updateValue Begin");
				logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
				
				String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "_" + "inspectorinfo" + "_" + "static" + "_" + parent;
				
				logger.log(Level.FINE, "updateValue clientKey_multiReadValue_inspectorinfo_static["+clientKey_multiReadValue_inspectorinfo_static+"]");
				
				if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {
					
					for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
						String address = this.addresses[x];
						String dbaddress = address + strLabel;
						if ( dbvalues.containsKey(dbaddress) ) {
							String value = dbvalues.get(dbaddress);
							value = RTDB_Helper.removeDBStringWrapper(value);
							lblAttibuteLabel[y].setText(value);
						}
					}
				}
				
			}//End of for keyAndValuesStatic
		}
		
		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
			
			logger.log(Level.FINE, "updateValue Begin");
			logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
			
			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
				String address = this.addresses[x];
				
				logger.log(Level.FINE, "updateValue address["+address+"]");
				
				String value = null;
				{
					String dbaddress = address + strValue;
					logger.log(Level.FINE, "updateValue strValue["+strValue+"] dbaddress["+dbaddress+"]");
					if ( dbvalues.containsKey(dbaddress) ) {
						value = dbvalues.get(dbaddress);
					} else {
						logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
					}
				}		
				
				logger.log(Level.FINE, "updateValue value["+value+"]");
				
				String valueTable = null;
				{
					String dbaddress = address + strValueTable;
					logger.log(Level.FINE, "updateValue strValueTable["+strValueTable+"] dbaddress["+dbaddress+"]");
					if ( dbvalues.containsKey(dbaddress) ) {
						valueTable = dbvalues.get(dbaddress);
					} else {
						logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
					}					
				}
				
				logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
				
				String name = null;
				{
					for( int r = 0 ; r < 12 ; ++r ) {
						String v = RTDB_Helper.getArrayValues(valueTable, 4, r );
						logger.log(Level.FINE, "updateValue getArrayValues r["+r+"] v["+v+"] == valueTable[i]["+valueTable+"]");
						if ( 0 == v.compareTo(value) ) {
							name = RTDB_Helper.getArrayValues(valueTable, 1, r );
							break;
						}
					}					
				}
				
				logger.log(Level.FINE, "updateValue name["+name+"]");
				
				if ( null != name ) {
					name = RTDB_Helper.removeDBStringWrapper(name);
					txtAttributeValue[y].setText(name);	
				} else {
					value = RTDB_Helper.removeDBStringWrapper(value);
					txtAttributeValue[y].setText(value);	
				}
				
				String valueAlarmVector = null;
				String validity = null;
				String forcedStatus = null;
				{
					{
						String dbaddress = address + strValueAlarmVector;
						logger.log(Level.FINE, "updateValue strValueAlarmVector["+strValueAlarmVector+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							valueAlarmVector = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					
					logger.log(Level.FINE, "updateValue valueAlarmVector["+valueAlarmVector+"]");
					
					{
						String dbaddress = address + strValidity;
						logger.log(Level.FINE, "updateValue strValidity["+strValidity+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							validity = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					
					logger.log(Level.FINE, "updateValue validity["+validity+"]");
					
					{
						String dbaddress = address + strForcedStatus;
						logger.log(Level.FINE, "updateValue strForcedStatus["+strForcedStatus+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							forcedStatus = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					
					logger.log(Level.FINE, "updateValue forcedStatus["+forcedStatus+"]");

				}
				
				String strColorCSS = RTDB_Helper.getColorCSS(valueAlarmVector, validity, forcedStatus);
				txtAttibuteColor[y].setStyleName(strColorCSS);
				
				logger.log(Level.FINE, "updateValue strColorCSS["+strColorCSS+"]");

			}
		}//End of for keyAndValuesDynamic
			
		
		logger.log(Level.FINE, "updateValue End");
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttributeValue[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	
	
	private Button btnUp			= null;
	private InlineLabel lblPageNum	= null;
	private Button btnDown			= null;
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
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

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}
	
	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}
}
