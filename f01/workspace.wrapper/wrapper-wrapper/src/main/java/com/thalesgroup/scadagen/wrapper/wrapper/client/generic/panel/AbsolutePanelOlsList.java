package com.thalesgroup.scadagen.wrapper.wrapper.client.generic.panel;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.common.client.event.MwtEventBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class AbsolutePanelOlsList extends UIWidget_i {

    /**
     * Main panel wrapping this widget and passed to its {@link ResizeComposite}
     * parent
     */
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
	public void init() {
		
		rootPanel = new AbsolutePanel();
		rootPanel.addStyleName("project-gwt-panel-ptwviewer-absolutePanelolsslist");
		
		uiWidget1 = new ScsOlsListPanel(MWT_EVENT_BUS, getListConfigId(), false);

		uiWidget2 = new ScsOlsListPanel(MWT_EVENT_BUS, getListConfigId2(), false);

		((AbsolutePanel)rootPanel).add(uiWidget1.getMainPanel(), 0, 0);
		
		((AbsolutePanel)rootPanel).add(uiWidget2.getMainPanel(), 0, 0);
	}

	public UIWidget_i getUIWidget() {
		return uiWidget1;
	}
	
	public UIWidget_i getUIWidget2() {
		return uiWidget2;
	}
	
}
