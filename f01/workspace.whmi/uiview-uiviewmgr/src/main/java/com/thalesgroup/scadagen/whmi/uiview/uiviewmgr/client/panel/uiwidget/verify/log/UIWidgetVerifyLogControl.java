package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.log;

import java.util.Arrays;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;

public class UIWidgetVerifyLogControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyLogControl.class.getName());
	
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
	
	private void addItems() {
		final String function = "addItems";
		Log.info("["+className+"] "+function+" Begin");
		
		ListBox level = (ListBox) uiGeneric.getWidget(STR_LEVEL);
		level.clear();
		for ( String s: STR_LEVELS) {
			level.addItem(s);
		}
		
		ListBox api = (ListBox) uiGeneric.getWidget(STR_API);
		api.clear();
		for ( String s: STR_APIS) {
			api.addItem(s);
		}
		
		Log.info("["+className+"] "+function+" End");
	}
	
	private void launch(String element) {
		final String function = "launch";
		Log.info("["+className+"] "+function+" Begin");
		
		UILogger log = UILoggerFactory.getInstance().getLogger(element);
		
		String l_classname		= uiGeneric.getWidgetValue("classnamevalue");
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
			case 0:
				log.trace(l_message);
				break;
			case 1:
				log.trace(l_classname, l_function, l_message);
				break;
			case 2:
				log.trace(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.trace(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.trace(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
			
		case 1:
		{
			switch(api){
			case 0:
				log.debug(l_message);
				break;
			case 1:
				log.debug(l_classname, l_function, l_message);
				break;
			case 2:
				log.debug(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.debug(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.debug(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
			
		case 2:
		{
			switch(api){
			case 0:
				log.info(l_message);
				break;
			case 1:
				log.info(l_classname, l_function, l_message);
				break;
			case 2:
				log.info(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.info(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.info(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
		
		case 3:
		{
			switch(api){
			case 0:
				log.warn(l_message);
				break;
			case 1:
				log.warn(l_classname, l_function, l_message);
				break;
			case 2:
				log.warn(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.warn(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.warn(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
		
		case 4:
		{
			switch(api){
			case 0:
				log.error(l_message);
				break;
			case 1:
				log.error(l_classname, l_function, l_message);
				break;
			case 2:
				log.error(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.error(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.error(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
		
		case 5:
		{
			switch(api){
			case 0:
				log.fatal(l_message);
				break;
			case 1:
				log.fatal(l_classname, l_function, l_message);
				break;
			case 2:
				log.fatal(l_classname, l_function, l_message, l_parameter1);
				break;
			case 3:
				log.fatal(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 4:
				log.fatal(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
		
		case 6:
		{
			switch(api){
			case 0:
				log.begin(l_classname, l_function);
				break;
			}
		}
		break;
		
		case 7:
		{
			switch(api){
			case 0:
				log.end(l_classname, l_function);
				break;
			}
		}
		break;
		
		case 8:
		{
			switch(api){
			case 0:
				log.beginEnd(l_classname, l_function, l_message);
				break;
			case 1:
				log.beginEnd(l_classname, l_function, l_message, l_parameter1);
				break;
			case 2:
				log.beginEnd(l_classname, l_function, l_message, l_parameter1, l_parameter2);
				break;
			case 3:
				log.beginEnd(l_classname, l_function, l_message, new Object[]{l_parameter1, l_parameter2});
				break;
			}
		}
		break;
			
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
