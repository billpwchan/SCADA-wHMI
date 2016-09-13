package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueUpdate;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIPanelAlarmBanner extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelAlarmBanner.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String 		UIPathUIPanelViewLayout = ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	private String strUIPanelAlarmBannerList			= "UIPanelAlarmBannerList";
	private String strUIWidgetAccessBarButton			= "UIPanelAlarmBannerButton";
    private String strUIWidgetOlsCounter				= "UIPanelOlsCounter";
	
	private UILayoutGeneric uiLayoutGeneric				= null;
	private UIWidget_i uiLayoutAlarmBannerList			= null;
	private UIWidget_i uiWidgetAccessBarButton 			= null;
	private UIWidget_i uiWidgetOlsCounter 				= null;

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		logger.info(className, function, "viewXMLFile", viewXMLFile);
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		uiLayoutAlarmBannerList = uiLayoutGeneric.getUIWidget(strUIPanelAlarmBannerList);
		if ( null != uiLayoutAlarmBannerList ) {
			uiLayoutAlarmBannerList.setUIWidgetEvent(new UIWidgetEventOnValueUpdate() {
				
				@Override
				public void onValueChange(String element, String value) {
					logger.info(className, function, "onValueChange element[{}] value[{}]", element, value);
					if ( null != uiWidgetOlsCounter ) {
						uiWidgetOlsCounter.setWidgetValue(element, value);
					} else {
						logger.warn(className, function, "onValueChange uiWidgetOlsCounter IS NULL");
					}
				}
			});
		} else {
			logger.warn(className, function, "uiPanelAlarmBannerList IS NULL");
		}
		
		uiWidgetAccessBarButton = uiLayoutGeneric.getUIWidget(strUIWidgetAccessBarButton);
		if ( null != uiWidgetAccessBarButton ) {
			uiWidgetAccessBarButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
				@Override
				public void onClickHandler(ClickEvent event) {
					Widget widget = (Widget) event.getSource();
					String element = uiWidgetAccessBarButton.getWidgetElement(widget);
					if ( null != element ) {
						onButton(element);
					} else {
						logger.warn(className, function, "onClickHandler onClickHandler button IS NULL");
					}
				}
			});
		} else {
			logger.warn(className, function, "uiWidgetAccessBarButton IS NULL");
		}
		
		uiWidgetOlsCounter = uiLayoutGeneric.getUIWidget(strUIWidgetOlsCounter);

	    logger.end(className, function);
	    
	}
	
	@Override
	public Panel getMainPanel() {
		
	    return rootPanel;
	}
	
	
	private void onButton(String element) {
		final String function = "onButton";
		
		logger.begin(className, function);
		logger.info(className, function, "element[{}]", element);
		
		if ( UIPanelAlarmBanner_i.WidgetArrtibute.alarm.equalsName(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(UIPathUIPanelViewLayout);
			taskLaunch.setUiPanel("UIViewAlarm");
			taskLaunch.setTitle("Alarm Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.event.equalsName(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(UIPathUIPanelViewLayout);
			taskLaunch.setUiPanel("UIViewEvent");
			taskLaunch.setTitle("Event Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.alarmsound.equalsName(element) ) {
			
			WidgetStatus status = uiWidgetAccessBarButton.getWidgetStatus(element);
			
			if ( null != status ) {
				if ( WidgetStatus.Up == status ) {
					uiWidgetAccessBarButton.setWidgetStatus(element, WidgetStatus.Down);
				} else {
					uiWidgetAccessBarButton.setWidgetStatus(element, WidgetStatus.Up);
				}
			} else {
				logger.warn(className, function, "status IS NULL");
			}
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.ackpage.equalsName(element) ) {
			
			if ( null != uiLayoutAlarmBannerList ) 
				uiLayoutAlarmBannerList.setWidgetValue("ackVisible", "");
			
		} else {
			logger.warn(className, function, "element UNKNOW");
		}
		
		logger.end(className, function);
	}

}
