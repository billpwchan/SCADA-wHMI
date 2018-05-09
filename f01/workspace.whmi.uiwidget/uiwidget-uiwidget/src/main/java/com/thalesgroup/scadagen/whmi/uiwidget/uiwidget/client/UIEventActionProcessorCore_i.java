package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.Map;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;

public interface UIEventActionProcessorCore_i {
	
	String getName();
	
	void setUINameCard(UINameCard uiNameCard);
	void setPrefix(String className);
	void setElement(String element);
	String getElementName();
	void setDictionariesCacheName(String strUIWidgetGeneric);
	void setEventBus(SimpleEventBus eventBus);
	SimpleEventBus getEventBus();
	void setOptsXMLFile(String optsXMLFile);
	String getOptsXMLFile();
	void setUIGeneric(UIGeneric uiGeneric);
	void setActionSetTagName(String actionset);
	String getActionSetTagName();
	void setActionTagName(String action);
	String getActionTagName();
	void init();
	
	UIEventAction getUIEventActionSetMgr(String actionsetkey);
	UIEventAction getUIEventActionMgr(String actionkey);
	
	boolean executeActionSet(String actionsetkey);
	
	boolean executeActionSet(final String actionsetkey, final Map<String, Map<String, Object>> override);
	boolean executeActionSet(final String actionsetkey, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeActionSet(final UIEventAction action, final Map<String, Map<String, Object>> override);
	boolean executeActionSet(final UIEventAction action, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeAction(final String actionkey, final Map<String, Map<String, Object>> override);
	boolean executeAction(final String actionkey, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler);
	
	boolean executeAction(final UIEventAction action, final Map<String, Map<String, Object>> override);
	boolean executeAction(final UIEventAction action, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler);
}
