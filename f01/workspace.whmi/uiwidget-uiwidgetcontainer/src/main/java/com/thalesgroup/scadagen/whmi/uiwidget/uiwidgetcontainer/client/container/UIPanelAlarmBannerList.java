package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel_1166B;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIPanelAlarmBannerList implements UIWidget_i, WrapperScsAlarmListPanelEvent {
	
	private static Logger logger = Logger.getLogger(UIPanelAlarmBannerList.class.getName());

	private UINameCard uiNameCard;
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	};
	
	private WrapperScsAlarmListPanel_1166B wrapperScsAlarmListPanel = null;
	private ComplexPanel complexPanel = null;
	@Override
	public void init(String xmlFile) {
		logger.log(Level.FINE, "getMainPanel Begin");
		
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel_1166B(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanel.setSize("1450px", "160px");
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			@Override
			public void valueChanged(String name, String value) {
				logger.log(Level.SEVERE, "valueChanged name["+name+"] value["+value+"]");
				if ( null != uiWidgetEvent ) {
					uiWidgetEvent.onValueChange(name, value);
				}
			}
		});
	   
	    complexPanel = wrapperScsAlarmListPanel.getMainPanel();
	    
	}
	
	@Override
	public ComplexPanel getMainPanel() {

		return complexPanel;
	}
	
	public void setWrapperScsAlarmListPanelEvent ( WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent ) {
		this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent;
	}
	WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent;
	
	@Override
	public void valueChanged(String name, String value) {
		logger.log(Level.FINE, "valueChanged name["+name+"] value["+value+"]");
		if ( null != wrapperScsAlarmListPanelEvent ) 
			this.wrapperScsAlarmListPanelEvent.valueChanged(name, value);
	}

	@Override
	public void setValue(String name) {
		// TODO Auto-generated method stub
		
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

	private UIWidgetEvent uiWidgetEvent = null;
	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		this.uiWidgetEvent = uiWidgetEvent;
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
