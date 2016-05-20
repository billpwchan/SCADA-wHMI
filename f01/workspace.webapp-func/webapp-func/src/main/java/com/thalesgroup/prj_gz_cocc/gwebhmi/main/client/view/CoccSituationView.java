package com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.LayoutPanel;
//import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.NavigationActivationEvent;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.SituationView;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.situation.view.widget.WidgetFactory;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.widget.CoccSVNavigationButton;

public class CoccSituationView extends SituationView {
	/** Map of Entity IDs to Camera IDs */
	private Map<String, String> cameraIDs_ = new HashMap<String, String>();

	/** Event bus */
	private final EventBus eventBus_;

	/** Logger */
	//private static final ClientLogger logger_ = ClientLogger.getClientLogger();

	/** List of Navigation Buttons */
	private List<CoccSVNavigationButton> buttons_;

	/** Handlers for Button Click Event */
	private List<HandlerRegistration> clickHandlerRegistrations_ = new ArrayList<HandlerRegistration>();

	/**
	 * Constructor
	 */
	public CoccSituationView() {
		super();
		eventBus_ = null;
	}

	/**
	 * Constructor
	 * @param widgetFactory Custom widget factory
	 */
	public CoccSituationView(final WidgetFactory widgetFactory) {
		this(widgetFactory, null);
	}

	/**
	 * Constructor
	 * @param widgetFactory Custom widget factory
	 * @param eventBus Event bus
	 */
	public CoccSituationView(final WidgetFactory widgetFactory, final EventBus eventBus) {
		super(widgetFactory);
		eventBus_ = eventBus;
		buttons_ = new ArrayList<CoccSVNavigationButton>();
	}

	public void appendButtons(final List<Map<String, String>> buttons) {
		LayoutPanel widget = (LayoutPanel)getWidget();
		for(final Map<String, String> map : buttons) {
			if(map.containsKey("navID") && map.containsKey("desc")) {
				// Extract Navigation ID and Description from the Map
				final String desc = map.remove("desc"), navID = map.remove("navID");

				// Create a Custom Navigation Button
				final CoccSVNavigationButton button = new CoccSVNavigationButton(
						Dictionary.getWording(desc),
						navID,
						map);

				// Include the Button to the List
				buttons_.add(button);

				// Add Click Event Handler to the Button; Include the Handler Registration to the List
				clickHandlerRegistrations_.add(button.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// Retrieve Button and its Navigation ID
						final CoccSVNavigationButton button = (CoccSVNavigationButton) event.getSource();
						final String navID = button.getNavID();
						if(eventBus_ != null && navID != null) {
							// Fire a Navigation Event to Change View
							eventBus_.fireEventFromSource(
									new NavigationActivationEvent(navID),
									CoccSituationView.this);
						}
					}
				}));
				
				// Append the Button to the View
				widget.add(button);
				widget.getWidgetContainerElement(button).setClassName(CoccSVNavigationButton.CSS_SITUATION_VIEW_NAV_BTN_WRAP);
			}
		}
	}

	/**
	 * Includes the camera ID mapping
	 * @param cameraIDs Map of entity IDs to camera IDs
	 */
	public void addCameraIDs(final Map<String, String> cameraIDs) {
		cameraIDs_.putAll(cameraIDs);
	}

	/**
	 * Returns the camera ID associated to the entity ID
	 * @param entityID Entity ID of the camera
	 * @return Camera ID associated to the entity ID, or null if no camera ID is mapped to this entity
	 */
	public String getCameraID(final String entityID) {
		return cameraIDs_.get(entityID);
	}

	/**
	 * Initializes the navigation buttons in this view
	 */
	public void initButtons() {
		for(CoccSVNavigationButton button : buttons_) {
			button.initButton();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		final LayoutPanel widget = (LayoutPanel) getWidget();

		// Remove Navigation Buttons from the Panel
		for(final CoccSVNavigationButton btn : buttons_) {
			widget.remove(btn);
		}

		// Remove Registered Handlers
		for(final HandlerRegistration hr : clickHandlerRegistrations_) {
			hr.removeHandler();
		}

		// Clean up Lists
		buttons_.clear();
		cameraIDs_.clear();
		clickHandlerRegistrations_.clear();
	}
}
