package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetBox extends UIWidgetRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(function);
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				
				logger.begin(function);
				
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(function, "element[{}]", element);
						if ( null != element ) {
							String actionsetkey = element;
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}
					}
				}
				logger.end(function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				
				logger.begin(function);
				
				String targetFunction = "";
				
				String os1	= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
				logger.info(function, "os1["+os1+"]");
				if ( null != os1 ) {
					if ( os1.equals(targetFunction) ) {
					}
				}
				logger.end(function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(function);
			}
		
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(function);
				envDown(null);
				logger.begin(function);
			};
		};

		logger.end(function);
	}

}
