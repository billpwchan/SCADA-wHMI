package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uiview.uiview.client.UIView_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsOlsListPanelEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class UIViewEvent implements UIView_i, WrapperScsOlsListPanelEvent {
	
	private static Logger logger = Logger.getLogger(UIViewEvent.class.getName());
	
	public static final int LAYOUT_BORDER	= 0;
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
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
	
	
	private String [] strNoOfEvents = new String [] {
			"No. of Event", "0"
	};	
	private String [] counterNames = { "eventlist_counter_all"};
	private InlineLabel[] inlineLabel;
		
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	

		HorizontalPanel numOfEventBar = new HorizontalPanel();
		numOfEventBar.getElement().getStyle().setPadding(20, Unit.PX);
		
		inlineLabel = new InlineLabel[strNoOfEvents.length];
		for(int i=0;i<strNoOfEvents.length;++i){
			inlineLabel[i] = new InlineLabel(strNoOfEvents[i]);
			inlineLabel[i].getElement().getStyle().setPadding(20, Unit.PX);
			if ( (i % 2) != 0 ) inlineLabel[i].setStyleName("project-event-summary-counter");
			numOfEventBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			numOfEventBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			numOfEventBar.add(inlineLabel[i]);
		}
		
		
		String strPrint = "Print";
		String strFilterReset = "Filter Reset";
		String strFilterApplied = "Filter Applied";
		String [] strFilters = new String [] {
				strPrint, strFilterReset, strFilterApplied
		};
		HorizontalPanel filterBar = new HorizontalPanel();
//		filterBar.getElement().getStyle().setPadding(20, Unit.PX);
		for(int i=0;i<strFilters.length;++i){
			Button button = new Button(strFilters[i]);
//			button.getElement().getStyle().setPadding(10, Unit.PX);
			button.setWidth("100px");
			button.setHeight("45px");
			
			if ( 0 == strFilters[i].compareToIgnoreCase(strFilterApplied) ) {
				button.getElement().getStyle().setColor(RGB_RED);
			}
			
			filterBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			filterBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			filterBar.add(button);
		}
		
//		String [] strNavigators = new String [] {
//				"|<", "<", "<<", "EVENTS", "/", "TOTAL_EVENTS", ">>", ">", ">|"
//		};
//		HorizontalPanel nagivatorBar = new HorizontalPanel();
//		nagivatorBar.getElement().getStyle().setPadding(10, Unit.PX);
//		for(int i=0;i<strNavigators.length;++i){
//			InlineLabel inlineLabel = new InlineLabel(strNavigators[i]);
//			inlineLabel.getElement().getStyle().setPadding(20, Unit.PX);
//			nagivatorBar.add(inlineLabel);
//		}
		
		HorizontalPanel upperBar = new HorizontalPanel();
		upperBar.setWidth("100%");
		upperBar.setHeight("100%");
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(numOfEventBar);
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(filterBar);
				
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setWidth("100%");
		bottomBar.setHeight("100%");
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		bottomBar.add(nagivatorBar);
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addNorth(upperBar, 60);
		basePanel.addSouth(bottomBar, 40);
		//basePanel.add(new CellTableEvent().GetTablePanel(15));
		
//
	    String SCS_OLS_LIST_ID = "scseventList";
	    WrapperScsOlsListPanel wrapperScsOlsListPanel = new WrapperScsOlsListPanel(SCS_OLS_LIST_ID, false);
	    wrapperScsOlsListPanel.setSize("100%", "100%");
	    wrapperScsOlsListPanel.setBorderWidth(1);
	    wrapperScsOlsListPanel.setCounterNames(counterNames);	    
	    wrapperScsOlsListPanel.setWrapperScsOlsListPanelEvent(this);
		
		basePanel.add(wrapperScsOlsListPanel.getMainPanel());
//
		
		UIPanelPanelToolBar uiPanelPanelToolBar = new UIPanelPanelToolBar();
		HorizontalPanel panelToolBar = uiPanelPanelToolBar.getMainPanel(this.uiNameCard);
		VerticalPanel toolBarPanel = new VerticalPanel();
		toolBarPanel.setBorderWidth(LAYOUT_BORDER);
		toolBarPanel.setWidth("100%");
		toolBarPanel.setHeight("100%");
		toolBarPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
	    toolBarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    toolBarPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    toolBarPanel.add(panelToolBar);
		
		uiPanelPanelToolBar.setButton("Event Summary", true);
//		
		DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
		root.setWidth("100%");
		root.setHeight("100%");
		root.addSouth(toolBarPanel, 50);
		root.add(basePanel);
		
		for ( int i = 0 ; i < counterNames.length; ++i) {
			Integer value = wrapperScsOlsListPanel.getCounter(counterNames[i]);
			if ( null != value ) {
				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
			}
		}
		
		logger.log(Level.FINE, "getMainPanel End");
		
	    return root;
	}
	@Override
	public void setTaskProvide(UITask_i taskProvide) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void valueChanged(String name, int value) {
		logger.log(Level.SEVERE, "valueChanged Begin");
		
		logger.log(Level.SEVERE, " **** valueChanged name["+name+"] value["+value+"]");
		for ( int i = 0 ; i < counterNames.length; ++i) {
			if ( 0 == name.compareTo(counterNames[i]) ) {
				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
			}			
		}
		
		logger.log(Level.SEVERE, "valueChanged End");
		
	}
}
