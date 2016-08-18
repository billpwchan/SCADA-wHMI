package com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.viewtoolbar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanelviewlayout.client.viewtoolbar.UIPanelImageToolBarEvent.UIPanelImageBarEventType;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIPanelImageToolBar {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelImageToolBar.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	public static final String UNIT_PX		= "px";
	
	public static final String RGB_BTN_SEL 	= "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG	= "#F1F1F1";
	public static final String IMG_NONE		= "none";
		
	public static final String basePath		= GWT.getModuleBaseURL();
	public static final String IMAGE_PATH	= "imgs";
	
	public static final int BUTTON_WIDTH	= 128;
	public static final int BUTTON_HIGHT	= 30;	
	public static final int FUNTION_WIDTH	= 128;
	public static final int IMG_BTN_WIDTH	= 45;
		
	public static final int EAST_WIDTH		= 160;
	public static final int SOUTH_HIGHT		= 50;
	public static final int WEST_WIDTH		= 160;
	public static final int NORTH_HIGHT		= 150;
	
	private HashMap<String, HashMap<String, UIPanelImageBarEventType>> hashMap;
	private HashMap<UIPanelImageBarEventType, Button> btnMap;
	private HashMap<Button, UIPanelImageBarEventType> eventMap;
	private HashMap<String, UIPanelImageBarEventType> strMap;
	
	private HashMap<String, UIPanelImageBarEventType> imageMaps;
	
	private UIPanelImageToolBarEvent uiPanelImageToolBarEvent = null;
	
	private UINameCard uiNameCard = null;
	public HorizontalPanel getMainPanel(UIPanelImageToolBarEvent uiPanelImageToolBarEvent, UINameCard uiNameCard) {
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		this.uiPanelImageToolBarEvent = uiPanelImageToolBarEvent;
				
		hashMap = new HashMap<String, HashMap<String, UIPanelImageBarEventType>>();
		btnMap = new HashMap<UIPanelImageBarEventType, Button>();
		eventMap = new HashMap<Button, UIPanelImageBarEventType>();
		strMap = new HashMap<String, UIPanelImageBarEventType>();
		
		imageMaps= new HashMap<String, UIPanelImageBarEventType>();

		/*
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/ZoomIn.png", UIPanelImageBarEventType.ZoomIn);
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/ZoomOut.png", UIPanelImageBarEventType.ZoomOut);
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/Zoom.png", UIPanelImageBarEventType.Zoom);
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/Locator.png", UIPanelImageBarEventType.Locator);
		*/
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/SplitH.png", UIPanelImageBarEventType.VDouble);
		imageMaps.put(basePath + "/" + IMAGE_PATH+"/hscs/SplitV.png", UIPanelImageBarEventType.HDouble);
		
		hashMap.put("IMAGE", imageMaps);
		
		HorizontalPanel hp = new HorizontalPanel();
//		hp.setBorderWidth(LAYOUT_BORDER);
		hp.getElement().getStyle().setPadding(1, Unit.PX);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
	    Iterator<Entry<String, UIPanelImageBarEventType>> it = (hashMap.get("IMAGE")).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, UIPanelImageBarEventType> pair = (Map.Entry<String, UIPanelImageBarEventType>)it.next();
	        
			Button btn = new Button();
			
			String btnStr = (String)pair.getKey();
			
			if ( -1 != btnStr.indexOf(IMAGE_PATH) ) {
				btn.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\""+btnStr+"\" width=\"32px\" height=\"32px\"></center></br></div>");
				btn.setSize(IMG_BTN_WIDTH + UNIT_PX, IMG_BTN_WIDTH + UNIT_PX);
			} else {
				btn.setText(btnStr);
				btn.setSize(IMG_BTN_WIDTH*4+UNIT_PX, IMG_BTN_WIDTH+UNIT_PX);
			}
			btn.addStyleName("project-gwt-button");
			
			btn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button btn = (Button)event.getSource();
					onButton(btn.getHTML());
				}
    		});
    		hp.add(btn);
    		   
    		strMap.put(btnStr, pair.getValue());
    		btnMap.put(pair.getValue(), btn);
    		eventMap.put(btn, pair.getValue());
	    }
		
	    logger.end(className, function);
		
		return hp;
	}

	private void onButton(String html ) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
		logger.info(className, function, "onButton html[{}]", html);
		
	    Iterator<Entry<String, UIPanelImageBarEventType>> it = (hashMap.get("IMAGE")).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, UIPanelImageBarEventType> pair = (Map.Entry<String, UIPanelImageBarEventType>)it.next();
			String btnStr = (String)pair.getKey();
			
			if ( -1 != html.indexOf(btnStr) ) {
				uiPanelImageToolBarEvent.onImageButtonEvent(pair.getValue());
			}
	    }
	    
	    logger.end(className, function);
	}
	
	public void setButton(UIPanelImageBarEventType uiPanelImageBarEventType, boolean onOff) {
		final String function = "setButton";
		
		logger.begin(className, function);
		
		logger.info(className, function, "setButton uiPanelImageBarEventType[{}] onOff[{}]", uiPanelImageBarEventType, onOff);
		
		Button btn = btnMap.get(uiPanelImageBarEventType);
		if ( onOff ) {
			btn.getElement().getStyle().setBackgroundColor(RGB_BTN_SEL);
			btn.getElement().getStyle().setBackgroundImage(IMG_NONE);			
		} else {
			btn.getElement().getStyle().setBackgroundColor(RGB_BTN_BG);
		}
		
		logger.end(className, function);
	}
}
