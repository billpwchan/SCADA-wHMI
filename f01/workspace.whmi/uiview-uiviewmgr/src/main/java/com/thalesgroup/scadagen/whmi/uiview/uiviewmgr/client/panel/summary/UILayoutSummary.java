package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

import java.util.HashMap;
import java.util.Map;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.LastCompilation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.LifeValue;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.UILayoutSummary_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel.ScsOlsListPanel_i.ParameterValue;

public class UILayoutSummary extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UILayoutGeneric uiLayoutGeneric = null;
		
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private SimpleEventBus eventBus		= null;
	private String eventBusName			= null;
	private String eventBusScope		= null;
	
	private String scsEnvIds			= null;
	
	private String envUp				= null;
	private String envDown				= null;
	private String terminate			= null;
	
	private String initDelayMS 			= null;
	private String initDelay			= null;
	private String envUpDelayMS 		= null;
	private String envUpDelay			= null;
	
	private String disableInitDelay		= null;
	private String disableEnvUpDelay	= null;
	
	private final String strUIWidgetGeneric = "UIWidgetGeneric";
	private final String strHeader = "header";

	@Override
	public void init() {
		final String function = "init";

		logger.begin(className, function);
		
		logger.info(className, function, "LAST_COMPILATION[{}]", LastCompilation.get());
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			eventBusName		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EventBusName.toString(), strHeader);
			eventBusScope		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EventBusScope.toString(), strHeader);
			
			scsEnvIds			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ScsEnvIds.toString(), strHeader);
			
			envUp				= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EnvUp.toString(), strHeader);
			envDown				= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EnvDown.toString(), strHeader);
			terminate			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.Terminate.toString(), strHeader);
			
			initDelayMS			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.InitDelayMS.toString(), strHeader);
			initDelay			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.InitDelay.toString(), strHeader);
			
			envUpDelayMS		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EnvUpDelayMS.toString(), strHeader);
			envUpDelay			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.EnvUpDelay.toString(), strHeader);

			disableInitDelay	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DisableInitDelay.toString(), strHeader);
			disableEnvUpDelay	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.DisableEnvUpDelay.toString(), strHeader);
		}
		
		logger.info(className, function, "eventBusName[{}] eventBusScope[{}]", eventBusName, eventBusScope);

		if ( null == eventBusName || eventBusName.trim().length() == 0) {
			eventBusName = this.viewXMLFile;
		}
		if ( null != eventBusScope && ParameterValue.Global.toString().equals(eventBusScope) ) {
		} else {
			eventBusName = eventBusName + "_" + uiNameCard.getUiScreen();
		}
		logger.info(className, function, "eventBusName[{}]", eventBusName);
		
		UIEventActionBus uiEventActionBus = UIEventActionBus.getInstance();
		uiEventActionBus.resetEventBus(eventBusName);
		eventBus = uiEventActionBus.getEventBus(eventBusName);
		
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
		
		if ( null != uiEventActionProcessor_i ) {
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
			
			if ( ! ( null != disableInitDelay && Boolean.parseBoolean(disableInitDelay) ) ) {
				int iInitDelay = 1000;
				if ( null != initDelayMS ) {
					try { 
						iInitDelay = Integer.parseInt(initDelayMS);
					} catch ( NumberFormatException ex ) {
						logger.warn(className, function, "Value of initdelayms[{}] IS INVALID", initDelayMS);
					}
				}
				if ( null != initDelay ) {
					if ( iInitDelay > 0 ) {
						uiEventActionProcessor_i.executeActionSet(initDelay, iInitDelay, null);
					} else {
						uiEventActionProcessor_i.executeActionSet(initDelay);
					}
				} else {
					uiEventActionProcessor_i.executeActionSet(LifeValue.init_delay.toString(), 1000, null);
				}
			}
			
			if ( ! ( null != disableInitDelay && Boolean.parseBoolean(disableEnvUpDelay) ) ) {
				int iEnvUpdelay = 1000;
				if ( null != envUpDelayMS ) {
					try { 
						iEnvUpdelay = Integer.parseInt(envUpDelayMS);
					} catch ( NumberFormatException ex ) {
						logger.warn(className, function, "Value of envUpDelayMS[{}] IS INVALID", envUpDelayMS);
					}
				}
				if ( null != envUpDelay ) {
					if ( iEnvUpdelay > 0 ) {
						uiEventActionProcessor_i.executeActionSet(envUpDelay, iEnvUpdelay, null);
					} else {
						uiEventActionProcessor_i.executeActionSet(envUpDelay);
					}
				} else {
					uiEventActionProcessor_i.executeActionSet(LifeValue.envup_delay.toString(), 1000, null);
				}
			}
			
		} else {
			logger.warn(className, function, "uiEventActionProcessor_i IS NULL");
		}

		logger.end(className, function);
	}
	
	@Override
	public void envUp(String env) {
		final String function = "envUp";
		logger.begin(className, function);
		
		if ( null != uiEventActionProcessor_i) {
			
			String actionkey = envUp;
			if ( null == envUp ) {
				actionkey = LifeValue.envup.toString();
			};
			
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put(ActionAttribute.OperationString2.toString(), env);
			
			Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
			override.put(actionkey, parameter);
			
			uiEventActionProcessor_i.executeActionSet(actionkey, override);
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void envDown(String env) {
		final String function = "envDown";
		logger.begin(className, function);
		
		if ( null != uiEventActionProcessor_i) {
			
			String actionkey = envDown;
			if ( null == envDown ) {
				actionkey = LifeValue.envdown.toString();
			};
			
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put(ActionAttribute.OperationString2.toString(), env);
			
			Map<String, Map<String, Object>> override = new HashMap<String, Map<String, Object>>();
			override.put(actionkey, parameter);
			
			uiEventActionProcessor_i.executeActionSet(actionkey, override);
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void terminate() {
		final String function = "terminate";
		logger.begin(className, function);
		
		if ( null != uiEventActionProcessor_i) {
			String actionkey = terminate;
			if ( null == terminate ) {
				actionkey = LifeValue.terminate.toString();
			};
			uiEventActionProcessor_i.executeActionSet(actionkey);
		}
		
		logger.end(className, function);
	}
	
	protected void initFactorys () {
		final String function = "initFactorys";
		logger.begin(className, function);

		UILayoutSummaryFactoryDepot factoryDepot = new UILayoutSummaryFactoryDepot();
		factoryDepot.setParameter(WidgetParameterName.SimpleEventBus.toString(), eventBusName);
		factoryDepot.setParameter(WidgetParameterName.ScsEnvIds.toString(), scsEnvIds);
		factoryDepot.init();

		logger.end(className, function);
	}
}
