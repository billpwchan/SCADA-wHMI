package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;

public class UIWidgetBlackboard extends UIWidgetRealize {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetBlackboard.class.getName());
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

			}

			@Override
			public void onActionReceived(final UIEventAction uiEventActionReceived) {
				final String function = "onActionReceived";

				logger.begin(className, function);

				if (null != uiEventActionReceived) {

					String oe = (String) uiEventActionReceived.getParameter(UIActionEventTargetAttribute.OperationElement.toString());

					logger.info(className, function, "oe[" + oe + "]");

					if (null != oe) {
						
						if (oe.equals(element)) {

							String os1 = (String) uiEventActionReceived.getParameter(ActionAttribute.OperationString1.toString());
							logger.info(className, function, "os1[" + os1 + "]");
							
							String os1s[] = new String[]{"SetWidgetValue", "SetWidgetStatus"};
							
							HashMap<String, HashMap<String, Object>> override = null;
							
							for ( String s : os1s ) {
								
								logger.info(className, function, "s[{}].equals(os1[{}])", s, os1);
								
								if ( s.equals(os1) ) {
									
									override = new HashMap<String, HashMap<String, Object>>();
									
									HashMap<String, Object> parameters = new HashMap<String, Object>();
									
									logger.info(className, function, "os1[" + os1 + "]");
									
									Object obj1 = uiEventActionReceived.getParameter(ActionAttribute.OperationString2.toString());
									Object obj2 = uiEventActionReceived.getParameter(ActionAttribute.OperationString3.toString());
									
									logger.info(className, function, "update Counter Values");
									
									logger.info(className, function, "obj1[{}]", obj1);
									logger.info(className, function, "obj2[{}]", obj2);
									
									if ( null != obj1 && null != obj2 ) {
										
										String key = obj1.toString();
										String value = obj2.toString();
										
										logger.info(className, function, "key[{}]", key);
										logger.info(className, function, "value[{}]", value);
										
										parameters.put(ActionAttribute.OperationString2.toString(), key);
										parameters.put(ActionAttribute.OperationString3.toString(), value);
									}
									
									override.put(s, parameters);
									
								}
							}
							
							uiEventActionProcessor_i.executeActionSet(os1, override);

						}
					}
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
