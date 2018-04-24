package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIEventActionMgr {

	private final String className = this.getClass().getSimpleName();
	private final UILogger logger = UILoggerFactory.getInstance().getLogger(this.getClass().getName());
	private String logPrefix = "";

	private String dictionariesCacheName = null;
	private String fileName = null;
	private String tag = null;
	public UIEventActionMgr(final String logPrefix, final String dictionariesCacheName, final String fileName, final String tag) {
		final String function = "UIEventActionMgr";
		logger.begin(className, function);
		
		this.logPrefix = logPrefix;
		this.dictionariesCacheName = dictionariesCacheName;
		this.fileName = fileName;
		this.tag = tag;
		
		logger.debug(className, function, this.logPrefix+"this.dictionariesCacheName[{}] this.fileName[{}] this.tag[{}]"
				, new Object[]{this.dictionariesCacheName, this.fileName, this.tag});
		
		logger.end(className, function);
	}
	
	private String [] uiEventActionKeys = null;
	private Map<String, UIEventAction> uiEventActions = null;
	
	public String [] getKeys () { return uiEventActionKeys; }
	public UIEventAction get(final String element) { return uiEventActions.get(element); }
	public Set<Entry<String, UIEventAction>> gets() { return uiEventActions.entrySet(); }
	
	public void init() {
		final String function = "UIEventActionMgr";
		logger.begin(className, function);
		
		final UIOptionCaches optionCaches = new UIOptionCaches(className, dictionariesCacheName, fileName, tag);
		
		optionCaches.init();
		
		uiEventActions = new HashMap<String, UIEventAction>();
		
		final Set<Entry<String, Map<String, String>>> optionsSet = optionCaches.getOptions();
		if ( null != optionsSet ) {
			for ( Entry<String, Map<String, String>> options : optionsSet ) {
				if ( null != options ) {
					final String optionKey = options.getKey();
					final Map<String, String> option = options.getValue();
					final UIEventAction uiEventAction = new UIEventAction();
					if ( null != option ) {
						for ( Entry<String, String> parameters : option.entrySet() ) {
							String parameterKey = parameters.getKey();
							String parameterValue = parameters.getValue();
//							logger.debug(className, function, this.logPrefix+" ADDED parameterKey[{}] parameterValue[{}]", parameterKey, parameterValue);
							uiEventAction.setParameter(parameterKey, parameterValue);
						}
					} else {
						logger.warn(className, function, this.logPrefix+"option IS NULL");
					}
					uiEventActions.put(optionKey, uiEventAction);
				} else {
					logger.warn(className, function, this.logPrefix+"options IS NULL");
				}
			}
		} else {
			logger.warn(className, function, this.logPrefix+"optionsSet IS NULL");
		}
	
		int size = uiEventActions.size();
		if ( size > 0 ) {
			logger.debug(className, function, this.logPrefix+"this.dictionariesCacheName[{}] this.fileName[{}] tag[{}] size[{}]", new Object[]{this.dictionariesCacheName, this.fileName, this.tag, size});
			uiEventActionKeys = uiEventActions.keySet().toArray(new String[0]);
		} else {
			uiEventActionKeys = new String[]{};
			logger.debug(className, function, this.logPrefix+"this.dictionariesCacheName[{}] this.fileName[{}] tag[{}] IS EMPTY", new Object[]{this.dictionariesCacheName, this.fileName, this.tag});
		}
		
		logger.end(className, function);
	}

}
