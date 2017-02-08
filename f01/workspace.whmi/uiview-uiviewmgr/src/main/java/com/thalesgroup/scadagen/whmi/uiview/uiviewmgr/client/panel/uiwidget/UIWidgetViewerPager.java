package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcTagControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetViewerPager extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetViewerPager.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
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
						String actionsetkey = element;
						
						HashMap<String, HashMap<String, Object>> override = null;
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

					}
				}
			}
			logger.begin(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
			
			logger.info(className, function, "oe[{}] element[{}]", oe, element);
			
			if ( null != oe ) {
				
				if ( oe.equals(element) ) {
					
					String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
					
					logger.info(className, function, "os1[{}]", os1);
					
					if ( null != os1 ) {
						
						String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
					
						if ( os1.equals("HasPreviousPage") ) {
							
							String actionsetkey = os1+"_"+os2;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
							
						} else if ( os1.equals("HasNextPage") ) {
							
							String actionsetkey = os1+"_"+os2;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
							
						} else if ( os1.equals("HasFastForwardPage") ) {
							
							String actionsetkey = os1+"_"+os2;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
							
						}
						
						String strPageValueChanged = "PagerValueChanged_";
						if ( os1.equals(strPageValueChanged+"PageStart") ) {
							if ( null != os2 ) {
								int value = 0;
								try {
									value = Integer.parseInt(os2);
								} catch ( NumberFormatException ex ) {
									logger.warn(className, function, "os2["+os2+"] ex[{}]", os2, ex.toString());
								}
								
								String actionsetkey = os1;
								String actionkey = os1;
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							} else {
								logger.warn(className, function, "os2 IS NULL");
							}
							
						} else if ( os1.equals(strPageValueChanged+"EndIndex") ) {
							if ( null != os2 ) {
								int value = 0;
								try {
									value = Integer.parseInt(os2);
								} catch ( NumberFormatException ex ) {
									logger.info(className, function, "os2["+os2+"] ex[{}]", os2, ex.toString());
								}
								
								String actionsetkey = os1;
								String actionkey = os1;
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							} else {
								logger.warn(className, function, "os2 IS NULL");
							}
						} else if ( os1.equals(strPageValueChanged+"Exact") ) {
							if ( null != os2 ) {
								int value = 0;
								try {
									value = Integer.parseInt(os2);
								} catch ( NumberFormatException ex ) {
									logger.info(className, function, "os2["+os2+"] ex[{}]", os2, ex.toString());
								}
								
								String actionsetkey = os1;
								String actionkey = os1;
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							} else {
								logger.warn(className, function, "os2 IS NULL");
							}
						}
					}
				}
			}
			
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

//		String strUIWidgetGeneric = "UIWidgetGeneric";
//		String strHeader = "header";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
//		if ( null != dictionariesCache ) {
//			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnAlias.toString(), strHeader);
//		}
		
//		logger.info(className, function, "columnAlias[{}]", columnAlias);
		
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
