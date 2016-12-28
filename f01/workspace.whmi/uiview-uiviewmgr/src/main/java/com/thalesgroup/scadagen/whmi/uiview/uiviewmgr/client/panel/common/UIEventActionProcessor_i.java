package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.ExecuteAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public interface UIEventActionProcessor_i {
	
	String getName();
	
	void setUINameCard(UINameCard uiNameCard);
	void setPrefix(String className);
	void setElement(String element);
	void setDictionariesCacheName(String strUIWidgetGeneric);
	void setEventBus(SimpleEventBus eventBus);
	void setOptsXMLFile(String optsXMLFile);
	void setUILayoutGeneric(UILayoutGeneric uiLayoutGeneric);
	void setUIWidgetGeneric(UIWidgetGeneric uiWidgetGeneric);
	void setActionSetTagName(String actionset);
	void setActionTagName(String action);
	void init();
	UIEventAction getUIEventActionSetMgr(String actionsetkey);
	UIEventAction getUIEventActionMgr(String actionkey);
	void executeActionSetInit();
	void executeActionSetInit(ExecuteAction_i executeActionHandler);
	void executeActionSetInit(int delayMillis, ExecuteAction_i executeActionHandler);
	void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override);
	void executeActionSet(String actionsetkey);
	void executeActionSet(String actionsetkey, ExecuteAction_i executeActionHandler);
	void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override,
			ExecuteAction_i executeActionHandler);
	void executeActionSet(UIEventAction action, ExecuteAction_i executeActionHandler);
	void executeAction(String actionkey, ExecuteAction_i executeActionHandler);
	void executeAction(UIEventAction action, ExecuteAction_i executeActionHandler);
	void execute(UIEventAction uiEventAction, ExecuteAction_i executeActionHandler);

}
