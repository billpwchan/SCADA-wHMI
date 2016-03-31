package com.thalesgroup.scadagen.whmi.uipanel.uipanelstatusbar.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIPanelGeneric;

public class UIPanelStatusBar {

	private static Logger logger = Logger.getLogger(UIPanelStatusBar.class.getName());
	
	private String strUIPanelCompany					= "UIPanelCompany.xml";
	private String strUIPanelCompanyTitle				= "UIPanelCompanyTitle.xml";
	private String strUIPanelDateTime					= "UIPanelDateTime.xml";
	private String strUIPanelTitle						= "UIPanelTitle.xml";
	private String strUIPanelOperatorProfile			= "UIPanelOperatorProfile.xml";
	
	private UIPanelGeneric uiPanelCompany				= null;
	private UIPanelGeneric uiPanelCompanyTitle			= null;
	private UIPanelGeneric uiPanelTitle					= null;
	private UIPanelGeneric uiPanelOperatorProfile		= null;
	private UIPanelGeneric uiPanelDateTime				= null;

	public static final String basePath		= GWT.getModuleBaseURL();

	private String strTitle = "";
	
	private String strOperator = "";
	private String strProfile = "";

	private UINameCard uiNameCard = null;

	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {

		logger.log(Level.FINE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
			@Override
			public void onEvenBusUIChanged(UIEvent uiEvent) {
				onUIEvent(uiEvent);
			}
		});

		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.addStyleName("project-gwt-panel-statusbar");

		HorizontalPanel topStatusBanner = new HorizontalPanel();
		topStatusBanner.setWidth("100%");
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topStatusBanner.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		uiPanelCompany = new UIPanelGeneric();
		uiPanelCompany.init(strUIPanelCompany);
		VerticalPanel companyPanel = uiPanelCompany.getMainPanel(this.uiNameCard);
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topStatusBanner.add(companyPanel);

		uiPanelCompanyTitle = new UIPanelGeneric();
		uiPanelCompanyTitle.init(strUIPanelCompanyTitle);
		VerticalPanel systemPanel = uiPanelCompanyTitle.getMainPanel(this.uiNameCard);
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topStatusBanner.add(systemPanel);
		
		uiPanelTitle = new UIPanelGeneric();
		uiPanelTitle.init(strUIPanelTitle);
		VerticalPanel titlePanel = uiPanelTitle.getMainPanel(this.uiNameCard);
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topStatusBanner.add(titlePanel);

		uiPanelOperatorProfile = new UIPanelGeneric();
		uiPanelOperatorProfile.init(strUIPanelOperatorProfile);
		VerticalPanel operatorProfilePanel = uiPanelOperatorProfile.getMainPanel(this.uiNameCard);
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		topStatusBanner.add(operatorProfilePanel);
		
		uiPanelDateTime = new UIPanelGeneric();
		uiPanelDateTime.init(strUIPanelDateTime);
		VerticalPanel datetTimePanel = uiPanelDateTime.getMainPanel(this.uiNameCard);
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		topStatusBanner.add(datetTimePanel);
		
		Timer t = new Timer() {
			public void run() {

				uiPanelDateTime.setValue("date");
				uiPanelDateTime.setValue("time");
			}
		};
		// Schedule the timer to run once every second, 1000 ms.
		t.scheduleRepeating(250);
		
		topStatusBanner.setCellWidth(companyPanel, "5%");
		topStatusBanner.setCellHeight(companyPanel, "100%");
		
		topStatusBanner.setCellWidth(systemPanel, "15%");
		topStatusBanner.setCellHeight(systemPanel, "100%");
		
		topStatusBanner.setCellWidth(titlePanel, "45%");
		topStatusBanner.setCellHeight(titlePanel, "100%");
		
		topStatusBanner.setCellWidth(operatorProfilePanel, "12%");
		topStatusBanner.setCellHeight(operatorProfilePanel, "100%");
		
		topStatusBanner.setCellWidth(datetTimePanel, "15%");
		topStatusBanner.setCellHeight(datetTimePanel, "100%");
		
		dockLayoutPanel.add(topStatusBanner);
		
		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}

	private void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		UITask_i taskProvide = null;

		taskProvide = uiEvent.getTaskProvide();

		if (null != taskProvide) {

			if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
					&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {

				logger.log(Level.FINE, "onUIEvent UIScreen is match and UIPath is match");

				if (UITaskMgr.isInstanceOf(UITaskTitle.class, taskProvide)) {

					logger.log(Level.FINE, "onUIEvent TaskTitle is match");

					UITaskTitle taskTitle = (UITaskTitle) taskProvide;
					String strTitle = taskTitle.getTitle();

					logger.log(Level.FINE, "onUIEvent strTitle[" + strTitle + "]");
					if (null != strTitle)		this.strTitle = strTitle;
					
					uiPanelTitle.setValue("title", this.strTitle);
					
				} else if (UITaskMgr.isInstanceOf(UITaskProfile.class, taskProvide)) {

					logger.log(Level.FINE, "onUIEvent TaskTitle is match");

					UITaskProfile taskOperator = (UITaskProfile) taskProvide;
					String strOperator = taskOperator.getOperator();
					String strProfile = taskOperator.getProfile();

					logger.log(Level.FINE, "onUIEvent strTitle[" + strProfile + "]");
					if (null != strOperator)	this.strOperator = strOperator; 
					if (null != strProfile) 	this.strProfile = strProfile;

					uiPanelOperatorProfile.setValue("operator", this.strOperator);
					uiPanelOperatorProfile.setValue("profile", this.strProfile);
					
				}
			}
		}

		logger.log(Level.FINE, "onUIEvent End");
	}

}
