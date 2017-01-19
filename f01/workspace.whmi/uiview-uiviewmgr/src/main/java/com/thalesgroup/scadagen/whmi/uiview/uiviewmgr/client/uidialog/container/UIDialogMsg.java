package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uidialog.uidialog.client.UIDialog_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgCtrl_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.UIDialogMsgEvent.UIDialogMsgEventType;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;

public class UIDialogMsg extends DialogBox implements UIDialog_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIDialogMsg.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static final String basePath	= GWT.getModuleBaseURL();
	private static final String folder		= "/resources/img/project/dialog/";
	
	private String element = className;
	
	int baseBoderX = 28, baseBoderY = 28;
	
	int baseWidth = 400, baseHeight = 600;
	
	public enum UIConfimDlgType {
		  DLG_ERR("DLG_ERR")
		, DLG_WAR("DLG_WAR")
		, DLG_OK("DLG_OK")
		, DLG_YESNO("DLG_YESNO")
		, DLG_OKCANCEL("DLG_OKCANCEL")
		, DLG_EXECANCEL("DLG_EXECANCEL")
		, DLG_CONFIRMCANCEL("DLG_CONFIRMCANCEL")
		;
		private final String text;
		private UIConfimDlgType(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
	
	private String title;
	public void setTitle(String title) { this.title = title; }
	public String setTitle() { return this.title; }
	
	private String msg;
	public void setMsg(String msg) { this.msg = msg; }
	public String getMsg() { return this.msg; }
	
	private UIConfimDlgType confimDlgType = null;
	public void setConfimDlgType(UIConfimDlgType confimDlgType) { this.confimDlgType = confimDlgType; }
	public UIConfimDlgType getConfimDlgType() { return this.confimDlgType; }
	
	private HashMap<String, UIDialogMsgCtrl_i> uiDialogMsgCtrls = new HashMap<String, UIDialogMsgCtrl_i>();
	public void setResponse(String msgOpt, UIDialogMsgCtrl_i uiDialogMsgCtrl_i) {
		String function = "setResponse";
		logger.debug(className, function, "msgOpt[{}]", msgOpt);
		this.uiDialogMsgCtrls.put(msgOpt, uiDialogMsgCtrl_i);
	}
	private Image image;
	private TextArea txtMsg;
	private Button btnOk;
	private Button btnCancel;
	private HorizontalPanel btnBar;
	
	private UINameCard uiNameCard;
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	public void setDialogMsg(UIConfimDlgType dlgConfirmcancel, String title, String msg) {
		String function = "setDialogMsg";
		
		logger.debug(className, function, "dlgConfirmcancel[{}] title[{}] msg[{}]"
				, new Object[]{dlgConfirmcancel, title, msg});
		
		this.setConfimDlgType(dlgConfirmcancel);
		this.setTitle(title);
		this.setMsg(msg);
	}
	
	public void popUp() {

		int baseWidth = 500;
		int baseHeight = 200;
		
		image = new Image();

		txtMsg = new TextArea();

		btnOk = new Button();
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UIDialogMsgCtrl_i uiDialogCtrl_i = uiDialogMsgCtrls.get(UIDialogMsgEventType.MSG_OPT_1.toString());
				if ( null != uiDialogCtrl_i ) { 
					uiDialogCtrl_i.response();
				}
				hide();
			}
		});
		btnCancel = new Button();
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UIDialogMsgCtrl_i uiDialogCtrl_i = uiDialogMsgCtrls.get(UIDialogMsgEventType.MSG_OPT_2.toString());
				if ( null != uiDialogCtrl_i ) { 
					uiDialogCtrl_i.response();
				}
				hide();
			}
		});
		
		btnBar = new HorizontalPanel();
		btnBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		btnBar.add(btnOk);
		btnBar.add(btnCancel);	
		
		switch ( confimDlgType ) {
			case DLG_ERR:
				image.setUrl(basePath + folder+"/error.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_WAR:
				image.setUrl(basePath + folder+"/warning.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_OK:
				image.setUrl(basePath + folder+"/info.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_YESNO:
				image.setUrl(basePath + folder+"/question.png");
				btnOk.setText("Yes");
				btnCancel.setText("No");
				break;
			case DLG_OKCANCEL:
				image.setUrl(basePath + folder+"/question.png");
				btnOk.setText("OK");
				btnCancel.setText("Cancel");
				break;
			case DLG_EXECANCEL:
				image.setUrl(basePath + folder+"/question.png");
				btnOk.setText("Execute");
				btnCancel.setText("Cancel");
				break;
			case DLG_CONFIRMCANCEL:
				image.setUrl(basePath + folder+"/question.png");
				btnOk.setText("Confirm");
				btnCancel.setText("Cancel");
				break;				
				
			default:
				break;
		}
		
		this.setText(title);

		txtMsg.setText(msg);
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		dockLayoutPanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);	
		dockLayoutPanel.addStyleName("project-"+element+"-panel-dialogmsg");

		btnOk.addStyleName("project-"+element+"-button-ok");
		
		btnCancel.addStyleName("project-"+element+"-button-cancel");

		btnBar.addStyleName("project-"+element+"-btnbar");
		dockLayoutPanel.addSouth(btnBar, 50);
		
		image.getElement().getStyle().setPadding(25, Unit.PX);  
		dockLayoutPanel.addWest(image, 90);
		
		txtMsg.setWidth("90%");
		txtMsg.setHeight("95%");
		txtMsg.getElement().getStyle().setPadding(10, Unit.PX);
		txtMsg.addStyleName("project-"+element+"-textarea");
		dockLayoutPanel.add(txtMsg);
		
		this.add(dockLayoutPanel);
		
		this.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
		int left = (Window.getClientWidth() / 2) - ( baseWidth / 2 ) - (baseBoderX / 2);
	    int top = (Window.getClientHeight() / 2) - ( baseHeight / 2 ) - (baseBoderY / 2);
	    this.setPopupPosition(left, top);
	    
	    this.show();

	}

	
	@Override
	public void onMouseMove(Widget sender, int x, int y) {
		super.onMouseMove(sender, x, y);
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
