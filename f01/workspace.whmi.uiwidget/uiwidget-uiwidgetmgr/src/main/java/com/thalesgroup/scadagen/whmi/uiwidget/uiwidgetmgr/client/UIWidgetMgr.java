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
	public void addUIWidgetFactory(UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(className, uiWidgetMgrEvent); }
	public void addUIWidgetFactory(String xmlName, UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.put(xmlName, uiWidgetMgrEvent); }
	public void removeUIWidgetFactory(UIWidgetMgrFactory uiWidgetMgrEvent) { this.uiWidgetMgrFactorys.remove(uiWidgetMgrEvent); }

	@Override
	public UIWidget_i getUIWidget(String uiCtrl, String uiView, UINameCard uiNameCard, String uiOpts, String element
			, String uiDict, Map<String, Object> options) {
		final String function = "getUIWidget";
		
		logger.begin(className, function);
		logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
		
		UIWidget_i uiWidget = null;
		
		for ( String xmlName : uiWidgetMgrFactorys.keySet() ) {
			
			UIWidgetMgrFactory uiWidgetMgrFactory = uiWidgetMgrFactorys.get(xmlName);
			
			if ( null != uiWidgetMgrFactory ) {
			
				logger.debug(className, function, "uiCtrl[{}]", uiCtrl);
				logger.debug(className, function, "uiView[{}]", uiView);
				logger.debug(className, function, "uiOpts[{}]", uiOpts);
			
				uiWidget = uiWidgetMgrFactory.getUIWidget(uiCtrl, uiView, uiNameCard, uiOpts, element, uiDict, options);
				
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
