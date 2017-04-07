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
	
	private void getCurrentHostName() {
		final String function = "getCurrentHostName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getCurrentHostName();
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void getCurrentIPAddress() {
		final String function = "getCurrentIPAddress";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = uiOpm_i.getCurrentIPAddress();
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void getCurrentHOMValue() {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String hvidvalue		= uiGeneric.getWidgetValue("hvidvalue");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		int result = uiOpm_i.getCurrentHOMValue(hvidvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
		logger.end(className, function);
	}
	
	private void getConfigHOMMask() {
		final String function = "getConfigHOMMask";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String keyvalue			= uiGeneric.getWidgetValue("keyvalue");
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		int result = uiOpm_i.getConfigHOMMask(keyvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "getcurrenthostname".equals(element) ) {
			getCurrentHostName();
		} else if ( "getcurrentipaddress".equals(element) ) {
			getCurrentIPAddress();
		} else if ( "getcurrenthomvalue".equals(element) ) {
			getCurrentHOMValue();
		} else if ( "getconfighommask".equals(element) ) {
			getConfigHOMMask();
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
