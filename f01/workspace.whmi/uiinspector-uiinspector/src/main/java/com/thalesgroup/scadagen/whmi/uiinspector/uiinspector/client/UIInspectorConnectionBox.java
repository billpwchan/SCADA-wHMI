package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorConnectionBox {
	
	private String widgetname				= "connectionbox";
	
	private FlexTable flexTable				= null;

	private InlineLabel lblScsEnvId			= null;
	private TextBox txtScsEnvId				= null;
	private InlineLabel lblDBAddress		= null;
	private TextBox txtDBAddress			= null;
	private InlineLabel lblPeriodMillis		= null;
	private TextBox txtPeriodMillis			= null;
	private Button btnRefresh				= null;
	
	private String scsEnvId					= null;
	private String dbaddress				= null;
	private String periodMillis				= null;
	
	public void setScsEnvId(String scsEnvId)		{ this.scsEnvId = scsEnvId; }
	public String getScsEnvId()						{ return scsEnvId; }
	
	public void setDBAddress(String dbaddress)		{ this.dbaddress = dbaddress; }
	public String getDBAddress()					{ return dbaddress; }
	
	public void setPeriodMillis(int periodMillis)	{ this.periodMillis = String.valueOf(periodMillis); }
	public int getPeriodMillis()					{ return Integer.parseInt(periodMillis); }
	
	public boolean periodMillisIsValid() {
		boolean result = false;
		if ( null != this.periodMillis ) {
			try {
				int period = Integer.parseInt(this.periodMillis);
				if ( period > 0 ) {
					result = true;
				}
			} catch ( NumberFormatException e ) {
				
			}			
		}
		return result;
	}

	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		flexTable 		= new FlexTable();
		flexTable.setWidth("100%");

		lblScsEnvId		= new InlineLabel("Scs Env ID: ");
		lblScsEnvId.addStyleName("project-gwt-inlinelabel-inspector"+widgetname+"-scsenvid");
		
		txtScsEnvId		= new TextBox();
		txtScsEnvId.addStyleName("project-gwt-textbox-inspector"+widgetname+"-scsenvid");
		
		lblDBAddress	= new InlineLabel("DB Address: ");
		lblDBAddress.addStyleName("project-gwt-inlinelabel-inspector"+widgetname+"-dbaddress");
		
		txtDBAddress	= new TextBox();
		txtDBAddress.addStyleName("project-gwt-textbox-inspector"+widgetname+"-dbaddress");
		
		lblPeriodMillis		= new InlineLabel("DB Address: ");
		lblPeriodMillis.addStyleName("project-gwt-inlinelabel-inspector"+widgetname+"-timer");
		
		txtPeriodMillis		= new TextBox();
		txtPeriodMillis.addStyleName("project-gwt-textbox-inspector"+widgetname+"-timer");
		
		btnRefresh		= new Button("Refresh");
		btnRefresh.addStyleName("project-gwt-button-inspector"+widgetname+"-refresh");
		
		btnRefresh.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Button btn = (Button)event.getSource();
				if (0 == btn.getText().compareTo("Refresh")) {
					
					if ( null != uiInspectorConnectionBoxEvent ) 
						uiInspectorConnectionBoxEvent.updated();
					
//					disconnect();
//					
//					String scsEnvId = txtScsEnvId.getText();
//					String dbAddress = txtDBAddress.getText();
//					String periodMillis = txtPeriodMillis.getText();
//					
//					setParent(dbAddress);
//					setAddresses(scsEnvId, new String[]{dbAddress});
//					setPeriodMillis(periodMillis);
//					
//					connect();
				}
			}
		});
		
		if ( null != this.scsEnvId ) txtScsEnvId.setText(this.scsEnvId);
		if ( null != this.dbaddress ) txtDBAddress.setText(this.dbaddress);
		if ( null != this.periodMillis ) txtPeriodMillis.setText(this.periodMillis);
		
		int row = 1, col = 0;
		flexTable.setWidget(row, col++, lblScsEnvId);
		flexTable.setWidget(row, col++, txtScsEnvId);
		
		++row;
		col = 0;
		flexTable.setWidget(row, col++, lblDBAddress);
		flexTable.setWidget(row, col++, txtDBAddress);
		
		++row;
		col = 0;
		flexTable.setWidget(row, col++, lblPeriodMillis);
		flexTable.setWidget(row, col++, txtPeriodMillis);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(flexTable);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		verticalPanel.add(btnRefresh);
		verticalPanel.addStyleName("project-gwt-panel-inspector"+widgetname+"-base");
		
		return verticalPanel;
		
	}
	
	private UIInspectorConnectionBoxEvent uiInspectorConnectionBoxEvent = null;
	public void setUIInspectorConnectionBoxEvent(UIInspectorConnectionBoxEvent uiInspectorConnectionBoxEvent) { 
		this.uiInspectorConnectionBoxEvent = uiInspectorConnectionBoxEvent; 
		}
}
