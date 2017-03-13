package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.GrcPointEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;

public class UIWidgetSocGrcPoint extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocGrcPoint.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {	
		}
		
		@Override
		public void onClick(ClickEvent event) {
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "os1["+os1+"]");
			
			if ( null != os1 ) {
				if ( os1.equals(GrcPointEvent.CurStatus.toString())) {
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					
					logger.info(className, function, "GRC Current Status");
										
					if ( null != obj1 && null != obj2 && null != obj3) {
						if ( obj1 instanceof String && obj2 instanceof String && obj3 instanceof String) {
							String scsenvid = (String)obj1;
							String dbalias = (String)obj2;
							String curStatusStr = (String)obj3;
							
							displayCurStatus(curStatusStr);
						} else {
							logger.warn(className, function, "obj1 or obj2 or obj3 IS NOT TYPE OF String");
						}
					} else {
						logger.warn(className, function, "obj1 or obj2 or obj3 IS NULL");
					}

				} else if ( os1.equals(GrcPointEvent.CurStep.toString())) {
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					
					logger.info(className, function, "GRC Current Step");
										
					if ( null != obj1 && null != obj2 && null != obj3) {
						if ( obj1 instanceof String && obj2 instanceof String && obj3 instanceof String) {
							String scsenvid = (String)obj1;
							String dbalias = (String)obj2;
							String curStepStr = (String)obj3;
							
							displayCurStep(curStepStr);
						} else {
							logger.warn(className, function, "obj1 or obj2 or obj3 IS NOT TYPE OF String");
						}
					} else {
						logger.warn(className, function, "obj1 or obj2 or obj3 IS NULL");
					}
				} else if ( os1.equals(GrcPointEvent.StepStatus.toString())) {
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					
					logger.info(className, function, "Step Status");
										
					if ( null != obj1 && null != obj2 && null != obj3) {
						if ( obj1 instanceof String && obj2 instanceof String && obj3 instanceof String) {
							String scsenvid = (String)obj1;
							String dbalias = (String)obj2;
							String stepStatusStr = (String)obj3;
							
							displayStepStatus(stepStatusStr);
						} else {
							logger.warn(className, function, "obj1 or obj2 or obj3 IS NOT TYPE OF String");
						}
					} else {
						logger.warn(className, function, "obj1 or obj2 or obj3 IS NULL");
					}
				} else if ( os1.equals(GrcPointEvent.DisplayMessage.toString())) {
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					
					logger.info(className, function, "Display Message");
										
					if ( null != obj1 && null != obj2 && null != obj3) {
						if ( obj1 instanceof String && obj2 instanceof String && obj3 instanceof String) {
							String scsenvid = (String)obj1;
							String dbalias = (String)obj2;
							String message = (String)obj3;
							
							displayMessage(message);
						} else {
							logger.warn(className, function, "obj1 or obj2 or obj3 IS NOT TYPE OF String");
						}
					} else {
						logger.warn(className, function, "obj1 or obj2 or obj3 IS NULL");
					}
				} else {
					// General Case
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe ["+oe+"]");
					logger.info(className, function, "os1["+os1+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							uiEventActionProcessor_i.executeActionSet(os1);
						}
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

//		String strUIWidgetGeneric = "UIWidgetGeneric";
//		String strHeader = "header";
//		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
//		if ( null != dictionariesCache ) {
//			targetDataGridColumnA	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A.toString(), strHeader);
//			targetDataGridA			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_A.toString(), strHeader);
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
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
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
	
	void displayMessage(String message) {
		uiWidgetGeneric.setWidgetValue("grcmessagevalue", message);
	}

	void displayCurStep(String stepStr) {
		uiWidgetGeneric.setWidgetValue("grccurstepvalue", stepStr);
	}
	
	void displayCurStatus(String statusStr) {
		String translatedGrcCurStatus = "&grcCurStatus" + statusStr;
		uiWidgetGeneric.setWidgetValue("grccurstatusvalue", translatedGrcCurStatus);
	}
	
	void displayStepStatus(String stepStatusStr) {
		String translatedStepStatus = "&grcStepStatus" + stepStatusStr;
		uiWidgetGeneric.setWidgetValue("grcstepstatusvalue", translatedStepStatus);
	}
}
