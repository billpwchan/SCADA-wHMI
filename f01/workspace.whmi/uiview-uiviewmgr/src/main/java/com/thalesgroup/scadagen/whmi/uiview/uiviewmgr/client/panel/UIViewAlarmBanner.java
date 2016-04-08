package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uipanel.uipanel.client.UIPanel_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIViewAlarmBanner implements UIPanel_i, WrapperScsAlarmListPanelEvent {
	
	private static Logger logger = Logger.getLogger(UIViewAlarmBanner.class.getName());

	private UINameCard uiNameCard;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.FINE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
		
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    WrapperScsAlarmListPanel wrapperScsAlarmListPanelBanner = new WrapperScsAlarmListPanel(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanelBanner.setSize("1400px", "200px");
	    wrapperScsAlarmListPanelBanner.setBorderWidth(1);
	    wrapperScsAlarmListPanelBanner.setWrapperScsAlarmListPanelEvent(this);
	    
	    DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
	    dockLayoutPanel.add(wrapperScsAlarmListPanelBanner.getMainPanel());
	    dockLayoutPanel.addStyleName("project-gwt-panel-alarmbanner");
	    
	    logger.log(Level.FINE, "getMainPanel Begin");
	    
		return dockLayoutPanel;
	}
	
	private UIViewAlarmBannerEvent uiViewAlarmBannerInterface = null;
	public void setUIViewAlarmBannerEvent(UIViewAlarmBannerEvent uiViewAlarmBannerInterface) {
		if ( null != uiViewAlarmBannerInterface ) 
			this.uiViewAlarmBannerInterface = uiViewAlarmBannerInterface;
	}

	@Override
	public void valueChanged(String name, String value) {
		if ( null != uiViewAlarmBannerInterface )
			uiViewAlarmBannerInterface.valueChanged(name, value);
	}

}
