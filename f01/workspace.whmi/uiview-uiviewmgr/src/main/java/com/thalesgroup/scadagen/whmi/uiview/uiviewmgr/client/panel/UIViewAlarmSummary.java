package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.scadasoft.gwebhmi.main.client.panels.ScsOlsListPanelMenuHandler;

public class UIViewAlarmSummary extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIViewAlarmSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIEventActionProcessor uiEventActionProcessor = null;

	private final String strAcknowledge = "Ack";
	private final String strAcknowledgePage = "Ack. Page";
	private final String strPrint = "Print";
	private final String strFilterReset = "Filter Reset";
	private final String strFilterApplied = "Filter Applied";
	private final String [] strFilters = new String [] {
			strAcknowledge, strAcknowledgePage, strPrint, strFilterReset, strFilterApplied
	};
	
	private String [] counterNames = { 
			"alarmlist_counter_all_unack", "alarmlist_counter_critical_unack", "alarmlist_counter_hight_unack", "alarmlist_counter_medium_unack"/*, "alarmlist_counter_low_unack"*/
			, "alarmlist_counter_all", "alarmlist_counter_critical", "alarmlist_counter_hight", "alarmlist_counter_medium"/*, "alarmlist_counter_low"*/
	};
	
	private String [] strNoOfAlarms = new String [] {
			"UnAck:", "0", "Super Critical (UnAck):", "0", "Critical (UnAck):", "0", "Less Critical (UnAck):", "0"
			,"Total:", "0", "Super Critical (All):", "0", "Critical (All):", "0", "Less Critical (All):", "0"
			
	};
	private InlineLabel[] inlineLabel;
	
	private void onButton(Button button) {
		final String function = "onButton";
		
		logger.begin(className, function);
		if ( null != button ) {
			String text = button.getText();
			logger.info(className, function, "onButton text[{}]", text);
			if ( null != text ) {
				if ( null != wrapperScsAlarmListPanel ) {
					if ( strAcknowledge.equals(text) ) {
						wrapperScsAlarmListPanel.ackVisibileSelected();
					} else if ( strAcknowledgePage.equals(text) ) {
						wrapperScsAlarmListPanel.ackVisibile();
					}
				} else {
					logger.warn(className, function, "wrapperScsAlarmListPanel IS NULL");
				}
			} else {
				logger.warn(className, function, "text IS NULL");
			}
		} else {
			logger.warn(className, function, "button IS NULL");
		}
		
		logger.end(className, function);
	}

	private WrapperScsAlarmListPanel wrapperScsAlarmListPanel = null;
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String optsXMLFile = "UIEventActionProcessor_CallImage.opts.xml";
		uiEventActionProcessor = new UIEventActionProcessor();
		uiEventActionProcessor.setUINameCard(uiNameCard);
		uiEventActionProcessor.setPrefix(className);
		uiEventActionProcessor.setElement(element);
		uiEventActionProcessor.setDictionariesCacheName("UIWidgetGeneric");
//		uiEventActionProcessor.setEventBus(eventBus);
		uiEventActionProcessor.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessor.setUIWidgetGeneric(uiWidgetGeneric);
		uiEventActionProcessor.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor.init();

//		uiEventActionProcessor.executeActionSetInit();
//		uiEventActionProcessor.executeActionSetInit(1000, null);
		
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
			
			button.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					onButton((Button) event.getSource());
				}
			});
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
	    wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel(SCS_ALARM_LIST_ID, false, false, true);
	    wrapperScsAlarmListPanel.setScsOlsListPanelMenuHandler(new ScsOlsListPanelMenuHandler() {
			
			@Override
			public void onSelection(Set<HashMap<String, String>> entity) {
				logger.warn(className, function, "entity[{}]", entity);
				
				if ( null != entity ) {
					HashMap<String, String> hashMap = entity.iterator().next();
					if ( null != hashMap ) {
						String sourceID = hashMap.get("sourceID");
						logger.info(className, function, "sourceID[{}]", sourceID);
						if ( null != sourceID ) {
							if ( null != uiEventActionProcessor ) {
								uiEventActionProcessor.executeActionSet(sourceID);
							} else {
								logger.warn(className, function, "uiEventActionProcessor IS NULL");	
							}
						} else {
							logger.warn(className, function, "sourceID IS NULL");	
						}
					} else {
						logger.warn(className, function, "hashMap IS NULL");	
					}
				} else {
					logger.warn(className, function, "entity IS NULL");	
				}
			}
		});
	    wrapperScsAlarmListPanel.setSize("100%", "100%");
	    wrapperScsAlarmListPanel.setBorderWidth(1);
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			
	    	@Override
	    	public void valueChanged(String name, String value) {
	    		logger.info(className, function, "valueChanged Begin");
	    		
	    		logger.info(className, function, "valueChanged name[{}] value[{}]", name, value);
	    		for ( int i = 0 ; i < counterNames.length; ++i) {
	    			if ( 0 == name.compareTo(counterNames[i]) ) {
	    				inlineLabel[(i*2)+1].setText(value);
	    			}			
	    		}

	    		logger.info(className, function, "valueChanged End");
	    		
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
		
		rootPanel = new DockLayoutPanel(Unit.PX);
//		root.addSouth(toolBarPanel, 50);
		rootPanel.add(basePanel);

		logger.end(className, function);
		
	}

}
