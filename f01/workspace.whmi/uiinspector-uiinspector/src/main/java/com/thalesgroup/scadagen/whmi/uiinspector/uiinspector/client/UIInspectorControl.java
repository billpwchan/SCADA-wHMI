package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIInspectorControl {
	
	public static final int LAYOUT_BORDER	= 0;
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";

	public Panel getMainPanel() {

		VerticalPanel vpCtrls  = new VerticalPanel();
		vpCtrls.setBorderWidth(LAYOUT_BORDER);
		vpCtrls.setWidth("100%");
		
		String btnWidth = "90px";
		String btnHeight = "26px";

		int numOfCtrlRow = 1;
		int numOfCtrlBtnRow = 4;
		for(int x=1;x<=numOfCtrlRow;++x){
			VerticalPanel vp0 = new VerticalPanel();
			vp0.setWidth("100%");
			vp0.getElement().getStyle().setPadding(5, Unit.PX);
			InlineLabel inlineLabel = new InlineLabel();
			inlineLabel.setText("Control: "+x);
			vp0.add(inlineLabel);
			
			HorizontalPanel hp1 = new HorizontalPanel();
			for(int y=1;y<=numOfCtrlBtnRow;++y){
				Button btnCtrl = new Button();
				btnCtrl.setText("Control "+y);
				btnCtrl.setWidth(btnWidth);
				btnCtrl.setHeight(btnHeight);
				btnCtrl.addStyleName("project-gwt-button");
				hp1.add(btnCtrl);
			}
			vp0.add(hp1);
			
			HorizontalPanel hp2 = new HorizontalPanel();
			for(int y=numOfCtrlBtnRow;y<=numOfCtrlBtnRow+numOfCtrlBtnRow-1;++y){
				Button btnCtrl = new Button();
				btnCtrl.setText("Control "+y);
				btnCtrl.setWidth(btnWidth);
				btnCtrl.setHeight(btnHeight);
				btnCtrl.addStyleName("project-gwt-button");
				hp2.add(btnCtrl);
			}
			vp0.add(hp2);
			
			vpCtrls.add(vp0);
		}
		
		Button btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setWidth("100px");
		btnExecute.setText("Execute");
//		btnExecute.setWidth(btnWidth);
//		btnExecute.setHeight(btnHeight);
		btnExecute.addStyleName("project-gwt-button");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnUp = new Button();
		btnUp.setWidth("50px");
		btnUp.getElement().getStyle().setPadding(10, Unit.PX);
		btnUp.addStyleName("project-gwt-button");
		btnUp.setText("Up");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		InlineLabel lblVol = new InlineLabel();
		lblVol.setWidth("50px");
		lblVol.getElement().getStyle().setPadding(10, Unit.PX);
		lblVol.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.setWidth("50px");
		btnDown.getElement().getStyle().setPadding(10, Unit.PX);
		btnDown.addStyleName("project-gwt-button");
		btnDown.setText("Down");
		btnDown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});	

		HorizontalPanel pageBar = new HorizontalPanel();

		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnUp);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(lblVol);
		
		pageBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pageBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageBar.add(btnDown);
		
		HorizontalPanel bottomBar = new HorizontalPanel();
		bottomBar.setWidth("100%");
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(pageBar);
		
		bottomBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomBar.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		bottomBar.add(btnExecute);
				
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		return vp;
	}
}
