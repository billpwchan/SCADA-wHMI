package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

public class UIInspectorControl implements UIInspectorTab_i {
	
	private static Logger logger = Logger.getLogger(UIInspectorControl.class.getName());
	
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

				int numOfCtrlBtnRow = 4;
				for(int x=0;x<addresses.length;++x){
					VerticalPanel vp0 = new VerticalPanel();
					vp0.setWidth("100%");
					vp0.getElement().getStyle().setPadding(5, Unit.PX);
					InlineLabel inlineLabel = new InlineLabel();
					inlineLabel.setText("Control: "+(x+1));
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

		vpCtrls  = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		Button btnExecute = new Button();
		btnExecute.getElement().getStyle().setPadding(10, Unit.PX);
		btnExecute.setWidth("100px");
		btnExecute.setText("Execute");
		btnExecute.addStyleName("project-gwt-button");
		btnExecute.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnUp = new Button();
		btnUp.addStyleName("project-gwt-button-inspector-up");
		btnUp.setText("▲");
		btnUp.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		InlineLabel lblPageNum = new InlineLabel();
		lblPageNum.addStyleName("project-gwt-inlinelabel-pagenum");
		lblPageNum.setText("1 / 1");
		
		Button btnDown = new Button();
		btnDown.addStyleName("project-gwt-button-inspector-down");
		btnDown.setText("▼");
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
		pageBar.add(lblPageNum);
		
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
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.addSouth(bottomBar, 50);
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}

}
