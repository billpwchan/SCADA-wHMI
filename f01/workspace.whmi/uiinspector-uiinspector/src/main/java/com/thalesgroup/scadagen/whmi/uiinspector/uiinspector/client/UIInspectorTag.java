package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.rtdblogic.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorTag implements UIPanelInspector_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorTag.class.getName());
	
	private String scsEnvId = null;
	private String dbaddress = null;
	
	@Override
	public void setConnection(String scsEnvId, String dbaddress) {
		logger.log(Level.SEVERE, "setConnection Begin");
		this.scsEnvId = scsEnvId;
		this.dbaddress = dbaddress;
		logger.log(Level.SEVERE, "setConnection this.scsEnvId["+this.scsEnvId+"] this.dbaddress["+this.dbaddress+"]");
	}

	@Override
	public void readyToReadChildrenData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void readyToReadStaticData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readyToSubscribeDynamicData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeConnection() {
		// TODO Auto-generated method stub
		
	}

	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");

		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		VerticalPanel vpCtrls  = new VerticalPanel();
//		vpCtrls.setBorderWidth(LAYOUT_BORDER);
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
				btnCtrl.addStyleName("project-gwt-button");
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
				btnCtrl.addStyleName("project-gwt-button");
				hp2.add(btnCtrl);
			}
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
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}
}
