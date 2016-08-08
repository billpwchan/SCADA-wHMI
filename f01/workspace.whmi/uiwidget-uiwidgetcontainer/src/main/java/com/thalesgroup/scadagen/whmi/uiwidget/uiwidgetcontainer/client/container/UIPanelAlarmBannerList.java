package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel_1166B;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIPanelAlarmBannerList extends UIWidget_i implements WrapperScsAlarmListPanelEvent {
	
	private Logger logger = Logger.getLogger(UIPanelAlarmBannerList.class.getName());

	private UINameCard uiNameCard;
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	};
	
	private WrapperScsAlarmListPanel_1166B wrapperScsAlarmListPanel = null;

	@Override
	public void init() {
		logger.log(Level.FINE, "getMainPanel Begin");
		
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel_1166B(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanel.setSize("1450px", "160px");
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			@Override
			public void valueChanged(String name, String value) {
				logger.log(Level.SEVERE, "valueChanged name["+name+"] value["+value+"]");
				if ( null != uiWidgetEventOnValueUpdate ) {
					uiWidgetEventOnValueUpdate.onValueChange(name, value);
				}
			}
		});
	   
	    rootPanel = wrapperScsAlarmListPanel.getMainPanel();
	    
	}
	
	private WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent;
	public void setWrapperScsAlarmListPanelEvent ( WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent ) {
		this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent;
	}
	
	
	@Override
	public void valueChanged(String name, String value) {
		logger.log(Level.FINE, "valueChanged name["+name+"] value["+value+"]");
		if ( null != wrapperScsAlarmListPanelEvent ) 
			this.wrapperScsAlarmListPanelEvent.valueChanged(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		logger.log(Level.FINE, "setValue name["+name+"] value["+value+"]");
		if ( null != name ) {
			if ( "ackVisible".equals(name) ) {
				if ( null != wrapperScsAlarmListPanel ) {
					wrapperScsAlarmListPanel.ackVisible();
				} else {
					logger.log(Level.SEVERE, "setValue wrapperScsAlarmListPanel IS NULL");
				}
			}
		} else {
			logger.log(Level.SEVERE, "setValue name IS NULL");
		}
	}

}
