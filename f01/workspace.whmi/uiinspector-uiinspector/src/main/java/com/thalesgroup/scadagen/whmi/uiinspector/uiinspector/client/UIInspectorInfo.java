package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
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
	
	@Override
	public void buildWidgets() {
		logger.log(Level.FINE, "buildWidgets Begin");
		
		buildWidgets(this.addresses.length);
	
		logger.log(Level.FINE, "buildWidgets End");
	}
	
	void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.FINE, "buildWidgets Begin");
		
		logger.log(Level.FINE, "buildWidgets numOfWidgets["+numOfWidgets+"]");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
//			UIInspectorInfoPanel info = new UIInspectorInfoPanel();
//			ComplexPanel infoPanel = info.getMainPanel(this.uiNameCard);
//			
//			vpCtrls.add(infoPanel);
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				txtAttribute = new TextBox[numOfWidgets];
				lblAttibuteLabel = new InlineLabel[numOfWidgets];
				txtAttibuteColor = new InlineLabel[numOfWidgets];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				for( int i = 0 ; i < numOfWidgets ; ++i ) {
					
					logger.log(Level.FINE, "buildWidgets i["+i+"]");
						
					lblAttibuteLabel[i] = new InlineLabel();
					lblAttibuteLabel[i].setWidth("100%");
					lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-label");
					lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
					flexTableAttibutes.setWidget(i+1, 0, lblAttibuteLabel[i]);
					txtAttribute[i] = new TextBox();
					txtAttribute[i].setWidth("95%");
					txtAttribute[i].setText("ATTRIBUTE_STATUS_"+(i+1));
					txtAttribute[i].addStyleName("project-gwt-textbox-inspector-"+tagname+"-value");
					txtAttribute[i].setReadOnly(true);
					txtAttribute[i].setMaxLength(16);
					flexTableAttibutes.setWidget(i+1, 1, txtAttribute[i]);
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
	
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		
		logger.log(Level.FINE, "updateValue Begin");
		logger.log(Level.FINE, "updateValue clientkey["+clientKey+"]");
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
		
		for ( int i = 0 ; i < this.addresses.length ; ++i ) {
			logger.log(Level.FINE, "updateValue addresses("+i+")["+addresses[i]+"]");
		}
		
		for ( String key : dbvalues.keySet() ) {
			logger.log(Level.FINE, "updateValue dbvalues.get("+key+")["+dbvalues.get(key)+"]");
		}

		String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "inspectorinfo" + "static" + parent;
		
		logger.log(Level.FINE, "updateValue clientKey_multiReadValue_inspectorinfo_static["+clientKey_multiReadValue_inspectorinfo_static+"]");
		
		if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {

			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				String address = this.addresses[i];
				String dbaddress = address + strLabel;
				if ( dbvalues.containsKey(dbaddress) ) {
					String value = dbvalues.get(dbaddress);
					value = RTDB_Helper.removeDBStringWrapper(value);
					lblAttibuteLabel[i].setText(value);
				}
			}
		} else {
			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				String address = this.addresses[i];
				
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
					txtAttribute[i].setText(name);	
				} else {
					value = RTDB_Helper.removeDBStringWrapper(value);
					txtAttribute[i].setText(value);	
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
				txtAttibuteColor[i].setStyleName(strColorCSS);
				
				logger.log(Level.FINE, "updateValue strColorCSS["+strColorCSS+"]");

			}
		}

		logger.log(Level.FINE, "updateValue End");
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttribute[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		Button btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-"+tagname+"-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-inspector-"+tagname+"-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-"+tagname+"-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
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
