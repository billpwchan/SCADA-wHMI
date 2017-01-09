package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl_i.AutoManuEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr_i.GrcExecMode;

public class UIWidgetSocControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	private final String strPrepareGrc = "PrepareGrc";
	private final String strLaunchGrc = "LaunchGrc";
	private final String strAbortGrc = "AbortGrc";
	
	private String targetDataGrid		= "";
	private String targetDataGridColumn = "";
	private String targetDataGridColumn2 = "";
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected = null;
	
	private int selectedStep = -1;
	
	private int autoMenu = -1;
	
	private int [] skips = null;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
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
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(className, function, "element[{}]", element);
					if ( null != element ) {
						String actionsetkey = element;
						
						// build scsEnvId
						String scsenvid = equipmentSelected.getStringValue(targetDataGridColumn);
						
						logger.info(className, function, "scsenvid[{}]", scsenvid);
						
						// build dbalias						
						String dbalias = equipmentSelected.getStringValue(targetDataGridColumn2);
						
						logger.info(className, function, "dbalias[{}]", dbalias);
						
						int iExecuteMode = getAutoManuMode();
						String executemode = String.valueOf(iExecuteMode);
//						String executemode = String.valueOf(GrcExecMode.StopOnFailed.getValue());
						
						int iCurretnStep = getCurStep();
						String curstep = String.valueOf(iCurretnStep);
//						String curstep = String.valueOf(0);
						
						int [] iSkips = getSkips();
						String skips = convertToStringSkips(iSkips);
//						String skipstep = "";
						// build key
						String strKeyPrepareGrc	= "grc"+strPrepareGrc+"_"+className+"_"+scsenvid+"_"+dbalias;
						String strKeyLaunchGrc	= "grc"+strLaunchGrc+"_"+className+"_"+scsenvid+"_"+dbalias;
						String strKeyAbortGrc	= "grc"+strAbortGrc+"_"+className+"_"+scsenvid+"_"+dbalias;
						
						logger.info(className, function, "strKeyPrepareGrc[{}]", strKeyPrepareGrc);
						logger.info(className, function, "strKeyLaunchGrc[{}]", strKeyLaunchGrc);
						logger.info(className, function, "strKeyAbortGrc[{}]", strKeyAbortGrc);
						
						logger.info(className, function, "executemode[{}]", executemode);
						logger.info(className, function, "curstep[{}]", curstep);
						logger.info(className, function, "skipstep[{}]", skips);
						
						HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
						{
							HashMap<String, Object> parameter = new HashMap<String, Object>();
							parameter.put(ActionAttribute.OperationString2.toString(), strKeyPrepareGrc);
							parameter.put(ActionAttribute.OperationString3.toString(), scsenvid);
							parameter.put(ActionAttribute.OperationString4.toString(), dbalias);
							override.put(strPrepareGrc, parameter);
						}
						{
							HashMap<String, Object> parameter = new HashMap<String, Object>();
							parameter.put(ActionAttribute.OperationString2.toString(), strKeyAbortGrc);
							parameter.put(ActionAttribute.OperationString3.toString(), scsenvid);
							parameter.put(ActionAttribute.OperationString4.toString(), dbalias);
							parameter.put(ActionAttribute.OperationString5.toString(), executemode);
							parameter.put(ActionAttribute.OperationString6.toString(), curstep);
							parameter.put(ActionAttribute.OperationString7.toString(), skips);
							override.put(strLaunchGrc, parameter);
						}
						{
							HashMap<String, Object> parameter = new HashMap<String, Object>();
							parameter.put(ActionAttribute.OperationString2.toString(), strKeyAbortGrc);
							parameter.put(ActionAttribute.OperationString3.toString(), scsenvid);
							parameter.put(ActionAttribute.OperationString4.toString(), dbalias);
							override.put(strAbortGrc, parameter);
						}

						uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
					}
				}
			}
			logger.begin(className, function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(className, function);
			
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(className, function, "os1["+os1+"]");
			
			if ( null != os1 ) {
				if ( os1.equals(AutoManuEvent.RadioBoxSelected.toString() ) ) {
					
					logger.info(className, function, "Store Selected RadioBox");
					
					String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
					
					if ( null != os2 ) {
						
						logger.info(className, function, "os2[{}]", os2);
						
						String autoManu = os2;
						
						if ( autoManu.equals("auto") ) {
							autoMenu = GrcExecMode.Auto.getValue();
						} else if ( autoManu.equals("manu") ) {
							autoMenu = GrcExecMode.StopOnFailed.getValue();
						} else {
							logger.warn(className, function, "os2[{}] type IS UNKNOW", os2);
						}
					} else {
						logger.warn(className, function, "os2 IS NULL");
					}
					
				} else if ( os1.equals(DataGridEvent.RowSelected.toString() ) ) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					
					logger.info(className, function, "Store Selected Row");
					
					if ( null != targetDataGrid ) {
						
						logger.info(className, function, "targetDataGrid[{}]", targetDataGrid);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected	= (String) obj1;
								
								logger.info(className, function, "datagridSelected[{}]", datagridSelected);

								if ( datagridSelected.equals(targetDataGrid) ) {
									if ( null != obj2 ) {
										if ( obj2 instanceof Equipment_i ) {
											equipmentSelected = (Equipment_i) obj2;
										} else {
											equipmentSelected = null;
											
											logger.warn(className, function, "obj2 IS NOT TYPE OF Equipment_i");
										}
									} else {
										logger.warn(className, function, "obj2 IS NULL");
									}
								}
							} else {
								logger.warn(className, function, "obj1 IS NOT TYPE OF String");
							}
						} else {
							logger.warn(className, function, "obj1 IS NULL");
						}
					} else {
						logger.warn(className, function, "targetDataGrid IS NULL");
					}
				} else {
					// General Case
					String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
					
					logger.info(className, function, "oe ["+oe+"]");
					logger.info(className, function, "os1["+os1+"]");
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							uiEventActionProcessor_i.executeActionSet(os1);
						}
					}
				}
			}
			logger.end(className, function);
		}
	};
		
	@Override
	public void init() {
		final String function = "init";
		logger.begin(className, function);
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			targetDataGrid			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid.toString(), strHeader);
			targetDataGridColumn	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn.toString(), strHeader);
			targetDataGridColumn2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn2.toString(), strHeader);
		}
		
		logger.info(className, function, "targetDataGridColumn[{}]", targetDataGridColumn);
		logger.info(className, function, "targetDataGrid[{}]", targetDataGrid);
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessorMgr("UIEventActionProcessor");

		uiEventActionProcessor_i.setUINameCard(uiNameCard);
		uiEventActionProcessor_i.setPrefix(className);
		uiEventActionProcessor_i.setElement(element);
		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
		uiEventActionProcessor_i.setEventBus(eventBus);
		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
		uiEventActionProcessor_i.setUIWidgetGeneric(uiWidgetGeneric);
		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
		uiEventActionProcessor_i.init();
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
					}
				}
			})
		);

		uiEventActionProcessor_i.executeActionSetInit();
		
		logger.end(className, function);
	}
	
	private int getAutoManuMode() {
		final String function = "getCurStep";
		logger.begin(className, function);
		int result = -1;
		result = autoMenu;
		logger.end(className, function);
		return result;		
	}
	
	private int getCurStep() {
		final String function = "getCurStep";
		logger.begin(className, function);
		int result = -1;
		result = selectedStep;
		logger.end(className, function);
		return result;
	}
	
	private String convertToStringSkips(int [] skips) {
		final String function = "convertToStringSkips";
		logger.begin(className, function);
		String result = "";
		if ( null != skips ) {
			for ( int i = 0 ; i < skips.length ; ++i ) {
				if ( result.length() > 0 ) result += ",";
				result += String.valueOf(skips[i]);
			}
		} else {
			logger.warn(className, function, "skips IS NULL");
		}
		logger.end(className, function);
		return result;
	}
	
	private int [] getSkips() {
		final String function = "getSkips";
		logger.begin(className, function);
		int [] skips = null;
		if ( null == this.skips ) {
			skips = new int[]{};
		} else {
			skips = this.skips;
		}
		logger.end(className, function);
		return skips;
	}

}
