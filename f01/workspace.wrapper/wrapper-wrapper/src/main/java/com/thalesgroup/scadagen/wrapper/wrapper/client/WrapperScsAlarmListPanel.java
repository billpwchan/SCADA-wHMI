package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsAlarmListPanel;

public class WrapperScsAlarmListPanel {
	
	private static Logger logger = Logger.getLogger(WrapperScsAlarmListPanel.class.getName());
	
	private String alarmListId;
	private boolean withAction;
	private boolean withCaption;
	private boolean withAck;
	public WrapperScsAlarmListPanel ( String alarmListId, boolean withAction, boolean withCaption, boolean withAck) {
		this.alarmListId	= alarmListId;
		this.withAction		= withAction;
		this.withCaption	= withCaption;
		this.withAck		= withAck;
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
	
	private ScsAlarmListPanel scsAlarmListPanel = null;
	
	private WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent = null;
	public void setWrapperScsAlarmListPanelEvent(WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent) { this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent; }
	public VerticalPanel getMainPanel() {

        final Set<FilterSetEvent> filterSet = null;
        
		scsAlarmListPanel = new ScsAlarmListPanel(AppUtils.EVENT_BUS, alarmListId, withAction, withCaption, withAck, filterSet);
	    scsAlarmListPanel.setWidth(this.width);
	    scsAlarmListPanel.setHeight(this.height);
	    scsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			
			@Override
			public void valueChanged(String name, String value) {
				logger.log(Level.FINE, "valueChanged name["+name+"] value["+value+"]");
				if ( null != wrapperScsAlarmListPanelEvent ) wrapperScsAlarmListPanelEvent.valueChanged(name, value);
			}
		});
	    
	    VerticalPanel alarmPanel = new VerticalPanel();
	    alarmPanel.setWidth(this.width);
	    alarmPanel.setHeight(this.height);
	    alarmPanel.setBorderWidth(this.border);
	    alarmPanel.add(scsAlarmListPanel);
	    
	    return alarmPanel;
	}

}
