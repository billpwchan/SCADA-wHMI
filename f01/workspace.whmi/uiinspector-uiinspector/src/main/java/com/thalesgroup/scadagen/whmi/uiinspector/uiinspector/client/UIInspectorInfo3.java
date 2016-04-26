package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ChildrenResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.SubscriptionResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess3;

public class UIInspectorInfo3 {
	
	private static Logger logger = Logger.getLogger(UIInspectorInfo3.class.getName());
	
	public static final String strCSSStatusGreen	= "project-gwt-inlinelabel-inspector-info-status-green";
	public static final String strCSSStatusRed		= "project-gwt-inlinelabel-inspector-info-status-red";
	public static final String strCSSStatusBlue		= "project-gwt-inlinelabel-inspector-info-status-blue";
	public static final String strCSSStatusGrey		= "project-gwt-inlinelabel-inspector-info-status-grey";
	
	private String strUnderscore		= "_";
	
	// Static Attribute
	private String strLabel				= ".label";
	private String strValueTable		= ":dal.valueTable";
	
	// Dynamic Attribute
	private String strValue				= ".value";
	private String strValidity			= ".validity"; // 0=invalid, 1=valid
	private String strValueAlarmVector	= ":dal.valueAlarmVector"; // (0,1)==0 = normal, (0,1)==1 = alarm 
	private String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus
	
	private WrapperScsRTDBAccess3 wrapperScsRTDBAccess3 = null;
	
	private int numOfPoints = 0;
	
	// 1 Get DBAddress Children
	// 2 Get DBAddress Static Data
	// 3 Get DBAddress Subscription Data
	
	private String scsEnvId = null;
	private String dbaddress = null;	
	public UIInspectorInfo3(int numOfPoints) {
		this.numOfPoints = numOfPoints;
	}
	public void init(String scsEnvId, String dbaddress) {
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;
		this.wrapperScsRTDBAccess3 = WrapperScsRTDBAccess3.getInstance();
	}
	public void readyToReadStaticData() {
		requestChildrenData();
	}
	public void readyToSubscribeDynamicData() {
		prepareDynmaicRequest();
		requestDynmaicData();
	}
	
	// Equipment List
	private LinkedHashMap<String, String[]> equpmentDBAddresses = null;
	
	// Attribute (Static)
	private String attributesStatic [] = new String[] {strLabel, strValueTable};
//	private LinkedHashMap<String, String[]>	attributeStaticDBAddresses = null;
	private LinkedList<String> attributeStaticDBAddressesToRead = null;
	private HashMap<String, String> attributesStaticValues = null;

	// Attribute (Dynamic)	
	private String attributesDynamic [] = new String[] { strValue, strValidity, strValueAlarmVector, strForcedStatus};
//	private LinkedHashMap<String, String[]> attributesDynamicDBAddress = null;
	private LinkedList<String> pointsDBAddressesDynamicToRead = null;
	private HashMap<String, String> attributesDynamicValues = null;
	
	private void requestChildrenData() {
		logger.log(Level.SEVERE, "requestChildrenData Begin");
		String clientkey = "getChildren" + strUnderscore + this.dbaddress;
		wrapperScsRTDBAccess3.getChildren(clientkey, this.scsEnvId, this.dbaddress, new ChildrenResult() {
			@Override
			public void setGetChildrenResult(String clientKey, String[] instances, int errorCode, String errorMessage) {
				processChildrenResult(clientKey, instances, errorCode, errorMessage);
				prepareStaticRequest();
				requestStaticData();
			}
		});
		logger.log(Level.SEVERE, "requestChildrenData End");
	}
	
	private void processChildrenResult(String clientkey, String[] instances, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processChildrenResult Begin");
		if ( null != instances ) {
			this.equpmentDBAddresses = new LinkedHashMap<String, String[]>();
			this.equpmentDBAddresses.put(this.dbaddress, instances);
			for ( int i = 0 ; i < instances.length ; ++i ) {
				logger.log(Level.SEVERE, "processChildrenResult this.dbaddress["+this.dbaddress+"], instances["+instances[i]+"]");
			}
		} else {
			logger.log(Level.SEVERE, "processChildrenResult instances IS NULL");
		}
		logger.log(Level.SEVERE, "processChildrenResult End");
	}
	
	private void prepareStaticRequest() {
		logger.log(Level.SEVERE, "prepareStaticRequest Begin");
		this.attributeStaticDBAddressesToRead = new LinkedList<String>();
//		this.attributeStaticDBAddresses = new LinkedHashMap<String, String[]>();
//		LinkedList<String> pointsAddress = new LinkedList<String>();
		if ( null != equpmentDBAddresses ) {
			Iterator<String> iterator = this.equpmentDBAddresses.keySet().iterator();
			while(iterator.hasNext()) {
				String dbaddress = iterator.next();
				String[] points = equpmentDBAddresses.get(dbaddress);
				for ( int x = 0 ; x < points.length ; ++x ) {
					for ( int y = 0 ; y < attributesStatic.length ; ++y ) {
//						pointsAddress.add(points[x]+attributesStatic[y]);
						attributeStaticDBAddressesToRead.add(points[x]+attributesStatic[y]);	
						
						
						logger.log(Level.SEVERE, "prepareStaticRequest points[x]["+points[x]+"]+attributesStatic[y]["+attributesStatic[y]+"] => ["+points[x]+attributesStatic[y]+"]");
					}
//					this.attributeStaticDBAddresses.put(dbaddress, pointsAddress.toArray(new String[0]));
				}
			}
		} else {
			logger.log(Level.SEVERE, "prepareStaticRequest equpmentDBAddresses IS NULL");
		}
		logger.log(Level.SEVERE, "prepareStaticRequest End");
	}
	
	private void requestStaticData() {
		logger.log(Level.SEVERE, "requestStaticData Begin");
		String clientkey = "multiReadValueRequestCaches" + strUnderscore + this.dbaddress;
		wrapperScsRTDBAccess3.multiReadValueRequestCaches(clientkey, this.scsEnvId, attributeStaticDBAddressesToRead.toArray(new String[0]), ReadResult.class.getName(), new ReadResult() {
			@Override
			public void setReadResult(String clientKey, String[][] values, int errorCode, String errorMessage) {
				processStaticData(clientKey, values, errorCode, errorMessage);
				readyToSubscribeDynamicData();
			}
		});
		logger.log(Level.SEVERE, "requestStaticData End");
	}
	
	private void processStaticData(String clientKey, String[][] values, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processStaticData Begin");
		attributesStaticValues = new HashMap<String, String>();
		if ( null != values && values.length > 0 ) {
			for ( int x = 0 ; x < values.length ; ++x ) {
				for ( int y = 0 ; y < values[x].length ; ++y ) {
					attributesStaticValues.put(values[x][0], values[x][1]);
					
					logger.log(Level.SEVERE, "processStaticData values[x][0]["+values[x][0]+"] values[x][0]["+values[x][1]+"]");
				}
			}
		}
		logger.log(Level.SEVERE, "processStaticData End");
	}
	
	private void prepareDynmaicRequest() {
		logger.log(Level.SEVERE, "prepareDynmaicRequest End");
		this.pointsDBAddressesDynamicToRead = new LinkedList<String>();
//		this.attributesDynamicDBAddress = new LinkedHashMap<String, String[]>();
//		LinkedList<String> pointsAddress = new LinkedList<String>();
		if ( null != equpmentDBAddresses ) {
			Iterator<String> iterator = this.equpmentDBAddresses.keySet().iterator();
			while(iterator.hasNext()) {
				String dbaddress = iterator.next();
				String[] points = equpmentDBAddresses.get(dbaddress);
				for ( int x = 0 ; x < points.length ; ++x ) {
					for ( int y = 0 ; y < attributesDynamic.length ; ++y ) {
//						pointsAddress.add(points[x]+attributesDynamic[y]);
						pointsDBAddressesDynamicToRead.add(points[x]+attributesDynamic[y]);
						
						logger.log(Level.SEVERE, "prepareDynmaicRequest points[x]["+points[x]+"]+attributesDynamic[y]["+attributesDynamic[y]+"] => ["+points[x]+attributesDynamic[y]+"]");
					}
//					this.attributesDynamicDBAddress.put(dbaddress, pointsAddress.toArray(new String[0]));
				}
			}
		} else {
			logger.log(Level.SEVERE, "prepareDynmaicRequest equpmentDBAddresses IS NULL");
		}
		logger.log(Level.SEVERE, "prepareDynmaicRequest End");
	}
	
	private void requestDynmaicData() {
		logger.log(Level.SEVERE, "requestDynmaicData Begin");
		String clientkey = "subscriptionRequest" + "_" + this.dbaddress;
		wrapperScsRTDBAccess3.subscriptionRequest(clientkey, this.scsEnvId, pointsDBAddressesDynamicToRead.toArray(new String[0]), new SubscriptionResult() {
			@Override
			public void setReadResultSubscription(String clientKey, String[][] values, int errorCode, String errorMessage) {
				processDynamicData(clientKey, values, errorCode, errorMessage);
				updateData();
			}
		});
		logger.log(Level.SEVERE, "requestDynmaicData End");
	}
	
	private void processDynamicData(String clientKey, String[][] values, int errorCode, String errorMessage) {
		logger.log(Level.SEVERE, "processDynamicData Begin");
		attributesDynamicValues = new HashMap<String, String>();
		if ( null != values ) {
			for ( int x = 0 ; x < values.length ; ++x ) {
				if ( 2 == values[x].length ) {
					attributesDynamicValues.put(values[x][0], values[x][1]);
					logger.log(Level.SEVERE, "processDynamicData values["+x+"][0]["+values[x][0]+"] values["+x+"][1]["+values[x][1]+"]");
				} else {				
					for ( int y = 0 ; y < values[x].length ; ++y ) {
						logger.log(Level.SEVERE, "processDynamicData values["+x+"]["+y+"]["+values[x][y]+"]");
					}
				}
			}
		}
		logger.log(Level.SEVERE, "processDynamicData End");		
	}
	
	private void updateData() {
		
	}

	
	private String getArrayValues(String string, int x, int y) {
		String str = null;
		
		
		logger.log(Level.FINE, "getArrayValues Begin");

		logger.log(Level.FINE, "getArrayValues string["+string+"]");
		logger.log(Level.FINE, "getArrayValues x["+x+"] y["+y+"]");
				
		if (null != string && string.length() > 0) {
			if (string.charAt(0) == '[')
				string = string.substring(1);
			if (string.charAt(string.length() - 1) == ']')
				string = string.substring(0, string.length() - 1);
			String[] strs = string.split("\\],\\[");
			if (strs.length > 0 && x < strs.length) {
				String s = strs[x];
				
				logger.log(Level.FINE, "getArrayValues s["+s+"]");
				
				//for (String s : strs) {
					//System.out.println("s [" + s + "]");
					String str2s[] = s.split(",");
					if ( str2s.length > 0 && y < str2s.length ) {
						str = str2s[y];
						
						logger.log(Level.FINE, "getArrayValues str["+str+"]");
						
						//for (String s2 : str2s) {
						//	System.out.println("s2 [" + s2 + "]");
						//}						
					}

				//}
			} else {
				// Invalid str length or index
			}
		}
		
		logger.log(Level.FINE, "getArrayValues End");

		
		return str;
	}
	
	private String getColorCSS(String alarmVector, String validity, String forcedStatus) {
		
		logger.log(Level.FINE, "getColorCSS Begin");
		
		String colorCSS	= strCSSStatusGrey;
		int intAlarmVector	= 0;
		int intValidity		= 0;
		int intForcedStatus	= 0;
		try {
			intAlarmVector = Integer.parseInt(alarmVector.split(",")[1]);
			intValidity = Integer.parseInt(validity);
			intForcedStatus = Integer.parseInt(forcedStatus);
		} catch ( NumberFormatException e ) {
			logger.log(Level.FINE, "getColorCSS NumberFormatException["+e.toString()+"]");
		}
		
		int intMO = 2, intAI = 8, intSS = 512;
		
		// 2=MO, AI=8, 512=SS
		if ( (intForcedStatus & intMO) == intMO || (intForcedStatus & intAI) == intAI || (intForcedStatus & intSS) == intSS ) {
			colorCSS = strCSSStatusBlue;
			
		// 0=invalid, 1=valid	
		} else if ( intValidity == 0 ) {
			colorCSS = strCSSStatusGrey;
			
		// 0=normal, 1=alarm
		} else if ( intAlarmVector == 1 ) {
			colorCSS = strCSSStatusRed;
			
		// Grey
		} else {
			colorCSS = strCSSStatusGreen;
		}
		
		logger.log(Level.FINE, "getColorCSS colorCode["+colorCSS+"]");
		
		logger.log(Level.FINE, "getColorCSS End");

		return colorCSS;
	}
	
	FlexTable flexTableAttibutes = null;

	private TextBox txtAttribute[];
	private InlineLabel lblAttibuteLabel[];
	private InlineLabel txtAttibuteColor[];
	public Panel getMainPanel() {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		flexTableAttibutes = new FlexTable();
		
//		int numOfPoints = 8;
		
		txtAttribute = new TextBox[numOfPoints];
		lblAttibuteLabel = new InlineLabel[numOfPoints];
		txtAttibuteColor = new InlineLabel[numOfPoints];
		
		FlexTable flexTableAttibutes = new FlexTable();
		flexTableAttibutes.setWidth("100%");
		for ( int i = 0; i < numOfPoints ; i ++ ) {
			
			lblAttibuteLabel[i] = new InlineLabel();
			lblAttibuteLabel[i].setWidth("100%");
			lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-info-label");
			lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
			flexTableAttibutes.setWidget(i+1, 0, lblAttibuteLabel[i]);
			txtAttribute[i] = new TextBox();
			txtAttribute[i].setWidth("95%");
			txtAttribute[i].setText("ATTRIBUTE_STATUS_"+(i+1));
			txtAttribute[i].addStyleName("project-gwt-textbox-inspector-info-value");
			txtAttribute[i].setReadOnly(true);
			txtAttribute[i].setMaxLength(16);
			flexTableAttibutes.setWidget(i+1, 1, txtAttribute[i]);
			txtAttibuteColor[i] = new InlineLabel();
			txtAttibuteColor[i].setText("R");
			txtAttibuteColor[i].setStyleName(strCSSStatusGrey);
			flexTableAttibutes.setWidget(i+1, 2, txtAttibuteColor[i]);
		}
		
		flexTableAttibutes.getColumnFormatter().addStyleName(0, "project-gwt-flextable-inspector-info-label-col");
		flexTableAttibutes.getColumnFormatter().addStyleName(1, "project-gwt-flextable-inspector-info-value-col");
		flexTableAttibutes.getColumnFormatter().addStyleName(2, "project-gwt-flextable-inspector-info-status-col");
		
		Button btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-down");
		btnDown.setText("▼");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});	
		
		Button btnAckCurPage = new Button();
		btnAckCurPage.addStyleName("project-gwt-button-inspector-info-ackpage");
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
		bottomBar.addStyleName("project-gwt-panel-inspector-info-bottom");
		
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
		basePanel.add(flexTableAttibutes);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.FINE, "getMainPanel End");
		
		return vp;
	}
}
