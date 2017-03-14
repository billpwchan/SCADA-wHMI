package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetCSSSelection extends UIWidgetRealize implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelection.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final String function = "onWidgetEvent";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiWidgetGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						String actionsetkey = element;
						uiEventActionProcessor_i.executeActionSet(actionsetkey);
					} else {
						logger.warn(className, function, "widget IS NULL");
					}
				} else {
					logger.warn(className, function, "event IS NULL");
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				
				logger.begin(className, function);
				
				if ( null != uiEventAction ) {
				
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe["+oe+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							
							String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
					
							uiEventActionProcessor_i.executeActionSet(os1);
						}
					}
				} else {
					logger.warn(className, function, "uiEventAction IS NULL");
				}

				logger.end(className, function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(className, function);
			}
		
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};
		
		logger.end(className, function);
	}

}
