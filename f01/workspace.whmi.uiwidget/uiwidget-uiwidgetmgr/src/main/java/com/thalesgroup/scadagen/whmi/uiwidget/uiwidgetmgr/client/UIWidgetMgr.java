package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIWidgetMgr implements UIWidgetMgrFactory {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIWidgetMgr() {};
	private static UIWidgetMgr instance = null;
	public static UIWidgetMgr getInstance() {
		if ( null == instance ) 
			instance = new UIWidgetMgr();
		return instance;
	}

	private HashMap<String, UIWidgetMgrFactory> uiWidgetMgrFactorys = new HashMap<String, UIWidgetMgrFactory>();
	public void clearUIWidgetFactorys() { this.uiWidgetMgrFactorys.clear(); }
	public void addUIWidgetFactory(String key, UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(key, uiWidgetMgrEvent); }
	public void removeUIWidgetFactory(String key) { this.uiWidgetMgrFactorys.remove(key); }

	@Override
	public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String element
			, String uiDict, Map<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
		
		UIWidget_i uiWidget = null;
		
		for ( String factoryName : uiWidgetMgrFactorys.keySet() ) {
			
			logger.debug(className, function, "factoryName[{}]", factoryName);
			
			UIWidgetMgrFactory factory = uiWidgetMgrFactorys.get(factoryName);
			
			if ( null != factory ) {
			
				logger.debug(className, function, "uiCtrl[{}] uiView[{}] uiOpts[{}]", new Object[]{uiCtrl, uiView, uiOpts});
				
				uiWidget = factory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, element, uiDict, options);
				
				if ( null != uiWidget ) break;
			
			} else {
				logger.warn(className, function, "uiWidgetMgrFactory IS NULL");
			}
		}
		
		if ( null == uiWidget ) {
			logger.warn(className, function, "uiWIdget IS NULL, uiCtrl[{}] NOT FOUND", uiCtrl);
		}
		
		logger.end(className, function);

		return uiWidget;
	}
}
