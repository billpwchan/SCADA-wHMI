package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewAlarmSummary;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIViewEventSummary;
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
	public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts
			, HashMap<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		
		logger.info(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});

		UIWidget_i uiWidget = null;
		
		if ( UIWidgetUtil.getClassSimpleName(
				UIViewAlarmSummary.class.getName()).equals(uiCtrl) ) {
			
			uiWidget = new UIViewAlarmSummary();

		} else if ( UIWidgetUtil.getClassSimpleName(
				UIViewEventSummary.class.getName()).equals(uiCtrl) ) {
			
			uiWidget = new UIViewEventSummary();
	
		} else if ( UIWidgetUtil.getClassSimpleName(
				UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
			
			uiWidget = new UILayoutEntryPoint();
			
		}

		if ( null != uiWidget ) {
			uiWidget.setUINameCard(uiNameCard);
			uiWidget.setViewXMLFile(uiView);
			uiWidget.setOptsXMLFile(uiOpts);
			uiWidget.init();
		} else {
			logger.warn(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}] widget IS NULL!", new Object[]{uiCtrl, uiView, uiOpts});
		}

		logger.end(className, function);

		return uiWidget;
		
	}
}
