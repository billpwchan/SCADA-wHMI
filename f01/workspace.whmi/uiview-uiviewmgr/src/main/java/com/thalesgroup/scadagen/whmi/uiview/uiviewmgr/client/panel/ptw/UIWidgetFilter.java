package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
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
	
	private Logger logger = Logger.getLogger(UIWidgetFilter.class.getName());
	
	private String className		= UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName());
	
	private String logPrefix		= "["+className+"] ";
	
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
			logger.log(Level.SEVERE, logPrefix+"onWidgetEvent element IS NULL");
		}
	}
	
	private void onWidgetEvent (Widget widget) {
		if ( null != widget ) {
			String element = uiWidgetGeneric.getWidgetElement(widget);
			setFilter(element);
		} else {
			logger.log(Level.SEVERE, logPrefix+"onWidgetEvent widget IS NULL");
		}
	}
	
	private void onButtonValueChange(ValueChangeEvent<String> event) {
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.log(Level.SEVERE, logPrefix+"onButtonValueChange event IS NULL");
		}
	}
	
	private void onButton(ClickEvent event) {
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.log(Level.SEVERE, logPrefix+"onButton event IS NULL");
		}
	}
		
	void onUIEvent(UIEvent uiEvent ) {
	}
		
	void onActionReceived(UIEventAction uiEventAction) {
		
		String op	= (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
		String od1	= (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
		String od2	= (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
		String od3	= (String) uiEventAction.getAction(ViewAttribute.OperationString3.toString());
		
		logger.log(Level.SEVERE, logPrefix+"onActionReceived op["+op+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od1["+od1+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od2["+od2+"]");
		logger.log(Level.SEVERE, logPrefix+"onActionReceived od3["+od3+"]");
		
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
					logger.log(Level.SEVERE, logPrefix+"onActionReceived Widget strSet1["+strSet1+"] IS NULL");
				}

				setFilter(strSet1);

			} else {
				logger.log(Level.SEVERE, logPrefix+"onActionReceived ViewerViewEvent type IS UNKNOW");
			}

		} else {
			logger.log(Level.SEVERE, logPrefix+"onActionReceived op IS NULL");
		}
		
	}

	@Override
	public void init() {
		
		logger.log(Level.FINE, logPrefix+"init Begin");
		
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
		
		logger.log(Level.FINE, logPrefix+"init End");
	}

}
