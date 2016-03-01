package com.thalesgroup.scadagen.wrapper.wrapper.client;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.presenter.filter.StringEnumFilterDescription;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.datagrid.view.header.event.FilterSetEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.AppUtils;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsAlarmListPanel;

public class WrapperScsAlarmListPanel implements WrapperScsAlarmListPanelEvent {
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
		
        // build layout SplitLayout is DockLayout
        // Set critical alarm filter event      
        final Set<String> values = new HashSet<String>();
        values.add( "CRITICAL" );
        values.add( "HIGH" );
        values.add( "MEDIUM" );
        StringEnumFilterDescription filterdesc = new StringEnumFilterDescription(values);
        final FilterSetEvent filterEvent = new FilterSetEvent("scsalarmList_priority_name", filterdesc);
        final Set<FilterSetEvent> filterSet = new HashSet<FilterSetEvent>();
        filterSet.add(filterEvent);
		
		scsAlarmListPanel = new ScsAlarmListPanel(AppUtils.EVENT_BUS, alarmListId, withAction, withCaption, withAck, filterSet);
	    scsAlarmListPanel.setWidth(this.width);
	    scsAlarmListPanel.setHeight(this.height);
	    scsAlarmListPanel.setHeight(this.height);
	    scsAlarmListPanel.setCounterNames(counterNames);
	    scsAlarmListPanel.setWrapperScsAlarmListPanelEvent(this);
	    
	    VerticalPanel alarmPanel = new VerticalPanel();
	    alarmPanel.setWidth(this.width);
	    alarmPanel.setHeight(this.height);
	    alarmPanel.setBorderWidth(this.border);
	    alarmPanel.add(scsAlarmListPanel);
	    
	    return alarmPanel;
	    
	}
	
	public Integer getCounter(String key) { return this.scsAlarmListPanel.getCounter(key); }
	
	private String [] counterNames; 
	public void setCounterNames(String [] counterNames) {
		this.counterNames = counterNames;
	}
	@Override
	public void valueChanged(String name, int value) {
		if ( null != wrapperScsAlarmListPanelEvent ) {
			wrapperScsAlarmListPanelEvent.valueChanged(name, value);
		}
	}
}
