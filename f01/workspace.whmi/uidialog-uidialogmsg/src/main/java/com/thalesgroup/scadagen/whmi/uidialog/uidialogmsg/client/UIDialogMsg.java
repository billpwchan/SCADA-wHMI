package com.thalesgroup.scadagen.whmi.uidialog.uidialogmsg.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uitask.uitask.client.UITask_i;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;

public class UIDialogMsg extends DialogBox implements DialogMsgMgrEvent {
	
	private static String IMAGE_PATH = "imgs";
	
	public static final String UNIT_PX		= "px";
		
	public static final int LAYOUT_BORDER	= 0;
	public static final String RGB_PAL_BG	= "#BEBEBE";
	
	public static final String RGB_RED		= "rgb( 255, 0, 0)";
	public static final String RGB_GREEN	= "rgb( 0, 255, 0)";
	public static final String RGB_BLUE		= "rgb( 0, 0, 255)";
	
	DialogMsgMgr dialogMsgMgr;
	
	int baseBoderX = 28, baseBoderY = 28;
	
	int baseWidth = 400, baseHeight = 600;
	
	public enum ConfimDlgType {
		DLG_ERR, DLG_WAR, DLG_OK, DLG_YESNO, DLG_OKCANCEL, DLG_EXECANCEL, DLG_CONFIRMCANCEL
	}
	
	private String title;
	public void setTitle(String title) { this.title = title; }
	public String setTitle() { return this.title; }
	
	private String msg;
	public void setMsg(String msg) { this.msg = msg; }
	public String getMsg() { return this.msg; }
	
	private ConfimDlgType confimDlgType;
	public void setConfimDlgType(ConfimDlgType confimDlgType) { this.confimDlgType = confimDlgType; }
	public ConfimDlgType getConfimDlgType() { return this.confimDlgType; }
	
	private UITask_i taskProvideYes;
	public UITask_i getTaskProvideYes() { return taskProvideYes; }
	public void setTaskProvideYes(UITask_i taskProvideYes) { this.taskProvideYes = taskProvideYes; }
	
	private UITask_i taskProvideNo;
	public UITask_i getTaskProvideNo() { return taskProvideNo; }
	public void setTaskProvideNo(UITask_i taskProvideNo) { this.taskProvideNo = taskProvideNo; }
	
	private InlineLabel lblTitle;
	private Image image;
	private TextArea txtMsg;
	private Button btnOk;
	private Button btnCancel;
	private HorizontalPanel btnBar;
	
	private UINameCard uiNameCard;
	public UIDialogMsg(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	public void setDialogMsg(ConfimDlgType dlgConfirmcancel, String title, String msg, UITask_i taskProvideYes, UITask_i taskProvideNo) {
		this.setConfimDlgType(dlgConfirmcancel);
		this.setTitle(title);
		this.setMsg(msg);
		this.setTaskProvideYes(taskProvideYes);
		this.setTaskProvideNo(taskProvideNo);
	}
	
	public void popUp(){
		
//Window.alert(this.getClass().getName());
		
		dialogMsgMgr = new DialogMsgMgr(this.uiNameCard, this);

		int baseWidth = 500;
		int baseHeight = 200;
		
		lblTitle = new InlineLabel();
		
		image = new Image();

		txtMsg = new TextArea();

		btnOk = new Button();
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogMsgMgr.setYes(taskProvideYes);
			}
		});
		btnCancel = new Button();
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogMsgMgr.setNo(taskProvideNo);
			}
		});
		
		btnBar = new HorizontalPanel();
		btnBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		btnBar.add(btnOk);
		btnBar.add(btnCancel);	
		
		String basePath = GWT.getModuleBaseURL();
		
		switch ( confimDlgType ) {
			case DLG_ERR:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/error.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_WAR:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/warning.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_OK:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/info.png");
				btnOk.setText("OK");
				btnCancel.setText("");
				btnCancel.setVisible(false);
				break;
			case DLG_YESNO:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/question.png");
				btnOk.setText("Yes");
				btnCancel.setText("No");
				break;
			case DLG_OKCANCEL:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/question.png");
				btnOk.setText("OK");
				btnCancel.setText("Cancel");
				break;
			case DLG_EXECANCEL:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/question.png");
				btnOk.setText("Execute");
				btnCancel.setText("Cancel");
				break;
			case DLG_CONFIRMCANCEL:
				image.setUrl(basePath + IMAGE_PATH+"/dialog/question.png");
				btnOk.setText("Confirm");
				btnCancel.setText("Cancel");
				break;				
				
			default:
				break;
		}
		
		lblTitle.setText(title);
		
		txtMsg.setText(msg);
		
		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		dockLayoutPanel.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
		dockLayoutPanel.getElement().getStyle().setWidth(baseWidth, Unit.PX);
		dockLayoutPanel.getElement().getStyle().setHeight(baseHeight, Unit.PX);		
		
		lblTitle.getElement().getStyle().setPadding(10, Unit.PX);
		lblTitle.getElement().getStyle().setFontStyle(FontStyle.OBLIQUE);
		
		btnOk.setWidth("100px");
		btnCancel.setWidth("100px");
		dockLayoutPanel.addNorth(lblTitle, 40);
		
		btnBar.getElement().getStyle().setPadding(10, Unit.PX);		
		btnBar.setWidth("100%");
		dockLayoutPanel.addSouth(btnBar, 50);
		
		image.getElement().getStyle().setPadding(25, Unit.PX);  
		dockLayoutPanel.addWest(image, 90);
		
		txtMsg.setWidth("90%");
		txtMsg.setHeight("95%");
		txtMsg.getElement().getStyle().setPadding(10, Unit.PX);
		txtMsg.getElement().getStyle().setBackgroundColor(RGB_PAL_BG);
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
//		if ( basePanel != null ) {
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
//		}
	}

	@Override
	public void dialogMsgMgrEvent(DialogMsgMgrEventType dialogMsgMgrEventType) {
//Window.alert("dialogMsgMgrEvent dialogMsgMgrEventType: "+dialogMsgMgrEventType.toString());
		switch ( dialogMsgMgrEventType )
		{
		case MSG_YES:
			this.hide();
			break;
		case MSG_NO:
			this.hide();
			break;
			default:
				break;
		}
	}



}
