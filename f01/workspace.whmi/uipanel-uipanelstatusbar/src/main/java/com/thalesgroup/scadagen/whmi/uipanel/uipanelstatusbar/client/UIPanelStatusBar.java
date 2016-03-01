package com.thalesgroup.scadagen.whmi.uipanel.uipanelstatusbar.client;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitaskmgr.client.UITaskMgr;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskProfile;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;

public class UIPanelStatusBar {
	
	public static final int LAYOUT_BORDER	= 0;

	private static Logger logger = Logger.getLogger(UIPanelStatusBar.class.getName());

	public static final String RGB_PAL_BG = "#BEBEBE";

	public static final String basePath		= GWT.getModuleBaseURL();
	public static final String IMAGE_PATH	= "imgs";

	public static final String THALES_LOGIN_NAME = "logo_thales.jpg";

	private InlineLabel titleInlineLabel = null;
	private InlineLabel operatorProfileInlineLabel = null;
	private InlineLabel datetimeInlineLabel = null;
	
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
		dockLayoutPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);

		HorizontalPanel topStatusBanner = new HorizontalPanel();
		topStatusBanner.setWidth("100%");
		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		topStatusBanner.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		topStatusBanner.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);

		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		Image imgThales = new Image(basePath + "/" + IMAGE_PATH + "/logo/" + THALES_LOGIN_NAME);
		topStatusBanner.add(imgThales);
		String strLabels[] = new String[] { "MCS - Main Control System" };
		InlineLabel inlineLabel[] = new InlineLabel[1];
		for (int i = 0; i < strLabels.length; i++) {
			inlineLabel[i] = new InlineLabel(strLabels[i]);
			inlineLabel[i].setWidth("250px");
			topStatusBanner.add(inlineLabel[i]);
		}

		final String strTitle = "TITLE";

		titleInlineLabel = new InlineLabel();
		titleInlineLabel.setWidth("250px");
		topStatusBanner.add(titleInlineLabel);

		setTitle(strTitle);

		operatorProfileInlineLabel = new InlineLabel();
		operatorProfileInlineLabel.setWidth("250px");
		topStatusBanner.add(operatorProfileInlineLabel);
		
		setProfile("OPERTOR", "PROFILE");

		topStatusBanner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		datetimeInlineLabel = new InlineLabel();
		datetimeInlineLabel.setText("dd MM yyyy HH:mm:ss.SSS");
		topStatusBanner.add(datetimeInlineLabel);
		
		Timer t = new Timer() {
			public void run() {
				setCurrentDateTime();
			}
		};
		// Schedule the timer to run once every second, 1000 ms.
		t.scheduleRepeating(250);
		
		topStatusBanner.setCellWidth(imgThales, "20%");
		topStatusBanner.setCellWidth(inlineLabel[0], "20%");
		topStatusBanner.setCellWidth(titleInlineLabel, "20%");
		topStatusBanner.setCellWidth(operatorProfileInlineLabel, "20%");
		topStatusBanner.setCellWidth(datetimeInlineLabel, "20%");
		
		dockLayoutPanel.add(topStatusBanner);
		
		// Fill the current datetime before timer start
		setCurrentDateTime();

		logger.log(Level.FINE, "getMainPanel End");

		return dockLayoutPanel;
	}
	
	private void setCurrentDateTime() {
		datetimeInlineLabel.setText(DateTimeFormat.getFormat("dd MMM yyyy HH:mm:ss").format(new Date()));
	}

	private void setTitle(String title) {

		logger.log(Level.FINE, "setTitle Begin");

		if (null != titleInlineLabel) {
			titleInlineLabel.setText(title);
		}
		logger.log(Level.FINE, "setTitle End");
	}
	
	private void setProfile(String operator, String profile) {

		logger.log(Level.FINE, "setProfile Begin");

		if (null != operatorProfileInlineLabel) {
			operatorProfileInlineLabel.setText(operator + " @ " + profile);
		}
		logger.log(Level.FINE, "setProfile End");
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
					
					setTitle(this.strTitle);
					
				} else if (UITaskMgr.isInstanceOf(UITaskProfile.class, taskProvide)) {

					logger.log(Level.FINE, "onUIEvent TaskTitle is match");

					UITaskProfile taskOperator = (UITaskProfile) taskProvide;
					String strOperator = taskOperator.getOperator();
					String strProfile = taskOperator.getProfile();

					logger.log(Level.FINE, "onUIEvent strTitle[" + strProfile + "]");
					if (null != strOperator)	this.strOperator = strOperator; 
					if (null != strProfile) 	this.strProfile = strProfile;
					
					setProfile(this.strOperator, this.strProfile);
					
				}
			}
		}

		logger.log(Level.FINE, "onUIEvent End");
	}

}
