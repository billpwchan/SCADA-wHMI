package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenroot.client;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIScreenRootMgr implements UIWidgetMgrFactory {

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private UIScreenRootMgr() {}
	private static UIScreenRootMgr instance = null;
	public static UIScreenRootMgr getInstance() {
		if ( null == instance ) {
			instance = new UIScreenRootMgr();
		}
		return instance;
	}
	
	@Override
	public UIWidget_i getUIWidget(
			final String uiCtrl
			, final String uiView
			, final UINameCard uiNameCard
			, final String uiOpts
			, final String uiElem
			, final String uiDict
			, final Map<String, Object> options) {
		final String function = "getUIWidget";
		logger.begin(function);
		logger.debug(function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		UIWidget_i uiWidget_i = null;

		if ( UIPanelScreen.class.getSimpleName().equals(uiCtrl) ) {

			uiWidget_i = new UIPanelScreen();
		}

		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setElement(uiElem);
			uiWidget_i.setDictionaryFolder(uiDict);
			uiWidget_i.setViewXMLFile(uiView);
			uiWidget_i.setOptsXMLFile(uiOpts);
			uiWidget_i.init();
		} else {
			logger.warn(function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}] widget IS NULL!"
					, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});
		}

		logger.end(function);
		return uiWidget_i;
	}
}
