package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetViewerPager extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetViewerPager.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strUnderLine = "_";
	
	private String logPrefix = "";

	@Override
	public void init() {
		super.init();
		
		logPrefix = "element[{}] ";
		
		final String function = "init";
		logger.begin(className, function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
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
							
							HashMap<String, HashMap<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

						}
					}
				}
				logger.begin(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				
				logger.begin(className, function);
				
				String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
				
				logger.info(className, function, logPrefix+"oe[{}] element[{}]", oe, element);
				
				if ( null != oe ) {
					
					if ( oe.equals(element) ) {
						
						String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
						
						logger.info(className, function, logPrefix+"os1[{}]", os1);
						
						if ( null != os1 ) {
							
							String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
							
							logger.info(className, function, logPrefix+"os2[{}]", os2);
						
							if ( os1.equals(UIWidgetViewerPager_i.ParameterName.HasPreviousPage.toString()) ) {
								
								String actionsetkey = os1+strUnderLine+os2;
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
								
							} else if ( os1.equals(UIWidgetViewerPager_i.ParameterName.HasFastBackwardPage.toString()) ) {
								
								String actionsetkey = os1+strUnderLine+os2;
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
								
							} else if ( os1.equals(UIWidgetViewerPager_i.ParameterName.HasFastForwardPage.toString()) ) {
								
								String actionsetkey = os1+strUnderLine+os2;
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
								
							} else if ( os1.equals(UIWidgetViewerPager_i.ParameterName.HasNextPage.toString()) ) {
								
								String actionsetkey = os1+strUnderLine+os2;
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
								
							}
							
							if ( os1.equals(UIWidgetViewerPager_i.ParameterName.PagerValueChanged_PageStart.toString()) ) {
								if ( null != os2 ) {
									int value = 0;
									try {
										value = Integer.parseInt(os2);
									} catch ( NumberFormatException ex ) {
										logger.warn(className, function, logPrefix+"os2[{}] ex[{}]", os2, ex.toString());
									}
									
									String actionsetkey = os1;
									String actionkey = os1;
									
									HashMap<String, Object> parameter = new HashMap<String, Object>();
									parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
									
									HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
									override.put(actionkey, parameter);
									
									uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
								} else {
									logger.warn(className, function, logPrefix+"os2 IS NULL");
								}
								
							} else if ( os1.equals(UIWidgetViewerPager_i.ParameterName.PagerValueChanged_EndIndex.toString()) ) {
								if ( null != os2 ) {
									int value = 0;
									try {
										value = Integer.parseInt(os2);
									} catch ( NumberFormatException ex ) {
										logger.info(className, function, logPrefix+"os2[{}] ex[{}]", os2, ex.toString());
									}
									
									String actionsetkey = os1;
									String actionkey = os1;
									
									HashMap<String, Object> parameter = new HashMap<String, Object>();
									parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
									
									HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
									override.put(actionkey, parameter);
									
									uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
								} else {
									logger.warn(className, function, logPrefix+"os2 IS NULL");
								}
							} else if ( os1.equals(UIWidgetViewerPager_i.ParameterName.PagerValueChanged_Exact.toString()) ) {
								if ( null != os2 ) {
									int value = 0;
									try {
										value = Integer.parseInt(os2);
									} catch ( NumberFormatException ex ) {
										logger.info(className, function, logPrefix+"os2[{}] ex[{}]", os2, ex.toString());
									}
									
									String actionsetkey = os1;
									String actionkey = os1;
									
									HashMap<String, Object> parameter = new HashMap<String, Object>();
									parameter.put(ActionAttribute.OperationString3.toString(), Integer.toString(value));
									
									HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
									override.put(actionkey, parameter);
									
									uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
								} else {
									logger.warn(className, function, logPrefix+"os2 IS NULL");
								}
							}
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
