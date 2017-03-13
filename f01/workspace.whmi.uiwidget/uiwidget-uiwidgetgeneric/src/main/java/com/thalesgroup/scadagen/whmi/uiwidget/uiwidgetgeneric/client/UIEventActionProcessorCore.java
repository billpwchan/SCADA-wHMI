package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventSenderAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorCore_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;

public class UIEventActionProcessorCore implements UIEventActionProcessorCore_i {

	private static final String className = UIWidgetUtil.getClassSimpleName(UIEventActionProcessorCore.class.getName());
	private static UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	protected String prefix = null;
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
	
	protected UIGeneric uiGeneric = null;
	@Override
	public void setUIGeneric(UIGeneric uiGeneric) {
		final String function = prefix+" setUIGeneric";
		if ( null != uiGeneric ) {
			this.uiGeneric = uiGeneric;
		} else {
			logger.warn(className, function, "uiWidgetGeneric IS NULL");
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
		final String function = prefix+" init";
		logger.begin(className, function);
		uiEventActionSetMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionSetTagName);
		uiEventActionSetMgr.init();
		uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionTagName);
		uiEventActionMgr.init();
		logger.end(className, function);
	}
	
	@Override
	public UIEventAction getUIEventActionSetMgr(String actionsetkey) {
		return this.uiEventActionSetMgr.get(actionsetkey);
	}
	
	@Override
	public UIEventAction getUIEventActionMgr(String actionkey) {
		return this.uiEventActionMgr.get(actionkey);
	}
	
	@Override
	public boolean executeActionSet(String actionsetkey) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		boolean bContinue = true;
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		bContinue = executeActionSet(actionsetkey, null, null);
		logger.end(className, function);
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		boolean bContinue = true;
		bContinue = executeActionSet(actionsetkey, override, null);
		logger.end(className, function);
		return bContinue;
	}

	/**
	 * Execute ActionSet by key
	 * @param actionsetkey
	 * @param stractionset
	 * @param straction
	 */
	@Override
	public boolean executeActionSet(String actionsetkey, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		logger.info(className, function, "actionsetkey[{}]", actionsetkey);
		
		boolean bContinue = true;

		UIEventAction actionset = uiEventActionSetMgr.get(actionsetkey);
		if ( null != actionset ) {
			actionset.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
			for ( String actionsetattributename : ActionAttribute.toStrings() ) {
				logger.info(className, function, "actionsetattributename[{}]", actionsetattributename);
				String actionsetattribute = (String) actionset.getParameter(actionsetattributename);
				logger.info(className, function, "actionsetattribute[{}]", actionsetattribute);
				
				if ( bContinue ) {
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
						bContinue = executeAction(action, override, executeActionHandler);
					}
				} else {
					logger.warn(className, function, "bContinue IS FALSE");
				}
			}
		} else {
			logger.warn(className, function, "actionsetkey[{}] actionset IS NULL", actionsetkey);
		}
		logger.end(className, function);
		
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeActionSet(action, override, null);
		logger.end(className, function);
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(UIEventAction action, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSet";
		logger.begin(className, function);
		boolean bContinue = true;
		if ( null != action ) {
			for ( String eventattributename : ActionAttribute.toStrings() ) {
				logger.info(className, function, "eventattributename[{}]", eventattributename);
				String actionkey = (String) action.getParameter(eventattributename);
				logger.info(className, function, "actionkey[{}]", actionkey);
				if ( bContinue ) {
					if ( null != actionkey ) {
						bContinue = executeAction(actionkey, override, executeActionHandler);
					} else {
						logger.warn(className, function, "actionkey IS NULL");
					}
				} else {
					logger.warn(className, function, "bContinue IS FALSE");
				}

			}
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
		return bContinue;
	}
	
	@Override
	public boolean executeAction(String actionkey, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		logger.info(className, function, "actionkey[{}]", actionkey);
		boolean bContinue = true;
		bContinue = executeAction(actionkey, override, null);
		logger.end(className, function);
		return bContinue;
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param actionkey: UIEventAction key
	 */
	@Override
	public boolean executeAction(String actionkey, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		logger.info(className, function, "actionkey[{}]", actionkey);
		boolean bContinue = true;
		UIEventAction action = uiEventActionMgr.get(actionkey);
		if ( null != action ) {
			action.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
			bContinue = executeAction(action, override, executeActionHandler);
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
		return bContinue;
	}
	
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		boolean bContinue = true;
		bContinue = executeAction(action, override, null);
		logger.begin(className, function);
		return bContinue;
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param action: UIEventAction instance
	 */
	@Override
	public boolean executeAction(UIEventAction action, HashMap<String, HashMap<String, Object>> override, UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeAction";
		logger.begin(className, function);
		
		boolean bContinue = true;
		
		if ( null != action ) {
			
			dumpUIEventAction(prefix, action);

			if ( null != executeActionHandler ) bContinue = executeActionHandler.executeHandler(action);
			
			if ( bContinue ) {
				
				String spt = (String) action.getParameter(UIActionEventSenderAttribute.SenderOperation.toString());
				
				logger.info(className, function, "spt[{}]", spt);
				
				if ( null != spt && spt.equals(UIActionEventType.event.toString()) ) {
					logger.info(className, function, "spt[{}] IS event", spt);
					String operationElement = UIEventActionProcessorCore.getOperationElement(prefix, action);
					logger.info(className, function, "operationElement[{}] == element[{}]", operationElement, element);
					if ( null == operationElement || ( null != operationElement && ! operationElement.equals(element)) ) {
						if ( null != simpleEventBus ) {
							
							// Make a copy
							UIEventAction actionCopy = new UIEventAction(action);
							// Remove the SenderOperation when it done
							actionCopy.removeParameter(UIActionEventSenderAttribute.SenderOperation.toString());
							
							UIEventActionExecuteMgr uiEventActionExecuteMgr = UIEventActionExecuteMgr.getInstance();
							UIEventActionExecute_i uiEventActionExecute_i = uiEventActionExecuteMgr.getUIEventActionExecute(UIActionEventType.event.toString());
//							UIEventActionExecute_i uiEventActionExecute_i = new UIEventActionBusFire();
							uiEventActionExecute_i.setLogPrefix(className);
							uiEventActionExecute_i.setSimpleEventBus(simpleEventBus);
							bContinue = uiEventActionExecute_i.executeAction(actionCopy, override);
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
						UIEventActionExecute_i uiEventActionExecute_i = uiEventActionExecuteMgr.getUIEventActionExecute(opa);
						if ( null != uiEventActionExecute_i ) {
							uiEventActionExecute_i.setUIEventActionProcessor(this);
							uiEventActionExecute_i.setUINameCard(uiNameCard);
							uiEventActionExecute_i.setLogPrefix(className);
							uiEventActionExecute_i.setInstance(className);
							uiEventActionExecute_i.setSimpleEventBus(simpleEventBus);
							uiEventActionExecute_i.setUIGeneric(uiGeneric);
							bContinue = uiEventActionExecute_i.executeAction(action, override);
						} else {
							logger.warn(className, function, "uiEventActionExecute IS NULL");
						}
					} else {
						logger.warn(className, function, "opa IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "bContinue IS FALSE");
			}
		} else {
			logger.warn(className, function, "action IS NULL");
		}
		logger.end(className, function);
		
		return bContinue;
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
