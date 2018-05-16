package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.common.GetCurrentIpAddressCallback_i;

public class UIWidgetVerifyOPMGetInfo extends UIWidgetRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private void getCurrentHostName() {
		final String function = "getCurrentHostName";
		logger.begin(function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentHostName();
		} else {
			logger.warn(function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(function);
	}
	
	private void getCurrentIPAddress() {
		final String function = "getCurrentIPAddress";
		logger.begin(function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentIPAddress();
		} else {
			logger.warn(function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(function);
	}
	
	private void getCurrentIPAddressAsync() {
		final String function = "getCurrentIPAddressAsync";
		logger.begin(function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		if ( null != uiOpm_i ) {
			uiOpm_i.getCurrentIPAddress(new GetCurrentIpAddressCallback_i() {
				@Override
				public void callback(String ipAddress) {
					String result = ipAddress;
					uiGeneric.setWidgetValue("resultvalue", result);
				}
			});

		} else {
			logger.warn(function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}

		logger.end(function);
	}
	
	private void getCurrentOperator() {
		final String function = "getCurrentOperator";
		logger.begin(function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentOperator();
		} else {
			logger.warn(function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(function);
	}
	
	private void getCurrentProfile() {
		final String function = "getCurrentProfile";
		logger.begin(function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentProfile();
		} else {
			logger.warn(function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(function);
	}
	
	private void launch(String element) {
		if ( 0 == "getCurrentHostName".compareToIgnoreCase(element) ) {
			getCurrentHostName();
		}
		else if ( 0 == "getCurrentIPAddress".compareToIgnoreCase(element) ) {
			getCurrentIPAddress();
		}
		else if ( 0 == "getCurrentIPAddressAsync".compareToIgnoreCase(element) ) {
			getCurrentIPAddressAsync();
		}
		else if ( 0 == "getCurrentOperator".compareToIgnoreCase(element) ) {
			getCurrentOperator();
		}
		else if ( 0 == "getCurrentProfile".compareToIgnoreCase(element) ) {
			getCurrentProfile();
		}
		
	}

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
				final String function = "onClick";
				logger.begin(function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(function);
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
				logger.begin(function);
				envDown(null);
				logger.end(function);
			};
		};

		logger.end(function);
	}

}
