package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.ols;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.mgrfactory.MgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ols.OlsMgr;

public class UIWidgetVerifyOLSControl extends UIWidgetRealize {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private String strOlsMgr = "OlsMgr";
	
	private Subject getSubject() {
		final String function = "getSubject";
		
		logger.begin(function);
		
		Subject subject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "update");
				JSONObject obj = this.subject.getState();
				uiGeneric.setWidgetValue("resultvalue", obj.toString());
			}
			
		};
		observer.setSubject(subject);

		logger.end(function);
		
		return subject;
	}
	
	private void deleteData() {
		final String function = "deleteData";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String listName		= uiGeneric.getWidgetValue("listnamevalue");
		String keyList		= uiGeneric.getWidgetValue("keylistvalue");
		
		String [] keyLists = keyList.split(",");

		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.deleteData(key, scsEnvId, listServer, listName, keyLists);
		
		logger.end(function);
	}

	private void subscribeOlsList() {
		final String function = "subscribeOlsList";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String listName		= uiGeneric.getWidgetValue("listnamevalue");
		String fieldList	= uiGeneric.getWidgetValue("fieldlistvalue");
		String filter		= uiGeneric.getWidgetValue("filtervalue");
		
		String [] fieldLists = fieldList.split(",");
		
		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.subscribeOlsList(key, scsEnvId, listServer, listName, fieldLists, filter);
		
		logger.end(function);
	}

	private void unsubscribeOlsList() {
		final String function = "unsubscribeOlsList";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String subUUID		= uiGeneric.getWidgetValue("subuuidvalue");
		
		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.unsubscribeOlsList(key, scsEnvId, listServer, subUUID);
		
		logger.end(function);
	}

	private void readData() {
		final String function = "readData";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String listName		= uiGeneric.getWidgetValue("listnamevalue");
		String fieldList	= uiGeneric.getWidgetValue("fieldlistvalue");
		String filter		= uiGeneric.getWidgetValue("filtervalue");
		
		String [] fieldLists = fieldList.split("|");
		
		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.readData(key, scsEnvId, listServer, listName, fieldLists, filter);
		
		logger.end(function);
	}

	private void insertData() {
		final String function = "insertData";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String listName		= uiGeneric.getWidgetValue("listnamevalue");
		String strOlsEntry	= uiGeneric.getWidgetValue("olsentryvalue");
		
		String [] strOlsEntrys = strOlsEntry.split("|");
		Map<String, String> olsEntry = new LinkedHashMap<String, String>();
		for ( String entry : strOlsEntrys ) {
			String [] keyValue = entry.split(",");
			olsEntry.put(keyValue[0], keyValue[1]);
		}
		
		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.insertData(key, scsEnvId, listServer, listName, olsEntry);
		
		logger.end(function);
	}

	private void updateData() {
		final String function = "updateData";
		logger.begin(function);
		
		String olskey		= uiGeneric.getWidgetValue("olskeyvalue");
		String key			= uiGeneric.getWidgetValue("keyvalue");
		String scsEnvId		= uiGeneric.getWidgetValue("scsenvidvalue");
		String listServer	= uiGeneric.getWidgetValue("listservervalue");
		String listName		= uiGeneric.getWidgetValue("listnamevalue");
		String olsEntryKey	= uiGeneric.getWidgetValue("olsentrykeyvalue");
		String strOlsEntry	= uiGeneric.getWidgetValue("olsentryvalue");
	
		String [] strOlsEntrys = strOlsEntry.split("|");
		Map<String, String> olsEntry = new LinkedHashMap<String, String>();
		for ( String entry : strOlsEntrys ) {
			String [] keyValue = entry.split(",");
			olsEntry.put(keyValue[0], keyValue[1]);
		}
		
		OlsMgr olsMgr = (OlsMgr) MgrFactory.getInstance().getMgr(strOlsMgr, olskey);
		
		olsMgr.setSubject(className + function, getSubject());
		
		olsMgr.updateData(key, scsEnvId, listServer, listName, olsEntryKey, olsEntry);
		
		logger.end(function);
	}
	
	private void launch(String element) {

		if ( "deletedata".equals(element) ) {
			deleteData();
		} else if ( "subscribeolslist".equals(element) ) {
			subscribeOlsList();
		} else if ( "unsubscribeolslist".equals(element) ) {
			unsubscribeOlsList();
		} else if ( "readdata".equals(element) ) {
			readData();
		} else if ( "insertdata".equals(element) ) {
			insertData();
		} else if ( "updatedata".equals(element) ) {
			updateData();
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
