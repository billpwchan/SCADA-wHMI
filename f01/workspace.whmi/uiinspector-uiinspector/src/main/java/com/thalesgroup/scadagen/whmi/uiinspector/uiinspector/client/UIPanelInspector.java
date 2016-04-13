package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorInfo;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccessEvent;

public class UIPanelInspector extends DialogBox implements WrapperScsRTDBAccessEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelInspector.class.getName());
	
	public static final String UNIT_PX		= "px";
	
	public static final String IMAGE_PATH	= "imgs";
	
	int baseBoderX = 28, baseBoderY = 28;
	
	int baseWidth = 400, baseHeight = 620;
	
	VerticalPanel basePanel;
	
//	private Label lblEquipmentName;
//	private Label lblEquipmentAlias;
	
	private TextBox txtAttributeStatus[];

	public UIPanelInspector() {
		logger.log(Level.FINE, "UIPanelInspector Begin");
		init ();
		logger.log(Level.FINE, "UIPanelInspector End");
	}
	
	private String strDot = ".";
	private String strUnderscore = "_";	
	private String strReadValue = "ReadValue";
	
	private String strB001 = "B001";

	private String strAlias = ":SITE1:B001:F001:ACCESS:DO001";
	
	private WrapperScsRTDBAccess wrapperScsRTDBAccess;
	
	private String strLabel			= "label";
	private String strName			= "name";
	private String strIsControlable	= "isControlable";
	public void createReadRequest () {
		
		logger.log(Level.FINE, "createReadRequest Begin");
		
		wrapperScsRTDBAccess = new WrapperScsRTDBAccess(this);
		
		logger.log(Level.FINE, "createReadRequest End");
		
	}
	
	public void setReadRequestCache () {
		
		logger.log(Level.FINE, "setReadRequestCache Begin");
		
		String dbaddresses = "";
		String [] values = new String[1];
		
		dbaddresses = strAlias + strDot + strLabel;
		values[0] = "Door B01DO001";
		wrapperScsRTDBAccess.cachePut(dbaddresses, values);
				
		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strLabel
											, strB001
											, dbaddresses);
		
		dbaddresses = strAlias + strDot + strName;
		values[0] = ":SITE1:B001:F001:ACCESS:DO001";
		wrapperScsRTDBAccess.cachePut(dbaddresses, values);

		wrapperScsRTDBAccess.readValueRequestCache(
											  strReadValue + strUnderscore + strName
											, strB001
											, dbaddresses);
		
		logger.log(Level.FINE, "setReadRequestCache End");
	}
	
	public void setReadRequest (int index) {
		
		logger.log(Level.FINE, "setReadRequest Begin");
		logger.log(Level.FINE, "setReadRequest index["+index+"]");
		
		int i = 0;
		
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strLabel
//											, strB001
//											, strAlias + strDot + strLabel);
//		if ( index == i++ )
//		wrapperScsRTDBAccess.readValueRequest(
//											  strReadValue + strUnderscore + strName
//											, strB001
//											, strAlias + strDot + strName);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											   strReadValue + strUnderscore + strIsControlable
											, strB001
											, strAlias + strDot + strIsControlable);
		logger.log(Level.FINE, "setReadRequest End");
	}
	
	private String lblEquipmentNameType="", lblEquipmentNameID="", lblEquipmentControlable="";
	@Override
	public void setReadResult(String key, String values[], int errorCode, String errorMessage) {
		logger.log(Level.FINE, "setReadResult Begin");
		if ( null != values && values.length > 0 ) {
			for ( String value: values) {
				
				if ( 0 == key.compareTo(strReadValue + strUnderscore + strLabel) ) {
					lblEquipmentNameType = value;
				}
				else if ( 0 == key.compareTo(strReadValue + strUnderscore + strName) ) {
					lblEquipmentNameID = value;
				}
				else if ( 0 == key.compareTo(strReadValue + strUnderscore + strIsControlable) ) {
					lblEquipmentControlable = value;
				}
			}
		}
		
		this.setText(lblEquipmentNameType);
		
//		lblEquipmentName.setText(" "+lblEquipmentNameType + " ");
//		lblEquipmentAlias.setText(" "+lblEquipmentNameID + " ");
		
		txtAttributeStatus[0].setText((0==lblEquipmentControlable.compareTo("0")?"No":"Yes"));
		
		logger.log(Level.FINE, "setReadResult End");
	}
	private UIInspectorInfo uiInspectorInfo;
	private int timerCounter = 0;
	public void init() {
		logger.log(Level.FINE, "init Begin");
		
//		lblEquipmentName = new Label(" - ");
//		lblEquipmentName.addStyleName("project-gwt-inlinelabel-equipmentname");
		
//		lblEquipmentAlias = new Label(" XXXXX_XXXXX ");
//		lblEquipmentAlias.addStyleName("project-gwt-inlinelabel-equipmentalias");
		
//		VerticalPanel verticalPanelTitle = new VerticalPanel();
//		verticalPanelTitle.addStyleName("project-gwt-panel-title");
//		verticalPanelTitle.add(lblEquipmentName);
//		verticalPanelEqpName.add(lblEquipmentAlias);
		
		String strHeadersLabel [] = new String[] { "Control Right","Control Right Reserved","Handover Right" };
		String strHeadersStatus [] = new String[] { "Yes / No","Not Reserved / Not", "Central / Station" };
		
		FlexTable flexTableHeader = new FlexTable();
//		flexTableHeader.setWidth("400px");
		flexTableHeader.addStyleName("project-gwt-flextable-header");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			InlineLabel inlineLabel = new InlineLabel(strHeadersLabel[i]);
			inlineLabel.getElement().getStyle().setPadding(10, Unit.PX);	
//			inlineLabel.setWidth("100%");
			inlineLabel.addStyleName("project-gwt-inlinelabel-headerlabel");
			flexTableHeader.setWidget(i, 0, inlineLabel);
			txtAttributeStatus[i] = new TextBox();
//			txtAttributeStatus[i].getElement().getStyle().setPadding(10, Unit.PX);	
			txtAttributeStatus[i].setText(strHeadersStatus[i]);
			txtAttributeStatus[i].setMaxLength(16);
			txtAttributeStatus[i].setReadOnly(true);
//			txtAttributeStatus[i].setWidth("100%");
			txtAttributeStatus[i].addStyleName("project-gwt-textbox-headervalue");
			flexTableHeader.setWidget(i, 2, txtAttributeStatus[i]);
		}
		
		uiInspectorInfo = new UIInspectorInfo();
		Panel panelInfo = uiInspectorInfo.getMainPanel();
		Panel panelCtrl = new UIInspectorControl().getMainPanel();
		Panel panelTag = new UIInspectorTag().getMainPanel();
		Panel panelAdv = new UIInspectorAdvance().getMainPanel();
		
		String strTabNames [] = new String[] {"Info","Control","Tagging","Advance"};

		TabPanel tabPanel = new TabPanel();
		tabPanel.getElement().getStyle().setWidth(400, Unit.PX);
		tabPanel.getElement().getStyle().setFontSize(16, Unit.PX);
		
		tabPanel.add(panelInfo, strTabNames[0]);
		tabPanel.add(panelCtrl, strTabNames[1]);
		tabPanel.add(panelTag, strTabNames[2]);
		tabPanel.add(panelAdv, strTabNames[3]);
		tabPanel.selectTab(0);
		
		Button btnClose = new Button("Close");
//		btnClose.setWidth("80px");
//		btnClose.setHeight("30px");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
	    });
		
		TextBox txtMsg = new TextBox();
		txtMsg.setReadOnly(true);
//		txtMsg.setWidth("300px");
//		txtMsg.setHeight("40px");
		txtMsg.addStyleName("project-gwt-textbox-inspector-bottom-message");
		
		HorizontalPanel bottomBar = new HorizontalPanel();
//		bottomBar.setWidth("100%");
		bottomBar.addStyleName("project-gwt-panel-inspector-bottom");
		bottomBar.getElement().getStyle().setPadding(10, Unit.PX);	
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.add(txtMsg);
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnClose);
		
		basePanel = new VerticalPanel();
//		basePanel.add(verticalPanelTitle);
		basePanel.add(flexTableHeader);
		basePanel.add(tabPanel);
		basePanel.add(bottomBar);
		basePanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		basePanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		this.add(basePanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (baseBoderX / 2);
        int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);

        this.setPopupPosition(left, top);
        
        this.createReadRequest();
        
        uiInspectorInfo.createReadRequest();
        uiInspectorInfo.initVariable();
        
        setReadRequestCache();
        uiInspectorInfo.setReadRequestCache();
        
        Timer t = new Timer() {

			@Override
			public void run() {
				
				executeTimer(timerCounter);
				
				timerCounter++;
				
				if ( timerCounter > 1/*+2*/ + ((/*1+*/2+3)*4) ) this.cancel();
			}
        	
        };
        
        t.scheduleRepeating(100);//200
        
        logger.log(Level.FINE, "init End");
        
	}
	private void executeTimer (int timerCounter) {
		
		logger.log(Level.FINE, "executeTimer Begin");
		
		logger.log(Level.FINE, "executeTimer timerCounter["+timerCounter+"]");
		
		this.setReadRequest(timerCounter);
				
		uiInspectorInfo.setReadRequest(timerCounter);
		
		logger.log(Level.FINE, "executeTimer End");
	}
	
	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
		if ( basePanel != null ) {
			boolean XtoMoveInvalid = false, YtoMoveInvalid = false;
			int XtoMove = 0, YtoMove = 0;
			if ( this.getPopupLeft() < 0 ) {
				XtoMove = 0;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupLeft() + baseWidth + baseBoderX > Window.getClientWidth() ) {
				XtoMove = Window.getClientWidth() - baseWidth - baseBoderX;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupTop() < 0 ) {
				YtoMove = 0;
				YtoMoveInvalid = true;
			}
			if ( this.getPopupTop() + baseHeight + baseBoderY > Window.getClientHeight() ) {
				YtoMove = Window.getClientHeight() - baseHeight - baseBoderY;
				YtoMoveInvalid = true;
			}
			if ( XtoMoveInvalid || YtoMoveInvalid ) {
				if ( ! XtoMoveInvalid ) XtoMove = this.getPopupLeft();
				if ( ! YtoMoveInvalid ) YtoMove = this.getPopupTop();
				this.setPopupPosition(XtoMove, YtoMove);
			}
		}
	}


}
