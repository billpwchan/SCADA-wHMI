package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIViewAlarm implements UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UIViewAlarm.class.getName());

	LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();
	public void addHandlerRegistration(HandlerRegistration handlerRegistration){
		handlerRegistrations.add(handlerRegistration);
	}
	public void removeHandlerRegistrations(){
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}
	
	private String [] counterNames = { 
			"alarmlist_counter_all_unack", "alarmlist_counter_critical_unack", "alarmlist_counter_hight_unack", "alarmlist_counter_medium_unack"/*, "alarmlist_counter_low_unack"*/
			, "alarmlist_counter_all", "alarmlist_counter_critical", "alarmlist_counter_hight", "alarmlist_counter_medium"/*, "alarmlist_counter_low"*/
	};
	
	private String [] strNoOfAlarms = new String [] {
			"UnAck:", "0", "Super Critical (UnAck):", "0", "Critical (UnAck):", "0", "Less Critical (UnAck):", "0"
			,"Total:", "0", "Super Critical (All):", "0", "Critical (All):", "0", "Less Critical (All):", "0"
			
	};
	private InlineLabel[] inlineLabel;
	
	private UINameCard uiNameCard;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private ComplexPanel root = null;
	@Override
	public void init(String xmlFile) {
		FlexTable flexTableFilters = new FlexTable();
		flexTableFilters.setWidth("100%");
		
		inlineLabel = new InlineLabel[strNoOfAlarms.length];
		for(int i=0;i<strNoOfAlarms.length;++i){
			inlineLabel[i] = new InlineLabel();
			inlineLabel[i].getElement().getStyle().setPadding(20, Unit.PX);
			if ( (i % 2) != 0 ) {
				inlineLabel[i].setStyleName("project-gwt-inlinelabel-alarmsummary-counter-value");
			} else {
				inlineLabel[i].setStyleName("project-gwt-inlinelabel-alarmsummary-counter-label");
			}
			inlineLabel[i].setWidth("100%");
			inlineLabel[i].setText(strNoOfAlarms[i]);
			flexTableFilters.setWidget(i/8, i%8, inlineLabel[i]);
		}

		String strAcknowledge = "Ack";
		String strAcknowledgePage = "Ack. Page";
		String strPrint = "Print";
		String strFilterReset = "Filter Reset";
		String strFilterApplied = "Filter Applied";
		String [] strFilters = new String [] {
				strAcknowledge, strAcknowledgePage, strPrint, strFilterReset, strFilterApplied
		};
		
		String strAcknowledgeCss		= "project-gwt-button-alarmsummary-ack";
		String strAcknowledgePageCss	= "project-gwt-button-alarmsummary-ackpage";
		String strPrintCss 				= "project-gwt-button-alarmsummary-print";
		String strFilterResetCss 		= "project-gwt-button-alarmsummary-filterreset";
		String strFilterAppliedCss 		= "project-gwt-button-alarmsummary-filterapplied";
		String strFilterCsss [] = new String [] { strAcknowledgeCss, strAcknowledgePageCss, strPrintCss, strFilterResetCss, strFilterAppliedCss};
		
		HorizontalPanel filterBar = new HorizontalPanel();
		for(int i=0;i<strFilters.length;++i){
			Button button = new Button(strFilters[i]);
			button.addStyleName(strFilterCsss[i]);

			filterBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			filterBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			filterBar.add(button);
		}
		
		HorizontalPanel upperBar = new HorizontalPanel();
		upperBar.addStyleName("project-gwt-panel-alarmsummary-upperbar");
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(flexTableFilters);
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(filterBar);
				

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addNorth(upperBar, 60);

	    String SCS_ALARM_LIST_ID = "scsalarmList";
	    WrapperScsAlarmListPanel wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel(SCS_ALARM_LIST_ID, false, false, true);
	    wrapperScsAlarmListPanel.setSize("100%", "100%");
	    wrapperScsAlarmListPanel.setBorderWidth(1);
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			
	    	@Override
	    	public void valueChanged(String name, String value) {
	    		logger.log(Level.FINE, "valueChanged Begin");
	    		
	    		logger.log(Level.FINE, " **** valueChanged name["+name+"] value["+value+"]");
	    		for ( int i = 0 ; i < counterNames.length; ++i) {
	    			if ( 0 == name.compareTo(counterNames[i]) ) {
	    				inlineLabel[(i*2)+1].setText(value);
	    			}			
	    		}

	    		logger.log(Level.FINE, "valueChanged End");
	    		
	    	}
		});

		basePanel.add(wrapperScsAlarmListPanel.getMainPanel());

//		UIPanelPanelToolBar uiPanelPanelToolBar = new UIPanelPanelToolBar();
//		HorizontalPanel panelToolBar = uiPanelPanelToolBar.getMainPanel(this.uiNameCard);
//		VerticalPanel toolBarPanel = new VerticalPanel();
//		toolBarPanel.addStyleName("project-gwt-panel-alarmsummary-toolbar");
//	    toolBarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//	    toolBarPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
//	    toolBarPanel.add(panelToolBar);
		
//		uiPanelPanelToolBar.setButton("Alarm Summary", true);
		
		root = new DockLayoutPanel(Unit.PX);
//		root.addSouth(toolBarPanel, 50);
		root.add(basePanel);

		logger.log(Level.FINE, "getMainPanel End");
		
	}
	@Override
	public ComplexPanel getMainPanel() {
		return root;
	}
	
	@Override
	public void setParameter(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}
	

}
