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
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.GetCurrentHOMValueEvent_i;

public class UIWidgetVerifyOPMHom extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMHom.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void isHOMAction() {
		final String function = "isHOMAction";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");

		String actionvalue	= uiGeneric.getWidgetValue("actionvalue");
	
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		boolean result = uiOpm_i.isHOMAction(actionvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));

		logger.end(className, function);
	}
	
	private void isBypassValue() {
		final String function = "isByPassValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");

		String hdvvaluevalue	= uiGeneric.getWidgetValue("hdvvalue");
		
		int hdvvalue = Integer.parseInt(hdvvaluevalue);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		boolean result = uiOpm_i.isBypassValue(hdvvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));

		logger.end(className, function);
	}

	private void checkHom() {
		final String function = "checkHom";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");

		String hdvvaluevalue	= uiGeneric.getWidgetValue("hdvvaluevalue");
		
		String identityvalue	= uiGeneric.getWidgetValue("identityvalue");
		
		int hdvvalue = Integer.parseInt(hdvvaluevalue);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);

		boolean result = uiOpm_i.checkHom(hdvvalue, identityvalue);
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));

		logger.end(className, function);
	}

	private void getCurrentHOMValue() {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");

		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
	
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.getCurrentHOMValue(scsenvidvalue, dbaddressvalue, new GetCurrentHOMValueEvent_i() {
			
			@Override
			public void update(String dbaddress, int value) {
				uiGeneric.setWidgetValue("resultvalue", String.valueOf(value));
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
		
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHom(functionvalue, locationvalue, actionvalue, modevalue, scsenvidvalue, dbaddressvalue, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void checkAccessWithHomHdvValue() {
		final String function = "checkAccessWithHomHdvValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String functionvalue	= uiGeneric.getWidgetValue("functionvalue");
		String locationvalue	= uiGeneric.getWidgetValue("locationvalue");
		String actionvalue		= uiGeneric.getWidgetValue("actionvalue");
		String modevalue		= uiGeneric.getWidgetValue("modevalue");
		
		String hdvvaluevalue	= uiGeneric.getWidgetValue("hdvvaluevalue");
		String identityvalue	= uiGeneric.getWidgetValue("identityvalue");
		
		int hdvvalue = Integer.parseInt(hdvvaluevalue);
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		uiOpm_i.checkAccessWithHom(functionvalue, locationvalue, actionvalue, modevalue, hdvvalue, identityvalue, new CheckAccessWithHOMEvent_i() {
			
			@Override
			public void result(boolean result) {
				uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
			}
		});
		logger.end(className, function);
	}
	
	private void launch(String element) {
		
		if ( 0 == "isHOMAction".compareToIgnoreCase(element) ) {
			isHOMAction();
		}
		else if ( 0 == "isBypassValue".compareToIgnoreCase(element) ) {
			isBypassValue();
		}
		else if ( 0 == "getCurrentHOMValue".compareToIgnoreCase(element) ) {
			getCurrentHOMValue();
		}
		else if ( 0 == "checkHom".compareToIgnoreCase(element) ) {
			checkHom();
		}
		else if ( 0 == "checkAccessWithHom".compareToIgnoreCase(element) ) {
			checkAccessWithHom();
		}
		else if ( 0 == "checkAccessWithHomHdvValue".compareToIgnoreCase(element) ) {
			checkAccessWithHomHdvValue();
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
				logger.end(className, function);
			};
		};

		logger.end(className, function);
	}

}
