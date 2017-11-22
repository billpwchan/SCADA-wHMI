package com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws_i;
import com.thalesgroup.scadagen.whmi.webapp.entrypointswitch.client.security.ScsLoginEntryPoint_i.PropertiesName;

public class SCADAgen implements IScsLoginEntryPoint {

	@Override
	public void launch(Map<String, Object> params) {
		
		String disDefMenu	= null;
		Object obj = params.get(PropertiesName.disableDefaultContextMenu.toString());
		if ( null != obj ) {
			if ( obj instanceof String ) {
				disDefMenu = (String)obj;
			}
		}
		
		if ( Boolean.parseBoolean(disDefMenu) ) {
			UITools.disableDefaultContextMenu(RootPanel.getBodyElement());
		}
		
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
