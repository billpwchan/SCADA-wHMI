package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueUpdate;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIPanelAlarmBanner extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(UIPanelAlarmBanner.class.getName());
	
	private final String 		UIPathUIPanelViewLayout = ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	private String strUIPanelAlarmBannerList			= "UIPanelAlarmBannerList";
	private String strUIWidgetAccessBarButton			= "UIPanelAlarmBannerButton.xml";
    private String strUIWidgetOlsCounter				= "UIPanelOlsCounter.xml";
	
	private UILayoutGeneric uiPanelGeneric				= null;
	private UIWidget_i uiPanelAlarmBannerList			= null;
	private UIWidget_i uiWidgetAccessBarButton 			= null;
	private UIWidget_i uiWidgetOlsCounter 				= null;

	@Override
	public void init() {
		logger.log(Level.FINE, "init Begin");
		logger.log(Level.FINE, "init xmlFile["+xmlFile+"]");
		
		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.setUINameCard(this.uiNameCard);
		uiPanelGeneric.setXMLFile(xmlFile+".xml");
		uiPanelGeneric.init();
		rootPanel = uiPanelGeneric.getMainPanel();
		
		uiPanelAlarmBannerList = uiPanelGeneric.getUIWidget(strUIPanelAlarmBannerList);
		if ( null != uiPanelAlarmBannerList ) {
			uiPanelAlarmBannerList.setUIWidgetEvent(new UIWidgetEventOnValueUpdate() {
				
				@Override
				public void onValueChange(String name, String value) {
					logger.log(Level.FINE, "onValueChange name["+name+"] value["+value+"]");
					if ( null != uiWidgetOlsCounter ) {
						uiWidgetOlsCounter.setValue(name, value);
					} else {
						logger.log(Level.SEVERE, "onValueChange uiWidgetOlsCounter IS NULL");
					}
				}
			});
		} else {
			logger.log(Level.SEVERE, "init uiPanelAlarmBannerList IS NULL");
		}
		
		uiWidgetAccessBarButton = uiPanelGeneric.getUIWidget(strUIWidgetAccessBarButton);
		if ( null != uiWidgetAccessBarButton ) {
			uiWidgetAccessBarButton.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
				@Override
				public void onClickHandler(ClickEvent event) {
					Widget widget = (Widget) event.getSource();
					String element = uiWidgetAccessBarButton.getWidgetElement(widget);
					if ( null != element ) {
						onButton(element);
					} else {
						logger.log(Level.SEVERE, "onClickHandler onClickHandler button IS NULL");
					}
				}
			});
		} else {
			logger.log(Level.SEVERE, "init uiWidgetAccessBarButton IS NULL");
		}
		
		uiWidgetOlsCounter = uiPanelGeneric.getUIWidget(strUIWidgetOlsCounter);

	    logger.log(Level.FINE, "getMainPanel End");
	    
	}
	
	@Override
	public Panel getMainPanel() {
		
	    return rootPanel;
	}
	
	
	private void onButton(String element) {
		
		logger.log(Level.FINE, "onButton Begin");
		
		logger.log(Level.SEVERE, "onButton element["+element+"]");
		
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
				logger.log(Level.SEVERE, "onButton status IS NULL");
			}
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.ackpage.equalsName(element) ) {
			
			if ( null != uiPanelAlarmBannerList ) 
				uiPanelAlarmBannerList.setValue("ackVisible", "");
			
		} else {
			logger.log(Level.SEVERE, "onButton element UNKNOW");
		}
		
		logger.log(Level.FINE, "onButton End");
	}

}
