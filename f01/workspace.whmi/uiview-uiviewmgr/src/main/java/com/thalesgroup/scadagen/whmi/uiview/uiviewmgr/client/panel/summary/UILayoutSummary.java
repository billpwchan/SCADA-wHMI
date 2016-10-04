package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionMgrOld;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect2;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect3;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect4;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSwitch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcManualOverrideControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcScanSuspendControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutGeneric_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;

public class UILayoutSummary extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;

	private SimpleEventBus eventBus = null;
	private String eventBusName = null;
	private String eventBusScope = null;
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	private String strHeader = "header";
	private String strOption = "option";
	
	private String [] uiEventActionKeys = null;
	private HashMap<String, UIEventAction> uiEventActions = null;

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);
		
		Database database = Database.getInstance();
		database.connect();
		database.connectTimer(250);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			eventBusName = dictionariesCache.getStringValue(optsXMLFile, ParameterName.eventbusname.toString(), strHeader);
			eventBusScope = dictionariesCache.getStringValue(optsXMLFile, ParameterName.eventbusscope.toString(), strHeader);
		}
		
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		logger.info(className, function, "eventBusScope[{}]", eventBusScope);

		if ( null == eventBusName || eventBusName.trim().length() == 0) {
			eventBusName = this.viewXMLFile;
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
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts
					, HashMap<String, Object> options) {
				final String function = "getUIWidget";

				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});

				if (null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(),
							uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget = null;

				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget = new UIWidgetViewer();

				} else if ( 
						UIWidgetUtil.getClassSimpleName(UIWidgetCtlControl.class.getName())
						.equals(uiCtrl) ) {
					
					uiWidget = new UIWidgetCtlControl();
						
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetDpcControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcScanSuspendControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetDpcScanSuspendControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcManualOverrideControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetDpcManualOverrideControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget = new UIWidgetFilter();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetPrint.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget = new UIWidgetPrint();

				} else if (
						UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new ScsOlsListPanel();
					uiWidget.setParameter(WidgetParameterName.MwtEventBus.toString(), new MwtEventBus());

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelect2.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetCSSSelect2();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelect3.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetCSSSelect3();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelect4.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget = new UIWidgetCSSSelect4();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSFilter.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget = new UIWidgetCSSFilter();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSwitch.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget = new UIWidgetCSSSwitch();

				}
				
				if ( null != uiWidget ) {
					uiWidget.setParameter(WidgetParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setViewXMLFile(uiView);
					uiWidget.setOptsXMLFile(uiOpts);
					uiWidget.init();	
				} else {
					logger.warn(className, function, "uiCtrl[{}] uiWidget IS NULL", uiCtrl);
				}

				return uiWidget;
			}
		});

		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		UIEventActionMgrOld mgr = new UIEventActionMgrOld(className,strUIWidgetGeneric, optsXMLFile);
		uiEventActionKeys = mgr.getActionKeys(strHeader, ActionAttribute.toStrings());
		uiEventActions = mgr.getActions(strOption, ViewAttribute.Operation.toString(), uiEventActionKeys, ViewAttribute.toStrings());
		
		for ( Entry<String, UIEventAction> entry : uiEventActions.entrySet() ) {
//			String uiEventActionKey = entry.getKey();
			UIEventAction uiEventAction = entry.getValue();
			eventBus.fireEventFromSource(uiEventAction, this);
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
		
		Database database = Database.getInstance();
		database.disconnectTimer();
		database.disconnect();
		
		if ( null != uiLayoutGeneric ) {
			uiLayoutGeneric.terminate();
		}
	}
}
