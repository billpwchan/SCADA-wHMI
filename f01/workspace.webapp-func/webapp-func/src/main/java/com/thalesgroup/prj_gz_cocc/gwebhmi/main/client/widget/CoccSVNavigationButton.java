package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
//import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;

public class CoccSVNavigationButton extends Button {
	/** CSS class name for navigation button containers */
	public static final String CSS_SITUATION_VIEW_NAV_BTN_WRAP = "divSituationViewNavBtnWrap";

	/** CSS class name for navigation buttons */
	public static final String CSS_SITUATION_VIEW_NAV_BTN = "divSituationViewNavBtn";

	/** CSS class name to indicate the button has been initialized */
	public static final String CSS_INITIALIZED = "initialized";

	//private static final ClientLogger logger_ = ClientLogger.getClientLogger();

	/** Navigation ID of the view associated to the button */
	private String navID_;

	/** Position properties of the button */
	private Map<String, String> dimension_;

	/**
	 * Constructor
	 */
	public CoccSVNavigationButton() {
		super();
	}

	/**
	 * Constructor
	 * @param desc Description shown on the button
	 * @param navID Navigation ID of the view associated to the button
	 * @param properties Dimension properties of the button
	 */
	public CoccSVNavigationButton(
			final String desc,
			final String navID,
			Map<String, String> properties) {
		// Call Parent Constructor
		super(desc);

		// Store Event Bus and Navigation ID
		navID_ = navID;

		// Store Dimension Properties
		dimension_ = new HashMap<String, String>(properties);

		// Set CSS Class Name
		addStyleName(CSS_SITUATION_VIEW_NAV_BTN);
	}

	/**
	 * Returns the navigation ID of the button
	 * @return Navigation ID associated with the button
	 */
	public String getNavID() {
		return navID_;
	}

	/**
	 * Initializes the position of the button
	 */
	public void initButton() {
		// Reuse the Style Instance
		Style style = getElement().getParentElement().getStyle();

		// Go through each Property
		for(final String key : dimension_.keySet()) {
			final String value = dimension_.get(key);
			if(value != null) {
				style.setProperty(key, value);
			}
		}

		// Indicate the button has been initialized
		getElement().addClassName(CSS_INITIALIZED);

		// Update Property MarginTop to Align the Button Vertically
		if(dimension_.get("top") == "50%" && dimension_.get("bottom") == "auto" &&
				dimension_.get("marginTop") == null) {
			// Margin-top = - (Button Height / 2)
			style.setMarginTop(-getOffsetHeight()/2, Style.Unit.PX);
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();

		// Clean up Map before Detaching
		dimension_.clear();
	}
}
