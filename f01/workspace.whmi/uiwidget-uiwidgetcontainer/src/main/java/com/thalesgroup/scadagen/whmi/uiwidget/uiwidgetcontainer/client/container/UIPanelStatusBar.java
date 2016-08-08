package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIPanelStatusBar extends UIWidget_i {

	private Logger logger = Logger.getLogger(UIPanelStatusBar.class.getName());
	
	private String strUIPanelCompany					= "UIPanelCompany.xml";
	private String strUIPanelCompanyTitle				= "UIPanelCompanyTitle.xml";
	private String strUIPanelDateTime					= "UIPanelDateTime.xml";
	private String strUIPanelTitle						= "UIPanelTitle.xml";
	private String strUIPanelOperatorProfile			= "UIPanelOperatorProfile.xml";
	
	UIWidget_i uiPanelGenericCompany				= null;
	UIWidget_i uiPanelGenericCompanyTitle			= null;
	UIWidget_i uiPanelGenericTitle					= null;
	UIWidget_i uiPanelGenericOperator				= null;
	UIWidget_i uiPanelGenericDateTime				= null;
	
	private UILayoutGeneric uiScreenGeneric =  null;
	
	private String strTitle = "";
	
	private String strOperator = "";
	private String strProfile = "";

	@Override
	public void init() {
		
		logger.log(Level.FINE, "init Begin");

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});
		
		uiScreenGeneric = new UILayoutGeneric();
		uiScreenGeneric.setUINameCard(this.uiNameCard);
		uiScreenGeneric.setXMLFile(xmlFile+".xml");
		uiScreenGeneric.init();
		rootPanel = uiScreenGeneric.getMainPanel();
		
		uiPanelGenericCompany		 = uiScreenGeneric.getUIWidget(strUIPanelCompany);
		uiPanelGenericCompanyTitle	 = uiScreenGeneric.getUIWidget(strUIPanelCompanyTitle);
		uiPanelGenericTitle			 = uiScreenGeneric.getUIWidget(strUIPanelTitle);
		uiPanelGenericOperator		 = uiScreenGeneric.getUIWidget(strUIPanelOperatorProfile);
		uiPanelGenericDateTime		 = uiScreenGeneric.getUIWidget(strUIPanelDateTime);
		
		Timer t = new Timer() {
			public void run() {
				uiPanelGenericDateTime.setValue("date");
				uiPanelGenericDateTime.setValue("time");
			}
		};
		// Schedule the timer to run once every second, 250 ms.
		t.scheduleRepeating(250);
		
		logger.log(Level.FINE, "init End");
		
	}

	private void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		UITask_i taskProvide = null;

		taskProvide = uiEvent.getTaskProvide();

		if (null != taskProvide) {

			if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
					&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

				logger.log(Level.FINE, "onUIEvent UIScreen is match and UIPath is match");

				if ( taskProvide instanceof UITaskTitle ) {

					logger.log(Level.FINE, "onUIEvent TaskTitle is match");

					UITaskTitle taskTitle = (UITaskTitle) taskProvide;
					String strTitle = taskTitle.getTitle();

					logger.log(Level.FINE, "onUIEvent strTitle[" + strTitle + "]");
					if (null != strTitle)		this.strTitle = strTitle;
					
					uiPanelGenericTitle.setValue("title", this.strTitle);
					
				} else if ( taskProvide instanceof UITaskProfile ) {

					logger.log(Level.FINE, "onUIEvent TaskTitle is match");

					UITaskProfile taskOperator = (UITaskProfile) taskProvide;
					String strOperator = taskOperator.getOperator();
					String strProfile = taskOperator.getProfile();

					logger.log(Level.FINE, "onUIEvent strTitle[" + strProfile + "]");
					if (null != strOperator)	this.strOperator = strOperator; 
					if (null != strProfile) 	this.strProfile = strProfile;

					uiPanelGenericOperator.setValue("operator", this.strOperator);
					uiPanelGenericOperator.setValue("profile", this.strProfile);
					
				}
			}
		}

		logger.log(Level.FINE, "onUIEvent End");
	}

}
