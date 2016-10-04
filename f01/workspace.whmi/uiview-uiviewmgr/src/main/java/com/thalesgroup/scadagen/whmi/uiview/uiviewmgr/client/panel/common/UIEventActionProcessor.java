package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionSetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.EventExecuteAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.WidgetExecuteAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIEventActionProcessor {

	private static final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessor.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String prefix = null;
	public void setPrefix(String prefix) { this.prefix = prefix; }
	
	private String dictionariesCacheName;
	public void setDictionariesCacheName(String dictionariesCacheName) {
		this.dictionariesCacheName = dictionariesCacheName;
	}
	
	private String element;
	public void setElement(String element) {
		final String function = prefix+" setEventBus";
		if ( null != element ) {
			this.element = element;
		} else {
			logger.warn(className, function, "setEventBus IS NULL");
		}
	}
	
	protected SimpleEventBus simpleEventBus = null;
	public void setEventBus(SimpleEventBus simpleEventBus) {
		final String function = prefix+" setEventBus";
		if ( null != simpleEventBus ) {
			this.simpleEventBus = simpleEventBus;
		} else {
			logger.warn(className, function, "simpleEventBus IS NULL");
		}
	}
	
	protected UIWidgetGeneric uiWidgetGeneric = null;
	public void setUIWidgetGeneric(UIWidgetGeneric uiWidgetGeneric) {
		final String function = prefix+" setUIWidgetGeneric";
		if ( null != uiWidgetGeneric ) {
			this.uiWidgetGeneric = uiWidgetGeneric;
		} else {
			logger.warn(className, function, "uiWidgetGeneric IS NULL");
		}
	}
	
	private String optsXMLFile;
	public void setOptsXMLFile(String optsXMLFile) {
		this.optsXMLFile=optsXMLFile;
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	public void executeActionSetInit() {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		executeActionSet("init");
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	public void executeActionSetInit(int delayMillis) {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		Timer t = new Timer() {
			public void run() {
				executeActionSet("init");
			}
		};
		t.schedule(delayMillis);
		logger.end(className, function);
	}	

	public void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		executeActionSet(actionsetkey, override, UIActionEventType.actionset.toString(), UIActionEventType.action.toString());
		logger.end(className, function);
	}
	
	public void executeActionSet(String actionsetkey) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		executeActionSet(actionsetkey, null, UIActionEventType.actionset.toString(), UIActionEventType.action.toString());
		logger.end(className, function);
	}

	/**
	 * Execute ActionSet by key
	 * @param actionsetkey
	 * @param stractionset
	 * @param straction
	 */
	public void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override, String stractionset, String straction) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		logger.info(className, function, "stractionset[{}]", stractionset);
		logger.info(className, function, "straction[{}]", straction);
				
		UIEventActionMgr uiEventActionSetMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, stractionset);
		uiEventActionSetMgr.init();
		
		UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, straction);
		uiEventActionMgr.init();
		
		UIEventAction actionset = uiEventActionSetMgr.get(actionsetkey);
		if ( null != actionset ) {
			actionset.setParameters(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
			for ( ActionSetAttribute ast : ActionSetAttribute.values() ) {
				String actionsetattributename = ast.toString();
				logger.info(className, function, "actionsetattributename[{}]", actionsetattributename);
				String actionsetattribute = (String) actionset.getParameter(actionsetattributename);
				logger.info(className, function, "actionsetattribute[{}]", actionsetattribute);
				if ( null != actionsetattribute ) {
					UIEventAction action = uiEventActionMgr.get(actionsetattribute);
					action.setParameters(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
					if ( null != override ) {
						
						for ( Entry<String, HashMap<String, Object>> entry : override.entrySet() ) {
							String key = entry.getKey();
							HashMap<String, Object> parameters = entry.getValue();
							logger.info(className, function, "key[{}] == actionsetattribute[{}]", key, actionsetattribute);
							if ( key.equals(actionsetattribute) ) {
								if ( null != parameters ) {
									for ( Entry<String, Object> parameter : parameters.entrySet() ) {
										String parameterKey		= parameter.getKey();
										Object parameterValue	= parameter.getValue();
										logger.info(className, function, "parameter[{}] value[{}]", parameterKey, parameterValue);
										action.setParameters(parameterKey, parameterValue);
									}
								} else {
									logger.warn(className, function, "parameters IS NULL");
								}
							}
						}
					}
					executeAction(action);
				}
			}
		} else {
			logger.warn(className, function, "actionsetkey[{}] actionset IS NULL", actionsetkey);
		}
		logger.end(className, function);
	}
	
	public void executeActionSet(UIEventAction action) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		
		if ( null != action ) {
			for ( EventExecuteAttribute eventattribute : EventExecuteAttribute.values() ) {
				String eventattributename = eventattribute.toString();
				logger.info(className, function, "eventattributename[{}]", eventattributename);
				String actionkey = (String) action.getParameter(eventattributename);
				logger.info(className, function, "actionkey[{}]", actionkey);
				if ( null != actionkey ) {
					executeAction(actionkey);
				} else {
					logger.warn(className, function, "actionkey IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param actionkey: UIEventAction key
	 */
	public void executeAction(String actionkey) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, UIActionEventType.action.toString());
		uiEventActionMgr.init();
		UIEventAction action = uiEventActionMgr.get(actionkey);
		if ( null != action ) {
			action.setParameters(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
			executeAction(action);
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		
		logger.end(className, function);
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param action: UIEventAction instance
	 */
	public void executeAction(UIEventAction action) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		if ( null != action ) {
			String opa = (String) action.getParameter(UIActionEventAttribute.OperationAction.toString());
			logger.info(className, function, "opa[{}]", opa);
			if ( null != opa ) {
				if ( opa.equals(UIActionEventType.event.toString()) ) {
					String operationTarget = UIEventActionProcessor.getOperationTarget(prefix, action);
					logger.warn(className, function, "operationTarget[{}] == element[{}]", operationTarget, element);
					if ( null != operationTarget && ! operationTarget.equals(element) ) {
						if ( null != simpleEventBus ) {
							UIEventActionBusFire uiActionEventExecute = new UIEventActionBusFire(className, simpleEventBus);
							uiActionEventExecute.executeAction(action);
						} else {
							logger.warn(className, function, "simpleEventBus IS NULL");
						}
					}
				} else if ( opa.equals(UIActionEventType.widget.toString()) ) {
					if ( null != uiWidgetGeneric ) {
						UIEventActionExecute uiEventActionExecute = new UIEventActionExecute(className, uiWidgetGeneric);
						uiEventActionExecute.executeAction(action);
					} else {
						logger.warn(className, function, "uiWidgetGeneric IS NULL");
					}
				} else if ( opa.equals(UIActionEventType.ctl.toString()) ) {
					UIEventActionCtrl uiEventActionCtrl = new UIEventActionCtrl(className, className);
					uiEventActionCtrl.execute(action);
				} else if ( opa.equals(UIActionEventType.dpc.toString()) ) {
					UIEventActionDpc uiEventActionDpc = new UIEventActionDpc(className, className);
					uiEventActionDpc.execute(action);
				} else if ( opa.equals(UIActionEventType.logic.toString()) ) {
					if ( null != uiWidgetGeneric ) {
						//UIEventActionExecute uiEventActionExecute = new UIEventActionExecute(className, uiWidgetGeneric);
						//uiEventActionExecute.executeAction(action);
					} else {
						logger.warn(className, function, "uiWidgetGeneric IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "opa IS NULL");
			}
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
	}
	
	
	public void execute(UIEventAction uiEventAction) {		
		final String function = prefix+" execute";
		logger.begin(className, function);
		if ( null != uiEventAction ) {
			
			UIEventActionProcessor.dumpUIEventAction(prefix, uiEventAction);
			
			boolean isWildMessage = false;
			boolean isSelfMessage = false;
			
			String operationTarget = UIEventActionProcessor.getOperationTarget(prefix, uiEventAction);
			logger.info(className, function, "operationTarget[{}] element[{}]", operationTarget, element);
			if ( null != operationTarget ) {
				isWildMessage = operationTarget.equals("*");
			}
			logger.info(className, function, "isWildMessage[{}]", isWildMessage);
				
			if ( null != operationTarget && null != element) {
				isSelfMessage = operationTarget.equals(element);
			} else {
				logger.warn(className, function, "operationTarget IS NULL or element IS NULL");
			}
			
			String opt = UIEventActionProcessor.getOperationType(prefix, uiEventAction);
			logger.info(className, function, "opt[{}]", opt);
			
			logger.info(className, function, "isWildMessage[{}] isSelfMessage[{}]", isWildMessage, isSelfMessage);
			if ( isWildMessage || isSelfMessage ) {
				if ( null != opt ) {
					if ( opt.equals(UIActionEventType.actionsetkey.toString()) ) {
						logger.info(className, function, "IS actionsetkey");
						for ( ActionSetAttribute actionSetAttribute : ActionSetAttribute.values() ) {
							String os = (String) uiEventAction.getParameter(actionSetAttribute.toString());
							logger.info(className, function, "loading executeActionSet os[{}]", os);
							executeActionSet(os);
						}
					} else if ( opt.equals(UIActionEventType.actionkey.toString()) ) { 
						logger.info(className, function, "IS actionkey");
						for ( ActionSetAttribute actionSetAttribute : ActionSetAttribute.values() ) {
							String os = (String) uiEventAction.getParameter(actionSetAttribute.toString());
							logger.info(className, function, "loading executeActionSet os[{}]", os);
							executeAction(os);
						}
					} else if ( opt.equals(UIActionEventType.action.toString()) ) { 
						logger.info(className, function, "IS action");
						executeAction(uiEventAction);
					}
				} else {
					logger.warn(className, function, "operation IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "widget IS NULL");
		}
		logger.end(className, function);
	}
	
	public static String getParameter(String prefix, UIEventAction uiEventAction, String parameter) {
		final String function = prefix+" getParameter";
		logger.begin(className, function);
		logger.info(className, function, "parameter[{}]", parameter);
		String parameterValue = null;
		if ( null != uiEventAction ) {
			parameterValue = (String) uiEventAction.getParameter(parameter);
		} else {
			logger.warn(className, function, "uiEventAction IS NULL");
		}
		logger.info(className, function, "parameter[{}] parameterValue[{}]", parameter, parameterValue);
		logger.end(className, function);
		return parameterValue;
	}
	public static String getOperationType(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" getOperationType";
		logger.begin(className, function);
		String operation = getParameter(prefix, uiEventAction, UIActionEventAttribute.OperationType.toString());
		logger.end(className, function);
		return operation;
	}
	public static String getOperationTarget(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" getOperationTarget";
		logger.begin(className, function);
		String operationTarget = getParameter(prefix, uiEventAction, UIActionEventAttribute.OperationTarget.toString());
		logger.end(className, function);
		return operationTarget;
	}
	
	public static void dumpUIEventAction(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" getOperationTarget";
		logger.begin(className, function);

		if ( null != uiEventAction ) {
			String opt	= (String) uiEventAction.getParameter(UIActionEventAttribute.OperationType.toString());
			String opa	= (String) uiEventAction.getParameter(UIActionEventAttribute.OperationAction.toString());
			String ot	= (String) uiEventAction.getParameter(UIActionEventAttribute.OperationTarget.toString());
			String os1	= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString1.toString());
			String os2	= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString2.toString());
			String os3	= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString3.toString());
			String os4	= (String) uiEventAction.getParameter(WidgetExecuteAttribute.OperationString4.toString());
			
			logger.info(className, function, "opt[{}]", opt);
			logger.info(className, function, "opa[{}]", opa);
			logger.info(className, function, "ot [{}]", ot);
			logger.info(className, function, "os1[{}]", os1);
			logger.info(className, function, "os2[{}]", os2);
			logger.info(className, function, "os3[{}]", os3);
			logger.info(className, function, "os4[{}]", os4);
		} else {
			logger.warn(className, function, "uiEventAction IS NULL", prefix);
		}
		
		logger.end(className, function);
	}

}
