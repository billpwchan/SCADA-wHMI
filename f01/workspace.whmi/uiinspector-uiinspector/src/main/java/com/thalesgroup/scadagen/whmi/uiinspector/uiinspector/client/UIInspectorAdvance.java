package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIInspectorAdvance {

	public Panel getMainPanel() {

		VerticalPanel vpCtrls  = new VerticalPanel();
//		vpCtrls.setBorderWidth(LAYOUT_BORDER);
		vpCtrls.setWidth("100%");
		
		String btnWidth = "90px";
		String btnHeight = "26px";

		int numOfCtrlRow= 1;
		
		String [] command = new String[] {"Success", "Fail"};
		String [] control = new String[] {"Apply", "Cancel"};
		String [] datapointmanagement = new String[] {"Inhibit", "Suspend"};
		
		for(int x=0;x<numOfCtrlRow;++x){
			VerticalPanel vp0 = new VerticalPanel();
			vp0.setWidth("100%");
			vp0.getElement().getStyle().setPadding(5, Unit.PX);
			InlineLabel inlineLabel = new InlineLabel();
			inlineLabel.setText("Advance "+(x+1));
			vp0.add(inlineLabel);
			
			HorizontalPanel hp1 = new HorizontalPanel();
			hp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			for(int y=0;y<command.length;++y){
				Button btnCtrl = new Button();
				btnCtrl.setText(command[y]);
				btnCtrl.setWidth(btnWidth);
				btnCtrl.setHeight(btnHeight);
				btnCtrl.addStyleName("project-gwt-button");
				hp1.add(btnCtrl);
			}
			vp0.add(hp1);
			
			HorizontalPanel hp2 = new HorizontalPanel();
			hp2.setWidth("100%");
			hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
						
				HorizontalPanel hp3 = new HorizontalPanel();
				hp3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				hp3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

				for(int y=0;y<control.length;++y){
					Button btnCtrl = new Button();
					btnCtrl.setText(control[y]);
					btnCtrl.setWidth(btnWidth);
					btnCtrl.setHeight(btnHeight);
					btnCtrl.addStyleName("project-gwt-button");
					hp3.add(btnCtrl);
				}
			
				HorizontalPanel hp4 = new HorizontalPanel();
				hp4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				hp4.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				
				for(int y=0;y<datapointmanagement.length;++y){
					Button btnCtrl = new Button();
					btnCtrl.setText(datapointmanagement[y]);
					btnCtrl.setWidth(btnWidth);
					btnCtrl.setHeight(btnHeight);
					btnCtrl.addStyleName("project-gwt-button");
					hp4.add(btnCtrl);
				}
			
				hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
				hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hp2.add(hp3);
				
				hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
				hp2.add(hp4);
			
			vp0.add(hp2);
			
			vpCtrls.add(vp0);
		}
				
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		return vp;
	}
}
