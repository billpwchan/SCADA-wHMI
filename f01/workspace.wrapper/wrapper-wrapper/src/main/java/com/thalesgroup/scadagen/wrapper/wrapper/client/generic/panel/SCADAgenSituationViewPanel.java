package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.AppUtils;

public class SCADAgenSituationViewPanel {
	public SCADAgenSituationViewPanel(String configurationId) { setConfigurationId(configurationId); }
	
	private String configurationId = "";
	public void setConfigurationId ( String configurationId ) { this.configurationId = configurationId; }
	public String getConfigurationId () { return this.configurationId; }
	
	private String width;
	private String height;
	public void setSize(String width, String height) {
		this.width = width;
		this.height = height;
	}
	private SCADAgenSituationViewPanelEvent scadagenSituationViewPanelEvent = null;
	public void setSCADAgenSituationViewPanelEvent ( SCADAgenSituationViewPanelEvent scadagenSituationViewPanelEvent ) {
		this.scadagenSituationViewPanelEvent = scadagenSituationViewPanelEvent;
	}
	
	private ScsSituationViewPanel scsSituationViewPanel = null;
	public HorizontalPanel getMainPanel() {
		
		scsSituationViewPanel = new ScsSituationViewPanel(this.configurationId, AppUtils.EVENT_BUS, this.scadagenSituationViewPanelEvent);
		scsSituationViewPanel.setWidth("100%");
		scsSituationViewPanel.setHeight("100%");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth(this.width);
		hp.setHeight(this.height);
		hp.add(scsSituationViewPanel);
		
		return hp;
	}
	
	public void terminate() {
		if ( null != scsSituationViewPanel ){
			scsSituationViewPanel.terminate();
		}
	}
}
