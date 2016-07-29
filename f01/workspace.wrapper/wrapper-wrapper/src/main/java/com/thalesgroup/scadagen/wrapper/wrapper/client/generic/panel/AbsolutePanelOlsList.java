package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class AbsolutePanelOlsList implements UIWidget_i {

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
    private AbsolutePanel mainPanel_ = null;
    private UIWidget_i uiWidget1 = null;
	private UIWidget_i uiWidget2 = null;
	
	// Internal
	private final SimpleEventBus MWT_EVENT_BUS = new MwtEventBus();
		
	private String listConfigId = null;
	public String getListConfigId() { return listConfigId; }
	public void setListConfigId(String listConfigId) { this.listConfigId = listConfigId; }
	
	private String listConfigId2 = null;
	public String getListConfigId2() { return listConfigId2; }
	public void setListConfigId2(String listConfigId2) { this.listConfigId2 = listConfigId2; }
	
	public AbsolutePanelOlsList(String listConfigId, String listConfigId2) {
		this.listConfigId = listConfigId;
		this.listConfigId2 = listConfigId2;
	}
	
	@Override
	public void init(String xmlFile) {
		mainPanel_ = new AbsolutePanel();
		mainPanel_.addStyleName("project-gwt-panel-ptwviewer-absolutePanelolsslist");
		uiWidget1 = new ScsOlsListPanel(MWT_EVENT_BUS, getListConfigId(), false);

		uiWidget2 = new ScsOlsListPanel(MWT_EVENT_BUS, getListConfigId2(), false);

		mainPanel_.add(uiWidget1.getMainPanel(), 0, 0);
		
		mainPanel_.add(uiWidget2.getMainPanel(), 0, 0);
	}

	@Override
	public ComplexPanel getMainPanel() {
		return mainPanel_;
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
	
	public UIWidget_i getUIWidget() {
		return uiWidget1;
	}
	
	public UIWidget_i getUIWidget2() {
		return uiWidget2;
	}
	
	private UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		}
	}
	@Override
	public void setParameter(String key, String value) {
		// TODO Auto-generated method stub
		
	}
}
