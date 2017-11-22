package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dictionaries;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJson;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadJsonFile;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetVerifyReadJSON extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyReadJSON.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private void readJson() {
		final String function = "readJson";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		
		JSONObject result = ReadJsonFile.readJson(dictionariesCacheNameValue, fileNameValue);
		
		uiGeneric.setWidgetValue("resultvalue", result.toString());
		
		logger.end(className, function);
	}
	
	private void readString() {
		final String function = "readString";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		String keyValue						= uiGeneric.getWidgetValue("keyvalue");
		String defaultvalueValue			= uiGeneric.getWidgetValue("defaultvaluevalue");
		
		String result = ReadJsonFile.readString(dictionariesCacheNameValue, fileNameValue, keyValue, defaultvalueValue);
		
		uiGeneric.setWidgetValue("resultvalue", result);
		
		logger.end(className, function);
	}
	
	private void readInt() {
		final String function = "readInt";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		String keyValue						= uiGeneric.getWidgetValue("keyvalue");
		String defaultvalueValue			= uiGeneric.getWidgetValue("defaultvaluevalue");
		int defaultvalue = Integer.parseInt(defaultvalueValue);
		
		int result = ReadJsonFile.readInt(dictionariesCacheNameValue, fileNameValue, keyValue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Integer.toString(result));
		
		logger.end(className, function);
	}
	
	private void readBoolean() {
		final String function = "readBoolean";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		String keyValue						= uiGeneric.getWidgetValue("keyvalue");
		String defaultvalueValue			= uiGeneric.getWidgetValue("defaultvaluevalue");
		boolean defaultvalue 				= Boolean.parseBoolean(defaultvalueValue);
		
		boolean result = ReadJsonFile.readBoolean(dictionariesCacheNameValue, fileNameValue, keyValue, defaultvalue);
		
		uiGeneric.setWidgetValue("resultvalue", Boolean.toString(result));
		
		logger.end(className, function);
	}
	
	private void readArray() {
		final String function = "readArray";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		String keyValue						= uiGeneric.getWidgetValue("keyvalue");
		
		JSONArray result = ReadJsonFile.readArray(dictionariesCacheNameValue, fileNameValue, keyValue);
		
		uiGeneric.setWidgetValue("resultvalue", result.toString());
		
		logger.end(className, function);
	}
	
	private void readObjectFromArray() {
		final String function = "readArray";
		logger.begin(className, function);
		
		String dictionariesCacheNameValue	= uiGeneric.getWidgetValue("dictionariescachenamevalue");
		String fileNameValue				= uiGeneric.getWidgetValue("filenamevalue");
		String arrayKeyValue				= uiGeneric.getWidgetValue("arraykeyvalue");
		
		String objKeyValue					= uiGeneric.getWidgetValue("objectkeyvalue");
		String objKeyValueValue				= uiGeneric.getWidgetValue("objectkeyvaluevalue");
		
		JSONArray array = ReadJsonFile.readArray(dictionariesCacheNameValue, fileNameValue, arrayKeyValue);
		JSONObject object = ReadJson.readObject(array, objKeyValue, objKeyValueValue);
		
		uiGeneric.setWidgetValue("resultvalue", object.toString());
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		final String function = "launch";
		logger.begin(className, function);
		
		if ( "readjson".equals(element) ) {
			readJson();
		} else if ( "readstring".equals(element) ) {
			readString();
		} else if ( "readint".equals(element) ) {
			readInt();
		} else if ( "readboolean".equals(element) ) {
			readBoolean();
		} else if ( "readarray".equals(element) ) {
			readArray();
		} else if ( "readobjectfromarray".equals(element) ) {
			readObjectFromArray();
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
