package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetConfiguration extends UIWidget_i {
	
	private String className = UIWidgetUtil.getClassSimpleName(UIWidgetConfiguration.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private final String strSetWidgetValue = "SetWidgetValue";
	
	private final String strSetWidgetStatus = "SetWidgetStatus";
	
	private final String supportOperations[] = new String[]{strSetWidgetValue, strSetWidgetStatus};
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
//	private HashMap<String, String> widgetConfigurationParameters = new HashMap<String, String>();
	
//	private UIWidgetCtrl_i uiWidgetCtrlExternal = null;
//	public void setUIWidgetCtrl(UIWidgetCtrl_i uiWidgetCtrlExternal) {this.uiWidgetCtrlExternal = uiWidgetCtrlExternal;}
	
	// Override the UIWidgetAccessable_i
	// Build the connection between UIWidgetConfiguration interface and UIWidget_i instance
//	@Override
//	public void setParameter(String key, Object value) { uiWidgetGeneric.setParameter(key, value); }
//	@Override
//	public Object getParameter(String key) { return uiWidgetGeneric.getParameter(key); }
//	@Override
//	public boolean containsParameterKey(String key) { return uiWidgetGeneric.containsParameterKey(key); }

	@Override
	public Widget getWidget(String widget) { return uiWidgetGeneric.getWidget(widget); }
	@Override
	public String getWidgetElement(Widget widget) { return uiWidgetGeneric.getWidgetElement(widget); }
	
	@Override
	public String [] getUIWidgetElements() { return uiWidgetGeneric.getUIWidgetElements(); }
	
	@Override
	public void setWidgetValue(String element, String value) { uiWidgetGeneric.setWidgetValue(element, value); }
	@Override
	public String getWidgetValue(String element) { return uiWidgetGeneric.getWidgetValue(element); }
	
	@Override
	public WidgetStatus getWidgetStatus(String element) { return uiWidgetGeneric.getWidgetStatus(element); }
	@Override
	public void setWidgetStatus(String element, WidgetStatus status) { uiWidgetGeneric.setWidgetStatus(element, status);}
	
//	@Override
//	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) { uiWidgetGeneric.setUIWidgetEvent(uiWidgetEvent); }
//	@Override
//	public UIWidgetEvent getUIWidgetEvent(UIWidgetEvent uiWidgetEvent) { return uiWidgetGeneric.getUIWidgetEvent(uiWidgetEvent); }
	// End of override UIWidgetAccessable_i

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {

			// Call external handling
			if ( null != ctrlHandler ) ctrlHandler.onUIEvent(uiEvent);
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String function = "onClick";
			
			logger.begin(className, function);
			
			if ( null != event ) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(className, function, "element[{}]", element);
					if ( null != element ) {
						String actionsetkey = element;
						
						HashMap<String, HashMap<String, Object>> override = null;
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
					}
				}
			}
			
			// Call external handling
			if ( null != ctrlHandler ) ctrlHandler.onClick(event);
			
			logger.begin(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventActionReceived) {
			final String function = "onActionReceived";

			logger.begin(className, function);

			if (null != uiEventActionReceived) {

				String oe = (String) uiEventActionReceived.getParameter(UIActionEventTargetAttribute.OperationElement.toString());

				logger.info(className, function, "oe[" + oe + "]");

				if (null != oe) {
					
					if (oe.equals(element)) {

						String os1 = (String) uiEventActionReceived.getParameter(ActionAttribute.OperationString1.toString());
						logger.info(className, function, "os1[" + os1 + "]");
						
						HashMap<String, HashMap<String, Object>> override = null;
						
						for ( String actionkey : supportOperations ) {
							
							logger.info(className, function, "operation[{}].equals(os1[{}])", actionkey, os1);
							
							if ( actionkey.equals(os1) ) {
								
								override = new HashMap<String, HashMap<String, Object>>();
								
								HashMap<String, Object> parameters = new HashMap<String, Object>();
								
								logger.info(className, function, "os1[" + os1 + "]");
								
								Object obj1 = uiEventActionReceived.getParameter(ActionAttribute.OperationString2.toString());
								Object obj2 = uiEventActionReceived.getParameter(ActionAttribute.OperationString3.toString());
								
								logger.info(className, function, "update Counter Values");
								
								logger.info(className, function, "obj1[{}]", obj1);
								logger.info(className, function, "obj2[{}]", obj2);
								
								if ( null != obj1 && null != obj2 ) {
									
									String key = obj1.toString();
									String value = obj2.toString();
									
									logger.info(className, function, "key[{}]", key);
									logger.info(className, function, "value[{}]", value);
									
									parameters.put(ActionAttribute.OperationString2.toString(), key);
									parameters.put(ActionAttribute.OperationString3.toString(), value);
								}
								
								override.put(actionkey, parameters);
								
							}
						}
						
						uiEventActionProcessor_i.executeActionSet(os1, override);

					}
				}
			}
			
			// Call external handling
			if ( null != ctrlHandler ) ctrlHandler.onActionReceived(uiEventActionReceived);
			
			logger.end(className, function);
		}
	};
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		className += " [" + element + "] ";
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

//		String strUIWidgetGeneric = "UIWidgetGeneric";
//		String strHeader = "header";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
//		if ( null != dictionariesCache ) {
//			for ( String key : UIWidgetConfigurationParameter.toStrings() ) {
//				String value = dictionariesCache.getStringValue(optsXMLFile, key, strHeader);
//				widgetConfigurationParameters.put(key, value);
//				
//				logger.info(className, function, "key[{}] value[{}]", key, value);
//			}
//		}
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName(strUIWidgetGeneric);
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

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
