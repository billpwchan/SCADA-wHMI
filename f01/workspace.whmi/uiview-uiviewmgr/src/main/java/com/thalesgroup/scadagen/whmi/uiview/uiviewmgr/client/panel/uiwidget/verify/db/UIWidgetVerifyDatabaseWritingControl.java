package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;

public class UIWidgetVerifyDatabaseWritingControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseWritingControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private DatabaseWrite_i databaseWrite_i = null;
	
	@Override
	public void terminate() {
		if ( null != databaseWrite_i ) databaseWrite_i.disconnect();
	}

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String function = "onClick";
			
			logger.begin(className, function);
			
			if ( null != event ) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(className, function, "element[{}]", element);
					if ( null != element ) {
						
						if ( 0 == "create".compareTo(element) ) {
							String strWriteClassName = uiWidgetGeneric.getWidgetValue("createvalue");
							logger.info(className, function, "strWriteClassName[{}]", strWriteClassName);
							databaseWrite_i = DatabaseWriteFactory.get(strWriteClassName);
						}
						
						if ( 0 == "connect".compareTo(element) ) {
							databaseWrite_i.connect();
						}
						
						if ( 0 == "disconnect".compareTo(element) ) {
							databaseWrite_i.disconnect();
						}
						
						String writedatevalue = "writedatevalue";
						if ( 0 == writedatevalue.compareTo(element)) {
							String writesintvalueprefix = writedatevalue+"_";
							
							String strkeyvalue 		= writesintvalueprefix+"keyvalue";
							String strscsenvidvalue = writesintvalueprefix+"scsenvidvalue";
							String straddressvalue 	= writesintvalueprefix+"addressvalue";
							String strsecondvalue 	= writesintvalueprefix+"secondvalue";
							String strusecondvalue 	= writesintvalueprefix+"usecondvalue";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue 	= uiWidgetGeneric.getWidgetValue(straddressvalue);
							String secondvalue 		= uiWidgetGeneric.getWidgetValue(strsecondvalue);
							String usecondvalue 	= uiWidgetGeneric.getWidgetValue(strusecondvalue);
							
							int intsecondvalue		= Integer.parseInt(secondvalue);
							int intusecondvalue		= Integer.parseInt(usecondvalue);
							
							databaseWrite_i.addWriteDateValueRequest(keyvalue, scsenvidvalue, addressvalue, intsecondvalue, intusecondvalue);
						}
						
						String writeintvalue = "writeintvalue";
						if ( 0 == writeintvalue.compareTo(element)) {
							String writesintvalueprefix = writeintvalue+"_";
							
							String strkeyvalue 		= writesintvalueprefix+"keyvalue";
							String strscsenvidvalue = writesintvalueprefix+"scsenvidvalue";
							String straddressvalue 	= writesintvalueprefix+"addressvalue";
							String strvalue 		= writesintvalueprefix+"value";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue 	= uiWidgetGeneric.getWidgetValue(straddressvalue);
							String value 			= uiWidgetGeneric.getWidgetValue(strvalue);
							
							int intvalue			= Integer.parseInt(value);
							
							databaseWrite_i.addWriteIntValueRequest(keyvalue, scsenvidvalue, addressvalue, intvalue);
						}
						
						String writefloatvalue = "writefloatvalue";
						if ( 0 == writefloatvalue.compareTo(element)) {
							String writesfloatvalueprefix = writefloatvalue+"_";
							
							String strkeyvalue 		= writesfloatvalueprefix+"keyvalue";
							String strscsenvidvalue = writesfloatvalueprefix+"scsenvidvalue";
							String straddressvalue 	= writesfloatvalueprefix+"addressvalue";
							String strvalue 		= writesfloatvalueprefix+"value";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue 	= uiWidgetGeneric.getWidgetValue(straddressvalue);
							String value 			= uiWidgetGeneric.getWidgetValue(strvalue);
							
							float floatvalue		= Float.parseFloat(value);
							
							databaseWrite_i.addWriteFloatValueRequest(keyvalue, scsenvidvalue, addressvalue, floatvalue);
						}
						
						String writestringvalue = "writestringvalue";
						if ( 0 == writestringvalue.compareTo(element)) {
							String writestringvalueprefix = writestringvalue+"_";
							
							String strkeyvalue 		= writestringvalueprefix+"keyvalue";
							String strscsenvidvalue = writestringvalueprefix+"scsenvidvalue";
							String straddressvalue 	= writestringvalueprefix+"addressvalue";
							String strvalue 		= writestringvalueprefix+"value";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue 	= uiWidgetGeneric.getWidgetValue(straddressvalue);
							String value 			= uiWidgetGeneric.getWidgetValue(strvalue);
							
							databaseWrite_i.addWriteStringValueRequest(keyvalue, scsenvidvalue, addressvalue, value);
						}
						
						String writevalue = "writevalue";
						if ( 0 == writevalue.compareTo(element)) {
							
							String writevalueprefix = writevalue+"_";
							
							String strkeyvalue 		= writevalueprefix+"keyvalue";
							String strscsenvidvalue = writevalueprefix+"scsenvidvalue";
							String straddressvalue 	= writevalueprefix+"addressvalue";
							String strvalue 		= writevalueprefix+"value";
							
							String keyvalue 		= uiWidgetGeneric.getWidgetValue(strkeyvalue);
							String scsenvidvalue 	= uiWidgetGeneric.getWidgetValue(strscsenvidvalue);
							String addressvalue 	= uiWidgetGeneric.getWidgetValue(straddressvalue);
							String value 			= uiWidgetGeneric.getWidgetValue(strvalue);
							
							databaseWrite_i.addWriteValueRequest(keyvalue, scsenvidvalue, addressvalue, value);
						}
					}
				}
			}
			logger.begin(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			logger.begin(className, function);
			logger.end(className, function);
		}
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

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
		
		logger.end(className, function);
	}

}
