
package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIScreenEmpty extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenEmpty.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	public static final String UNIT_PX = "px";
	public static final int LAYOUT_BORDER = 0;

	public static final String RGB_RED = "rgb( 255, 0, 0)";
	public static final String RGB_GREEN = "rgb( 0, 255, 0)";
	public static final String RGB_BLUE = "rgb( 0, 0, 255)";

	public static final String RGB_BTN_SEL = "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG = "#F1F1F1";
	public static final String IMG_NONE = "none";

	public static final String RGB_PAL_BG = "#BEBEBE";

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		hp.setHeight("100%");
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.add(hp);

		logger.end(className, function);
	}

}
