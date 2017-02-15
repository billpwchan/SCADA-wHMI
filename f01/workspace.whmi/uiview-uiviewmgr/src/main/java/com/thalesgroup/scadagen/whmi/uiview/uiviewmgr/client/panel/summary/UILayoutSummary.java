package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

import java.util.HashMap;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgr_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.LastCompilation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutSoc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelection;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSwitch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UILayoutConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBlackboard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcManualOverrideControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcScanSuspendControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcTagControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetOPMChangePasswordControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetOPMVerifyChangePassword;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetOPMVerifyControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocDelayControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocTitle;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBox;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewerPager;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i.ParameterValue;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UILayoutSummary extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;
		
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private SimpleEventBus eventBus		= null;
	private String eventBusName			= null;
	private String eventBusScope		= null;
	private String initDelayMS 			= null;
	private String scsEnvIds			= null;
	
	private final String strUIWidgetGeneric = "UIWidgetGeneric";
	private final String strHeader = "header";

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);
		
		logger.info(className, function, "LAST_COMPILATION[{}]", LastCompilation.get());
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			eventBusName = dictionariesCache.getStringValue(optsXMLFile, ParameterName.EventBusName.toString(), strHeader);
			eventBusScope = dictionariesCache.getStringValue(optsXMLFile, ParameterName.EventBusScope.toString(), strHeader);
			initDelayMS = dictionariesCache.getStringValue(optsXMLFile, ParameterName.InitDelayMS.toString(), strHeader);
			scsEnvIds = dictionariesCache.getStringValue(optsXMLFile, ParameterName.ScsEnvIds.toString(), strHeader);
		}
		
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		logger.info(className, function, "eventBusScope[{}]", eventBusScope);

		if ( null == eventBusName || eventBusName.trim().length() == 0) {
			eventBusName = this.viewXMLFile;
		}
		if ( ! ( null != eventBusScope && 0 != ParameterValue.Global.toString().compareTo(eventBusScope) ) ) {
			eventBusName += "_" + uiNameCard.getUiScreen();
		}
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		
		
		eventBus = UIEventActionBus.getInstance().getEventBus(eventBusName);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				message = Translation.getDBMessage(message);
				
				return message;
			}
		});
		
		String key = UIWidgetUtil.getClassSimpleName(UIDialogMsg.class.getName());
		UIDialogMgr.getInstance().addDialogs(key, new UIDialogMgr_i() {
			@Override
			public UIDialog_i getDialog() {
				return new UIDialogMsg();
			}
		});

		UIWidgetMgr.getInstance().addUIWidgetFactory(className, new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElement
					, String uiDict
					, HashMap<String, Object> options) {
				final String function = "getUIWidget";

				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});

				if (null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(),
							uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget_i = null;

				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetConfiguration.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetConfiguration();

				} else if (
						UIWidgetUtil.getClassSimpleName(UILayoutConfiguration.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UILayoutConfiguration();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewer.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetViewer();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetViewerPager.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetViewerPager();

				} else if ( 
						UIWidgetUtil.getClassSimpleName(UIWidgetCtlControl.class.getName())
						.equals(uiCtrl) ) {
					
					uiWidget_i = new UIWidgetCtlControl();
						
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcControl();

				}  else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcTagControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcTagControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcScanSuspendControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcScanSuspendControl();

				} else  if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDpcManualOverrideControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDpcManualOverrideControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetFilter();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetPrint.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetPrint();

				} else if (
						UIWidgetUtil.getClassSimpleName(ScsOlsListPanel.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new ScsOlsListPanel();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelection.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetCSSSelection();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetCSSSwitch.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetCSSSwitch();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetBlackboard.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetBlackboard();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetBox.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetBox();
					
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetOPMChangePasswordControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetOPMChangePasswordControl();
					
					
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetOPMVerifyChangePassword.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetOPMVerifyChangePassword();
					
					
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetOPMVerifyControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetOPMVerifyControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UILayoutLogin.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UILayoutLogin();

				} else if (
						UIWidgetUtil.getClassSimpleName(UILayoutSoc.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UILayoutSoc();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetDataGrid.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetDataGrid();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocDelayControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocDelayControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocAutoManuControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocAutoManuControl();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocTitle.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocTitle();

				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocGrcPoint.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocGrcPoint();

				}  else {
					logger.warn(className, function, "uiCtrl[{}] type for UIWidget IS UNKNOW", uiCtrl);
				}
				
				if ( null != uiWidget_i ) {
					uiWidget_i.setParameter(WidgetParameterName.SimpleEventBus.toString(), eventBusName);
					uiWidget_i.setParameter(WidgetParameterName.ScsEnvIds.toString(), scsEnvIds);
					uiWidget_i.setUINameCard(uiNameCard);
					uiWidget_i.setElement(uiElement);
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
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();

		uiEventActionProcessor_i.executeActionSetInit();
		
		int delay = 1000;
		if ( null != initDelayMS ) {
			try { 
				delay = Integer.parseInt(initDelayMS);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "Value of initdelayms[{}] IS INVALID", initDelayMS);
			}
		}
		uiEventActionProcessor_i.executeActionSetInit(delay, null);
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
		
		if ( null != uiLayoutGeneric ) {
			uiLayoutGeneric.terminate();
		}
	}
}
