package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common.PTWCommonEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common.PTWCommonEventHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.dpc.DpcMgr;

public class PTWAction implements UIWidget_i {
	
    /** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
	
	private EventBus eventBus = null;
	
	private InlineLabel lblOperation = null;
	private InlineLabel lblOperationDetail = null;
	
	private TextBox txtScsEnvId = null;
	private TextBox txtAlias = null;
	private TextBox txtPTWTag1 = null;
	private TextBox txtPTWTag2 = null;
	
	private Button btnSetPTW = null;
//	private Button btnSetPTW1 = null;
//	private Button btnSetPTW2 = null;
	private Button btnUnSetPTW = null;
	
	private Button btnCancelFilter = null;
	
	private DpcMgr dpcAccess = null;
	
	private VerticalPanel root = null;
	
	public PTWAction (EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	private void sendDpcCommand ( TaggingStatus status ) {
		
		String scsEnvId = txtScsEnvId.getText();
		String alias = txtAlias.getText();
		String ptwTag1 = txtPTWTag1.getText();
		String ptwTag2 = txtPTWTag2.getText();

		s_logger.debug(PTWAction.class.getName() + "scsEnvId[" + scsEnvId + "]");
		s_logger.debug(PTWAction.class.getName() + "alias[" + alias + "]");
		s_logger.debug(PTWAction.class.getName() + "ptwTag1[" + ptwTag1 + "]");
		s_logger.debug(PTWAction.class.getName() + "ptwTag2[" + ptwTag2 + "]");
		
		dpcAccess.sendChangeEqpTag("changeEqpTag_ptwviewertester", scsEnvId, alias, status, ptwTag1, ptwTag2);		
	}
	
	private void fireEvent() {
		
		this.eventBus.fireEventFromSource(new PTWCommonEvent("Filter", "NAN"), this);
		
	}
	
	private UINameCard uiNameCard = null;
	public void setUINameCard (UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);			
		}
	}

	@Override
	public void init(String xmlFile) {

		dpcAccess = DpcMgr.getInstance("ptwviewertester");
		
			//Label
			lblOperation = new InlineLabel();
			lblOperation.setText("Operation:");
			lblOperation.addStyleName("project-gwt-panel-ptwviewertester-hp");
			
			lblOperationDetail = new InlineLabel();
			lblOperationDetail.setText("Operation Detail:");
			lblOperationDetail.addStyleName("project-gwt-panel-ptwviewertester-hp");

			txtScsEnvId = new TextBox();
			txtScsEnvId.setText("OCC");
			txtScsEnvId.addStyleName("project-gwt-textbox-ptwviewertester-scsenvid");
			
			txtAlias = new TextBox();
			txtAlias.setText("<alias>CTVECTTTVF0001");
			txtAlias.addStyleName("project-gwt-textbox-ptwviewertester-alias");
			
			txtPTWTag1 = new TextBox();
			txtPTWTag1.setText("PTWTAG1");
			txtPTWTag1.addStyleName("project-gwt-textbox-ptwviewertester-ptwtag1");
			
			txtPTWTag2 = new TextBox();
			txtPTWTag2.setText("PTWTAG2");
			txtPTWTag2.addStyleName("project-gwt-textbox-ptwviewertester-ptwtag2");
			
			btnSetPTW = new Button();
			btnSetPTW.setText("Set PTW");
			btnSetPTW.addStyleName("project-gwt-button-ptwviewertester-setptw");
			btnSetPTW.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					sendDpcCommand(TaggingStatus.ALL_TAGGING);
				}
			});
			
//			btnSetPTW1 = new Button();
//			btnSetPTW1.setText("Set PTW TAG 1");
//			btnSetPTW1.addStyleName("project-gwt-button-ptwviewertester-setptw");
//			btnSetPTW1.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					
//					sendDpcCommand(TaggingStatus.TAGGING_1);
//				}
//			});
//			
//			btnSetPTW2 = new Button();
//			btnSetPTW2.setText("Set PTW TAG 2");
//			btnSetPTW2.addStyleName("project-gwt-button-ptwviewertester-setptw");
//			btnSetPTW2.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					
//					sendDpcCommand(TaggingStatus.TAGGING_2);
//				}
//			});
			
			btnUnSetPTW = new Button();
			btnUnSetPTW.setText("UnSet PTW");
			btnUnSetPTW.addStyleName("project-gwt-button-ptwviewertester-unsetptw");
			btnUnSetPTW.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					sendDpcCommand(TaggingStatus.NO_TAGGING);
				}
			});
			
			btnCancelFilter = new Button();
			btnCancelFilter.setText("Cancel Filter");
			btnCancelFilter.addStyleName("project-gwt-button-ptwviewertester-cancelfilter");
			btnCancelFilter.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					fireEvent();
				}
			});
			
			VerticalPanel vp = new VerticalPanel();
			vp.addStyleName("project-gwt-panel-ptwviewertester-hp");
			vp.add(lblOperation);
			vp.add(lblOperationDetail);
			vp.add(txtScsEnvId);
			vp.add(txtAlias);
			vp.add(txtPTWTag1);
			vp.add(txtPTWTag2);
			vp.add(btnSetPTW);
//			vp.add(btnSetPTW1);
//			vp.add(btnSetPTW2);
			vp.add(btnUnSetPTW);
			
			vp.add(btnCancelFilter);
			
		root = new VerticalPanel();
		root.addStyleName("project-gwt-panel-ptwviewertester-root");
		
		root.add(vp);
		
		this.eventBus.addHandler(PTWCommonEvent.TYPE, new PTWCommonEventHandler() {
			
			@Override
			public void onOperation(PTWCommonEvent ptwCommonEvent) {
				
				String operation = ptwCommonEvent.getOperation();
				
				String operationDetails = ptwCommonEvent.getOperationDetails();
				
				lblOperation.setText("Operation[" + operation + "]");
				
				lblOperationDetail.setText("Operation Details[" + operationDetails + "]");
				
				if ( null != operationDetails ) {
					txtAlias.setText(operationDetails);
				} else {
					txtAlias.setText("");
				}
				
			}
		});
		
	}

	@Override
	public ComplexPanel getMainPanel() {
		return root;
	}

	@Override
	public Widget getWidget(String widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetElement(Widget widget) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String name, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(String key, String value) {
		// TODO Auto-generated method stub
		
	}

}
