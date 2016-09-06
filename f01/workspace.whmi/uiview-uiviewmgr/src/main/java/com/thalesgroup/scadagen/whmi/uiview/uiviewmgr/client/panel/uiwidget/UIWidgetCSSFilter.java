package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.CSSSelectViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.CSSSelectEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetCSSFilter extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCSSFilter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 			= null;

	private final String strSet1				= "set1";
	private final String strSet0				= "set0";
		
	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	@Override
	public Widget getWidget(String element) {
		Widget widget = null;
		widget = uiWidgetGeneric.getWidget(element);
		return widget;
	}
	
	private void setFilter(String element) {
		final String function = "setFilter";
		
		logger.begin(className, function);
		if ( null != element ) {
			if ( element.equals(strSet1) ) {
				
				// Select Filter 
				{
					UIEventAction action = new UIEventAction();
					action.setParameters(ViewAttribute.Operation.toString(), CSSSelectEvent.CSSApply.toString());
					action.setParameters(ViewAttribute.OperationString1.toString(), strSet1);
					eventBus.fireEventFromSource(action, this);					
				}
				{
					UIEventAction action = new UIEventAction();
					action.setParameters(ViewAttribute.Operation.toString(), CSSSelectEvent.CSSRemove.toString());
					action.setParameters(ViewAttribute.OperationString1.toString(), strSet1);
					eventBus.fireEventFromSource(action, this);
				}
				
			} else if ( element.equals(strSet0) ) {
				
				// Select Filter 
				{
					UIEventAction action = new UIEventAction();
					action.setParameters(ViewAttribute.Operation.toString(), CSSSelectEvent.CSSApply.toString());
					action.setParameters(ViewAttribute.OperationString1.toString(), strSet0);
					eventBus.fireEventFromSource(action, this);
				}
				{
					UIEventAction action = new UIEventAction();
					action.setParameters(ViewAttribute.Operation.toString(), CSSSelectEvent.CSSRemove.toString());
					action.setParameters(ViewAttribute.OperationString1.toString(), strSet0);
					eventBus.fireEventFromSource(action, this);					
				}
				
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
		
		String op	= (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
		String od1	= (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
		String od2	= (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
		String od3	= (String) uiEventAction.getAction(ViewAttribute.OperationString3.toString());
		
		logger.info(className, function, "op["+op+"]");
		logger.info(className, function, "od1["+od1+"]");
		logger.info(className, function, "od2["+od2+"]");
		logger.info(className, function, "od3["+od3+"]");
		
		if ( null != op ) {

			// Action
			if ( op.equals(CSSSelectViewEvent.SetDefaultCSS.toString()) ) {
				
				Widget widget = uiWidgetGeneric.getWidget(strSet1);
				if ( null != widget ) {
					((RadioButton)widget).setValue(true);
				} else {
					logger.warn(className, function, "Widget strSet1[{}] IS NULL", strSet1);
				}

				setFilter(strSet1);

			} else {
				logger.warn(className, function, "uiEventAction Operation type IS UNKNOW");
			}

		} else {
			logger.warn(className, function, "op IS NULL");
		}
		
		logger.end(className, function);
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setXMLFile(xmlFile);
		uiWidgetGeneric.init();
		
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
		
		logger.end(className, function);
	}

}
