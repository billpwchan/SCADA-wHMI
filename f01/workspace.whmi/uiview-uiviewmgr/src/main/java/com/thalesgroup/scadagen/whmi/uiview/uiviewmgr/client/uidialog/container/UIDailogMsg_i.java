package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.uidialog.container;

import com.google.gwt.core.client.GWT;

public interface UIDailogMsg_i {
	
	public static final String STR_BASEPATH		= GWT.getModuleBaseURL();
	public static final String STR_FOLDER		= "/resources/img/project/dialog/";
	
	public static final int baseBoderX = 28, baseBoderY = 28;
	
	public static final int baseWidth = 400, baseHeight = 600;
	
	public static final String STR_ONE	= "1";
	public static final String STR_TWO	= "2";
	
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

	public static final String strOK		= "OK";
	public static final String strCancel	= "Cancel";
	public static final String strExecute	= "Execute";
	public static final String strYes		= "Yes";
	public static final String strNo		= "No";
	public static final String strConfirm	= "Confirm";
	public static final String strEmpty		= "";
	
	public enum UIConfimDlgParameter {
		CSS_PREFIX("CSS_PREFIX")
		
		, IMG_BASE("IMG_BASE")
		, IMG_PREFIX("IMG_PREFIX")
		, IMG_DLG_ERR("IMG_DLG_ERR")
		, IMG_DLG_WAR("IMG_DLG_WAR")
		, IMG_DLG_OK("IMG_DLG_OK")
		, IMG_DLG_YESNO("IMG_DLG_YESNO")
		, IMG_DLG_OKCANCEL("IMG_DLG_OKCANCEL")
		, IMG_DLG_EXECANCEL("IMG_DLG_EXECANCEL")
		, IMG_DLG_CONFIRMCANCEL("IMG_DLG_CONFIRMCANCEL")
		
		, BTN_FOCUS("BTN_FOCUS")
		;
		private final String text;
		private UIConfimDlgParameter(final String text) { this.text = text; }
		@Override
		public String toString() { return this.text; }
	}
}
