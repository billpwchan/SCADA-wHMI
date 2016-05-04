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
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorAdvance implements UIInspectorTab_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorAdvance.class.getName());

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
		logger.log(Level.SEVERE, "setParent this.parent["+this.parent+"]");
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.SEVERE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.SEVERE, "setAddresses this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.SEVERE, "setAddresses End");
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}

	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.SEVERE, "updateDisplay Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			if ( null != this.addresses ) {
				
				String btnWidth = "90px";
				String btnHeight = "26px";
		
				String [] command = new String[] {"Success", "Fail"};
				String [] control = new String[] {"Apply", "Cancel"};
				String [] datapointmanagement = new String[] {"Inhibit", "Suspend"};				
				
				for ( int x = 0 ; x < numOfWidgets ; ++x ) {
					
					VerticalPanel vp0 = new VerticalPanel();
					vp0.setWidth("100%");
					vp0.getElement().getStyle().setPadding(5, Unit.PX);
					InlineLabel inlineLabel = new InlineLabel();
					inlineLabel.setText("Advance: "+(x+1));
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
			} else {
				logger.log(Level.SEVERE, "updateDisplay this.pointStatics IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "updateDisplay points IS NULL");
		}
		
		logger.log(Level.SEVERE, "updateDisplay End");
	}
	
	
	@Override
	public void connect() {
		logger.log(Level.SEVERE, "connect Begin");
		
		logger.log(Level.SEVERE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.SEVERE, "disconnect Begin");
		
		logger.log(Level.SEVERE, "disconnect End");
	}
	
	
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
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
