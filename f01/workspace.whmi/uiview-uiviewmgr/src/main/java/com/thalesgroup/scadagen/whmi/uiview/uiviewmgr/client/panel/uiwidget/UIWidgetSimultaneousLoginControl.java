package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.control.Gws;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;

public class UIWidgetSimultaneousLoginControl extends UIWidgetRealize {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetSimultaneousLoginControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private Set<HashMap<String, String>> rowStorage = null;
	
	private String columnNameGwsIdentity	= "gdg_column_gws_identity";

	private String columnNameArea			= "gdg_column_area";
	private String columnNameServiceOwnerID	= "gdg_column_serviceownerid";
	private String columnNameAlias			= "gdg_column_alias";
	
	private String columnNameResrReservedID	= "gdg_column_resrvreservedid";

	private final String loginValidProcedure	= "login_valid_procedure";
	private final String loginInvalidProcedure	= "login_invalid_procedure";
	
	private final String strSimultaneousLoginRequest	= "simultaneous_login_request";
	private final String strSimultaneousLogoutRequest	= "simultaneous_logout_request";
	
	private final String strLoginRequest	= "loginrequest";
	private final String strLogoutRequest	= "logoutrequest";
	private final String strValidateLogin	= "validatelogin";
	
	private int recordThreshold		= 1;

	private int dataDelayTime		= 10000;
	private int writingDelayTime	= 10000;
	private int checkingDelayTime	= 10000;
	
	private void loginRequest() {
		final String function = "loginRequest";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(strSimultaneousLoginRequest);
		
		logger.end(className, function);
	}
	
	private void logoutRequest() {
		final String function = "logoutRequest";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(strSimultaneousLogoutRequest);
		
		logger.end(className, function);
	}
	
	private void validiteLoginValid() {
		final String function = "validiteLoginValid";
		logger.begin(className, function);

		uiEventActionProcessor_i.executeActionSet(loginValidProcedure);
		
		logger.end(className, function);
	}
	
	private void validiteLoginInvalid() {
		final String function = "validiteLoginInvalid";
		logger.begin(className, function);
		
		uiEventActionProcessor_i.executeActionSet(loginInvalidProcedure);
		
		logger.end(className, function);
	}

	private void validiteLogin() {
		final String function = "validiteLogin";
		logger.begin(className, function);
		
		SimultaneousLogin simultaneousLogin = SimultaneousLogin.getInstance();
		String area = simultaneousLogin.getArea();
		String usrIdentity = simultaneousLogin.getUsrIdentity();
	
		logger.debug(className, function, "columnNameGwsIdentity[{}] area[{}]", columnNameGwsIdentity, area);
		logger.debug(className, function, "columnNameResrReservedID[{}] usrIdentity[{}]", columnNameResrReservedID, usrIdentity);

		Map<String, Integer> records = new HashMap<String, Integer>();
		
		if ( null != rowStorage ) {
			
			logger.debug(className, function, "rowStorage.size[{}]", rowStorage.size());
			
			for ( HashMap<String, String> columns : rowStorage ) {
				
				String gwsUsrIdentity = columns.get(columnNameResrReservedID);
				if ( null != gwsUsrIdentity ) {
					if ( gwsUsrIdentity.equals(usrIdentity) ) {
						
						String gwsIdentity = columns.get(columnNameGwsIdentity);
						String gwsArea = simultaneousLogin.getArea(gwsIdentity);
						
						logger.debug(className, function, "gwsIdentity[{}] gwsArea[{}]", gwsIdentity, gwsArea);
						
						if ( null == records.get(gwsArea) ) records.put(gwsArea, 0);

						records.put(gwsArea, records.get(gwsArea) + 1);
						
						logger.debug(className, function, "records.get({})[{}]", gwsArea, records.get(gwsArea));
					}
				} else {
					logger.warn(className, function, "gwsUsrIdentity IS NULL");
				}
			}
		} else {
			logger.warn(className, function, "rowStorage IS NULL");
		}
		
		if ( null == records.get(area) ) records.put(area, 0);
		
		int record = records.get(area);
		logger.debug(className, function, "area[{}] record[{}] > recordThreshold[{}]", new Object[]{area, record, recordThreshold});
		
		if ( record <= recordThreshold ) {
			// Login Valid, forword to Main
			
			logger.debug(className, function, "Login Valid, forword to Main");
			
			validiteLoginValid();

		} else {
			// Login Invalid, forword to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");
			
			validiteLoginInvalid();
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
		
		if ( strLoginRequest.equals(element) ) {
			
			loginRequest();
		}
		else if ( strLogoutRequest.equals(element) ) {							
			
			logoutRequest();
		}
		else if ( strValidateLogin.equals(element) ) {

			validiteLogin();
		}
		
		logger.end(className, function);
	}
	
	private void loadData(Set<HashMap<String, String>> rowUpdated) {
		final String function = "loadData";
		logger.begin(className, function);
		
		logger.debug(className, function, "rowUpdated.size()[{}]", rowUpdated.size());
		
		if ( null == rowStorage ) rowStorage = new HashSet<HashMap<String, String>>();
		for ( HashMap<String, String> hashMap : rowUpdated ) {
			rowStorage.add(hashMap);
    	}
		
		for ( HashMap<String, String> entities : rowStorage ) {
			if ( null != entities ) {
				String gwsIdentity = entities.get(columnNameGwsIdentity);
				Gws gws = new Gws(entities.get(columnNameArea), entities.get(columnNameServiceOwnerID), entities.get(columnNameAlias));
				SimultaneousLogin.getInstance().setGws(gwsIdentity, gws);
				
				logger.debug(className, function, "gwsIdentity[{}] gws.area[{}] gws.scsEnvId[{}] gws.alias[{}]", new Object[]{gwsIdentity, gws.area, gws.scsEnvId, gws.alias});
			}
		}
		
		// Dump Debug Information
		if ( logger.isDebugEnabled() ) {
			int rowStorageCounter = 0;
			for ( HashMap<String, String> entities : rowStorage ) {
				
				logger.debug(className, function, "rowStorageCounter[{}]", rowStorageCounter++);
				
				if ( null != entities ) {
					for ( Entry<String, String> entity : entities.entrySet() ) {
						
						if ( null != entity ) {
							String key = entity.getKey();
							String value = entity.getValue();
							
							logger.debug(className, function, "key[{}] value[{}]", key, value);
						} else {
							logger.warn(className, function, "entity IS NULL");
						}

					}
				} else {
					logger.warn(className, function, "entities IS NULL");
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

		String strRecordThreshold 		= null;
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

			strRecordThreshold			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.RecordThreshold.toString(), strHeader);
			
			strDataDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.DataDelayTime.toString(), strHeader);
			
			strWritingDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.WritingDelayTime.toString(), strHeader);
			
			strCheckingDelayTime		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.CheckingDelayTime.toString(), strHeader);
			
			if ( logger.isDebugEnabled() ) {
				
				logger.debug(className, function, "columnNameArea[{}]", columnNameArea);
				
				logger.debug(className, function, "columnNameServiceOwnerID[{}]", columnNameServiceOwnerID);
				
				logger.debug(className, function, "columnNameAlias[{}]", columnNameAlias);
				
				logger.debug(className, function, "columnNameGwsIdentity[{}]", columnNameGwsIdentity);
				
				logger.debug(className, function, "columnNameResrReservedID[{}]", columnNameResrReservedID);
				
				logger.debug(className, function, "strRecordThreshold[{}]", strRecordThreshold);
				
				logger.debug(className, function, "strWritingDelayTime[{}]", strWritingDelayTime);
				
				logger.debug(className, function, "strCheckingDelayTime[{}]", strCheckingDelayTime);
			}
		}

		if ( null != strRecordThreshold && ! strRecordThreshold.isEmpty() ) {
			try {
				recordThreshold = Integer.parseInt(strRecordThreshold);
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strRecordThreshold[{}] NumberFormatException:"+ex.toString(), strRecordThreshold);
			}
		} else {
			logger.warn(className, function, "strRecordThreshold[{}] IS INVALID", strRecordThreshold);
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
							
							Set<HashMap<String, String>> rowUpdated = (Set<HashMap<String, String>>) obj1;
							
							loadData(rowUpdated);

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
