package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasktitle.client.UITaskTitle;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;
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
	
	private String title = "";
	
	private String strUIWidgetGeneric = "UIWidgetGeneric";
	
	private String opmApi			= null;
	private final String strOpmApi	= "OpmApi";
	private final String strHeader	= "header";
	
	private final String strOperator = "operator";
	private final String strProfile	= "profile";
	
	private final String strDate = "date";
	private final String strTime = "time";
	private final String strTitle = "title";

	@Override
	public void init() {
		
		final String function = "init";
		
		logger.begin(className, function);
		
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			opmApi = dictionariesCache.getStringValue(optsXMLFile, strOpmApi, strHeader);
		}

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
				uiPanelGenericDateTime.setWidgetValue(strDate, null);
				uiPanelGenericDateTime.setWidgetValue(strTime, null);
			}
		};
		// Schedule the timer to run once every second, 250 ms.
		t.scheduleRepeating(250);
		
		// Operator and Profile
		OpmMgr opmMgr = OpmMgr.getInstance();
		UIOpm_i uiOpm_i = opmMgr.getOpm(opmApi);
		
		String operator = uiOpm_i.getOperator();
		String profile = uiOpm_i.getProfile();

		logger.info(className, function, "operator[{}] profile[{}]", operator, profile);

		uiPanelGenericOperator.setWidgetValue(strOperator, operator);
		uiPanelGenericOperator.setWidgetValue(strProfile, profile);
		
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
						String title = taskTitle.getTitle();

						logger.info(className, function, "strTitle[{}]", title);
						if (null != title)		this.title = title;
						
						uiPanelGenericTitle.setWidgetValue(strTitle, this.title);
						
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
