package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl_i.AutoManuEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.CtlBrcStatus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.GrcExecStatus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.GrcPointEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr_i.GrcExecMode;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpmSCADAgen;

public class UIWidgetSocControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSocControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

	private UIWidgetGeneric uiWidgetGeneric = null;
	
	private UIEventActionProcessor_i uiEventActionProcessor_i = null;
	
	String strKeyPrepareGrc	= "";
	String strKeyLaunchGrc	= "";
	String strKeyAbortGrc	= "";
	
	private String targetDataGridA			= "";
	private String targetDataGridColumnA	= "";
	private String targetDataGridColumnA2	= "";
	private String targetDataGridColumnA3	= "";
	private String targetDataGridB			= "";
	private String targetDataGridColumnB	= "";
	private String targetDataGridColumnB2	= "";
	private String targetDataGridColumnB3	= "";
	
	private String datagridSelected = null;
	private Equipment_i equipmentSelected = null;
	
	private String scsenvid = "";
	private String dbalias = "";
	private String grcName = "";
	
	private int curStep = 1;
	private int firstStep = 1;
	private int lastExecutedStep = 0;
	private int numSteps = 0;
	
	private int grcStatus = 0;
	//private int stepStatus = 0;
	
	private int autoManu = 0;
	
	private List<Integer> skipList = new ArrayList<Integer>();
	
	private WrapperScsRTDBAccess rtdb = WrapperScsRTDBAccess.getInstance();
	
	//TODO: Need to change GrcMgr to support multiple instances
	private GrcMgr grcMgr = GrcMgr.getInstance("UIEventActionGrc");

	private int [] steps = null;
	private String [] eqpList = null;
	
	private String startElement = "start";
	private String stopElement = "stop";
	private String retryElement = "retry";
	private String skipElement = "skip";
	
	private String reserveAttributeName = null;
	private String reserveAttributeType = null;
	private String reservedValueStr = null;
	private String unreservedValueStr = null;
	
	private int notExecutedSteps = 0;
	private int completedSteps = 0;
	private int failedSteps = 0;
	private int skippedSteps = 0;

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			
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
						
						// build scsEnvId
						scsenvid = equipmentSelected.getStringValue(targetDataGridColumnA);
						
						logger.info(className, function, "scsenvid[{}]", scsenvid);
						
						// build dbalias						
						dbalias = equipmentSelected.getStringValue(targetDataGridColumnA2);
						
						logger.info(className, function, "dbalias[{}]", dbalias);
						
						// Custom handler for "start", "retry", "skip", "stop"
						if (element.equals(startElement)) {

							firstStep = 1;
							
							if (reserveNeeded()) {
								reserveEquipment(steps);
							} else {
								preparegrc();
							}
							
						} else if (element.equals(retryElement)) {

							firstStep = getCurStep();
							
							if (reserveNeeded()) {
								reserveEquipment(steps);
							} else {
								preparegrc();
							}
							
						} else if (element.equals(skipElement)) {

							firstStep = getCurStep() + 1;
							
							if (reserveNeeded()) {
								reserveEquipment(steps);
							} else {
								preparegrc();
							}
							
						} else if (element.equals(stopElement)) {
							
							setAbortGrcResultCallback();
							
							grcMgr.abortGrc(strKeyAbortGrc, scsenvid, dbalias);
						}
					}
				}
			}
			logger.end(className, function);
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
						
						String autoManuStr = os2;
						
						if ( autoManuStr.equals("auto") ) {
							autoManu = GrcExecMode.Auto.getValue();

						} else if ( autoManuStr.equals("manu") ) {
							autoManu = GrcExecMode.StopOnFailed.getValue();

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
					
					if ( null != targetDataGridA ) {
						
						logger.info(className, function, "targetDataGridA[{}]", targetDataGridA);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected	= (String) obj1;
								
								logger.info(className, function, "datagridSelected[{}]", datagridSelected);

								if ( datagridSelected.equals(targetDataGridA) ) {
									if ( null != obj2 ) {
										if ( obj2 instanceof Equipment_i ) {
											equipmentSelected = (Equipment_i) obj2;
											
											scsenvid = equipmentSelected.getStringValue(targetDataGridColumnA);
											dbalias = equipmentSelected.getStringValue(targetDataGridColumnA2);
											grcName = equipmentSelected.getStringValue(targetDataGridColumnA3);
											
											// Reset GRC and Status
											grcStatus = 0;											
											readGrcCurStatus();
											
											// Reset step counts
											notExecutedSteps = 0;
											completedSteps = 0;
											failedSteps = 0;
											skippedSteps = 0;
											lastExecutedStep = 0;
											
											// Clear display message
											String msg = "";
											sendDisplayMessageEvent(msg);
											
											// Read step and eqp from brctable for check reserve later
											readStepEqp();
											
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
						logger.warn(className, function, "targetDataGridA IS NULL");
					}
					
				} else if ( os1.equals(DataGridEvent.ValueChange.toString() )) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					Object obj4 = uiEventAction.getParameter(ViewAttribute.OperationObject4.toString());
					
					logger.info(className, function, "DataGrid ValueChange");
	
					if ( null != obj1 && null != obj2 && null != obj3 && null != obj4 ) {
						if ( obj1 instanceof String ) {
							String dataGrid = (String) obj1;
							if (dataGrid.equals(targetDataGridB)) {
								
								String label = (String)obj2;
								Equipment_i eqp = (Equipment_i)obj3;
								Boolean value = (Boolean)obj4;
								
								if (label.equals(targetDataGridColumnB)) {
									Integer n = eqp.getNumberValue(targetDataGridColumnB2).intValue();
									if (value) {
										addStepToSkip(n);
									} else {
										removeStepToSkip(n);
									}
								}
							}
						}
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
			targetDataGridA			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_A.toString(), strHeader);
			targetDataGridColumnA	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A.toString(), strHeader);
			targetDataGridColumnA2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A2.toString(), strHeader);
			targetDataGridColumnA3	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_A3.toString(), strHeader);
			targetDataGridB			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGrid_B.toString(), strHeader);
			targetDataGridColumnB	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_B.toString(), strHeader);
			targetDataGridColumnB2	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_B2.toString(), strHeader);
			targetDataGridColumnB3	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.TargetDataGridColumn_B3.toString(), strHeader);
			
			String element	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.StartElement.toString(), strHeader);
			if (element != null && !element.isEmpty()) {
				startElement = element;
			}
			element	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.StopElement.toString(), strHeader);
			if (element != null && !element.isEmpty()) {
				stopElement = element;
			}
			element	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.RetryElement.toString(), strHeader);
			if (element != null && !element.isEmpty()) {
				retryElement = element;
			}
			element	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.SkipElement.toString(), strHeader);
			if (element != null && !element.isEmpty()) {
				skipElement = element;
			}
			
			reserveAttributeName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeName.toString(), strHeader);
			reserveAttributeType	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeType.toString(), strHeader);
			reservedValueStr		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReservedValueStr.toString(), strHeader);
			unreservedValueStr		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.UnreservedValueStr.toString(), strHeader);
		}

		logger.info(className, function, "targetDataGridA[{}]", targetDataGridA);
		logger.info(className, function, "targetDataGridColumnA[{}]", targetDataGridColumnA);
		logger.info(className, function, "targetDataGridColumnA2[{}]", targetDataGridColumnA2);
		logger.info(className, function, "targetDataGridColumnA3[{}]", targetDataGridColumnA3);
		logger.info(className, function, "targetDataGridB[{}]", targetDataGridB);
		logger.info(className, function, "targetDataGridColumnB[{}]", targetDataGridColumnB);
		logger.info(className, function, "targetDataGridColumnB2[{}]", targetDataGridColumnB2);
		logger.info(className, function, "targetDataGridColumnB3[{}]", targetDataGridColumnB3);
		
		logger.info(className, function, "startElement[{}]", startElement);
		logger.info(className, function, "stopElement[{}]", stopElement);
		logger.info(className, function, "retryElement[{}]", retryElement);
		logger.info(className, function, "skipElement[{}]", skipElement);
		
		logger.info(className, function, "reserveAttributeName[{}]", reserveAttributeName);
		logger.info(className, function, "reserveAttributeType[{}]", reserveAttributeType);
		logger.info(className, function, "reservedValueStr[{}]", reservedValueStr);
		logger.info(className, function, "unreservedValueStr[{}]", unreservedValueStr);
		
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
		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
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
	
	private boolean reserveNeeded() {
		final String function = "readStepAliases";
		
		if (reserveAttributeName == null || reserveAttributeName.isEmpty() ||
			reserveAttributeType == null || reserveAttributeType.isEmpty() ||
			(!reserveAttributeType.equalsIgnoreCase("String") && !reserveAttributeType.equalsIgnoreCase("Integer")) ||
			reservedValueStr == null || unreservedValueStr == null) {
			logger.debug(className, function, "reserve not setup");
			return false;
		}

		logger.debug(className, function, "reserve is setup");
		return true;
	}
	
	private void readStepEqp() {
		final String function = "readStepAliases";
		
		logger.begin(className, function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		
		String [] dbaddresses = new String [2];
		
		dbaddresses[0] = "<alias>" + dbalias + ".brctable(0:$, number)";
		dbaddresses[1] = "<alias>" + dbalias + ".brctable(0:$, eqp)";
		
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				
				if (values == null || values.length != 2) {
					logger.error(className, function, "return values is null or size not correct");
					return;
				}
				logger.debug(className, function, "numbers [{}]", values[0]);
				String str1 = values[0].substring(1, values[0].length()-1);
				String str2 = str1.replaceAll("\"", "");
				logger.debug(className, function, "numbers substring [{}]", str1);
				logger.debug(className, function, "numbers replaceAll [{}]", str2);
				String [] numbers = str2.split(",");
				
				logger.debug(className, function, "eqps [{}]", values[1]);
				String str3 = values[1].substring(1, values[1].length()-1);
				String str4 = str3.replaceAll("\"", "");
				logger.debug(className, function, "eqps substring [{}]", str3);
				logger.debug(className, function, "eqps replaceAll [{}]", str4);
				String [] eqps = str4.split(",");
				
				numSteps = 0;
				
				for (int i=0; i<numbers.length; i++) {
					if (numSteps > 0 && (numbers[i].equals("0") || eqps[i] == null || eqps[i].isEmpty())) {
						break;
					}
					numSteps++;
				}
				
				steps = new int[numSteps];
				eqpList = new String[numSteps];
				for (int row=0; row<numSteps; row++) {
					steps[row] = Integer.parseInt(numbers[row]);
					eqpList[row] = eqps[row];
				}
			}
			
		});
		
	}
	
	private void setPrepareGrcResultCallback() {
		final String function = "setPrepareGrcResultCallback";
		
		logger.begin(className, function);
		
		Subject prepareGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "prepareGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(className, function, errorMsg);
							
							sendDisplayMessageEvent(errorMsg);
						}
					} else {
						logger.info(className, function, "prepareGrc done. launchgrc");
						launchgrc();
					}
				}
			}
			
		};
		observer.setSubject(prepareGrcSubject);
		
		String callback = "setPrepareGrcResult";
		String key = strKeyPrepareGrc + callback;
		grcMgr.setSubject(key, prepareGrcSubject);
		
		logger.end(className, function);
	}
	
	private void setLaunchGrcResultCallback() {
		final String function = "setLaunchGrcResultCallback";
		
		logger.begin(className, function);
		
		Subject launchGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "launchGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(className, function, errorMsg);
							
							sendDisplayMessageEvent(errorMsg);
						}
					}
				}
			}
			
		};
		observer.setSubject(launchGrcSubject);
		
		String callback = "setLaunchGrcResult";
		String key = strKeyLaunchGrc + callback;
		grcMgr.setSubject(key, launchGrcSubject);
		
		logger.end(className, function);
	}
	
	private void setGrcStatusResultCallback() {
		final String function = "setGrcStatusResultCallback";
		
		logger.begin(className, function);
		
		Subject grcStatusSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "grcStatusSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				int errorCode = 0;
				if (errorCodeObj != null) {
					errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(className, function, errorMsg);
							
							sendDisplayMessageEvent(errorMsg);
						}
					}
				}
				
				if (errorCode == 0) {
					JSONNumber grcStatusObj = obj.get("grcStatus").isNumber();
					if (grcStatusObj != null) {
						String grcStatusStr = grcStatusObj.toString();
	
						sendGrcStatusEvent(grcStatusStr);

						int prevGrcStatus = grcStatus;
						grcStatus = (int) grcStatusObj.doubleValue();
						
						String actionsetkey = "GrcStatus_";
						if (grcStatus == GrcExecStatus.Terminated.getValue()) {
							if (prevGrcStatus != grcStatus ) {
								if (reserveNeeded()) {
									String [] reserveAliases = getReserveAlias();
									unsetReserve(reserveAliases);
								}
								
								readGrcStepStatuses(numSteps);
							}
							actionsetkey = actionsetkey + GrcExecStatus.Terminated.name();
							
						} else if (grcStatus == GrcExecStatus.WaitForRun.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.WaitForRun.name();
							
						} else if (grcStatus == GrcExecStatus.Initializing.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Initializing.name();
							
						} else if (grcStatus == GrcExecStatus.Running.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Running.name();
							
						} else if (grcStatus == GrcExecStatus.Waiting.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Waiting.name();
							
						} else if (grcStatus == GrcExecStatus.Stopped.getValue()) {
							
							logger.debug(className, function, "autoManu[{}]", autoManu);
							
							if (prevGrcStatus != grcStatus) {
								if (reserveNeeded()) {
									String [] reserveAliases = getReserveAlias();
									unsetReserve(reserveAliases);
								}
								
								readGrcStepStatuses(numSteps);
							}
							
							if (autoManu == GrcExecMode.Auto.getValue()) {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name();							
							} else {
								
								actionsetkey = null;
							}
						} else if (grcStatus == GrcExecStatus.Aborted.getValue()) {
							if (prevGrcStatus != grcStatus) {
								if (reserveNeeded()) {
									String [] reserveAliases = getReserveAlias();
									unsetReserve(reserveAliases);
								}
								
								readGrcStepStatuses(numSteps);
							}
							actionsetkey = actionsetkey + GrcExecStatus.Aborted.name();
							
						} else if (grcStatus == GrcExecStatus.Suspended.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Suspended.name();
						} else if (grcStatus == GrcExecStatus.Resumed.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Resumed.name();
						}
						
						if (actionsetkey != null) {
							logger.debug(className, function, "send actionsetkey [{}]", actionsetkey);
						
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}
					}
				}
			}
			
		};
		observer.setSubject(grcStatusSubject);
		
		String callback = "setGrcStatusResult";
		String key = strKeyLaunchGrc + callback;
		grcMgr.setSubject(key, grcStatusSubject);
		
		logger.end(className, function);
	}
	

	private void setStepResultCallback() {
		final String function = "setStepResultCallback";
		
		logger.begin(className, function);
		
		Subject stepSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "stepSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				int errorCode = 0;
				if (errorCodeObj != null) {
					errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(className, function, errorMsg);
							
							sendDisplayMessageEvent(errorMsg);
						}
					}
				}
				
				if (errorCode == 0) {
					JSONNumber stepObj = obj.get("step").isNumber();
					if (stepObj != null) {
						String stepStr = stepObj.toString();
						
						// Ignore step 0
						//if (!stepStr.equals("0")) {
							sendGrcStepEvent(stepStr);
						//}
							
						curStep = (int)stepObj.doubleValue();
						if (curStep > 0) {
							lastExecutedStep = curStep;
						}
						logger.debug(className, function, "current step [{}]", curStep);
						logger.debug(className, function, "last executed step [{}]", lastExecutedStep);
					}
				}
				
				sendReloadColumnDataEvent();
			}
			
		};
		observer.setSubject(stepSubject);
		
		String callback = "setStepResult";
		String key = strKeyLaunchGrc + callback;
		grcMgr.setSubject(key, stepSubject);
		
		logger.end(className, function);
	}
	
	private void setAbortGrcResultCallback() {
		final String function = "setAbortGrcCallback";
		
		logger.begin(className, function);
		
		Subject abortGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(className, function, "abortGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(className, function, errorMsg);
							
							sendDisplayMessageEvent(errorMsg);
						}
					}
				}
				readGrcCurStatus();
			}
			
		};
		observer.setSubject(abortGrcSubject);
		
		String callback = "setAbortGrcResult";
		String key = strKeyAbortGrc + callback;
		grcMgr.setSubject(key, abortGrcSubject);
		
		logger.end(className, function);
	}
	
	protected void removeStepToSkip(Integer n) {
		final String function = "removeStepToSkip";
		logger.debug(className, function, "remove [{}] from skip list", n);
		if (skipList.contains(n)) {
			skipList.remove(n);
			logger.debug(className, function, "[{}] removed from skip list", n);
		}
	}

	protected void addStepToSkip(Integer n) {
		final String function = "addStepToSkip";
		logger.debug(className, function, "add [{}] to skip list", n);
		if (!skipList.contains(n)) {
			skipList.add(n);
			logger.debug(className, function, "[{}] added to skip list", n);
		}
	}

//	private int getAutoManuMode() {
//		final String function = "getAutoManuMode";
//		logger.begin(className, function);
//		int result = -1;
//		result = autoManu;
//		logger.end(className, function);
//		return result;
//	}
	
	private int getCurStep() {
		final String function = "getCurStep";
		logger.begin(className, function);
		int result = 1;
		
		if (curStep > 0) {
			result = curStep;
		}
		logger.end(className, function);
		return result;
	}
	
//	private String convertToStringSkips(int [] skips) {
//		final String function = "convertToStringSkips";
//		logger.begin(className, function);
//		String result = "";
//		if ( null != skips ) {
//			for ( int i = 0 ; i < skips.length ; ++i ) {
//				if ( result.length() > 0 ) result += ",";
//				result += String.valueOf(skips[i]);
//			}
//		} else {
//			logger.warn(className, function, "skips IS NULL");
//		}
//		logger.end(className, function);
//		return result;
//	}
	
	private int [] getSkips() {
		final String function = "getSkips";
		logger.begin(className, function);
		int [] skips = null;
		if ( null == this.skipList || skipList.isEmpty() ) {
			skips = new int []{};
		} else {
			Collections.sort(skipList);
			Integer [] a = skipList.toArray(new Integer[skipList.size()]);
			skips = new int [skipList.size()];
			for (int i=0; i<a.length; i++) {
				skips[i] = a[i];
			}
		}
		
		if (logger.isDebugEnabled()) {
			String skipStepsStr = "";
			for (int i=0; i<skips.length; i++) {
				skipStepsStr += skips[i] + " ";
			}
			logger.debug(className, function, "skip steps=[{}]", skipStepsStr);
		}
		logger.end(className, function);
		return skips;
	}

	private EventBus getEventBus() {
		return this.eventBus;
	}
	
//	private void sendReloadDataEvent() {
//		final String function = "sendReloadDataEvent";
//		UIEventAction reloadDataEvent = new UIEventAction();
//		if (reloadDataEvent != null) {
//			reloadDataEvent.setParameter(ViewAttribute.OperationString1.toString(), DataGridEvent.ReloadFromDataSource.toString());
//			reloadDataEvent.setParameter(ViewAttribute.OperationObject1.toString(), targetDataGridB);
//			reloadDataEvent.setParameter(ViewAttribute.OperationObject2.toString(), scsenvid);
//			reloadDataEvent.setParameter(ViewAttribute.OperationObject3.toString(), dbalias);
//			getEventBus().fireEvent(reloadDataEvent);
//			logger.debug(className, function, "fire UIEventAction reloadDataEvent");
//		}
//	}
	
	private void sendReloadColumnDataEvent() {
		final String function = "sendReloadColumnDataEvent";
		UIEventAction reloadDataEvent = new UIEventAction();
		if (reloadDataEvent != null) {
			reloadDataEvent.setParameter(ViewAttribute.OperationString1.toString(), DataGridEvent.ReloadColumnData.toString());
			reloadDataEvent.setParameter(ViewAttribute.OperationObject1.toString(), targetDataGridB);
			reloadDataEvent.setParameter(ViewAttribute.OperationObject2.toString(), targetDataGridColumnB3);
			getEventBus().fireEvent(reloadDataEvent);
			logger.debug(className, function, "fire UIEventAction reloadDataEvent");
		}
	}

	private void sendDisplayMessageEvent(String msg) {
		final String function = "sendDisplayMessageEvent";
		UIEventAction displayMessageEvent = new UIEventAction();
		if (displayMessageEvent != null) {
			displayMessageEvent.setParameter(ViewAttribute.OperationString1.toString(), GrcPointEvent.DisplayMessage.toString());
			displayMessageEvent.setParameter(ViewAttribute.OperationObject1.toString(), scsenvid);
			displayMessageEvent.setParameter(ViewAttribute.OperationObject2.toString(), dbalias);
			displayMessageEvent.setParameter(ViewAttribute.OperationObject3.toString(), msg);
			getEventBus().fireEvent(displayMessageEvent);
			logger.debug(className, function, "fire UIEventAction displayMessageEvent");
		}
	}
	
	private void sendGrcStatusEvent(String statusStr) {
		final String function = "sendGrcStatusEvent";
		
		UIEventAction grcStatusEvent = new UIEventAction();
		if (grcStatusEvent != null) {
			grcStatusEvent.setParameter(ViewAttribute.OperationString1.toString(), GrcPointEvent.CurStatus.toString());
			grcStatusEvent.setParameter(ViewAttribute.OperationObject1.toString(), scsenvid);
			grcStatusEvent.setParameter(ViewAttribute.OperationObject2.toString(), dbalias);
			grcStatusEvent.setParameter(ViewAttribute.OperationObject3.toString(), statusStr);
			getEventBus().fireEvent(grcStatusEvent);
			logger.debug(className, function, "fire UIEventAction grcStatusEvent");
		}
	}
	
	private void sendGrcStepEvent(String stepStr) {
		final String function = "sendGrcStepEvent";
		
		UIEventAction grcStepEvent = new UIEventAction();
		if (grcStepEvent != null) {
			grcStepEvent.setParameter(ViewAttribute.OperationString1.toString(), GrcPointEvent.CurStep.toString());
			grcStepEvent.setParameter(ViewAttribute.OperationObject1.toString(), scsenvid);
			grcStepEvent.setParameter(ViewAttribute.OperationObject2.toString(), dbalias);
			grcStepEvent.setParameter(ViewAttribute.OperationObject3.toString(), stepStr);
			getEventBus().fireEvent(grcStepEvent);
			logger.debug(className, function, "fire UIEventAction grcStepEvent");
		}
	}

	
	private void readGrcCurStatus() {
		final String function = "readGrcCurStatus";
		
		logger.begin(className, function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		String [] dbaddresses = new String [1];
		
		dbaddresses[0] = "<alias>" + dbalias + ".curstatus";
		// Use multiRead to read GRC curstatus
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readGrcCurStatus setReadResult";

				if (errorCode != 0) {
					logger.debug(className, function, "readResult errorCode=[{}]");
					sendDisplayMessageEvent(errorMessage);
				} else {
					if (values != null) {
						String curStatusStr = values[0];
						sendGrcStatusEvent(curStatusStr);
						
						grcStatus = Integer.parseInt(curStatusStr);
						String actionsetkey = "GrcStatus_";
						if (grcStatus == GrcExecStatus.Terminated.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Terminated.name();
						} else if (grcStatus == GrcExecStatus.WaitForRun.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.WaitForRun.name();
						} else if (grcStatus == GrcExecStatus.Initializing.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Initializing.name();
						} else if (grcStatus == GrcExecStatus.Running.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Running.name();
						} else if (grcStatus == GrcExecStatus.Waiting.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Waiting.name();
						} else if (grcStatus == GrcExecStatus.Stopped.getValue()) {

							logger.debug(className, function, "autoManu[{}]", autoManu);
							if (autoManu == GrcExecMode.Auto.getValue()) {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name();
							} else {
								// Read step status to get step status
								readGrcStepStatuses(numSteps);
								
								actionsetkey = null;
							}
						} else if (grcStatus == GrcExecStatus.Aborted.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Aborted.name();
						} else if (grcStatus == GrcExecStatus.Suspended.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Suspended.name();
						} else if (grcStatus == GrcExecStatus.Resumed.getValue()) {
							actionsetkey = actionsetkey + GrcExecStatus.Resumed.name();
						}
						
						if (actionsetkey != null) {
							logger.debug(className, function, "send actionsetkey [{}]", actionsetkey);
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}
						
						logger.debug(className, function, "readResult curStatus=[{}]", curStatusStr);
					}
				}
			}

		});
		
		logger.end(className, function);
	}
	
//	private void readGrcStepStatus(int step) {
//		final String function = "readGrcStepStatus";
//		
//		logger.begin(className, function);
//		
//		String clientKey = function + "_" + scsenvid + "_" + dbalias;
//		String [] dbaddresses = new String [1];
//		
//		dbaddresses[0] = "<alias>" + dbalias + ".brctable(" + Integer.toString(step) + ",4)";
//		// Use multiRead to read GRC step status
//		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {
//
//			@Override
//			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
//				final String function = "readGrcStepStatus setReadResult";
//
//				if (errorCode != 0) {
//					logger.debug(className, function, "readResult errorCode=[{}]");
//					sendDisplayMessageEvent(errorMessage);
//				} else {
//					if (values != null) {
//						String stepStatusStr = values[0];
//						
//						logger.debug(className, function, "stepStatus=[{}]", stepStatusStr);
//											
//						stepStatus = Integer.parseInt(stepStatusStr);
//						String actionsetkey = "GrcStatus_";
//						if (grcStatus == GrcExecStatus.Stopped.getValue()) {
//							if (autoManu == GrcExecMode.StopOnFailed.getValue() && stepStatus == CtlBrcStatus.Failed.getValue() ) {
//								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name() + "_Manu_" + CtlBrcStatus.Failed.name();
//							} else {
//								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name();
//							}
//						}
//						logger.debug(className, function, "send actionsetkey [{}]", actionsetkey);
//						uiEventActionProcessor_i.executeActionSet(actionsetkey);					
//					}
//				}
//			}
//
//		});
//		
//		logger.end(className, function);
//	}
	
	private void readGrcStepStatuses(int step) {
		final String function = "readGrcStepStatuses";
		
		logger.begin(className, function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		String [] dbaddresses = new String [1];

		dbaddresses[0] = "<alias>" + dbalias + ".brctable(1:" + Integer.toString(step) + ",4)";
		
		// Use multiRead to read GRC step status
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readGrcStepStatus setReadResult";

				if (errorCode != 0) {
					logger.debug(className, function, "readResult errorCode=[{}]");
					sendDisplayMessageEvent(errorMessage);
				} else {
					if (values != null) {					
						logger.debug(className, function, "step Statuses=[{}]", values[0]);
						
						String removedBracketStr = values[0].substring(1, values[0].length()-1);
						String [] statuses = removedBracketStr.replaceAll("\"", "").split(",");
											
						for (String status: statuses) {
							if (status.equals("1")) {
								notExecutedSteps++;
							} else if (status.equals("2")) {
								completedSteps++;
							} else if (status.equals("3")) {
								failedSteps++;
							} else if (status.equals("4")) {
								skippedSteps++;
							} 
						}
						
						logger.debug(className, function, "notExecutedSteps[{}]", Integer.toString(notExecutedSteps));
						logger.debug(className, function, "completedSteps[{}]", Integer.toString(completedSteps));
						logger.debug(className, function, "failedSteps[{}]", Integer.toString(failedSteps));
						logger.debug(className, function, "skippedSteps[{}]", Integer.toString(skippedSteps));
						logger.debug(className, function, "lastExecutedStep[{}]", Integer.toString(lastExecutedStep));
						logger.debug(className, function, "numSteps[{}]", Integer.toString(numSteps));
						
						// Send display message event
						Date d = new Date();
						String msg = d.toString() + ": SOC [" + grcName + "] ";

						if (failedSteps > 0) {
							if (lastExecutedStep < numSteps-1) {
								msg = msg + "incomplete with " + Integer.toString(failedSteps) + " failed step(s)";
							} else {
								msg = msg + "complete with " + Integer.toString(failedSteps) + " failed step(s)";
							}
						} else {
							if (lastExecutedStep < numSteps-1) {
								msg = msg + "incomplete without any failed step";
							} else {
								msg = msg + "complete without any failed step";
							}
						}

						if (lastExecutedStep > 0) {
							sendDisplayMessageEvent(msg);
						}

						String actionsetkey = "GrcStatus_";
						if (grcStatus == GrcExecStatus.Stopped.getValue()) {
							logger.debug(className, function, "GrcStatus is STOPPED");

							if (autoManu == GrcExecMode.StopOnFailed.getValue() && lastExecutedStep > 0 && failedSteps > 0 ) {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name() + "_Manu_" + CtlBrcStatus.Failed.name();
							} else {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name();
							}
							
							logger.debug(className, function, "send actionsetkey [{}]", actionsetkey);
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}							
					}
				}
			}

		});
		
		logger.end(className, function);
	}
	
	public void reserveEquipment(int [] steps) {
		final String function = "reserveEquipment";
		
		logger.begin(className, function);
		final String [] reserveAliases = getReserveAlias();
		
		String clientKey = "readReserve1" + "_" + scsenvid + "_" + dbalias;

		// First check if any equipment reserved by other 
		readReserve(clientKey, reserveAliases, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readReserve1 setReadResult";
				logger.begin(className, function);
				
				logger.debug(className, function, "clientKey [{}]  values length [{}]", key, values.length);
				boolean hasFailed = false;
				String [] reserveAliases2 = new String [reserveAliases.length];
				int reserveCnt = 0;
				
				for (int i=0; i<values.length; i++) {
					logger.debug(className, function, "values[{}]=[{}]", i, values[i]);
					String unquotedStr = "";
					
					if (values[i] != null) {
						if (values[i].equals("null")) {
							unquotedStr = "";
						} else {
							unquotedStr = values[i].replaceAll("\"", "");
						}
					}
					logger.debug(className, function, "unquotedStr [{}]", unquotedStr);
					
					// Check if reserve is set
					if (isReserveSet(unquotedStr)) {
						if (reserveAttributeType.equalsIgnoreCase("String") && reservedValueStr.equalsIgnoreCase("Operator")) {
							if (checkOperatorRight(unquotedStr)) {
								continue;
							} else {
								// Equipment is reserved by other. return failed
								hasFailed = true;
								break;
							}
						} else {
							// Equipment is reserved by other. return failed
							hasFailed = true;
							break;
						}
					} else {
						// Reserve not set, save the alias for set reserve
						logger.debug(className, function, "reserveAliases2 reserveCnt[{}]= reserveAliases[{}]", reserveCnt, reserveAliases[i]);
						reserveAliases2[reserveCnt] = reserveAliases[i];
						reserveCnt++;				
					}
				}
				
				if (hasFailed) {
					// Found equipment reserved by other
					String errorMsg = "Reserve equipment failed. Unable to launch GRC.";
					sendDisplayMessageEvent(errorMsg);
					
					logger.warn(className, function, errorMsg);			
					
				} else {
					// No equipment reserved by other
					// Check any equipment need to set reserve
					if (reserveCnt > 0) {
						// Set equipment reserve
						setReserve(reserveAliases2);
						
						String clientKey = "readReserve2" + "_" + scsenvid + "_" + dbalias;
						
						// Check reserve is set successfully
						readReserve(clientKey, reserveAliases2, new MultiReadResult() {
	
							@Override
							public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
								final String function = "readReserve2 setReadResult";
								logger.begin(className, function);
								
								logger.debug(className, function, "clientKey [{}] check reserve are set", key);
								boolean hasFailed = false;
								for (int j=0; j<values.length; j++) {
									String unquotedStr = "";
									
									if (values[j] != null) {
										if (values[j].equals("null")) {
											unquotedStr = "";
										} else {
											unquotedStr = values[j].replaceAll("\"", "");
										}
									}
									// Check if reserve is set
									if (isReserveSet(unquotedStr)) {
										if (reserveAttributeType.equalsIgnoreCase("String") && reservedValueStr.equalsIgnoreCase("Operator")) {
											if (checkOperatorRight(unquotedStr)) {
												continue;
											} else {
												// equipment reserved by other
												hasFailed = true;
												break;
											}
										}
									} else {
										// Unable to set equipment reserve
										hasFailed = true;
										break;
									}
								}
								
								if (hasFailed) {
									// Unable to set equipment reserve
									String errorMsg = "Reserve equipment failed. Unable to launch GRC.";
									sendDisplayMessageEvent(errorMsg);
									
									logger.warn(className, function, errorMsg);
									
								} else {
									// All equipment are now reserved
									preparegrc();
								}
								
								logger.end(className, function);
							}			
						});
					} else {
						// All equipment already reserved
						preparegrc();
					}
				}
				
				logger.end(className, function);
			}		
		});
		
		logger.end(className, function);
	}
	
	public String [] getReserveAlias() {
		final String function = "getReserveAlias";
		logger.begin(className, function);
		
		String [] aliases = null;
		
		if (eqpList != null && eqpList.length > 0) {
			
			int aliasCnt = 0;
			for (String eqp: eqpList) {
				if (eqp != null && !eqp.isEmpty()) {
					aliasCnt++;
				}
			}
			logger.debug(className, function, "eqpList length[{}]  non empty alias count[{}]", eqpList.length, aliasCnt);
			
			aliases = new String [aliasCnt];
			int index = 0;
			for (String eqp: eqpList) {
				if (eqp != null && !eqp.isEmpty()) {
					aliases[index] = eqp + "." + reserveAttributeName;
					logger.debug(className, function, "alias[{}] = [{}]", index, aliases[index]);
					index++;
				}
			}
		}
		
		logger.end(className, function);
		return aliases;
	}
	
	protected void readReserve(String key, String [] reserveAliases, MultiReadResult multiReadResult) {
		final String function = "readReserve";
		
		logger.begin(className, function);
		
		rtdb.multiReadValue(key, scsenvid, reserveAliases, multiReadResult);
		
		logger.end(className, function);
	}
	
	public boolean isReserveSet(String reserveResult) {
		final String function = "isReserveSet";
		
		logger.begin(className, function);
		logger.debug(className, function, "reserveResult[{}]", reserveResult);
		
		boolean ret = false;
		
		if (reserveResult == null) {
			logger.debug(className, function, "reserveResult is null return false");
			ret = false;
		} else {
			if (reserveResult.isEmpty()) {		
				logger.debug(className, function, "reserveResult is empty return false");
				ret = false;
			} else {
				logger.debug(className, function, "reserveResult is not empty");
				if (reserveAttributeType.equalsIgnoreCase("Integer") ||
					(reserveAttributeType.equalsIgnoreCase("String") && !reservedValueStr.equalsIgnoreCase("Operator"))) {
					if (reserveResult.equals(reservedValueStr)) {
						logger.debug(className, function, "reserveResult equals to reservedValueStr[{}]. return true", reservedValueStr);
						ret = true;
					} else {
						logger.debug(className, function, "reserveResult not equals to reservedValueStr[{}]. return false", reservedValueStr);
						ret = false;
					}
				} else if (reserveAttributeType.equalsIgnoreCase("String") && reservedValueStr.equalsIgnoreCase("Operator")) {
					logger.debug(className, function, "reservedValueStr using operator to check right. return true");
					ret = true;
				}			
			}
		}
		
		logger.end(className, function);
		return ret;
	}
	
	public void setReserve(String [] reserveAliases) {
		final String function = "setReserve";
		
		logger.begin(className, function);

		String operator = UIOpmSCADAgen.getInstance().getOperator();
		
		for (String alias: reserveAliases) {
			logger.debug(className, function, "check alias[{}]", alias);
			
			if (alias != null && !alias.isEmpty()) {
				String clientKey = function + "_" + scsenvid + "_" + alias;
				
				logger.debug(className, function, "setReserve for alias[{}]", alias);
				
				if (reserveAttributeType.equalsIgnoreCase("String")) {
					if (reservedValueStr.equalsIgnoreCase("Operator")) {
						rtdb.writeStringValue(clientKey, scsenvid, alias, operator);
					} else {
						rtdb.writeStringValue(clientKey, scsenvid, alias, reservedValueStr);
					}
				} else if (reserveAttributeType.equalsIgnoreCase("Integer")) {
					rtdb.writeIntValue(clientKey, scsenvid, alias, Integer.parseInt(reservedValueStr));
				}
			}
		}
		
		logger.end(className, function);
	}
	
	public void unsetReserve(String [] reserveAliases) {
		final String function = "unsetReserve";
		
		logger.begin(className, function);
		
		for (String alias: reserveAliases) {
			logger.debug(className, function, "reserveAlias alias[{}]", alias);
			
			if (alias != null && !alias.isEmpty()) {
				String clientKey = function + "_" + scsenvid + "_" + alias;
				
				logger.debug(className, function, "unsetReserve for alias[{}]", alias);
		
				if (reserveAttributeType.equalsIgnoreCase("String")) {
					rtdb.writeStringValue(clientKey, scsenvid, alias, unreservedValueStr);
				} else if (reserveAttributeType.equalsIgnoreCase("Integer")) {
					rtdb.writeIntValue(clientKey, scsenvid, alias, Integer.parseInt(unreservedValueStr));
				}
			}
		}
		
		logger.end(className, function);
	}
	
	public boolean checkOperatorRight(String reserveResult) {
		final String function = "checkReserveRight";
		
		logger.begin(className, function);
		
		boolean ret = false;

		// Operator is keyword for using runtime login operator name for reserve value		
		String operator = UIOpmSCADAgen.getInstance().getOperator();
		logger.debug(className, function, "compare reserveResult[{}] with operator[{}]", reserveResult, operator);
		if (reserveResult.equals(operator)) {				
			ret = true;
		}


		logger.debug(className, function, "return [{}]", Boolean.toString(ret));
		logger.end(className, function);
		return ret;
	}
	
	protected void preparegrc() {
		final String function="preparegrc";
		
		logger.begin(className, function);
		
		setPrepareGrcResultCallback();
		
		grcMgr.prepareGrc(strKeyPrepareGrc, scsenvid, dbalias);
		
		logger.end(className, function);
	}
	
	protected void launchgrc() {
		final String function="launchgrc";
		
		logger.begin(className, function);
		
		int [] iSkips = getSkips();
		
		setLaunchGrcResultCallback();
		
		setGrcStatusResultCallback();
		
		setStepResultCallback();
		
		// Reset step counts
		notExecutedSteps = 0;
		completedSteps = 0;
		failedSteps = 0;
		skippedSteps = 0;
		lastExecutedStep = 0;

		grcMgr.launchGrc(strKeyLaunchGrc, scsenvid, dbalias, (short)autoManu, firstStep, iSkips);
		
		logger.end(className, function);
	}
}
