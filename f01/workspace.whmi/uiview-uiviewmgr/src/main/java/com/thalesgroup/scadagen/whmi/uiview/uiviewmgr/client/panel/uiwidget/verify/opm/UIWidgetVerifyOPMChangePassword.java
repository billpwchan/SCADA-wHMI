package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIWrapperRpcEvent_i;

public class UIWidgetVerifyOPMChangePassword extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMChangePassword.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void changePassword() {
		final String function = "changePassword";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String usernamevalue	= uiGeneric.getWidgetValue("usernamevalue");
		String oldpassvalue		= uiGeneric.getWidgetValue("oldpassvalue");
		String newpassvalue		= uiGeneric.getWidgetValue("newpassvalue");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		uiOpm_i.changePassword(usernamevalue, oldpassvalue, newpassvalue, new UIWrapperRpcEvent_i() {

			@Override
			public void event(JSONObject jsobject) {
				// TODO Auto-generated method stub
				uiGeneric.setWidgetValue("resultvalue", jsobject.toString());
			}

		});
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "changepassword".equals(element) ) {
			changePassword();
		}
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
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				// TODO Auto-generated method stub
				
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				// TODO Auto-generated method stub
			}
		
			@Override
			public void envUp(String env) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void envDown(String env) {
				// TODO Auto-generated method stub
			}
			
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
