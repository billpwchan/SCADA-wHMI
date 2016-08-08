package com.thalesgroup.scadagen.whmi.uiscreen.uiscreendss.client;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIScreenDSS extends UIWidget_i {
	
	private  Logger logger = Logger.getLogger(UIScreenDSS.class.getName());
	
	private final String UIPathUIPanelScreen		= ":UIGws:UIPanelScreen";

	public static final String UNIT_PX		= "px";
	
	private static String IMAGE_PATH = "imgs";
	
	public static final int LAYOUT_BORDER	= 0;
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
	
	@Override
	public void init() {
		
		rootPanel = new DockLayoutPanel(Unit.PX);
		rootPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		
		// Top
		HorizontalPanel stationBar = new HorizontalPanel();
		stationBar.getElement().getStyle().setPadding(10, Unit.PX);
		stationBar.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		stationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		stationBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		InlineLabel lblStation = new InlineLabel();
		lblStation.getElement().getStyle().setPadding(10, Unit.PX);
		lblStation.setText(" Station ");
		lblStation.setWidth(200+"px");
		stationBar.add(lblStation);
		
		
		RadioButtonBar stationRadioButtonBar = new RadioButtonBar();
		String strStnName []	= new String[]{"SHS", "LMC"};
		String strStnTitle []	= new String[]{"Sheung Shui Station", "Lok Ma Chau Station"};
		for ( int i = 0 ; i < strStnName.length ; ++i ) {
			String name = strStnName[i];
			String title = strStnTitle[i];
			HightLightableButton button = new HightLightableButton();
			button.setSize("70px", "30px");
			button.setText(name);
			button.setTitle(title);
			stationBar.add(button);
			stationRadioButtonBar.addButton(button);
		}
		
		HorizontalPanel topLv2 = new HorizontalPanel();
		topLv2.getElement().getStyle().setPadding(10, Unit.PX);
		topLv2.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		topLv2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Button btnCloseDssPanel = new Button();
		btnCloseDssPanel.setSize("200px", "30px");
		btnCloseDssPanel.addStyleName("project-gwt-button");
		btnCloseDssPanel.setText("Close DSS Panel");
		btnCloseDssPanel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onBtnClose();
			}
	    });
		topLv2.add(btnCloseDssPanel);
		topLv2.getElement().getStyle().setPadding(10, Unit.PX);
		
		SplitLayoutPanel top = new SplitLayoutPanel(LAYOUT_BORDER);
		top.addLineEnd(topLv2, 200 + 20);
		top.add(stationBar);
		// End of Top
		
		// Main
		DockLayoutPanel main = new DockLayoutPanel(Unit.PX);
		main.getElement().getStyle().setBackgroundColor("rgb( 255, 255, 255)");
		main.add(getMainPanel_Train_LMC());
		// End of Main
		
		// Bottom
		
			// InformationResource Panel
				// InformationResource Button panel
				HorizontalPanel hInfoResc1 = new HorizontalPanel();
				hInfoResc1.getElement().getStyle().setPadding(10, Unit.PX);
				hInfoResc1.getElement().getStyle().setBackgroundColor("rgb( 152, 245, 255)");
				hInfoResc1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				hInfoResc1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hInfoResc1.setWidth("100%");
				hInfoResc1.setHeight(50+"px");
				
				InlineLabel lblInfoReso = new InlineLabel();
				lblInfoReso.getElement().getStyle().setPadding(10, Unit.PX);
				lblInfoReso.setWidth(100+"px");
				lblInfoReso.setHeight(50+"px");
				lblInfoReso.setText(" Information/\nResource ");
				hInfoResc1.add(lblInfoReso);
				
				RadioButtonBar InfoResoRadioButtonBar = new RadioButtonBar();
				String strInfoResoName []	= new String[]{"Track\nview", "Station\nview", "GIS", "TEL directory", "System SCH", "Emer.\nResource", "What-if\nAnalysis", "Check List\nfor Emer. Plan"};
				for ( int i = 0 ; i < strInfoResoName.length ; ++i ) {
					String name = strInfoResoName[i];
					HightLightableButton button = new HightLightableButton();
					button.setSize(75+(strInfoResoName.length!=(i+1)?0:40)+"px", 50+"px");
					button.setText(name);
					hInfoResc1.add(button);
					InfoResoRadioButtonBar.addButton(button);
				}
				// End of InformationResource Button panel
		
				// InformationResource Select Panel
				HorizontalPanel hInfoResc2 = new HorizontalPanel();
				hInfoResc2.getElement().getStyle().setBackgroundColor("rgb( 152, 245, 255)");
				hInfoResc2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hInfoResc2.setWidth("100%");
				hInfoResc2.setHeight(50+"px");
			
				InlineLabel lblFileName = new InlineLabel();
				lblFileName.getElement().getStyle().setPadding(10, Unit.PX);
				lblFileName.setWidth("100px");
				lblFileName.setText("File Name");
				hInfoResc2.add(lblFileName);
				
				ListBox cboFileName = new ListBox();
				cboFileName.setWidth(220+"px");
				hInfoResc2.add(cboFileName);

				InlineLabel lblSelectionName = new InlineLabel();
				lblSelectionName.getElement().getStyle().setPadding(10, Unit.PX);
				lblSelectionName.setWidth("100px");
				lblSelectionName.setText("Selection Name");
				hInfoResc2.add(lblSelectionName);
				
				ListBox cboSelectionName = new ListBox();
				cboSelectionName.setWidth(220+"px");
				hInfoResc2.add(cboSelectionName);
				
				String btnPlayTools[] = new String[]{"Play", "Stop", "<<", ">>"};
				for ( int i = 0 ; i < btnPlayTools.length ; ++i ) {
					String name = btnPlayTools[i];
					Button btn = new Button();
					btn.setSize(40+"px", 40+"px");
					btn.addStyleName("project-gwt-button");
					btn.setText(name);
					hInfoResc2.add(btn);
				}
				// End of InformationResource Select Panel
				
				HorizontalPanel hInfoResc3 = new HorizontalPanel();
				hInfoResc3.getElement().getStyle().setBackgroundColor("rgb( 152, 245, 255)");
				hInfoResc3.setWidth("100%");
				hInfoResc3.setHeight("100%");
				
			VerticalPanel informationRescourcePanel = new VerticalPanel();
			informationRescourcePanel.setBorderWidth(LAYOUT_BORDER);
			informationRescourcePanel.setSize("100%", "100%");
			informationRescourcePanel.add(hInfoResc1);
			informationRescourcePanel.setCellHeight(hInfoResc1, 10+"%");
			informationRescourcePanel.add(hInfoResc2);
			informationRescourcePanel.setCellHeight(hInfoResc2, 10+"%");
			informationRescourcePanel.add(hInfoResc3);
			informationRescourcePanel.setCellHeight(hInfoResc3, 80+"%");
			
			// End of InformationResource Panel	
			
		
			// Decision Support Tool Panel
			HorizontalPanel dssToolsPanelLv1 = new HorizontalPanel();
			dssToolsPanelLv1.getElement().getStyle().setBackgroundColor("rgb( 162, 205, 90)");
			dssToolsPanelLv1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			dssToolsPanelLv1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			dssToolsPanelLv1.setWidth("100%");
			dssToolsPanelLv1.setHeight(50+"px");
			
			InlineLabel lblDecisionSupportTool = new InlineLabel();
			lblDecisionSupportTool.setText("Decision Support Tool");
			dssToolsPanelLv1.add(lblDecisionSupportTool);
			
			HorizontalPanel dssToolsPanelLv2 = new HorizontalPanel();
			dssToolsPanelLv2.getElement().getStyle().setBackgroundColor("rgb( 162, 205, 90)");
			dssToolsPanelLv2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			dssToolsPanelLv2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			dssToolsPanelLv2.setWidth("100%");
			dssToolsPanelLv2.setHeight(50+"px");
			
			RadioButtonBar dssToolButtonBar =  new RadioButtonBar();
			String strDSSToolName[] = new String[]{"Station Evacuation", "Tunnel Evacuation"};
			for ( int i = 0 ; i < strDSSToolName.length ; ++i ) {
				String name = strDSSToolName[i];
				HightLightableButton button = new HightLightableButton();
				button.setSize(250+"px", 40+"px");
				button.setText(name);
				dssToolsPanelLv2.add(button);
				dssToolButtonBar.addButton(button);
			}
			
			HorizontalPanel dssToolsPanelLv3 = new HorizontalPanel();
			dssToolsPanelLv3.getElement().getStyle().setBackgroundColor("rgb( 173, 255, 47)");
			dssToolsPanelLv3.setWidth("100%");
			dssToolsPanelLv3.setHeight("100%");
			
			VerticalPanel descisionSupportToolPanel = new VerticalPanel();
			descisionSupportToolPanel.setBorderWidth(LAYOUT_BORDER);
			descisionSupportToolPanel.setSize("100%", "100%");
			descisionSupportToolPanel.add(dssToolsPanelLv1);
			descisionSupportToolPanel.setCellHeight(dssToolsPanelLv1, 10+"%");
			descisionSupportToolPanel.add(dssToolsPanelLv2);
			descisionSupportToolPanel.setCellHeight(dssToolsPanelLv2, 10+"%");
			descisionSupportToolPanel.add(dssToolsPanelLv3);
			descisionSupportToolPanel.setCellHeight(dssToolsPanelLv3, 80+"%");
			// End of Decision Support Tool Panel
				
		HorizontalPanel bottom = new HorizontalPanel();
		bottom.setSize("100%", "100%");
		bottom.add(informationRescourcePanel);
		bottom.setCellWidth(informationRescourcePanel, 50+"%");
		bottom.add(descisionSupportToolPanel);
		bottom.setCellWidth(descisionSupportToolPanel, 50+"%");
		// End of Bottom
		
		((DockLayoutPanel)rootPanel).addNorth(top, 50);
		((DockLayoutPanel)rootPanel).addSouth(bottom, 350);
		((DockLayoutPanel)rootPanel).add(main);
		
		logger.log(Level.FINE, "getMainPanel End");
		
	}
	
	public class HightLightableButton extends Button {
				
		public static final String RGB_BTN_SEL = "rgb(246, 230, 139)";
		public static final String RGB_BTN_BG = "#F1F1F1";
		public static final String IMG_NONE = "none";
		
		private UITaskLaunch taskLaunch = null;
		public UITaskLaunch getTaskLaunch() { return taskLaunch; }
		public void setTaskLaunch( UITaskLaunch taskLaunch ) { this.taskLaunch = new UITaskLaunch(taskLaunch); }
		
		public HightLightableButton() {
			super();
		}
		public HightLightableButton( String string ) {
			super(string);
		}
		private boolean hightLight = false;
		/**
		 * set the button as hight light
		 * @return
		 */
		public boolean isHightLight() { return hightLight; };
		/**
		 * get the button is hight light or not
		 * @param hightLight
		 */
		public void setHightLight ( boolean hightLight ) {
			
			logger.log(Level.FINE, "setHightLight Begin hightLight["+hightLight+"]");
			
			this.hightLight = hightLight;
			if ( hightLight ) {
				this.getElement().getStyle().setBackgroundColor(RGB_BTN_SEL);
				this.getElement().getStyle().setBackgroundImage(IMG_NONE);
			} else {
				this.getElement().getStyle().setBackgroundColor(RGB_BTN_BG);
			}
			
			logger.log(Level.FINE, "setHightLight End");
		}
	}
	
	class RadioButtonBar {
		private ArrayList<HightLightableButton> buttons = new ArrayList<HightLightableButton>();

		public void addButton(HightLightableButton button) { 
			button.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					selectButton((HightLightableButton) event.getSource());
				}
		    });
			buttons.add(button); 
		}
		public void removeButton(HightLightableButton button) { buttons.remove(button); }
		
		public void selectButton(HightLightableButton button) {
			if ( ! button.isHightLight() ) {
				for (int c = 0; c < buttons.size(); c++) {
					HightLightableButton btn = (HightLightableButton) buttons.get(c);
					if (btn != button) {
						btn.setHightLight(false);
					} else {
						btn.setHightLight(true);
					}
				} // for
			}
		}
	}
	
	private void onBtnClose () {
		
		logger.log(Level.FINE, "onBtnClose Begin");
		
		UITaskLaunch taskLaunch = new UITaskLaunch();
		taskLaunch.setTaskUiScreen(0);
		taskLaunch.setUiPath(UIPathUIPanelScreen);
		taskLaunch.setUiPanel("UIScreenMMI");
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
		
		logger.log(Level.FINE, "onBtnClose End");
	}
	
	void onUIEvent(UIEvent uiEvent) {

		logger.log(Level.FINE, "onUIEvent Begin");

		if (null != uiEvent) {
			UITask_i taskProvide = uiEvent.getTaskProvide();
			if (null != taskProvide) {
				if (uiNameCard.getUiScreen() == uiEvent.getTaskProvide().getTaskUiScreen()
						&& 0 == uiNameCard.getUiPath().compareToIgnoreCase(uiEvent.getTaskProvide().getUiPath())) {
					
				}
			}

		}
		
		logger.log(Level.FINE, "onUIEvent End");

	}
	
	private DockLayoutPanel getMainPanel_Train_LMC() {
		VerticalPanel main = new VerticalPanel();
		main.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		main.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		main.setWidth("50%");
		main.setHeight("100%");
		
		String baseUrl = GWT.getModuleBaseURL();
		String subUrl = IMAGE_PATH + "/dss/";
		
		String strBrandImgUrl = baseUrl + subUrl + "TunnelTitle.jpg";
		
		Image imgBrand = new Image();
		imgBrand.setUrl(strBrandImgUrl);
		
		
		InlineLabel lblStation = new InlineLabel();
		lblStation.setText("LMC - SHS");
		lblStation.getElement().getStyle().setPadding(100, Unit.PX);
		
		VerticalPanel vpLeft = new VerticalPanel();
		vpLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpLeft.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vpLeft.getElement().getStyle().setPadding(100, Unit.PX);
		
		String strLeftImgUrl = baseUrl + subUrl + "TunLeft.jpg";
//		String strLeftDisableImgUrl = baseUrl + subUrl + "TunLeft_Disabled.jpg";
		String imgHtmlLeft = "<img src=\""+strLeftImgUrl+"\" width=\"116px\" height=\"117px\" />";
		Button btnLeft = new Button();
		btnLeft.setHTML(imgHtmlLeft);
		
		InlineLabel lblGotoLeft = new InlineLabel();
		lblGotoLeft.setText("go to LMC");
		
		vpLeft.add(btnLeft);
		vpLeft.add(lblGotoLeft);
		
		String strImgTrainUrl = baseUrl + subUrl + "train_dir.png";
		
		Image imgTrain = new Image();
		imgTrain.setUrl(strImgTrainUrl);
		
		VerticalPanel vpRight = new VerticalPanel();
		vpRight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpRight.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vpRight.getElement().getStyle().setPadding(100, Unit.PX);
		
		String strRightImgUrl = baseUrl + subUrl + "TunRight.jpg";
//		String strRightDisableImgUrl = baseUrl + subUrl + "TunRight_Disabled.jpg";
		String imgHtmlRight = "<img src=\""+strRightImgUrl+"\" width=\"116px\" height=\"117px\" />";
		Button btnRight = new Button();
		btnRight.setHTML(imgHtmlRight);
		
		InlineLabel lblGotoRight = new InlineLabel();
		lblGotoRight.setText("go to SHS");
		
		vpRight.add(btnRight);
		vpRight.add(lblGotoRight);
		
		HorizontalPanel trainMain = new HorizontalPanel();
		
		trainMain.add(vpLeft);
		trainMain.add(imgTrain);
		trainMain.add(vpRight);		
		
		HorizontalPanel buttonBar = new HorizontalPanel();
		buttonBar.getElement().getStyle().setPadding(100, Unit.PX);
		buttonBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonBar.setWidth("100%");
		buttonBar.setHeight("100%");
		
		Button btnHome = new Button();
		btnHome.addStyleName("project-gwt-button");
		btnHome.setText("Home");
		
		Button btnBack = new Button();
		btnBack.addStyleName("project-gwt-button");
		btnBack.setText("Back");
		
		buttonBar.add(btnHome);
		buttonBar.add(btnBack);
		
		main.add(imgBrand);
		main.add(lblStation);
		main.add(trainMain);		
		main.add(buttonBar);
		
		DockLayoutPanel base = new DockLayoutPanel(Unit.PX);
		base.add(main);
		
		return base;
	}

}
