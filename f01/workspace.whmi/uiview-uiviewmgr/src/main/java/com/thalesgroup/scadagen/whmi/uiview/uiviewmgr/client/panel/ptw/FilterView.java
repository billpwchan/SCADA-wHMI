package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.FilterViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class FilterView extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(FilterView.class.getName());
	
	private String logPrefix				= "[FilterView] ";
	
	private SimpleEventBus eventBus 		= null;
	
	final String strFilterColumn			= "ptwdciset_SL_Cond_Res";
	final String strFilterValueSet1			= "1";
	final String strFilterValueSet0			= "0";

	final String strSet1					= "set1";
	final String strSet0					= "set0";
	final String strClear					= "clear";
	
	private Widget widgetClear				= null;
	
	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	private void onButton(ClickEvent event) {
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
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
						action.setParameters(ViewAttribute.Operation.toString(), View_i.FilterViewEvent.AddFilter.toString());
						action.setParameters(ViewAttribute.OperationString1.toString(), strFilterColumn);
						action.setParameters(ViewAttribute.OperationString2.toString(), strFilterValueSet0);
						eventBus.fireEventFromSource(action, this);
						
					} else if ( element.equals(strClear) ) {
						
						// Clean Filter 

						UIEventAction action = new UIEventAction();
						action.setParameters(ViewAttribute.Operation.toString(), View_i.FilterViewEvent.RemoveFilter.toString());
						eventBus.fireEventFromSource(action, this);
						
						
						if ( null != widgetClear ) uiWidgetGeneric.setWidgetStatus(strClear, WidgetStatus.Disable);
					}

				} else {
					
				}
			}
		} else {
			logger.log(Level.SEVERE, logPrefix+"onClickHandler onClickHandler button IS NULL");
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
			
			WidgetStatus statusClear		= null;
			
			// Filter Action
			if ( op.equals(View_i.ViewerViewEvent.FilterAdded.toString()) ) {
				
				statusClear		= WidgetStatus.Down;
				
			} else if ( op.equals(View_i.ViewerViewEvent.FilterRemoved.toString()) ) {
				
				statusClear		= WidgetStatus.Disable;
				
			} else {
				logger.log(Level.SEVERE, logPrefix+"onActionReceived ViewerViewEvent type IS UNKNOW");
			}

			if ( null != widgetClear && null != statusClear ) 	uiWidgetGeneric.setWidgetStatus(strClear, statusClear);
		}
		
	}

	@Override
	public void init() {
		
		logger.log(Level.FINE, logPrefix+"init Begin");
		
		eventBus = (SimpleEventBus) parameters.get(ParameterName.SimpleEventBus.toString());

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setXMLFile(ViewWidget.UIPanelPTWFilter.toString());
		uiWidgetGeneric.init();
		
		widgetClear	= uiWidgetGeneric.getWidget( strClear );
		
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
