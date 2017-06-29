rpackage com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

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

	private int initDelayTime		= 10000;
	private int dataDelayTime		= 10000;
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
	
//	private boolean loggedin = false;
	private void validateLogin() {
		final String function = "validateLogin";
		logger.begin(className, function);

		int result = SimultaneousLogin.getInstance().validateLogin();
		
		logger.debug(className, function, "result[{}]", result);
		
		if (       0 != (result & SimultaneousLogin_i.Bit_Pos_SelfGwsIdentity_IsInvalid)
				|| 0 != (result & SimultaneousLogin_i.Bit_Pos_SelfGwsArea_IsInvalid)
				|| 0 != (result & SimultaneousLogin_i.Bit_Pos_SelfUsrIdentity_IsInvalid)
				|| 0 != (result & SimultaneousLogin_i.Bit_Pos_Storage_IsEmpty)
			) {
			
			// Login Invalid, forword to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginInvalidSelfIdentityProcedure);
		}
		else if ( 0 != (result & SimultaneousLogin_i.Bit_Pos_IsByPassUsrIdentity) ) {
			
			// Login Valid, forword to Main
			
			logger.debug(className, function, "Login Valid, forword to Main");
			
//			loggedin = true;
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginValidProcedure);
		}
		else if ( 0 != (result & SimultaneousLogin_i.Bit_Pos_ReservedInOtherArea) ) {
			
			// Login Invalid, forword to Logout
			
			logger.debug(className, function, "Login Invalid, forword to Logout");

			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginInvalidThresHoldReachProcedure);
		}
		else if ( 0 != (result & SimultaneousLogin_i.Bit_Pos_ReservedSelfGws) ) {
			
			// Login Valid, forword to Main
			
			logger.debug(className, function, "Login Valid, forword to Main");
			
//			loggedin = true;
			
			uiEventActionProcessor_i.executeActionSet(UIWidgetSimultaneousLoginControl_i.loginValidProcedure);

		}
		
		logger.end(className, function);
	}

	private Timer t1 = null;
	private Timer t2 = null;
	private Timer t3 = null;
	
//	boolean loginRequested = false;
	private void login() {
		final String function = "login";
		logger.begin(className, function);
		
		logger.debug(className, function, "initDelayTime[{}] start...", initDelayTime);
		t1 = new Timer() {
			public void run() {
				
				logger.debug(className, function, "p1 run...");
				
				// Receive the User and GWS information from GDG
				SimultaneousLogin.getInstance().init();

				logger.debug(className, function, "dataDelayTime[{}] start...", dataDelayTime);
				t2 = new Timer() {
					public void run() {
						
						logger.debug(className, function, "p2 run...");	
						
						// Verify target GWS is not reserved by other user
						validateLogin();
					
						logger.debug(className, function, "checkingDelayTime[{}] start...", checkingDelayTime);
						t3 = new Timer() {
							public void run() {
							
								logger.debug(className, function, "p3 run...");
								
								// Request login in the GWS
//								loginRequested = true;
								loginRequest();
							}
						};
						t3.schedule(checkingDelayTime);
					
					}
				};
				t2.schedule(dataDelayTime);
			}
		};
		t1.schedule(initDelayTime);
		
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

			validateLogin();
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
		
//		logger.debug(className, function, "loginRequested[{}] loggedin[{}]", loginRequested, loggedin);
//		if ( loginRequested ) {
//			if ( ! loggedin ) {
				validateLogin();
//			}
//		}
		
		logger.end(className, function);
	}
	
	private void loadParameter() {		
		final String function = "init";
		logger.begin(className, function);
	
		String strInitDelayTime 		= null;
		String strDataDelayTime 		= null;
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

			strInitDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.InitDelayTime.toString(), strHeader);
			strDataDelayTime			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.DataDelayTime.toString(), strHeader);
			strCheckingDelayTime		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetSimultaneousLoginControl_i.ParameterName.CheckingDelayTime.toString(), strHeader);
			
			if ( logger.isDebugEnabled() ) {
				
				logger.debug(className, function, "columnNameArea[{}]", columnNameArea);
				logger.debug(className, function, "columnNameServiceOwnerID[{}]", columnNameServiceOwnerID);
				logger.debug(className, function, "columnNameAlias[{}]", columnNameAlias);
				logger.debug(className, function, "columnNameGwsIdentity[{}]", columnNameGwsIdentity);
				logger.debug(className, function, "columnNameResrReservedID[{}]", columnNameResrReservedID);
				
				logger.debug(className, function, "onActionReceived[{}]", strInitDelayTime);
				logger.debug(className, function, "strWritingDelayTime[{}]", strDataDelayTime);
				logger.debug(className, function, "strCheckingDelayTime[{}]", strCheckingDelayTime);
			}
		}
		
		if ( null != strInitDelayTime && ! strInitDelayTime.isEmpty() ) {
			try {
				initDelayTime = Integer.parseInt(strInitDelayTime);
				if ( initDelayTime < 0 ) initDelayTime = 0;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strInitDelayTime[{}] NumberFormatException:"+ex.toString(), strInitDelayTime);
			}
		} else {
			logger.warn(className, function, "strInitDelayTime[{}] IS INVALID", strInitDelayTime);
		}
		
		if ( null != strDataDelayTime && ! strDataDelayTime.isEmpty() ) {
			try {
				dataDelayTime = Integer.parseInt(strDataDelayTime);
				if ( dataDelayTime < 0 ) dataDelayTime = 0;
			} catch (NumberFormatException ex) {
				logger.warn(className, function, "strDataDelayTime[{}] NumberFormatException:"+ex.toString(), strDataDelayTime);
			}
		} else {
			logger.warn(className, function, "strDataDelayTime[{}] IS INVALID", strDataDelayTime);
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
//			else if ( os1.startsWith(UIWidgetSimultaneousControl_i.SimultaneousLoginEvent.PagerValueChanged_EndIndex.toString() ) ) {
//				// Activate Selection
//				
//				String os2 = (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
//				
//				logger.debug(className, function, "os2[{}]", os2);
//				
//				try {
//					endIndex = Integer.parseInt(os2);
//				} catch ( NumberFormatException ex ) {
//					logger.warn(className, function, "os2["+os2+"] ex:"+ex.toString());
//				}
//				
//				logger.debug(className, function, "endIndex[{}]", endIndex);
//
//			}
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
