package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect_i.CSSSelectEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect_i.CSSSelectViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetCSSSelect_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;

public class UIWidgetCSSSelect2 extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetCSSSelect2.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	// External
	private SimpleEventBus eventBus			= null;

	private UILayoutGeneric uiLayoutGeneric	= null;
	
	private String cssElementName0			= null;
	private String cssValueToVisibile0		= null;
	private String cssValueToInvisibile0	= null;
	
	private String cssElementName1			= null;
	private String cssValueToVisibile1		= null;
	private String cssValueToInvisibile1	= null;
	
	private final String strSet1			= "set1";
	private final String strSet0			= "set0";
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		if ( null != uiEventAction ) {
			String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
			String od1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "op["+op+"]");
			logger.info(className, function, "od1["+od1+"]");
			
			if ( null != op ) {

				// Action
				if ( op.equals(CSSSelectEvent.CSSApply.toString()) ) {
					
					if ( null != od1 ) {
						
						if ( strSet0.equals(od1) ) {
							
							modifyCss(cssElementName0, cssValueToVisibile0, true);
							modifyCss(cssElementName1, cssValueToInvisibile1, true);
						} else if ( strSet1.equals(od1) ) {
							
							modifyCss(cssElementName0, cssValueToInvisibile0, true);
							modifyCss(cssElementName1, cssValueToVisibile1, true);
						} else {
							logger.warn(className, function, "Widget od1[{}] IS UNKNOW", od1);
						}

					} else {
						logger.warn(className, function, "od1[{}] IS NULL", od1);
					}
					
				} else if ( op.equals(CSSSelectEvent.CSSRemove.toString()) ) {
					
					if ( null != od1 ) {
						if ( strSet0.equals(od1) ) {
							
							modifyCss(cssElementName0, cssValueToInvisibile0, false);
							modifyCss(cssElementName1, cssValueToVisibile1, false);
						} else if ( strSet1.equals(od1) ) {
							
							modifyCss(cssElementName0, cssValueToVisibile0, false);
							modifyCss(cssElementName1, cssValueToInvisibile1, false);
						} else {
							logger.warn(className, function, "Widget od1[{}] IS UNKNOW", od1);
						}					
					} else {
						logger.warn(className, function, "od1[{}] IS NULL", od1);
					}
					
				} else if ( op.equals(CSSSelectViewEvent.SetDefaultCSS.toString()) ) {
					
					uiLayoutGeneric.setWidgetValue(strSet1, "true");

				} else {
					logger.warn(className, function, "uiEventAction Operation type IS UNKNOW");
				}

			} else {
				logger.warn(className, function, "op IS NULL");
			}
		}
		

		logger.end(className, function);
	}

	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);

		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			cssElementName0			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSElementName0.toString(), strHeader);
			cssValueToVisibile0		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSValueApplyToElement0.toString(), strHeader);
			cssValueToInvisibile0	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSValueRemoveFromElement0.toString(), strHeader);
			
			cssElementName1			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSElementName1.toString(), strHeader);
			cssValueToVisibile1		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSValueApplyToElement1.toString(), strHeader);
			cssValueToInvisibile1	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.CSSValueRemoveFromElement1.toString(), strHeader);
		}
		
		logger.info(className, function, "cssElementName0[{}]",		cssElementName0);
		logger.info(className, function, "cssValueToApply0[{}]",	cssValueToVisibile0);
		logger.info(className, function, "cssValueToRemove0[{}]",	cssValueToInvisibile0);
		logger.info(className, function, "cssElementName1[{}]",		cssElementName1);
		logger.info(className, function, "cssValueToApply1[{}]",	cssValueToVisibile1);
		logger.info(className, function, "cssValueToRemove1[{}]",	cssValueToInvisibile1);

		uiLayoutGeneric = new UILayoutGeneric();
		
		uiLayoutGeneric.setUINameCard(this.uiNameCard);
		uiLayoutGeneric.setViewXMLFile(viewXMLFile);
		uiLayoutGeneric.setOptsXMLFile(optsXMLFile);
		uiLayoutGeneric.init();
		rootPanel = uiLayoutGeneric.getMainPanel();
		
		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);
			
		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);
		
		logger.end(className, function);
	}
	
	private void modifyCss(String element, String style, boolean set ) {
		final String function = "modifyCss";
		
		logger.info(className, function, "element[{}] style[{}] set[{}]", new Object[]{element, style, set});
		
		UIWidget_i uiWidget = uiLayoutGeneric.getUIWidget(element);
		if ( null != uiWidget ) {
			Panel panel = uiWidget.getMainPanel();
			if ( null != panel ) {
				if ( null != style && style.trim().length() > 0 ) {
					if ( set ) {
						panel.addStyleName(style);
					} else {
						panel.removeStyleName(style);
					}
				} else {
					logger.warn(className, function, "element[{}] style IS NULL OR length IS ZERO", element);
				}
			} else {
				logger.warn(className, function, "element[{}] panel IS NULL", element);
			}			
		} else {
			logger.warn(className, function, "element[{}] uiWidget IS NULL", element);
		}
	}
}
