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
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i.GetCurrentHOMValueEvent_i;

public class UIWidgetVerifyOPMGetInfo extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMGetInfo.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void getCurrentHostName() {
		final String function = "getCurrentHostName";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(className, function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentHostName();
		} else {
			logger.warn(className, function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void getCurrentIPAddress() {
		final String function = "getCurrentIPAddress";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		logger.debug(className, function, "uiopmapivalue[{}]",uiopmapivalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		String result = null;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getCurrentIPAddress();
		} else {
			logger.warn(className, function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void getCurrentHOMValue() {
		final String function = "getCurrentHOMValue";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String aliasvalue		= uiGeneric.getWidgetValue("aliasvalue");
		
		logger.debug(className, function, "uiopmapivalue[{}]",uiopmapivalue);
		logger.debug(className, function, "scsenvidvalue[{}]", scsenvidvalue);
		logger.debug(className, function, "hvidvalue[{}]", aliasvalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		if ( null != uiOpm_i ) {
			uiOpm_i.getCurrentHOMValue(scsenvidvalue, aliasvalue, new GetCurrentHOMValueEvent_i() {
				
				@Override
				public void update(final String dbaddress, final int value) {
					
					String result = Integer.toString(value);
					logger.debug(className, function, "result[{}]", result);
					
					uiGeneric.setWidgetValue("resultvalue", result);
				}
			});
		} else {
			logger.warn(className, function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}

		logger.end(className, function);
	}
	
	private void getConfigHOMMask() {
		final String function = "getConfigHOMMask";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		String keyvalue			= uiGeneric.getWidgetValue("keyvalue");
		
		logger.debug(className, function, "uiopmapivalue[{}]",uiopmapivalue);
		logger.debug(className, function, "keyvalue[{}]", keyvalue);
		
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
		
		int result = -1;
		if ( null != uiOpm_i ) {
			result = uiOpm_i.getConfigHOMMask(keyvalue);
		} else {
			logger.warn(className, function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
		}
		
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
		logger.end(className, function);
	}
	
//	private void getHOMLevelDefaultValue() {
//		final String function = "getHOMLevelDefaultValue";
//		logger.begin(className, function);
//		
//		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
//		
//		logger.debug(className, function, "uiopmapivalue[{}]",uiopmapivalue);
//		
//		OpmMgr opmMgr = OpmMgr.getInstance();
//		UIOpm_i uiOpm_i = opmMgr.getOpm(uiopmapivalue);
//		
//		int result = -1;
//		if ( null != uiOpm_i ) {
//			result = uiOpm_i.getHOMLevelDefaultValue();
//		} else {
//			logger.warn(className, function, "uiopmapivalue[{}] uiOpm_i IS NULL", uiopmapivalue);
//		}
//		
//		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
//		logger.end(className, function);
//	}
	
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
//		else if ( "gethomleveldefaultvalue".equals(element) ) {
//			getHOMLevelDefaultValue();
//		}
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
