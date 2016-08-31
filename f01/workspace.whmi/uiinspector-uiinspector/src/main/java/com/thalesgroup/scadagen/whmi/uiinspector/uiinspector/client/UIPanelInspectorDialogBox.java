package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;


public class UIPanelInspectorDialogBox extends DialogBox implements UIInspector_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelInspectorDialogBox.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 600, baseHeight = 620;
	
	private UIPanelInspector uiPanelInspector = null;
	private Panel rootPanel = null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = "setUINameCard";
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.end(className, function);
	}
	
	private String xml = null;
	@Override
	public void setXMLFile(String xml) {
		this.xml = xml;
	}
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		logger.info(className, function, "xml[{}]", xml);
		
		
		this.setModal(false);
		
		
		uiPanelInspector = new UIPanelInspector();
		uiPanelInspector.setUINameCard(this.uiNameCard);
		uiPanelInspector.setUIPanelInspectorEvent(new UIPanelInspectorDialogBoxEvent() {
			
			@Override
			public void setTitle(String text) {
				setText(text);
			}
			
			@Override
			public void hideDialogBox() {
				hide();
			}
		});
		uiPanelInspector.init();
		rootPanel = uiPanelInspector.getMainPanel();
		logger.info(className, function, "mouseX[{}] mouseY[{}]", mouseX, mouseY);

		this.add(rootPanel);
		this.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

		logger.end(className, function);
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
		
		logger.begin(className, function);
		uiPanelInspector.setParent(scsEnvId, parent);
		logger.end(className, function);
	}

	@Override
	public void setPeriod(String period) {
		final String function = "setPeriod";
		
		logger.begin(className, function);
		uiPanelInspector.setPeriod(period);
		logger.end(className, function);
	}

	@Override
	public void connect() {
		final String function = "connect";
		
		logger.begin(className, function);
		uiPanelInspector.connect();
		logger.end(className, function);
	}

	@Override
	public void disconnect() {
		final String function = "disconnect";
		
		logger.begin(className, function);
		uiPanelInspector.disconnect();
		logger.end(className, function);
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
	public void setUIPanelInspectorEvent(UIPanelInspectorDialogBoxEvent uiPanelInspectorDialogBoxEvent) {
		// TODO Auto-generated method stub
		
	}

}
