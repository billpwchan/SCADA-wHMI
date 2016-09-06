package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewAlarm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIViewMgr implements UIWidgetMgrFactory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIViewMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIViewMgr() {};
	private static UIViewMgr instance = null;
	public static UIViewMgr getInstance() {
		if ( null == instance ) instance = new UIViewMgr();
		return instance;
	}

	@Override
	public UIWidget_i getUIWidget(String widget, String view, UINameCard uiNameCard, HashMap<String, Object> options) {
		final String function = "getUIWidget";
		
		final String strXml = ".xml";
		
		logger.begin(className, function);
		
		logger.info(className, function, "widget[{}], view[{}]", widget, view);

		UIWidget_i uiWidget = null;
		
		if ( "UIViewAlarm".equals(widget) ) {
			uiWidget = new UIViewAlarm();
			uiWidget.setUINameCard(uiNameCard);
			uiWidget.init();	
		} else if ( "UIViewEvent".equals(widget) ) {
			uiWidget = new UIViewEvent();
			uiWidget.setUINameCard(uiNameCard);
			uiWidget.init();	
		} else if ( "UILayoutEntryPoint".equals(widget) ) {
			uiWidget = new UILayoutEntryPoint();
			uiWidget.setUINameCard(uiNameCard);
			uiWidget.setXMLFile(view+strXml);
			uiWidget.init();
		}
		
		if ( null == uiWidget ) {
			logger.warn(className, function, "widget IS NULL! widget[{}], view[{}]", widget, view);
		}

		logger.end(className, function);

		return uiWidget;
		
	}
}
