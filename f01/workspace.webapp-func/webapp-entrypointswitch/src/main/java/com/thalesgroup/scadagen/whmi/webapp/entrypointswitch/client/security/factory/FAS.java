package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPointInstant;

public class FAS implements IScsLoginEntryPoint {
	
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[FAS] ";

	@Override
	public void launch(Map<String, Object> params) {
		LOGGER.debug(LOG_PREFIX+" launch.init...");
		
        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());
        
	    String uiPanel	= "UILayoutEntryPoint";
	    String uiView	= "UILayoutEntryPointLoginSummary/UILayoutEntryPointLoginSummary.view.xml";
	    String uiOpts	= null;
	    String uiElem	= "UILayoutEntryPointLoginSummary";
	    String uiDicts	= null;	
        
    	ResizeComposite widget = new UIWidgetEntryPointInstant(uiPanel, uiView, uiOpts, uiElem, uiDicts);
    	
    	RootPanel.get().add(widget);
	}

}