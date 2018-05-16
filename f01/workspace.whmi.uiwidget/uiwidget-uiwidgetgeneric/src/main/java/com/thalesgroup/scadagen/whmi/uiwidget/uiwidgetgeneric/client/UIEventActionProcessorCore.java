package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventSenderAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionExecute_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessorCore_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIGeneric;

public class UIEventActionProcessorCore implements UIEventActionProcessorCore_i {

	private final String className = this.getClass().getSimpleName();
	private final UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	UIEventActionProcessorCoreUtil uiEventActionProcessorCoreUtil = new UIEventActionProcessorCoreUtil();
	
	protected String prefix = null;
	public void setPrefix(String prefix) { this.prefix = prefix; }
	
	private String dictionariesCacheName;
	public void setDictionariesCacheName(final String dictionariesCacheName) {
		this.dictionariesCacheName = dictionariesCacheName;
	}
	
	private UINameCard uiNameCard;
	@Override
	public void setUINameCard(final UINameCard uiNameCard) {
		final String function = prefix+" setElement";
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
		} else {
			logger.warn(function, "uiNameCard IS NULL");
		}
	}

	@Override
	public String getName() {
		return className;
	}
	
	private String element;
	@Override
	public void setElement(final String element) {
		final String function = prefix+" setElement";
		if ( null != element ) {
			this.element = element;
		} else {
			logger.warn(function, "setElement IS NULL");
		}
	}
	@Override
	public String getElementName() { return this.element; }
	
	protected SimpleEventBus simpleEventBus = null;
	@Override
	public void setEventBus(final SimpleEventBus simpleEventBus) {
		final String function = prefix+" setEventBus";
		if ( null != simpleEventBus ) {
			this.simpleEventBus = simpleEventBus;
		} else {
			logger.warn(function, "simpleEventBus IS NULL");
		}
	}
	@Override
	public SimpleEventBus getEventBus() { return this.simpleEventBus; }
	
	protected UIGeneric uiGeneric = null;
	@Override
	public void setUIGeneric(final UIGeneric uiGeneric) {
		final String function = prefix+" setUIGeneric";
		if ( null != uiGeneric ) {
			this.uiGeneric = uiGeneric;
		} else {
			logger.warn(function, "uiWidgetGeneric IS NULL");
		}
	}

	protected String optsXMLFile;
	@Override
	public void setOptsXMLFile(final String optsXMLFile) { this.optsXMLFile = optsXMLFile; }
	@Override
	public String getOptsXMLFile() { return this.optsXMLFile; }
	
	protected String actionSetTagName = null;
	@Override
	public void setActionSetTagName(final String actionSetTagName) { this.actionSetTagName = actionSetTagName; }
	@Override
	public String getActionSetTagName() { return this.actionSetTagName; }
	
	protected String actionTagName = null;
	@Override
	public void setActionTagName(final String actionTagName) { this.actionTagName = actionTagName; }
	@Override
	public String getActionTagName() { return this.actionTagName; }
	
	protected UIEventActionMgr uiEventActionSetMgr = null;
	protected UIEventActionMgr uiEventActionMgr = null;
	@Override
	public void init() {
		final String function = prefix+" init";
		logger.begin(function);
		uiEventActionSetMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionSetTagName);
		uiEventActionSetMgr.init();
		uiEventActionMgr = new UIEventActionMgr(className, dictionariesCacheName, optsXMLFile, actionTagName);
		uiEventActionMgr.init();
		logger.end(function);
	}
	
	@Override
	public UIEventAction getUIEventActionSetMgr(final String actionsetkey) {
		return this.uiEventActionSetMgr.get(actionsetkey);
	}
	
	@Override
	public UIEventAction getUIEventActionMgr(final String actionkey) {
		return this.uiEventActionMgr.get(actionkey);
	}
	
	@Override
	public boolean executeActionSet(final String actionsetkey) {
		final String function = prefix+" executeActionSet optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		logger.debug(function, "actionsetkey[{}]", actionsetkey);
		boolean bContinue = true;
		logger.debug(function, "actionsetkey[{}]", actionsetkey);
		bContinue = executeActionSet(actionsetkey, null, null);
		logger.end(function);
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(final String actionsetkey, final Map<String, Map<String, Object>> override) {
		final String function = prefix+" executeActionSet optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		logger.debug(function, "actionsetkey[{}]", actionsetkey);
		boolean bContinue = true;
		bContinue = executeActionSet(actionsetkey, override, null);
		logger.end(function);
		return bContinue;
	}

	/**
	 * Execute ActionSet by key
	 * @param actionsetkey
	 * @param stractionset
	 * @param straction
	 */
	@Override
	public boolean executeActionSet(final String actionsetkey, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSet optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		logger.debug(function, "actionsetkey[{}]", actionsetkey);
		
		boolean bContinue = true;

		UIEventAction actionset = uiEventActionSetMgr.get(actionsetkey);
		if ( null != actionset ) {
			actionset.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.actionsetkey.toString());
			for ( String actionsetattributename : ActionAttribute.toStrings() ) {
				String actionsetattribute = (String) actionset.getParameter(actionsetattributename);
				if ( bContinue ) {
					if ( null != actionsetattribute ) {
						logger.debug(function, "actionsetattributename[{}] actionsetattribute[{}]", actionsetattributename, actionsetattribute);
						UIEventAction action = uiEventActionMgr.get(actionsetattribute);
						action.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
						if ( null != override ) {
							
							for ( Entry<String, Map<String, Object>> entry : override.entrySet() ) {
								String key = entry.getKey();
								Map<String, Object> parameters = entry.getValue();
								logger.debug(function, "key[{}] == actionsetattribute[{}]", key, actionsetattribute);
								if ( key.equals(actionsetattribute) ) {
									if ( null != parameters ) {
										for ( Entry<String, Object> parameter : parameters.entrySet() ) {
											String parameterKey		= parameter.getKey();
											Object parameterValue	= parameter.getValue();
											logger.debug(function, "parameter[{}] value[{}]", parameterKey, parameterValue);
											action.setParameter(parameterKey, parameterValue);
										}
									} else {
										logger.warn(function, "parameters IS NULL");
									}
								}
							}
						}
						bContinue = executeAction(action, override, executeActionHandler);
					}
				} else {
					logger.warn(function, "bContinue IS FALSE");
				}
			}
		} else {
			logger.warn(function, "actionsetkey[{}] actionset IS NULL", actionsetkey);
		}
		logger.end(function);
		
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(final UIEventAction action, final Map<String, Map<String, Object>> override) {
		final String function = prefix+" executeActionSet optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		boolean bContinue = true;
		bContinue = executeActionSet(action, override, null);
		logger.end(function);
		return bContinue;
	}
	
	@Override
	public boolean executeActionSet(final UIEventAction action, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeActionSet optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		boolean bContinue = true;
		if ( null != action ) {
			for ( String eventattributename : ActionAttribute.toStrings() ) {
				logger.debug(function, "eventattributename[{}]", eventattributename);
				String actionkey = (String) action.getParameter(eventattributename);
				logger.debug(function, "actionkey[{}]", actionkey);
				if ( bContinue ) {
					if ( null != actionkey ) {
						bContinue = executeAction(actionkey, override, executeActionHandler);
					} else {
						logger.warn(function, "actionkey IS NULL");
					}
				} else {
					logger.warn(function, "bContinue IS FALSE");
				}

			}
		} else {
			logger.warn(function, "action IS NULL");
		}
		logger.end(function);
		return bContinue;
	}
	
	@Override
	public boolean executeAction(final String actionkey, final Map<String, Map<String, Object>> override) {
		final String function = prefix+" executeAction optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		logger.debug(function, "actionkey[{}]", actionkey);
		boolean bContinue = true;
		bContinue = executeAction(actionkey, override, null);
		logger.end(function);
		return bContinue;
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param actionkey: UIEventAction key
	 */
	@Override
	public boolean executeAction(final String actionkey, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeAction optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		logger.debug(function, "actionkey[{}]", actionkey);
		boolean bContinue = true;
		UIEventAction action = uiEventActionMgr.get(actionkey);
		if ( null != action ) {
			action.setParameter(UIActionEventAttribute.OperationType.toString(), UIActionEventType.action.toString());
			bContinue = executeAction(action, override, executeActionHandler);
		} else {
			logger.warn(function, "action IS NULL");
		}
		logger.end(function);
		return bContinue;
	}
	
	@Override
	public boolean executeAction(final UIEventAction action, final Map<String, Map<String, Object>> override) {
		final String function = prefix+" executeAction optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		boolean bContinue = true;
		bContinue = executeAction(action, override, null);
		logger.begin(function);
		return bContinue;
	}
	
	/**
	 * Execute Action (Widget/Event)
	 * @param action: UIEventAction instance
	 */
	@Override
	public boolean executeAction(final UIEventAction action, final Map<String, Map<String, Object>> override, final UIExecuteActionHandler_i executeActionHandler) {
		final String function = prefix+" executeAction optsXMLFile["+optsXMLFile+"]";
		logger.begin(function);
		
		boolean bContinue = true;
		
		if ( null != action ) {
			
			uiEventActionProcessorCoreUtil.dumpUIEventAction(prefix, action);

			if ( null != executeActionHandler ) bContinue = executeActionHandler.executeHandler(action);
			
			if ( bContinue ) {
				
				String spt = (String) action.getParameter(UIActionEventSenderAttribute.SenderOperation.toString());
				
				logger.debug(function, "spt[{}]", spt);
				
				if ( null != spt && spt.equals(UIActionEventType.event.toString()) ) {
					logger.debug(function, "spt[{}] IS event", spt);
					String operationElement = uiEventActionProcessorCoreUtil.getOperationElement(prefix, action);
					logger.debug(function, "operationElement[{}] == element[{}]", operationElement, element);
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
							logger.warn(function, "event operationTarget[{}] IS element[{}]", operationElement, element);
						}
					} else {
						logger.warn(function, "operationTarget[{}] == element[{}]", operationElement, element);
					}
				} else {
					String opa = (String) action.getParameter(UIActionEventAttribute.OperationAction.toString());
					logger.debug(function, "opa[{}]", opa);
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
							logger.warn(function, "uiEventActionExecute IS NULL");
						}
					} else {
						logger.warn(function, "opa IS NULL");
					}
				}
			} else {
				logger.warn(function, "bContinue IS FALSE");
			}
		} else {
			logger.warn(function, "action IS NULL");
		}
		logger.end(function);
		
		return bContinue;
	}

}
