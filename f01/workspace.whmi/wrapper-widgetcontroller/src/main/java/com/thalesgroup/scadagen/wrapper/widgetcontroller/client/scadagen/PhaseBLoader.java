package com.thalesgroup.scadagen.wrapper.widgetcontroller.client.scadagen;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitProcess_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.common.InitReady_i;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitDatabase;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.InitTranslation;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitAccess;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitControlPriority;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitHom;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitNetwork;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitOpm;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitPage;
import com.thalesgroup.scadagen.wrapper.widgetcontroller.client.init.opm.InitUser;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class PhaseBLoader implements Loader_i{

	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
	
	public final static String strUINetworkSCADAgenKey						= "UINetworkSCADAgenKey";
	public final static String strUINetworkSCADAgen							= "UINetworkSCADAgen";
	
//	public final static String strUIControlPrioritySCADAgenKey				= "UIControlPrioritySCADAgenKey";
//	public final static String strUIControlPrioritySCADAgen					= "UIControlPrioritySCADAgen";
//	
//	public final static String strUIControlPriorityReservationKey			= "UIControlPriorityReservationKey";
//	public final static String strUIControlPriorityReservation				= "UIControlPriorityReservation";
//	
//	public final static String strUIControlPriorityPTWKey				    = "UIControlPriorityPTWKey";
//	public final static String strUIControlPriorityPTW					    = "UIControlPriorityPTW";
//	
//	public final static String strUIHomSCADAgenKey			                = "UIHomSCADAgenKey";
//	public final static String strUIHomSCADAgen             				= "UIHomSCADAgen";
	
	public final static String strUIAccessSCADAgenKey						= "UIAccessSCADAgenKey";
	public final static String strUIAccessSCADAgen							= "UIAccessSCADAgen";
	
	public final static String strUIPageSCADAgenKey							= "UIPageSCADAgenKey";
	public final static String strUIPageSCADAgen							= "UIPageSCADAgen";
	
	public final static String strUIUserSCADAgenKey							= "UIUserSCADAgenKey";
	public final static String strUIUserSCADAgen							= "UIUserSCADAgen";
	
	public final static String strDatabaseReadingSingletonKey				= "DatabaseReadingSingletonKey";
	public final static String strDatabaseSubscribeSingletonKey				= "DatabaseSubscribeSingletonKey";
	public final static String strDatabaseSubscribeSingletonPeriodMillisKey = "DatabaseSubscribeSingletonPeriodMillisKey";
	public final static String strDatabaseWritingSingletonKey				= "DatabaseWritingSingletonKey";
	public final static String strDatabaseGetFullPathSingletonKey			= "DatabaseGetFullPathSingletonKey";
	
	public final static String strDatabaseMultiReadingProxySingleton		= "DatabaseMultiReadingProxySingleton";
	public final static String strDatabaseGroupPollingDiffSingleton			= "DatabaseGroupPollingDiffSingleton";
	public final static String strDatabaseSubscribePeriodMillis				= "500";
	public final static String strDatabaseWritingSingleton					= "DatabaseWritingSingleton";
	public final static String strDatabaseGetFullPathSingleton				= "DatabaseGetFullPathSingleton";
	
	@Override
	public void iniDefaultParameterName() {
		parameters.put(strUIOpmSCADAgenKey, strUIOpmSCADAgen);
		
//		parameters.put(strUIControlPrioritySCADAgenKey, strUIControlPrioritySCADAgen);
//		parameters.put(strUIControlPriorityReservationKey, strUIControlPriorityReservation);
//		parameters.put(strUIControlPriorityPTWKey, strUIControlPriorityPTW);
		
		parameters.put(strUIAccessSCADAgenKey, strUIAccessSCADAgen);
		parameters.put(strUIPageSCADAgenKey, strUIPageSCADAgen);
		parameters.put(strUINetworkSCADAgenKey, strUINetworkSCADAgen);
		parameters.put(strUIUserSCADAgenKey, strUIUserSCADAgen);
		
		parameters.put(strDatabaseReadingSingletonKey, strDatabaseMultiReadingProxySingleton);
		parameters.put(strDatabaseSubscribeSingletonKey, strDatabaseGroupPollingDiffSingleton);
		parameters.put(strDatabaseSubscribeSingletonPeriodMillisKey, strDatabaseSubscribePeriodMillis);
		parameters.put(strDatabaseWritingSingletonKey, strDatabaseWritingSingleton);
		parameters.put(strDatabaseGetFullPathSingletonKey, strDatabaseGetFullPathSingleton);
	}
	
	public Map<String, String> parameters = new HashMap<String, String>();
	@Override
	public void setParameter(String key, String value) { parameters.put(key, value); }
	
	@Override
	public InitProcess_i getLoader() { return initProcess; };

	private InitProcess_i initProcess = null;
	
	private PhaseBLoader() {
		final String function = "PhaseBLoader";
		logger.begin(function);
		
		initProcess = new InitProcess_i() {
			
			@Override
			public void process(final Map<String, Object> params, final InitReady_i initReady) {
				final String function = "process";
				logger.begin(function);

				initOpmSub();
				
				boolean opmInitInValid = false;
				// Loading SCADAgen OPM Factory
				InitOpm.getInstance().initFactory();
				try {
			        // Init the SCADAgen OPM API
			        InitOpm.getInstance().initOpm(parameters.get(strUIOpmSCADAgenKey));
				} catch (Exception ex) {
					opmInitInValid = true;
					logger.warn(function, "uiOpm_i init Exception:"+ex.toString());
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
						logger.warn(function, "opmInitInValid[{}] bypass RPC init", opmInitInValid);
					}
					
					// Init for the Database Singleton Usage
			        InitDatabase.getInstance().initDatabaseReadingSingletonKey(parameters.get(strDatabaseReadingSingletonKey));
			        InitDatabase.getInstance().initDatabaseSubscribeSingleton(parameters.get(strDatabaseSubscribeSingletonKey), intDatabaseSubscribePeriodMillis);
			        InitDatabase.getInstance().initDatabaseWritingSingleton(parameters.get(strDatabaseWritingSingletonKey));
			        InitDatabase.getInstance().initDatabaseGetFullPathSingleton(parameters.get(strDatabaseGetFullPathSingletonKey));
										        
			        logger.debug(function, " strTranslatePatten["+InitTranslation.strTranslatePatten+"]");
			        
			        
			        // Load the Translation Pattern and Flag from the Locate setting
			        TranslationMgr translationMgr = TranslationMgr.getInstance();
			        
			        if ( null != translationMgr ) {
			        	
			        	logger.debug(function, " InitTranslation.strTranslatePatten["+InitTranslation.strTranslatePatten+"]");
				        String translatePatten = Translation.getWording(InitTranslation.strTranslatePatten);
				        logger.debug(function, " InitTranslation.strTranslatePatten["+InitTranslation.strTranslatePatten+"] translatePatten["+translatePatten+"]");
				        if ( null != translatePatten && translatePatten.equals(InitTranslation.strTranslatePatten) ) translatePatten = null;
				        
				        logger.debug(function, " InitTranslation.strTranslateFlag["+InitTranslation.strTranslateFlag+"]");
				        String translateFlag = Translation.getWording(InitTranslation.strTranslateFlag);
				        logger.debug(function, " InitTranslation.strTranslateFlag["+InitTranslation.strTranslateFlag+"] translateFlag["+translateFlag+"]");
				        if ( null != translateFlag && translateFlag.equals(InitTranslation.strTranslateFlag) ) translateFlag = null;
				        
				        logger.debug(function, " translatePatten["+translatePatten+"]");
				        initTranslation.setTranslationPattern(translatePatten);
				        
				        logger.debug(function, " translateFlag["+translateFlag+"]");
				        initTranslation.setTranslationFlag(translateFlag);
				        
			        } else {
			        	logger.warn(function, "translationMgr IS NULL", translationMgr);
			        }
			        
					// Loading SCADAgen Control Priority Factory
					InitControlPriority.getInstance().initFactory();
					try {
				        // Init the SCADAgen Control Priority API
						String [] keys = InitControlPriority.getInstance().getKeys();
						for(String key: keys) {
							logger.debug(function, "InitControlPriority key[{}]", key);
							InitControlPriority.getInstance().initControlPriority(key);
						}
//				        InitControlPriority.getInstance().initControlPriority(parameters.get(strUIControlPrioritySCADAgenKey));
					} catch (Exception ex) {
						logger.warn(function, "InitControlPriority init Exception:"+ex.toString());
					}
					
					// Loading SCADAgen Hand Over Management Factory
					InitHom.getInstance().initFactory();
					try {
				        // Init the SCADAgen HOM API
						String [] keys = InitHom.getInstance().getKeys();
						for(String key: keys) {
							logger.debug(function, "InitHom key[{}]", key);
							InitHom.getInstance().initHom(key);
						}
//				        InitHom.getInstance().initHom(strUIHomSCADAgenKey);
					} catch (Exception ex) {
						logger.warn(function, "InitHom init Exception:"+ex.toString());
					}

				} else {
					logger.warn(function, "opmInitInValid[{}] bypass RPC init", opmInitInValid);
				}

				if ( null != initReady ) {
					initReady.ready(params);
				} else {
					logger.warn(function, "initReady IS NULL");
				}

			    logger.end(function);
			}
		};
		
		logger.end(function);
	}
	
	private void initOpmSub() {
		final String function = "initOpms";
		logger.begin(function);

		InitNetwork.getInstance().initFactory();
		try {
	        // Init the SCADAgen Control Priority API
	        InitNetwork.getInstance().initNetwork(parameters.get(strUINetworkSCADAgenKey));
		} catch (Exception ex) {
			logger.warn(function, "InitNetwork init Exception:"+ex.toString());
		}
		
		InitAccess.getInstance().initFactory();
		try {
			InitAccess.getInstance().initAccess(parameters.get(strUIAccessSCADAgenKey));
		} catch (Exception ex) {
			logger.warn(function, "InitAccess init Exception:"+ex.toString());
		}
		
		InitPage.getInstance().initFactory();
		try {
			InitPage.getInstance().initPage(parameters.get(strUIPageSCADAgenKey));
		} catch (Exception ex) {
			logger.warn(function, "InitPage init Exception:"+ex.toString());
		}
		
		InitUser.getInstance().initFactory();
		try {
			InitUser.getInstance().initUser(parameters.get(strUIUserSCADAgenKey));
		} catch (Exception ex) {
			logger.warn(function, "InitUser init Exception:"+ex.toString());
		}
		logger.end(function);
	}
	
}
