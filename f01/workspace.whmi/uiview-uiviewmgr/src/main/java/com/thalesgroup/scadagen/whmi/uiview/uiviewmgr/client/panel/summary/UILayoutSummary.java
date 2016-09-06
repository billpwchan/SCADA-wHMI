package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.OptionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.WidgetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;

public class UILayoutSummary extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;

	private SimpleEventBus eventBus = null;
	private String eventBusName = null;
	private String eventBusScope = null;

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);
		
		eventBusName = (String) getParameter(WidgetAttribute.eventbusname.toString());
		eventBusScope = (String) getParameter(WidgetAttribute.eventbusscope.toString());
		
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		logger.info(className, function, "eventBusScope[{}]", eventBusScope);

		if ( null == eventBusName || eventBusName.trim().length() == 0) {
			eventBusName = this.xmlFile;
		}
		if ( 0 == "global".compareToIgnoreCase(eventBusScope) ) {
			eventBusName += uiNameCard.getUiScreen();
		}
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		
		
		eventBus = UIEventActionBus.getInstance().getEventBus(eventBusName);

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

				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName())
						.equals(widget)
						) {

					uiWidget = new UIWidgetViewer();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetAction.class.getName())
						.equals(widget)
						) {

					uiWidget = new UIWidgetAction();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if ( 
						UIWidgetUtil.getClassSimpleName(UIWidgetCtlControl.class.getName())
						.equals(widget)
						||
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcControl.class.getName())
						.equals(widget)
						) {
					
					String option1 = "";
					String option2 = "";
					String option3 = "";
					String option4 = "";
					String option5 = "";

					if (null != options) {
						
						option1	= (String) (options.containsKey(OptionAttribute.option1.toString())?options.get(OptionAttribute.option1.toString()):null);
						option2	= (String) (options.containsKey(OptionAttribute.option2.toString())?options.get(OptionAttribute.option2.toString()):null);
						option3	= (String) (options.containsKey(OptionAttribute.option3.toString())?options.get(OptionAttribute.option3.toString()):null);
						option4	= (String) (options.containsKey(OptionAttribute.option4.toString())?options.get(OptionAttribute.option4.toString()):null);
						option5	= (String) (options.containsKey(OptionAttribute.option5.toString())?options.get(OptionAttribute.option5.toString()):null);

					} else {
						logger.warn(className, function, "options IS NULL");
					}

					if ( ViewWidget.UIWidgetCtlControl.toString().equals(widget) ) {
						uiWidget = new UIWidgetCtlControl();
					}
					if ( ViewWidget.UIWidgetDpcControl.toString().equals(widget) ) {
						uiWidget = new UIWidgetDpcControl();
					}
					
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setParameter(ParameterName.ColumnAlias.toString(), option1);
					uiWidget.setParameter(ParameterName.ColumnStatus.toString(), option2);
					uiWidget.setParameter(ParameterName.ColumnServiceOwner.toString(), option3);
					uiWidget.setParameter(ParameterName.ValueUnSet.toString(), option4);
					uiWidget.setParameter(ParameterName.ValueSet.toString(), option5);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName())
						.equals(widget)
						) {

					String option1 = "";
					String option2 = "";
					String option3 = "";

					if (null != options) {
						option1	= (String) (options.containsKey(OptionAttribute.option1.toString())?options.get(OptionAttribute.option1.toString()):null);
						option2	= (String) (options.containsKey(OptionAttribute.option2.toString())?options.get(OptionAttribute.option2.toString()):null);
						option3	= (String) (options.containsKey(OptionAttribute.option3.toString())?options.get(OptionAttribute.option3.toString()):null);
					} else {
						logger.warn(className, function, "options IS NULL");
					}

					uiWidget = new UIWidgetFilter();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setParameter(ParameterName.FilterColumn.toString(), option1);
					uiWidget.setParameter(ParameterName.FilterValueSet0.toString(), option2);
					uiWidget.setParameter(ParameterName.FilterValueSet1.toString(), option3);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetPrint.class.getName())
						.equals(widget)
						) {

					uiWidget = new UIWidgetPrint();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (
						UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName())
						.equals(widget)
						) {

					String option1 = "";
					String option2 = "";
					String option3 = "";

					if (null != options) {
						option1	= (String) (options.containsKey(OptionAttribute.option1.toString())?options.get(OptionAttribute.option1.toString()):null);
						option2	= (String) (options.containsKey(OptionAttribute.option2.toString())?options.get(OptionAttribute.option2.toString()):null);
						option3	= (String) (options.containsKey(OptionAttribute.option3.toString())?options.get(OptionAttribute.option3.toString()):null);
					} else {
						logger.warn(className, function, "options IS NULL");
					}

					uiWidget = new ScsOlsListPanel();
					uiWidget.setParameter(ParameterName.MwtEventBus.toString(), new MwtEventBus());
					uiWidget.setParameter(ParameterName.ListConfigId.toString(), option1);
					uiWidget.setParameter(ParameterName.MenuEnable.toString(), option2);
					uiWidget.setParameter(ParameterName.SelectionMode.toString(), option3);
					uiWidget.init();
					
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelect.class.getName())
						.equals(widget)
						) {
					
					String option1 = "";
					String option2 = "";
					String option3 = "";
					String option4 = "";
					String option5 = "";
					String option6 = "";
					
					if ( null != options ) {
						option1 = (String) (options.containsKey(OptionAttribute.option1.toString())?options.get(OptionAttribute.option1.toString()):null);
						option2 = (String) (options.containsKey(OptionAttribute.option2.toString())?options.get(OptionAttribute.option2.toString()):null);
						option3 = (String) (options.containsKey(OptionAttribute.option3.toString())?options.get(OptionAttribute.option3.toString()):null);
						option4 = (String) (options.containsKey(OptionAttribute.option4.toString())?options.get(OptionAttribute.option4.toString()):null);
						option5 = (String) (options.containsKey(OptionAttribute.option5.toString())?options.get(OptionAttribute.option5.toString()):null);
						option6 = (String) (options.containsKey(OptionAttribute.option6.toString())?options.get(OptionAttribute.option6.toString()):null);
					} else {
						logger.warn(className, function, "options IS NULL");
					}
					
					uiWidget = new UIWidgetCSSSelect();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);

					uiWidget.setParameter(ParameterName.CSSElementName0.toString(), option1);
					uiWidget.setParameter(ParameterName.CSSValueApplyToElement0.toString(), option2);
					uiWidget.setParameter(ParameterName.CSSValueRemoveFromElement0.toString(), option3);
						
					uiWidget.setParameter(ParameterName.CSSElementName1.toString(), option4);
					uiWidget.setParameter(ParameterName.CSSValueApplyToElement1.toString(), option5);
					uiWidget.setParameter(ParameterName.CSSValueRemoveFromElement1.toString(), option6);					
					
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
					uiWidget.init();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSFilter.class.getName())
						.equals(widget)
						) {
					
					String option1 = "";
					String option2 = "";
					
					if ( null != options ) {
						option1 = (String) (options.containsKey(OptionAttribute.option1.toString())?options.get(OptionAttribute.option1.toString()):null);
						option2 = (String) (options.containsKey(OptionAttribute.option2.toString())?options.get(OptionAttribute.option2.toString()):null);
					} else {
						logger.warn(className, function, "options IS NULL");
					}
					
					uiWidget = new UIWidgetCSSFilter();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBusName);
					
					uiWidget.setParameter(ParameterName.CSSElementName0.toString(), option1);
					uiWidget.setParameter(ParameterName.CSSElementName1.toString(), option2);

					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(viewSel);
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

		for ( ActionAttribute actionAttribute : ActionAttribute.values() ) {
			String strActionAttribute = actionAttribute.toString();
			String action = (String) getParameter(strActionAttribute);
			logger.info(className, function, "ActionAttribute strActionAttribute[{}] action[{}]", strActionAttribute, action);
			if ( null != action ) {
				UIEventAction uiEventAction = new UIEventAction();
				uiEventAction.setParameters(ViewAttribute.Operation.toString(), action);
				eventBus.fireEventFromSource(uiEventAction, this);
			} else {
				logger.info(className, function, "ActionAttribute strActionAttribute[{}] action IS NULL", strActionAttribute);
			}
		}

		logger.end(className, function);
	}
}
