package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;

public interface UIEventActionProcessorCore_i {
	
	String getName();
	
	void setUINameCard(UINameCard uiNameCard);
	void setPrefix(String className);
	void setElement(String element);
	void setDictionariesCacheName(String strUIWidgetGeneric);
	void setEventBus(SimpleEventBus eventBus);
	void setOptsXMLFile(String optsXMLFile);
	void setUIGeneric(UIGeneric uiGeneric);
	void setActionSetTagName(String actionset);
	void setActionTagName(String action);
	void init();
	
	UIEventAction getUIEventActionSetMgr(String actionsetkey);
	UIEventAction getUIEventActionMgr(String actionkey);
	
	boolean executeActionSet(String actionsetkey);
	
	boolean executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override);
	boolean executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeActionSet(UIEventAction action, HashMap<String, HashMap<String, Object>> override);
	boolean executeActionSet(UIEventAction action, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeAction(String actionkey, HashMap<String, HashMap<String, Object>> override);
	boolean executeAction(String actionkey, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override);
	boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler);
}