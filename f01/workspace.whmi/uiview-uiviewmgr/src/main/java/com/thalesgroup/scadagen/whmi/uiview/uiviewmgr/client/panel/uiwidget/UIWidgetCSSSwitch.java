package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Panel;
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
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetCSSSwitch extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCSSSwitch.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus			= null;

	private UILayoutGeneric uiLayoutGeneric	= null;
	
	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			if ( null != uiEventAction ) {
				
				String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
				
				logger.info(className, function, "oe["+oe+"]");
				
				if ( null != oe ) {
					if ( oe.equals(element) ) {
						
						String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
						
						logger.info(className, function, "os1["+os1+"]");
				
						HashMap<String, HashMap<String, Object>> override = null;
						
						uiEventActionProcessor_i.executeActionSet(os1, override, new ExecuteAction_i() {
							
							@Override
							public boolean executeHandler(UIEventAction uiEventAction) {
								String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
								String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
								String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
								String os4	= (String) uiEventAction.getParameter(ViewAttribute.OperationString4.toString());
		
								logger.info(className, function, "os1["+os1+"]");
								logger.info(className, function, "os2["+os2+"]");
								logger.info(className, function, "os3["+os3+"]");
								logger.info(className, function, "os4["+os4+"]");						
								
								if ( os1.equals("ModifyCSS") ) {
									
									if ( null != os2 && null != os3 && null != os4 ) {
										String cssElementName = os2;
										String cssValueName = os3;
										boolean applyRemove = os4.equals("true");
										modifyCss(cssElementName, cssValueName, applyRemove);
									} else {
										logger.warn(className, function, "os2 IS NULL or os3 IS NULL or os4 IS NULL ");
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

		uiLayoutGeneric = new UILayoutGeneric();
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
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
	
	private void modifyCss(String element, String style, boolean set ) {
		final String function = "modifyCss";
		
		logger.info(className, function, "element[{}] style[{}] set[{}]", new Object[]{element, style, set});
		
		UIWidget_i uiWidget = uiLayoutGeneric.getUIWidget(element);
		if ( null != uiWidget ) {
			Panel panel = uiWidget.getMainPanel();
			if ( null != panel ) {
				if ( null != style && style.trim().length() > 0 ) {
					if ( set ) {
						panel.addStyleName(style);
					} else {
						panel.removeStyleName(style);
					}
				} else {
					logger.warn(className, function, "element[{}] style IS NULL OR length IS ZERO", element);
				}
			} else {
				logger.warn(className, function, "element[{}] panel IS NULL", element);
			}			
		} else {
			logger.warn(className, function, "element[{}] uiWidget IS NULL", element);
		}
	}
}
