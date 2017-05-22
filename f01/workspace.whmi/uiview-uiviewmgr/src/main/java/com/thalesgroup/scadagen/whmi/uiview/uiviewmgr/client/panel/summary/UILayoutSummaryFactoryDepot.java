package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventType_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionAlm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBusFire;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionCtrl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDialogMsg;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDpc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionGrc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionJS;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uilayout.UILayoutSoc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UILayoutConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBlackboard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetBox;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelection;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSwitch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCtlControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDioBtnsControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcManualOverrideControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcScanSuspendControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcTagControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetMatrixViewer;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetOPMChangePasswordControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetPrint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocDelayControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocFilterControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocTitle;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewerPager;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGetChildrenControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGroupPollingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGroupPollingDiffControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseMultiReadingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabasePollingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseSubscriptionControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseWritingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dictionaries.UIWidgetVerifyReadJSON;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.hilc.UIWidgetVerifyHILCControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMChangePassword;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMCheckAccess;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMGetInfo;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.tsc.UIIWidgetVerifyTSCControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.UIWidgetVerifyUIEventActionControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.UIWidgetVerifyUIEventActionGenericControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecuteMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessor;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.ScsMatrixPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UILayoutSummaryFactoryDepot {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummaryFactoryDepot.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String eventBusName = null;
	public void setEventBusName(String eventBusName) {
		this.eventBusName = eventBusName;
	}
	
	private String scsEnvIds = null;
	public void setScsEnvIds(String scsEnvIds) {
		this.scsEnvIds = scsEnvIds;
	}
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
		});
		
		UIDialogMgr uiDialogMgr = UIDialogMgr.getInstance();
		uiDialogMgr.addUIDialogMgrFactory(className, new UIDialogMgrFactory() {
			
			@Override
			public UIDialog_i getUIDialog(String key) {
				final String function = "getUIDialog";
				logger.info(className, function, "key[{}]", key);
				
				UIDialog_i uiDialog_i = null;
				if (
						UIWidgetUtil.getClassSimpleName(UIDialogMsg.class.getName())
						.equals(key)
						) {
					uiDialog_i = new UIDialogMsg();
				}
				
				if ( null == uiDialog_i ) logger.warn(className, function, "key[{}] uiDialog_i IS NULL", key);
				
				return uiDialog_i;
			}
		});

		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		uiWidgetMgr.addUIWidgetFactory(className, new UIWidgetMgrFactory() {
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
						UIWidgetUtil.getClassSimpleName(UIWidgetMatrixViewer.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetMatrixViewer();

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
						UIWidgetUtil.getClassSimpleName(UIWidgetDioBtnsControl.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new UIWidgetDioBtnsControl();

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
						UIWidgetUtil.getClassSimpleName(ScsMatrixPanel.class.getName())
						.equals(uiCtrl)
						) {
					
					uiWidget_i = new ScsMatrixPanel();

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
					
				} else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetSocFilterControl.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetSocFilterControl();

				} else {
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
		
		UIWidgetMgr uiWidgetMgr1Verify = UIWidgetMgr.getInstance();
		uiWidgetMgr1Verify.addUIWidgetFactory("verify", new UIWidgetMgrFactory() {
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
					UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMChangePassword.class.getName())
					.equals(uiCtrl)
					) {

					uiWidget_i = new UIWidgetVerifyOPMChangePassword();
				
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMCheckAccess.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetVerifyOPMCheckAccess();

				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMGetInfo.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetVerifyOPMGetInfo();

				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyReadJSON.class.getName())
						.equals(uiCtrl)
						) {

					uiWidget_i = new UIWidgetVerifyReadJSON();

				} else 
				if (
					UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseWritingControl.class.getName())
					.equals(uiCtrl)
					) {

					uiWidget_i = new UIWidgetVerifyDatabaseWritingControl();

				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseMultiReadingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseMultiReadingControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabasePollingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabasePollingControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseSubscriptionControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseSubscriptionControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGroupPollingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGroupPollingDiffControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingDiffControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGetChildrenControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGetChildrenControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyUIEventActionControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionGenericControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyUIEventActionGenericControl();
		
				} else 
				if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyHILCControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyHILCControl();
		
				}
				else if (
						UIWidgetUtil.getClassSimpleName(UIIWidgetVerifyTSCControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIIWidgetVerifyTSCControl();
		
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
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGenericMgr.addUIGenericMgrFactory(className, new UIGenericMgrFactory() {

			@Override
			public UIGeneric getUIGeneric(String key) {
				final String function = "getUIGeneric";
				logger.info(className, function, "key[{}]", key);
				
				UIGeneric uiGeneric = null;
				
				if ( UIWidgetUtil.getClassSimpleName(UIWidgetGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UIWidgetGeneric();
				}
				else 
				if ( UIWidgetUtil.getClassSimpleName(UILayoutGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UILayoutGeneric();
				}
				
				if ( null == uiGeneric ) logger.warn(className, function, "key[{}] uiGeneric IS NULL", key);
				
				return uiGeneric;
			}
		});

		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessorMgr.addUIEventActionProcessorMgrFactory(className, new UIEventActionProcessorMgrFactory() {
			
			@Override
			public UIEventActionProcessor_i getUIEventActionProcessor(String key) {
				final String function = "getUIEventActionProcessor";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionProcessor_i uiEventActionProcessor_i = null;
				
				if ( UIWidgetUtil.getClassSimpleName(UIEventActionProcessor.class.getName())
						.equals(key) ) {
					uiEventActionProcessor_i = new UIEventActionProcessor();
				}
				
				if ( null == uiEventActionProcessor_i ) logger.warn(className, function, "key[{}] uiEventActionProcessor_i IS NULL", key);
				
				return uiEventActionProcessor_i;
			}
		});
		
		UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
		uiEventActionExecuteMgr.addUIEventActionExecute(className, new UIEventActionExecuteMgrFactory() {
			
			@Override
			public UIEventActionExecute_i getUIEventActionExecute(String key) {
				final String function = "getUIEventActionExecute";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionExecute_i uiEventActionExecute_i = null;
				
				if ( key.equals(UIActionEventType_i.UIActionEventType.alm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionAlm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.ctl.toString()) ) {
					uiEventActionExecute_i = new UIEventActionCtrl();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dbm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDbm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dialogmsg.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDialogMsg();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.dpc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDpc();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.grc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionGrc();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.opm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionOpm();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.uitask.toString()) ) {
					uiEventActionExecute_i = new UIEventActionTaskLaunch();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.widget.toString()) ) {
					uiEventActionExecute_i = new UIEventActionWidget();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.event.toString()) ) {
					uiEventActionExecute_i = new UIEventActionBusFire();
				}
				else if ( key.equals(UIActionEventType_i.UIActionEventType.js.toString()) ) {
					uiEventActionExecute_i = new UIEventActionJS();
				}
				
				if ( null == uiEventActionExecute_i ) logger.warn(className, function, "key[{}] uiEventActionExecute_i IS NULL", key);
				
				return uiEventActionExecute_i;
			}
		});
		
		logger.end(className, function);
	}
}
