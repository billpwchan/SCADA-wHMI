package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetVerifyUIEventActionGenericControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionGenericControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private void executeAction(String element) {
		final String function = "executeAction";
		logger.begin(className, function);
		
		logger.debug(className, function, "element[{}]", element);
		
		if ( null != element ) {
			
			final String execute = "execute";

			if ( 0 == execute.compareTo(element) ) {

				String jsdata		= uiGeneric.getWidgetValue("jsondata");
				logger.debug(className, function, "jsdata[{}]", jsdata);

				UIEventAction uiEventAction = new UIEventAction();
				Map<String, Map<String, Object>> override = null;
				
				JSONObject json = ReadJson.readJson(jsdata);
				
				for ( String strActionEventAttribute : UIActionEventAttribute.toStrings() ) {
					String actionEventAttribute = ReadJson.readString(json, strActionEventAttribute, null);
					logger.debug(className, function, "strActionEventAttribute[{}] actionEventAttribute[{}]", strActionEventAttribute, actionEventAttribute);
					
					if ( null != actionEventAttribute ) {
						uiEventAction.setParameter(strActionEventAttribute, actionEventAttribute);
					}
				}
				
				for ( String strActionAttribute : ActionAttribute.toStrings() ) {
					String actionAttribute = ReadJson.readString(json, strActionAttribute, null);
					logger.debug(className, function, "strActionAttribute[{}] actionAttribute[{}]", strActionAttribute, actionAttribute);
					
					if ( null != actionAttribute ) {
						uiEventAction.setParameter(strActionAttribute, actionAttribute);
					}
				}
				
				if ( null != uiEventActionProcessor_i ) {	
					uiEventActionProcessor_i.executeAction(uiEventAction, override);
					
				} else {
					logger.warn(className, function, "uiEventActionExecute IS NULL");
				}

			} else {
				logger.warn(className, function, "element[{}] for operation IS INVALID ");
			}
		}
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i#onUIEvent(com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent)
			 */
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}

			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i#onClick(com.google.gwt.event.dom.client.ClickEvent)
			 */
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						
						logger.debug(className, function, "element[{}]", element);

						executeAction(element);
					}
				}
				logger.end(className, function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i#onActionReceived(com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction)
			 */
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				logger.beginEnd(className, function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#init()
			 */
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(className, function);
			}
		
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#envUp(java.lang.String)
			 */
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(className, function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#envDown(java.lang.String)
			 */
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(className, function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#terminate()
			 */
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.end(className, function);
			};
		};

		logger.end(className, function);
	}
}
