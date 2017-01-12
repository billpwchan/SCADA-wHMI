package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.CtlMgr;

public class UIWidgetCtlControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCtlControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private CtlMgr ctlMgr 				= null;
	
	private String columnAlias			= "";
	private String columnStatus			= "";
	private String columnServiceOwner	= "";
	
	private String valueSet				= "";
	private String valueUnSet			= "";
	
	private final String strSet			= "set";
	private final String strUnSet		= "unset";
	
	private final String strDci			= "dci";
	private final String strDio			= "dio";
	
	private Set<HashMap<String, String>> selectedSet = null;
	
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
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
							
							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
								
								logger.info(className, function, "os1[{}]", os1);
								
								if ( null != os1 ) {
									if ( os1.equals("SendCtlControl") ) {
										
											int byPassInitCond = 0;
											int byPassRetCond = 0;
											int sendAnyway = 0;
									
											for ( HashMap<String, String> hashMap : selectedSet ) {
											String selectedAlias = hashMap.get(columnAlias);
											String selectedServiceOwner = hashMap.get(columnServiceOwner);
											
											String scsEnvId = selectedServiceOwner;
											String alias = selectedAlias;
											
											logger.info(className, function, "alias BF [{}]", alias);
											
											alias = selectedAlias.replace(strDci, strDio);
											
											logger.info(className, function, "alias AF [{}]", alias);
											
											WidgetStatus curStatusSet = uiWidgetGeneric.getWidgetStatus(strSet);
											WidgetStatus curStatusUnSet = uiWidgetGeneric.getWidgetStatus(strUnSet);
											
											int value = 0;
											if ( WidgetStatus.Down == curStatusSet ) {
												value = 1;
											} else if  ( WidgetStatus.Down == curStatusUnSet ) {
												value = 0;
											}
											
											logger.info(className, function, "WidgetStatus curStatusSet[{}] WidgetStatus curStatusUnSet[{}]", curStatusSet, curStatusUnSet);
											
											ctlMgr.sendControl(scsEnvId, new String[]{alias}, value, byPassInitCond, byPassRetCond, sendAnyway);
										}
				
									}
								}
								return true;
							}
						});
					}
				}
			}
			logger.begin(className, function);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "os1["+os1+"]");
			
			if ( null != os1 ) {
				// Filter Action
				if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
					
					logger.info(className, function, "FilterAdded");
					
					uiEventActionProcessor_i.executeActionSet(os1);
					
				} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
					
					logger.info(className, function, "FilterRemoved");
					
					uiEventActionProcessor_i.executeActionSet(os1);
				
				} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
					// Activate Selection
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					
					logger.info(className, function, "Store Selected Row");
					
					selectedSet	= (Set<HashMap<String, String>>) obj1;
					
					String selectedStatus1 = null;
					for ( HashMap<String, String> hashMap : selectedSet ) {
						selectedStatus1 = hashMap.get(columnStatus);
					}
					
					if ( null != selectedStatus1 ) {
						if ( valueSet.equals(selectedStatus1) ) {
							String actionsetkey = os1+"_valueUnset";
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}
						if ( valueUnSet.equals(selectedStatus1) ) {
							String actionsetkey = os1+"_valueSet";
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}
					}

				} else {
					// General Case
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe ["+oe+"]");
					logger.info(className, function, "os1["+os1+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							uiEventActionProcessor_i.executeActionSet(os1);
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
		
		ctlMgr = CtlMgr.getInstance("ptw");
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnAlias.toString(), strHeader);
			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnStatus.toString(), strHeader);
			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnServiceOwner.toString(), strHeader);
			valueSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueSet.toString(), strHeader);
			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueUnSet.toString(), strHeader);
		}
		
		logger.info(className, function, "columnAlias[{}]", columnAlias);
		logger.info(className, function, "columnStatus[{}]", columnStatus);
		logger.info(className, function, "columnServiceOwner[{}]", columnServiceOwner);
		logger.info(className, function, "valueSet[{}]", valueSet);
		logger.info(className, function, "valueUnSet[{}]", valueUnSet);

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
