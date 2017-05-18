package com.thalesgroup.scadasoft.gwebhmi.security.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws_i;
import com.thalesgroup.scadasoft.gwebhmi.security.client.ScsLoginEntryPoint_i.PropertiesName;

public class WHMI implements IScsLoginEntryPoint {

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
		
		uiGws.setParameter(UIGws_i.DictionaryFolder, 	params.get(PropertiesName.dictionary.toString()));
		uiGws.setParameter(UIGws_i.PropertyFolder, 		params.get(PropertiesName.property.toString()));
		uiGws.setParameter(UIGws_i.JsonFolder, 			params.get(PropertiesName.json.toString()));
		uiGws.setParameter(UIGws_i.UICtrl, 				params.get(PropertiesName.uiCtrl.toString()));
		uiGws.setParameter(UIGws_i.ViewXMLFile, 		params.get(PropertiesName.uiView.toString()));
		uiGws.setParameter(UIGws_i.OptsXMLFile, 		params.get(PropertiesName.uiOpts.toString()));
		uiGws.setParameter(UIGws_i.Element, 			params.get(PropertiesName.element.toString()));

		uiGws.init();
		RootLayoutPanel.get().add(uiGws.getMainPanel());

	}
	
}
