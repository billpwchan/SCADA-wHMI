package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws_i;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.main.AppEntryPoint_i.PropertiesName;

public class SCADAgen implements IAppEntryPoint {
	
	/** logger */
    private static final ClientLogger LOGGER = ClientLogger.getClientLogger();
    private static final String LOG_PREFIX = "[SCADAgen] ";

	@Override
	public void launch(final Map<String, Object> params) {
		LOGGER.debug(LOG_PREFIX+" launch...");
		
    	if ( null != params ) {

    		final UIGws uiGws = new UIGws();
    		
    		uiGws.setParameter(UIGws_i.Parameters.uiDict.toString(), params.get(PropertiesName.dictionary.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiProp.toString(), params.get(PropertiesName.property.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiJson.toString(), params.get(PropertiesName.json.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiCtrl.toString(), params.get(PropertiesName.uiCtrl.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiView.toString(), params.get(PropertiesName.uiView.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiOpts.toString(), params.get(PropertiesName.uiOpts.toString()));
    		uiGws.setParameter(UIGws_i.Parameters.uiElem.toString(), params.get(PropertiesName.element.toString()));
    		
    		uiGws.init();
    		RootLayoutPanel.get().add(uiGws.getMainPanel());
    		
    	}
	}

}
