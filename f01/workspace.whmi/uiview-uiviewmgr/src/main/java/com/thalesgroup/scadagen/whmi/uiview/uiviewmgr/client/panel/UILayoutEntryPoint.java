package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.HashMap;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UILayoutEntryPoint extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutEntryPoint.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;

	private SimpleEventBus eventBus = null;

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);

		eventBus = UIEventActionBus.getInstance().getEventBus(xmlFile);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				// message = Translation.getWording(message);
				return message;
			}
		});
		
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
		
		String action1 = null;
		if ( containsParameterKey(ActionAttribute.action1.toString()) ) {
			action1 = (String) getParameter(ActionAttribute.action1.toString());
		}
		
		if ( null != action1 ) {
			
			logger.info(className, function, "action1[{}]", action1);
			
			if ( action1.equals(UIView_i.SummaryViewEvent.SetDefaultFilter.toString()) ) {
				UIEventAction uiEventAction1 = new UIEventAction();
				uiEventAction1.setParameters(UIView_i.ViewAttribute.Operation.toString(), UIView_i.SummaryViewEvent.SetDefaultFilter.toString());
				eventBus.fireEventFromSource(uiEventAction1, this);
			}
		}

		logger.end(className, function);
	}
}
