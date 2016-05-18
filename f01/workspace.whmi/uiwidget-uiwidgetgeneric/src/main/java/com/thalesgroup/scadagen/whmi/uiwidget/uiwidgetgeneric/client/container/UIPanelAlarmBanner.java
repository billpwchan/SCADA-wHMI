package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container;

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
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIPanelAlarmBanner implements UIWidget_i, UIPanel_i {
	
	private static Logger logger = Logger.getLogger(UIPanelAlarmBanner.class.getName());
	
	private final String 		UIPathUIPanelViewLayout = ":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout";

	private String xmlFile								= "UIPanelAlarmBanner.xml";
    
	private String strUIPanelAlarmBannerList			= "UIPanelAlarmBannerList";
	private String strUIWidgetAccessBarButton			= "UIPanelAlarmBannerButton.xml";
    private String strUIWidgetOlsCounter				= "UIPanelOlsCounter.xml";
	
	private UILayoutGeneric uiPanelGeneric				= null;
	private UIWidget_i uiPanelAlarmBannerList			= null;
	private UIWidget_i uiWidgetAccessBarButton 			= null;
	private UIWidget_i uiWidgetOlsCounter 				= null;
	
	private UINameCard uiNameCard;
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		uiPanelGeneric = new UILayoutGeneric();
		uiPanelGeneric.init(xmlFile);
		ComplexPanel complexPanel = uiPanelGeneric.getMainPanel(this.uiNameCard);
		
		uiPanelAlarmBannerList = uiPanelGeneric.getUIWidget(strUIPanelAlarmBannerList);
		if ( null != uiPanelAlarmBannerList ) {
			uiPanelAlarmBannerList.setUIWidgetEvent(new UIWidgetEvent() {
				
				@Override
				public void onValueChange(String name, String value) {
					logger.log(Level.SEVERE, "onValueChange name["+name+"] value["+value+"]");
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

	    logger.log(Level.FINE, "getMainPanel End");
	    
	    return complexPanel;
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
			
			WidgetStatus status = WidgetStatus.valueOf(uiWidgetAccessBarButton.getWidgetStatus(element));
			
			if ( null != status ) {
				if ( WidgetStatus.Up == status ) {
					uiWidgetAccessBarButton.setWidgetStatus(element, WidgetStatus.Down.toString());
				} else {
					uiWidgetAccessBarButton.setWidgetStatus(element, WidgetStatus.Up.toString());
				}
			} else {
				logger.log(Level.SEVERE, "onButton status IS NULL");
			}
		} else if ( UIPanelAlarmBanner_i.WidgetArrtibute.ackpage.equalsName(element) ) {
			
			String UIPathUIScreenMMI 	= ":UIGws:UIPanelScreen:UIScreenMMI";
			
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setUiPanel("UIInspectorConnectionBox");
			taskLaunch.setTaskUiScreen(uiNameCard.getUiScreen());
			taskLaunch.setUiPath(UIPathUIScreenMMI);
			uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
			
		} else {
			logger.log(Level.SEVERE, "onButton element UNKNOW");
		}
		
		logger.log(Level.FINE, "onButton End");
	}


	@Override
	public void init(String xmlFile) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}

}
