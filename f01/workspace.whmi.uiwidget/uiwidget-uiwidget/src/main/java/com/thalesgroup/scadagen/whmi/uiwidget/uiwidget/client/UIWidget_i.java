package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnKeyPressHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueUpdate;

public abstract class UIWidget_i implements UIWidgetAccessable_i  {
	
	protected Logger logger = Logger.getLogger(UIWidget_i.class.getName());
	protected String logPrefix = "UIWidget_i ";

	protected UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.log(Level.SEVERE, logPrefix + "setUINameCard uiNameCard IS NULL");
		}
	}
	
	protected String xmlFile = null;
	@Override
	public void setXMLFile(String xmlFile) {
		this.xmlFile = xmlFile;
		logger.log(Level.FINE, "setXMLFile xmlFile["+this.xmlFile+"]");
		if ( null == this.xmlFile ) {
			logger.log(Level.SEVERE, "setXMLFile xmlFile IS NULL");
		}
	}
	
	protected ComplexPanel rootPanel = null;
	@Override
	public ComplexPanel getMainPanel() {
		if ( null == rootPanel ) {
			logger.log(Level.SEVERE, "getMainPanel rootPanel IS NULL");
		}
		return rootPanel;
	}
	
	protected HashMap<String, String> parameters = new HashMap<String, String>();
    @Override
    public void setParameter(String key, String value) {
    	logger.log(Level.FINE, "setParameter key["+key+"] value["+value+"]");
    	parameters.put(key, value);
    }
    
    protected UIWidgetEventOnValueUpdate			uiWidgetEventOnValueUpdate			= null;
	protected UIWidgetEventOnKeyPressHandler		uiWidgetEventOnKeyPressHandler		= null;
	protected UIWidgetEventOnClickHandler			uiWidgetEventOnClickHandler			= null;
	protected UIWidgetEventOnValueChangeHandler		uiWidgetEventOnValueChangeHandler	= null;
	
	@Override
	public void setUIWidgetEvent(UIWidgetEvent uiWidgetEvent) {
		if ( uiWidgetEvent instanceof UIWidgetEventOnValueUpdate ) {
			this.uiWidgetEventOnValueUpdate = (UIWidgetEventOnValueUpdate) uiWidgetEvent;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnKeyPressHandler ) {
			this.uiWidgetEventOnKeyPressHandler = (UIWidgetEventOnKeyPressHandler) uiWidgetEvent;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnClickHandler ) {
			this.uiWidgetEventOnClickHandler = (UIWidgetEventOnClickHandler) uiWidgetEvent;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnValueChangeHandler ) {
			this.uiWidgetEventOnValueChangeHandler = (UIWidgetEventOnValueChangeHandler) uiWidgetEvent;
		}
	}
	
	@Override
	public UIWidgetEvent getUIWidgetEvent (UIWidgetEvent uiWidgetEvent) {
		if ( uiWidgetEvent instanceof UIWidgetEventOnValueUpdate ) {
			return this.uiWidgetEventOnValueUpdate;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnKeyPressHandler ) {
			return this.uiWidgetEventOnKeyPressHandler;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnClickHandler ) {
			return this.uiWidgetEventOnClickHandler;
			
		} else if ( uiWidgetEvent instanceof UIWidgetEventOnValueChangeHandler ) {
			return this.uiWidgetEventOnValueChangeHandler;
		}
		return null;
	}
	
	protected UIEvent_i uiEvent_i = null;
	@Override
	public void setUIEvent(UIEvent_i uiEvent_i) {
		this.uiEvent_i = uiEvent_i;
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
	public String getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, String up) {
		// TODO Auto-generated method stub
		
	}

}
