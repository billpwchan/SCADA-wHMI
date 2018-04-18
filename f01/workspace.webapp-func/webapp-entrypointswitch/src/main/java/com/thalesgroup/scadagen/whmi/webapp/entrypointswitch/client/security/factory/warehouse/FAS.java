package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.warehouse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.scadagen.whmi.config.configenv.client.WebConfigMgrEvent;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGwsWebConfigMgr;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.IEntryPoint;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory.IFramework;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.UIWidgetEntryPointInstant;

public class FAS implements IFramework {
	
	private final String className_ = this.getClass().getSimpleName();
    private final ClientLogger logger_ = ClientLogger.getClientLogger(this.getClass().getName());
    private final String logPrefix_= "["+className_+"] ";

	@Override
	public void launch(final Map<String, Object> params) {
		final String f = "launch";
		logger_.debug(logPrefix_+f);
		
		final String mode	= IFramework.XML_MODE;
		final String module	= IFramework.XML_MODULE;
		final String folder	= IEntryPoint.STR_XML_FOLDER;
	    final String xml	= className_+IFramework.XML_EXTENSION;
	    final String tag	= IFramework.XML_TAG;
		
        final List<String> keys = new LinkedList<String>();
        for ( String properties : PropertiesName.toStrings() ) {
        	keys.add(properties);
        }
        
        final UIGwsWebConfigMgr web = UIGwsWebConfigMgr.getInstance();
        
        web.getWebConfig( mode,  module, folder, xml, tag, keys, new WebConfigMgrEvent() {
			@Override
			public void updated(Map<String, String> keyValues) {
				logger_.debug(logPrefix_+f+" getWebConfig updated");
				
				final Map<String, Object> params = new HashMap<String, Object>();
				for ( String key : keyValues.keySet() ) {
					params.put(key, keyValues.get(key));
        		}
				
				realize(params);
			}
			@Override
			public void failed() {
				logger_.debug(logPrefix_+f+" getWebConfig failed");
				realize(null);
			}
        });
	}

	private void realize(final Map<String, Object> params) {
		logger_.debug(logPrefix_+" realize");
		
        UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

    	Widget widget = new UIWidgetEntryPointInstant(params);
    	
    	RootPanel.get().add(widget);
	}
}
