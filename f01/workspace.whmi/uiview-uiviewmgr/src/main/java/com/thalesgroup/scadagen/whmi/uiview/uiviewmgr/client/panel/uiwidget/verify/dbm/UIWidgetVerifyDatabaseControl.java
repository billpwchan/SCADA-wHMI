package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dbm;

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
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.engine.wrapper.Database;

public class UIWidgetVerifyDatabaseControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
//	private String strDatabase = "Database";
//	
//	private Subject getSubject() {
//		final String function = "getSubject";
//		
//		logger.begin(className, function);
//		
//		Subject subject = new Subject();
//		Observer observer = new Observer() {
//
//			@Override
//			public void setSubject(Subject subject) {
//				this.subject = subject;	
//				this.subject.attach(this);
//			}
//
//			@Override
//			public void update() {
//				logger.debug(className, function, "update");
//				JSONObject obj = this.subject.getState();
//				uiGeneric.setWidgetValue("resultvalue", obj.toString());
//			}
//			
//		};
//		observer.setSubject(subject);
//
//		logger.end(className, function);
//		
//		return subject;
//	}
	
	private void writeDataValueRequest() {
		final String function = "writeDataValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("address");
		long second			= Long.parseLong(uiGeneric.getWidgetValue("second"));
		long usecond		= Long.parseLong(uiGeneric.getWidgetValue("usecond"));
		
		Database database = new Database();
		database.connect();
		
		database.writeDateValueRequest(key, scsEnvId, address, second, usecond);
		
		logger.end(className, function);
	}
	
	private void writeIntValueRequest() {
		final String function = "write";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("address");
		int value			= Integer.parseInt(uiGeneric.getWidgetValue("value"));
		
		Database database = new Database();
		database.connect();
		database.writeIntValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeFloatValueRequest() {
		final String function = "writeFloatValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("address");
		float value			= Float.parseFloat(uiGeneric.getWidgetValue("value"));
		
		Database database = new Database();
		database.connect();
		database.writeFloatValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeStringValueRequest() {
		final String function = "writeStringValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("address");
		String value		= uiGeneric.getWidgetValue("value");
		
		Database database = new Database();
		database.connect();
		database.writeStringValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void writeValueRequest() {		
		final String function = "writeValueRequest";
		logger.begin(className, function);
		
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String address		= uiGeneric.getWidgetValue("address");
		String value		= uiGeneric.getWidgetValue("value");
		
		Database database = new Database();
		database.connect();
		database.writeValueRequest(key, scsEnvId, address, value);
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		final String function = "launch";
		logger.begin(className, function);
		
		logger.debug(className, function, "element[{}]", element);
		
		element = element.toLowerCase();
		
		if ( "writeDataValueRequest".toLowerCase().equals(element) ) {
			writeDataValueRequest();
		}
		else if ( "writeIntValueRequest".toLowerCase().equals(element) ) {
			writeIntValueRequest();
		}
		else if ( "writeFloatValueRequest".toLowerCase().equals(element) ) {
			writeFloatValueRequest();
		}
		else if ( "writeStringValueRequest".toLowerCase().equals(element) ) {
			writeStringValueRequest();
		}
		else if ( "writeValueRequest".toLowerCase().equals(element) ) {
			writeValueRequest();
		}
		logger.end(className, function);
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
