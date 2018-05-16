package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriorityFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i.UIControlPriorityCallback;

public class UIWidgetVerifyControlPriority extends UIWidgetRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private void requestReservation() {
		final String function = "requestReservation";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		uiControlPriority_i.requestReservation(scsenvidvalue, dbaddressvalue, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String value = ReadJson.readString(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, null);
				int code = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_CODE, -1);
				uiGeneric.setWidgetValue("resultvalue", code+"|"+value);
			}
		});
		
		logger.end(function);
	}
	
	private void withdrawReservation() {
		final String function = "withdrawReservation";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		uiControlPriority_i.withdrawReservation(scsenvidvalue, dbaddressvalue, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String value = ReadJson.readString(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, null);
				int code = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_CODE, -1);
				uiGeneric.setWidgetValue("resultvalue", code+"|"+value);
			}
		});

		logger.end(function);
	}
	
	private void getCurrentReservationBy() {
		final String function = "getCurrentReservationBy";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		uiControlPriority_i.getCurrentReservationBy(scsenvidvalue, dbaddressvalue, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				String value = ReadJson.readString(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, null);
				int code = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_CODE, -1);
				uiGeneric.setWidgetValue("resultvalue", code+"|"+value);
			}
		});
		
		logger.end(function);
	}
	
	private void checkReservationLevel() {
		final String function = "checkReservationLevel";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String identityvalue	= uiGeneric.getWidgetValue("identityvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		int result = uiControlPriority_i.checkReservationLevel(identityvalue);
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));

		logger.end(function);
	}
	
	private void checkReservationAvailability() {
		final String function = "checkReservationAvailability";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String identityvalue	= uiGeneric.getWidgetValue("identityvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		int result =  uiControlPriority_i.checkReservationAvailability(identityvalue);
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));

		logger.end(function);
	}
	
	private void checkReservationAvailabilityDbm() {
		final String function = "checkReservationAvailabilityDbm";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		String scsenvidvalue	= uiGeneric.getWidgetValue("scsenvidvalue");
		String dbaddressvalue	= uiGeneric.getWidgetValue("dbaddressvalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		uiControlPriority_i.checkReservationAvailability(scsenvidvalue, dbaddressvalue, new UIControlPriorityCallback() {
			
			@Override
			public void callBack(String strJson) {
				int result = ReadJson.readInt(ReadJson.readJson(strJson), UIControlPriority_i.FIELD_VALUE, -1);
				uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
			}
		});

		logger.end(function);
	}
	
	private void getUsrIdentity() {
		final String function = "getUsrIdentity";
		logger.begin(function);
		
		String uicpapivalue		= uiGeneric.getWidgetValue("uicpapivalue");
		
		UIControlPriority_i uiControlPriority_i = UIControlPriorityFactory.getInstance().get(uicpapivalue);
		
		String result = uiControlPriority_i.getUsrIdentity();
		
		uiGeneric.setWidgetValue("resultvalue", result);

		logger.end(function);
	}	

	private void launch(String element) {
		final String function = "launch";
		logger.begin(function);
		logger.debug(function, "element[{}]", element);
		if ( 0 == "requestReservation".compareToIgnoreCase(element) ) {
			requestReservation();
		} 
		else if ( 0 == "withdrawReservation".compareToIgnoreCase(element) ) {
			withdrawReservation();
		} 
		else if ( 0 == "getCurrentReservationBy".compareToIgnoreCase(element) ) {
			getCurrentReservationBy();
		}
		else if ( 0 == "checkReservationLevel".compareToIgnoreCase(element) ) {
			checkReservationLevel();
		}
		else if ( 0 == "checkReservationAvailability".compareToIgnoreCase(element) ) {
			checkReservationAvailability();
		}
		else if ( 0 == "checkReservationAvailabilityDbm".compareToIgnoreCase(element) ) {
			checkReservationAvailabilityDbm();
		}
		else if ( 0 == "getUsrIdentity".compareToIgnoreCase(element) ) {
			getUsrIdentity();
		}
		logger.end(function);
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
