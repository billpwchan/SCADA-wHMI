package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init;

import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.db.UIWidgetVerifyDatabaseGetChildrenControl;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMChangePassword;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMCheckAccess;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.opm.UIWidgetVerifyOPMGetInfo;
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
	private final static String className = UIWidgetUtil.getClassSimpleName(InitUIWidgetVerifyFactorys.class.getName());
	private final static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
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
				
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMCheckAccess.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetVerifyOPMCheckAccess();
	
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyOPMGetInfo.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetVerifyOPMGetInfo();
	
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyReadJSON.class.getName())
						.equals(uiCtrl)
						) {
	
					uiWidget_i = new UIWidgetVerifyReadJSON();
	
				} 
				else if (
					UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseWritingControl.class.getName())
					.equals(uiCtrl)
					) {
	
					uiWidget_i = new UIWidgetVerifyDatabaseWritingControl();
	
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseMultiReadingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseMultiReadingControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabasePollingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabasePollingControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseSubscriptionControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseSubscriptionControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGroupPollingControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGroupPollingDiffControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGroupPollingDiffControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseGetChildrenControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseGetChildrenControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyUIEventActionControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyUIEventActionGenericControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyUIEventActionGenericControl();
		
				} 
				else if (
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
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyTranslationControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyTranslationControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyScsRTDBComponentControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyScsRTDBComponentControl();
		
				} 
				else if (
						UIWidgetUtil.getClassSimpleName(UIWidgetVerifyDatabaseControl.class.getName())
						.equals(uiCtrl)
						) {
		
					uiWidget_i = new UIWidgetVerifyDatabaseControl();
		
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
