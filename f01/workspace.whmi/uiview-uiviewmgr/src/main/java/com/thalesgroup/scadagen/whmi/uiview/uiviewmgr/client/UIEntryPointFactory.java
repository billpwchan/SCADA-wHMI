package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIEntryPointFactory implements UIWidgetMgrFactory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIEntryPointFactory.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIEntryPointFactory() {};
	private static UIEntryPointFactory instance = null;
	public static UIEntryPointFactory getInstance() {
		if ( null == instance ) instance = new UIEntryPointFactory();
		return instance;
	}

	@Override
	public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElem, String uiDict
			, Map<String, Object> options) {
		final String function = "getUIWidget";
		logger.begin(className, function);
		
		logger.debug(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] uiDict[{}]"
				, new Object[]{uiCtrl, uiView, uiOpts, uiElem, uiDict});

		UIWidget_i uiWidget_i = null;
		if ( UIWidgetUtil.getClassSimpleName(
				UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
			
			uiWidget_i = new UILayoutEntryPoint();
			
		}

		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setElement(uiElem);
			uiWidget_i.setDictionaryFolder(uiDict);
			uiWidget_i.setViewXMLFile(uiView);
			uiWidget_i.setOptsXMLFile(uiOpts);
			uiWidget_i.init();
		} else {
			logger.warn(className, function, "uiCtrl[{}], uiView[{}] uiOpts[{}] uiElem[{}] widget IS NULL!"
					, new Object[]{uiCtrl, uiView, uiOpts});
		}

		logger.end(className, function);

		return uiWidget_i;
		
	}
}
