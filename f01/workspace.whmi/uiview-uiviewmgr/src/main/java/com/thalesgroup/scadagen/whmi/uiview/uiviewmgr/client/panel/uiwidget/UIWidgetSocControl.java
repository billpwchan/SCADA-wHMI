package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDataGrid_i.DataGridEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocAutoManuControl_i.AutoManuEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.CtlBrcStatus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.MessageTranslationID;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.GrcExecStatus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetSocGrcPoint_i.GrcPointEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.soc.Equipment_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventType;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventActionProcessor_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIEventActionProcessorMgr;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.MultiReadResult;
import com.thalesgroup.scadagen.wrapper.wrapper.client.WrapperScsRTDBAccess;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.ctl.GrcMgr_i.GrcExecMode;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Observer;
import com.thalesgroup.scadagen.wrapper.wrapper.client.observer.Subject;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriorityFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i;

public class UIWidgetSocControl extends UIWidget_i {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
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
	
	private String uiControlPriorityResAPI  = "";
	
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
	
	private String reserveIdentifier = null;
	private String resrvReserveReqID = null;
	private String resrvUnreserveReqID = null;
	private String resrvReservedID = null;
	private String reserveAttributeName = null;
	private String reserveAttributeType = null;
	private String reservedValueStr = null;
	private String unreservedValueStr = null;
	
	private int maxReserveRetry = 1; //default max retry
	
	private String messageDatetimefmt = null;
	
	private UIControlPriority_i uiControlPriority = null;
	
	private int notExecutedSteps = 0;
	private int completedSteps = 0;
	private int failedSteps = 0;
	private int skippedSteps = 0;

	private class ReserveVerifier
	{
		private String clientKey;
		private String [] aliases;
		private int reserveRetryCount;
		
		public ReserveVerifier(String clientKey, String [] aliases, int reserveRetryCount)
		{
			this.clientKey = clientKey;
			this.aliases = aliases;
			this.reserveRetryCount = reserveRetryCount;
		}
		
		public void verifyNow()
		{
			readReserve(clientKey, aliases, createMultiReadResult());
		}
		
		private MultiReadResult createMultiReadResult()
		{
			return new MultiReadResult() {
				
				@Override
				public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
					
					final String function = "ReserveVerifier setReadResult";
					logger.begin(function);
					String reserveKey = getReserveKey();
					logger.debug(function, "current Operator is:[{}]", reserveKey);
					logger.debug(function, "values:[{}]", values);
					logger.debug(function, "clientKey [{}] check reserve are set", key);
					boolean hasFailed = false;
					
					for (int j=0; j<values.length; j++) {
						String unquotedStr = "";
						
						if (values[j] != null) {
							if (values[j].equals("null")) {
								unquotedStr = "";
							} else {
								unquotedStr = values[j].replaceAll("\\\\", "");
								unquotedStr = unquotedStr.replaceAll("\"", "");
							}
						}
						
						// Check whether reservation was set by current operator
						String unquotedReserveKey = reserveKey.replaceAll("\"", "");
						logger.debug(function, "compare operator[{}] with reservedID[{}]", unquotedStr, unquotedReserveKey);
						if (unquotedStr.equals(unquotedReserveKey)) {
							// Current equipment's reservation is set by current operator, check next equipment.
							continue;
						} else {
							// Unable to set equipment reservation
							hasFailed = true;
							break;
						}
					}
					
					if (hasFailed) {
						if(reserveRetryCount > 0)
						{
							reserveRetryCount--;
							logger.warn(function, "readReserve failed try again count={}.",reserveRetryCount);
							Timer readAgainTimer = new Timer() {
								
								@Override
								public void run() {
									readReserve(clientKey, aliases, createMultiReadResult());
								}
							};
							readAgainTimer.schedule(0);
						} else {
							// Unable to set one of the equipment's reservation
							String errorMsg = MessageTranslationID.E_Reserve_fail_unable_launch_grc.toString();
							sendDisplayMessageEvent(errorMsg);
							
							// Sending DisableCheckBoxEvent when SOC cannot launch due to reservation fail
							sendDisableCheckBoxEvent(false);

							logger.warn(function, "readReserve error");
						}
					} else {
						// All equipment are successfully reserved
						reserveRetryCount = 0;
						preparegrc();
					}
					
					logger.end(function);
				}
			};
		}
	}

	private UIWidgetCtrl_i uiWidgetCtrl_i = new UIWidgetCtrl_i() {
		
		@Override
		public void onUIEvent(UIEvent uiEvent) {
			
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String function = "onClick";
			
			logger.begin(function);
			
			if ( null != event ) {
				Widget widget = (Widget) event.getSource();
				if ( null != widget ) {
					String element = uiWidgetGeneric.getWidgetElement(widget);
					logger.info(function, "element[{}]", element);
					if ( null != element ) {
						
						// build scsEnvId
						scsenvid = equipmentSelected.getStringValue(targetDataGridColumnA);
						
						logger.info(function, "scsenvid[{}]", scsenvid);
						
						// build dbalias						
						dbalias = equipmentSelected.getStringValue(targetDataGridColumnA2);
						
						logger.info(function, "dbalias[{}]", dbalias);
						
						// Custom handler for "start", "retry", "skip", "stop"
						if (element.equals(startElement)) {

							firstStep = 1;
							
							if (reserveNeeded()) {
								reserveEquipment(steps);
							} else {
								preparegrc();
							}
							sendDisableCheckBoxEvent(true);
							
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
							
							if (grcStatus != GrcExecStatus.Terminated.getValue() &&
								grcStatus != GrcExecStatus.Aborted.getValue() &&
								grcStatus != GrcExecStatus.Stopped.getValue()) {

								setAbortGrcResultCallback();

								grcMgr.abortGrc(strKeyAbortGrc, scsenvid, dbalias);
							}

							if (autoManu == GrcExecMode.StopOnFailed.getValue() && lastExecutedStep > 0 && failedSteps > 0 ) {
								logger.debug("Sending DisableCheckBox event when SOC is stopped");
								sendDisableCheckBoxEvent(false);

								String actionsetkey = "GrcStatus_" + GrcExecStatus.Stopped.name();
								uiEventActionProcessor_i.executeActionSet(actionsetkey);
							}
						}
					}
				}
			}
			logger.end(function);
		}
		
		@Override
		public void onActionReceived(UIEventAction uiEventAction) {
			final String function = "onActionReceived";
			
			logger.begin(function);
			
			String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
			
			logger.info(function, "os1[{}]", os1);
			
			if ( null != os1 ) {
				if ( os1.equals(AutoManuEvent.RadioBoxSelected.toString() ) ) {
					
					logger.info(function, "Store Selected RadioBox");
					
					String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
					
					if ( null != os2 ) {
						
						logger.info(function, "os2[{}]", os2);
						
						String autoManuStr = os2;
						
						if ( autoManuStr.equals("auto") ) {
							autoManu = GrcExecMode.Auto.getValue();

						} else if ( autoManuStr.equals("manu") ) {
							autoManu = GrcExecMode.StopOnFailed.getValue();

						} else {
							logger.warn(function, "os2[{}] type IS UNKNOW", os2);
						}
					} else {
						logger.warn(function, "os2 IS NULL");
					}
					
				} else if ( os1.equals(DataGridEvent.RowSelected.toString() ) ) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					
					logger.info(function, "Store Selected Row");
					
					if ( null != targetDataGridA ) {
						
						logger.info(function, "targetDataGridA[{}]", targetDataGridA);
						
						if ( null != obj1 ) {
							if ( obj1 instanceof String ) {
								datagridSelected	= (String) obj1;
								
								logger.info(function, "datagridSelected[{}]", datagridSelected);

								if ( datagridSelected.equals(targetDataGridA) ) {
									// When selecting the row of SOCDetailList, the "datagridSelected" will be different from
									// "targetDataGridA". Which means when the if statement is true, it means another row of
									// "SOCCardList" is selected. Then need to clean the skipped steps.
									
									logger.info(function, "current skipped steps: [{}]", skipList);
									skipList = new ArrayList<Integer>();
									logger.info(function, "skipped steps after cleanning: [{}]", skipList);
									
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

											// Read step and eqp from brctable for check reservation later
											readStepEqp();
											
										} else {
											equipmentSelected = null;
											
											logger.warn(function, "obj2 IS NOT TYPE OF Equipment_i");
										}
									} else {
										logger.warn(function, "obj2 IS NULL");
									}
								} 
								
							} else {
								logger.warn(function, "obj1 IS NOT TYPE OF String");
							}
						} else {
							logger.warn(function, "obj1 IS NULL");
						}
					} else {
						logger.warn(function, "targetDataGridA IS NULL");
					}
					
				} else if ( os1.equals(DataGridEvent.ValueChange.toString() )) {
					
					Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
					Object obj2 = uiEventAction.getParameter(ViewAttribute.OperationObject2.toString());
					Object obj3 = uiEventAction.getParameter(ViewAttribute.OperationObject3.toString());
					Object obj4 = uiEventAction.getParameter(ViewAttribute.OperationObject4.toString());
					
					logger.info(function, "DataGrid ValueChange");
	
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
					
					logger.info(function, "oe [{}]", oe);
					logger.info(function, "os1[{}]", os1);
					
					if ( null != oe ) {
						if ( oe.equals(element) ) {
							uiEventActionProcessor_i.executeActionSet(os1);
						}
					}
				}
			}
			logger.end(function);
		}
	};
		
	@Override
	public void init() {
		final String function = "init";
		logger.begin(function);
		
		
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(function, "strEventBusName[{}]", strEventBusName);

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
			
			reserveIdentifier		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveIdentifier.toString(), strHeader);
			resrvReserveReqID		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeReserveReqID.toString(), strHeader);
			resrvUnreserveReqID		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeUnreserveReqID.toString(), strHeader);
			resrvReservedID			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeReservedID.toString(), strHeader);
			
			reserveAttributeName	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeName.toString(), strHeader);
			reserveAttributeType	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReserveAttributeType.toString(), strHeader);
			reservedValueStr		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ReservedValueStr.toString(), strHeader);
			unreservedValueStr		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.UnreservedValueStr.toString(), strHeader);
			
			uiControlPriorityResAPI = dictionariesCache.getStringValue(optsXMLFile, ParameterName.UIControlPriorityResAPI.toString(), strHeader);
			if (uiControlPriorityResAPI == null || uiControlPriorityResAPI.isEmpty()) {
				logger.warn(function, "config uiControlPriorityResAPI is null or emtpry. Use default UIControlPriorityReservation");
				uiControlPriorityResAPI = "UIControlPriorityReservation";
			}
			try
			{
				maxReserveRetry		= Integer.parseInt(dictionariesCache.getStringValue(optsXMLFile, ParameterName.MaxReserveRetry.toString(), strHeader));
				if (maxReserveRetry < 1)
				{
					maxReserveRetry = 1;
				}
			}
			catch(Exception e)
			{
				maxReserveRetry = 1; // default number of retry before consider reservation failure.
			}
			
			messageDatetimefmt		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.MessageDatetimeFormat.toString(), strHeader);
		}

		logger.info(function, "targetDataGridA[{}]", targetDataGridA);
		logger.info(function, "targetDataGridColumnA[{}]", targetDataGridColumnA);
		logger.info(function, "targetDataGridColumnA2[{}]", targetDataGridColumnA2);
		logger.info(function, "targetDataGridColumnA3[{}]", targetDataGridColumnA3);
		logger.info(function, "targetDataGridB[{}]", targetDataGridB);
		logger.info(function, "targetDataGridColumnB[{}]", targetDataGridColumnB);
		logger.info(function, "targetDataGridColumnB2[{}]", targetDataGridColumnB2);
		logger.info(function, "targetDataGridColumnB3[{}]", targetDataGridColumnB3);
		
		logger.info(function, "startElement[{}]", startElement);
		logger.info(function, "stopElement[{}]", stopElement);
		logger.info(function, "retryElement[{}]", retryElement);
		logger.info(function, "skipElement[{}]", skipElement);
		
		logger.info(function, "reserveIdentifier[{}]", reserveIdentifier);
		logger.info(function, "resrvReserveReqID[{}]", resrvReserveReqID);
		logger.info(function, "resrvUnreserveReqID[{}]", resrvUnreserveReqID);
		logger.info(function, "resrvReservedID[{}]", resrvReservedID);
		logger.info(function, "reserveAttributeName[{}]", reserveAttributeName);
		logger.info(function, "reserveAttributeType[{}]", reserveAttributeType);
		logger.info(function, "reservedValueStr[{}]", reservedValueStr);
		logger.info(function, "unreservedValueStr[{}]", unreservedValueStr);
		logger.info(function, "uiControlPriorityResAPI[{}]", uiControlPriorityResAPI);
		
		logger.info(function, "maxReserveRetry[{}]", maxReserveRetry);
		
		logger.info(function, "messageDatetimefmt[{}]", messageDatetimefmt);
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");

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
		
		logger.end(function);
	}
	
	private boolean reserveNeeded() {
		final String function = "readStepAliases";
		
		if (reserveAttributeName == null || reserveAttributeName.isEmpty() ||
			reserveAttributeType == null || reserveAttributeType.isEmpty() ||
			(!reserveAttributeType.equalsIgnoreCase("String") && !reserveAttributeType.equalsIgnoreCase("Integer")) ||
			reservedValueStr == null || unreservedValueStr == null) {
			logger.debug(function, "reserve not setup");
			return false;
		}

		logger.debug(function, "reserve is setup");
		return true;
	}
	
	private void readStepEqp() {
		final String function = "readStepAliases";
		
		logger.begin(function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		
		String [] dbaddresses = new String [2];
		
		dbaddresses[0] = "<alias>" + dbalias + ".brctable(0:$, number)";
		dbaddresses[1] = "<alias>" + dbalias + ".brctable(0:$, eqp)";
		
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				
				if (values == null || values.length != 2) {
					logger.error(function, "return values is null or size not correct");
					return;
				}
				logger.debug(function, "numbers [{}]", values[0]);
				String str1 = values[0].substring(1, values[0].length()-1);
				String str2 = str1.replaceAll("\"", "");
				logger.debug(function, "numbers substring [{}]", str1);
				logger.debug(function, "numbers replaceAll [{}]", str2);
				String [] numbers = str2.split(",");
				
				logger.debug(function, "eqps [{}]", values[1]);
				String str3 = values[1].substring(1, values[1].length()-1);
				String str4 = str3.replaceAll("\"", "");
				logger.debug(function, "eqps substring [{}]", str3);
				logger.debug(function, "eqps replaceAll [{}]", str4);
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
		
		logger.begin(function);
		
		Subject prepareGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "prepareGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(function, errorMsg);
							String msg = MessageTranslationID.E_grc_prepare_result_error.toString();
							sendDisplayMessageEvent(msg, new Object[]{errorMsg});
						}
					} else {
						logger.info(function, "prepareGrc done. launchgrc");
						sendDisplayMessageEvent("");
						launchgrc();
					}
				}
			}
			
		};
		observer.setSubject(prepareGrcSubject);
		
		String callback = "setPrepareGrcResult";
		String key = strKeyPrepareGrc + callback;
		grcMgr.setSubject(key, prepareGrcSubject);
		
		logger.end(function);
	}
	
	private void setLaunchGrcResultCallback() {
		final String function = "setLaunchGrcResultCallback";
		
		logger.begin(function);
		
		Subject launchGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "launchGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(function, errorMsg);
							String msg = MessageTranslationID.E_grc_launch_result_error.toString();
							sendDisplayMessageEvent(msg, new Object[]{errorMsg});
						}
					}
				}
			}
			
		};
		observer.setSubject(launchGrcSubject);
		
		String callback = "setLaunchGrcResult";
		String key = strKeyLaunchGrc + callback;
		grcMgr.setSubject(key, launchGrcSubject);
		
		logger.end(function);
	}
	
	private void setGrcStatusResultCallback() {
		final String function = "setGrcStatusResultCallback";
		
		logger.begin(function);
		
		Subject grcStatusSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "grcStatusSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				int errorCode = 0;
				if (errorCodeObj != null) {
					errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(function, errorMsg);
							String msg = MessageTranslationID.E_grc_result_update_error.toString();
							sendDisplayMessageEvent(msg, new Object[]{errorMsg});
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
									String [] reserveAliases = getReserveAlias(resrvUnreserveReqID);
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
							
							logger.debug(function, "autoManu[{}]", autoManu);
							
							if (prevGrcStatus != grcStatus) {
								if (reserveNeeded()) {
									String [] reserveAliases = getReserveAlias(resrvUnreserveReqID);
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
									String [] reserveAliases = getReserveAlias(resrvUnreserveReqID);
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
							logger.debug(function, "send actionsetkey [{}]", actionsetkey);

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
		
		logger.end(function);
	}
	

	private void setStepResultCallback() {
		final String function = "setStepResultCallback";
		
		logger.begin(function);
		
		Subject stepSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "stepSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				int errorCode = 0;
				if (errorCodeObj != null) {
					errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(function, errorMsg);
							String msg = MessageTranslationID.E_grc_step_result_error.toString();
							sendDisplayMessageEvent(msg, new Object[]{errorMsg});
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
						logger.debug(function, "current step [{}]", curStep);
						logger.debug(function, "last executed step [{}]", lastExecutedStep);
					}
				}
				sendReloadColumnDataEvent(targetDataGridColumnB3);
			}
			
		};
		observer.setSubject(stepSubject);
		
		String callback = "setStepResult";
		String key = strKeyLaunchGrc + callback;
		grcMgr.setSubject(key, stepSubject);
		
		logger.end(function);
	}
	
	private void setAbortGrcResultCallback() {
		final String function = "setAbortGrcCallback";
		
		logger.begin(function);
		
		Subject abortGrcSubject = new Subject();
		Observer observer = new Observer() {

			@Override
			public void setSubject(Subject subject) {
				this.subject = subject;	
				this.subject.attach(this);
			}

			@Override
			public void update() {
				logger.debug(function, "abortGrcSubject update");
				JSONObject obj = this.subject.getState();
				JSONNumber errorCodeObj = obj.get("errorCode").isNumber();
				if (errorCodeObj != null) {
					int errorCode = (int)errorCodeObj.doubleValue();
					
					if (errorCode != 0) {
						JSONString errorMsgObj = obj.get("errorMessage").isString();
						if (errorMsgObj != null) {
							String errorMsg = errorMsgObj.stringValue();
							logger.error(function, errorMsg);
							String msg = MessageTranslationID.E_grc_abort_update_error.toString();
							sendDisplayMessageEvent(msg, new Object[]{errorMsg});
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
		
		logger.end(function);
	}
	
	protected void removeStepToSkip(Integer n) {
		final String function = "removeStepToSkip";
		logger.debug(function, "remove [{}] from skip list", n);
		if (skipList.contains(n)) {
			skipList.remove(n);
			logger.debug(function, "[{}] removed from skip list", n);
		}
	}

	protected void addStepToSkip(Integer n) {
		final String function = "addStepToSkip";
		logger.debug(function, "add [{}] to skip list", n);
		if (!skipList.contains(n)) {
			skipList.add(n);
			logger.debug(function, "[{}] added to skip list", n);
		}
	}
	
	private int getCurStep() {
		final String function = "getCurStep";
		logger.begin(function);
		int result = 1;
		
		if (curStep > 0) {
			result = curStep;
		}
		logger.end(function);
		return result;
	}
	
	private int [] getSkips() {
		final String function = "getSkips";
		logger.begin(function);
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
		
//		if (logger.isDebugEnabled()) {
			String skipStepsStr = "";
			for (int i=0; i<skips.length; i++) {
				skipStepsStr += skips[i] + " ";
			}
			logger.debug(function, "skip steps=[{}]", skipStepsStr);
//		}
		logger.end(function);
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
//			logger.debug(function, "fire UIEventAction reloadDataEvent");
//		}
//	}
	
	private void sendReloadColumnDataEvent(String TargetColumn) {
		final String function = "sendReloadColumnDataEvent";
		UIEventAction reloadDataEvent = new UIEventAction();
		if (reloadDataEvent != null) {
			reloadDataEvent.setParameter(ViewAttribute.OperationString1.toString(), DataGridEvent.ReloadColumnData.toString());
			reloadDataEvent.setParameter(ViewAttribute.OperationString2.toString(), targetDataGridB);
			reloadDataEvent.setParameter(ViewAttribute.OperationString3.toString(), TargetColumn);
			getEventBus().fireEvent(reloadDataEvent);
			logger.debug(function, "fire UIEventAction reloadDataEvent");
		}
	}
	
	private void sendDisableCheckBoxEvent(boolean flag){
		final String function = "sendDisableCheckBoxEvent";
		UIEventAction disableCheckBoxEvent = new UIEventAction();
		if (disableCheckBoxEvent != null){
			disableCheckBoxEvent.setParameter(ViewAttribute.OperationString1.toString(), DataGridEvent.DisableCheckBox.toString());
			disableCheckBoxEvent.setParameter(ViewAttribute.OperationString2.toString(), targetDataGridB);
			disableCheckBoxEvent.setParameter(ViewAttribute.OperationString3.toString(), flag);
			getEventBus().fireEvent(disableCheckBoxEvent);
			logger.debug(function, "fire UIEventAction disableCheckBoxEvent[{}]", flag);
		}
	}

	private void sendDisplayMessageEvent(String msg) {
		sendDisplayMessageEvent(msg, null);
	}
	private void sendDisplayMessageEvent(String msgWithPlaceHolder, Object[] msgParam) {
		final String function = "sendDisplayMessageEvent";
		UIEventAction displayMessageEvent = new UIEventAction();
		if (displayMessageEvent != null) {
			displayMessageEvent.setParameter(ViewAttribute.OperationString1.toString(), GrcPointEvent.DisplayMessage.toString());
			displayMessageEvent.setParameter(ViewAttribute.OperationObject1.toString(), scsenvid);
			displayMessageEvent.setParameter(ViewAttribute.OperationObject2.toString(), dbalias);
			displayMessageEvent.setParameter(ViewAttribute.OperationObject3.toString(), msgWithPlaceHolder);
			displayMessageEvent.setParameter(ViewAttribute.OperationObject4.toString(), msgParam);
			getEventBus().fireEvent(displayMessageEvent);
			logger.debug(function, "fire UIEventAction displayMessageEvent");
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
			logger.debug(function, "fire UIEventAction grcStatusEvent");
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
			logger.debug(function, "fire UIEventAction grcStepEvent");
		}
	}

	
	private void readGrcCurStatus() {
		final String function = "readGrcCurStatus";
		
		logger.begin(function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		String [] dbaddresses = new String [1];
		
		dbaddresses[0] = "<alias>" + dbalias + ".curstatus";
		// Use multiRead to read GRC curstatus
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readGrcCurStatus setReadResult";

				if (errorCode != 0) {
					logger.debug(function, "readResult errorCode=[{}]");
					String msg = MessageTranslationID.E_rtdb_read_error.toString();
					sendDisplayMessageEvent(msg);
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

							logger.debug(function, "autoManu[{}]", autoManu);
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
							logger.debug(function, "send actionsetkey [{}]", actionsetkey);
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						}

						logger.debug(function, "readResult curStatus=[{}]", curStatusStr);
					}
				}
			}

		});
		
		logger.end(function);
	}

	private void readGrcStepStatuses(int step) {
		final String function = "readGrcStepStatuses";
		
		logger.begin(function);
		
		String clientKey = function + "_" + scsenvid + "_" + dbalias;
		String [] dbaddresses = new String [1];

		dbaddresses[0] = "<alias>" + dbalias + ".brctable(1:" + Integer.toString(step) + ",4)";
		
		// Use multiRead to read GRC step status
		rtdb.multiReadValue(clientKey, scsenvid, dbaddresses, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readGrcStepStatus setReadResult";

				if (errorCode != 0) {
					logger.debug(function, "readResult errorCode=[{}]");
					String msg = MessageTranslationID.E_rtdb_read_error.toString();
					sendDisplayMessageEvent(msg);
				} else {
					if (values != null) {					
						logger.debug(function, "step Statuses=[{}]", values[0]);
						
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
						
						logger.debug(function, "notExecutedSteps[{}]", Integer.toString(notExecutedSteps));
						logger.debug(function, "completedSteps[{}]", Integer.toString(completedSteps));
						logger.debug(function, "failedSteps[{}]", Integer.toString(failedSteps));
						logger.debug(function, "skippedSteps[{}]", Integer.toString(skippedSteps));
						logger.debug(function, "lastExecutedStep[{}]", Integer.toString(lastExecutedStep));
						logger.debug(function, "numSteps[{}]", Integer.toString(numSteps));
						
						// Send display message event
						Date d = new Date();
						String msg = "";
						if(!messageDatetimefmt.isEmpty())
						{
							DateTimeFormat fmt = DateTimeFormat.getFormat(messageDatetimefmt);
							String formattedStr = "";
							try
							{
								formattedStr += fmt.format(d);
							}
							catch(Exception e) {
								logger.error(function, e.toString());
								formattedStr += d;
							}
							msg += formattedStr;
						}
						else
						{
							msg += d.toString();
						}
						
						if (failedSteps > 0) {
							if (lastExecutedStep < numSteps-1) {
								msg = msg + MessageTranslationID.E_SOC_incompleted_failed_steps.toString();
							} else {
								msg = msg + MessageTranslationID.E_SOC_completed_failed_steps.toString();
							}
						} else {
							if (lastExecutedStep < numSteps-1) {
								msg = msg + MessageTranslationID.E_SOC_incompleted_no_failed_steps.toString();
							} else {
								msg = msg + MessageTranslationID.E_SOC_completed_no_failed_steps.toString();
							}
						}

						if (lastExecutedStep > 0) {
							sendDisplayMessageEvent(msg, new Object[]{grcName, failedSteps});
						}

						String actionsetkey = "GrcStatus_";
						if (grcStatus == GrcExecStatus.Stopped.getValue()) {
							logger.debug(function, "GrcStatus is STOPPED");

							if (autoManu == GrcExecMode.StopOnFailed.getValue() && lastExecutedStep > 0 && failedSteps > 0 ) {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name() + "_Manu_" + CtlBrcStatus.Failed.name();
							} else {
								actionsetkey = actionsetkey + GrcExecStatus.Stopped.name();

								logger.debug("Sending DisableCheckBox event when SOC ends.");
								sendDisableCheckBoxEvent(false);
							}

							logger.debug(function, "send actionsetkey [{}]", actionsetkey);
							uiEventActionProcessor_i.executeActionSet(actionsetkey);
						} else {
							logger.debug("Sending DisableCheckBox event when SOC ends.");
							sendDisableCheckBoxEvent(false);
						}
						logger.debug(function, "SOC ends with setReadResult-values:[{}]", values);
					}
				}
			}

		});
		
		logger.end(function);
	}
	
	public void reserveEquipment(int [] steps) {
		final String function = "reserveEquipment";
		
		logger.begin(function);
		String [] reservedAliasesProto = getReserveAlias(reserveAttributeName);
		String [] reservedAliases = null;
		// Remove the skipped steps from "reservedAliasesProto":
		int [] skippedSteps = getSkips();
		if (skippedSteps.length == 0 || !skippedSteps.equals(null)) {
			for (int i = 0; i < skippedSteps.length; i++) {
				logger.debug(function, "current i: [{}], skippedSteps-i: [{}]", i, skippedSteps[i]);
				logger.debug(function, "reservedAliasesProto-skippedSteps-i before removal: [{}]", reservedAliasesProto[skippedSteps[i]-1]);
				reservedAliasesProto[skippedSteps[i]-1] = "";
				logger.debug(function, "reservedAliasesProto-skippedSteps-i after removal: [{}]", reservedAliasesProto[skippedSteps[i]-1]);
			}
			// remove the null entries for reservedAliasesProto.
			List<String> tempList = new ArrayList<String>();
			for (int i = 0; i < reservedAliasesProto.length ; i++) {
				logger.debug(function, "reservedAliasesProto[{}] before removeAll: [{}]", i, reservedAliasesProto[i]);
				if (!reservedAliasesProto[i].equals("") ){
					tempList.add(reservedAliasesProto[i]);
				}
			}
			reservedAliases = new String[tempList.size()];
			reservedAliases = tempList.toArray(reservedAliases);
			// reservedIDAliases is the array for reservation reading
			for (int i = 0; i < reservedAliases.length ; i++) {
				logger.debug(function, "reservedAliases[{}] after convertion: [{}]", i, reservedAliases[i]);
			}
		}
		
		String clientKey = "readReserve1" + "_" + scsenvid + "_" + dbalias;

		// First check if any equipment reserved by other 
		readReserve(clientKey, reservedAliases, new MultiReadResult() {

			@Override
			public void setReadResult(String key, String[] values, int errorCode, String errorMessage) {
				final String function = "readReserve1 setReadResult";
				logger.begin(function);
				
				logger.debug(function, "clientKey [{}]  values length [{}]", key, values.length);
				boolean hasFailed = false;
				String [] reservedIDAliasesProto = getReserveAlias(resrvReservedID);
				String [] reserveReqAliasesProto = getReserveAlias(resrvReserveReqID);
				String [] reservedIDAliases = null;
				String [] reserveReqAliases = null;
				// Remove the skipped steps from "reservedIDAliasesProto" and "reserveReqAliasesProto" :
				int [] skippedSteps = getSkips();
				if (skippedSteps.length == 0 || !skippedSteps.equals(null)) {
					for (int i = 0; i < skippedSteps.length; i++) {
						logger.debug(function, "current i: [{}], skippedSteps-i: [{}]", i, skippedSteps[i]);
						logger.debug(function, "reservedIDAliasesProto-skippedSteps-i before removal: [{}]", reservedIDAliasesProto[skippedSteps[i]-1]);
						logger.debug(function, "reserveReqAliasesProto-skippedSteps-i before removal: [{}]", reserveReqAliasesProto[skippedSteps[i]-1]);
						reservedIDAliasesProto[skippedSteps[i]-1] = "";
						reserveReqAliasesProto[skippedSteps[i]-1] = "";
						logger.debug(function, "reservedIDAliasesProto-skippedSteps-i after removal: [{}]", reservedIDAliasesProto[skippedSteps[i]-1]);
						logger.debug(function, "reserveReqAliasesProto-skippedSteps-i after removal: [{}]", reserveReqAliasesProto[skippedSteps[i]-1]);
					}
					
					// remove the null entries for reservedIDAliasesProto.
					List<String> tempList = new ArrayList<String>();
					for (int i = 0; i < reservedIDAliasesProto.length ; i++) {
						logger.debug(function, "reservedIDAliasesProto[{}] before removeAll: [{}]", i, reservedIDAliasesProto[i]);
						if (!reservedIDAliasesProto[i].equals("") ){
							tempList.add(reservedIDAliasesProto[i]);
						}
					}
					reservedIDAliases = new String[tempList.size()];
					reservedIDAliases = tempList.toArray(reservedIDAliases);
					// reservedIDAliases is the array for reservation reading
					for (int i = 0; i < reservedIDAliases.length ; i++) {
						logger.debug(function, "reservedIDAliases[{}] after convertion: [{}]", i, reservedIDAliases[i]);
					}
					
					// remove the null entries for reserveReqAliasesProto.
					List<String> tempList1 = new ArrayList<String>();
					for (int i = 0; i < reserveReqAliasesProto.length ; i++) {
						logger.debug(function, "reserveReqAliasesProto[{}] before removeAll: [{}]", i, reserveReqAliasesProto[i]);
						if (!reserveReqAliasesProto[i].equals("") ){
							tempList1.add(reserveReqAliasesProto[i]);
						}
					}
					reserveReqAliases = new String[tempList1.size()];
					reserveReqAliases = tempList1.toArray(reserveReqAliases);
					// reserveReqAliases is the array for reservation requesting
					for (int i = 0; i < reserveReqAliases.length ; i++) {
						logger.debug(function, "reserveReqAliases[{}] after convertion: [{}]", i, reserveReqAliases[i]);
					}
				}
				
				int reserveCnt = 0;
				
				for (int i=0; i<values.length; i++) {
					logger.debug(function, "values[{}]=[{}]", i, values[i]);
					String unquotedStr = "";
					
					if (values[i] != null) {
						if (values[i].equals("null")) {
							unquotedStr = "";
						} else {
							unquotedStr = values[i].replaceAll("\"", "");
						}
					}
					logger.debug(function, "unquotedStr [{}]", unquotedStr);
					
					// Check if reserve is set
					if ((unquotedStr == "1")) {
						// Equipment is reserved, return failed
						hasFailed = true;
						break;
					} else {
						// Increase the total number of all equipment need to reserve.
						reserveCnt++;
					}
				}
				
				if (hasFailed) {
					// Found any equipment was already reserved
					String errorMsg = MessageTranslationID.E_Reserve_fail_unable_launch_grc.toString();
					sendDisplayMessageEvent(errorMsg);

					// Sending DisableCheckBoxEvent when SOC cannot launch due to reservation fail
					sendDisableCheckBoxEvent(false);
					
					logger.warn(function, errorMsg);
					
				} else {
					// No equipment reserved by other person, check any equipment need to set reservation
					if (reserveCnt > 0) {
						// Set equipment reservation
						setReserve(reserveReqAliases);
						
						String clientKey = "reserveReqAliases" + "_" + scsenvid + "_" + dbalias;
						
						// Check whether reservation is set successfully
						new ReserveVerifier(clientKey, reservedIDAliases, maxReserveRetry).verifyNow();
	
					} else {
						// All equipment already reserved
						preparegrc();
					}
				}
				
				logger.end(function);
			}		
		});
		
		logger.end(function);
	}
	
	public String [] getReserveAlias(String targetAttribute) {
		// "targetAttribute" means the column name of RTDB column. E.g.: "reserved", "resrvReservedID", "resrvReserveReqID"
		final String function = "getReserveAlias";
		logger.begin(function);
		
		String [] aliases = null;
		
		if (eqpList != null && eqpList.length > 0) {
			
			int aliasCnt = 0;
			for (String eqp: eqpList) {
				if (eqp != null && !eqp.isEmpty()) {
					aliasCnt++;
				}
			}
			logger.debug(function, "eqpList length[{}]  non empty alias count[{}]", eqpList.length, aliasCnt);
			
			aliases = new String [aliasCnt];
			int index = 0;
			for (String eqp: eqpList) {
				if (eqp != null && !eqp.isEmpty()) {
					aliases[index] = eqp + "." + targetAttribute;      
					logger.debug(function, "alias[{}] = [{}]", index, aliases[index]);
					index++;
				}
			}
		}
		logger.end(function);
		return aliases;
		// "aliases[]" is used for referring to the target column values in the RTDB 
	}

	protected void readReserve(String key, String [] reserveAliases, MultiReadResult multiReadResult) {
		final String function = "readReserve";
		
		logger.begin(function);
		
		rtdb.multiReadValue(key, scsenvid, reserveAliases, multiReadResult);
		
		logger.end(function);
	}
	
	public void setReserve(String [] reserveReqAliases) {
		final String function = "setReserve";
		logger.begin(function);
		
		String reserveKey = getReserveKey();
		logger.debug(function, "current Operator is:[{}]", reserveKey);
		
		for (String reqAlias: reserveReqAliases) {
			logger.debug(function, "check reqAlias[{}]", reqAlias);
			
			if (reqAlias != null && !reqAlias.isEmpty()) {
				String clientKey = function + "_" + scsenvid + "_" + reqAlias;
				
				logger.debug(function, "setReserve for reqAlias[{}]", reqAlias);
				rtdb.writeStringValue(clientKey, scsenvid, reqAlias, reserveKey);
			}
		}
		
		logger.end(function);
	}
	
	public void unsetReserve(String [] resrvUnreserveReqID) {
		final String function = "unsetReserve";
		logger.begin(function);
		
		String reserveKey = getReserveKey();
		logger.debug(function, "current Operator is:[{}]", reserveKey);
		
		for (String unReqAlias: resrvUnreserveReqID) {
			logger.debug(function, "check unReqAlias[{}]", unReqAlias);
			
			if (unReqAlias != null && !unReqAlias.isEmpty()) {
				String clientKey = function + "_" + scsenvid + "_" + unReqAlias;
				
				logger.debug(function, "unsetReserve for unReqAlias[{}]", unReqAlias);
				rtdb.writeStringValue(clientKey, scsenvid, unReqAlias, reserveKey);
			}
		}
		
		logger.end(function);
	}
	
	protected void preparegrc() {
		final String function="preparegrc";
		
		logger.begin(function);
		
		setPrepareGrcResultCallback();
		
		grcMgr.prepareGrc(strKeyPrepareGrc, scsenvid, dbalias);
		
		logger.end(function);
	}
	
	protected void launchgrc() {
		final String function="launchgrc";
		
		logger.begin(function);
		
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
		
		logger.end(function);
	}
	
	protected String getReserveKey() {
		String resKey = "";

		uiControlPriority = UIControlPriorityFactory.getInstance().get(uiControlPriorityResAPI);
		if (uiControlPriority != null) {
			resKey = uiControlPriority.getReservationKey();
		}
		
		return resKey;
	}
}