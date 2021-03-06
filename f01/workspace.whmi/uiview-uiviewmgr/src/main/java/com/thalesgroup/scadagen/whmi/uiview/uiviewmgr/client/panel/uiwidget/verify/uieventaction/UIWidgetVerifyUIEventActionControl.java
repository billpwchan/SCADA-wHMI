package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.UIWidgetVerifyUIEventActionControl_i.SubjectAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.common.Mgr_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory.MgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;

public class UIWidgetVerifyUIEventActionControl extends UIWidgetRealize {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private Subject getSubject() {
		final String function = "getSubject";
		
		logger.begin(function);
		
		Subject subject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "update");
				JSONObject obj = this.subject.getState();
				uiGeneric.setWidgetValue("resultvalue", obj.toString());
			}
			
		};
		observer.setSubject(subject);

		logger.end(function);
		
		return subject;
	}
	
	private void executeAction(String element) {
		final String function = "executeAction";
		logger.begin(function);
		
		logger.debug(function, "element[{}]", element);
		
		if ( null != element ) {
			
			final String execute = "execute";

			if ( 0 == execute.compareTo(element) ) {

				UIEventAction uiEventAction = new UIEventAction();
				Map<String, Map<String, Object>> override = null;
				
				for ( String strActionEventAttribute : UIActionEventAttribute.toStrings() ) {
					String actionEventAttribute		= uiGeneric.getWidgetValue(strActionEventAttribute);
					logger.debug(function, "strActionEventAttribute[{}] actionEventAttribute[{}]", strActionEventAttribute, actionEventAttribute);
					
					if ( null != actionEventAttribute ) {
						uiEventAction.setParameter(strActionEventAttribute, actionEventAttribute);
					}
				}
				
				for ( String strActionAttribute : ActionAttribute.toStrings() ) {
					String actionAttribute		= uiGeneric.getWidgetValue(strActionAttribute);
					logger.debug(function, "actionAttribute[{}]", actionAttribute);
					
					if ( null != actionAttribute ) {
						uiEventAction.setParameter(strActionAttribute, actionAttribute);
					}
				}
				
				String strSubjectAttribute1 = uiGeneric.getWidgetValue(SubjectAttribute.SubjectAttribute1.toString());
				String strSubjectAttribute2 = uiGeneric.getWidgetValue(SubjectAttribute.SubjectAttribute2.toString());
				String strSubjectAttribute3 = uiGeneric.getWidgetValue(SubjectAttribute.SubjectAttribute3.toString());
				
				logger.debug(function, "strSubjectAttribute1[{}]", strSubjectAttribute1);
				logger.debug(function, "strSubjectAttribute2[{}]", strSubjectAttribute2);
				logger.debug(function, "strSubjectAttribute3[{}]", strSubjectAttribute3);
				
				if ( null != strSubjectAttribute1 && null != strSubjectAttribute2 && null != strSubjectAttribute3 ) {
					MgrFactory mgrFactory = MgrFactory.getInstance();
					Mgr_i mgr = mgrFactory.getMgr(strSubjectAttribute1, strSubjectAttribute2);
					if ( null != mgr ) {
						mgr.setSubject(strSubjectAttribute3, getSubject());
					} else {
						logger.warn(function, "mgr IS NULL");
					}
				}
				
				if ( null != uiEventActionProcessor_i ) {
					uiEventActionProcessor_i.executeAction(uiEventAction, override);
				} else {
					logger.warn(function, "uiEventActionExecute IS NULL");
				}
				
			} else {
				logger.warn(function, "element[{}] for operation IS INVALID ");
			}
		}
		logger.end(function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(function);
		
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
				logger.begin(function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						
						logger.debug(function, "element[{}]", element);

						executeAction(element);
					}
				}
				logger.end(function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i#onActionReceived(com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction)
			 */
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				logger.beginEnd(function);
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
				logger.beginEnd(function);
			}
		
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#envUp(java.lang.String)
			 */
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#envDown(java.lang.String)
			 */
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(function);
			}
			
			/*
			 * (non-Javadoc)
			 * @see com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i#terminate()
			 */
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(function);
				envDown(null);
				logger.end(function);
			};
		};

		logger.end(function);
	}
}
