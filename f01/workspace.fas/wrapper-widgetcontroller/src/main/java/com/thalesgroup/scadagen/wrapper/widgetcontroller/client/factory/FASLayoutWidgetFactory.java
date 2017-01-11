package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory;

import com.google.gwt.event.shared.EventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.AppLayoutsData;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.dto.WidgetConfig;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.factory.HypWidgetFactory;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.layout.view.IWidgetController;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPoint;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.factory.FASLayoutWidgetFactory_i.FASWidget;

/**
 * Showcase Widget Factory
 */
public class FASLayoutWidgetFactory extends HypWidgetFactory {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[FASLayoutWidgetFactory] ";

	/**
	 * Factory constructor
	 * @param eventBus event bus to share between widgets
	 * @param appLayoutsData Application Layout configuration
	 */
	public FASLayoutWidgetFactory(final EventBus eventBus, final AppLayoutsData appLayoutsData) {
		super(eventBus, appLayoutsData);
	}

	@Override
	public IWidgetController buildWidget(final WidgetConfig widgetConf) {

		final String typeId = widgetConf.getTypeId();
		IWidgetController widget = null;

		LOGGER.debug(LOG_PREFIX + "try to build: " + typeId);
		
	    String uiPanel	= "UILayoutEntryPoint";
	    String uiOpts	= null;
	    String uiElem	= null;
	    String uiDicts	= null;	
		
		if (FASWidget.PTW_DPC_PANEL.toString().equals(typeId)) {
			
			String uiView	= "UILayoutEntryPointDpcTagSummary.view.xml";

	    	widget = new UIWidgetEntryPoint(uiPanel, uiView, uiOpts, uiElem, uiDicts);
	    	
		}
		else if (FASWidget.LOGIN_PANEL.toString().equals(typeId)) {

	    	String uiView	= "UILayoutEntryPointLoginSummary.view.xml";
	    	
	    	widget = new UIWidgetEntryPoint(uiPanel, uiView, uiOpts, uiElem, uiDicts);
	    	
		}
		else if (FASWidget.CHANGE_PASSWORD_PANEL.toString().equals(typeId)) {

	    	String uiView	= "UILayoutOPMChangePasswordSummary.view.xml";
	    	
	    	widget = new UIWidgetEntryPoint(uiPanel, uiView, uiOpts, uiElem, uiDicts);
	    	
		}
		else if (FASWidget.SOC_PANEL.toString().equals(typeId)) {

	    	String uiView	= "UILayoutSocSummary.view.xml";
	    	
	    	widget = new UIWidgetEntryPoint(uiPanel, uiView, uiOpts, uiElem, uiDicts);
	    	
		}
		else {
			widget = super.buildWidget(widgetConf);
		}

		return widget;
	}


}
