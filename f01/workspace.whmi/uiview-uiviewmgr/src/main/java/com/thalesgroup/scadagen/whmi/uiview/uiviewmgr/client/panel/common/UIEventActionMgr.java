package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIEventActionMgr {
	private final String className = UIWidgetUtil.getClassSimpleName(UIEventActionMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	private String logPrefix = "";

	private String dictionariesCacheName = null;
	private String fileName = null;
	private String tag = null;
	public UIEventActionMgr(String logPrefix, String dictionariesCacheName, String fileName, String tag) {
		final String function = "UIEventActionMgr";
		logger.begin(className, function);
		
		this.logPrefix = logPrefix;
		this.dictionariesCacheName = dictionariesCacheName;
		this.fileName = fileName;
		this.tag = tag;
		
		logger.info(className, function, this.logPrefix+"this.dictionariesCacheName[{}]", this.dictionariesCacheName);
		logger.info(className, function, this.logPrefix+"this.fileName[{}]", this.fileName);
		logger.info(className, function, this.logPrefix+"this.tag[{}]", this.tag);
		
		logger.end(className, function);
	}
	
	private String [] uiEventActionKeys = null;
	private HashMap<String, UIEventAction> uiEventActions = null;
	
	public String [] getKeys () { return uiEventActionKeys; }
	public UIEventAction get(String element) { return uiEventActions.get(element); }
	public Set<Entry<String, UIEventAction>> gets() { return uiEventActions.entrySet(); }
	
	public void init() {
		final String function = "UIEventActionMgr";
		logger.begin(className, function);
		
		UIOptionCaches optionCaches = new UIOptionCaches(className, dictionariesCacheName, fileName, tag);
		
		optionCaches.init();
		
		uiEventActions = new HashMap<String, UIEventAction>();
		
		Set<Entry<String, HashMap<String, String>>> optionsSet = optionCaches.getOptions();
		if ( null != optionsSet ) {
			for ( Entry<String, HashMap<String, String>> options : optionsSet ) {
				if ( null != options ) {
					String optionKey = options.getKey();
					HashMap<String, String> option = options.getValue();
					UIEventAction uiEventAction = new UIEventAction();
					if ( null != option ) {
						for ( Entry<String, String> parameters : option.entrySet() ) {
							String parameterKey = parameters.getKey();
							String parameterValue = parameters.getValue();
							uiEventAction.setParameters(parameterKey, parameterValue);
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
			logger.info(className, function, this.logPrefix+"this.dictionariesCacheName[{}] this.fileName[{}] tag[{}] size[{}]", new Object[]{this.dictionariesCacheName, this.fileName, this.tag, size});
			uiEventActionKeys = uiEventActions.keySet().toArray(new String[0]);
		} else {
			uiEventActionKeys = new String[]{};
			logger.info(className, function, this.logPrefix+"this.dictionariesCacheName[{}] this.fileName[{}] tag[{}] IS EMPTY", new Object[]{this.dictionariesCacheName, this.fileName, this.tag});
		}
		
		logger.end(className, function);
	}

}
