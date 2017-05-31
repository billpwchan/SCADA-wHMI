package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelnavigation.client.container.UIPanelNavigation;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitJSNI;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitUIDialogMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitUIEventActionExecuteMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitUIEventActionProcessorMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitUIGenericMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.init.InitUIWidgetFactorys;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

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
		
	private final String strHeader = "header";
	
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		String strInitdelayms = null;
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			strInitdelayms = dictionariesCache.getStringValue(optsXMLFile, ParameterName.InitDelayMS.toString(), strHeader);
			logger.debug(className, function, "strInitdelayms[{}]", strInitdelayms);
		}

		handlerRegistrations.add(		
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
				}
			})
		);
		
		UIPanelNavigation.getInstance().resetInstance();
		
		// Init Factorys
		InitUIDialogMgrFactorys.init();
		InitUIWidgetFactorys.init();
		InitUIGenericMgrFactorys.init();
		InitUIEventActionProcessorMgrFactorys.init();
		InitUIEventActionExecuteMgrFactorys.init();
		
		InitJSNI.init();

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

}
