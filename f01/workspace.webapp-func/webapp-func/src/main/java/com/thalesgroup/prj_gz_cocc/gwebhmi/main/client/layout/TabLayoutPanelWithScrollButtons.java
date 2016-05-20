package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.LayoutPanel;

// 2015-07-28 delo: [COCC-16] Class First Created
/**
 * This class extends TabLayoutPanel and implements a pair of scroll buttons to
 * navigate between tabs when the tabs overflow their container.
 * @author delo
 *
 */
public class TabLayoutPanelWithScrollButtons extends com.google.gwt.user.client.ui.TabLayoutPanel {
	private static final String CSS_TAB_SCROLL_BTN = "tabScrollBtn";
	private static final String CSS_TAB_SCROLL_BTN_WRAP = "tabScrollBtnWrap";
	private Button leftBtn_;
	private Button rightBtn_;

	/**
	 * Constructor
	 * @param inBarHeight Height of the tab bar
	 * @param inBarUnit Unit of the height
	 */
	public TabLayoutPanelWithScrollButtons(double inBarHeight, Unit inBarUnit) {
		super(inBarHeight, inBarUnit);
		LayoutPanel panel = (LayoutPanel) getWidget();

		// Create Buttons
		leftBtn_ = new Button("<<");
		leftBtn_.setWidth(inBarHeight + inBarUnit.toString());
		leftBtn_.addStyleName(CSS_TAB_SCROLL_BTN);
		rightBtn_ = new Button(">>");
		rightBtn_.setWidth(inBarHeight + inBarUnit.toString());
		rightBtn_.addStyleName(CSS_TAB_SCROLL_BTN);

		// Add Buttons to the Layout Panel and Initialize their Position
		panel.add(leftBtn_);
		panel.setWidgetLeftWidth(leftBtn_, 0, Unit.PX, inBarHeight, Unit.PX);
		panel.setWidgetTopHeight(leftBtn_, 0, Unit.PX, inBarHeight, inBarUnit);
		panel.add(rightBtn_);
		panel.setWidgetRightWidth(rightBtn_, 0, Unit.PX, inBarHeight, Unit.PX);
		panel.setWidgetTopHeight(rightBtn_, 0, Unit.PX, inBarHeight, inBarUnit);

		// Add CSS Class Name to Enable Simpler Management
		panel.getWidgetContainerElement(leftBtn_).setClassName(CSS_TAB_SCROLL_BTN_WRAP);
		panel.getWidgetContainerElement(rightBtn_).setClassName(CSS_TAB_SCROLL_BTN_WRAP);
	}

	/**
	 * Attaches a click event handler to the left button
	 * @param inHandler The click event handler to attach
	 */
	public void attachLeftBtnClickHandler(ClickHandler inHandler) {
		leftBtn_.addClickHandler(inHandler);
	}

	/**
	 * Attaches a click event handler to the right button
	 * @param inHandler The click event handler to attach
	 */
	public void attachRightBtnClickHandler(ClickHandler inHandler) {
		rightBtn_.addClickHandler(inHandler);
	}
}