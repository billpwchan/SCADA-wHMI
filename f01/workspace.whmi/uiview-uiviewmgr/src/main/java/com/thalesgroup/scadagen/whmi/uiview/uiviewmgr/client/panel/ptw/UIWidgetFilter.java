package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.SummaryViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetFilter extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 			= null;
		
	private final String strFilterColumn		= "ptwdciset_SL_Cond_Res";
	private final String strFilterValueSet1		= "1";
	private final String strFilterValueSet0		= "0";

	private final String strSet1				= "set1";
	private final String strSet0				= "set0";
	private final String strClear				= "clear";
	private final String strApplied				= "applied";
	
	private Widget widgetClear				= null;
	
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
				
				// Set Filter 
				// Value as 1

				UIEventAction action = new UIEventAction();
				action.setParameters(ViewAttribute.Operation.toString(), FilterViewEvent.AddFilter.toString());
				action.setParameters(ViewAttribute.OperationString1.toString(), strFilterColumn);
				action.setParameters(ViewAttribute.OperationString2.toString(), strFilterValueSet1);
				eventBus.fireEventFromSource(action, this);
				
			} else if ( element.equals(strSet0) ) {
				
				// Set Filter 
				// Value as 0

				UIEventAction action = new UIEventAction();
				action.setParameters(ViewAttribute.Operation.toString(), FilterViewEvent.AddFilter.toString());
				action.setParameters(ViewAttribute.OperationString1.toString(), strFilterColumn);
				action.setParameters(ViewAttribute.OperationString2.toString(), strFilterValueSet0);
				eventBus.fireEventFromSource(action, this);
				
			} else if ( element.equals(strClear) ) {
				
				// Clean Filter 

				UIEventAction action = new UIEventAction();
				action.setParameters(ViewAttribute.Operation.toString(), FilterViewEvent.RemoveFilter.toString());
				eventBus.fireEventFromSource(action, this);
				
				
				if ( null != widgetClear ) uiWidgetGeneric.setWidgetStatus(strClear, WidgetStatus.Disable);
			}
		} else {
			logger.error(className, function, "element IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private void onWidgetEvent (Widget widget) {
		final String function = "onWidgetEvent";
		if ( null != widget ) {
			String element = uiWidgetGeneric.getWidgetElement(widget);
			setFilter(element);
		} else {
			logger.error(className, function, "widget IS NULL");
		}
	}
	
	private void onButtonValueChange(ValueChangeEvent<String> event) {
		final String function = "onButtonValueChange";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.error(className, function, "event IS NULL");
		}
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.error(className, function, "event IS NULL");
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
		
		logger.error(className, function, "op["+op+"]");
		logger.error(className, function, "od1["+od1+"]");
		logger.error(className, function, "od2["+od2+"]");
		logger.error(className, function, "od3["+od3+"]");
		
		if ( null != op ) {

			// Filter Action
			if ( op.equals(ViewerViewEvent.FilterAdded.toString()) ) {

				uiWidgetGeneric.setWidgetStatus(strClear, WidgetStatus.Down);
				uiWidgetGeneric.setWidgetStatus(strApplied, WidgetStatus.Down);
				
			} else if ( op.equals(ViewerViewEvent.FilterRemoved.toString()) ) {

				uiWidgetGeneric.setWidgetStatus(strClear, WidgetStatus.Disable);
				uiWidgetGeneric.setWidgetStatus(strApplied, WidgetStatus.Up);
				
			} else if ( op.equals(SummaryViewEvent.SetDefaultFilter.toString()) ) {
				
				Widget widget = uiWidgetGeneric.getWidget(strSet1);
				if ( null != widget ) {
					((RadioButton)widget).setValue(true);
				} else {
					logger.error(className, function, "Widget strSet1[{}] IS NULL", strSet1);
				}

				setFilter(strSet1);

			} else {
				logger.error(className, function, "ViewerViewEvent type IS UNKNOW");
			}

		} else {
			logger.error(className, function, "op IS NULL");
		}
		
		logger.end(className, function);
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.info(className, function, "init Begin");
		
		if ( containsParameterKey(ParameterName.SimpleEventBus.toString()) ) {
			Object o = parameters.get(ParameterName.SimpleEventBus.toString());
			if ( null != o ) {
				String eventBusName = (String) o;
				this.eventBus = UIEventActionBus.getInstance().getEventBus(eventBusName);
			}
		}

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setXMLFile(xmlFile);
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
