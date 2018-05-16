package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Panel;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UILayoutRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;

public class UIWidgetCSSSwitch extends UILayoutRealize {
	
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private void modifyCss(String element, String style, boolean set ) {
		final String function = "modifyCss";
		
		logger.info(function, "element[{}] style[{}] set[{}]", new Object[]{element, style, set});
		
		UIWidget_i uiWidget = ((UILayoutGeneric)uiGeneric).getUIWidget(element);
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
					logger.warn(function, "element[{}] style IS NULL OR length IS ZERO", element);
				}
			} else {
				logger.warn(function, "element[{}] panel IS NULL", element);
			}			
		} else {
			logger.warn(function, "element[{}] uiWidget IS NULL", element);
		}
	}
	
	private void execute(UIEventAction uiEventAction) {
		final String function = "execute";
		logger.begin(function);
		
		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
		
		logger.info(function, "os1["+os1+"]");

		Map<String, Map<String, Object>> override = null;
		
		uiEventActionProcessor_i.executeActionSet(os1, override, new UIExecuteActionHandler_i() {
			
			@Override
			public boolean executeHandler(UIEventAction uiEventAction) {
				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
				String os3	= (String) uiEventAction.getParameter(ViewAttribute.OperationString3.toString());
				String os4	= (String) uiEventAction.getParameter(ViewAttribute.OperationString4.toString());

				logger.info(function, "os1["+os1+"]");
				logger.info(function, "os2["+os2+"]");
				logger.info(function, "os3["+os3+"]");
				logger.info(function, "os4["+os4+"]");						
				
				if ( os1.equals("ModifyCSS") ) {
					
					if ( null != os2 && null != os3 && null != os4 ) {
						String cssElementName = os2;
						String cssValueName = os3;
						boolean applyRemove = os4.equals("true");
						modifyCss(cssElementName, cssValueName, applyRemove);
					} else {
						logger.warn(function, "os2 IS NULL or os3 IS NULL or os4 IS NULL ");
					}
				}
				return true;
			}
		});
		logger.end(function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				logger.begin(function);
				
				if ( null != uiEventAction ) {
					
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(function, "oe["+oe+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							
							execute(uiEventAction);
						}
					}
				}

				logger.end(function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(function);
			}
		
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(function);
				envDown(null);
				logger.begin(function);
			};
		};

		logger.end(function);
	}
	
	@Override
	public boolean fromUILayoutSummaryInit(UIEventAction uiEventAction) {
		execute(uiEventAction);
		return true;
	}

}
