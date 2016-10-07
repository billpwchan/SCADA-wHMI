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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionExecuteOld;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.ValidityStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIWidgetDpcScanSuspendControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDpcScanSuspendControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private DpcMgr dpcMgr				= null;
	
	private String columnAlias			= "";
	private String columnStatus			= "";
	private String columnServiceOwner	= "";
	
	private String valueSet				= "";
	private String valueUnSet			= "";
	
	private final String strSet					= "set";
	private final String strUnSet				= "unset";
	private final String strApply				= "apply";

	private Set<HashMap<String, String>> selectedSet = null;
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
				if ( null != element ) {
					
					String statusSet		= null;
					String statusUnSet	= null;
					String statusApply	= null;
					
					if ( element.equals(strSet) ) {

						statusSet				= WidgetStatus.Down.toString();
						statusUnSet				= WidgetStatus.Disable.toString();
						statusApply				= WidgetStatus.Up.toString();

					} else if ( element.equals(strUnSet) ) {
						
						statusSet				= WidgetStatus.Disable.toString();
						statusUnSet				= WidgetStatus.Down.toString();
						statusApply				= WidgetStatus.Up.toString();

					} else if ( element.equals(strApply) ) {

						statusSet				= WidgetStatus.Disable.toString();
						statusUnSet				= WidgetStatus.Disable.toString();
						statusApply				= WidgetStatus.Disable.toString();
						
						for ( HashMap<String, String> hashMap : selectedSet ) {
							String selectedAlias = hashMap.get(columnAlias);
							String selectedServiceOwner = hashMap.get(columnServiceOwner);
							
							logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
							
							String scsEnvId = selectedServiceOwner;
							String alias = selectedAlias;
							
							logger.info(className, function, "alias BF [{}]", alias);
							
							alias = "<alias>" + selectedAlias;
							
							logger.info(className, function, "alias AF [{}]", alias);
							
							WidgetStatus curStatusSet = uiWidgetGeneric.getWidgetStatus(strSet);
							
							ValidityStatus validityStatus = DCP_i.ValidityStatus.VALID;
							if ( WidgetStatus.Down == curStatusSet ) {
								validityStatus = ValidityStatus.OPERATOR_INHIBIT;
							}
							
							String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + validityStatus.toString() + "_" + alias;
							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, validityStatus);
	
//							ValidityStatus validityStatus = ValidityStatus.NO_ALARM_INHIBIT_VAR;
//							if ( WidgetStatus.Down == curStatusSet ) {
//								validityStatus = ValidityStatus.ALARM_INHIBIT_VAR;
//							}
//
//							String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + validityStatus.toString() + "_" + alias;
//								
//							logger.info(className, function, "key[{}]", key);
//							
//							dpcMgr.sendChangeVarStatus(key, scsEnvId, alias, validityStatus);

						}

					}

					UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
					
					uiWidgetGenericAction.action("SetWidgetStatus", strSet, statusSet);
					uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, statusUnSet);
					uiWidgetGenericAction.action("SetWidgetStatus", strApply, statusApply);
					
				}
			} else {
				logger.warn(className, function, "button IS NULL");
			}
		}
		
		logger.end(className, function);
	}


	private UIWidgetGeneric uiWidgetGeneric = null;
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	@SuppressWarnings("unchecked")
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		if ( null != uiEventAction ) {
			String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
		
			Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
			
			logger.info(className, function, "op[{}]", op);
			logger.info(className, function, "obj1[{}]", obj1);
			
			if ( null != op ) {
				
				String statusSet		= null;
				String statusUnSet	= null;
				String statusApply	= null;
				
				// Filter Action
				if ( op.equals(ViewerViewEvent.FilterAdded) || op.equals(ViewerViewEvent.FilterRemoved) ) {
					
					statusSet				= WidgetStatus.Disable.toString();
					statusUnSet				= WidgetStatus.Disable.toString();
					statusApply				= WidgetStatus.Disable.toString();
					
				} else if ( op.equals(ViewerViewEvent.RowSelected.toString() ) ) {
					// Activate Selection
					
					selectedSet	= (Set<HashMap<String, String>>) obj1;
					
					String selectedStatus1 = null;
					for ( HashMap<String, String> hashMap : selectedSet ) {
	
						selectedStatus1 = hashMap.get(columnStatus);
					}
	
					if ( null != selectedStatus1 ) {
						if ( valueUnSet.equals(selectedStatus1) ) {
							statusSet				= WidgetStatus.Up.toString();
							statusUnSet				= WidgetStatus.Disable.toString();
						}
						if ( valueSet.equals(selectedStatus1) ) {
							statusSet				= WidgetStatus.Disable.toString();
							statusUnSet				= WidgetStatus.Up.toString();
						}
					}
					
					statusApply				= WidgetStatus.Disable.toString();
				} else {
					logger.warn(className, function, "op[{}] type IS UNKNOW", op);
				}
	
				UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
				
				uiWidgetGenericAction.action("SetWidgetStatus", strSet, statusSet);
				uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, statusUnSet);
				uiWidgetGenericAction.action("SetWidgetStatus", strApply, statusApply);
			}
		} else {
			logger.warn(className, function, "uiEventAction IS NULL");
		}
		

		
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		dpcMgr = DpcMgr.getInstance("almmgn");
		
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
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				onButton(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);

		UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
		
		uiWidgetGenericAction.action("SetWidgetStatus", strSet, "Disable");
		uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, "Disable");
		uiWidgetGenericAction.action("SetWidgetStatus", strApply, "Disable");
		
		logger.end(className, function);
	}

}