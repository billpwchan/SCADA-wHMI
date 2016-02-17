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
import com.google.gwt.user.client.ui.Label;
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
	public static final int LAYOUT_BORDER	= 0;
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";
	
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String IMAGE_PATH	= "imgs";
	
	int baseBoderX = 28, baseBoderY = 28;
	
	int baseWidth = 300, baseHeight = 600;
	
	VerticalPanel basePanel;
	
	private Label lblEquipmentName;
	
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
	
	private String strShortName		= "shortname";
	private String strUNIVNAME		= "UNIVNAME";
	private String strIsControlable	= "isControlable";
	public void createReadRequest () {
		
		logger.log(Level.FINE, "createReadRequest Begin");
		
		wrapperScsRTDBAccess = new WrapperScsRTDBAccess(this);
		
		logger.log(Level.FINE, "createReadRequest End");
		
	}
	public void setReadRequest (int index) {
		
		logger.log(Level.FINE, "setReadRequest Begin");
		logger.log(Level.FINE, "setReadRequest index["+index+"]");
		
		int i = 0;
		
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strShortName
											, strB001
											, strAlias + strDot + strShortName);
		if ( index == i++ )
		wrapperScsRTDBAccess.readValueRequest(
											  strReadValue + strUnderscore + strUNIVNAME
											, strB001
											, strAlias + strDot + strUNIVNAME);
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
//Window.alert("UIPanelInspector setReadResult: ["+key+"] value["+value+"] errorCode["+errorCode+"]");
				
				if ( 0 == key.compareTo(strReadValue + strUnderscore + strShortName) ) {
					lblEquipmentNameType = value;
				}
				else if ( 0 == key.compareTo(strReadValue + strUnderscore + strUNIVNAME) ) {
					lblEquipmentNameID = value;
				}
				else if ( 0 == key.compareTo(strReadValue + strUnderscore + strIsControlable) ) {
					lblEquipmentControlable = value;
				}
			}
		}
		
		lblEquipmentName.setText(" Equipment Type: "+lblEquipmentNameType+" ID: "+lblEquipmentNameID+" ");
		txtAttributeStatus[0].setText((0==lblEquipmentControlable.compareTo("0")?"No":"Yes"));
		
		logger.log(Level.FINE, "setReadResult End");
	}
	private UIInspectorInfo uiInspectorInfo;
	private int timerCounter = 0;
	public void init() {
		logger.log(Level.FINE, "init Begin");
		
		lblEquipmentName = new Label(" Equipment Type: XXXX_XXXX ID: XXXXX_XXXXX ");
		lblEquipmentName.getElement().getStyle().setPadding(10, Unit.PX);	
		
		VerticalPanel verticalPanelEqpName = new VerticalPanel();
		verticalPanelEqpName.add(lblEquipmentName);
		
		String strHeadersLabel [] = new String[] { "Equipment Control Right","Control Right Reserved","Equipment Handover Right" };
		String strHeadersStatus [] = new String[] { "Yes / No","Not Reserved / Not", "Central / Station" };
		
		FlexTable flexTableHeader = new FlexTable();
		flexTableHeader.setWidth("400px");
		txtAttributeStatus = new TextBox[strHeadersStatus.length];
		for ( int i = 0 ; i < strHeadersLabel.length ; i++ ) {
			InlineLabel inlineLabel = new InlineLabel(strHeadersLabel[i]);
			inlineLabel.getElement().getStyle().setPadding(10, Unit.PX);	
			inlineLabel.setWidth("100%");
			flexTableHeader.setWidget(i, 0, inlineLabel);
			txtAttributeStatus[i] = new TextBox();
//			txtAttributeStatus[i].getElement().getStyle().setPadding(10, Unit.PX);	
			txtAttributeStatus[i].setText(strHeadersStatus[i]);
			txtAttributeStatus[i].setMaxLength(16);
			txtAttributeStatus[i].setWidth("100%");
			flexTableHeader.setWidget(i, 2, txtAttributeStatus[i]);
		}
		
		uiInspectorInfo = new UIInspectorInfo();
		Panel panelInfo = uiInspectorInfo.getMainPanel();
		Panel panelCtrl = new UIInspectorControl().getMainPanel();
//		Panel panelTag = new UIInspectorTag().getMainPanel();
//		Panel panelAdv = new UIInspectorAdvance().getMainPanel();
		
		String strTabNames [] = new String[] {"Info","Control","Tagging","Advance"};

		TabPanel tabPanel = new TabPanel();
		tabPanel.getElement().getStyle().setWidth(400, Unit.PX);
		
		tabPanel.add(panelInfo, strTabNames[0]);
		tabPanel.add(panelCtrl, strTabNames[1]);
//		tabPanel.add(panelTag, strTabNames[2]);
//		tabPanel.add(panelAdv, strTabNames[3]);
		tabPanel.selectTab(0);
		
		Button btnClose = new Button("Close");
		btnClose.setWidth("80px");
		btnClose.setHeight("30px");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
	    });
		
		TextBox txtMsg = new TextBox();
		txtMsg.setWidth("300px");
		txtMsg.setHeight("40px");
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setWidth("100%");
		bottomBar.getElement().getStyle().setPadding(10, Unit.PX);	
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.add(txtMsg);
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnClose);
		
		basePanel = new VerticalPanel();
		basePanel.setBorderWidth(LAYOUT_BORDER);
		basePanel.add(verticalPanelEqpName);
		basePanel.add(flexTableHeader);
		basePanel.add(tabPanel);
		basePanel.add(bottomBar);
		basePanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		basePanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		basePanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);
		this.add(basePanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (baseBoderX / 2);
        int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);

        this.setPopupPosition(left, top);
        
        this.createReadRequest();
        
        uiInspectorInfo.createReadRequest();
        uiInspectorInfo.initVariable();
        
        Timer t = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				executeTimer(timerCounter);
				
				timerCounter++;
				
				if ( timerCounter > 3 + ((1+2+3)*4) ) this.cancel();
			}
        	
        };
        
        t.scheduleRepeating(200);//200
        
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
