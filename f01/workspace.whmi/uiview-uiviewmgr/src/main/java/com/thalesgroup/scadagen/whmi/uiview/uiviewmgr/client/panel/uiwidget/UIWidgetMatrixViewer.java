package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UILayoutGeneric;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UILayoutRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.matrix.ScsMatrixPanel;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.ScsMatrixPresenterClient;
import com.thalesgroup.scadagen.wrapper.wrapper.client.generic.presenter.SelectionEvent;

public class UIWidgetMatrixViewer extends UILayoutRealize {

	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetMatrixViewer.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

//	private UIEventActionProcessor_i uiEventActionProcessorContextMenu_i = null;
	
	private String scsMatrixElement = null;
	
	private ScsMatrixPanel scsMatrixPanel				= null;
	private ScsMatrixPresenterClient matrixPresenter	= null;
	//private ScsMatrixView matrixView				= null;
	//private GenericMatrixPanelMenu contextMenu					= null;
	

	@Override
	public void init() {
		super.init();
		
		final String function = "init";		
		logger.begin(className, function);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			scsMatrixElement			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetMatrixViewer_i.ParameterName.ScsMatrixElement.toString(), strHeader);
		}
		logger.info(className, function, "scsMatrixElement[{}]", scsMatrixElement);
		
		if ( null == scsMatrixElement ) {
			
			logger.warn(className, function, "scsMatrixElement IS NULL");
			
			String strScsMatrixPanel = UIWidgetUtil.getClassSimpleName(ScsMatrixPanel.class.getName());
			scsMatrixElement = strScsMatrixPanel;
			
			logger.warn(className, function, "Using default ScsMatrixPanel ClassName for scsMatrixElement[{}] AS DEFAULT", scsMatrixElement);
		}
		
		Object object = (ScsMatrixPanel)((UILayoutGeneric)uiGeneric).getUIWidget(scsMatrixElement);
		if ( null != object ) {
			if ( object instanceof ScsMatrixPanel ) {
				scsMatrixPanel = (ScsMatrixPanel)object;
			} else {
				logger.warn(className, function, "scsMatrixElement[{}] instanceof ScsMatrixPanel IS FALSE");
			}
		} else {
			logger.warn(className, function, "scsMatrixElement[{}] IS NULL");
		}
		
		if ( null != scsMatrixPanel ) {
			
//			matrixView = scsMatrixPanel.getView();
			matrixPresenter = scsMatrixPanel.getPresenter();
//			contextMenu = scsMatrixPanel.getContextMenu();
			
			if ( null != matrixPresenter ) {
				
				matrixPresenter.setSelectionEvent(new SelectionEvent() {
					
					@Override
					public void onSelection(Set<HashMap<String, String>> entities) {
						final String function = "onSelection fireFilterEvent";
						
						logger.begin(className, function);
						
						String actionsetkey = "RowSelected";
						HashMap<String, Object> parameter = new HashMap<String, Object>();
						parameter.put(ViewAttribute.OperationObject1.toString(), entities);
						
						HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
						override.put("RowSelected", parameter);
						
						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
						
						logger.end(className, function);
					}
				});

			} else {
				logger.warn(className, function, "matrixPresenter IS NULL");
			}
			
		} else {
			logger.warn(className, function, "scsMatrixPanel IS NULL");
		}
	
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
				
				logger.begin(className, function);
				
				if ( null != uiEventAction ) {
					
					String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
					String os2 = (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
					String os3 = (String) uiEventAction.getParameter(ActionAttribute.OperationString3.toString());
					String os4 = (String) uiEventAction.getParameter(ActionAttribute.OperationString4.toString());
					String os5 = (String) uiEventAction.getParameter(ActionAttribute.OperationString5.toString());
					
					logger.info(className, function, "os1[{}]", os1);
					logger.info(className, function, "os2[{}]", os2);
					logger.info(className, function, "os3[{}]", os3);
					logger.info(className, function, "os4[{}]", os4);
					logger.info(className, function, "os5[{}]", os5);
					
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe[{}] element[{}]", oe, element);
					
					if ( null != oe ) {
						
						if ( oe.equals(element) ) {
					
							if ( null != os1 ) {

							}
						}
					}
					
					if ( null != os1 ) {
						

					} else {
						logger.warn(className, function, "os1 IS NULL");
					}
				} else {
					logger.warn(className, function, "uiEventAction IS NULL");
				}
				
				logger.end(className, function);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {

			}
			
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.begin(className, function);

				logger.end(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.begin(className, function);
//				if ( null != scsMatrixPanel ) {
//					scsMatrixPanel.terminate();
//					scsMatrixPanel = null;
//				}
				logger.end(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
//				envDown(null);
				logger.end(className, function);
			}
		};
		
		logger.end(className, function);
	}
}
