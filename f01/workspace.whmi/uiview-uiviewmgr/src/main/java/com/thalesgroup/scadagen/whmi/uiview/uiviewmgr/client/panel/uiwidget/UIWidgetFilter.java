package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewOperation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIWidgetGenericAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.FilterParameter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetFilter extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 			= null;

	private final String strSet1				= "set1";
	private final String strClear				= "clear";
	private final String strApplied				= "applied";

	private Widget widgetClear				= null;
	
	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	private String strHeader = "header";
	private String strOption = "option";

	@Override
	public Widget getWidget(String element) {
		Widget widget = null;
		widget = uiWidgetGeneric.getWidget(element);
		return widget;
	}
	
	private UIEventAction getUIEventAction(String element) {
		final String function = "getUIEventAction";
		
		logger.begin(className, function);
		
		logger.info(className, function, "element[{}]", element);
		UIEventAction action = null;
		if ( null != element ) {
		
			action = mgr.getUIEventAction(element);

			if ( null == action ) {
				logger.warn(className, function, "element[{}] IS UNKNOW TYPE", element);
			}
		} else {
			logger.warn(className, function, "element IS NULL");
		}
		logger.end(className, function);
		return action;
	}
	
	private void fireUIEventAction(UIEventAction uiEventAction) {
		final String function = "fireUIEventAction";
		
		logger.begin(className, function);
		if ( null != uiEventAction ) {
			
			if ( logger.isInfoEnabled() ) {
				for ( String parameterKey : uiEventAction.getParameterKeys() ) {
					String parameterValue = (String) uiEventAction.getParameter(parameterKey);
					logger.info(className, function, "parameterKey[{}] parameterValue[{}]", parameterKey, parameterValue);
				}
			}

			eventBus.fireEventFromSource(uiEventAction, this);
		} else {
			logger.warn(className, function, "fireUIEventAction IS NULL");
		}
		logger.begin(className, function);
	}
	
	private void setFilter(String element) {
		final String function = "setFilter";
		
		logger.begin(className, function);
		
		logger.info(className, function, "element[{}]", element);
		if ( null != element ) {
			
			UIEventAction action = getUIEventAction(element);
			if ( null != action ) {
				fireUIEventAction(action);
			} else {
				logger.warn(className, function, "action IS NULL");
			}
			
			if ( element.equals(strClear) ) {
				if ( null != widgetClear ) uiWidgetGeneric.setWidgetStatus(strClear, WidgetStatus.Disable);
			}

		} else {
			logger.warn(className, function, "element IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private void onWidgetEvent (Widget widget) {
		final String function = "onWidgetEvent";
		if ( null != widget ) {
			String element = uiWidgetGeneric.getWidgetElement(widget);
			setFilter(element);
		} else {
			logger.warn(className, function, "widget IS NULL");
		}
	}
	
	private void onButtonValueChange(ValueChangeEvent<String> event) {
		final String function = "onButtonValueChange";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.warn(className, function, "event IS NULL");
		}
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.warn(className, function, "event IS NULL");
		}
	}
		
	void onUIEvent(UIEvent uiEvent ) {
	}
		
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		String ot	= (String) uiEventAction.getParameter(ViewAttribute.OperationTarget.toString());
		String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
		String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
		String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
		
		logger.info(className, function, "ot["+ot+"]");
		logger.info(className, function, "op["+op+"]");
		logger.info(className, function, "os1["+os1+"]");
		logger.info(className, function, "os2["+os2+"]");
		logger.info(className, function, "os3["+os3+"]");
		
		if ( null != ot ) {
			if ( ot.equals(className) ) {
				
				if ( null != op ) {
					if ( op.equals("SetFilter") ) {
						
						setFilter(os1);
						
					} else if ( UIWidgetGenericAction.isSupportedAction(op) ) {

						UIWidgetGenericAction uiWidgetGenericAction = new UIWidgetGenericAction(className);
						uiWidgetGenericAction.action(uiWidgetGeneric, uiEventAction);

					} 
				}
			}
		}

		logger.end(className, function);
	}

	private UIActionEventMgr mgr = null;
	// Create Filter Operation Index
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "Loading strEventBusName[{}]", strEventBusName);
		
		mgr = new UIActionEventMgr(className, strUIWidgetGeneric, optsXMLFile);
		mgr.initActionKeys(strHeader, ViewOperation.toStrings());
		mgr.initActions(strOption, ViewAttribute.Operation.toString(), FilterParameter.toStrings());
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		widgetClear	= uiWidgetGeneric.getWidget( strClear );
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnValueChangeHandler() {
			
			@Override
			public void setUIWidgetEventOnValueChangeHandler(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				onButtonValueChange(event);
			}
		});
		
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

		uiWidgetGeneric.setWidgetStatus( strSet1,  WidgetStatus.Down );
		uiWidgetGeneric.setWidgetStatus( strClear,  WidgetStatus.Disable );
		
		logger.end(className, function);
	}

}
