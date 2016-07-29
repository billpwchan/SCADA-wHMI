package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIPanelInspectorDialogBoxEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIPanelInspectorDialogBox extends DialogBox implements UIInspector_i {
	
	private final Logger logger = Logger.getLogger(UIPanelInspectorDialogBox.class.getName());
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 600, baseHeight = 620;
	
	private UIPanelInspector uiPanelInspector = null;
	private ComplexPanel basePanel = null;
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		logger.log(Level.FINE, "setUINameCard Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	@Override
	public void init(String xml) {
		logger.log(Level.FINE, "init Begin");
		
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
		uiPanelInspector.init(xml);
		basePanel = uiPanelInspector.getMainPanel();
		logger.log(Level.SEVERE, "onUIEvent mouseX["+mouseX+"] mouseY["+mouseY+"]");

		this.add(basePanel);
		this.addStyleName("project-gwt-panel-inspector-dialogbox");
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

        logger.log(Level.FINE, "init End");
	}
	
	
	@Override
	public ComplexPanel getMainPanel() {
        return basePanel;
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
		logger.log(Level.FINE, "setParent Begin");
		uiPanelInspector.setParent(scsEnvId, parent);
		logger.log(Level.FINE, "setParent End");
	}

	@Override
	public void setPeriod(String period) {
		logger.log(Level.FINE, "setPeriod Begin");
		uiPanelInspector.setPeriod(period);
		logger.log(Level.FINE, "setPeriod End");
	}

	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		uiPanelInspector.connect();
		logger.log(Level.FINE, "connect End");
	}

	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		uiPanelInspector.disconnect();
		logger.log(Level.FINE, "disconnect End");
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
