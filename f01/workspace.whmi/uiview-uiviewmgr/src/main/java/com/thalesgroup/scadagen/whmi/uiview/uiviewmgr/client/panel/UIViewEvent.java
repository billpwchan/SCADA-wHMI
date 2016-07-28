package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsOlsListPanelEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class UIViewEvent implements UIWidget_i {
	
	private static Logger logger = Logger.getLogger(UIViewEvent.class.getName());
	
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
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private ComplexPanel root = new DockLayoutPanel(Unit.PX);
	@Override
	public void init(String xmlFile) {
		
		HorizontalPanel numOfEventBar = new HorizontalPanel();
		numOfEventBar.getElement().getStyle().setPadding(20, Unit.PX);
		
		inlineLabel = new InlineLabel[strNoOfEvents.length];
		for(int i=0;i<strNoOfEvents.length;++i){
			inlineLabel[i] = new InlineLabel(strNoOfEvents[i]);
			inlineLabel[i].getElement().getStyle().setPadding(20, Unit.PX);
			if ( (i % 2) != 0 ) {
				inlineLabel[i].setStyleName("project-gwt-inlinelabel-eventsummary-counter-value");
			} else {
				inlineLabel[i].setStyleName("project-gwt-inlinelabel-eventsummary-counter-label");
			}
			numOfEventBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			numOfEventBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			numOfEventBar.add(inlineLabel[i]);
			
			inlineLabel[i].setVisible(false);
		}
		
		
		String strPrint = "Print";
		String strFilterReset = "Filter Reset";
		String strFilterApplied = "Filter Applied";
		String [] strFilters = new String [] {
				strPrint, strFilterReset, strFilterApplied
		};
		
		String strPrintCss 				= "project-gwt-button-eventsummary-print";
		String strFilterResetCss 		= "project-gwt-button-eventsummary-filterreset";
		String strFilterAppliedCss 		= "project-gwt-button-eventsummary-filterapplied";
		String strFilterCsss [] = new String [] { strPrintCss, strFilterResetCss, strFilterAppliedCss};
		
		HorizontalPanel filterBar = new HorizontalPanel();
		for(int i=0;i<strFilters.length;++i){
			Button button = new Button(strFilters[i]);
			button.addStyleName(strFilterCsss[i]);

			filterBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			filterBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			filterBar.add(button);
		}

		HorizontalPanel upperBar = new HorizontalPanel();

		upperBar.addStyleName("project-gwt-panel-eventsummary-upperbar");
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(numOfEventBar);
		upperBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		upperBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		upperBar.add(filterBar);

		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addNorth(upperBar, 60);

	    String SCS_OLS_LIST_ID = "scseventList";
	    WrapperScsOlsListPanel wrapperScsOlsListPanel = new WrapperScsOlsListPanel(SCS_OLS_LIST_ID, false);
	    wrapperScsOlsListPanel.setSize("100%", "100%");
	    wrapperScsOlsListPanel.setBorderWidth(1);
	    wrapperScsOlsListPanel.setWrapperScsOlsListPanelEvent(new WrapperScsOlsListPanelEvent() {
			
	    	@Override
	    	public void valueChanged(String name, int value) {
	    		logger.log(Level.FINE, "valueChanged Begin");
	    		
	    		logger.log(Level.FINE, " **** valueChanged name["+name+"] value["+value+"]");
	    		for ( int i = 0 ; i < counterNames.length; ++i) {
	    			if ( 0 == name.compareTo(counterNames[i]) ) {
	    				inlineLabel[(i*2)+1].setText(String.valueOf(value));
	    			}
	    		}
	    		
	    		logger.log(Level.FINE, "valueChanged End");
	    		
	    	}
		});
		
		basePanel.add(wrapperScsOlsListPanel.getMainPanel());
		
//		UIPanelPanelToolBar uiPanelPanelToolBar = new UIPanelPanelToolBar();
//		HorizontalPanel panelToolBar = uiPanelPanelToolBar.getMainPanel(this.uiNameCard);
//		VerticalPanel toolBarPanel = new VerticalPanel();
//		toolBarPanel.addStyleName("project-gwt-panel-eventsummary-toolbar");
//	    toolBarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//	    toolBarPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//	    
//	    toolBarPanel.add(panelToolBar);
//		
//		uiPanelPanelToolBar.setButton("Event Summary", true);
//		
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
