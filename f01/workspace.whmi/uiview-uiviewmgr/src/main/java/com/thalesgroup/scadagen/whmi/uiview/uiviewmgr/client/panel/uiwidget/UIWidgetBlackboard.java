package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetBlackboard extends UIWidgetRealize {

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
				// TODO Auto-generated method stub

			}

			@Override
			public void onActionReceived(final UIEventAction uiEventActionReceived) {
				final String function = "onActionReceived";

				logger.begin(function);

				if (null != uiEventActionReceived) {

					String oe = (String) uiEventActionReceived.getParameter(UIActionEventTargetAttribute.OperationElement.toString());

					logger.info(function, "oe[" + oe + "]");

					if (null != oe) {
						
						if (oe.equals(element)) {

							String os1 = (String) uiEventActionReceived.getParameter(ActionAttribute.OperationString1.toString());
							logger.info(function, "os1[" + os1 + "]");
							
							String os1s[] = new String[]{"SetWidgetValue", "SetWidgetStatus"};
							
							Map<String, Map<String, Object>> override = null;
							
							for ( String s : os1s ) {
								
								logger.info(function, "s[{}].equals(os1[{}])", s, os1);
								
								if ( s.equals(os1) ) {
									
									override = new HashMap<String, Map<String, Object>>();
									
									Map<String, Object> parameters = new HashMap<String, Object>();
									
									logger.info(function, "os1[" + os1 + "]");
									
									Object obj1 = uiEventActionReceived.getParameter(ActionAttribute.OperationString2.toString());
									Object obj2 = uiEventActionReceived.getParameter(ActionAttribute.OperationString3.toString());
									
									logger.info(function, "update Counter Values");
									
									logger.info(function, "obj1[{}]", obj1);
									logger.info(function, "obj2[{}]", obj2);
									
									if ( null != obj1 && null != obj2 ) {
										
										String key = obj1.toString();
										String value = obj2.toString();
										
										logger.info(function, "key[{}]", key);
										logger.info(function, "value[{}]", value);
										
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
