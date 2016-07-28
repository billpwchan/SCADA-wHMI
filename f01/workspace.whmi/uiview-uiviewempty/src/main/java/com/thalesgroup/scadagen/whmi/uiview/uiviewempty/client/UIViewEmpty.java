package com.thalesgroup.scadagen.whmi.uiview.uiviewempty.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIViewEmpty implements UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UIViewEmpty.class.getName());

	public static final String UNIT_PX = "px";
	public static final int LAYOUT_BORDER = 0;

	public static final String RGB_RED = "rgb( 255, 0, 0)";
	public static final String RGB_GREEN = "rgb( 0, 255, 0)";
	public static final String RGB_BLUE = "rgb( 0, 0, 255)";

	public static final String RGB_BTN_SEL = "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG = "#F1F1F1";
	public static final String IMG_NONE = "none";

	public static final String RGB_PAL_BG = "#BEBEBE";

	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private ComplexPanel root = null;
	@Override
	public void init(String xmlFile) {
		logger.log(Level.FINE, "getMainPanel Begin");

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
		root.add(hp);

		logger.log(Level.FINE, "getMainPanel End");
	}

	@Override
	public ComplexPanel getMainPanel() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public void setParameter(String key, String value) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}
}
