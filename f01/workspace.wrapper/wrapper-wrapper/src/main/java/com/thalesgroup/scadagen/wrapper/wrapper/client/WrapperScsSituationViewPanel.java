package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsSituationViewPanel;

public class WrapperScsSituationViewPanel {
	public WrapperScsSituationViewPanel(String configurationId) {
		setConfigurationId(configurationId);
	}
	private String configurationId = "";
	public void setConfigurationId ( String configurationId ) {
		this.configurationId =configurationId;
	}
	private String width;
	private String height;
	public void setSize(String width, String height) {
		this.width = width;
		this.height = height;
	}
	public HorizontalPanel getMainPanel() {
		
		ScsSituationViewPanel scsSituationViewPanel = new ScsSituationViewPanel(this.configurationId, AppUtils.EVENT_BUS);
		scsSituationViewPanel.setWidth("100%");
		scsSituationViewPanel.setHeight("100%");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth(this.width);
		hp.setHeight(this.height);
		hp.add(scsSituationViewPanel);
		
		return hp;
	}
}
