package com.thalesgroup.scadasoft.gwebhmi.main.client.factory;

import java.util.Map;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.thalesgroup.prj_gz_cocc.gwebhmi.main.client.layout.CoccAppPanel;

public class COCC implements IAppEntryPoint {

	@Override
	public void launch(Map<String, String> map) {
		
    	final CoccAppPanel appPanel = new CoccAppPanel();
        RootLayoutPanel.get().add(appPanel);
	}

}
