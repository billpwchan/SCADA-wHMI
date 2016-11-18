package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIDialogURL extends DialogBox {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogURL.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 600, baseHeight = 620;
	
	private Panel rootPanel = null;
	
	private String url = null;
	public void setURL (String url ) {	
		this.url = url;
	}
	
	private boolean modeless = false;
	public void setModeless(boolean modeless) { 
		final String function = "setModeless";
		logger.beginEnd(className, function);
		this.modeless = modeless;
	}
	
	private UINameCard uiNameCard = null;
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = "setUINameCard";
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.end(className, function);
	}

	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		this.setModal(modeless);
		
		HTML iframe = new HTML("<iframe src="+url+" width=100% height=100% frameborder=0></iframe>");
		iframe.setStyleName("project-gwt-html-uidialogurl");
		
		// Auto close handle
		rootPanel = new FocusPanel();		
		rootPanel.add(iframe);
//		rootPanel.add(uiPanelInspector.getMainPanel());
		((FocusPanel)rootPanel).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ( null != event ) {
//					if ( null != uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick(); 
				} else {
					logger.info(className, function, "event IS NULL");
				}
			}
		});
		
		
		logger.info(className, function, "mouseX[{}] mouseY[{}]", mouseX, mouseY);

		this.add(rootPanel);
		this.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		
		logger.end(className, function);
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
	

	public void close() {
		final String function = "close";
		logger.begin(className, function);
//		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClose();
		logger.end(className, function);
	}
	

	public void beginDragging(MouseDownEvent event) {
		super.beginDragging(event);
//		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick();
	}
	

	public void endDragging(MouseUpEvent event) {
		super.endDragging(event);
//		if ( null != this.uiInspectorDialogBoxEvent ) uiInspectorDialogBoxEvent.onClick();
	}

}
