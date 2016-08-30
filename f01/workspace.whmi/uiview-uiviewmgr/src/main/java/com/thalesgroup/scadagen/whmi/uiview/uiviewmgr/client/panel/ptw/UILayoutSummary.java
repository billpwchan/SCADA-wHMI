package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.HashMap;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.SummaryViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget.UIWidgetAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget.UIWidgetControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.OptionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;

public class UILayoutSummary extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummary.class.getName());
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
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(),
							uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget = null;
				String viewSel = widget;
				if (null != view && view.trim().length() > 0)
					viewSel = view;
				
				if ( ! viewSel.endsWith(strXml) ) {
					viewSel += strXml;
				}
				
				logger.info(className, function, "viewSel[{}]", viewSel);

				if (ViewWidget.UIWidgetViewer.toString().equals(widget)) {

					uiWidget = new UIWidgetViewer();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), xmlFile);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (ViewWidget.UIWidgetAction.toString().equals(widget)) {

					uiWidget = new UIWidgetAction();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), xmlFile);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (ViewWidget.UIWidgetControl.toString().equals(widget)) {

					uiWidget = new UIWidgetControl();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), xmlFile);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (ViewWidget.UIWidgetFilter.toString().equals(widget)) {

					String strFilterColumn = "";
					String strFilterValueSet1 = "";
					String strFilterValueSet0 = "";

					if (null != options) {
						if (options.containsKey(OptionAttribute.option1.toString()))
							strFilterColumn = (String) options.get(OptionAttribute.option1.toString());

						if (options.containsKey(OptionAttribute.option2.toString()))
							strFilterValueSet0 = (String) options.get(OptionAttribute.option2.toString());

						if (options.containsKey(OptionAttribute.option3.toString()))
							strFilterValueSet1 = (String) options.get(OptionAttribute.option3.toString());
					}

					uiWidget = new UIWidgetFilter();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), xmlFile);
					uiWidget.setParameter(ParameterName.FilterColumn.toString(), strFilterColumn);
					uiWidget.setParameter(ParameterName.FilterValueSet0.toString(), strFilterValueSet0);
					uiWidget.setParameter(ParameterName.FilterValueSet1.toString(), strFilterValueSet1);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (ViewWidget.UIWidgetPrint.toString().equals(widget)) {

					uiWidget = new UIWidgetPrint();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), xmlFile);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (ViewWidget.ScsOlsListPanel.toString().equals(widget)) {

					String listConfigId = "";
					String menuEnable = "";
					String selectionMode = "";

					if (null != options) {
						if (options.containsKey(OptionAttribute.option1.toString()))
							listConfigId = (String) options.get(OptionAttribute.option1.toString());

						if (options.containsKey(OptionAttribute.option2.toString()))
							menuEnable = (String) options.get(OptionAttribute.option2.toString());

						if (options.containsKey(OptionAttribute.option3.toString()))
							selectionMode = (String) options.get(OptionAttribute.option3.toString());
					}

					uiWidget = new ScsOlsListPanel();
					uiWidget.setParameter(ParameterName.MwtEventBus.toString(), new MwtEventBus());
					uiWidget.setParameter(ParameterName.ListConfigId.toString(), listConfigId);
					uiWidget.setParameter(ParameterName.MenuEnable.toString(), menuEnable);
					uiWidget.setParameter(ParameterName.SelectionMode.toString(), selectionMode);
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
			
			if ( action1.equals(SummaryViewEvent.SetDefaultFilter.toString()) ) {
				UIEventAction uiEventAction1 = new UIEventAction();
				uiEventAction1.setParameters(ViewAttribute.Operation.toString(), SummaryViewEvent.SetDefaultFilter.toString());
				eventBus.fireEventFromSource(uiEventAction1, this);
			}
		}

		logger.end(className, function);
	}
}
