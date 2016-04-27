package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.Point;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelLogicDynamicDataEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelLogicStaticDataEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelInspector extends DialogBox implements UIPanelInspector_i {
	
	private static Logger logger = Logger.getLogger(UIPanelInspector.class.getName());
	
	private String strTabNames [] = new String[] {"Info","Control","Tagging","Advance"};

	private String strLabel			= ".label";
	private String strIsControlable	= ".isControlable";
	
	// Static Attribute List
	private String attributesStatic[]	= new String[] {strLabel};

	// Dynamic Attribute List
	private String attributesDynamic[]	= new String[] {strIsControlable};
	
	private UIPanelInspectorRTDBLogic uiPanelRTDBLogin = null;
	
	private String scsEnvId		= null;
	private String dbaddress	= null;

	public void setConnection(String scsEnvId, String dbaddress) {
		logger.log(Level.SEVERE, "setConnection Begin");
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;

		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"] this.dbaddress["+this.dbaddress+"]");
		
		this.uiPanelRTDBLogin = new UIPanelInspectorRTDBLogic();
		this.uiPanelRTDBLogin.setUINameCard(this.uiNameCard);
		this.uiPanelRTDBLogin.setConnection(this.scsEnvId, this.dbaddress);
		this.uiPanelRTDBLogin.setAttibute(UIPanelInspectorRTDBLogic_i.strStatic, attributesStatic);
		this.uiPanelRTDBLogin.setAttibute(UIPanelInspectorRTDBLogic_i.strDynamic, attributesDynamic);

		this.uiPanelRTDBLogin.setUIPanelLogicStaticDataEvent(new UIPanelLogicStaticDataEvent() {
			@Override
			public void ready(LinkedList<Point> points) {
				updateStaticDisplay(points);
			}
		});
		this.uiPanelRTDBLogin.setUIPanelLogicDynamicDataEvent(new UIPanelLogicDynamicDataEvent() {
			@Override
			public void update(LinkedList<Point> points) {
				updateDynamicDisplay(points);
			}
		});
		logger.log(Level.SEVERE, "setConnection End");
	}

	public void setConnections(String scsEnvId, String dbaddress) {
		logger.log(Level.SEVERE, "setConnection Begin");
		
		Iterator<UIPanelInspector_i> iterator = uiInspectorTags.iterator();
		while ( iterator.hasNext() ) {
			UIPanelInspector_i uiPanelInspector_i = iterator.next();
			if ( null != uiPanelInspector_i ) {
				uiPanelInspector_i.setConnection(scsEnvId, dbaddress);
			} else {
				logger.log(Level.SEVERE, "setConnection uiPanelInspector_i IS NULL");
			}
		}
		
		logger.log(Level.SEVERE, "setConnection End");		
	}
	
	@Override
	public void readyToReadChildrenData() {
		logger.log(Level.SEVERE, "readyToReadChildrenData Begin");
		
		logger.log(Level.SEVERE, "readyToReadChildrenData End");
	}
	
	@Override
	public void readyToReadStaticData() {
		logger.log(Level.SEVERE, "readToReadStaticData Begin");
		
		this.uiPanelRTDBLogin.readyToReadStaticData();
		
		logger.log(Level.SEVERE, "readToReadStaticData End");
	}
	
	@Override
	public void readyToSubscribeDynamicData() {
		logger.log(Level.SEVERE, "requestDynmaicData Begin");
		
		this.uiPanelRTDBLogin.readyToSubscribeDynamicData();
		
		logger.log(Level.SEVERE, "requestDynmaicData End");
	}
	
	@Override
	public void removeConnection() {
		logger.log(Level.SEVERE, "removeConnection Begin");
		
		this.uiPanelRTDBLogin.removeDynamicSubscription();
		
		logger.log(Level.SEVERE, "removeConnection End");
	}
	
	private void updateStaticDisplay(LinkedList<Point> points) {
		logger.log(Level.SEVERE, "updateStaticDisplay Begin");
		
		if ( null != points ) {
			String label = null;
			Iterator<Point> iterator = points.iterator();
			while ( iterator.hasNext() ) {
				Point point = iterator.next();
				label = point.getValue(strLabel);
			}
			if ( null != label ) this.setText(label);
			
		} else {
			logger.log(Level.SEVERE, "updateStaticDisplay points IS NULL");
		}
		
		readyToSubscribeDynamicData();
		
		logger.log(Level.SEVERE, "updateStaticDisplay End");
	}
	
	private void updateDynamicDisplay(LinkedList<Point> points) {
		logger.log(Level.SEVERE, "updateDynamicDisplay Begin");
		String controlable = null;
		Iterator<Point> iterator = points.iterator();
		while ( iterator.hasNext() ) {
			Point point = iterator.next();
			controlable = point.getValue(strIsControlable);
		}
		if ( null != controlable ) txtAttributeStatus[0].setText((0==controlable.compareTo("0")?"No":"Yes"));
		logger.log(Level.SEVERE, "updateDynamicDisplay End");
	}
	
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		
		readyToReadStaticData();
		
		logger.log(Level.SEVERE, "connect End");
	}
	
	public void connects() {
		logger.log(Level.SEVERE, "connects Begin");

		Iterator<UIPanelInspector_i> iterator = uiInspectorTags.iterator();
		while ( iterator.hasNext() ) {
			UIPanelInspector_i uiPanelInspector_i = iterator.next();
			if ( null != uiPanelInspector_i ) {
				uiPanelInspector_i.readyToReadChildrenData();
			} else {
				logger.log(Level.SEVERE, "connects uiPanelInspector_i IS NULL");
			}
		}
		logger.log(Level.SEVERE, "connects End");
	}
	
	private void disconnects() {
		logger.log(Level.SEVERE, "tagsDisconnect Begin");
		
		Iterator<UIPanelInspector_i> iterator = uiInspectorTags.iterator();
		while ( iterator.hasNext() ) {
			UIPanelInspector_i uiPanelInspector_i = iterator.next();
			if ( null != uiPanelInspector_i ) {
				uiPanelInspector_i.removeConnection();
			} else {
				logger.log(Level.SEVERE, "tagsConnect uiPanelInspector_i IS NULL");
			}
		}
		logger.log(Level.SEVERE, "tagsDisconnect End");
	}
	
	private UIInspectorInfo uiInspectorInfo			= null;
	private UIInspectorControl uiInspectorControl	= null;
	private UIInspectorTag uiInspectorTag			= null;
	private UIInspectorAdvance uiInspectorAdvance	= null;
	
	private LinkedList<UIPanelInspector_i> uiInspectorTags = null;
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 400, baseHeight = 620;
	private VerticalPanel basePanel = null;
	private TextBox txtAttributeStatus[] = null;
	
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		String strHeadersLabel [] = new String[] { "Control Right","Control Right Reserved","Handover Right" };
		String strHeadersStatus [] = new String[] { "Yes / No","Not Reserved / Not", "Central / Station" };
		
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
	
		uiInspectorInfo		= new UIInspectorInfo();
		uiInspectorControl	= new UIInspectorControl();
		uiInspectorTag		= new UIInspectorTag();
		uiInspectorAdvance	= new UIInspectorAdvance();
		
		uiInspectorTags 	= new LinkedList<UIPanelInspector_i>();
		
		uiInspectorTags.add(uiInspectorInfo);
		uiInspectorTags.add(uiInspectorControl);
		uiInspectorTags.add(uiInspectorTag);
		uiInspectorTags.add(uiInspectorAdvance);
		
		ComplexPanel panelInfo		= uiInspectorInfo.getMainPanel(this.uiNameCard);
		ComplexPanel panelCtrl		= uiInspectorControl.getMainPanel(this.uiNameCard);
		ComplexPanel panelTag		= uiInspectorTag.getMainPanel(this.uiNameCard);
		ComplexPanel panelAdv		= uiInspectorAdvance.getMainPanel(this.uiNameCard);
		
		TabPanel tabPanel = new TabPanel();
		tabPanel.getElement().getStyle().setWidth(400, Unit.PX);
		tabPanel.getElement().getStyle().setFontSize(16, Unit.PX);
		
		tabPanel.add(panelInfo, strTabNames[0]);
		tabPanel.add(panelCtrl, strTabNames[1]);
		tabPanel.add(panelTag, strTabNames[2]);
		tabPanel.add(panelAdv, strTabNames[3]);
		tabPanel.selectTab(0);
		
		Button btnClose = new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-bottom-close");
		
		btnClose.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				disconnects();
				
				hide();
			}
	    });
		
		TextBox txtMsg = new TextBox();
		txtMsg.setReadOnly(true);
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
    
        logger.log(Level.SEVERE, "getMainPanel End");
        
        return basePanel;
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
