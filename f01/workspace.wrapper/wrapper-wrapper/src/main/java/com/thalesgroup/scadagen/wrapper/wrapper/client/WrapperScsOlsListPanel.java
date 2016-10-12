package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanelMenuHandler;

/**
 * A widget displaying an alamm list panel.
 */
public class WrapperScsOlsListPanel implements WrapperScsOlsListPanelEvent {
	
	private static Logger logger = Logger.getLogger(WrapperScsOlsListPanel.class.getName());
	
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
	    scsOlsListPanel.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
			
			@Override
			public void onSelection(Set<HashMap<String, String>> entity) {
				if ( null != scsOlsListPanelMenuHandler ) {
					scsOlsListPanelMenuHandler.onSelection(entity);
				}
			}
		});
	    scsOlsListPanel.setWidth(this.width);
	    scsOlsListPanel.setHeight(this.height);
	    scsOlsListPanel.setWrapperScsOlsListPanelEvent(wrapperScsOlsListPanelEvent);
	    
	    VerticalPanel olsPanel = new VerticalPanel();
	    olsPanel.setWidth(this.width);
	    olsPanel.setHeight(this.height);
	    olsPanel.setBorderWidth(this.border);
	    olsPanel.add(scsOlsListPanel);
	    
	    return olsPanel;
	    
	}

	@Override
	public void valueChanged(String name, int value) {
		logger.log(Level.FINE, "valueChanged name["+name+"] value["+value+"]");
		if ( null != wrapperScsOlsListPanelEvent ) wrapperScsOlsListPanelEvent.valueChanged(name, value);
	}
	
    private ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler = null;
    public void setScsOlsListPanelMenuHandler(ScsOlsListPanelMenuHandler scsOlsListPanelMenuHandler) {
    	this.scsOlsListPanelMenuHandler = scsOlsListPanelMenuHandler;
    }
}
