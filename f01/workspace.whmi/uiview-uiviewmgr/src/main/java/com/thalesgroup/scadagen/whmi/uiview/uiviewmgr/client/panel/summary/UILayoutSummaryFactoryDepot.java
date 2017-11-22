package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary;

import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIDialogMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIEventActionExecuteMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIEventActionProcessorMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIGenericMgrFactorys;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIWidgetFactorys;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.summary.init.InitUIWidgetVerifyFactorys;

public class UILayoutSummaryFactoryDepot {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UILayoutSummaryFactoryDepot.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private Map<String, String> parameters = new HashMap<String, String>();
	public void setParameter(String key , String value) { this.parameters.put(key, value); }
	
	public void init() {
		final String function = "init";
		logger.begin(className, function);
	
		InitUIDialogMgrFactorys.init();
		
		InitUIWidgetFactorys.init(parameters);
		
		InitUIWidgetVerifyFactorys.init(parameters);
		
		InitUIGenericMgrFactorys.init();
		
		InitUIEventActionProcessorMgrFactorys.init();
		
		InitUIEventActionExecuteMgrFactorys.init();
		
		logger.end(className, function);
	}
}
