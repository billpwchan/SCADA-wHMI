package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionMgrOld;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIOptionCaches;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIOptionMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewOperation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionExecute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.FilterParameter;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetFilter_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetFilter extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetFilter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 		= null;

	private final String strSet1			= "set1";
	private final String strClear			= "clear";
	
	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	private String strHeader = "header";
	private String strOption = "option";

	@Override
	public Widget getWidget(String element) {
		Widget widget = null;
		widget = uiWidgetGeneric.getWidget(element);
		return widget;
	}
	
	private UIEventAction getUIEventAction(String element) {
		final String function = "getUIEventAction";
		
		logger.begin(className, function);
		
		logger.info(className, function, "element[{}]", element);
		UIEventAction action = null;
		if ( null != element ) {
		
			action = mgr.getUIEventAction(element);

			if ( null == action ) {
				logger.warn(className, function, "element[{}] IS UNKNOW TYPE", element);
			}
		} else {
			logger.warn(className, function, "element IS NULL");
		}
		logger.end(className, function);
		return action;
	}
	
	private void fireUIEventAction(UIEventAction uiEventAction) {
		final String function = "fireUIEventAction";
		
		logger.begin(className, function);
		if ( null != uiEventAction ) {
			
			if ( logger.isInfoEnabled() ) {
				for ( String parameterKey : uiEventAction.getParameterKeys() ) {
					String parameterValue = (String) uiEventAction.getParameter(parameterKey);
					logger.info(className, function, "parameterKey[{}] parameterValue[{}]", parameterKey, parameterValue);
				}
			}

			eventBus.fireEventFromSource(uiEventAction, this);
		} else {
			logger.warn(className, function, "fireUIEventAction IS NULL");
		}
		logger.begin(className, function);
	}
	
	private void setFilter(String element) {
		final String function = "setFilter";
		
		logger.begin(className, function);
		
		logger.info(className, function, "element[{}]", element);
		if ( null != element ) {
			
			UIEventAction action = getUIEventAction(element);
			if ( null != action ) {
				fireUIEventAction(action);
			} else {
				logger.warn(className, function, "action IS NULL");
			}
			
			if ( element.equals(strClear) ) {		
				uiEventActionExecute.action("SetWidgetStatus", strClear, "Disable");
			}

		} else {
			logger.warn(className, function, "element IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private void onWidgetEvent (Widget widget) {
		final String function = "onWidgetEvent";
		if ( null != widget ) {
			String element = uiWidgetGeneric.getWidgetElement(widget);
			setFilter(element);
		} else {
			logger.warn(className, function, "widget IS NULL");
		}
	}
	
	private void onButtonValueChange(ValueChangeEvent<String> event) {
		final String function = "onButtonValueChange";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.warn(className, function, "event IS NULL");
		}
	}
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			onWidgetEvent(widget);
		} else {
			logger.warn(className, function, "event IS NULL");
		}
	}
		
	void onUIEvent(UIEvent uiEvent ) {
	}
		
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		if ( null != uiEventAction ) {
			String ot	= (String) uiEventAction.getParameter(ViewAttribute.OperationTarget.toString());
			String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "ot["+ot+"]");
			logger.info(className, function, "op["+op+"]");
			logger.info(className, function, "os1["+os1+"]");
			
			if ( null != ot ) {
				if ( ot.equals(className) ) {
					
					if ( null != op ) {
						if ( op.equals("SetFilter") ) {
							
							setFilter(os1);
							
						} else if ( UIEventActionExecute.isSupportedAction(op) ) {
	
							uiEventActionExecute.action(uiEventAction);
	
						} 
					}
				}
			}
		} else {
			logger.warn(className, function, "uiEventAction IS NULL");
		}
		
		logger.end(className, function);
	}

	private UIEventActionMgrOld mgr = null;
	private UIEventActionExecute uiEventActionExecute = null;
	// Create Filter Operation Index
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "Loading strEventBusName[{}]", strEventBusName);
		
		mgr = new UIEventActionMgrOld(className, strUIWidgetGeneric, optsXMLFile);
		mgr.initActionKeys(strHeader, ViewOperation.toStrings());
		mgr.initActions(strOption, ViewAttribute.Operation.toString(), FilterParameter.toStrings());
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		uiEventActionExecute = new UIEventActionExecute(className, uiWidgetGeneric);
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnValueChangeHandler() {
			
			@Override
			public void setUIWidgetEventOnValueChangeHandler(ValueChangeEvent<String> event) {
				// TODO Auto-generated method stub
				onButtonValueChange(event);
			}
		});
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				onButton(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);
		
		uiEventActionExecute.action("SetWidgetStatus", strSet1, "Down");
		uiEventActionExecute.action("SetWidgetStatus", strClear, "Disable");
		
		{
			String className = UIWidgetUtil.getClassSimpleName(UIOptionMgr.class.getName());
			logger.info(className, function, "Testing ["+className+"] Begin");
			
			UIOptionMgr uiOptionMgr = new UIOptionMgr(className);
			
			HashMap<String, HashMap<String, String>> options = uiOptionMgr.getOptions(strUIWidgetGeneric, optsXMLFile, "option");
			if ( null != options ) {
				for ( Entry<String, HashMap<String, String>> option : options.entrySet() ) {
					if ( null != option ) {
						String optionKey = option.getKey();
						HashMap<String, String> optionValue = option.getValue();
						logger.info(className, function, "optionKey[{}]", optionKey);
						if ( null != optionValue ) {
							for ( Entry<String, String> parameters : optionValue.entrySet() ) {
								String key = parameters.getKey();
								String value = parameters.getValue();
								logger.info(className, function, "optionKey[{}] key[{}] value[{}]", new Object[]{optionKey, key, value});
							}
						} else {
							logger.warn(className, function, "optionValue IS NULL");
						}
					} else {
						logger.warn(className, function, "options IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "options IS NULL");
			}
			logger.info(className, function, "Testing "+className+" End");
		}
		
		{
			String className = UIWidgetUtil.getClassSimpleName(UIOptionCaches.class.getName());
			logger.info(className, function, "Testing ["+className+"] Begin");
			
			UIOptionCaches optionCaches = new UIOptionCaches(className, strUIWidgetGeneric, optsXMLFile, "option");
			
			optionCaches.init();
		
			String [] optionKeys = optionCaches.getKeys();
			if ( null != optionKeys ) {
				for (String optionKey : optionKeys ) {
					logger.info(className, function, "optionKey[{}]", optionKey);
				}
			} else {
				logger.warn(className, function, "optionKeys IS NULL");
			}
			
			Set<Entry<String, HashMap<String, String>>> options = optionCaches.getOptions();
			if ( null != options ) {
				for ( Entry<String, HashMap<String, String>> entry : options ) {
					if ( null != entry ) {
						String optionKey = entry.getKey();
						HashMap<String, String> option = entry.getValue();
						logger.info(className, function, "key[{}]", optionKey);
						if ( null != option ) {
							for ( String elementKey : option.keySet() ) {
								logger.info(className, function, "key[{}] elementKey[{}]", optionKey, elementKey);
								String value = option.get(elementKey);
								logger.info(className, function, "key[{}] elementKey[{}] value[{}]", new Object[]{optionKey, elementKey, value});
							}
						} else {
							logger.warn(className, function, "option IS NULL");
						}
					} else {
						logger.warn(className, function, "entry IS NULL");
					}
				}
			} else {
				logger.warn(className, function, "optionKeys IS NULL");
			}
			logger.info(className, function, "Testing "+className+" End");
		}

		{
			String className = UIWidgetUtil.getClassSimpleName(UIEventActionMgr.class.getName());
			logger.info(className, function, "Testing ["+className+"] Begin");
			UIEventActionMgr uiEventActionMgr = new UIEventActionMgr(className, strUIWidgetGeneric, optsXMLFile, "option");
			uiEventActionMgr.init();
			
			String [] uiEventActionKeys = uiEventActionMgr.getKeys();
			if ( null != uiEventActionKeys ) {
				for (String uiEventActionKey : uiEventActionKeys ) {
					logger.info(className, function, "uiEventActionKey[{}]", uiEventActionKey);
				}
			} else {
				logger.warn(className, function, "uiEventActionKeys IS NULL");
			}
		
			Set<Entry<String, UIEventAction>> uiEventActions = uiEventActionMgr.gets();
			if ( null != uiEventActions ) {
				for ( Entry<String, UIEventAction> entry : uiEventActions ) {
					if ( null != entry ) {
						String uiEventActionKey = entry.getKey();
						UIEventAction uiEventAction = entry.getValue();
						logger.info(className, function, "key[{}]", uiEventActionKey);
						if ( null != uiEventAction ) {
							for ( String key : uiEventAction.getParameterKeys() ) {
								logger.info(className, function, "key[{}] key[{}]", uiEventActionKey, key);
								String value = (String) uiEventAction.getParameter(key);
								logger.info(className, function, "key[{}] key[{}] value[{}]", new Object[]{uiEventActionKey, key, value});
							}
						} else {
							logger.warn(className, function, "uiEventAction IS NULL");
						}
					} else {
						logger.warn(className, function, "entry IS NULL");
					}
					
				}
			} else {
				logger.warn(className, function, "uiEventActions IS NULL");
			}
			logger.info(className, function, "Testing "+className+" End");
		}
		
		
		
		
		logger.end(className, function);
	}

}
