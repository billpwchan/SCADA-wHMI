package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class PTWPanel extends UIWidget_i {
	
	/** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();
    
    private String strPTWPanel		= "PTWPanel";
    private String strPTWSummary	= "PTWSummary";
    private String strPTWViewer		= "PTWViewer";
    private String strPTWAction		= "PTWAction";
    
    private String strptwdciset		= "ptwdciset";
    private String strptwdciunset	= "ptwdciset";
    
    private String logPrefix = strPTWPanel;
	
	private String strUIPanelPTWSumary				= "UIPanelPTWSummary.xml";

	private UILayoutGeneric uiLayoutGeneric			= null;
	
	private SimpleEventBus eventBus = null;

	@Override
	public void init() {
		
		s_logger.debug(logPrefix + "init");

		eventBus = UIEventActionBus.getInstance().getEventBus(strPTWSummary);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				return message;
			}
		});
		UIWidgetMgr.getInstance().addUIWidgetFactory("", new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String widget) {
				UIWidget_i uiWidget = null;
				if ( 0 == widget.compareTo(strPTWViewer) ) {
					uiWidget = new PTWViewer(eventBus, strptwdciset, strptwdciunset);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(null);
					uiWidget.init();
				}
				else 
					if ( 0 == widget.compareTo(strPTWAction) ) {
						uiWidget = new PTWAction(eventBus);
						uiWidget.setUINameCard(uiNameCard);
						uiWidget.setXMLFile(null);
						uiWidget.init();
				}
				return uiWidget;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(strUIPanelPTWSumary);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		s_logger.debug(logPrefix + "End");
	}
}
