package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client.UIScreenEmpty;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIScreenMgr implements UIWidgetMgrFactory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
			, HashMap<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		
		logger.info(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		UIWidget_i uiWidget_i = null;

		if ( UIWidgetUtil.getClassSimpleName(UIScreenMMI.class.getName())
		.equals(uiCtrl) ) {

			uiWidget_i = new UIScreenMMI();

		} else if ( UIWidgetUtil.getClassSimpleName(
				UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
			
			uiWidget_i = new UILayoutEntryPoint();
			
		} else {
			
			uiWidget_i = new UIScreenEmpty();

		}

		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setElement(uiElem);
			uiWidget_i.setDictionaryFolder(uiDict);
			uiWidget_i.setViewXMLFile(uiView);
			uiWidget_i.setOptsXMLFile(uiOpts);
			uiWidget_i.init();
		} else {
			logger.warn(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}] widget IS NULL!", new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});
		}

		logger.end(className, function);

		return uiWidget_i;
		
	}

}
