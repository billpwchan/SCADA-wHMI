package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGetChildrenControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGetFullPathControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGroupPollingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGroupPollingDiffControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseMultiReadingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabasePollingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseSubscriptionControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseWritingControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dbm.UIWidgetVerifyDatabaseControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dbm.UIWidgetVerifyScsRTDBComponentControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.dictionaries.UIWidgetVerifyReadJSON;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.hilc.UIWidgetVerifyHILCControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.log.UIWidgetVerifyLogControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.ols.UIWidgetVerifyOLSControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyControlPriority;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMChangePassword;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMCheckAccess;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMGetInfo;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMHom;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMLoginLogout;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.translaction.UIWidgetVerifyTranslationControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.tsc.UIIWidgetVerifyTSCControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.UIWidgetVerifyUIEventActionControl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.uieventaction.UIWidgetVerifyUIEventActionGenericControl;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class InitUIWidgetVerifyFactorys {

	private final static String name = InitUIWidgetVerifyFactorys.class.getName();
	private final static String className = InitUIWidgetVerifyFactorys.class.getSimpleName();
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(name);

	public static void init(final Map<String, String> parameters) {
		String function = "init";
		logger.begin(className, function);

		UIWidgetMgr uiWidgetMgr = UIWidgetMgr.getInstance();
		uiWidgetMgr.removeUIWidgetFactory(name);
		uiWidgetMgr.addUIWidgetFactory(name, new UIWidgetMgrFactory() {

			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElement
					, String uiDict
					, Map<String, Object> options) {
				final String function = "getUIWidget";

				logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});

				if (null != uiNameCard) {
					logger.debug(className, function, "uiNameCard UIPath[{}] UIScreen[{}]"
							, uiNameCard.getUiPath(),uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				logger.info(className, function, "options IS NULL[{}]", null == options);

				UIWidget_i uiWidget_i = null;

				if (
					UIWidgetVerifyControlPriority.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyControlPriority();
				} 
				else if (
					UIWidgetVerifyOPMChangePassword.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOPMChangePassword();
				} 
				else if (
						UIWidgetVerifyOPMCheckAccess.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOPMCheckAccess();
				}
				else if (
						UIWidgetVerifyOPMGetInfo.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOPMGetInfo();
				}
				else if (
						UIWidgetVerifyOPMHom.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOPMHom();
				}
				else if (
						UIWidgetVerifyOPMLoginLogout.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOPMLoginLogout();
				}
				else if (
						UIWidgetVerifyReadJSON.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyReadJSON();
				} 
				else if (
					UIWidgetVerifyDatabaseWritingControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseWritingControl();
				} 
				else if (
						UIWidgetVerifyDatabaseMultiReadingControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseMultiReadingControl();
				} 
				else if (
						UIWidgetVerifyDatabasePollingControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabasePollingControl();
				} 
				else if (
						UIWidgetVerifyDatabaseSubscriptionControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseSubscriptionControl();
				} 
				else if (
						UIWidgetVerifyDatabaseGroupPollingControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingControl();
				} 
				else if (
						UIWidgetVerifyDatabaseGroupPollingDiffControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingDiffControl();
				} 
				else if (
						UIWidgetVerifyDatabaseGetChildrenControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseGetChildrenControl();
				}
				else if (
						UIWidgetVerifyDatabaseGetFullPathControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseGetFullPathControl();
				}
				else if (
						UIWidgetVerifyUIEventActionControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyUIEventActionControl();
				} 
				else if (
						UIWidgetVerifyUIEventActionGenericControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyUIEventActionGenericControl();
				} 
				else if (
						UIWidgetVerifyHILCControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyHILCControl();
				}
				else if (
						UIIWidgetVerifyTSCControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIIWidgetVerifyTSCControl();
				} 
				else if (
						UIWidgetVerifyTranslationControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyTranslationControl();
				} 
				else if (
						UIWidgetVerifyScsRTDBComponentControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyScsRTDBComponentControl();
				} 
				else if (
						UIWidgetVerifyDatabaseControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyDatabaseControl();
				}
				else if (
						UIWidgetVerifyOLSControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyOLSControl();
				}
				else if (
						UIWidgetVerifyLogControl.class.getSimpleName().equals(uiCtrl)) {

					uiWidget_i = new UIWidgetVerifyLogControl();
				}

				if ( null != uiWidget_i ) {
					uiWidget_i.setParameter(WidgetParameterName.SimpleEventBus.toString(), parameters.get(WidgetParameterName.SimpleEventBus.toString()));
					uiWidget_i.setParameter(WidgetParameterName.ScsEnvIds.toString(), parameters.get(WidgetParameterName.ScsEnvIds.toString()));
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
	}
}
