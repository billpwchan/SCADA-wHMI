package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBusFire;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionMgrOld;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionExecute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewOperation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.WidgetParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSFilter_i.ViewActionKeyOperation;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSFilter_i.ViewActionSetKey;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetCSSFilter extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCSSFilter.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 			= null;

	private String strUIWidgetGeneric = "UIWidgetGeneric";
	private String strHeader = "header";
	private String strOption = "option";

	private UIWidgetGeneric uiWidgetGeneric	= null;
	
	private void setFilter(String element) {
		final String function = "setFilter";
		
		logger.begin(className, function);
		if ( null != element ) {
			
			logger.info(className, function, "element[{}]", element);

			UIEventActionBusFire uiActionEventExecute = new UIEventActionBusFire(className, eventBus);
			
			uiActionEventExecute.executeActionSet(mgr.getUIEventActionOperations(), element, mgr.getUIEventActions());

		} else {
			logger.warn(className, function, "element IS NULL");
		}
		
		logger.end(className, function);
	}
	
	private void onWidgetEvent (Widget widget) {
		final String function = "onWidgetEvent";
		logger.begin(className, function);
		if ( null != widget ) {
			String element = uiWidgetGeneric.getWidgetElement(widget);
			logger.info(className, function, "element[{}]", element);
			setFilter(element);
		} else {
			logger.warn(className, function, "widget IS NULL");
		}
		logger.end(className, function);
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
			String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
			String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
			
			logger.info(className, function, "op["+op+"]");
			logger.info(className, function, "os1["+os1+"]");
			logger.info(className, function, "os2["+os2+"]");
			logger.info(className, function, "os3["+os3+"]");
			
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
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(WidgetParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		
		mgr = new UIEventActionMgrOld(className,strUIWidgetGeneric, optsXMLFile);
		
		mgr.initActionKeys(strHeader, ViewOperation.toStrings());
		mgr.initActions(strOption, ViewAttribute.Operation.toString(), ViewAttribute.toStrings());
		
		mgr.initActionOperationKeys(strHeader, ViewActionSetKey.toStrings());
		mgr.initActionOperations(strOption, ViewAttribute.Operation.toString(), ViewActionKeyOperation.toStrings());

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

		uiEventActionExecute.action("SetWidgetStatus", "set0", "Down");
		
		logger.end(className, function);
	}

}
