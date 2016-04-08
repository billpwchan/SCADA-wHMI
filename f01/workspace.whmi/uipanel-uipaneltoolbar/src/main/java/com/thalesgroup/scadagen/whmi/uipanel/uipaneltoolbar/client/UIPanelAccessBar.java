package com.thalesgroup.scadagen.whmi.uipanel.uipaneltoolbar.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.generic.uiwidget.UIWidgetGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;

public class UIPanelAccessBar implements UIPanel_i {
	
	private static Logger logger = Logger.getLogger(UIPanelAccessBar.class.getName());

	private UIWidgetGeneric uiWidgetAccessBarButton 		= null;
	private String strUIPanelAccessBarButton			= "UIPanelAccessBarButton.xml";
	
	private UINameCard uiNameCard;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		uiWidgetAccessBarButton = new UIWidgetGeneric();
		uiWidgetAccessBarButton.init(strUIPanelAccessBarButton);
		uiWidgetAccessBarButton.setUIWidgetEvent(new UIWidgetEvent() {
			@Override
			public void onClickHandler(ClickEvent event) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetAccessBarButton.getWidgetElement(widget);
					onButtonClick(element);
				} else {
					logger.log(Level.SEVERE, "onClickHandler onClickHandler button IS NULL");
				}
			}
			@Override
			public void onKeyPressHandler(KeyPressEvent event) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onValueChange(String name, String value) {
				// TODO Auto-generated method stub
				
			}
		});
		ComplexPanel complexPanel = uiWidgetAccessBarButton.getMainPanel(this.uiNameCard);

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});

 		logger.log(Level.SEVERE, "getMainPanel End");
 		
		return complexPanel;
	}
	
	private void setHistoryButton(UITaskHistory taskHistory) {
		
		logger.log(Level.SEVERE, "setHistoryButton Begin");
		
		logger.log(Level.SEVERE, "setHistoryButton taskHistory.getTaskType()["+taskHistory.getTaskType()+"]");
		
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

		logger.log(Level.SEVERE, "setHistoryButton End");
	}
	
	private void setSplitButton(UITaskSplit taskSplit) {
		
		logger.log(Level.SEVERE, "setSplitButton Begin");
		
		logger.log(Level.SEVERE, "setSplitButton taskSplit.getTaskType()["+taskSplit.getTaskType()+"]");
		
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

		logger.log(Level.SEVERE, "setSplitButton End");
	}
	
	void onUIEvent(UIEvent uiEvent ) {
		
		logger.log(Level.SEVERE, "onUIEvent Begin");
		if ( null != uiEvent ) {
			
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if ( null != taskProvide ) 
			{
				if ( uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
					&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath()) ) {
					
					if ( UITaskMgr.isInstanceOf(UITaskHistory.class, taskProvide)){
						
						logger.log(Level.SEVERE, "onUIEvent taskProvide is TaskHistory");
						
						setHistoryButton((UITaskHistory)taskProvide);
						
					} else if ( UITaskMgr.isInstanceOf(UITaskSplit.class, taskProvide)){
						
						logger.log(Level.SEVERE, "onUIEvent taskProvide is UITaskSplit");
					
						setSplitButton((UITaskSplit)taskProvide);
					}
				}
			}
		}
		logger.log(Level.SEVERE, "onUIEvent End");
	}
	
	void onButtonClick ( String element ) {
		
		logger.log(Level.SEVERE, "onButtonClick elementValue["+element+"]");
		
		if ( null != element ) {
			
			if ( UIPanelAccessBarInterface.WidgetArrtibute.logout.equalsName(element) ) {
				logger.log(Level.SEVERE, "onButtonClick strLogout");
				
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setTaskUiScreen(0);
				taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI");
				taskLaunch.setUiPanel("UIDialogMsg");
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.dss.equalsName(element) ) {
				logger.log(Level.SEVERE, "onButtonClick strDSS");
				
				UITaskLaunch taskLaunch = new UITaskLaunch();
				taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
				taskLaunch.setUiPath(":UIGws:UIPanelScreen");
				taskLaunch.setUiPanel("UIScreenDSS");
				this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));		
				
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.previous.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.log(Level.SEVERE, "onButtonClick strPreviousDisable");
				} else {
					logger.log(Level.SEVERE, "onButtonClick strPrevious");
					
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
					taskHistory.setTaskType(UITaskHistory.TaskType.Previous);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
				
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.next.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.log(Level.SEVERE, "onButtonClick strNextDisable");
				} else {
					logger.log(Level.SEVERE, "onButtonClick strNext");
			
					UITaskHistory taskHistory = new UITaskHistory();
					taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
					taskHistory.setTaskType(UITaskHistory.TaskType.Next);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.splith.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.log(Level.SEVERE, "onButtonClick strSplitHDisable");
				} else {
					logger.log(Level.SEVERE, "onButtonClick strSplitH");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
					taskSplit.setTaskType(UITaskSplit.SplitType.Horizontal);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.splitv.equalsName(element) ) {
				
				WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
				
				if ( WidgetStatus.Disable == status ) {
					logger.log(Level.SEVERE, "onButtonClick strSplitVDisable");
				} else {
					logger.log(Level.SEVERE, "onButtonClick strSplitV");
					
					UITaskSplit taskSplit = new UITaskSplit();
					taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
					taskSplit.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
					taskSplit.setTaskType(UITaskSplit.SplitType.Vertical);
					this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
				}
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.stationoperation.equalsName(element)  ) {
				logger.log(Level.SEVERE, "onButtonClick strStationOperation");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.dss.equalsName(element) ) {
				logger.log(Level.SEVERE, "onButtonClick strDss");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.print.equalsName(element) ) {
				logger.log(Level.SEVERE, "onButtonClick strPrint");
			} else if ( UIPanelAccessBarInterface.WidgetArrtibute.help.equalsName(element) ) {
				logger.log(Level.SEVERE, "onButtonClick strHelp");
			}
		}
	}
}
