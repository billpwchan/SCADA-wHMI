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
import com.thalesgroup.scadagen.whmi.uitask.uitaskhistory.client.UITaskHistory.TaskType;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;

public class UIPanelAccessBar implements UIPanel_i {
	
	private static Logger logger = Logger.getLogger(UIPanelAccessBar.class.getName());
	
	public static final String UNIT_PX		= "px";
	public static final int LAYOUT_BORDER	= 0;	
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";
	
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String IMAGE_PATH	= "imgs";
	
	public static final int BUTTON_WIDTH	= 128;
	public static final int BUTTON_HIGHT	= 30;	
	public static final int FUNTION_WIDTH	= 128;
	public static final int IMG_BTN_WIDTH	= 45;
		
	public static final int EAST_WIDTH		= 160;
	public static final int SOUTH_HIGHT		= 50;
	public static final int WEST_WIDTH		= 160;
	public static final int NORTH_HIGHT		= 150;
	
	private String basePath = GWT.getModuleBaseURL();
	
//	private String strTipsBack 			= "History Back";
	private String strPrevious 			= basePath + IMAGE_PATH+"/hscs/Previous.png";
	private String strPreviousDisable 	= basePath + IMAGE_PATH+"/hscs/PreviousDisable.png";
//	private String strTipsForward 		= "History Forward";
	private String strNext 				= basePath + IMAGE_PATH+"/hscs/Next.png";
	private String strNextDisable		= basePath + IMAGE_PATH+"/hscs/NextDisable.png";
	
	private String strHelp 				= basePath + IMAGE_PATH+"/hscs/help.png";
	
//	String strAlarm 			= basePath + IMAGE_PATH+"/hscs/Alarm_Green.png";
	
	private String strEmergency 		= basePath + IMAGE_PATH+"/hscs/Emergency.png";
	
	private String strPrint 			= basePath + IMAGE_PATH+"/hscs/Print.png";
	
	private String strDSS 				= basePath + IMAGE_PATH+"/hscs/DSS.png";	
	private String strLogout 			= basePath + IMAGE_PATH+"/hscs/Logout.PNG";
	
//	private Button btnBack;
//	private Button btnForward;
	
	private HashMap<String, Button> mapStrBtn;
	private Button buttons[];
	
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.setBorderWidth(LAYOUT_BORDER);
		buttonBar.getElement().getStyle().setPadding(1, Unit.PX);
		buttonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				
		String strImgs [] = new String[] {
				strPrevious
				, strNext
				, strHelp
				, strDSS
//				, strAlarm
				, strEmergency
				, strPrint
				, strLogout
		};
		
		buttons = new Button[strImgs.length];
		mapStrBtn = new HashMap<String, Button>();
		
		for(int i=0;i<strImgs.length;i++){
			buttons[i] = new Button();
			mapStrBtn.put(strImgs[i], buttons[i]);
			buttons[i].setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+strImgs[i]+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			buttons[i].setSize(IMG_BTN_WIDTH+UNIT_PX, IMG_BTN_WIDTH+UNIT_PX);
			buttons[i].addStyleName("project-gwt-button");
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
 		dockLayoutPanel.add(buttonBar);
 		
 		logger.log(Level.FINE, "getMainPanel End");
 		
		return dockLayoutPanel;
	}
	
	private void setHistoryButton(UITaskHistory taskHistory) {
		
		logger.log(Level.FINE, "setHistoryButton Begin");
		
		logger.log(Level.FINE, "setHistoryButton taskHistory.getTaskType()["+taskHistory.getTaskType()+"]");
		
		Button button = null;
		switch (taskHistory.getTaskType()) {
		case PreviousEnable:
			button = mapStrBtn.get(strPrevious);
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+strPrevious+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setEnabled(true);
			break;
		case PreviousDisable:
			button = mapStrBtn.get(strPrevious);
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+strPreviousDisable+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setEnabled(true);
			break;
		case NextEnable:
			button = mapStrBtn.get(strNext);
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+strNext+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setEnabled(true);
			break;
		case NextDisable:
			button = mapStrBtn.get(strNext);
			button.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+strNextDisable+"\" width=\"32px\" height=\"32px\"></center></br></div>");
			button.setEnabled(true);
			break;
		default:
			break;
		}
		
		logger.log(Level.FINE, "setHistoryButton End");
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
		
		} else if ( button.getHTML().indexOf(strDSS) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strDSS");
			
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen");
			taskLaunch.setUiPanel("UIScreenDSS");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			
		} else if ( button.getHTML().indexOf(strPreviousDisable) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strPreviousDisable");
			
		} else if ( button.getHTML().indexOf(strPrevious) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strPrevious");
			
			UITaskHistory taskHistory = new UITaskHistory();
			taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskHistory.setTaskType(TaskType.Previous);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
	
		} else if ( button.getHTML().indexOf(strNextDisable) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strNextDisable");
			
		} else if ( button.getHTML().indexOf(strNext) != -1 ) {
			
			logger.log(Level.FINE, "onButtonClick strNext");
			
			UITaskHistory taskHistory = new UITaskHistory();
			taskHistory.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskHistory.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskHistory.setTaskType(TaskType.Next);
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskHistory));
		
		} else {
			
//			Window.alert("Assert to operation:" + button.getText());
			
		}
	}
}
