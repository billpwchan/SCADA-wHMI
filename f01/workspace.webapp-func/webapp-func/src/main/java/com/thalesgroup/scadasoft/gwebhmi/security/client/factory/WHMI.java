package com.thalesgroup.scadasoft.gwebhmi.security.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.scadagen.whmi.uiroot.uiroot.client.UIGws;
import com.thalesgroup.scadasoft.gwebhmi.security.client.ScsLoginEntryPoint_i.PropertiesName;

public class WHMI implements IScsLoginEntryPoint {

	@Override
	public void launch(Map<String, String> map) {
	    	
		String dictionary	= map.get(PropertiesName.dictionary.toString());
		String property 	= map.get(PropertiesName.property.toString());
		String json		 	= map.get(PropertiesName.json.toString());
		String uiCtrl		= map.get(PropertiesName.uiCtrl.toString());
		String uiView		= map.get(PropertiesName.uiView.toString());
		String uiOpts		= map.get(PropertiesName.uiOpts.toString());
		String element 		= map.get(PropertiesName.element.toString());
		
		String disDefMenu	= map.get(PropertiesName.disableDefaultContextMenu.toString());
		
		if ( Boolean.parseBoolean(disDefMenu) ) {
			UITools.disableDefaultContextMenu(RootPanel.getBodyElement());
		}
		
		final UIGws uiGws = new UIGws();
		uiGws.setDictionaryFolder(dictionary);
		uiGws.setPropertyFolder(property);
		uiGws.setJsonFolder(json);
		uiGws.setUICtrl(uiCtrl);
		uiGws.setViewXMLFile(uiView);
		uiGws.setOptsXMLFile(uiOpts);
		uiGws.setElement(element);
		uiGws.init();
		RootLayoutPanel.get().add(uiGws.getMainPanel());

	}
	
}
