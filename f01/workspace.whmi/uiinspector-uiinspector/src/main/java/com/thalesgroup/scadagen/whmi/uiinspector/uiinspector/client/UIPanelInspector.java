package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogic;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicChildEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicDynamicEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicHeader;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspectorRTDBLogicStaticEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.storage.Point;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelInspector extends DialogBox implements UIInspectorTab_i, UIInspector_i {
	
	private static Logger logger = Logger.getLogger(UIPanelInspector.class.getName());
	
	private String strTabNames [] = new String[] {"Info","Control","Tagging","Advance"};

	private String strLabel			= ".label";
	private String strIsControlable	= ".isControlable";
	
	// Static Attribute List
	private String staticAttibutes[]	= new String[] {strLabel};

	// Dynamic Attribute List
	private String dynamicAttibutes[]	= new String[] {strIsControlable};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.SEVERE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		RTDB_Helper.addressesIsValid(this.addresses);
		
		logger.log(Level.SEVERE, "setAddresses End");
	}
	
	private UIPanelInspectorRTDBLogic logic 			= null;
	private UIPanelInspectorRTDBLogicHeader headerLogic	= null;
	@Override
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		
		if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
			
			String address = this.addresses[0];
			
			{
				if ( null != headerLogic ) headerLogic.disconnect();
				
				headerLogic = new UIPanelInspectorRTDBLogicHeader(this.uiNameCard);
				headerLogic.setScsEnvId(scsEnvId);
				headerLogic.setDBaddress(address);
				headerLogic.setUIPanelInspectorRTDBLogicChildEvent(new UIPanelInspectorRTDBLogicChildEvent() {
					@Override
					public void ready(String[] instances) {
//						buildTabsAddress(scsEnvId, instances);
//						makeTabsSetAddress();
//						makeTabsBuildWidgets();
//						makeTabsConnect();
					}
				});
				if ( null != headerLogic ) headerLogic.connect();
			}
			
			{
				if ( null != logic ) logic.disconnect();
				
				logic = new UIPanelInspectorRTDBLogic(this.uiNameCard);
				
				logic.setScsEnvId(scsEnvId);
				logic.setParent(parent);
				logic.setDBaddresses(this.addresses);
				logic.setStaticAttributes(staticAttibutes);
//				logic.setDynamicAttributes(dynamicAttibutes);
				logic.setUIPanelInspectorRTDBLogicStaticEvent(new UIPanelInspectorRTDBLogicStaticEvent() {
					@Override
					public void ready(Point[] points) {
						updateStaticDisplay(points);
					}
				});
				logic.setUIPanelInspectorRTDBLogicDynamicEventt(new UIPanelInspectorRTDBLogicDynamicEvent() {
					@Override
					public void update(Point[] points) {
						updateDynamicDisplay(points);
					}
				});
				
				
				if ( null != logic ) logic.connect();				
			}
			
		} else {
			logger.log(Level.SEVERE, "connect addressIsValid IS FALSE");
		}

		logger.log(Level.SEVERE, "connect End");
	}
	
	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}
	
	public void buildWidgets(int numOfWidgets) {
	
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.SEVERE, "removeConnection Begin");
		
		if ( null != logic ) logic.disconnect();
		if ( null != headerLogic ) headerLogic.disconnect();
		
		logger.log(Level.SEVERE, "removeConnection End");
	}
	
	private void updateStaticDisplay(Point[] points) {
		logger.log(Level.SEVERE, "updateStaticDisplay Begin");
		
		if ( null != points ) {
			String label = null;
			for(Point point : points ) {
				label = point.getValue(strLabel);
			}
			if ( null != label ) this.setText(label);
			
		} else {
			logger.log(Level.SEVERE, "updateStaticDisplay points IS NULL");
		}
		
		logger.log(Level.SEVERE, "updateStaticDisplay End");
	}
	
	private void updateDynamicDisplay(Point[] points) {
		logger.log(Level.SEVERE, "updateDynamicDisplay Begin");
		
		String controlable = null;
		for(Point point : points ) {
			controlable = point.getValue(strIsControlable);
		}
		if ( null != controlable ) txtAttributeStatus[0].setText((0==controlable.compareTo("0")?"No":"Yes"));
		
		logger.log(Level.SEVERE, "updateDynamicDisplay End");
	}
	
	private LinkedList<String> infos	= new LinkedList<String>();
	private LinkedList<String> controls	= new LinkedList<String>();
	private LinkedList<String> tags		= new LinkedList<String>();
	private LinkedList<String> advances	= new LinkedList<String>();
	@Override
	public void buildTabsAddress(String scsEnvId, String[] instances) {
		
		logger.log(Level.SEVERE, "updateTabs Begin");

		String[] infoPrefix				= new String[] {"dci", "aci", "sci"};
		String[] controlPrefix			= new String[] {"dio", "aio", "sio"};
		String[] tagsEnding				= new String[] {"PTW", "LR"};
		
		logger.log(Level.SEVERE, "updateTabs Iterator Begin");
		
		for ( String dbaddress : instances ) {
			logger.log(Level.SEVERE, "updateTabs Iterator dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {
				String dbaddressTokenes[] = dbaddress.split(":");
				String point = dbaddressTokenes[dbaddressTokenes.length-1];
				
				if ( null != point ) {
					for ( String s : controlPrefix ) {
						if ( point.startsWith(s) ) {
							controls.add(dbaddress);
							continue;
						}
					}
					for ( String s : tagsEnding ) {
						if ( point.endsWith(s) ) {
							tags.add(dbaddress);
							continue;
						}
					}
					for ( String s : infoPrefix ) {
						if ( point.startsWith(s) ) {
							infos.add(dbaddress);
							advances.add(dbaddress);
						}
					}
				} else {
					logger.log(Level.SEVERE, "updateTabs Iterator point IS NULL");
				}
			} else {
				logger.log(Level.SEVERE, "updateTabs Iterator dbaddress IS NULL");
			}
		}

		logger.log(Level.SEVERE, "updateTabs Iterator End");
		
		for ( String dbaddress : infos )	{ logger.log(Level.SEVERE, "updateTabs infos dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : controls )	{ logger.log(Level.SEVERE, "updateTabs controls dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : tags )		{ logger.log(Level.SEVERE, "updateTabs tags dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : advances )	{ logger.log(Level.SEVERE, "updateTabs advances dbaddress["+dbaddress+"]"); }
		
		uiInspectorInfo.setAddresses	(scsEnvId, infos.toArray(new String[0]));
		uiInspectorControl.setAddresses	(scsEnvId, controls.toArray(new String[0]));
		uiInspectorTag.setAddresses		(scsEnvId, tags.toArray(new String[0]));
		uiInspectorAdvance.setAddresses	(scsEnvId, advances.toArray(new String[0]));
		
		logger.log(Level.SEVERE, "updateTabs End");
	}
	
	@Override
	public void makeTabsSetAddress() {
		for ( String dbaddress : infos )	{ logger.log(Level.SEVERE, "updateTabs infos dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : controls )	{ logger.log(Level.SEVERE, "updateTabs controls dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : tags )		{ logger.log(Level.SEVERE, "updateTabs tags dbaddress["+dbaddress+"]"); }
		for ( String dbaddress : advances )	{ logger.log(Level.SEVERE, "updateTabs advances dbaddress["+dbaddress+"]"); }
		
		uiInspectorInfo.setAddresses	(scsEnvId, infos.toArray(new String[0]));
		uiInspectorControl.setAddresses	(scsEnvId, controls.toArray(new String[0]));
		uiInspectorTag.setAddresses		(scsEnvId, tags.toArray(new String[0]));
		uiInspectorAdvance.setAddresses	(scsEnvId, advances.toArray(new String[0]));		
	}
	
	@Override
	public void makeTabsBuildWidgets() {
		logger.log(Level.SEVERE, "makeTabsBuildWidgets Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.buildWidgets();
			} else {
				logger.log(Level.SEVERE, "makeTabsBuildWidgets uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.SEVERE, "makeTabsBuildWidgets End");
	}
	
	@Override
	public void makeTabsConnect() {
		logger.log(Level.SEVERE, "makeTabsConnect Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.connect();
			} else {
				logger.log(Level.SEVERE, "makeTabsConnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.SEVERE, "makeTabsConnect End");
	}

	@Override
	public void makeTabsDisconnect() {
		logger.log(Level.SEVERE, "tagsDisconnect Begin");
		
		for ( UIInspectorTab_i uiPanelInspector : uiInspectorTabs ) {
			if ( null != uiPanelInspector ) {
				uiPanelInspector.disconnect();
			} else {
				logger.log(Level.SEVERE, "tagsDisconnect uiPanelInspector_i IS NULL");
			}
		}

		logger.log(Level.SEVERE, "tagsDisconnect End");
	}
	
	private UIInspectorTab_i uiInspectorInfo	= null;
	private UIInspectorTab_i uiInspectorControl	= null;
	private UIInspectorTab_i uiInspectorTag		= null;
	private UIInspectorTab_i uiInspectorAdvance	= null;
	
	private LinkedList<UIInspectorTab_i> uiInspectorTabs = null;
	
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
	
		uiInspectorTabs 	= new LinkedList<UIInspectorTab_i>();
		
		uiInspectorInfo		= new UIInspectorInfo();
		uiInspectorControl	= new UIInspectorControl();
		uiInspectorTag		= new UIInspectorTag();
		uiInspectorAdvance	= new UIInspectorAdvance();

		uiInspectorTabs.add(uiInspectorInfo);
		uiInspectorTabs.add(uiInspectorControl);
		uiInspectorTabs.add(uiInspectorTag);
		uiInspectorTabs.add(uiInspectorAdvance);
		
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
				
//				disconnect();
//				
//				makeTabsDisconnect();
				
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
