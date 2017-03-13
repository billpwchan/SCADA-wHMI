package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;


public class UIPanelAccessBar extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelAccessBar.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private final String UIPathUIPanelViewLayout	= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	@Override
	public void init() {
		final String function = "function";
		
		logger.begin(className, function);
		logger.info(className, function, "viewXMLFile[{}] optsXMLFile[{}]", viewXMLFile, optsXMLFile);

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
		uiEventActionProcessor_i.setElement(className);
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor_i.setEventBus(null);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					onButtonClick(element);
				} else {
					logger.warn(className, function, "onClickHandler onClickHandler button IS NULL");
				}
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					onUIEvent(uiEvent);
				}
			})
		);

		logger.end(className, function);
 		
	}
	
	private void setHistoryButton(UITaskHistory taskHistory) {
		final String function = "setHistoryButton";
		
		logger.begin(className, function);
		
		logger.info(className, function, "taskHistory.getTaskType()[{}]", taskHistory.getTaskType());
		
		Widget widget = null;
		WidgetStatus status = null;

		switch (taskHistory.getTaskType()) {
		case PreviousEnable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.previous.toString() );
			status = WidgetStatus.Up;
			break;
		case PreviousDisable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.previous.toString() );
			status = WidgetStatus.Disable;
			break;
		case NextEnable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.next.toString() );
			status = WidgetStatus.Up;
			break;
		case NextDisable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.next.toString() );
			status = WidgetStatus.Disable;
			break;
		default:
			break;
		}
		
		if ( null != widget ) {
			uiWidgetGeneric.setWidgetStatus(widget, status);
		}

		logger.end(className, function);
	}
	
	private void setSplitButton(UITaskSplit taskSplit) {
		final String function = "setSplitButton";
		
		logger.begin(className, function);
		
		logger.info(className, function, "taskSplit.getTaskType()[{}]", taskSplit.getTaskType());
		
		Widget widget = null;
		WidgetStatus status = null;
		
		switch (taskSplit.getTaskType()) {
		case HorizontalHightLight:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Down;
			break;
		case HorizontalEnable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Up;
			break;
		case HorizontalDisable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Disable;
			break;
		case VerticalHightLight:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Down;
			break;
		case VerticalEnable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Up;
			break;
		case VerticalDisable:
			widget = uiWidgetGeneric.getWidget( UIPanelAccessBar_i.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Disable;
			break;
		default:
			break;
		}
		
		if ( null != widget ) {
			uiWidgetGeneric.setWidgetStatus(widget, status);
		}

		logger.end(className, function);
	}
	
	void onUIEvent(UIEvent uiEvent ) {
		final String function = "onUIEvent";
		
		logger.begin(className, function);
		if ( null != uiEvent ) {
			
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if ( null != taskProvide ) 
			{
				if ( uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
					&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath()) ) {
					
					if ( taskProvide instanceof UITaskHistory ){
						
						logger.info(className, function, "taskProvide is TaskHistory");
						
						setHistoryButton((UITaskHistory)taskProvide);
						
					} else if ( taskProvide instanceof UITaskSplit){
						
						logger.info(className, function, "taskProvide is UITaskSplit");
					
						setSplitButton((UITaskSplit)taskProvide);
					}
				}
			}
		}
		logger.end(className, function);
	}
	
	void onButtonClick ( String element ) {
		final String function = "onButtonClick";
		
		logger.info(className, function, "elementValue[{}]", element);
		
		if ( null != element ) {
			
			if ( UIPanelAccessBar_i.WidgetArrtibute.logout.equalsName(element) ) {
				logger.info(className, function, "logout[{}]", UIPanelAccessBar_i.WidgetArrtibute.logout);
				
				if ( null != element ) {
					String actionsetkey = element;
					uiEventActionProcessor_i.executeActionSet(actionsetkey);
				}
				
			} else if ( UIPanelAccessBar_i.WidgetArrtibute.previous.equalsName(element) ) {
				
				logger.info(className, function, "previous[{}]", UIPanelAccessBar_i.WidgetArrtibute.previous);
				
				WidgetStatus status = uiWidgetGeneric.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.info(className, function, "strPreviousDisable");
				} else {
					logger.info(className, function, "strPrevious");
					
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(UIPathUIPanelViewLayout);
					taskHistory.setTaskType(UITaskHistory.TaskType.Previous);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
				
			} else if ( UIPanelAccessBar_i.WidgetArrtibute.next.equalsName(element) ) {
				
				logger.warn(className, function, "next["+UIPanelAccessBar_i.WidgetArrtibute.next+"]");
				
				WidgetStatus status = uiWidgetGeneric.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.info(className, function, "strNextDisable");
				} else {
					logger.info(className, function, "strNext");
			
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(UIPathUIPanelViewLayout);
					taskHistory.setTaskType(UITaskHistory.TaskType.Next);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
			} else if ( UIPanelAccessBar_i.WidgetArrtibute.splith.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetGeneric.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.info(className, function, "strSplitHDisable");
				} else {
					logger.info(className, function, "strSplitH");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(UIPathUIPanelViewLayout);
					taskSplit.setTaskType(UITaskSplit.SplitType.Horizontal);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( UIPanelAccessBar_i.WidgetArrtibute.splitv.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetGeneric.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.info(className, function, "strSplitVDisable");
				} else {
					logger.info(className, function, "strSplitV");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(UIPathUIPanelViewLayout);
					taskSplit.setTaskType(UITaskSplit.SplitType.Vertical);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( 
					UIPanelAccessBar_i.WidgetArrtibute.stationoperation.equalsName(element)  
					|| UIPanelAccessBar_i.WidgetArrtibute.dss.equalsName(element)
					|| UIPanelAccessBar_i.WidgetArrtibute.print.equalsName(element) 
					|| UIPanelAccessBar_i.WidgetArrtibute.help.equalsName(element)
					) {

				logger.info(className, function, "Calling uiEventActionProcessor executeActionSet element[{}]", element);
				
				uiEventActionProcessor_i.executeActionSet(element);
			}
		}
	}

}
