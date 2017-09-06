package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db;

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
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadSingle2SingleResult_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseGetFullPathFactory;

public class UIWidgetVerifyDatabaseGetFullPathControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGetFullPathControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private DatabaseSingle2SingleRead_i databaseSingleRead_i = null;
	
	private void create() {
		final String function = "create";
		logger.begin(className, function);
		String strReadClassName = uiGeneric.getWidgetValue("createvalue");
		logger.info(className, function, "strReadClassName[{}]", strReadClassName);
		databaseSingleRead_i = DatabaseGetFullPathFactory.get(strReadClassName);
		logger.end(className, function);
	}
	
	private void connect() {
		final String function = "connect";
		logger.begin(className, function);
		databaseSingleRead_i.connect();
		logger.end(className, function);
	}
	
	private void disconnect() {
		final String function = "disconnect";
		logger.begin(className, function);
		databaseSingleRead_i.disconnect();
		logger.end(className, function);
	}
	
	private void getFullPath() {
		final String function = "getFullPath";
		logger.begin(className, function);
		
		final String getfullpath = "getfullpath"+"_";
		
		String strkeyvalue 		= getfullpath+"keyvalue";
		String strscsenvidvalue = getfullpath+"scsenvidvalue";
		String straddressvalue 	= getfullpath+"addressvalue";
		
		String keyvalue 		= uiGeneric.getWidgetValue(strkeyvalue);
		String scsenvidvalue 	= uiGeneric.getWidgetValue(strscsenvidvalue);
		String addressvalue 	= uiGeneric.getWidgetValue(straddressvalue);
		
		databaseSingleRead_i.addSingle2SingleRequest(keyvalue, scsenvidvalue, addressvalue, new DatabaseReadSingle2SingleResult_i() {
			
			@Override
			public void update(String key, String value) {
				String result = "Result from key["+key+"]";
				result += " value["+value+"]";
				uiGeneric.setWidgetValue(getfullpath+"resultvalue", result);
			}
		});
	}
	
	private void launch(String element) {
		
		if ( 0 == "create".compareToIgnoreCase(element) ) {
			create();
		}
		else if ( 0 == "connect".compareToIgnoreCase(element) ) {
			connect();
		}
		else if ( 0 == "disconnect".compareToIgnoreCase(element) ) {
			disconnect();
		}
		else if ( 0 == "getFullPath".compareToIgnoreCase(element) ) {
			getFullPath();
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
				if ( null != databaseSingleRead_i ) databaseSingleRead_i.disconnect();
				envDown(null);
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}
}