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

public class UIInspectorTag {

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
		
		String [] tagging = new String[] {"Tag", "Un-Tag"};
		String [] control = new String[] {"Execute"};

		for(int x=0;x<numOfCtrlRow;++x){
			VerticalPanel vp0 = new VerticalPanel();
			vp0.setWidth("100%");
			vp0.getElement().getStyle().setPadding(5, Unit.PX);
			InlineLabel inlineLabel = new InlineLabel();
			inlineLabel.setText("Tagging "+(x+1));
			vp0.add(inlineLabel);
			
			HorizontalPanel hp1 = new HorizontalPanel();
//			hp1.setWidth("100%");
			hp1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			hp1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			for(int y=0;y<tagging.length;++y){
				Button btnCtrl = new Button();
				btnCtrl.setText(tagging[y]);
				btnCtrl.setWidth(btnWidth);
				btnCtrl.setHeight(btnHeight);
				hp1.add(btnCtrl);
			}
			vp0.add(hp1);
			
			HorizontalPanel hp2 = new HorizontalPanel();
			hp2.setWidth("100%");
			hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			for(int y=0;y<control.length;++y){
				Button btnCtrl = new Button();
				btnCtrl.setText(control[y]);
				btnCtrl.setWidth(btnWidth);
				btnCtrl.setHeight(btnHeight);
				hp2.add(btnCtrl);
			}
			vp0.add(hp2);
			
			vpCtrls.add(vp0);
		}
				
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		return vp;
	}
}
