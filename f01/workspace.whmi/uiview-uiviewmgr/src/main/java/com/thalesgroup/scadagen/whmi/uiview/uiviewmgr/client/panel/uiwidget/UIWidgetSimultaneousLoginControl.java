package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.SimultaneousLogin;
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

	private int dataDelayTime		= 10000;
	private int writingDelayTime	= 10000;
	private int checkingDelayTime	= 10000;
	
	private void loginRequest() {
		final String function = "loginRequest";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.strSimultaneousLoginRequest);
		
		logger.end(className, function);
	}
	
	private void logoutRequest() {
		final String function = "logoutRequest";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.strSimultaneousLogoutRequest);
		
		logger.end(className, function);
	}
	
	private void validiteLogin() {
		final String function = "validiteLogin";
		logger.begin(className, function);
		
		int loginIsValid = SimultaneousLogin.getInstance().validiteLogin();
		
		logger.debug(className, function, "loginIsValid[{}]", loginIsValid);
		
		if ( loginIsValid == 1 ) {
			// Login Valid, forword to Main
			
			logger.debug(className, function, "Login Valid, forword to Main");
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginValidProcedure);

		} else if ( loginIsValid == 0 ) {
			// Login Invalid, forword to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginInvalidThresHoldReachProcedure);
		} else if ( loginIsValid == -1 ) {
			// Login Invalid, forword to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginInvalidSelfIdentityProcedure);
		}
		
		logger.end(className, function);
	}

	private Timer t1 = null;
	private Timer t2 = null;
	private Timer t3 = null;
	private void login() {
		final String function = "login";
		logger.begin(className, function);
		
		// WritingDelayTime
		logger.debug(className, function, "Data delay dataDelayTime[{}] start...", dataDelayTime);
		t1 = new Timer() {
			public void run() {

				SimultaneousLogin.getInstance().init();

				logger.debug(className, function, "Writing delay writingDelayTime[{}] start...", writingDelayTime);
				t2 = new Timer() {
					public void run() {
					
						loginRequest();
						
						// CheckingDelayTime
						logger.debug(className, function, "Checking delay checkingDelayTime[{}] start...", checkingDelayTime);

						t3 = new Timer() {
							public void run() {
							
								logger.debug(className, function, "validiteLogin...");
								validiteLogin();
							
							}
						};
						t3.schedule(checkingDelayTime);
					
					}
				};
				t2.schedule(writingDelayTime);
			}
		};
		t1.schedule(dataDelayTime);
		
		logger.end(className, function);
	}
	
	private void elementAction(String element) {
		final String function = "elementAction";
		logger.begin(className, function);
		
		logger.debug(className, function, "element[{}]", element);
		
		if ( UIWidgetSimultaneousLoginControl_i.strLoginRequest.equals(element) ) {
			
			loginRequest();
		}
		else if ( UIWidgetSimultaneousLoginControl_i.strLogoutRequest.equals(element) ) {							
			
			logoutRequest();
		}
		else if ( UIWidgetSimultaneousLoginControl_i.strValidateLogin.equals(element) ) {

			validiteLogin();
		}
		
		logger.end(className, function);
	}
	
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
				
				logger.debug(className, function, "gwsIdentity[{}] area[{}] scsEnvId[{}] alias[{}] resrReservedID[{}]", new Object[]{gwsIdentity, area, scsEnvId, alias, resrReservedID});
				
			} else {
				logger.warn(className, function, "entities IS NULL");
			}
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		String strDataDelayTime 		= null;
		String strWritingDelayTime 		= null;
		String strCheckingDelayTime 	= null;

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			
			columnNameArea				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameArea.toString(), strHeader);
			columnNameServiceOwnerID	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameServiceOwnerID.toString(), strHeader);
			columnNameAlias				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameAlias.toString(), strHeader);
			columnNameGwsIdentity		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameGwsIdentity.toString(), strHeader);
			columnNameResrReservedID	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.ColumnNameResrReservedID.toString(), strHeader);

			strDataDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.DataDelayTime.toString(), strHeader);
			strWritingDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.WritingDelayTime.toString(), strHeader);
			strCheckingDelayTime		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.CheckingDelayTime.toString(), strHeader);
			
			if ( logger.isDebugEnabled() ) {
				
				logger.debug(className, function, "columnNameArea[{}]", columnNameArea);
				logger.debug(className, function, "columnNameServiceOwnerID[{}]", columnNameServiceOwnerID);
				logger.debug(className, function, "columnNameAlias[{}]", columnNameAlias);
				logger.debug(className, function, "columnNameGwsIdentity[{}]", columnNameGwsIdentity);
				logger.debug(className, function, "columnNameResrReservedID[{}]", columnNameResrReservedID);
				
				logger.debug(className, function, "strDataDelayTime[{}]", strDataDelayTime);
				logger.debug(className, function, "strWritingDelayTime[{}]", strWritingDelayTime);
				logger.debug(className, function, "strCheckingDelayTime[{}]", strCheckingDelayTime);
			}
		}
		
		if ( null != strDataDelayTime && ! strDataDelayTime.isEmpty() ) {
			try {
				dataDelayTime = Integer.parseInt(strDataDelayTime);
				if ( dataDelayTime < 0 ) dataDelayTime = 0;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strDataDelayTime[{}] NumberFormatException:"+ex.toString(), strDataDelayTime);
			}
		} else {
			logger.warn(className, function, "strWritingDelayTime[{}] IS INVALID", strWritingDelayTime);
		}
		
		if ( null != strWritingDelayTime && ! strWritingDelayTime.isEmpty() ) {
			try {
				writingDelayTime = Integer.parseInt(strWritingDelayTime);
				if ( writingDelayTime < 0 ) writingDelayTime = 0;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strWritingDelayTime[{}] NumberFormatException:"+ex.toString(), strWritingDelayTime);
			}
		} else {
			logger.warn(className, function, "strWritingDelayTime[{}] IS INVALID", strWritingDelayTime);
		}		
		
		if ( null != strCheckingDelayTime && ! strCheckingDelayTime.isEmpty() ) {
			try {
				checkingDelayTime = Integer.parseInt(strCheckingDelayTime);
				if ( checkingDelayTime < 0 ) checkingDelayTime = 0;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strCheckingDelayTime[{}] NumberFormatException:"+ex.toString(), strCheckingDelayTime);
			}
		} else {
			logger.warn(className, function, "strCheckingDelayTime[{}] IS INVALID", strCheckingDelayTime);
		}
		
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
							
							elementAction(element);

						}
					}
				}
				logger.end(className, function);
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
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
//					else if ( os1.startsWith(UIWidgetSimultaneousControl_i.SimultaneousLoginEvent.PagerValueChanged_EndIndex.toString() ) ) {
//						// Activate Selection
//						
//						String os2 = (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
//						
//						logger.debug(className, function, "os2[{}]", os2);
//						
//						try {
//							endIndex = Integer.parseInt(os2);
//						} catch ( NumberFormatException ex ) {
//							logger.warn(className, function, "os2["+os2+"] ex:"+ex.toString());
//						}
//						
//						logger.debug(className, function, "endIndex[{}]", endIndex);
//
//					}
					else {
						// General Case
						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.debug(className, function, "oe ["+oe+"]");
						logger.debug(className, function, "os1["+os1+"]");
						
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
