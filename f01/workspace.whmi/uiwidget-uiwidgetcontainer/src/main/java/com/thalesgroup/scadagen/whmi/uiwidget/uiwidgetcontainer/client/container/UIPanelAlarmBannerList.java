package com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetcontainer.client.container;

import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanel_1166B;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsAlarmListPanelEvent;

public class UIPanelAlarmBannerList extends UIWidget_i implements WrapperScsAlarmListPanelEvent {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIPanelAlarmBannerList.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private UINameCard uiNameCard;
	public void setUINameCard(UINameCard uiNameCard) {
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);
	};
	
	private WrapperScsAlarmListPanel_1166B wrapperScsAlarmListPanel = null;

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
	    String ALARM_LIST_BANNER_ID = "alarmListBanner";
	    wrapperScsAlarmListPanel = new WrapperScsAlarmListPanel_1166B(ALARM_LIST_BANNER_ID, false, false, false);
	    wrapperScsAlarmListPanel.setSize("1450px", "160px");
	    wrapperScsAlarmListPanel.setWrapperScsAlarmListPanelEvent(new WrapperScsAlarmListPanelEvent() {
			@Override
			public void valueChanged(String name, String value) {
				logger.warn(className, function, "valueChanged name[{}] value[{}]", name, value);
				if ( null != uiWidgetEventOnValueUpdate ) {
					uiWidgetEventOnValueUpdate.onValueChange(name, value);
				}
			}
		});
	   
	    rootPanel = wrapperScsAlarmListPanel.getMainPanel();

	    logger.end(className, function);
	    
	}
	
	private WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent;
	public void setWrapperScsAlarmListPanelEvent ( WrapperScsAlarmListPanelEvent wrapperScsAlarmListPanelEvent ) {
		this.wrapperScsAlarmListPanelEvent = wrapperScsAlarmListPanelEvent;
	}
	
	
	@Override
	public void valueChanged(String name, String value) {
		logger.info(className, "valueChanged", "name[{}] value[{}]", name, value);
		if ( null != wrapperScsAlarmListPanelEvent ) 
			this.wrapperScsAlarmListPanelEvent.valueChanged(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		final String function = "setValue";
		logger.info(className, function, "setValue name[{}] value[{}]", name, value);
		if ( null != name ) {
			if ( "ackVisible".equals(name) ) {
				if ( null != wrapperScsAlarmListPanel ) {
					wrapperScsAlarmListPanel.ackVisible();
				} else {
					logger.warn(className, function, "setValue wrapperScsAlarmListPanel IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "setValue name IS NULL");
		}
	}

}
