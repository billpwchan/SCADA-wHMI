package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uiview.uiview.client.UIView_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIViewAlarm implements UIView_i, WrapperScsAlarmListPanelEvent {
	
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
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard){
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		

		FlexTable flexTableFilters = new FlexTable();
		flexTableFilters.setWidth("100%");
//		flexTableFilters.setBorderWidth(LAYOUT_BORDER);
		
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

		String strAcknowledgePage = "Ack. Page";
		String strPrint = "Print";
		String strFilterReset = "Filter Reset";
		String strFilterApplied = "Filter Applied";
		String [] strFilters = new String [] {
				strAcknowledgePage, strPrint, strFilterReset, strFilterApplied
		};
		
		String strAcknowledgePageCss	= "project-gwt-button-alarmsummary-ackpage";
		String strPrintCss 				= "project-gwt-button-alarmsummary-print";
		String strFilterResetCss 		= "project-gwt-button-alarmsummary-filterreset";
		String strFilterAppliedCss 		= "project-gwt-button-alarmsummary-filterapplied";
		String strFilterCsss [] = new String [] { strAcknowledgePageCss, strPrintCss, strFilterResetCss, strFilterAppliedCss};
		
		HorizontalPanel filterBar = new HorizontalPanel();
//		filterBar.getElement().getStyle().setPadding(20, Unit.PX);
		for(int i=0;i<strFilters.length;++i){
			Button button = new Button(strFilters[i]);
//			button.getElement().getStyle().setPadding(10, Unit.PX);
//			button.setWidth("100px");
//			button.setHeight("45px");
			button.addStyleName(strFilterCsss[i]);
//			if ( 0 == strFilters[i].compareToIgnoreCase(strFilterApplied) ) {
//				button.getElement().getStyle().setColor(RGB_RED);
//			}
			
			filterBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			filterBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			filterBar.add(button);
		}
		
//		String [] strNavigators = new String [] {
//				"|<", "<", "<<", "ALARMS", "/", "TOTAL_ALARMS", ">>", ">", ">|"
//		};
//		HorizontalPanel nagivatorBar = new HorizontalPanel();
//		nagivatorBar.getElement().getStyle().setPadding(10, Unit.PX);
//		for(int i=0;i<strNavigators.length;++i){
//			InlineLabel inlineLabel = new InlineLabel(strNavigators[i]);
//			inlineLabel.getElement().getStyle().setPadding(20, Unit.PX);
//			nagivatorBar.add(inlineLabel);
//		}
		
		HorizontalPanel upperBar = new HorizontalPanel();
//		upperBar.setWidth("100%");
//		upperBar.setHeight("100%");
		upperBar.addStyleName("project-gwt-panel-alarmsummary-upperbar");
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(flexTableFilters);
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(filterBar);
				
//		HorizontalPanel bottomBar = new HorizontalPanel();
////		bottomBar.setWidth("100%");
////		bottomBar.setHeight("100%");
//		bottomBar.addStyleName("project-gwt-panel-alarmsummary-bottombar");
//		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
////		bottomBar.add(nagivatorBar);
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addNorth(upperBar, 60);
//		basePanel.addSouth(bottomBar, 40);
		//basePanel.add(new CellTableAlarm().GetTablePanel(15));
		
//
	
	    String SCS_ALARM_LIST_ID = "scsalarmList";
	    WrapperScsAlarmListPanel wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel(SCS_ALARM_LIST_ID, false, false, true);
	    wrapperScsAlarmListPanel.setSize("100%", "100%");
	    wrapperScsAlarmListPanel.setBorderWidth(1);
//	    wrapperScsAlarmListPanel.setCounterNames(counterNames);
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(this);

		basePanel.add(wrapperScsAlarmListPanel.getMainPanel());
		
//
		
		
		UIPanelPanelToolBar uiPanelPanelToolBar = new UIPanelPanelToolBar();
		HorizontalPanel panelToolBar = uiPanelPanelToolBar.getMainPanel(this.uiNameCard);
		VerticalPanel toolBarPanel = new VerticalPanel();
//		toolBarPanel.setBorderWidth(LAYOUT_BORDER);
//		toolBarPanel.setWidth("100%");
//		toolBarPanel.setHeight("100%");
		toolBarPanel.addStyleName("project-gwt-panel-alarmsummary-toolbar");
	    toolBarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    toolBarPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    toolBarPanel.add(panelToolBar);
		
		uiPanelPanelToolBar.setButton("Alarm Summary", true);
		
		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
		root.addSouth(toolBarPanel, 50);
		root.add(basePanel);
		
//		for ( int i = 0 ; i < counterNames.length; ++i) {
//			Integer value = wrapperScsAlarmListPanel.getCounter(counterNames[i]);
//			if ( null != value ) {
//				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
//			}
//		}
		
		logger.log(Level.FINE, "getMainPanel End");
		
	    return root;
	}
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void valueChanged(String name, int value) {
		logger.log(Level.FINE, "valueChanged Begin");
		
		logger.log(Level.FINE, " **** valueChanged name["+name+"] value["+value+"]");
		for ( int i = 0 ; i < counterNames.length; ++i) {
			if ( 0 == name.compareTo(counterNames[i]) ) {
				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
			}			
		}

		logger.log(Level.FINE, "valueChanged End");
		
	}
}
