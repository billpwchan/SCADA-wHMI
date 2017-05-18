package com.thalesgroup.scadasoft.gwebhmi.security.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.component.UITools;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.dictionary.Dictionary;
import com.thalesgroup.prj_gz_cocc.gwebhmi.security.client.CoccLoginPanel;

public class COCC implements IScsLoginEntryPoint {

	@Override
	public void launch(Map<String, Object> params) {
		
    	UITools.disableDefaultContextMenu(RootPanel.getBodyElement());

    	CoccLoginPanel loginPanel_ = new CoccLoginPanel();
        loginPanel_.setHeaderText(Dictionary.getWording("loginHeaderText"));
        RootPanel.get().add(loginPanel_);
        
	}

}
