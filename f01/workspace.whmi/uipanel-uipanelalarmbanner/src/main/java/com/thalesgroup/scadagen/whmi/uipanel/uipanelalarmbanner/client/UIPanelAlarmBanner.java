package com.thalesgroup.scadagen.whmi.uipanel.uipanelalarmbanner.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIPanelAlarmBanner implements UIPanel_i, WrapperScsAlarmListPanelEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelAlarmBanner.class.getName());
	
	public static final String UNIT_PX		= "px";
	
	public static final String IMAGE_PATH	= "imgs";
	
	int BUTTON_WIDTH	= 120;
	int BUTTON_HEIGHT	= 32;
	
	int LABEL_WIDTH		= 120;
	int LABEL_HEIGHT	= 23;
	
    String strAlarmSummary = "Alarm";
    String strEventSummary = "Event";
    
    private String [] counterNames = { "alarmlist_banner_counter", "alarmlist_banner_counter_unack"};
    private String strAlarmLbls [] = new String[] {"Total","0","Unack","0"};
    private InlineLabel[] inlineLabel;

	private UINameCard uiNameCard;
	public DockLayoutPanel getMainPanel(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
	    HorizontalPanel basePanel = new HorizontalPanel();
	    basePanel.setHeight("100%");
	    basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    basePanel.addStyleName("project-gwt-panel-alarmbanner");
	    
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    WrapperScsAlarmListPanel wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanel.setSize("1550px", "100%");
//	    wrapperScsAlarmListPanel.setAddStyleName("project-gwt-panel-alarmbanner-wrapper");
	    wrapperScsAlarmListPanel.setCounterNames(counterNames);
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(this);
	    
	    VerticalPanel buttonPanel = new VerticalPanel();
//	    verticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
	    buttonPanel.setHeight("100%");
	    buttonPanel.addStyleName("project-gwt-panel-alarmbanner");
	    buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    String lblSums [] = new String[] { strAlarmSummary , strEventSummary };
	    
	    for ( String lbl: lblSums) {
		    final Button button = new Button(lbl);
		    button.setSize(BUTTON_WIDTH+UNIT_PX, BUTTON_HEIGHT+UNIT_PX);
		    button.addStyleName("project-gwt-button");
	   		buttonPanel.add(button);
	   		button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					//Window.alert("Summary Button Clicked");
					Button btn = (Button) event.getSource();
					onButton(btn.getText());
				}
			});	    	
	    }
	    
	    String basePath		= GWT.getModuleBaseURL();
	    	       		
   		String strSoundPath			= basePath + "/" + IMAGE_PATH+"/hscs/Sound.png";
   		final String strNoSoundPath	= basePath + "/" + IMAGE_PATH+"/hscs/NoSound.png";
   		
		//GWT.getModuleBaseURL()
		final String audioHtmlUp	= "<div width=\"90px\" height=\"32px\"><center><img src=\""+strSoundPath+"\" width=\"18px\" height=\"18px\"><label>Audible</label></center></br></div>";
		final String audioHtmlDown	= "<div width=\"90px\" height=\"32px\"><center><img src=\""+strNoSoundPath+"\" width=\"18px\" height=\"18px\"><label>Silent</label></center></br></div>";
		
		final Button audioButton = new Button();
		audioButton.setSize(BUTTON_WIDTH+UNIT_PX, BUTTON_HEIGHT+UNIT_PX);
		audioButton.addStyleName("project-gwt-button");
		audioButton.setHTML(audioHtmlUp);
		audioButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Button button = (Button)event.getSource();
				if ( button.getHTML().indexOf(strNoSoundPath) != -1 ) {
					button.setHTML(audioHtmlUp);
				} else {
					button.setHTML(audioHtmlDown);
				}
			}
		});
   		buttonPanel.add(audioButton);
   		
	    VerticalPanel statPanel = new VerticalPanel();
	    statPanel.setWidth("100%");
	    statPanel.setHeight("100%");
	    statPanel.addStyleName("project-gwt-panel-alarmbanner");
	    statPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    statPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    
	    inlineLabel = new InlineLabel[strAlarmLbls.length];
	    for(int i=0;i<strAlarmLbls.length;i++){
	    	inlineLabel[i] = new InlineLabel(strAlarmLbls[i]);
	    	inlineLabel[i].setSize(LABEL_WIDTH+UNIT_PX,LABEL_HEIGHT +UNIT_PX);
	    	inlineLabel[i].getElement().getStyle().setPadding(5, Unit.PX);
	    	if ( (i % 2) != 0 ) { 
	    		inlineLabel[i].setStyleName("project-gwt-inlinelabel-alarmbanner-counter-value");
	    	} else {
	    		inlineLabel[i].setStyleName("project-gwt-inlinelabel-alarmbanner-counter-label");
	    	}
	    	statPanel.add(inlineLabel[i]);
	    }
	    
	    basePanel.add(wrapperScsAlarmListPanel.getMainPanel());
	    basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	    basePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	    basePanel.add(buttonPanel);
	    basePanel.add(statPanel);
	    	   
	    DockLayoutPanel root = new DockLayoutPanel(Unit.PX);
	    root.add(basePanel);
	    root.setWidth("100%");
	    root.setHeight("100%");
	    
		for ( int i = 0 ; i < counterNames.length; ++i) {
			Integer value = wrapperScsAlarmListPanel.getCounter(counterNames[i]);
			if ( null != value ) {
				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
			}
		}
	    
	    logger.log(Level.FINE, "getMainPanel End");
	    
	    return root;
	}
	
	private void onButton(String strBtn) {
		
		logger.log(Level.FINE, "onButton Begin");
		
		if ( 0 == strAlarmSummary.compareToIgnoreCase(strBtn) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewAlarm");
			taskLaunch.setTitle("Alarm Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( 0 == strEventSummary.compareToIgnoreCase(strBtn) ) {
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewEvent");
			taskLaunch.setTitle("Event Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		}
		
		logger.log(Level.FINE, "onButton End");
	}
	
	@Override
	public void valueChanged(String name, int value) {
		logger.log(Level.SEVERE, "valueChanged Begin");
		
		logger.log(Level.SEVERE, " **** valueChanged name["+name+"] value["+value+"]");
		for ( int i = 0 ; i < counterNames.length; ++i) {
			if ( 0 == name.compareTo(counterNames[i]) ) {
				this.inlineLabel[(i*2)+1].setText(String.valueOf(value));
			}			
		}
		
		logger.log(Level.SEVERE, "valueChanged End");
		
	}
}
