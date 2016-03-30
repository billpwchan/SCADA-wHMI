package com.thalesgroup.scadagen.wrapper.wrapper.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;

/**
 * A widget displaying an alamm list panel.
 */
public class WrapperScsOlsListPanel implements WrapperScsOlsListPanelEvent {
	
	private String alarmListId;
	private boolean withCaption;
	public WrapperScsOlsListPanel ( String olsListId, boolean withCaption) {
		this.alarmListId	= olsListId;
		this.withCaption	= withCaption;
	}
	private String width	= "100%";
	private String height	= "100%";
	public void setSize(String width, String height) {
		this.width  = width;
		this.height = height;
	}
	private int border		= 0;
	public void setBorderWidth(int border) {
		this.border = border;
	}
	private  ScsOlsListPanel scsOlsListPanel = null;
	private WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent = null;
	public void setWrapperScsOlsListPanelEvent(WrapperScsOlsListPanelEvent wrapperScsOlsListPanelEvent) { this.wrapperScsOlsListPanelEvent = wrapperScsOlsListPanelEvent; }
	public VerticalPanel getMainPanel() {
		
	    scsOlsListPanel = new ScsOlsListPanel(AppUtils.EVENT_BUS, alarmListId, withCaption, null);
	    scsOlsListPanel.setWidth(this.width);
	    scsOlsListPanel.setHeight(this.height);
	    scsOlsListPanel.setCounterNames(counterNames);
	    scsOlsListPanel.setWrapperScsOlsListPanelEvent(wrapperScsOlsListPanelEvent);
	    
	    VerticalPanel olsPanel = new VerticalPanel();
	    olsPanel.setWidth(this.width);
	    olsPanel.setHeight(this.height);
	    olsPanel.setBorderWidth(this.border);
	    olsPanel.add(scsOlsListPanel);
	    
	    return olsPanel;
	    
	}
	
	public Integer getCounter(String key) { return this.scsOlsListPanel.getCounter(key); }
	
	private String [] counterNames; 
	public void setCounterNames(String [] counterNames) {
		this.counterNames = counterNames;
	}
	@Override
	public void valueChanged(String name, int value) {
		if ( null != wrapperScsOlsListPanelEvent ) {
			wrapperScsOlsListPanelEvent.valueChanged(name, value);
		}
	}

}
