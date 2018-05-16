package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dialog;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorWrapper;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.panel.UIPanelInspectorEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;

public class UIInspectorDialogBox extends DialogBox implements UIInspector_i {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 600, baseHeight = 620;
	
	private UIPanelInspectorWrapper uiPanelInspector = null;
	private Panel rootPanel = null;
	private String hvid = null;
	private String hvType = null;
	
	private String function = null;
	private String location = null;
	public void setFunctionLocation(String function, String location) {
		this.function = function;
		this.location = location;
	}
	
	private boolean modeless = false;
	public void setModeless(boolean modeless) { 
		final String function = "setModeless";
		logger.beginEnd(function);
		this.modeless = modeless;
	}
	
	private UIInspectorDialogBoxEvent uiInspectorDialogBoxEvent = null;
	public void setUIInspectorDialogBoxEvent(UIInspectorDialogBoxEvent uiInspectorDialogBoxEvent) { 
		final String function = "setUIInspectorDialogBoxEvent";
		logger.beginEnd(function);
		this.uiInspectorDialogBoxEvent = uiInspectorDialogBoxEvent;
	}
	
//	private String element = null;
	@Override
	public void setElement(String element) {
//		final String function = "setElement";
//		logger.begin(function);
//		
//		this.element = element;
//		
//		logger.end(function);
	}
	@Override
	public String getElementName() { return null; }
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = "setUINameCard";
		logger.begin(function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.end(function);
	}

	@Override
	public UINameCard getUINameCard() { return this.uiNameCard; }

	
	public void setHvInfo(String hvid, String hvType) {
		this.hvid = hvid;
		this.hvType = hvType;
	}
	
	private String viewXMLFile = null;
	@Override
	public void setViewXMLFile(String viewXMLFile) { this.viewXMLFile = viewXMLFile; }
	@Override
	public String getViewXMLFile() { return this.viewXMLFile; }
	
	private String optsXMLFile = null;
	@Override
	public void setOptsXMLFile(String optsXMLFile) {this.optsXMLFile = optsXMLFile;}
	@Override
	public String getOptsXMLFile() { return this.optsXMLFile; }
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(function);
		logger.debug(function, "viewXMLFile[{}] optsXMLFile[{}]", viewXMLFile, optsXMLFile);
		
		this.setModal(modeless);
		
		uiPanelInspector = new UIPanelInspectorWrapper();
		uiPanelInspector.setUINameCard(this.uiNameCard);
		uiPanelInspector.setHvInfo(this.hvid, this.hvType);
		uiPanelInspector.setFunctionLocation(this.function, this.location);
		uiPanelInspector.setUIInspectorEvent(new UIPanelInspectorEvent() {
			
			@Override
			public void setTitle(String text) {
				setText(text);
			}
			
			@Override
			public void onClose() {
				final String function = "onClose";
				
				logger.begin(function);

				if ( null != uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClose();
				
				logger.end(function);
			}
		});
		uiPanelInspector.init();
		
		// Auto close handle	
		rootPanel = new FocusPanel();
		rootPanel.add(uiPanelInspector.getMainPanel());
		((FocusPanel)rootPanel).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ( null != event ) {
					if ( null != uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick(); 
				} else {
					logger.debug(function, "event IS NULL");
				}
			}
		});
		
		logger.debug(function, "mouseX[{}] mouseY[{}]", mouseX, mouseY);

		this.add(rootPanel);
		this.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		
		logger.end(function);
	}
	
	
	@Override
	public Panel getMainPanel() {
        return rootPanel;
	}
	
	private int mouseX = 0, mouseY = 0;
	public void setMousePosition(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		
		int screenWidth = Window.getClientWidth();
		int screenHeight = Window.getClientHeight();

		int windowWidth = 1920; //Window.getClientWidth();
		int windowHeight = screenHeight;
		
		int curScreenID = this.uiNameCard.getUiScreen();
		
		int widthMultBase = 2;
		int widthMultValue = 1;
		if ( mouseX >= 0 && mouseX <= screenWidth ) {
			widthMultBase = 4;
			widthMultValue = (mouseX > (windowWidth / 2) ? 1 : 3);
		}
		
		int left	= (windowWidth * curScreenID) + ((windowWidth / widthMultBase) * widthMultValue) - ( baseWidth / 2 ) - (baseBoderX / 2);
        int top		= (windowHeight / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);

        this.setPopupPosition(left, top);
	}

	@Override
	public void setParent(String scsEnvId, String parent) {
		final String function = "setParent";
		logger.begin(function);
		uiPanelInspector.setParent(scsEnvId, parent);
		logger.end(function);
	}

	@Override
	public void connect() {
		final String function = "connect";
		logger.begin(function);
		uiPanelInspector.connect();
		logger.end(function);
	}

	@Override
	public void disconnect() {
		final String function = "disconnect";
		logger.begin(function);
		uiPanelInspector.disconnect();
		logger.end(function);
	}

	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
		if ( rootPanel != null ) {
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
	
	@Override
	public void close() {
		final String function = "close";
		logger.begin(function);
		terminate();
		logger.end(function);
	}
	
	@Override
	public void beginDragging(MouseDownEvent event) {
		super.beginDragging(event);
		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick();
	}
	
	@Override
	public void endDragging(MouseUpEvent event) {
		super.endDragging(event);
		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick();
	}

	@Override
	public void setCtrlHandler(String ctrlHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		final String function = "terminate";
		logger.begin(function);
		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClose();
		logger.end(function);
	}
}
