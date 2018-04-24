package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client.loader;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class LoadByKey extends UIWidgetRealize {
	
	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
//		String strUIWidgetGeneric = "UIWidgetGeneric";
//		String strHeader = "header";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
//		if ( null != dictionariesCache ) {
//			valueSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcControl_i.ParameterName.ValueSet.toString(), strHeader);
//		}		
//		logger.info(className, function, "valueSet[{}]", valueSet);
		
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
					final Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						final String element = uiGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							final String actionsetkey = element;
							
							final Map<String, Map<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									final String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.debug(className, function, "os1[{}]", os1);
									
									if ( null != os1 ) {
//										if ( os1.equals("SendDpcInhibitControl") ) {
//									
//										}
									}
									return true;
								}
							});
						}
					}
				}
				logger.begin(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				logger.begin(className, function);
				
				final String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				
				logger.debug(className, function, "os1["+os1+"]");
				
				if ( null != os1 ) {
//					// Filter Action
//					if ( os1.equals(UIWidgetViewer_i.ViewerViewEvent.FilterAdded.toString()) ) {
//						
//						logger.info(className, function, "FilterAdded");
//						
//						uiEventActionProcessor_i.executeActionSet(os1);
//						
//					} else
					{
						// General Case
						final String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.debug(className, function, "oe ["+oe+"]");
						logger.debug(className, function, "os1["+os1+"]");
						
						if ( null != oe ) {
							if ( oe.equals(element) ) {
								uiEventActionProcessor_i.executeActionSet(os1);
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

	public LoadByKey(final String parent) {
		final String f = "LoadByKey";
		logger.begin(className, f);
		
		final String optsXMLFile = parent+"/"+className+".opts.xml";
		logger.debug(className, f, "optsXMLFile[{}]", new Object[]{optsXMLFile});
		
		logger.end(className, f);
	}
	
	public Map<String, Object>[] load(final String profile, final int numOfScreen) {
		final String f = "load";
		logger.begin(className, f);
		
		logger.debug(className, f, "Prepare numOfScreen[{}]", numOfScreen);
		
		Map<String, Object>[] params = null;
		
		if(null!=profile) {
//			params = new Map[numOfScreen];
			
		}
		
		logger.end(className, f);
		return params;
	}
}
