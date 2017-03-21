package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialogMgrFactory;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uidialog.uidialogmgr.client.UIDialogMgr;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.UIPanelViewLayout;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionExecuteMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessor;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIGenericMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecuteMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGenericMgrFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionAlm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBusFire;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionCtrl;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDbm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDialogMsg;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionDpc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionGrc;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionOpm;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionTaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionWidget;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.entrypoint.UILayoutEntryPoint;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UILayoutConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetConfiguration;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container.UIDialogMsg;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelAccessBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelEmpty;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelSoundServerController;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container.UIPanelStatusBar;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingleton_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseWrite_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseSubscribeFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseWriteFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

/**
 * @author syau
 *
 */
public class UIScreenMMI extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenMMI.class.getName());
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private final String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private String strInitdelayms = null;
	
	private String strDatabaseReadingSingletonKey = null;
	private String strDatabaseSubscribeSingletonKey = null;
	private String strDatabaseSubscribePeriodMillis = null;
	private String strDatabaseWritingSingleton = null;
	
	private final String strHeader = "header";
	
	@Override
	public void init() {
		
		final String function = "init";
		
		logger.begin(className, function);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strInitdelayms = dictionariesCache.getStringValue(optsXMLFile, ParameterName.InitDelayMS.toString(), strHeader);
			
			strDatabaseReadingSingletonKey		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DatabaseReadingSingletonKey.toString(), strHeader);
			strDatabaseSubscribeSingletonKey	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DatabaseSubscribeSingletonKey.toString(), strHeader);
			strDatabaseSubscribePeriodMillis	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DatabaseSubscribeSingletonPeriodMillis.toString(), strHeader);
			strDatabaseWritingSingleton			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DatabaseWritingSingleton.toString(), strHeader);
		}
		
		{
			logger.debug(className, function, "strDatabaseReadingSingletonKey[{}]", strDatabaseReadingSingletonKey);
			
			DatabaseMultiRead_i databaseReading_i = DatabaseMultiReadFactory.get(strDatabaseReadingSingletonKey);
			if ( null != databaseReading_i ) {
				if ( databaseReading_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "strDatabaseReadingSingletonKey instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseReading_i).connectOnce();
				} else {
					databaseReading_i.connect();
				}
			} else {
				logger.debug(className, function, "databaseReading_i IS NULL");
			}
		}
		
		
		int intDatabaseSubscribePeriodMillis = 1000;
		if ( null != strDatabaseSubscribePeriodMillis ) {
			try { 
				intDatabaseSubscribePeriodMillis = Integer.parseInt(strDatabaseSubscribePeriodMillis);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "Value of strDatabaseSubscribePeriodMillis[{}] IS INVALID", strDatabaseSubscribePeriodMillis);
			}
		}		
		{
			logger.debug(className, function, "strDatabaseSubscribeSingletonKey[{}]", strDatabaseSubscribeSingletonKey);
			logger.debug(className, function, "intDatabaseSubscribePeriodMillis[{}]", intDatabaseSubscribePeriodMillis);
			
			DatabaseSubscribe_i databaseSubscribe_i = DatabaseSubscribeFactory.get(strDatabaseSubscribeSingletonKey);
			if ( null != databaseSubscribe_i ) {
				databaseSubscribe_i.setPeriodMillis(intDatabaseSubscribePeriodMillis);
				if ( databaseSubscribe_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "databaseSubscribe_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseSubscribe_i).connectOnce();
				} else {
					databaseSubscribe_i.connect();
				}
				
			} else {
				logger.debug(className, function, "databaseSubscribe_i IS NULL");
			}
		}

		{
			logger.debug(className, function, "strDatabaseWritingSingleton[{}]", strDatabaseWritingSingleton);
			
			DatabaseWrite_i databaseWriting_i = DatabaseWriteFactory.get(strDatabaseWritingSingleton);
			if ( null != databaseWriting_i ) {
				if ( databaseWriting_i instanceof DatabaseSingleton_i ) {
					logger.debug(className, function, "databaseWriting_i instanceof DatabaseSingleton_i");
					((DatabaseSingleton_i) databaseWriting_i).connectOnce();
				} else {
					databaseWriting_i.connect();
				}
			} else {
				logger.debug(className, function, "databaseWriting_i IS NULL");
			}
		}
		
		handlerRegistrations.add(		
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
				}
			})
		);
		
		
		UIPanelNavigation.getInstance().resetInstance();
		
		initFactorys();

		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
//		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();

		uiEventActionProcessor_i.executeActionSetInit();
		
		int delay = 1000;
		if ( null != strInitdelayms ) {
			try { 
				delay = Integer.parseInt(strInitdelayms);
			} catch ( NumberFormatException ex ) {
				logger.warn(className, function, "Value of initdelayms[{}] IS INVALID", strInitdelayms);
			}
		}
		uiEventActionProcessor_i.executeActionSet("init_delay", delay, null);
		
		//Start the Navigation Menu
		logger.info(className, function, "Start the Navigation Menu Begin");
		
		UIPanelNavigation.getInstance().getMenus(this.uiNameCard).readyToGetMenu("", "", 0, "");
		
		logger.end(className, function);
	}
	
	private void initFactorys() {
		final String function = "initFactory";
		logger.begin(className, function);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
		});
		UIDialogMgr uiDialgMgr = UIDialogMgr.getInstance();
		uiDialgMgr.clearUIDialogMgrFactorys();
		uiDialgMgr.addUIDialogMgrFactory(className, new UIDialogMgrFactory() {
			
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
		uiWidgetMgr.clearUIWidgetFactorys();
		uiWidgetMgr.addUIWidgetFactory(className, new UIWidgetMgrFactory() {
			
			@Override
			public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String uiElement
					, String uiDict
					, HashMap<String, Object> options) {
				final String function = "getUIWidget";
				
				logger.info(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}] uiDict[{}]", new Object[]{uiCtrl, uiView, uiOpts, uiDict});
				
				if ( null != uiNameCard) {
					logger.info(className, function, "uiNameCard UIPath[{}] UIScreen[{}]", uiNameCard.getUiPath(), uiNameCard.getUiScreen());
				} else {
					logger.warn(className, function, "uiNameCard IS NULL");
				}
				
				if ( null == options ) {
					logger.warn(className, function, "options IS NULL[{}]", null == options);
				}
				
				UIWidget_i uiWidget_i = null;
				
				if ( null != uiCtrl) {
					
					if ( ! uiCtrl.startsWith("WidgetFactory") ) {

						if (  UIWidgetUtil.getClassSimpleName(
								UIPanelSoundServerController.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelSoundServerController();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelAccessBar.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelAccessBar();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelStatusBar.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelStatusBar();

						} else if (  UIWidgetUtil.getClassSimpleName(
								UIPanelViewLayout.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelViewLayout();

						} else if ( UIWidgetUtil.getClassSimpleName(
								UIPanelEmpty.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UIPanelEmpty();

						} else if ( UIWidgetUtil.getClassSimpleName(
								UILayoutEntryPoint.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UILayoutEntryPoint();
							
						} else if ( UIWidgetUtil.getClassSimpleName(
								UILayoutSummary.class.getName()).equals(uiCtrl) ) {
							
							uiWidget_i = new UILayoutSummary();

						}
						
						if ( null != uiWidget_i ) {
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.setElement(uiElement);
							uiWidget_i.setDictionaryFolder(uiDict);
							uiWidget_i.setViewXMLFile(uiView);
							uiWidget_i.setOptsXMLFile(uiOpts);
							uiWidget_i.init();
						} else {
							logger.warn(className, function, "uiCtrl[{}] uiWidget_i IS NULL", uiCtrl);
						}
						
					} else {
						String params [] = uiCtrl.split("_");
						
						if ( params[1].equals("UINavigationMenu") ) {
							
							uiWidget_i = UIPanelNavigation.getInstance();
							
							logger.info(className, function, "getUIWidget uiCtrl[{}] widgetType[{}] menuLevel[{}] menuType[{}]", new Object[]{uiCtrl, params[1], params[2], params[3]});
								
							uiWidget_i.setParameter("menuLevel", params[2]);
							uiWidget_i.setParameter("menuType", params[3]);
							uiWidget_i.setUINameCard(uiNameCard);
							uiWidget_i.getMainPanel();
						}
					}
				} else {
					logger.warn(className, function, "getUIWidget widget IS NULL");
				}
				
				logger.info(className, function, "getUIWidget uiWidget[{}]", uiWidget_i);

				return uiWidget_i;
			}
		});
		
		UIGenericMgr uiGenericMgr = UIGenericMgr.getInstance();
		uiGenericMgr.clearUIGenericMgrFactorys();
		uiGenericMgr.addUIGenericMgrFactory(className, new UIGenericMgrFactory() {

			@Override
			public UIGeneric getUIGeneric(String key) {
				final String function = "getUIGeneric";
				logger.info(className, function, "key[{}]", key);
				
				UIGeneric uiGeneric = null;
				
				if ( UIWidgetUtil.getClassSimpleName(UILayoutGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UILayoutGeneric();
				} 
				else
				if ( UIWidgetUtil.getClassSimpleName(UIWidgetGeneric.class.getName())
						.equals(key) ) {
					uiGeneric = new UIWidgetGeneric();
				}
				
				if ( null == uiGeneric ) logger.warn(className, function, "key[{}] uiGeneric IS NULL", key);
				
				return uiGeneric;
			}
		});
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessorMgr.clearUIEventActionProcessorMgrFactorys();
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
		uiEventActionExecuteMgr.clearUIEventActionExecuteMgrFactorys();
		uiEventActionExecuteMgr.addUIEventActionExecute(className, new UIEventActionExecuteMgrFactory() {
			
			@Override
			public UIEventActionExecute_i getUIEventActionExecute(String key) {
				final String function = "getUIEventActionExecute";
				logger.info(className, function, "key[{}]", key);
				
				UIEventActionExecute_i uiEventActionExecute_i = null;
				
				if ( key.equals(UIActionEventType.alm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionAlm();
				}
				else if ( key.equals(UIActionEventType.ctl.toString()) ) {
					uiEventActionExecute_i = new UIEventActionCtrl();
				}
				else if ( key.equals(UIActionEventType.dbm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDbm();
				}
				else if ( key.equals(UIActionEventType.dialogmsg.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDialogMsg();
				}
				else if ( key.equals(UIActionEventType.dpc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionDpc();
				}
				else if ( key.equals(UIActionEventType.grc.toString()) ) {
					uiEventActionExecute_i = new UIEventActionGrc();
				}
				else if ( key.equals(UIActionEventType.opm.toString()) ) {
					uiEventActionExecute_i = new UIEventActionOpm();
				}
				else if ( key.equals(UIActionEventType.uitask.toString()) ) {
					uiEventActionExecute_i = new UIEventActionTaskLaunch();
				}
				else if ( key.equals(UIActionEventType.widget.toString()) ) {
					uiEventActionExecute_i = new UIEventActionWidget();
				}
				else if ( key.equals(UIActionEventType.event.toString()) ) {
					uiEventActionExecute_i = new UIEventActionBusFire();
				}
				
				if ( null == uiEventActionExecute_i ) logger.warn(className, function, "key[{}] uiEventActionExecute_i IS NULL", key);
				
				return uiEventActionExecute_i;
			}
		});

		logger.end(className, function);
	}
}
