package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dictionaries;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UILayoutRealize;

public class UIWidgetVerifyDictionariesCachedJSON extends UILayoutRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDictionariesCachedJSON.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void execute(UIEventAction uiEventAction) {
		final String function = "execute";
		logger.begin(className, function);
//		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
//		
//		logger.info(className, function, "os1["+os1+"]");
//
//		HashMap<String, HashMap<String, Object>> override = null;
//		
//		uiEventActionProcessor_i.executeActionSet(os1, override, new UIExecuteActionHandler_i() {
//			
//			@Override
//			public boolean executeHandler(UIEventAction uiEventAction) {
//				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
//				String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
//				String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
//				String os4	= (String) uiEventAction.getParameter(ViewAttribute.OperationString4.toString());
//
//				logger.info(className, function, "os1["+os1+"]");
//				logger.info(className, function, "os2["+os2+"]");
//				logger.info(className, function, "os3["+os3+"]");
//				logger.info(className, function, "os4["+os4+"]");						
//				
//				if ( os1.equals("ModifyCSS") ) {
//					
//					if ( null != os2 && null != os3 && null != os4 ) {
//						String cssElementName = os2;
//						String cssValueName = os3;
//						boolean applyRemove = os4.equals("true");
//						modifyCss(cssElementName, cssValueName, applyRemove);
//					} else {
//						logger.warn(className, function, "os2 IS NULL or os3 IS NULL or os4 IS NULL ");
//					}
//				}
//				return true;
//			}
//		});
		logger.end(className, function);
	}
	
	private void readString() {
		final String function = "readString";
		logger.begin(className, function);
		
		String dictionariesCacheName	= uiGeneric.getWidgetValue("dictionariescache_string_value");
		String filenamevalue			= uiGeneric.getWidgetValue("filename_string_value");
		String keyvalue					= uiGeneric.getWidgetValue("key_string_value");
		String defaultvalue				= uiGeneric.getWidgetValue("default_string_value");
		
		String result = ReadJson.readString(dictionariesCacheName, filenamevalue, keyvalue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void readInt() {
		final String function = "readInt";
		logger.begin(className, function);
		
		String dictionariesCacheName	= uiGeneric.getWidgetValue("dictionariescache_int_value");
		String filenamevalue			= uiGeneric.getWidgetValue("filename_int_value");
		String keyvalue					= uiGeneric.getWidgetValue("key_int_value");
		String defaultvalue				= uiGeneric.getWidgetValue("default_int_value");
		
		String result = ReadJson.readString(dictionariesCacheName, filenamevalue, keyvalue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
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
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							if ( "readstring".equals(element) ) {
								readString();
							} else if ( "readInt".equals(element) ) {
								readInt();
							}
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
//				final String function = "onActionReceived";
//				logger.begin(className, function);
//				if ( null != uiEventAction ) {
//					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
//					logger.info(className, function, "oe["+oe+"]");
//					if ( null != oe ) {
//						if ( oe.equals(element) ) {
//							execute(uiEventAction);
//						}
//					}
//				}
//				logger.end(className, function);
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
	
	@Override
	public boolean fromUILayoutSummaryInit(UIEventAction uiEventAction) {
		execute(uiEventAction);
		return true;
	}
}
