package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Timer;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin_i.StorageAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetSimultaneousLoginControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSimultaneousLoginControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private String columnNameGwsIdentity	= "gdg_column_gws_identity";

	private String columnNameArea			= "gdg_column_area";
	private String columnNameServiceOwnerID	= "gdg_column_serviceownerid";
	private String columnNameAlias			= "gdg_column_alias";
	
	private String columnNameResrReservedID	= "gdg_column_resrvreservedid";

	private int intervalPhaseA	= 500;
	private int intervalPhaseB	= 500;
	private int intervalPhaseC	= 10000;
	private int intervalPhaseD	= 5000;
	
	private int timeoutPhaseB	= 20;
	
	private void loginRequest() {
		final String function = "loginRequest";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.strSimultaneousLoginRequest);
		
		logger.end(className, function);
	}

//	private void logoutRequest() {
//		final String function = "logoutRequest";
//		logger.begin(className, function);
//		
//		exit(UIWidgetSimultaneousLoginControl_i.strSimultaneousLogoutRequest);
//		
//		logger.end(className, function);
//	}
	
	private boolean isByPassUsrIdentity() {
		final String function = "isByPassUsrIdentity";
		logger.begin(className, function);
		boolean ret = false;
		
		int bitPos = SimultaneousLogin.getInstance().validateCondition();
		String bitPosString = SimultaneousLogin.getInstance().getBitPosString(bitPos);
		logger.debug(className, function, "bitPos[{}] bitPosString[{}]", bitPos, bitPosString);
		if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_IsByPassUsrIdentity) ) {
			
			// Login Valid, forward to Main
			
			ret = true;
			
			logger.debug(className, function, "Login Valid By Pass, forword to Main");
			
			exit(UIWidgetSimultaneousLoginControl_i.loginValidByPassProcedure);
		}
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}
	
	private boolean isReservedByOther() {
		final String function = "isReservedByOther";
		logger.begin(className, function);
		boolean ret = false;
		
		int bitPos = SimultaneousLogin.getInstance().validateCondition();
		String bitPosString = SimultaneousLogin.getInstance().getBitPosString(bitPos);
		logger.debug(className, function, "bitPos[{}] bitPosString[{}]", bitPos, bitPosString);
		if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_ReservedByOther) ) {
			
			// Login Invalid, forward to Logout
			
			ret = true;
			
			logger.debug(className, function, "Login Invalid, forword to Logout");

			exit(UIWidgetSimultaneousLoginControl_i.loginInvalidReservedByOtherProcedure);
		}
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}
	
	private boolean isReservedBySelf() {
		final String function = "isReservedBySelf";
		logger.begin(className, function);
		boolean ret = false;
		
		int bitPos = SimultaneousLogin.getInstance().validateCondition();
		String bitPosString = SimultaneousLogin.getInstance().getBitPosString(bitPos);
		logger.debug(className, function, "bitPos[{}] bitPosString[{}]", bitPos, bitPosString);
		if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_ReservedSelfGws) ) {
			
			// Login Invalid, forward to Logout
			
			ret = true;
			
			logger.debug(className, function, "Login Invalid, forword to Logout");

			exit(UIWidgetSimultaneousLoginControl_i.loginValidProcedure);
		}
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}	
	
	private boolean isReservedInOtherArea() {
		final String function = "isReservedInOtherArea";
		logger.begin(className, function);
		boolean ret = false;
		
		int bitPos = SimultaneousLogin.getInstance().validateCondition();
		String bitPosString = SimultaneousLogin.getInstance().getBitPosString(bitPos);
		logger.debug(className, function, "bitPos[{}] bitPosString[{}]", bitPos, bitPosString);
		if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_ReservedInOtherArea) ) {
			
			// Login Invalid, forward to Logout
			
			ret = true;
			
			logger.debug(className, function, "Login Invalid, forword to Logout");

			exit(UIWidgetSimultaneousLoginControl_i.loginInvalidThresHoldReachProcedure);
		}
		logger.debug(className, function, "ret[{}]", ret);
		logger.end(className, function);
		return ret;
	}
	
	private void validateCondition() {
		final String function = "validateCondition";
		logger.begin(className, function);

		int bitPos = SimultaneousLogin.getInstance().validateCondition();
		String bitPosString = SimultaneousLogin.getInstance().getBitPosString(bitPos);
		
		logger.debug(className, function, "bitPos[{}] bitPosString[{}]", bitPos, bitPosString);
		
		if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_IsByPassUsrIdentity) ) {
			
			// Login Valid By Pass, forward to Main
			
			logger.debug(className, function, "Login Valid By Pass, forword to Main");
			
			exit(UIWidgetSimultaneousLoginControl_i.loginValidByPassProcedure);
		}
		else if (
				0 != (bitPos & SimultaneousLogin_i.Bit_Pos_SelfGwsIdentity_IsInvalid)
				|| 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_SelfGwsArea_IsInvalid)
				|| 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_SelfUsrIdentity_IsInvalid)
				|| 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_Storage_IsEmpty)
			) {
			
			// Login Invalid, forward to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");
			
			exit(UIWidgetSimultaneousLoginControl_i.loginInvalidSelfIdentityProcedure);
		}
		else if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_ReservedInOtherArea) ) {
			
			// Login Invalid, forward to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");

			exit(UIWidgetSimultaneousLoginControl_i.loginInvalidThresHoldReachProcedure);
		}
		else if ( 0 != (bitPos & SimultaneousLogin_i.Bit_Pos_ReservedSelfGws) ) {

			// Login Valid, forward to Main
			
			logger.debug(className, function, "Login Valid, forword to Main");
			
			exit(UIWidgetSimultaneousLoginControl_i.loginValidProcedure);

		}
		logger.end(className, function);
	}
	
	private void exit(String actionsetkey) {
		final String function = "exit";
		logger.begin(className, function);
		logger.debug(className, function, "actionsetkey[{}]", actionsetkey);
		cancelTimers();
		uiEventActionProcessor_i.executeActionSet(actionsetkey);
		logger.end(className, function);
	}

	private Timer t1 = null;
	private Timer t2 = null;
	private Timer t3 = null;
	private Timer t4 = null;
	
	private void cancelTimers() {
		final String function = "cancelTimer";
		logger.begin(className, function);
		if ( null != t1 ) {
			logger.debug(className, function, "cancel and release t1");
			t1.cancel();
			t1 = null;
		}
		if ( null != t2 ) {
			logger.debug(className, function, "cancel and release t2");
			t2.cancel();
			t2 = null;
		}
		if ( null != t3 ) {
			logger.debug(className, function, "cancel and release t3");
			t3.cancel();
			t3 = null;
		}
		if ( null != t4 ) {
			logger.debug(className, function, "cancel and release t4");
			t4.cancel();
			t4 = null;
		}
		logger.end(className, function);
	}

	private int phaseBCounter = 0;
	private boolean isDuplicatePassed = false;
	private void login() {
		final String function = "login";
		logger.begin(className, function);
		
		// Receive the parameter from configuration file
		SimultaneousLogin.getInstance().loadConfig();
		
		// Receive the Self Identity from Server Side
		logger.debug(className, function, "Phase A intervalPhaseA[{}] start...", intervalPhaseA);
		t1 = new Timer() {
			public void run() {
				logger.debug(className, function, "Phase A running...");
				if ( SimultaneousLogin.getInstance().isSelfIdentityReady() 
					&& SimultaneousLogin.getInstance().isUsrIdentityReady() 
					) {
					logger.debug(className, function, "Phase A stop...");
					t1.cancel();
					
					if ( ! isByPassUsrIdentity() ) {
						
						// Receive Self Wkst from GDG
						logger.debug(className, function, "Phase B intervalPhaseB[{}] start...", intervalPhaseB);
						t2 = new Timer() {
							public void run() {
								logger.debug(className, function, "Phase B running...");
								
								// Receive SelfWkst Timeout
								logger.debug(className, function, "Phase B phaseBCounter[{}] > timeoutPhaseB[{}]", phaseBCounter, timeoutPhaseB);
								if ( phaseBCounter >= timeoutPhaseB ) {
									logger.debug(className, function, "Phase B phaseBCounter[{}] > timeoutPhaseB[{}] Timeout reach", phaseBCounter, timeoutPhaseB);
									t2.cancel();
									exit(UIWidgetSimultaneousLoginControl_i.loginInvalidSelfIdentityProcedure);
								}
								phaseBCounter++;
								
								if ( SimultaneousLogin.getInstance().getWkstInfo() ) {
									logger.debug(className, function, "Phase B stop...");
									t2.cancel();
									
									// Receive Duplicate Area
									logger.debug(className, function, "Phase B intervalPhaseC[{}] start...", intervalPhaseC);
									t3 = new Timer() {
										public void run() {
											logger.debug(className, function, "Phase B running...");

											if ( ! isReservedByOther() ) {
												if ( ! isReservedInOtherArea() ) {
													if ( ! isReservedBySelf() ) {
														isDuplicatePassed = true;
														loginRequest();
														
														// Receive Duplicate Area
														logger.debug(className, function, "Phase B intervalPhaseD[{}] start...", intervalPhaseD);
														t4 = new Timer() {
															public void run() {
																logger.debug(className, function, "Phase B running...");
																
																// Reserve Timeout
																exit(UIWidgetSimultaneousLoginControl_i.loginInvalidReserveTimeoutProcedure);
															} // t4 run
														};
														t4.schedule(intervalPhaseD);
													} // Reserved By Self
												} // Reserved In Other Area
											} // Reserved By Other
										}// t3 run
									};
									t3.schedule(intervalPhaseC);
								} // if getWkstInfo
							} // t2 run
						};
						t2.scheduleRepeating(intervalPhaseB);
					} // By pass user 
				} // IF isSelfIdentityReady && isUsrIdentityReady
			} // t1 run
		};
		t1.scheduleRepeating(intervalPhaseA);

		logger.end(className, function);
	}
	
//	private void elementAction(String element) {
//		final String function = "elementAction";
//		logger.begin(className, function);
//		
//		logger.debug(className, function, "element[{}]", element);
//		
//		if ( UIWidgetSimultaneousLoginControl_i.strLoginRequest.equals(element) ) {
//			
//			loginRequest();
//		}
//		else if ( UIWidgetSimultaneousLoginControl_i.strLogoutRequest.equals(element) ) {							
//			
//			logoutRequest();
//		}
//		else if ( UIWidgetSimultaneousLoginControl_i.strValidateLogin.equals(element) ) {
//
//			validateLogin(true);
//		}
//		
//		logger.end(className, function);
//	}
	
	private void storeData(Set<HashMap<String, String>> rowUpdated) {
		final String function = "storeData";
		logger.begin(className, function);
		
		logger.debug(className, function, "rowUpdated.size()[{}]", rowUpdated.size());
		
		for ( HashMap<String, String> entities : rowUpdated ) {
			if ( null != entities ) {

				String area = entities.get(columnNameArea);
				String scsEnvId = entities.get(columnNameServiceOwnerID);
				String alias = entities.get(columnNameAlias);
				String resrReservedID = entities.get(columnNameResrReservedID);
				
				Map<String, String> entity = new HashMap<String, String>();
				
				entity.put(StorageAttribute.Area.toString()				, area);
				entity.put(StorageAttribute.ScsEnvId.toString()			, scsEnvId);
				entity.put(StorageAttribute.Alias.toString()			, alias);
				entity.put(StorageAttribute.ResrReservedID.toString()	, resrReservedID);
				
				String gwsIdentity = entities.get(columnNameGwsIdentity);
				SimultaneousLogin.getInstance().setStorage(gwsIdentity, entity);
				
				logger.debug(className, function, "gwsIdentity[{}] Area[{}] ScsEnvId[{}] Alias[{}] ResrReservedID[{}]"
						, new Object[]{
								gwsIdentity
								, entity.get(StorageAttribute.Area.toString())
								, entity.get(StorageAttribute.ScsEnvId.toString())
								, entity.get(StorageAttribute.Alias.toString())
								, entity.get(StorageAttribute.ResrReservedID.toString())
				});
			} else {
				logger.warn(className, function, "entities IS NULL");
			}
		}

		logger.debug(className, function, "isDuplicatePassed[{}]", isDuplicatePassed);
		if ( isDuplicatePassed ) validateCondition();
		
		logger.end(className, function);
	}
	
	private int convertIntValue(String key, int defaultValue) {
		final String function = "convertIntValue";
		logger.begin(className, function);
		int ret = 0;
		if ( null != key && ! key.isEmpty() ) {
			try {
				ret = Integer.parseInt(key);
				if ( ret < 0 ) ret = defaultValue;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "key[{}] NumberFormatException:"+ex.toString(), key);
			}
		} else {
			logger.warn(className, function, "key[{}] IS INVALID", key);
		}
		logger.debug(className, function, "key[{}] ret[{}]", key, ret);
		logger.end(className, function);
		return ret;
	}
	
	private void loadParameter() {		
		final String function = "init";
		logger.begin(className, function);
	
		String strIntervalPhaseA 		= null;
		String strIntervalPhaseB 		= null;
		String strIntervalPhaseC 		= null;
		String strIntervalPhaseD 		= null;
		
		String strTimeoutPhaseB 		= null;

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			
			columnNameArea				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameArea.toString(), strHeader);
			columnNameServiceOwnerID	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameServiceOwnerID.toString(), strHeader);
			columnNameAlias				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameAlias.toString(), strHeader);
			columnNameGwsIdentity		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameGwsIdentity.toString(), strHeader);
			columnNameResrReservedID	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameResrReservedID.toString(), strHeader);

			strIntervalPhaseA			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.IntervalPhaseA.toString(), strHeader);
			strIntervalPhaseB			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.IntervalPhaseB.toString(), strHeader);
			strIntervalPhaseC			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.IntervalPhaseC.toString(), strHeader);
			strIntervalPhaseD			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.IntervalPhaseD.toString(), strHeader);
			
			strTimeoutPhaseB			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.TimeoutPhaseB.toString(), strHeader);

			logger.debug(className, function, "columnNameArea[{}]", columnNameArea);
			logger.debug(className, function, "columnNameServiceOwnerID[{}]", columnNameServiceOwnerID);
			logger.debug(className, function, "columnNameAlias[{}]", columnNameAlias);
			logger.debug(className, function, "columnNameGwsIdentity[{}]", columnNameGwsIdentity);
			logger.debug(className, function, "columnNameResrReservedID[{}]", columnNameResrReservedID);
				
			logger.debug(className, function, "strIntervalPhaseA[{}]", strIntervalPhaseA);
			logger.debug(className, function, "strIntervalPhaseB[{}]", strIntervalPhaseB);
			logger.debug(className, function, "strIntervalPhaseC[{}]", strIntervalPhaseC);
			logger.debug(className, function, "strIntervalPhaseD[{}]", strIntervalPhaseD);
			
			logger.debug(className, function, "strTimeoutPhaseB[{}]", strTimeoutPhaseB);
		}
		
		intervalPhaseA = convertIntValue(strIntervalPhaseA, intervalPhaseA);
		intervalPhaseB = convertIntValue(strIntervalPhaseB, intervalPhaseB);
		intervalPhaseC = convertIntValue(strIntervalPhaseC, intervalPhaseC);
		intervalPhaseD = convertIntValue(strIntervalPhaseD, intervalPhaseD);
		
		timeoutPhaseB = convertIntValue(strTimeoutPhaseB, timeoutPhaseB);
		
		logger.end(className, function);
	}
	
	@SuppressWarnings("unchecked")
	public void actionReceived(UIEventAction uiEventAction) {
		final String function = "actionReceived";
		logger.begin(className, function);
		String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
		
		logger.debug(className, function, "os1["+os1+"]");
		
		if ( null != os1 ) {
			// Filter Action
			if ( os1.equals(UIWidgetSimultaneousLoginControl_i.SimultaneousLoginEvent.RowUpdated.toString() ) ) {
				// Activate Selection
				
				Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
				
				logger.debug(className, function, "Store Selected Row");
				
				if ( null != obj1 ) {
					
					storeData((Set<HashMap<String, String>>) obj1);

				} else {
					logger.warn(className, function, "obj1 IS NULL");
				}

			}
			else {
				// General Case
				String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
				
				logger.debug(className, function, "oe ["+oe+"]");
				logger.debug(className, function, "os1["+os1+"]");
				
				if ( null != oe ) {
					if ( oe.equals(element) ) {
						exit(os1);
					}
				}
			}
		}
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		loadParameter();
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String function = "onClick";
				logger.begin(className, function);
				
//				if ( null != event ) {
//					Widget widget = (Widget) event.getSource();
//					if ( null != widget ) {
//						String element = uiGeneric.getWidgetElement(widget);
//						logger.debug(className, function, "element[{}]", element);
//						if ( null != element ) {
//							
//							elementAction(element);
//
//						}
//					}
//				}
				logger.end(className, function);
			}
			
			@Override
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				logger.begin(className, function);
				
				actionReceived(uiEventAction);
				
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

				logger.end(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);

				envDown(null);
				
				logger.end(className, function);
			};
		};
		
		// Preform the Login
		login();

		logger.end(className, function);
	}
}
