package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import com.google.gwt.event.shared.SimpleEventBus;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.SummaryViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.View_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class UIWidgetSummary extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSummary.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private final String strXml			= ".xml";
	
	private final String strSummary		= "UIWidgetSummary";
	private final String strViewer		= "UIWidgetViewer";
	private final String strAction		= "UIWidgetAction";
	private final String strControl		= "UIWidgetControl";
	private final String strFilter		= "UIWidgetFilter";
	private final String strPrint		= "UIWidgetPrint";

	private UILayoutGeneric uiLayoutGeneric			= null;
	
	private SimpleEventBus eventBus = null;

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		eventBus = UIEventActionBus.getInstance().getEventBus(strSummary);

		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
//				message = Translation.getWording(message);
				return message;
			}
		});
		
		UIWidgetMgr.getInstance().addUIWidgetFactory(strSummary, new UIWidgetMgrFactory() {
			@Override
			public UIWidget_i getUIWidget(String widget) {
				UIWidget_i uiWidget = null;
				if ( 0 == widget.compareTo(strViewer) ) {
					
					uiWidget = new UIWidgetViewer();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), strSummary);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(strViewer+strXml);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strAction) ) {
					
					uiWidget = new UIWidgetAction();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), strSummary);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(strAction+strXml);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strControl) ) {
					
					uiWidget = new UIWidgetControl();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), strSummary);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(strControl+strXml);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strFilter) ) {
					
					uiWidget = new UIWidgetFilter();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), strSummary);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(strFilter+strXml);
					uiWidget.init();
					
				} else if ( 0 == widget.compareTo(strPrint) ) {
					
					uiWidget = new UIWidgetPrint();
					uiWidget.setParameter(ParameterName.SimpleEventBus.toString(), strSummary);
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.setXMLFile(strPrint+strXml);
					uiWidget.init();
				}
				return uiWidget;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setXMLFile(strSummary+strXml);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		{
			UIEventAction action1 = new UIEventAction();
			action1.setParameters(ViewAttribute.Operation.toString(), SummaryViewEvent.SetDefaultFilter.toString());
			eventBus.fireEventFromSource(action1, this);
		}
		
		logger.end(className, function);
	}
}
