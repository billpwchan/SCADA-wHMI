package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;
import java.util.LinkedList;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnKeyPressHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueUpdate;

public abstract class UIWidget_i implements UIWidgetAccessable_i  {
	
	protected String className = UIWidgetUtil.getClassSimpleName(UIWidget_i.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	

	protected UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.warn(className, "setUINameCard", "uiNameCard IS NULL");
		}
	}
	
	protected String xmlFile = null;
	@Override
	public void setXMLFile(String xmlFile) {
		final String function = "setXMLFile";
		this.xmlFile = xmlFile;
		logger.info(className, function, "xmlFile[{}]", this.xmlFile);
		if ( null == this.xmlFile ) {
			logger.warn(className, function, "xmlFile IS NULL");
		}
	}
	
	protected Panel rootPanel = null;
	@Override
	public Panel getMainPanel() {
		if ( null == rootPanel ) {
			logger.warn(className, "getMainPanel", "rootPanel IS NULL");
		}
		return rootPanel;
	}
	
	protected HashMap<String, Object> parameters = new HashMap<String, Object>();
    @Override
    public void setParameter(String key, Object value) {
    	logger.info(className, "setParameter", "key[{}] value[{}]", key, value);
    	parameters.put(key, value);
    }
    public Object getParameter(String key) {
    	return parameters.get(key);
    }
    public boolean containsParameterKey(String key) {
    	return parameters.containsKey(key);
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
//	
//	protected UIEvent_i uiEvent_i = null;
//	@Override
//	public void setUIEvent(UIEvent_i uiEvent_i) {
//		this.uiEvent_i = uiEvent_i;
//	}
//	
	protected LinkedList<HandlerRegistration> handlerRegistrations = new LinkedList<HandlerRegistration>();
	@Override
	public void addHandlerRegistration(HandlerRegistration handlerRegistration) {
		handlerRegistrations.add(handlerRegistration);
	}
	@Override
	public void removeHandlerRegistrations() {
		HandlerRegistration handlerRegistration = handlerRegistrations.poll();
		while ( null != handlerRegistration ) {
			handlerRegistration.removeHandler();
			handlerRegistration = handlerRegistrations.poll();
		}
	}
	
	@Override
	public void terminate() {
		removeHandlerRegistrations();
	};
	
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
	public WidgetStatus getWidgetStatus(String element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWidgetStatus(String element, WidgetStatus status) {
		// TODO Auto-generated method stub
		
	}

}
