package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dictionaries;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UILayoutRealize;

public class UIWidgetVerifyDictionariesCachedJSON extends UILayoutRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDictionariesCachedJSON.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void readString() {
		final String function = "readString";
		logger.begin(className, function);
		String funKey = function.toLowerCase();
		
		String dictionariesCacheName	= uiGeneric.getWidgetValue("dictionariescache_"+funKey+"_value");
		String filenamevalue			= uiGeneric.getWidgetValue("filename_"+funKey+"_value");
		String keyvalue					= uiGeneric.getWidgetValue("key_"+funKey+"_value");
		String defaultvalue				= uiGeneric.getWidgetValue("default_"+funKey+"_value");
		
		String result = ReadJson.readString(dictionariesCacheName, filenamevalue, keyvalue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void readInt() {
		final String function = "readInt";
		logger.begin(className, function);
		String funKey = function.toLowerCase();
		
		String dictionariesCacheName	= uiGeneric.getWidgetValue("dictionariescache_"+funKey+"_value");
		String filenamevalue			= uiGeneric.getWidgetValue("filename_"+funKey+"_value");
		String keyvalue					= uiGeneric.getWidgetValue("key_"+funKey+"_value");
		String defaultvalue				= uiGeneric.getWidgetValue("default_"+funKey+"_value");
		
		String result = ReadJson.readString(dictionariesCacheName, filenamevalue, keyvalue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", result);
		logger.end(className, function);
	}
	
	private void launch(String element) {
		if ( "readstring".equals(element) ) {
			readString();
		} else if ( "readInt".equals(element) ) {
			readInt();
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
