package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client.UIScreenEmpty;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIScreenMgr implements UIWidgetMgrFactory {

	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private UIScreenMgr() {}
	private static UIScreenMgr instance = null;
	public static UIScreenMgr getInstance() {
		if ( null == instance ) {
			instance = new UIScreenMgr();
		}
		return instance;
	}
	
	@Override
	public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElem
			, String uiDict
			, Map<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(function);
		
		logger.info(function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		UIWidget_i uiWidget_i = null;

		if ( 
				UIScreenMMI.class.getSimpleName().equals(uiCtrl) ) {

			uiWidget_i = new UIScreenMMI();
		}
		else if (
				UIScreenEmpty.class.getSimpleName().equals(uiCtrl) ) {
			
			uiWidget_i = new UIScreenEmpty();
		}
		else if (
				UILayoutEntryPoint.class.getSimpleName().equals(uiCtrl) ) {
			
			uiWidget_i = new UILayoutEntryPoint();
		}
		else if (
				UILayoutSummary.class.getSimpleName().equals(uiCtrl) ) {
			
			uiWidget_i = new UILayoutSummary();	
		}

		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setElement(uiElem);
			uiWidget_i.setDictionaryFolder(uiDict);
			uiWidget_i.setViewXMLFile(uiView);
			uiWidget_i.setOptsXMLFile(uiOpts);
			uiWidget_i.init();
		} else {
			logger.warn(function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}] widget IS NULL!", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});
		}

		logger.end(function);

		return uiWidget_i;
	}
}
