package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitasklaunch.client.UITaskLaunch;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;

public class UIInspectorConnectionBox extends DialogBox {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorConnectionBox.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String UIPathUIScreenMMI 	= ":UIGws:UIPanelScreen:UIScreenMMI";
	
	private String widgetname				= "connectionbox";
	
//	private InlineLabel lblScsEnvId			= null;
//	private TextBox txtScsEnvId				= null;
	private InlineLabel lblDBAddress		= null;
	private TextBox txtDBAddress			= null;
	private InlineLabel lblPeriodMillis		= null;
	private TextBox txtPeriodMillis			= null;
	private Button btnConnect				= null;
	private Button btnClose					= null;
	
//	private String scsEnvId					= "B001";
	private String dbAddress				= "OCC_CTVECTTTVF0001";
	private String periodMillis				= "250";
	
//	public void setScsEnvId(String scsEnvId)		{ this.scsEnvId = scsEnvId; }
//	public String getScsEnvId()						{ return scsEnvId; }
	
	public void setDBAddress(String dbaddress)		{ this.dbAddress = dbaddress; }
	public String getDBAddress()					{ return dbAddress; }
	
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
	
	public void exchange(boolean isSet) {
		if ( isSet ) {
//			txtScsEnvId.setText(scsEnvId);
			txtDBAddress.setText(dbAddress);
			txtPeriodMillis.setText(periodMillis);
		} else {
//			scsEnvId = txtScsEnvId.getText();
			dbAddress = txtDBAddress.getText();
			periodMillis = txtPeriodMillis.getText();
		}
	}
	
	private void connect() {
		UITaskLaunch taskLaunch = new UITaskLaunch();
		taskLaunch.setUiPanel("UIPanelInspector");
		taskLaunch.setTaskUiScreen(uiNameCard.getUiScreen());
		taskLaunch.setUiPath(UIPathUIScreenMMI);
		taskLaunch.setOption(new String[]{dbAddress, String.valueOf(periodMillis)});
		uiNameCard.getUiEventBus().fireEvent(new UIEvent(taskLaunch));
	}
	
	private void close() {
		this.hide();
	}
	
	int baseBoderX = 28, baseBoderY = 28;
	int baseWidth = 400, baseHeight = 620;
	private VerticalPanel basePanel = null;

	private UINameCard uiNameCard = null;

	public Panel getMainPanel(UINameCard uiNameCard) {
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
		this.setTitle("Inspector Panel Launcher");
		
		FlexTable flexTable 		= new FlexTable();
		flexTable.addStyleName("project-gwt-flexTable-inspector-"+widgetname);

//		lblScsEnvId		= new InlineLabel("Scs Env ID: ");
//		lblScsEnvId.addStyleName("project-gwt-inlinelabel-inspector-"+widgetname+"-scsenvid");
//		
//		txtScsEnvId		= new TextBox();
//		txtScsEnvId.addStyleName("project-gwt-textbox-inspector-"+widgetname+"-scsenvid");
		
		lblDBAddress	= new InlineLabel("DB Address: ");
		lblDBAddress.addStyleName("project-gwt-inlinelabel-inspector-"+widgetname+"-dbaddress");
		
		txtDBAddress	= new TextBox();
		txtDBAddress.addStyleName("project-gwt-textbox-inspector-"+widgetname+"-dbaddress");
		
		lblPeriodMillis		= new InlineLabel("Millis Period: ");
		lblPeriodMillis.addStyleName("project-gwt-inlinelabel-inspector-"+widgetname+"-timer");
		
		txtPeriodMillis		= new TextBox();
		txtPeriodMillis.addStyleName("project-gwt-textbox-inspector-"+widgetname+"-timer");
		
		btnConnect		= new Button("Connect");
		btnConnect.addStyleName("project-gwt-button-inspector-"+widgetname+"-refresh");
		
		btnConnect.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				exchange(false);
				connect();
			}
		});
		
		btnClose		= new Button("Close");
		btnClose.addStyleName("project-gwt-button-inspector-"+widgetname+"-refresh");
		
		btnClose.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				close();
				
			}
		});
		
//		if ( null != this.scsEnvId ) txtScsEnvId.setText(this.scsEnvId);
		if ( null != this.dbAddress ) txtDBAddress.setText(this.dbAddress);
		if ( null != this.periodMillis ) txtPeriodMillis.setText(this.periodMillis);
		
		int row = 1, col = 0;
//		flexTable.setWidget(row, col++, lblScsEnvId);
//		flexTable.setWidget(row, col++, txtScsEnvId);
//		
//		++row;
		col = 0;
		flexTable.setWidget(row, col++, lblDBAddress);
		flexTable.setWidget(row, col++, txtDBAddress);
		
		++row;
		col = 0;
		flexTable.setWidget(row, col++, lblPeriodMillis);
		flexTable.setWidget(row, col++, txtPeriodMillis);
		
		HorizontalPanel horizionalPanel = new HorizontalPanel();
		horizionalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		horizionalPanel.add(btnConnect);
		horizionalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		horizionalPanel.add(btnClose);
		horizionalPanel.addStyleName("project-gwt-panel-inspector-"+widgetname+"-horizionalPanel");

		basePanel = new VerticalPanel();
		basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		basePanel.add(flexTable);
		basePanel.add(horizionalPanel);
		basePanel.addStyleName("project-gwt-panel-inspector-"+widgetname+"-base");
		
		this.add(basePanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (baseBoderX / 2);
        int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);

        this.setPopupPosition(left, top);
    
		exchange(true);
		
		logger.end(className, function);
		
		return basePanel;
		
	}
	
	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
		if ( basePanel != null ) {
			boolean XtoMoveInvalid = false, YtoMoveInvalid = false;
			int XtoMove = 0, YtoMove = 0;
			if ( this.getPopupLeft() < 0 ) {
				XtoMove = 0;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupLeft() + baseWidth + baseBoderX > Window.getClientWidth() ) {
				XtoMove = Window.getClientWidth() - baseWidth - baseBoderX;
				XtoMoveInvalid = true;
			}
			if ( this.getPopupTop() < 0 ) {
				YtoMove = 0;
				YtoMoveInvalid = true;
			}
			if ( this.getPopupTop() + baseHeight + baseBoderY > Window.getClientHeight() ) {
				YtoMove = Window.getClientHeight() - baseHeight - baseBoderY;
				YtoMoveInvalid = true;
			}
			if ( XtoMoveInvalid || YtoMoveInvalid ) {
				if ( ! XtoMoveInvalid ) XtoMove = this.getPopupLeft();
				if ( ! YtoMoveInvalid ) YtoMove = this.getPopupTop();
				this.setPopupPosition(XtoMove, YtoMove);
			}
		}
	}
}
