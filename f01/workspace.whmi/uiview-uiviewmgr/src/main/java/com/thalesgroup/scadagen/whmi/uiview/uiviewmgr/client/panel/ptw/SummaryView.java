package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewWidget;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class SummaryView extends UIWidget_i {
	
	private Logger logger = Logger.getLogger(SummaryView.class.getName());
	
	private String logPrefix		= "[SummaryView] ";
    
    private String strPTWSummary	= "PTWSummary";
    private String strPTWViewer		= "PTWViewer";
    private String strPTWAction		= "PTWAction";
    private String strPTWFilter		= "PTWFilter";
    
    private String strptwdciset 	= "ptwdciset";

	private UILayoutGeneric uiLayoutGeneric			= null;
	
	private SimpleEventBus eventBus = null;

	@Override
	public void init() {
		
		logger.log(Level.SEVERE, logPrefix+"init Begin");

		eventBus = UIEventActionBus.getInstance().getEventBus(strPTWSummary);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				return message;
			}
		});
		
		UIWidgetMgr.getInstance().addUIWidgetFactory(ViewWidget.UIPanelPTWSummary.toString(), new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String widget) {
				UIWidget_i uiWidget = null;
				if ( 0 == widget.compareTo(strPTWViewer) ) {
					
					uiWidget = new ViewerView();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBus);
					uiWidget.setParameter(ParameterName.ListConfigId.toString(), strptwdciset);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strPTWAction) ) {
					
					uiWidget = new ActionView();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBus);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strPTWFilter) ) {
					
					uiWidget = new FilterView();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), eventBus);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.init();
				}
				return uiWidget;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(ViewWidget.UIPanelPTWSummary.toString());
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		logger.log(Level.SEVERE, logPrefix+"init End");
	}
}
