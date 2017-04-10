package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import com.google.gwt.event.dom.client.ClickEvent;
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
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.CheckAccessWithHOMEvent_i;

public class UIWidgetVerifyOPMCheckAccess extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMCheckAccess.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void checkAccess() {
		final String function = "checkAccess";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		boolean result = uiOpm_i.checkAccess(functionvalue, locationvalue, actionvalue, modevalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
		
		logger.end(className, function);
	}
	
	private void checkAccessWithProfile() {
		final String function = "checkAccessWithProfileName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithProfileName(functionvalue, locationvalue, actionvalue, modevalue, hvid, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHostName() {
		final String function = "checkAccessWithHostName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHostName(functionvalue, locationvalue, actionvalue, modevalue, hvid, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHom() {
		final String function = "checkAccessWithHom";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hvid				= uiGeneric.getWidgetValue("hvidvalue");
		
		String keyvalue			= uiGeneric.getWidgetValue("keyvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHom(functionvalue, locationvalue, actionvalue, modevalue, hvid, keyvalue, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "checkaccess".equals(element) ) {
			checkAccess();
		} else if ( "checkaccesswithprofilename".equals(element) ) {
			checkAccessWithProfile();
		} else if ( "checkaccesswithhostname".equals(element) ) {
			checkAccessWithHostName();
		} else if ( "checkaccesswithhom".equals(element) ) {
			checkAccessWithHom();
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
						logger.debug(className, function, "element[{}]", element);
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
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}

}
