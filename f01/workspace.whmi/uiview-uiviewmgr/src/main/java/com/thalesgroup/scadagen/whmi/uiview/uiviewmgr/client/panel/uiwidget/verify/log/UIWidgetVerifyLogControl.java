package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.log;

import java.util.Arrays;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.filter.UILoggerExConfig;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetVerifyLogControl extends UIWidgetRealize {
	
	private final String className = this.getClass().getSimpleName();
	
	private final String STR_FATAL		= "fatal";
	private final String STR_ERROR		= "error";
	private final String STR_WARN		= "warn";
	private final String STR_DEBUG		= "debug";
	private final String STR_INFO		= "info";
	private final String STR_TRACE		= "trace";
	private final String STR_BEGIN		= "begin";
	private final String STR_END		= "end";
	private final String STR_BEGINEND	= "beginend";
	
	private final String [] STR_LEVELS	= {
			STR_FATAL, STR_ERROR, STR_WARN, STR_DEBUG, STR_INFO, STR_TRACE, STR_BEGIN, STR_END, STR_BEGINEND
			};
	
	private final int int_LOG_LEVEL_9 = Arrays.asList(STR_LEVELS).indexOf(STR_FATAL);
	private final int int_LOG_LEVEL_8 = Arrays.asList(STR_LEVELS).indexOf(STR_ERROR);
	private final int int_LOG_LEVEL_7 = Arrays.asList(STR_LEVELS).indexOf(STR_WARN);
	private final int int_LOG_LEVEL_6 = Arrays.asList(STR_LEVELS).indexOf(STR_DEBUG);
	private final int int_LOG_LEVEL_5 = Arrays.asList(STR_LEVELS).indexOf(STR_INFO);
	private final int int_LOG_LEVEL_4 = Arrays.asList(STR_LEVELS).indexOf(STR_TRACE);
	private final int int_LOG_LEVEL_3 = Arrays.asList(STR_LEVELS).indexOf(STR_BEGIN);
	private final int int_LOG_LEVEL_2 = Arrays.asList(STR_LEVELS).indexOf(STR_END);
	private final int int_LOG_LEVEL_1 = Arrays.asList(STR_LEVELS).indexOf(STR_BEGINEND);

	private final String STR_LOG_API_5 = "f, m, new Object[]{p1, p2}";
	private final String STR_LOG_API_4 = "f, m, p1, p2";
	private final String STR_LOG_API_3 = "f, m, p1";
	private final String STR_LOG_API_2 = "f, m";
	private final String STR_LOG_API_1 = "m";
	
	private final int int_LOG_API_5 = 0;
	private final int int_LOG_API_4 = 1;
	private final int int_LOG_API_3 = 2;
	private final int int_LOG_API_2 = 3;
	private final int int_LOG_API_1 = 4;

	private final String [] STR_APIS = {
			STR_LOG_API_5, STR_LOG_API_4, STR_LOG_API_3, STR_LOG_API_2, STR_LOG_API_1
		};
	
	private final String STR_LEVEL		= "level";
	private final String STR_API		= "api";
	
	private final String STR_ADDLOGLEVEL	= "addloglevel";
	private final String STR_REMOVELOGLEVEL	= "removeloglevel";
	private final String STR_CLEARLOGLEVEL	= "clearloglevel";
	
	private final String STR_GETCURRENTLOGLEVEL = "getcurrentloglevel";
	private final String STR_SETCURRENTLOGLEVEL = "setcurrentloglevel";
	
	private void addItems() {
		final String function = "addItems";
		Log.info("["+className+"] "+function+" Begin");
		
		ListBox level = (ListBox) uiGeneric.getWidget(STR_LEVEL);
		if ( null != level ) {
			level.clear();
			for ( String s: STR_LEVELS) { level.addItem(s); }
		}
		
		ListBox api = (ListBox) uiGeneric.getWidget(STR_API);
		if ( null != api ) {
			api.clear();
			for ( String s: STR_APIS) { api.addItem(s); }
		}
		
		Log.info("["+className+"] "+function+" End");
	}
	
	private void launch(String element) {
		final String function = "launch";
		Log.info("["+className+"] "+function+" Begin");
		
		final String name = uiGeneric.getWidgetValue("namevalue");
		Log.info("["+className+"] "+function+" name["+name+"]");
		
		if ( 0 == element.compareToIgnoreCase(STR_ADDLOGLEVEL) ) {
			String namespace = uiGeneric.getWidgetValue("namespacevalue");
			String level = uiGeneric.getWidgetValue("levelvalue");
			
			UILoggerExConfig.getInstance().addFilter(Integer.parseInt(level), namespace);
		}
		else if ( 0 == element.compareToIgnoreCase(STR_REMOVELOGLEVEL) ) {
			String namespace = uiGeneric.getWidgetValue("namespacevalue");
			
			UILoggerExConfig.getInstance().removeFilter(namespace);
		}
		else if ( 0 == element.compareToIgnoreCase(STR_CLEARLOGLEVEL) ) {

			UILoggerExConfig.getInstance().clearFilters();
		}
		else if ( 0 == element.compareToIgnoreCase(STR_GETCURRENTLOGLEVEL) ) {
			
			UILogger_i logger = UILoggerFactory.getInstance().getUILogger(name, this.getClass().getName());
			Log.info("["+className+"] "+function+" logger["+logger+"]");
			
			uiGeneric.setWidgetValue("loglevelvalue", String.valueOf(logger.getCurrentLogLevel()));
		}
		else if ( 0 == element.compareToIgnoreCase(STR_SETCURRENTLOGLEVEL) ) {
			
			UILogger_i logger = UILoggerFactory.getInstance().getUILogger(name, this.getClass().getName());
			Log.info("["+className+"] "+function+" logger["+logger+"]");
			
			String l_loglevel		= uiGeneric.getWidgetValue("loglevelvalue");
			try {
				logger.setCurrentLogLevel(Integer.parseInt(l_loglevel));
			} catch (NumberFormatException e) {
				Log.warn(e.toString());
			}
		} else {
			
			Log.info("["+className+"] "+function+" name["+name+"]");
			
			final String c	= uiGeneric.getWidgetValue("classnamevalue");
			Log.info("["+className+"] "+function+" name["+name+"] c["+c+"]");
			
			final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(name, c);
			Log.info("["+className+"] "+function+" name["+name+"] c["+c+"] logger["+logger+"]");
			
			final String f	= uiGeneric.getWidgetValue("functionvalue");
			final String m	= uiGeneric.getWidgetValue("messagevalue");
			final String p1	= uiGeneric.getWidgetValue("parameter1value");
			final String p2	= uiGeneric.getWidgetValue("parameter2value");
			
			final String strLevel = ((ListBox) uiGeneric.getWidget(STR_LEVEL)).getSelectedItemText();
			Log.info("["+className+"] "+function+" strLevel["+strLevel+"]");
			final int level = Arrays.asList(STR_LEVELS).indexOf(strLevel);
			Log.info("["+className+"] "+function+" strLevel["+strLevel+"] level["+level+"]");
			
			final String strApi = ((ListBox) uiGeneric.getWidget(STR_API)).getSelectedItemText();
			Log.info("["+className+"] "+function+" strApi["+strApi+"]");
			final int api = Arrays.asList(STR_APIS).indexOf(strApi);
			Log.info("["+className+"] "+function+" strApi["+strApi+"] api["+api+"]");

			Log.info("["+className+"] "+function+"name["+name+"] level["+level+"] api["+api+"] c["+c+"] f["+f+"] m["+m+"] p1["+p1+"] p2["+p2+"]");
		
			if(int_LOG_LEVEL_4==level) {
					switch(api){
						case int_LOG_API_1: logger.trace(m); break;
						case int_LOG_API_2: logger.trace(f, m); break;
						case int_LOG_API_3: logger.trace(f, m, p1); break;
						case int_LOG_API_4: logger.trace(f, m, p1, p2); break;
						case int_LOG_API_5: logger.trace(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_6==level) {
					switch(api){
						case int_LOG_API_1: logger.debug(m); break;
						case int_LOG_API_2: logger.debug(f, m); break;
						case int_LOG_API_3: logger.debug(f, m, p1); break;
						case int_LOG_API_4: logger.debug(f, m, p1, p2); break;
						case int_LOG_API_5: logger.debug(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_5==level) {
					switch(api){
						case int_LOG_API_1: logger.info(m); break;
						case int_LOG_API_2: logger.info(f, m); break;
						case int_LOG_API_3: logger.info(f, m, p1); break;
						case int_LOG_API_4: logger.info(f, m, p1, p2); break;
						case int_LOG_API_5: logger.info(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_7==level) {
					switch(api){
						case int_LOG_API_1: logger.warn(m); break;
						case int_LOG_API_2: logger.warn(f, m); break;
						case int_LOG_API_3: logger.warn(f, m, p1); break;
						case int_LOG_API_4: logger.warn(f, m, p1, p2); break;
						case int_LOG_API_5: logger.warn(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_8==level) {
					switch(api){
						case int_LOG_API_1: logger.error(m); break;
						case int_LOG_API_2: logger.error(f, m); break;
						case int_LOG_API_3: logger.error(f, m, p1); break;
						case int_LOG_API_4: logger.error(f, m, p1, p2); break;
						case int_LOG_API_5: logger.error(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_9==level) {
					switch(api){
						case int_LOG_API_1: logger.fatal(m); break;
						case int_LOG_API_2: logger.fatal(f, m); break;
						case int_LOG_API_3: logger.fatal(f, m, p1); break;
						case int_LOG_API_4: logger.fatal(f, m, p1, p2); break;
						case int_LOG_API_5: logger.fatal(f, m, new Object[]{p1, p2}); break;
					}
			} else if(int_LOG_LEVEL_3==level) {
					switch(api){
						case int_LOG_API_1: logger.begin(f); break;
					}
			} else if(int_LOG_LEVEL_2==level) {
					switch(api){
						case int_LOG_API_1: logger.end(f); break;
					}
			} else if(int_LOG_LEVEL_1==level) {
					switch(api){
						case int_LOG_API_1: logger.beginEnd(m); break;
						case int_LOG_API_2: logger.beginEnd(f, m); break;
						case int_LOG_API_3: logger.beginEnd(f, m, p1); break;
						case int_LOG_API_4: logger.beginEnd(f, m, p1, p2); break;
						case int_LOG_API_5: logger.beginEnd(f, m, new Object[]{p1, p2}); break;
					}
			}
		}

		Log.info("["+className+"] "+function+" End");
	}

	@Override
	public void init() {
		super.init();
		
		addItems();
		
		final String function = "init";
		Log.info("["+className+"] "+function+" Begin");

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				Log.info("["+className+"] "+function+" Begin");
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						Log.info("["+className+"] "+function+" element["+element+"]");
						if ( null != element ) {
							launch(element);
						}
					}
				}
				Log.info("["+className+"] "+function+" End");
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
				Log.info("["+className+"] "+function+" Begin");
				envDown(null);
				Log.info("["+className+"] "+function+" End");
			};
		};

		Log.info("["+className+"] "+function+" End");
	}
}
