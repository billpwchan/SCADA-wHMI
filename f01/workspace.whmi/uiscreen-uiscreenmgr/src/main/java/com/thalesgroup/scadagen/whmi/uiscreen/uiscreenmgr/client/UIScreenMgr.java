package com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmgr.client;

import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreendss.client.UIScreenDSS;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenempty.client.UIScreenEmpty;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenLogin;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenlogin.client.UIScreenOPM;
import com.thalesgroup.scadagen.whmi.uiscreen.uiscreenmmi.client.UIScreenMMI;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIScreenMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIScreenMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private UIScreenMgr() {}
	private static UIScreenMgr instance = null;
	public static UIScreenMgr getInstance() {
		if ( null == instance ) {
			instance = new UIScreenMgr();
		}
		return instance;
	}
	
	public Panel getMainPanel(String uiPanel, UINameCard uiNameCard){
		final String function = "getMainPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, " uiNameCard["+uiNameCard.getUiPath()+"]");
		
		UIWidget_i uiWidget_i = this.getPanel(uiPanel, uiNameCard);
		Panel panel = uiWidget_i.getMainPanel();
		
		logger.end(className, function);

		return panel;
	}

	public UIWidget_i getPanel(String uiCtrl, UINameCard uiNameCard){
		final String function = "getPanel";
		
		logger.begin(className, function);
		
		logger.info(className, function, "uiCtrl[{}]", uiCtrl);
		
		String strUIScreenLogin	= "UIScreenLogin.view.xml";
		String strUIScreenMMI	= "UIScreenMMI.view.xml";
		String strUIScreenDSS	= "UIScreenDSS.view.xml";
		String strUIScreenOPM	= "UIScreenOPM.view.xml";
		
		String viewXMLFile = null;
		String optsXMLFile = null;
		UIWidget_i uiWidget_i = null;
		
		if ( UIWidgetUtil.getClassSimpleName(UIScreenLogin.class.getName())
				.equals(uiCtrl) ) {	
			
			viewXMLFile = strUIScreenLogin;
			
			uiWidget_i = new UIScreenLogin();

		} else if ( UIWidgetUtil.getClassSimpleName(UIScreenMMI.class.getName())
				.equals(uiCtrl) ) {

			viewXMLFile = strUIScreenMMI;
			
			uiWidget_i = new UIScreenMMI();

		} else if ( UIWidgetUtil.getClassSimpleName(UIScreenDSS.class.getName())
				.equals(uiCtrl) ) {
			
			viewXMLFile = strUIScreenDSS;
			
			uiWidget_i = new UIScreenDSS();

		} else if ( UIWidgetUtil.getClassSimpleName(UIScreenOPM.class.getName())
				.equals(uiCtrl) ) {
			
			viewXMLFile = strUIScreenOPM;
			
			uiWidget_i = new UIScreenOPM();

		} else {
			
			uiWidget_i = new UIScreenEmpty();

		}
		
		if ( null != uiWidget_i ) {
			uiWidget_i.setUINameCard(uiNameCard);
			uiWidget_i.setViewXMLFile(viewXMLFile);
			uiWidget_i.setOptsXMLFile(optsXMLFile);
			uiWidget_i.init();	
		}
		
		logger.end(className, function);

		return uiWidget_i;
	}
	
}
