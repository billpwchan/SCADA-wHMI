package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventSenderAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.ExecuteAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIEventActionProcessor implements UIEventActionProcessor_i {

	private static final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessor.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String prefix = null;
	public void setPrefix(String prefix) { this.prefix = prefix; }
	
	private String dictionariesCacheName;
	public void setDictionariesCacheName(String dictionariesCacheName) {
		this.dictionariesCacheName = dictionariesCacheName;
	}
	
	private UINameCard uiNameCard;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		final String function = prefix+" setElement";
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
		} else {
			logger.warn(className, function, "uiNameCard IS NULL");
		}
	}
	

	@Override
	public String getName() {
		return className;
	}
	
	private String element;
	@Override
	public void setElement(String element) {
		final String function = prefix+" setElement";
		if ( null != element ) {
			this.element = element;
		} else {
			logger.warn(className, function, "setElement IS NULL");
		}
	}
	
	protected SimpleEventBus simpleEventBus = null;
	@Override
	public void setEventBus(SimpleEventBus simpleEventBus) {
		final String function = prefix+" setEventBus";
		if ( null != simpleEventBus ) {
			this.simpleEventBus = simpleEventBus;
		} else {
			logger.warn(className, function, "simpleEventBus IS NULL");
		}
	}
	
	protected UIWidgetGeneric uiWidgetGeneric = null;
	@Override
	public void setUIWidgetGeneric(UIWidgetGeneric uiWidgetGeneric) {
		final String function = prefix+" setUIWidgetGeneric";
		if ( null != uiWidgetGeneric ) {
			this.uiWidgetGeneric = uiWidgetGeneric;
		} else {
			logger.warn(className, function, "uiWidgetGeneric IS NULL");
		}
	}
	
	protected UILayoutGeneric uiLayoutGeneric = null;
	@Override
	public void setUILayoutGeneric(UILayoutGeneric uiLayoutGeneric) {
		final String function = prefix+" setUIWidgetGeneric";
		if ( null != uiLayoutGeneric ) {
			this.uiLayoutGeneric = uiLayoutGeneric;
		} else {
			logger.warn(className, function, "uiLayoutGeneric IS NULL");
		}
	}
	
	private String optsXMLFile;
	@Override
	public void setOptsXMLFile(String optsXMLFile) {
		this.optsXMLFile=optsXMLFile;
	}
	
	private String actionSetTagName = null;
	@Override
	public void setActionSetTagName(String actionSetTagName) {
		this.actionSetTagName = actionSetTagName;
	}
	
	private String actionTagName = null;
	@Override
	public void setActionTagName(String actionTagName ) {
		this.actionTagName = actionTagName;
	}
	
	private UIEventActionMgr uiEventActionSetMgr = null;
	private UIEventActionMgr uiEventActionMgr = null;
	@Override
	public void init() {
		uiEventActionSetMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionSetTagName);
		uiEventActionSetMgr.init();
		
		uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionTagName);
		uiEventActionMgr.init();
	}
	
	@Override
	public UIEventAction getUIEventActionSetMgr(String actionsetkey) {
		return this.uiEventActionSetMgr.get(actionsetkey);
	}
	
	@Override
	public UIEventAction getUIEventActionMgr(String actionkey) {
		return this.uiEventActionMgr.get(actionkey);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetInit() {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		executeActionSetInit(null);
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetInit(ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		executeActionSet("init", executeActionHandler);
		logger.end(className, function);
	}
	
	/**
	 * Load and Execute the Local Init Action Set
	 */
	@Override
	public void executeActionSetInit(int delayMillis, final ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeActionSetInit";
		logger.begin(className, function);
		if ( delayMillis >= 0 ) {
			Timer t = new Timer() {
				public void run() {
					executeActionSet("init_delay", executeActionHandler);
				}
			};
			t.schedule(delayMillis);
		} else {
			logger.warn(className, function, "delayMillis[{}] < 0", delayMillis);
		}
		logger.end(className, function);
	}
	
	@Override
	public void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		executeActionSet(actionsetkey, override/*, UIActionEventType.actionset.toString(), UIActionEventType.action.toString()*/, null);
		logger.end(className, function);
	}

//	public void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override, ExecuteAction_i executeActionHandler) {
//		final String function = prefix+" executeActionSet";
//		logger.begin(className, function);
//		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
//		executeActionSet(actionsetkey, override, UIActionEventType.actionset.toString(), UIActionEventType.action.toString(), executeActionHandler);
//		logger.end(className, function);
//	}
	
	@Override
	public void executeActionSet(String actionsetkey) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		executeActionSet(actionsetkey, null/*, UIActionEventType.actionset.toString(), UIActionEventType.action.toString()*/, null);
		logger.end(className, function);
	}
	
	@Override
	public void executeActionSet(String actionsetkey, ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		executeActionSet(actionsetkey, null/*, UIActionEventType.actionset.toString(), UIActionEventType.action.toString()*/, executeActionHandler);
		logger.end(className, function);
	}

	/**
	 * Execute ActionSet by key
	 * @param actionsetkey
	 * @param stractionset
	 * @param straction
	 */
	@Override
	public void executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override/*, String stractionset, String straction*/, ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
//		logger.info(className, function, "stractionset[{}]", stractionset);
//		logger.info(className, function, "straction[{}]", straction);
//				
//		UIEventActionMgr uiEventActionSetMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, stractionset);
//		uiEventActionSetMgr.init();
//		
//		UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, straction);
//		uiEventActionMgr.init();
//		
		UIEventAction actionset = uiEventActionSetMgr.get(actionsetkey);
		if ( null != actionset ) {
			actionset.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
			for ( ActionAttribute ast : ActionAttribute.values() ) {
				String actionsetattributename = ast.toString();
				logger.info(className, function, "actionsetattributename[{}]", actionsetattributename);
				String actionsetattribute = (String) actionset.getParameter(actionsetattributename);
				logger.info(className, function, "actionsetattribute[{}]", actionsetattribute);
				if ( null != actionsetattribute ) {
					UIEventAction action = uiEventActionMgr.get(actionsetattribute);
					action.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
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
										action.setParameter(parameterKey, parameterValue);
									}
								} else {
									logger.warn(className, function, "parameters IS NULL");
								}
							}
						}
					}
					executeAction(action, executeActionHandler);
				}
			}
		} else {
			logger.warn(className, function, "actionsetkey[{}] actionset IS NULL", actionsetkey);
		}
		logger.end(className, function);
	}
	
	@Override
	public void executeActionSet(UIEventAction action, ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		
		if ( null != action ) {
			for ( ActionAttribute eventattribute : ActionAttribute.values() ) {
				String eventattributename = eventattribute.toString();
				logger.info(className, function, "eventattributename[{}]", eventattributename);
				String actionkey = (String) action.getParameter(eventattributename);
				logger.info(className, function, "actionkey[{}]", actionkey);
				if ( null != actionkey ) {
					executeAction(actionkey, executeActionHandler);
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
	@Override
	public void executeAction(String actionkey, ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
//		UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, UIActionEventType.action.toString());
//		uiEventActionMgr.init();
		UIEventAction action = uiEventActionMgr.get(actionkey);
		if ( null != action ) {
			action.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
			executeAction(action, executeActionHandler);
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		
		logger.end(className, function);
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param action: UIEventAction instance
	 */
	@Override
	public void executeAction(UIEventAction action, ExecuteAction_i executeActionHandler) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		if ( null != action ) {
			
			dumpUIEventAction(prefix, action);
			
			boolean containue = true;
			if ( null != executeActionHandler ) containue =  executeActionHandler.executeHandler(action);
			
			if ( containue ) {
				
				String spt = (String) action.getParameter(UIActionEventSenderAttribute.SenderOperation.toString());
				
				logger.info(className, function, "spt[{}]", spt);
				
				if ( null != spt && spt.equals(UIActionEventType.event.toString()) ) {
					logger.info(className, function, "spt[{}] IS event", spt);
					String operationElement = UIEventActionProcessor.getOperationElement(prefix, action);
					logger.info(className, function, "operationElement[{}] == element[{}]", operationElement, element);
					if ( null == operationElement || ( /*null != element && */null != operationElement && ! operationElement.equals(element)) ) {
						if ( null != simpleEventBus ) {
							
							// Make a copy
							UIEventAction actionCopy = new UIEventAction(action);
							// Remove the SenderOperation when it done
							actionCopy.removeParameter(UIActionEventSenderAttribute.SenderOperation.toString());
							
							UIEventActionExecute_i uiEventActionExecute = new UIEventActionBusFire();
							uiEventActionExecute.setLogPrefix(className);
							uiEventActionExecute.setSimpleEventBus(simpleEventBus);
							uiEventActionExecute.executeAction(actionCopy);
						} else {
							logger.warn(className, function, "event operationTarget[{}] IS element[{}]", operationElement, element);
						}
					} else {
						logger.warn(className, function, "operationTarget[{}] == element[{}]", operationElement, element);
					}
				} else {
					String opa = (String) action.getParameter(UIActionEventAttribute.OperationAction.toString());
					logger.info(className, function, "opa[{}]", opa);
					if ( null != opa ) {
						UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
						UIEventActionExecute_i uiEventActionExecute = uiEventActionExecuteMgr.getUIEventActionExecute(opa);
						if ( null != uiEventActionExecute ) {
							uiEventActionExecute.setUIEventActionProcessor(this);
							uiEventActionExecute.setUINameCard(uiNameCard);
							uiEventActionExecute.setLogPrefix(className);
							uiEventActionExecute.setInstance(className);
							uiEventActionExecute.setSimpleEventBus(simpleEventBus);
							uiEventActionExecute.setUIWidgetGeneric(uiWidgetGeneric);
							uiEventActionExecute.setUILayoutGeneric(uiLayoutGeneric);
							uiEventActionExecute.executeAction(action);
						} else {
							logger.warn(className, function, "uiEventActionExecute IS NULL");
						}
					} else {
						logger.warn(className, function, "opa IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "containue IS FALSE");
			}
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
	}
	
	@Override
	public void execute(UIEventAction uiEventAction, ExecuteAction_i executeActionHandler) {		
		final String function = prefix+" execute";
		logger.begin(className, function);
		if ( null != uiEventAction ) {
			
			UIEventActionProcessor.dumpUIEventAction(prefix, uiEventAction);
			
			boolean isWildMessage = false;
			boolean isSelfMessage = false;
			
			String operationElement = UIEventActionProcessor.getOperationElement(prefix, uiEventAction);
			logger.info(className, function, "operationElement[{}] element[{}]", operationElement, element);
			if ( null != operationElement ) {
				isWildMessage = operationElement.equals("*");
			}
			logger.info(className, function, "isWildMessage[{}]", isWildMessage);
				
			if ( null != operationElement && null != element) {
				isSelfMessage = operationElement.equals(element);
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
						for ( ActionAttribute actionSetAttribute : ActionAttribute.values() ) {
							String os = (String) uiEventAction.getParameter(actionSetAttribute.toString());
							logger.info(className, function, "loading executeActionSet os[{}]", os);
							executeActionSet(os, executeActionHandler);
						}
					} else if ( opt.equals(UIActionEventType.actionkey.toString()) ) { 
						logger.info(className, function, "IS actionkey");
						for ( ActionAttribute actionSetAttribute : ActionAttribute.values() ) {
							String os = (String) uiEventAction.getParameter(actionSetAttribute.toString());
							logger.info(className, function, "loading executeActionSet os[{}]", os);
							executeAction(os, executeActionHandler);
						}
					} else if ( opt.equals(UIActionEventType.action.toString()) ) { 
						logger.info(className, function, "IS action");
						executeAction(uiEventAction, executeActionHandler);
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
	
	private static String getParameter(String prefix, UIEventAction uiEventAction, String parameter) {
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
	private static String getOperationType(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" getOperationType";
		logger.begin(className, function);
		String operation = getParameter(prefix, uiEventAction, UIActionEventAttribute.OperationType.toString());
		logger.end(className, function);
		return operation;
	}
	private static String getOperationElement(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" getOperationElement";
		logger.begin(className, function);
		String operationElement = getParameter(prefix, uiEventAction, UIActionEventTargetAttribute.OperationElement.toString());
		logger.end(className, function);
		return operationElement;
	}
	
	public static void dumpUIEventAction(String prefix, UIEventAction uiEventAction) {
		final String function = prefix+" dumpUIEventAction";
		logger.begin(className, function);

		if ( null != uiEventAction ) {
			for ( String key : uiEventAction.getParameterKeys() ) {
				Object object = uiEventAction.getParameter(key);
				if ( object instanceof String ) {
					String string = (String)object;
					logger.info(className, function, "string[{}]", string);
				} else {
					logger.info(className, function, "object[{}]", object);
				}
			}
		} else {
			logger.warn(className, function, "uiEventAction IS NULL", prefix);
		}
		
		logger.end(className, function);
	}


}
