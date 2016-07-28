package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.ClientLogger;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.ptw.common.PTWCommonEventBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetmgr.client.UIWidgetMgrFactory;

public class PTWPanel implements UIWidget_i {
	
	/** Logger */
    private final ClientLogger s_logger = ClientLogger.getClientLogger();

	private UINameCard uiNameCard = null;
	public void setUINameCard (UINameCard uiNameCard) { 
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	}
	
	private String strUIPanelPTWSumary				= "UIPanelPTWSummary.xml";

	private UILayoutGeneric uiLayoutGeneric			= null;
	
	
	private SimpleEventBus eventBus = null;
	private ComplexPanel root = null;
	@Override
	public void init(String xmlFile) {
		
		s_logger.debug(PTWAction.class.getName() + "init");

		eventBus = PTWCommonEventBus.getInstance();

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
				if ( 0 == widget.compareTo("PTWViewer") ) {
					uiWidget = new PTWViewer(eventBus, "AlarmList", "EventList");
					uiWidget.setUINameCard(uiNameCard);
					uiWidget.init(null);
				}
//				else
//					if ( 0 == widget.compareTo("PtwActionsPanel_UIWidget") ) {
//						uiWidget = new PtwActionsPanel_UIWidget(eventBus);
//						uiWidget.setUINameCard(uiNameCard);
//						uiWidget.init(null);
//					}
				else 
					if ( 0 == widget.compareTo("PTWAction") ) {
						uiWidget = new PTWAction(eventBus);
						uiWidget.setUINameCard(uiNameCard);
						uiWidget.init(null);
				}
				return uiWidget;
			}
		});
		
		uiLayoutGeneric = new UILayoutGeneric();
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.init(strUIPanelPTWSumary);
		root = uiLayoutGeneric.getMainPanel();
		
		s_logger.debug(PTWAction.class.getName() + "End");
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
