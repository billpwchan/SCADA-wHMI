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
	
	private void checkAccesses() {
		final String function = "checkAccesses";
		logger.begin(className, function);
		
		String uiopmapivalue	= uiGeneric.getWidgetValue("uiopmapivalue");
		
		String name1value	= uiGeneric.getWidgetValue("name1value");
		String value1value	= uiGeneric.getWidgetValue("value1value");
		
		String name2value	= uiGeneric.getWidgetValue("name2value");
		String value2value	= uiGeneric.getWidgetValue("value2value");
		
		String name3value	= uiGeneric.getWidgetValue("name3value");
		String value3value	= uiGeneric.getWidgetValue("value3value");
		
		String name4value	= uiGeneric.getWidgetValue("name4value");
		String value4value	= uiGeneric.getWidgetValue("value4value");
		
		UIOpm_i uiOpm_i = OpmMgr.getInstance().getOpm(uiopmapivalue);
		
		boolean result = uiOpm_i.checkAccess(
				name1value, value1value
				, name2value, value2value
				, name3value, value3value
				, name4value, value4value
				);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( 0 == "checkAccess".compareToIgnoreCase(element) ) {
			checkAccess();
		}
		else if ( 0 == "checkAccesses".compareToIgnoreCase(element) ) {
			checkAccesses();
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
