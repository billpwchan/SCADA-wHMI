package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitControlPriority;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitDatabase;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitOpm;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitTranslation;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class PhaseBLoader implements Loader_i{
	
	private static final String className = UIWidgetUtil.getClassSimpleName(PhaseBLoader.class.getName());
	private static final UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static PhaseBLoader instance = null; 
	public static PhaseBLoader getInstance() {
		if ( null == instance ) {
			instance = new PhaseBLoader();
			instance.iniDefaultParameterName();
		}
		return instance;
	}
	public final static String strUIOpmSCADAgenKey							= "UIOpmSCADAgenKey";
	public final static String strUIOpmSCADAgen								= "UIOpmSCADAgen";
	
	public final static String strUIControlPrioritySCADAgenKey				= "UIControlPrioritySCADAgenKey";
	public final static String strUIControlPrioritySCADAgen					= "UIControlPrioritySCADAgen";
	
	public final static String strDatabaseReadingSingletonKey				= "DatabaseReadingSingletonKey";
	public final static String strDatabaseSubscribeSingletonKey				= "DatabaseSubscribeSingletonKey";
	public final static String strDatabaseSubscribeSingletonPeriodMillisKey = "DatabaseSubscribeSingletonPeriodMillisKey";
	public final static String strDatabaseWritingSingletonKey				= "DatabaseWritingSingletonKey";
	
	public final static String strDatabaseMultiReadingProxySingleton		= "DatabaseMultiReadingProxySingleton";
	public final static String strDatabaseGroupPollingDiffSingleton			= "DatabaseGroupPollingDiffSingleton";
	public final static String strDatabaseSubscribePeriodMillis				= "500";
	public final static String strDatabaseWritingSingleton					= "DatabaseWritingSingleton";
	
	
	@Override
	public void iniDefaultParameterName() {
		parameters.put(strUIOpmSCADAgenKey, strUIOpmSCADAgen);
		parameters.put(strUIControlPrioritySCADAgenKey, strUIControlPrioritySCADAgen);
		parameters.put(strDatabaseReadingSingletonKey, strDatabaseMultiReadingProxySingleton);
		parameters.put(strDatabaseSubscribeSingletonKey, strDatabaseGroupPollingDiffSingleton);
		parameters.put(strDatabaseSubscribeSingletonPeriodMillisKey, strDatabaseSubscribePeriodMillis);
		parameters.put(strDatabaseWritingSingletonKey, strDatabaseWritingSingleton);
	}
	
	public Map<String, String> parameters = new HashMap<String, String>();
	@Override
	public void setParameter(String key, String value) { parameters.put(key, value); }
	
	@Override
	public InitProcess_i getLoader() { return initProcess; };

	private InitProcess_i initProcess = null;
	
	private PhaseBLoader() {
		final String function = "PhaseBLoader";
		logger.begin(className, function);
		
		initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(className, function);

				boolean opmInitInValid = false;
				
				// Loading SCADAgen OPM Factory
				InitOpm.getInstance().initOpmFactory();

				try {
			        // Init the SCADAgen OPM API
			        InitOpm.getInstance().initOpm(parameters.get(strUIOpmSCADAgenKey));
				} catch (Exception ex) {
					opmInitInValid = true;
					logger.warn(className, function, "uiOpm_i init Exception:"+ex.toString());
				}
				
				// Loading SCADAgen Control Priority Factory
				InitControlPriority.getInstance().initControlPriorityFactory();
				
				try {
			        // Init the SCADAgen Control Priority API
			        InitControlPriority.getInstance().initControlPriority(parameters.get(strUIControlPrioritySCADAgenKey));
				} catch (Exception ex) {
					logger.warn(className, function, "uiControlPriority_i init Exception:"+ex.toString());
				}
				
		        // Init for the translation
		        InitTranslation initTranslation = InitTranslation.getInstance();
		        initTranslation.initTranslationEngine();
		        
				if ( ! opmInitInValid ) {
					
					int intDatabaseSubscribePeriodMillis = 0;
					String strDatabaseSubscribePeriodMillisValue = parameters.get(strDatabaseSubscribeSingletonPeriodMillisKey);
					try {
						intDatabaseSubscribePeriodMillis = Integer.parseInt(strDatabaseSubscribePeriodMillisValue);
					} catch ( NumberFormatException ex) {
						logger.warn(className, function, "opmInitInValid[{}] bypass RPC init", opmInitInValid);
					}
					
					// Init for the Database Singleton Usage
			        InitDatabase.getInstance().initDatabaseReadingSingletonKey(parameters.get(strDatabaseReadingSingletonKey));
			        InitDatabase.getInstance().initDatabaseSubscribeSingleton(parameters.get(strDatabaseSubscribeSingletonKey), intDatabaseSubscribePeriodMillis);
			        InitDatabase.getInstance().initDatabaseWritingSingleton(parameters.get(strDatabaseWritingSingletonKey));
										        
			        logger.debug(className, function, " strTranslatePatten["+InitTranslation.strTranslatePatten+"]");
			        
			        
			        // Load the Translation Pattern and Flag from the Locate setting
			        TranslationMgr translationMgr = TranslationMgr.getInstance();
			        
			        if ( null != translationMgr ) {
			        	
			        	logger.debug(className, function, " InitTranslation.strTranslatePatten["+InitTranslation.strTranslatePatten+"]");
				        String translatePatten = Translation.getWording(InitTranslation.strTranslatePatten);
				        logger.debug(className, function, " InitTranslation.strTranslatePatten["+InitTranslation.strTranslatePatten+"] translatePatten["+translatePatten+"]");
				        if ( null != translatePatten && translatePatten.equals(InitTranslation.strTranslatePatten) ) translatePatten = null;
				        
				        logger.debug(className, function, " InitTranslation.strTranslateFlag["+InitTranslation.strTranslateFlag+"]");
				        String translateFlag = Translation.getWording(InitTranslation.strTranslateFlag);
				        logger.debug(className, function, " InitTranslation.strTranslateFlag["+InitTranslation.strTranslateFlag+"] translateFlag["+translateFlag+"]");
				        if ( null != translateFlag && translateFlag.equals(InitTranslation.strTranslateFlag) ) translateFlag = null;
				        
				        logger.debug(className, function, " translatePatten["+translatePatten+"]");
				        initTranslation.setTranslationPattern(translatePatten);
				        
				        logger.debug(className, function, " translateFlag["+translateFlag+"]");
				        initTranslation.setTranslationFlag(translateFlag);
				        
			        } else {
			        	logger.warn(className, function, "translationMgr IS NULL", translationMgr);
			        }

				} else {
					logger.warn(className, function, "opmInitInValid[{}] bypass RPC init", opmInitInValid);
				}

				if ( null != initReady ) {
					initReady.ready(params);
				} else {
					logger.warn(className, function, "initReady IS NULL");
				}

			    logger.end(className, function);
			}
		};
		
		logger.end(className, function);
	}
	
}
