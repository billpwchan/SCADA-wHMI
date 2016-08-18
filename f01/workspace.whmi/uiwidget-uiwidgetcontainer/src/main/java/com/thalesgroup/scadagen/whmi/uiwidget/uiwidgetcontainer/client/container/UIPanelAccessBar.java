package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;


public class UIPanelAccessBar extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelAccessBar.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String UIPathUIPanelScreen		= ":UIGws:UIPanelScreen";
	private final String UIPathUIScreenMMI			= ":UIGws:UIPanelScreen:UIScreenMMI";
	private final String UIPathUIPanelViewLayout	= ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	private UIWidgetGeneric uiWidgetAccessBarButton = null;
	private String strUIPanelAccessBarButton		= "UIPanelAccessBarButton.xml";
	
	@Override
	public void init() {
		final String function = "function";
		
		logger.info(className, function, "Begin");
		logger.info(className, function, "xmlFile[{}]", xmlFile);

		uiWidgetAccessBarButton = new UIWidgetGeneric();
		uiWidgetAccessBarButton.setUINameCard(this.uiNameCard);
		uiWidgetAccessBarButton.setXMLFile(strUIPanelAccessBarButton);
		uiWidgetAccessBarButton.init();
		uiWidgetAccessBarButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetAccessBarButton.getWidgetElement(widget);
					onButtonClick(element);
				} else {
					logger.error(className, function, "onClickHandler onClickHandler button IS NULL");
				}
			}
		});
		
		rootPanel = uiWidgetAccessBarButton.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					onUIEvent(uiEvent);
				}
			})
		);

 		logger.info(className, function, "End");
 		
	}
	
	private void setHistoryButton(UITaskHistory taskHistory) {
		final String function = "setHistoryButton";
		
		logger.info(className, function, "Begin");
		
		logger.error(className, function, "taskHistory.getTaskType()[{}]", taskHistory.getTaskType());
		
		Widget widget = null;
		WidgetStatus status = null;

		switch (taskHistory.getTaskType()) {
		case PreviousEnable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.previous.toString() );
			status = WidgetStatus.Up;
			break;
		case PreviousDisable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.previous.toString() );
			status = WidgetStatus.Disable;
			break;
		case NextEnable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.next.toString() );
			status = WidgetStatus.Up;
			break;
		case NextDisable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.next.toString() );
			status = WidgetStatus.Disable;
			break;
		default:
			break;
		}
		
		if ( null != widget ) {
			uiWidgetAccessBarButton.setWidgetStatus(widget, status);
		}

		logger.info(className, function, "End");
	}
	
	private void setSplitButton(UITaskSplit taskSplit) {
		final String function = "setSplitButton";
		
		logger.info(className, function, "Begin");
		
		logger.error(className, function, "taskSplit.getTaskType()[{}]", taskSplit.getTaskType());
		
		Widget widget = null;
		WidgetStatus status = null;
		
		switch (taskSplit.getTaskType()) {
		case HorizontalHightLight:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Down;
			break;
		case HorizontalEnable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Up;
			break;
		case HorizontalDisable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splith.toString() );
			status = WidgetStatus.Disable;
			break;
		case VerticalHightLight:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Down;
			break;
		case VerticalEnable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Up;
			break;
		case VerticalDisable:
			widget = uiWidgetAccessBarButton.getWidget( UIPanelAccessBarInterface.WidgetArrtibute.splitv.toString() );
			status = WidgetStatus.Disable;
			break;
		default:
			break;
		}
		
		if ( null != widget ) {
			uiWidgetAccessBarButton.setWidgetStatus(widget, status);
		}

		logger.info(className, function, "End");
	}
	
	void onUIEvent(UIEvent uiEvent ) {
		final String function = "onUIEvent";
		
		logger.info(className, function, "Begin");
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
		logger.info(className, function, "End");
	}
	
	void onButtonClick ( String element ) {
		final String function = "onButtonClick";
		
		logger.error(className, function, "elementValue[{}]", element);
		
		if ( null != element ) {
			
			if ( UIPanelAccessBarInterface.WidgetArrtibute.logout.equalsName(element) ) {
				logger.error(className, function, "logout[{}]", UIPanelAccessBarInterface.WidgetArrtibute.logout);
				
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setTaskUiScreen(0);
				taskLaunch.setUiPath(UIPathUIScreenMMI);
				taskLaunch.setUiPanel("UIDialogMsg");
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.dss.equalsName(element) ) {
				logger.error(className, function, "dss[{}]", UIPanelAccessBarInterface.WidgetArrtibute.dss);
				
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
				taskLaunch.setUiPath(UIPathUIPanelScreen);
				taskLaunch.setUiPanel("UIScreenDSS");
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));		
				
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.previous.equalsName(element) ) {
				
				logger.error(className, function, "previous[{}]", UIPanelAccessBarInterface.WidgetArrtibute.previous);
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.error(className, function, "strPreviousDisable");
				} else {
					logger.error(className, function, "strPrevious");
					
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(UIPathUIPanelViewLayout);
					taskHistory.setTaskType(UITaskHistory.TaskType.Previous);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
				
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.next.equalsName(element) ) {
				
				logger.error(className, function, "next["+UIPanelAccessBarInterface.WidgetArrtibute.next+"]");
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.error(className, function, "strNextDisable");
				} else {
					logger.error(className, function, "strNext");
			
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(UIPathUIPanelViewLayout);
					taskHistory.setTaskType(UITaskHistory.TaskType.Next);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.splith.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.error(className, function, "strSplitHDisable");
				} else {
					logger.error(className, function, "strSplitH");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(UIPathUIPanelViewLayout);
					taskSplit.setTaskType(UITaskSplit.SplitType.Horizontal);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.splitv.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.error(className, function, "strSplitVDisable");
				} else {
					logger.error(className, function, "strSplitV");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(UIPathUIPanelViewLayout);
					taskSplit.setTaskType(UITaskSplit.SplitType.Vertical);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.stationoperation.equalsName(element)  ) {
				logger.error(className, function, "strStationOperation");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.dss.equalsName(element) ) {
				logger.error(className, function, "strDss");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.print.equalsName(element) ) {
				logger.error(className, function, "strPrint");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.help.equalsName(element) ) {
				logger.error(className, function, "strHelp");
			}
		}
	}

}
