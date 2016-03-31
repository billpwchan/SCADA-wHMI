package com.thalesgroup.scadagen.whmi.uipanel.uipanelalarmbanner.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIPanelGeneric;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.UIPanelGenericEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperSoundServerPanel;

public class UIPanelAlarmBanner implements UIPanel_i, WrapperScsAlarmListPanelEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelAlarmBanner.class.getName());
	
	public static final String UNIT_PX		= "px";
	
	public static final String IMAGE_PATH	= "imgs";
	
	int BUTTON_WIDTH	= 120;
	int BUTTON_HEIGHT	= 32;
	
	int LABEL_WIDTH		= 120;
	int LABEL_HEIGHT	= 23;
	
	String strTipAckPage		= "Acknowledge";
	String strAckPage			= "Ack Page";
	
	String strTipAlarmSummary	= "Alarm Smmary";
    String strAlarmSummary		= "Alarm";
    
    String strTipEventSummary	= "Event Summary";
    String strEventSummary		= "Event";
    
    String strAckPageCSS	= "project-gwt-button-alarmbanner-ackpage";
    String strAlmSumCSS		= "project-gwt-button-alarmbanner-almsum";
    String strEvtSumCSS		= "project-gwt-button-alarmbanner-evtsum";
    
    private UIPanelGeneric uiPanelOlsCounter = null;
    private String [] counterNames = null;
    
    
    private String strAlarm = "alarm";
    private String strEvent = "event";
    private String strAudio = "audio";
    
	private UIPanelGeneric uiPanelAccessBarButton 		= null;
	private String strUIPanelAccessBarButton			= "UIPanelAlarmBannerButton.xml";
    
	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		uiPanelOlsCounter = new UIPanelGeneric();
		uiPanelOlsCounter.init("UIPanelOlsCounter.xml");
		
		counterNames = uiPanelOlsCounter.getElementValues(UIPanelGeneric.strElement);
		
	    HorizontalPanel basePanel = new HorizontalPanel();
	    basePanel.setHeight("100%"); // Dont't remove it
	    basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    basePanel.addStyleName("project-gwt-panel-alarmbanner");
	    
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    WrapperScsAlarmListPanel wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanel.setSize("1450px", "100%");
	    wrapperScsAlarmListPanel.setCounterNames(counterNames);
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(this);
	    
		uiPanelAccessBarButton = new UIPanelGeneric();
		uiPanelAccessBarButton.init(strUIPanelAccessBarButton);
		uiPanelAccessBarButton.setUIPanelGenericEvent(new UIPanelGenericEvent() {
			@Override
			public void onClickHandler(ClickEvent event) {
				Widget widget = (Widget) event.getSource();
				String element = uiPanelAccessBarButton.getWidgetName(widget);
				if ( null != element ) {
					if ( 0 == element.compareTo(strAlarm) || 0 == element.compareTo(strEvent) ) {
						onButton(element);
					} 				
				} else {
					logger.log(Level.SEVERE, "onClickHandler onClickHandler button IS NULL");
				}
			}
			@Override
			public void onKeyPressHandler(KeyPressEvent event) {
			}
		});

	    VerticalPanel panelButtonVertical1 = new VerticalPanel();
	    panelButtonVertical1.addStyleName("project-gwt-panel-alarmbanner-button-vertical1");
	    panelButtonVertical1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    panelButtonVertical1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    VerticalPanel panelButtonVertical2 = new VerticalPanel();
	    panelButtonVertical2.addStyleName("project-gwt-panel-alarmbanner-button-vertical2");
	    panelButtonVertical2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    panelButtonVertical2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    String lblSums2 []		= new String[] { strAlarmSummary , strEventSummary };
	    String strTipSums2[]	= new String[] { strTipAlarmSummary, strTipEventSummary};
	    String strCsss2 []		= new String[] { strAlmSumCSS, strEvtSumCSS};
	    
	    String lblSums1 [] 		= new String[] { strAckPage };
	    String strTipSums1[] 	= new String[] { strTipAckPage};
	    String strCsss1 [] 		= new String[] { strAckPageCSS};
	    
	    for ( int i = 0 ; i < lblSums2.length ; ++i) {
		    final Button button = new Button();
		    button.setText(lblSums2[i]);
		    button.setTitle(strTipSums2[i]);
		    button.setSize(BUTTON_WIDTH+UNIT_PX, BUTTON_HEIGHT+UNIT_PX);
		    button.addStyleName(strCsss2[i]);
		    panelButtonVertical2.add(button);
	   		button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button btn = (Button) event.getSource();
					onButton(btn.getText());
				}
			});	    	
	    }
	    
	    for ( int i = 0 ; i < lblSums1.length ; ++i) {
		    final Button button = new Button();
		    button.setText(lblSums1[i]);
		    button.setTitle(strTipSums1[i]);
		    button.setSize(BUTTON_WIDTH+UNIT_PX, BUTTON_HEIGHT+UNIT_PX);
		    button.addStyleName(strCsss1[i]);
		    panelButtonVertical1.add(button);
	   		button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button btn = (Button) event.getSource();
					onButton(btn.getText());
				}
			});
	    }
	    
	    String basePath					= GWT.getModuleBaseURL();
	    	       		
	    final String strSoundPath		= basePath + "/" + IMAGE_PATH+"/hscs/Sound.png";
   		final String strNoSoundPath		= basePath + "/" + IMAGE_PATH+"/hscs/NoSound.png";
   		
   		final String strTipSound		= "Alarm Sound Audible";
   		final String strTipSoundDisable	= "Alarm Sound Silent";
   		
		//GWT.getModuleBaseURL()
		final String audioHtmlUp		= "<div width=\"90px\" height=\"32px\"><center><img src=\""+strSoundPath+"\" width=\"18px\" height=\"18px\"><label>Audible</label></center></br></div>";
		final String audioHtmlDown		= "<div width=\"90px\" height=\"32px\"><center><img src=\""+strNoSoundPath+"\" width=\"18px\" height=\"18px\"><label>Silent</label></center></br></div>";
		
		final Button audioButton = new Button();
		audioButton.setSize(BUTTON_WIDTH+UNIT_PX, BUTTON_HEIGHT+UNIT_PX);
		audioButton.addStyleName("project-gwt-button-alarmbanner-audio");
		audioButton.setHTML(audioHtmlUp);
		audioButton.setTitle(strTipSound);
		audioButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Button button = (Button)event.getSource();
				if ( button.getHTML().indexOf(strNoSoundPath) != -1 ) {
					button.setHTML(audioHtmlUp);
					audioButton.setTitle(strTipSound);
				} else {
					button.setHTML(audioHtmlDown);
					audioButton.setTitle(strTipSoundDisable);
				}
			}
		});
		panelButtonVertical1.add(audioButton);

	    HorizontalPanel panelButtonHorizontal = new HorizontalPanel();
	    panelButtonHorizontal.addStyleName("project-gwt-panel-alarmbanner-button-horizontal");
	    panelButtonHorizontal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    panelButtonHorizontal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    panelButtonHorizontal.add(panelButtonVertical1);
	    panelButtonHorizontal.add(panelButtonVertical2);
   		
	    
	    VerticalPanel panelVerticalPanel = new VerticalPanel();
	    panelButtonHorizontal.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    panelButtonHorizontal.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    

		panelVerticalPanel.add(uiPanelAccessBarButton.getMainPanel(this.uiNameCard));
	    panelVerticalPanel.add(panelButtonHorizontal);
	    panelVerticalPanel.add(new WrapperSoundServerPanel().getMainPanel());

	    //
	    VerticalPanel statPanel = uiPanelOlsCounter.getMainPanel(this.uiNameCard);
	    //
		
	    basePanel.add(wrapperScsAlarmListPanel.getMainPanel());
	    basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    basePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    basePanel.add(panelVerticalPanel);
	    basePanel.add(statPanel);
	    	   
	    DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
	    root.add(basePanel);
	    root.setWidth("100%");
	    root.setHeight("100%");

	    for ( int i = 0 ; i < counterNames.length; ++i) {
	    	Integer value = wrapperScsAlarmListPanel.getCounter(counterNames[i]);
	    	if ( null != value ) {
	    		uiPanelOlsCounter.setValue(counterNames[i], String.valueOf(value));
	    	}
	    }
	    
	    logger.log(Level.FINE, "getMainPanel End");
	    
	    return root;
	}
	
	
	private void onButton(String element) {
		
		logger.log(Level.FINE, "onButton Begin");
		
		logger.log(Level.FINE, "onButton element["+element+"]");
		
		if ( 0 == element.compareTo(strAlarm) || 0 == strAlarmSummary.compareToIgnoreCase(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewAlarm");
			taskLaunch.setTitle("Alarm Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( 0 == element.compareTo(strEvent) || 0 == strEventSummary.compareToIgnoreCase(element) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewEvent");
			taskLaunch.setTitle("Event Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if (0 == element.compareTo(strAudio) ) {
			
			String status = uiPanelAccessBarButton.getWidgetStatus(element);
			
			if ( null != status ) {
				
				if ( 0 == status.compareTo(UIPanelGeneric.strUp) ) {
					uiPanelAccessBarButton.setWidgetStatus(element, UIPanelGeneric.strDown);
				} else {
					uiPanelAccessBarButton.setWidgetStatus(element, UIPanelGeneric.strUp);
				}
			} else {
				logger.log(Level.FINE, "onButton status IS NULL");
			}
			

		}
		
		logger.log(Level.FINE, "onButton End");
	}
	
	@Override
	public void valueChanged(String name, int value) {
		logger.log(Level.SEVERE, "valueChanged Begin");
		
		logger.log(Level.SEVERE, " **** valueChanged name["+name+"] value["+value+"]");

		if ( null != uiPanelOlsCounter ) uiPanelOlsCounter.setValue(name, String.valueOf(value));
		
		logger.log(Level.SEVERE, "valueChanged End");
		
	}

}
