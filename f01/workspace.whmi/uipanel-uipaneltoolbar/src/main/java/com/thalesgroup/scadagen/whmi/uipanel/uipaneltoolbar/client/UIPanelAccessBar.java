package com.thalesgroup.scadagen.whmi.uipanel.uipaneltoolbar.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitasksplit.client.UITaskSplit;

public class UIPanelAccessBar implements UIPanel_i {
	
	private static Logger logger = Logger.getLogger(UIPanelAccessBar.class.getName());

	public static final String IMAGE_PATH	= "imgs";
	
	private String baseURL								= GWT.getModuleBaseURL();
	
	private String basePath								= baseURL + IMAGE_PATH+"/hscs/";
	
	private String strTipsHorizontalSplit				= "Horizontal Split";
	private String strSplitH							= "SplitH.png";
	
	private String strTipsHorizontalSplitDisabled		= "Horizontal Split Disabled";
	private String strSplitHDisable						= "SplitHDisable.png";
	
	private String strTipVerticalSplit					= "Vertical Split";
	private String strSplitV							= "SplitV.png";
	
	private String strTipVerticalSplitDisabled			= "Vertical Split Disabled";
	private String strSplitVDisable						= "SplitVDisable.png";
	
	private String strTipBack 							= "History Back";
	private String strPrevious 							= "Previous.png";
	
	private String strTipBackDisabled					= "History Back Disabled";
	private String strPreviousDisable 					= "PreviousDisable.png";
	
	private String strTipForward 						= "History Forward";
	private String strNext 								= "Next.png";
	
	private String strTipForwardDisabled 				= "History Forward Disabled";
	private String strNextDisable						= "NextDisable.png";
	
	private String strTipHelp 							= "Help";
	private String strHelp 								= "help.png";
	
	private String strTipEmergency 						= "Station Operation";
	private String strEmergency 						= "Emergency.png";
	
	private String strTipPrintScreen					= "Print Screen";
	private String strPrint 							= "Print.png";
	
	private String strTipsDSS							= "Decision Support System";
	private String strDSS 								= "DSS.png";
	
	private String strTipsLogout						= "Logout";
	private String strLogout 							= "Logout.PNG";
	
	private HashMap<String, Button> mapStrBtn;
	private Button buttons[];
	
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.addStyleName("project-gwt-panel-accessbar-buttonbar");
		buttonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				
		String strImgs [] = new String[] {
				strSplitH
				, strSplitV
				, strPrevious
				, strNext
				, strHelp
				, strDSS
//				, strAlarm
				, strEmergency
				, strPrint
				, strLogout
		};
		
		String strTips [] = new String[] {
				strTipsHorizontalSplit
				, strTipVerticalSplit
				, strTipBack
				, strTipForward
				, strTipHelp
				, strTipsDSS
//				, strAlarm
				, strTipEmergency
				, strTipPrintScreen
				, strTipsLogout
		};
		
		
		buttons = new Button[strImgs.length];
		mapStrBtn = new HashMap<String, Button>();
		
		for(int i=0;i<strImgs.length;i++){
			buttons[i] = new Button();
			mapStrBtn.put(strImgs[i], buttons[i]);
			buttons[i].setTitle(strTips[i]);
			buttons[i].setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+basePath+strImgs[i]+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			buttons[i].addStyleName("project-gwt-button-accessbar");
			buttons[i].addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					onButtonClick((Button)event.getSource());
				}
			});
			buttonBar.add(buttons[i]);
			
//			if ( -1 != buttons[i].getHTML().indexOf(strPrevious) 
//					|| -1 != buttons[i].getHTML().indexOf(strNext) ) {
//				 buttons[i].setVisible(false);
//			}
		}

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});

 		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
 		dockLayoutPanel.addStyleName("project-gwt-panel-accessbar");
 		dockLayoutPanel.add(buttonBar);
 		
 		logger.log(Level.FINE, "getMainPanel End");
 		
		return dockLayoutPanel;
	}
	
	private void setHistoryButton(UITaskHistory taskHistory) {
		
		logger.log(Level.FINE, "setHistoryButton Begin");
		
		logger.log(Level.FINE, "setHistoryButton taskHistory.getTaskType()["+taskHistory.getTaskType()+"]");
		
		Button button = null;
		String strName = "";
		String strSrc = "";
		String strTip = "";
		switch (taskHistory.getTaskType()) {
		case PreviousEnable:
			strName = strPrevious;
			strSrc = strPrevious;
			strTip = strTipBack;
			break;
		case PreviousDisable:
			strName = strPrevious;
			strSrc = strPreviousDisable;
			strTip = strTipBackDisabled;
			break;
		case NextEnable:
			strName = strNext;
			strSrc = strNext;
			strTip = strTipForward;
			break;
		case NextDisable:
			strName = strNext;
			strSrc = strNextDisable;
			strTip = strTipForwardDisabled;
			break;
		default:
			break;
		}
	
		button = mapStrBtn.get(strName);
		if ( null != button ) {
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+basePath+strSrc+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setTitle(strTip);
			button.setEnabled(true);
		}
		
		logger.log(Level.FINE, "setHistoryButton End");
	}
	
	private void setSplitButton(UITaskSplit taskSplit) {
		
		logger.log(Level.SEVERE, "setSplitButton Begin");
		
		logger.log(Level.SEVERE, "setSplitButton taskSplit.getTaskType()["+taskSplit.getTaskType()+"]");
		
		Button button = null;
		String strName = "";
		String strSrc = "";
		String strTip = "";
		boolean hightLight = false;
		String styleName = null;
		boolean enable = false;
		switch (taskSplit.getTaskType()) {
		case HorizontalHightLight:
			hightLight = true;
		case HorizontalEnable:
			strName = strSplitH;
			strSrc = strSplitH;
			strTip = strTipsHorizontalSplit;
			enable = true;
			styleName = "project-gwt-button-split-h-selected";
			break;
		case HorizontalDisable:
			strName = strSplitH;
			strSrc = strSplitHDisable;
			strTip = strTipsHorizontalSplitDisabled;
			enable = false;
			styleName = "project-gwt-button-split-h-selected";
			break;
		case VerticalHightLight:
			hightLight = true;
		case VerticalEnable:
			strName = strSplitV;
			strSrc = strSplitV;
			strTip = strTipVerticalSplit;
			enable = true;
			styleName = "project-gwt-button-split-v-selected";
			break;
		case VerticalDisable:
			strName = strSplitV;
			strSrc = strSplitVDisable;
			strTip = strTipVerticalSplitDisabled;
			enable = false;
			styleName = "project-gwt-button-split-v-selected";
			break;
		default:
			break;
		}
		
		button = mapStrBtn.get(strName);
		if ( null != button ) {
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+basePath+strSrc+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setTitle(strTip);
			button.setEnabled(enable);
			
			logger.log(Level.SEVERE, "setSplitButton addStyleName["+styleName+"] hightLight["+hightLight+"]");
	
			
			if ( null != styleName ) {
				if ( hightLight ) {
					button.addStyleName(styleName);
				} else {
					button.removeStyleName(styleName);
				}		
			}
			
		}
		
		logger.log(Level.SEVERE, "setSplitButton End");
	}
	
	void onUIEvent(UIEvent uiEvent ) {
		
		logger.log(Level.FINE, "onUIEvent Begin");
		if ( null != uiEvent ) {
			
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if ( null != taskProvide ) 
			{
				if ( uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
					&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath()) ) {
					
					if ( UITaskMgr.isInstanceOf(UITaskHistory.class, taskProvide)){
						
						logger.log(Level.FINE, "onUIEvent taskProvide is TaskHistory");
						
						UITaskHistory taskHistory = (UITaskHistory)taskProvide;				
						
						setHistoryButton(taskHistory);
					} else if ( UITaskMgr.isInstanceOf(UITaskSplit.class, taskProvide)){
						
						logger.log(Level.SEVERE, "onUIEvent taskProvide is UITaskSplit");
						
						UITaskSplit taskSplit = (UITaskSplit)taskProvide;				
						
						setSplitButton(taskSplit);
					}
				}
			}
		}
		logger.log(Level.FINE, "onUIEvent End");
	}
	
	void onButtonClick ( Button button ) {
		
		
		if ( button.getHTML().indexOf(strLogout) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strLogout");
			
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setTaskUiScreen(0);
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI");
			taskLaunch.setUiPanel("UIDialogMsg");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		
		}
		
		if ( button.getHTML().indexOf(strDSS) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strDSS");
			
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen");
			taskLaunch.setUiPanel("UIScreenDSS");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			
		}
		
		if ( button.getHTML().indexOf(strPreviousDisable) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strPreviousDisable");
			
		} else if ( button.getHTML().indexOf(strPrevious) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strPrevious");
			
			UITaskHistory taskHistory = new UITaskHistory();
			taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskHistory.setTaskType(UITaskHistory.TaskType.Previous);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
	
		} else if ( button.getHTML().indexOf(strNextDisable) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strNextDisable");
			
		} else if ( button.getHTML().indexOf(strNext) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strNext");
			
			UITaskHistory taskHistory = new UITaskHistory();
			taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskHistory.setTaskType(UITaskHistory.TaskType.Next);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));

		}
		
		if ( button.getHTML().indexOf(strSplitHDisable) != -1 ) {
					
			logger.log(Level.SEVERE, "onButtonClick strSplitHDisable");
			
		} else if ( button.getHTML().indexOf(strSplitH) != -1 ) {
			
			logger.log(Level.SEVERE, "onButtonClick strSplitH");
			
			UITaskSplit taskSplit = new UITaskSplit();
			taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskSplit.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskSplit.setTaskType(UITaskSplit.SplitType.Horizontal);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));

		} else if ( button.getHTML().indexOf(strSplitVDisable) != -1 ) {
			
			logger.log(Level.SEVERE, "onButtonClick strSplitVDisable");
			
		} else if ( button.getHTML().indexOf(strSplitV) != -1 ) {
			
			logger.log(Level.SEVERE, "onButtonClick strSplitV");
			
			UITaskSplit taskSplit = new UITaskSplit();
			taskSplit.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskSplit.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskSplit.setTaskType(UITaskSplit.SplitType.Vertical);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskSplit));
			
		}
	}
}
