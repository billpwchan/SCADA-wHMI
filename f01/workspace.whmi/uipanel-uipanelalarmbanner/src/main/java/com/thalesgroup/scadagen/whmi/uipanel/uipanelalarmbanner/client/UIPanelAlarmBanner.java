package com.thalesgroup.scadagen.whmi.uipanel.uipanelalarmbanner.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.generic.uipanel.UIPanelGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIPanelAlarmBanner implements UIPanel_i {
	
	private static Logger logger = Logger.getLogger(UIPanelAlarmBanner.class.getName());

	private String xmlFile								= "UIPanelAlarmBanner.xml";
    
	private String strUIPanelAlarmBannerList			= "UIPanelAlarmBannerList";
	private String strUIWidgetAccessBarButton			= "UIPanelAlarmBannerButton.xml";
    private String strUIWidgetOlsCounter				= "UIPanelOlsCounter.xml";
	
	private UIPanelGeneric uiPanelGeneric				= null;
	private UIWidget_i uiPanelAlarmBannerList			= null;
	private UIWidget_i uiWidgetAccessBarButton 			= null;
	private UIWidget_i uiWidgetOlsCounter 				= null;
	
	private UINameCard uiNameCard;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		uiPanelGeneric = new UIPanelGeneric();
		uiPanelGeneric.init(xmlFile);
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel(this.uiNameCard);
		
		uiPanelAlarmBannerList = uiPanelGeneric.getUIWidget(strUIPanelAlarmBannerList);
		if ( null != uiPanelAlarmBannerList ) {
			uiPanelAlarmBannerList.setUIWidgetEvent(new UIWidgetEvent() {
				
				@Override
				public void onValueChange(String name, String value) {
					if ( null != uiWidgetOlsCounter ) 
						uiWidgetOlsCounter.setValue(name, value);
				}
				
				@Override
				public void onKeyPressHandler(KeyPressEvent event) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onClickHandler(ClickEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		uiWidgetAccessBarButton = uiPanelGeneric.getUIWidget(strUIWidgetAccessBarButton);
		uiWidgetAccessBarButton.setUIWidgetEvent(new UIWidgetEvent() {
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
			@Override
			public void onKeyPressHandler(KeyPressEvent event) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onValueChange(String name, String value) {
				// TODO Auto-generated method stub
				
			}
		});
		
		uiWidgetOlsCounter = uiPanelGeneric.getUIWidget(strUIWidgetOlsCounter);

	    logger.log(Level.SEVERE, "getMainPanel End");
	    
	    return complexPanel;
	}
	
	
	private void onButton(String element) {
		
		logger.log(Level.SEVERE, "onButton Begin");
		
		logger.log(Level.SEVERE, "onButton element["+element+"]");
		
		if ( UIPanelAlarmBanner_i.WidgetArrtibute.alarm.equalsName(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewAlarm");
			taskLaunch.setTitle("Alarm Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.event.equalsName(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewEvent");
			taskLaunch.setTitle("Event Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.audio.equalsName(element) ) {
			
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
		} else {
			logger.log(Level.SEVERE, "onButton element UNKNOW");
		}
		
		logger.log(Level.SEVERE, "onButton End");
	}

}
