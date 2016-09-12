package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint_i.PrintViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetPrint extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetPrint.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 		= null;
	
	final String strPrint					= "print";
	
	final String strViewOnly				= "viewonly";
	final String strGlobal					= "global";
	
//	private Widget widgetPrint				= null;
	
	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	@Override
	public Widget getWidget(String element) {
		Widget widget = null;
		widget = uiWidgetGeneric.getWidget(element);
		return widget;
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
				if ( null != element ) {

					if ( element.equals(strPrint) ) {
						
						// Set Filter 
						// Value as 1

						UIEventAction action = new UIEventAction();
						action.setParameters(ViewAttribute.Operation.toString(), PrintViewEvent.Print.toString());
						action.setParameters(ViewAttribute.OperationString1.toString(), strViewOnly);
						eventBus.fireEventFromSource(action, this);
						
					}

				} else {
					
				}
			}
		} else {
			logger.warn(className, function, "button IS NULL");
		}
		
		logger.end(className, function);
	}
		
	void onUIEvent(UIEvent uiEvent ) {
	}
		
	void onActionReceived(UIEventAction uiEventAction) {
		
//		String op	= (String) uiEventAction.getAction(ViewAttribute.Operation.toString());
//		String od1	= (String) uiEventAction.getAction(ViewAttribute.OperationString1.toString());
//		String od2	= (String) uiEventAction.getAction(ViewAttribute.OperationString2.toString());
//		String od3	= (String) uiEventAction.getAction(ViewAttribute.OperationString3.toString());
//		
//		logger.info(className, function, "onActionReceived op["+op+"]");
//		logger.info(className, function, "onActionReceived od1["+od1+"]");
//		logger.info(className, function, "onActionReceived od2["+od2+"]");
//		logger.info(className, function, "onActionReceived od3["+od3+"]");
//		
//		if ( null != op ) {
//			
//			WidgetStatus statusClear		= null;
//			
//			// Filter Action
//			if ( op.equals(View_i.ViewerViewEvent.FilterAdded.toString()) ) {
//				
//				statusClear		= WidgetStatus.Down;
//				
//			} else if ( op.equals(View_i.ViewerViewEvent.FilterRemoved.toString()) ) {
//				
//				statusClear		= WidgetStatus.Disable;
//				
//			} else {
//				logger.warn(className, function, "onActionReceived ViewerViewEvent type IS UNKNOW");
//			}
//
//			if ( null != widgetClear && null != statusClear ) 	uiWidgetGeneric.setWidgetStatus(strClear, statusClear);
//		}
		
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(WidgetParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
//		widgetPrint	= uiWidgetGeneric.getWidget( strPrint );
		
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

//		uiWidgetGeneric.setWidgetStatus( strSet1,  WidgetStatus.Down );
//		uiWidgetGeneric.setWidgetStatus( strClear,  WidgetStatus.Disable );
		
		logger.end(className, function);
	}

}
