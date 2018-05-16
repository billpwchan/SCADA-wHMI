package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;

public class UIWidgetVerifyDatabaseMultiReadingControl extends UIWidget_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	
	@Override
	public void terminate() {
		if ( null != databaseMultiRead_i ) databaseMultiRead_i.disconnect();
	}

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String function = "onClick";
			
			logger.begin(function);
			
			if ( null != event ) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(function, "element[{}]", element);
					if ( null != element ) {
						
						if ( 0 == "create".compareTo(element) ) {
							String strReadClassName = uiWidgetGeneric.getWidgetValue("createvalue");
							logger.info(function, "strReadClassName[{}]", strReadClassName);
							databaseMultiRead_i = DatabaseMultiReadFactory.get(strReadClassName);
						}
						
						if ( 0 == "connect".compareTo(element) ) {
							databaseMultiRead_i.connect();
						}
						
						if ( 0 == "disconnect".compareTo(element) ) {
							databaseMultiRead_i.disconnect();
						}

						String muiltread = "multiread";
						if ( 0 == muiltread.compareTo(element)) {
							
							final String muiltreadprefix = muiltread+"_";
							
							String strkeyvalue 			= muiltreadprefix+"keyvalue";
							String strscsenvidvalue 	= muiltreadprefix+"scsenvidvalue";
							String straddressvalue1 	= muiltreadprefix+"addressvalue1";
							String straddressvalue2 	= muiltreadprefix+"addressvalue2";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue1 	= uiWidgetGeneric.getWidgetValue(straddressvalue1);
							String addressvalue2 	= uiWidgetGeneric.getWidgetValue(straddressvalue2);
							
							String [] addressvalues = new String[]{addressvalue1, addressvalue2};
							
							databaseMultiRead_i.addMultiReadValueRequest(keyvalue, scsenvidvalue, addressvalues, new DatabasePairEvent_i() {
								
								@Override
								public void update(String key, String [] dbaddresses, String[] values) {
									String result = "Result from key["+key+"]";
									for ( int i = 0 ; i < values.length ; ++i ) {
										String dbaddress = dbaddresses[i];
										String value = values[i];
										result += " ("+i+")dbaddress["+dbaddress+"] value["+value+"]";
									}
									uiWidgetGeneric.setWidgetValue(muiltreadprefix+"resultvalue", result);
								}
							});
							
						}
						
					}
				}
			}
			logger.begin(function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			logger.begin(function);
			logger.end(function);
		}
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(function);
		
		String strEventBusName = getStringParameter(UIRealize_i.ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(function, "strEventBusName[{}]", strEventBusName);

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
					}
				}
			})
		);

		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(function);
	}

}
