package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.verify.translaction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationEngine;
import com.thalesgroup.scadagen.whmi.translation.translationmgr.client.TranslationMgr;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.util.Translation;

public class UIWidgetVerifyTranslationControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetVerifyTranslationControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private void getTranslationPattern() {
		final String function = "getTranslationPattern";
		logger.begin(className, function);
		
		String strOutput = Translation.getTranslatePattern();
		logger.debug(className, function, " strOutput[{}]", strOutput);
		
		uiGeneric.setWidgetValue("resultvalue", strOutput);
		
		logger.end(className, function);
	}
	
	private void setTranslationPattern() {
		final String function = "setTranslationPattern";
		logger.begin(className, function);
		
		String strInputStr		= uiGeneric.getWidgetValue("inputstrvalue");
		
		logger.debug(className, function, " strInputStr[{}]", strInputStr);
		Translation.setTranslatePattern(strInputStr);
		
		logger.end(className, function);
	}
	
	private void getTranslationFlag() {
		final String function = "getTranslationFlag";
		logger.begin(className, function);
		
		String strOutput = Translation.getTranslateFlag();
		logger.debug(className, function, " strOutput[{}]", strOutput);
		
		uiGeneric.setWidgetValue("resultvalue", strOutput);
		
		logger.end(className, function);
	}
	
	private void setTranslationFlag() {
		final String function = "setTranslationFlag";
		logger.begin(className, function);
		
		String strInputStr		= uiGeneric.getWidgetValue("inputstrvalue");
		
		logger.debug(className, function, " strInputStr[{}]", strInputStr);
		Translation.setTranslateFlag(strInputStr);
		
		logger.end(className, function);
	}
	
	private void getTranslationEngine() {
		final String function = "getTranslationEngine";
		logger.begin(className, function);
		
		String strOutputStr = null;
		
		TranslationEngine translationEngine = TranslationMgr.getInstance().getTranslationEngine();
		
		if ( null != translationEngine ) strOutputStr = translationEngine.toString();
		
		uiGeneric.setWidgetValue("resultvalue", strOutputStr);
		
		logger.end(className, function);
	}
	
	private void setTranslationEngine() {
		final String function = "setTranslationEngine";
		logger.begin(className, function);
		
		TranslationMgr.getInstance().setTranslationEngine(new TranslationEngine() {
			@Override
			public String getMessage(String message) {
				
				return Translation.getDBMessage(message);
			}
			
			@Override
			public String getMessage(String msgWithPlaceHolder, Object[] msgParam) {
				String result = Translation.getDBMessage(msgWithPlaceHolder); 
				if (null != msgParam && msgParam.length > 0)
				{
					String [] splits = result.split("{}");
					final StringBuffer buffer = new StringBuffer();
					for ( int i = 0 ; i < splits.length ; ++i) {
						buffer.append(splits[i]);
						if ( i < splits.length - 1 ) {
							if ( null != msgParam[i] ) {
								buffer.append(msgParam[i]);
							} else {
								buffer.append("{}");
							}
						}
					}
					result = buffer.toString();
				}
				return result;
			}
		});
		
		logger.end(className, function);
	}
	
	private void translation() {
		final String function = "translation";
		logger.begin(className, function);
		
		String strInputStr		= uiGeneric.getWidgetValue("inputstrvalue");
		
		TranslationMgr translationMgr = TranslationMgr.getInstance();
        String strOutputStr = translationMgr.getTranslation(strInputStr);
        logger.debug(className, function, " strInputStr[{}] strOutputStr[{}]", strInputStr, strOutputStr);
		
		uiGeneric.setWidgetValue("resultvalue", strOutputStr);
		
		logger.end(className, function);
	}
	
	private void launch(String element) {
		final String function = "launch";
		logger.begin(className, function);
		
		element = element.toLowerCase();
		
		logger.debug(className, function, " element[{}]", element);

		if ( "getTranslationPattern".toLowerCase().equals(element)) {
			getTranslationPattern();
		} 
		else if ( "setTranslationPattern".toLowerCase().equals(element)) {
			setTranslationPattern();
		}
		else if ( "getTranslationFlag".toLowerCase().equals(element)) {
			getTranslationFlag();
		}
		else if ( "setTranslationFlag".toLowerCase().equals(element)) {
			setTranslationFlag();
		}
		else if ( "getTranslationEngine".toLowerCase().equals(element) ) {
			getTranslationEngine();
		}
		else if ( "setTranslationEngine".toLowerCase().equals(element) ) {
			setTranslationEngine();
		}
		else if ( "translation".toLowerCase().equals(element) ) {
			translation();
		} 
		logger.end(className, function);
	}

	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(className, function, "element[{}]", element);
						if ( null != element ) {
							launch(element);
						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				// TODO Auto-generated method stub
				
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
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
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.begin(className, function);
			};
		};

		logger.end(className, function);
	}

}
