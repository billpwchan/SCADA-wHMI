package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetPrint extends UIWidgetRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());

	// Create Filter Operation Index
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
				final String function = "onButton";
				logger.begin(function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(function, "element["+element+"]");
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
				
				if ( null != uiEventAction ) {
					
					String os1	= (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
					
					logger.debug(function, "os1["+os1+"]");
					
					if ( os1.equals(UIWidgetSimultaneousLoginControl_i.SimultaneousLoginEvent.RowUpdated.toString() ) ) {
						// Activate Selection
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						
						logger.debug(function, "Store Selected Row");
						
						if ( null != obj1 ) {
							
//							storeData((Set<HashMap<String, String>>) obj1);
							
							Set<HashMap<String, String>> rowUpdated = (Set<HashMap<String, String>>) obj1;
							for ( HashMap<String, String> entities : rowUpdated ) {
								if ( null != entities ) {
									for ( Entry<String, String> entity : entities.entrySet() ) {
										String key = entity.getKey();
										String value = entity.getValue();
										
										logger.debug(function, "rowUpdated key[{}] value[{}]", key, value);
									}
								}
							}

						} else {
							logger.warn(function, "obj1 IS NULL");
						}

					}
					
//					if ( os1.equals("FilterAdded") ) {
//						
//						String actionsetkey = "set_filter_added";
//						uiEventActionProcessor_i.executeActionSet(actionsetkey);
//						
//					}
//					else {
//						// General Case
//						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
//						
//						logger.debug(function, "oe ["+oe+"]");
//						logger.debug(function, "os1["+os1+"]");
//						
//						if ( null != oe ) {
//							if ( oe.equals(element) ) {
//								uiEventActionProcessor_i.executeActionSet(os1);
//							}
//						}
//					}
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
