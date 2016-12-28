package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UILayoutEntryPoint extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutEntryPoint.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);

		UIWidgetMgr.getInstance().addUIWidgetFactory(className, new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiDict
					, HashMap<String, Object> options) {
				final String function = "getUIWidget";
				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});
				if (null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget_i = null;

				if ( UIWidgetUtil.getClassSimpleName(
						UILayoutSummary.class.getName()).equals(uiCtrl) ) {
					
					uiWidget_i = new UILayoutSummary();

				}
				
				if ( null != uiWidget_i ) {
					uiWidget_i.setUINameCard(uiNameCard);
					uiWidget_i.setDictionaryFolder(uiDict);
					uiWidget_i.setViewXMLFile(uiView);
					uiWidget_i.setOptsXMLFile(uiOpts);
					uiWidget_i.init();					
				} else {
					logger.warn(className, function, "uiCtrl[{}] uiWidget IS NULL", uiCtrl);
				}
				
				return uiWidget_i;
			}
		});

		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();

		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
		super.terminate();
		if ( null != uiLayoutGeneric ) {
			uiLayoutGeneric.terminate();
		}
	}
}
