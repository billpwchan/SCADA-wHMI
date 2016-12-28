package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelStatusBar extends UIWidget_i {

	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelStatusBar.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String strUIPanelCompany					= "UIPanelCompany";
	private String strUIPanelCompanyTitle				= "UIPanelCompanyTitle";
	private String strUIPanelDateTime					= "UIPanelDateTime";
	private String strUIPanelTitle						= "UIPanelTitle";
	private String strUIPanelOperatorProfile			= "UIPanelOperatorProfile";
	
	UIWidget_i uiPanelGenericCompany				= null;
	UIWidget_i uiPanelGenericCompanyTitle			= null;
	UIWidget_i uiPanelGenericTitle					= null;
	UIWidget_i uiPanelGenericOperator				= null;
	UIWidget_i uiPanelGenericDateTime				= null;
	
	private UILayoutGeneric uiLayoutGeneric =  null;
	
	private String strTitle = "";
	
	private String strOperator = "";
	private String strProfile = "";

	@Override
	public void init() {
		
		final String function = "init";
		
		logger.begin(className, function);

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					onUIEvent(uiEvent);
				}
			})
		);
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setDictionaryFolder(dictionaryFolder);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiPanelGenericCompany		 = uiLayoutGeneric.getUIWidget(strUIPanelCompany);
		uiPanelGenericCompanyTitle	 = uiLayoutGeneric.getUIWidget(strUIPanelCompanyTitle);
		uiPanelGenericTitle			 = uiLayoutGeneric.getUIWidget(strUIPanelTitle);
		uiPanelGenericOperator		 = uiLayoutGeneric.getUIWidget(strUIPanelOperatorProfile);
		uiPanelGenericDateTime		 = uiLayoutGeneric.getUIWidget(strUIPanelDateTime);
		
		Timer t = new Timer() {
			public void run() {
				uiPanelGenericDateTime.setWidgetValue("date", null);
				uiPanelGenericDateTime.setWidgetValue("time", null);
			}
		};
		// Schedule the timer to run once every second, 250 ms.
		t.scheduleRepeating(250);
		
		logger.end(className, function);
		
	}

	private void onUIEvent(UIEvent uiEvent) {
		
		final String function = "onUIEvent";

		logger.begin(className, function);

		if ( null != uiEvent ) {
			
			UITask_i taskProvide = uiEvent.getTaskProvide();
			
			if (null != taskProvide) {

				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

					logger.info(className, function, "UIScreen is match and UIPath is match");

					if ( taskProvide instanceof UITaskTitle ) {

						logger.info(className, function, "TaskTitle is match");

						UITaskTitle taskTitle = (UITaskTitle) taskProvide;
						String strTitle = taskTitle.getTitle();

						logger.info(className, function, "strTitle[{}]", strTitle);
						if (null != strTitle)		this.strTitle = strTitle;
						
						uiPanelGenericTitle.setWidgetValue("title", this.strTitle);
						
					} else if ( taskProvide instanceof UITaskProfile ) {

						logger.info(className, function, "TaskTitle is match");

						UITaskProfile taskOperator = (UITaskProfile) taskProvide;
						String strOperator = taskOperator.getOperator();
						String strProfile = taskOperator.getProfile();

						logger.info(className, function, "strTitle[{}]", strProfile);
						if (null != strOperator)	this.strOperator = strOperator; 
						if (null != strProfile) 	this.strProfile = strProfile;

						uiPanelGenericOperator.setWidgetValue("operator", this.strOperator);
						uiPanelGenericOperator.setWidgetValue("profile", this.strProfile);
						
					}
				}
			} else {
				logger.warn(className, function, "taskProvide IS NULL");
			}
		} else {
			logger.warn(className, function, "uiEvent IS NULL");
		}

		logger.end(className, function);
	}

}
