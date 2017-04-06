package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIWidgetVerifyOPMGetInfo extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMGetInfo.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void getHostName() {
		final String function = "getHostName";
		logger.begin(className, function);
		String funkey = function.toLowerCase();
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapi_"+funkey+"_value");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getCurrentHostName();
		
		uiGeneric.setWidgetValue("result_"+funkey+"_value", result);
		logger.end(className, function);
	}
	
	private void getIPAddress() {
		final String function = "getIPAddress";
		logger.begin(className, function);
		String funkey = function.toLowerCase();
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapi_"+funkey+"_value");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getCurrentIPAddress();
		
		uiGeneric.setWidgetValue("result_"+funkey+"_value", result);
		logger.end(className, function);
	}
	
	private void getCurrentHOMValue() {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		String funkey = function.toLowerCase();
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapi_"+funkey+"_value");
		String hvidvalue	= uiGeneric.getWidgetValue("hvid_"+funkey+"_value");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getCurrentHOMValue(hvidvalue);
		
		uiGeneric.setWidgetValue("result_"+funkey+"_value", result);
		logger.end(className, function);
	}
	
	private void getConfigHOMMask() {
		final String function = "getConfigHOMMask";
		logger.begin(className, function);
		String funkey = function.toLowerCase();
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapi_"+funkey+"_value");
		String keyvalue	= uiGeneric.getWidgetValue("key_"+funkey+"_value");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getConfigHOMMask(keyvalue);
		
		uiGeneric.setWidgetValue("result_"+funkey+"_value", result);
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "gethostname".equals(element) ) {
			getHostName();
		} else if ( "getipaddress".equals(element) ) {
			getIPAddress();
		} else if ( "getconfighommask".equals(element) ) {
			getConfigHOMMask();
		} else if ( "getcurrenthomvalue".equals(element) ) {
			getCurrentHOMValue();
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
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}

}
