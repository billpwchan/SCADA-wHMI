package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetBlackboard extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetBlackboard.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus = null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onActionReceived(final UIEventAction uiEventActionReceived) {
			final String function = "onActionReceived";

			logger.begin(className, function);

			if (null != uiEventActionReceived) {

				String oe = (String) uiEventActionReceived
						.getParameter(UIActionEventTargetAttribute.OperationElement.toString());

				logger.info(className, function, "oe[" + oe + "]");

				if (null != oe) {
					if (oe.equals(element)) {

						String os1 = (String) uiEventActionReceived.getParameter(ViewAttribute.OperationString1.toString());
						final String os1e = os1;

						logger.info(className, function, "os1[" + os1 + "]");

						uiEventActionProcessor_i.executeActionSet(os1, new ExecuteAction_i() {

							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								
								String os1 = (String) uiEventAction
										.getParameter(ViewAttribute.OperationString1.toString());
								String os2 = (String) uiEventAction
										.getParameter(ViewAttribute.OperationString2.toString());
								String os3 = (String) uiEventAction
										.getParameter(ViewAttribute.OperationString3.toString());
								String os4 = (String) uiEventAction
										.getParameter(ViewAttribute.OperationString4.toString());

								logger.info(className, function, "os1[" + os1 + "]");
								logger.info(className, function, "os2[" + os2 + "]");
								logger.info(className, function, "os3[" + os3 + "]");
								logger.info(className, function, "os4[" + os4 + "]");
								
								logger.info(className, function, "executeHandler os1e[{}] os1[{}]", os1e, os1);
								
								if ( /*os1e.equals("CounterValueChanged") &&*/ os1.equals("SetWidgetValue") || os1.equals("SetWidgetStatus") ) {
									
									Object obj1 = uiEventActionReceived.getParameter(ViewAttribute.OperationString2.toString());
									Object obj2 = uiEventActionReceived.getParameter(ViewAttribute.OperationString3.toString());
									
									logger.info(className, function, "update Counter Values");
									
									logger.info(className, function, "obj1[{}]", obj1);
									logger.info(className, function, "obj2[{}]", obj2);
									
									if ( null != obj1 && null != obj2 ) {
										
										String key = obj1.toString();
										String value = obj2.toString();
										
										logger.info(className, function, "key[{}]", key);
										logger.info(className, function, "value[{}]", value);
										
										uiEventAction.setParameter(ViewAttribute.OperationString2.toString(), key);
										uiEventAction.setParameter(ViewAttribute.OperationString3.toString(), value);
									} else {
										logger.warn(className, function, "obj1 IS NULL or obj2 IS NULL");
									}

								}

								return true;
							}
						});
					}
				}
			}

			logger.end(className, function);
		}
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		rootPanel = uiWidgetGeneric.getMainPanel();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIWidgetGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();

		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
					}
				}
			})
		);

		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(className, function);
	}

}
