package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetConfiguration extends UIWidgetRealize {
	
	private String className = UIWidgetUtil.getClassSimpleName(UIWidgetConfiguration.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strSetWidgetValue = "SetWidgetValue";
	
	private final String strSetWidgetStatus = "SetWidgetStatus";
	
	private final String supportOperations[] = new String[]{strSetWidgetValue, strSetWidgetStatus};

	@Override
	public Widget getWidget(String widget) { return uiGeneric.getWidget(widget); }
	@Override
	public String getWidgetElement(Widget widget) { return uiGeneric.getWidgetElement(widget); }
	
	@Override
	public String [] getUIWidgetElements() { return uiGeneric.getUIWidgetElements(); }
	
	@Override
	public void setWidgetValue(String element, String value) { uiGeneric.setWidgetValue(element, value); }
	@Override
	public String getWidgetValue(String element) { return uiGeneric.getWidgetValue(element); }
	
	@Override
	public WidgetStatus getWidgetStatus(String element) { return uiGeneric.getWidgetStatus(element); }
	@Override
	public void setWidgetStatus(String element, WidgetStatus status) { uiGeneric.setWidgetStatus(element, status);}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		className += " [" + element + "] ";
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {

				// Call external handling
				if ( null != ctrlHandler ) ctrlHandler.onUIEvent(uiEvent);
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				
				logger.begin(className, function);
				
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							String actionsetkey = element;
							
							Map<String, Map<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						}
					}
				}
				
				// Call external handling
				if ( null != ctrlHandler ) ctrlHandler.onClick(event);
				
				logger.begin(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventActionReceived) {
				final String function = "onActionReceived";

				logger.begin(className, function);

				if (null != uiEventActionReceived) {

					String oe = (String) uiEventActionReceived.getParameter(UIActionEventTargetAttribute.OperationElement.toString());

					logger.info(className, function, "oe[" + oe + "]");

					if (null != oe) {
						
						if (oe.equals(element)) {

							String os1 = (String) uiEventActionReceived.getParameter(ActionAttribute.OperationString1.toString());
							logger.info(className, function, "os1[" + os1 + "]");
							
							Map<String, Map<String, Object>> override = null;
							
							for ( String actionkey : supportOperations ) {
								
								logger.info(className, function, "operation[{}].equals(os1[{}])", actionkey, os1);
								
								if ( actionkey.equals(os1) ) {
									
									override = new HashMap<String, Map<String, Object>>();
									
									Map<String, Object> parameters = new HashMap<String, Object>();
									
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
									
									override.put(actionkey, parameters);
									
								}
							}
							
							uiEventActionProcessor_i.executeActionSet(os1, override);

						}
					}
				}
				
				// Call external handling
				if ( null != ctrlHandler ) ctrlHandler.onActionReceived(uiEventActionReceived);
				
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
