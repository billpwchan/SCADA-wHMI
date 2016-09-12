package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIActionEventMgr {
	private final String className = UIWidgetUtil.getClassSimpleName(UIActionEventMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";
	
	private String optsXMLFile = null;
	private String dictionariesCacheName = null;
	public UIActionEventMgr(String logPrefix, String dictionariesCacheName, String optsXMLFile) {
		final String function = "initActionKeys";
		
		this.logPrefix = "-> "+logPrefix+" ";
		logger.begin(className, function);
		logger.info(className, function, logPrefix+"this.dictionariesCacheName[{}]", this.dictionariesCacheName);
		logger.info(className, function, logPrefix+"this.optsXMLFile[{}]", this.optsXMLFile);
		this.dictionariesCacheName = dictionariesCacheName;
		this.optsXMLFile = optsXMLFile;
		logger.end(className, function);
	}
	
	private String [] uiEventActionKeys = null;
	private HashMap<String, UIEventAction> uiEventActions = null;
	public String [] getUIEventActionKeys () { return uiEventActionKeys; }
	public UIEventAction getUIEventAction(String element) { return uiEventActions.get(element); }
	public HashMap<String, UIEventAction> getUIEventActions() { return uiEventActions; }
	public void initActionKeys( String strTag, String [] strKeys) {
		final String function = "initActionKeys";
		
		logger.begin(className, function);
		logger.info(className, function, logPrefix+"strTag[{}]", strTag);
		logger.info(className, function, logPrefix+"strKeys[{}]", strKeys);
		uiEventActionKeys = getActionKeys(strTag, strKeys);
		logger.end(className, function);
	}
	public void initActions( String strTag, String strTagKey, String [] strParameterNames ) {
		initActions( strTag, strTagKey, uiEventActionKeys, strParameterNames);
	}
	public void initActions( String strTag, String strTagKey, String [] strKeys, String [] strParameterNames ) {
		final String function = "initActions";
		
		logger.begin(className, function);
		logger.info(className, function, logPrefix+"strTag[{}]", strTag);
		logger.info(className, function, logPrefix+"strTagKey[{}]", strTagKey);
		if ( logger.isInfoEnabled() ) {
			for ( String keys : strKeys ) {
				logger.info(className, function, logPrefix+"keys key[{}]", keys);
			}
		}
		if ( logger.isInfoEnabled() ) {
			for ( String keys : strParameterNames ) {
				logger.info(className, function, logPrefix+"keys key[{}]", keys);
			}
		}
		uiEventActions = getActions(strTag, strTagKey, strKeys, strParameterNames);
		logger.end(className, function);
	}
	
	private String [] uiEventActionOperationKeys = null;
	private HashMap<String, UIEventAction> uiEventActionOperations = null;
	public String [] getEventActionOperationKeys () { return uiEventActionOperationKeys; }
	public HashMap<String, UIEventAction> getUIEventActionOperations() { return uiEventActionOperations; }
	public void initActionOperationKeys( String strTag, String [] strOperationKeys) {
		final String function = "initActionOperationKeys";
		logger.begin(className, function);
		if ( logger.isInfoEnabled() ) {
			for ( String keys : strOperationKeys ) {
				logger.info(className, function, logPrefix+"strOperationKeys key[{}]", keys);
			}
		}
		logger.info(className, function, logPrefix+"strTag[{}]", strTag);
		logger.info(className, function, logPrefix+"strOperationKeys[{}]", strOperationKeys);
		uiEventActionOperationKeys = getActionKeys(strTag, strOperationKeys);
		logger.end(className, function);
	}
	public void initActionOperations( String strTag, String strTagKey, String [] strParameterNames ) {
		initActionOperations( strTag, strTagKey, uiEventActionOperationKeys, strParameterNames );
	}
	public void initActionOperations( String strTag, String strTagKey, String [] strOperationKeys, String [] strParameterNames ) {
		final String function = "initActionOperations";
		logger.begin(className, function);

		logger.info(className, function, logPrefix+"strTag[{}]", strTag);
		logger.info(className, function, logPrefix+"strTagKey[{}]", strTagKey);
		if ( logger.isInfoEnabled() ) {
			for ( String keys : strOperationKeys ) {
				logger.info(className, function, logPrefix+"strOperationKeys key[{}]", keys);
			}
		}		
		if ( logger.isInfoEnabled() ) {
			for ( String keys : strParameterNames ) {
				logger.info(className, function, logPrefix+"strParameterNames key[{}]", keys);
			}
		}		
		uiEventActionOperations = getActions(strTag, strTagKey, strOperationKeys, strParameterNames);
		logger.end(className, function);
	}
	
	public HashMap<String, UIEventAction> getActions( String strTag , String strTagKey , String [] strOperationKeys ) {
		return getActions(strTag, strTagKey, strOperationKeys, null);
	}
	public HashMap<String, UIEventAction> getActions( String strTag , String strTagKey , String [] strOperationKeys , String [] strParameterNames ) {
		final String function = "initActions";
		
		logger.begin(className, function);
		
		HashMap<String, UIEventAction> uiEventActions = new HashMap<String, UIEventAction>();
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			for ( String filterSet : strOperationKeys ) {
				logger.info(className, function, logPrefix+"filterSet[{}]", filterSet);
				UIEventAction uiEventAction = new UIEventAction();
				String operation = dictionariesCache.getStringValue(optsXMLFile, strTagKey, strTag, filterSet);
				logger.info(className, function, logPrefix+"operation[{}]", operation);
				uiEventAction.setParameters(strTagKey, operation);
				if ( null != strParameterNames ) {
					for ( String strFilterParameterName : strParameterNames ) {
						logger.info(className, function, logPrefix+"filterSet[{}] parameterName[{}]", filterSet, strFilterParameterName);
						String value = dictionariesCache.getStringValue(optsXMLFile, strFilterParameterName, strTag, filterSet);
						logger.info(className, function, logPrefix+"filterSet[{}] parameterName[{}] value[{}]", new Object[]{filterSet, strFilterParameterName, value});
						uiEventAction.setParameters(strFilterParameterName, value);
					}
				}
				uiEventActions.put(filterSet, uiEventAction);
				
				if ( uiEventAction.getParameterKeySize() == 0 ) {
					logger.warn(className, function, logPrefix+"filterSet[{}] IS EMPTY", filterSet);
				}
			}
			logger.info(className, function, logPrefix+"uiEventActions.size()[{}]", uiEventActions.size());
		}
		
		
		logger.end(className, function);
		
		return uiEventActions;
	}
	
	public String [] getActionKeys ( String strTag, String [] strOperationKeys ) {
		final String function = "initActions";
		
		logger.begin(className, function);
		String [] operationKeys = null;
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(dictionariesCacheName);
		if ( null != dictionariesCache ) {
			ArrayList<String> arrayListOperationKeys = new ArrayList<String>();
			for ( String strFilterOperationKey : strOperationKeys ) {
				logger.info(className, function, logPrefix+"Loading strFilterOperationKey[{}]", strFilterOperationKey);
				String filterOperationValue = dictionariesCache.getStringValue(optsXMLFile, strFilterOperationKey, strTag);
				logger.info(className, function, logPrefix+"Loading strFilterOperationKey[{}] filterOperationValue[{}]", strFilterOperationKey, filterOperationValue);
				if ( null != filterOperationValue ) {
					arrayListOperationKeys.add(filterOperationValue);
				}
			}
			operationKeys = arrayListOperationKeys.toArray(new String[0]);
		}
		logger.end(className, function);
		return operationKeys;
	}

}
