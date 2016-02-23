package com.thalesgroup.scadagen.whmi.uiview.uiviewempty.client;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uiview.uiview.client.UIView_i;

public class UIViewEmpty implements UIView_i {
	
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

	public static final String IMAGE_PATH = "imgs";

	private UINameCard uiNameCard = null;

//	private InlineLabel equipmenpLabel = null;

	@Override
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		logger.log(Level.FINE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
		root.add(hp);

		logger.log(Level.FINE, "getMainPanel End");

		return root;
	}

	LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();

	public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}

	public void removeHandlerRegistrations() {
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}

	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		// TODO Auto-generated method stub
		
	}
}
