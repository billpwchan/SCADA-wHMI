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
	
	private final String STR_TRACE		= "trace";
	private final String STR_DEBUG		= "debug";
	private final String STR_INFO		= "info";
	private final String STR_WARN		= "warn";
	private final String STR_ERROR		= "error";
	private final String STR_FATAL		= "fatal";
	private final String STR_BEGIN		= "begin";
	private final String STR_END		= "end";
	private final String STR_BEGINEND	= "beginend";
	
	private final String [] STR_LEVELS	= {
			STR_TRACE
			, STR_DEBUG
			, STR_INFO
			, STR_WARN
			, STR_ERROR
			, STR_FATAL
			, STR_BEGIN
			, STR_END
			, STR_BEGINEND
			};
	
	private final int INT_ZERO			= 0;
	private final int INT_ONE			= 1;
	private final int INT_TWO			= 2;
	private final int INT_THREE			= 3;
	private final int INT_FOUE			= 4;
	
	private final String [] STR_APIS	= {
			Integer.toString(INT_ZERO)
			, Integer.toString(INT_ONE)
			, Integer.toString(INT_TWO)
			, Integer.toString(INT_THREE)
			, Integer.toString(INT_FOUE)
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
			for ( String s: STR_LEVELS) {
				level.addItem(s);
			}
		}
		
		ListBox api = (ListBox) uiGeneric.getWidget(STR_API);
		if ( null != api ) {
			api.clear();
			for ( String s: STR_APIS) {
				api.addItem(s);
			}
		}
		
		Log.info("["+className+"] "+function+" End");
	}
	
	private void launch(String element) {
		final String function = "launch";
		Log.info("["+className+"] "+function+" Begin");
		
		String name = uiGeneric.getWidgetValue("namevalue");
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
			
			String l_classname		= uiGeneric.getWidgetValue("classnamevalue");
			Log.info("["+className+"] "+function+" l_classname["+l_classname+"]");
			
			UILogger_i logger = UILoggerFactory.getInstance().getUILogger(name, l_classname);
			Log.info("["+className+"] "+function+" logger["+logger+"]");
			
			String l_function		= uiGeneric.getWidgetValue("functionvalue");
			String l_message		= uiGeneric.getWidgetValue("messagevalue");
			String l_parameter1		= uiGeneric.getWidgetValue("parameter1value");
			String l_parameter2		= uiGeneric.getWidgetValue("parameter2value");
			
			String strLevel = ((ListBox) uiGeneric.getWidget(STR_LEVEL)).getSelectedItemText();
			Log.info("["+className+"] "+function+" strLevel["+strLevel+"]");
			int level = Arrays.asList(STR_LEVELS).indexOf(strLevel);
			Log.info("["+className+"] "+function+" level["+level+"]");
			
			String strApi = ((ListBox) uiGeneric.getWidget(STR_API)).getSelectedItemText();
			Log.info("["+className+"] "+function+" strApi["+strApi+"]");
			int api = Arrays.asList(STR_APIS).indexOf(strApi);
			Log.info("["+className+"] "+function+" api["+api+"]");
			
			Log.info("["+className+"] "+function+" level["+level+"] api["+api+"]");
			
			Log.info("["+className+"] "+function+" l_classname["+l_classname+"]");
			Log.info("["+className+"] "+function+" l_function["+l_function+"]");
			Log.info("["+className+"] "+function+" l_message["+l_message+"]");
			Log.info("["+className+"] "+function+" l_parameter1["+l_parameter1+"]");
			Log.info("["+className+"] "+function+" l_parameter2["+l_parameter2+"]");		
		
			switch(level){
			case 0:
			{
				switch(api){
				case 0: logger.trace(l_message); break;
				case 1: logger.trace(l_function, l_message); break;
				case 2: logger.trace(l_function, l_message, l_parameter1); break;
				case 3: logger.trace(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.trace(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
				
			case 1:
			{
				switch(api){
				case 0: logger.debug(l_message); break;
				case 1: logger.debug(l_function, l_message); break;
				case 2: logger.debug(l_function, l_message, l_parameter1); break;
				case 3: logger.debug(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.debug(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
				
			case 2:
			{
				switch(api){
				case 0: logger.info(l_message); break;
				case 1: logger.info(l_function, l_message); break;
				case 2: logger.info(l_function, l_message, l_parameter1); break;
				case 3: logger.info(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.info(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
			
			case 3:
			{
				switch(api){
				case 0: logger.warn(l_message); break;
				case 1: logger.warn(l_function, l_message); break;
				case 2: logger.warn(l_function, l_message, l_parameter1); break;
				case 3: logger.warn(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.warn(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
			
			case 4:
			{
				switch(api){
				case 0: logger.error(l_message); break;
				case 1: logger.error(l_function, l_message); break;
				case 2: logger.error(l_function, l_message, l_parameter1); break;
				case 3: logger.error(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.error(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
			
			case 5:
			{
				switch(api){
				case 0: logger.fatal(l_message); break;
				case 1: logger.fatal(l_function, l_message); break;
				case 2: logger.fatal(l_function, l_message, l_parameter1); break;
				case 3: logger.fatal(l_function, l_message, l_parameter1, l_parameter2); break;
				case 4: logger.fatal(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
			
			case 6:
			{
				switch(api){
				case 0: logger.begin(l_function); break;
				}
			}
			break;
			
			case 7:
			{
				switch(api){
				case 0: logger.end(l_function); break;
				}
			}
			break;
			
			case 8:
			{
				switch(api){
				case 0: logger.beginEnd(l_function, l_message); break;
				case 1: logger.beginEnd(l_function, l_message, l_parameter1); break;
				case 2: logger.beginEnd(l_function, l_message, l_parameter1, l_parameter2); break;
				case 3: logger.beginEnd(l_function, l_message, new Object[]{l_parameter1, l_parameter2}); break;
				}
			}
			break;
				
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
