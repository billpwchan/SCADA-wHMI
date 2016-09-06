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
			public UIWidget_i getUIWidget(String widget, String view, UINameCard uiNameCard,
					HashMap<String, Object> options) {
				final String function = "getUIWidget";

				final String strXml = ".xml";

				logger.info(className, function, "widget[{}] view[{}]", widget, view);
				if (null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget = null;
				String viewSel = widget;
				if (null != view && view.trim().length() > 0)
					viewSel = view;

				if ( UIWidgetUtil.getClassSimpleName(
						UILayoutSummary.class.getName()).equals(widget) ) {
					uiWidget = new UILayoutSummary();
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel+strXml);
					if ( null != options ) {
						for(String key : options.keySet()) {
							Object value = options.get(key);
							uiWidget.setParameter(key, value);
						}
					}
					uiWidget.init();
				}
				
				return uiWidget;
			}
		});

		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(xmlFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();

		logger.end(className, function);
	}
}
