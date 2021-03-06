package com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client;

import java.util.HashMap;
import java.util.LinkedList;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnKeyPressHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueChangeHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnValueUpdate;

public abstract class UIWidget_i implements UIWidgetConfigurable_i  {
	
	protected String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	protected String element = null;
	@Override
	public void setElement(String element) {
		final String function = "setElement";
		this.element = element;
		logger.debug(function, "element[{}]", this.element);
		if ( null == this.element ) {
			logger.warn(function, "element IS NULL");
		}
	}
	@Override
	public String getElementName() { return this.element; }

	protected UINameCard uiNameCard = null;
	@Override
	public void setUINameCard(UINameCard uiNameCard) {
		if ( null != uiNameCard ) {
			this.uiNameCard = new UINameCard(uiNameCard);
			this.uiNameCard.appendUIPanel(this);
		} else {
			logger.warn("setUINameCard", "uiNameCard IS NULL");
		}
	}
	@Override
	public UINameCard getUINameCard() { return this.uiNameCard; }
	
	protected UIWidgetCtrl_i ctrlHandler = null;
	@Override
	public void setCtrlHandler(UIWidgetCtrl_i ctrlHandler) {
		final String function = "setCtrlHandler";
		this.ctrlHandler = ctrlHandler;
		logger.debug(function, "ctrlHandler[{}]", this.ctrlHandler);
		if ( null == this.ctrlHandler ) {
			logger.warn(function, "ctrlHandler IS NULL");
		}
	}
	
	protected String strCtrlHandler = null;
	@Override
	public void setCtrlHandler(String strCtrlHandler) {
		final String function = "setCtrlHandler";
		this.strCtrlHandler = strCtrlHandler;
		logger.debug(function, "ctrlHandler[{}]", this.strCtrlHandler);
		if ( null == this.strCtrlHandler ) {
			logger.warn(function, "ctrlHandler IS NULL");
		}
	}
	
	protected String dictionaryFolder = null;
	@Override
	public void setDictionaryFolder ( String dictionaryFolder ) {
		final String function = "setDictionaryFolder";
		this.dictionaryFolder = dictionaryFolder;
		logger.debug(function, "dictionaryFolder[{}]", this.dictionaryFolder);
		if ( null == this.dictionaryFolder ) {
			logger.warn(function, "dictionaryFolder IS NULL");
		}
	}
	
	protected String propertyFolder = null;
	@Override
	public void setPropertyFolder ( String propertyFolder ) {
		final String function = "setPropertyFolder";
		this.propertyFolder = propertyFolder;
		logger.debug(function, "propertyFolder[{}]", this.propertyFolder);
		if ( null == this.propertyFolder ) {
			logger.warn(function, "propertyFolder IS NULL");
		}
	}
	
	protected String viewXMLFile = null;
	@Override
	public void setViewXMLFile(String viewXMLFile) {
		final String function = "setViewXMLFile";
		this.viewXMLFile = viewXMLFile;
		logger.debug(function, "viewXMLFile[{}]", this.viewXMLFile);
		if ( null == this.viewXMLFile ) {
			logger.warn(function, "viewXMLFile IS NULL");
		}
	}
	public String getViewXMLFile() { return this.viewXMLFile; }
	
	protected String optsXMLFile = null;
	@Override
	public void setOptsXMLFile(String optsXMLFile) {
		final String function = "setOptXMLFile";
		this.optsXMLFile = optsXMLFile;
		logger.debug(function, "optXMLFile[{}]", this.optsXMLFile);
		if ( null == this.optsXMLFile ) {
			logger.warn(function, "optXMLFile IS NULL");
		}
	}
	public String getOptsXMLFile() { return this.optsXMLFile; }
	
	protected Panel rootPanel = null;
	@Override
	public Panel getMainPanel() {
		if ( null == rootPanel ) {
			logger.warn("getMainPanel", "rootPanel IS NULL");
		}
		return rootPanel;
	}
	
	protected HashMap<String, Object> parameters = new HashMap<String, Object>();
    @Override
    public void setParameter(String key, Object value) {
    	logger.debug("setParameter", "key[{}] value[{}]", key, value);
    	parameters.put(key, value);
    }
    public Object getParameter(String key) {
    	return parameters.get(key);
    }
    public boolean containsParameterKey(String key) {
    	return parameters.containsKey(key);
    }
    public String getStringParameter( String key ) {
    	final String function = "function";
    	logger.debug(function, "key[{}]", key);
    	String result = null;
		if ( containsParameterKey(key) ) {
			Object o = parameters.get(key);
			if ( null != o && o instanceof String) {
				result = (String) o;
			}
		} else {
			logger.warn(function, "key[{}] IS NULL", key);
		}
		return result;
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
	public String [] getUIWidgetElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWidgetValue(String element) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setWidgetValue(String element, String value, Object[] msgParam) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setWidgetValue(String element, String value) {
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
	
	@Override
	public void envUp(String env) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void envDown(String env) {
		// TODO Auto-generated method stub
		
	}

}
