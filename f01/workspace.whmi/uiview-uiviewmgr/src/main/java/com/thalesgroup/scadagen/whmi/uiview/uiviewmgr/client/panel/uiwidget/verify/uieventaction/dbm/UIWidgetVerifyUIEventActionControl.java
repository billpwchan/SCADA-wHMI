package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.dbm;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;

public class UIWidgetVerifyUIEventActionControl extends UIWidgetRealize implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIEventActionExecute_i uiEventActionExecute = null;
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
						String element = uiWidgetGeneric.getWidgetElement(widget);
						
						logger.info(className, function, "element[{}]", element);

						if ( null != element ) {
							
							final String create = "create";
							final String execute = "execute";
							
							if ( 0 == create.compareTo(element) ) {

								String strOperationType = uiWidgetGeneric.getWidgetValue(UIActionEventAttribute.OperationType.toString());
								logger.info(className, function, "strOperationType[{}]", strOperationType);
								
								UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
								uiEventActionExecute = uiEventActionExecuteMgr.getUIEventActionExecute(strOperationType);
								if ( null == uiEventActionExecute ) {
									logger.warn(className, function, "uiEventActionExecute IS NULL");
								}
							} 
							else
							if ( 0 == execute.compareTo(element) ) {
								
								if ( null != uiEventActionExecute ) {
									
									UIEventAction uiEventAction = new UIEventAction();
									HashMap<String, HashMap<String, Object>> override = null;
									
									for ( String strActionAttribute : ActionAttribute.toStrings() ) {
										String strOperationString		= uiWidgetGeneric.getWidgetValue(strActionAttribute);
										logger.info(className, function, "strActionAttribute[{}]", strActionAttribute);
										
										uiEventAction.setParameter(strActionAttribute, strOperationString);
									}
									
									uiEventActionExecute.executeAction(uiEventAction, override);
									
								} else {
									logger.warn(className, function, "uiEventActionExecute IS NULL");
								}
								
							} else {
								logger.warn(className, function, "element[{}] for operation IS INVALID ");
							}
						}
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
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}
}
