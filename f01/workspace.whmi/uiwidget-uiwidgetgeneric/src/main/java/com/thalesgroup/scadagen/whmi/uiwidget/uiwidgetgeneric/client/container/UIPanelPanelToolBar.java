package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.container.UIPanelPanelToolBarEvent.UIPanelPanelToolBarEventType;

public class UIPanelPanelToolBar {

	private static Logger logger = Logger.getLogger(UIPanelPanelToolBar.class.getName());

	public static final String UNIT_PX = "px";
	public static final int LAYOUT_BORDER = 0;
	
	private static String IMAGE_PATH = "imgs";

	public static final String RGB_RED = "rgb( 255, 0, 0)";
	public static final String RGB_GREEN = "rgb( 0, 255, 0)";
	public static final String RGB_BLUE = "rgb( 0, 0, 255)";

	public static final String RGB_BTN_SEL = "rgb(246, 230, 139)";
	public static final String RGB_BTN_BG = "#F1F1F1";
	public static final String IMG_NONE = "none";

	public static final String RGB_PAL_BG = "#BEBEBE";

	public static final int BUTTON_WIDTH = 128;
	public static final int BUTTON_HIGHT = 30;
	public static final int FUNTION_WIDTH = 128;
	public static final int IMG_BTN_WIDTH = 45;

	public static final int EAST_WIDTH = 160;
	public static final int SOUTH_HIGHT = 50;
	public static final int WEST_WIDTH = 160;
	public static final int NORTH_HIGHT = 150;
	
	
//	private UIPanelPanelToolBarEvent uiPanelPanelToolBarEvent;

	private HashMap<String, HashMap<String, UIPanelPanelToolBarEventType>> hashMap;
	private HashMap<UIPanelPanelToolBarEventType, Button> btnMap;
	private HashMap<Button, UIPanelPanelToolBarEventType> eventMap;
	private HashMap<String, UIPanelPanelToolBarEventType> strMap;

	private HashMap<String, UIPanelPanelToolBarEventType> panelMaps;

	private UINameCard uiNameCard = null;

	public HorizontalPanel getMainPanel(UINameCard uiNameCard) {

		logger.log(Level.FINE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
//		this.uiPanelPanelToolBarEvent = uiPanelPanelToolBarEvent;

		hashMap = new HashMap<String, HashMap<String, UIPanelPanelToolBarEventType>>();
		btnMap = new HashMap<UIPanelPanelToolBarEventType, Button>();
		eventMap = new HashMap<Button, UIPanelPanelToolBarEventType>();
		strMap = new HashMap<String, UIPanelPanelToolBarEventType>();

		panelMaps = new HashMap<String, UIPanelPanelToolBarEventType>();

		panelMaps.put("Alarm Summary", UIPanelPanelToolBarEventType.AlarmSummary);
		panelMaps.put("Event Summary", UIPanelPanelToolBarEventType.EventSummary);
		panelMaps.put("Alarm Management", UIPanelPanelToolBarEventType.AlarmManagement);

		hashMap.put("PANEL", panelMaps);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setBorderWidth(LAYOUT_BORDER);
		hp.getElement().getStyle().setPadding(1, Unit.PX);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Iterator<Entry<String, UIPanelPanelToolBarEventType>> it = (hashMap.get("PANEL")).entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, UIPanelPanelToolBarEventType> pair = (Map.Entry<String, UIPanelPanelToolBarEventType>) it
					.next();

			Button btn = new Button();

			String btnStr = (String) pair.getKey();

			if (-1 != btnStr.indexOf(IMAGE_PATH)) {
				btn.setHTML("<div width=\"90px\" height=\"32px\"><center><img src=\"" + btnStr
						+ "\" width=\"32px\" height=\"32px\"></center></br></div>");
				btn.setSize(IMG_BTN_WIDTH + UNIT_PX, IMG_BTN_WIDTH + UNIT_PX);
			} else {
				btn.setSize(IMG_BTN_WIDTH * 4 + UNIT_PX, IMG_BTN_WIDTH + UNIT_PX);
				btn.addStyleName("project-gwt-button");
				btn.setText(btnStr);
			}

			btn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Button btn = (Button) event.getSource();
					onButton(btn.getText());
				}
			});
			hp.add(btn);

			strMap.put(btnStr, pair.getValue());
			btnMap.put(pair.getValue(), btn);
			eventMap.put(btn, pair.getValue());
		}

		logger.log(Level.FINE, "getMainPanel End");

		return hp;
	}
	
	private void onButton( String label ) {
		if ( 0 == label.compareToIgnoreCase("Alarm Summary") ){
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewAlarm");
			taskLaunch.setTitle("Alarm Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		} else if ( 0 == label.compareToIgnoreCase("Event Summary") ){
			UITaskLaunch taskLaunch = new UITaskLaunch();
			taskLaunch.setType("P");
			taskLaunch.setTaskUiScreen(this.uiNameCard.getUiScreen());
			taskLaunch.setUiPath(":UIGws:UIPanelScreen:UIScreenMMI:UIPanelViewLayout");
			taskLaunch.setUiPanel("UIViewEvent");
			taskLaunch.setTitle("Event Summary");
			this.uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		}
	}
	
	public void setButton(String title, boolean onOff) {
		UIPanelPanelToolBarEventType uiPanelPanelToolBarEventType = panelMaps.get(title);
		setButton(uiPanelPanelToolBarEventType, true);
	}

	public void setButton(UIPanelPanelToolBarEventType UIPanelPanelToolBarEventType, boolean onOff) {
		Button btn = btnMap.get(UIPanelPanelToolBarEventType);
		if ( onOff ) {
			btn.getElement().getStyle().setBackgroundColor(RGB_BTN_SEL);
			btn.getElement().getStyle().setBackgroundImage(IMG_NONE);			
		} else {
			btn.getElement().getStyle().setBackgroundColor(RGB_BTN_BG);
		}
	}

}
