package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.common.UIPanelInspector_i;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.timer.CountdownTimer;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.timer.CountdownTimerEvent;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.util.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.config.configenv.client.ReadProp;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseReadSingle2SingleResult_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSingle2SingleRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseGetFullPathFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseHelper;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.HVID2SCS;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.OpmMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.UIOpm_i;

public class UIInspectorMgr {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIInspectorMgr.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private static HashMap<String, UIInspectorMgr> instances = new HashMap<String, UIInspectorMgr>();
	private UIInspectorMgr () {}
	public static UIInspectorMgr getInstance(String key) {
		if ( ! instances.containsKey(key) ) { instances.put(key, new UIInspectorMgr()); }
		UIInspectorMgr instance = instances.get(key);
		return instance;
	}
	
	private UIInspectorDialogBox uiInspectorDialogbox = null;
	private CountdownTimer countdownTimer = null;

	private DatabaseSingle2SingleRead_i databaseSingle2SingleRead_i = null;
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	public void getFunctionLocationAndLaunchInspectorDialog(final UINameCard uiNameCard, final HashMap<String, String> options) {
		final String function = "getFunctionLocationAndLaunchInspectorDialog";
				
		logger.debug(className, function, "Launch Inspector Panel OPM Checking...");
		
		final String dictionariesCacheName = UIInspector_i.strUIInspector;
		final String inspDialogBoxPropPrefix = "inspectorpaneldialogbox.";
		final String inspDialogBoxProp = inspDialogBoxPropPrefix+"properties";
		
		String strDatabasePathPatternKey = inspDialogBoxPropPrefix+UIPanelInspector_i.strDatabasePathPatternKey;
		logger.debug(className, function, "strDatabasePathPatternKey[{}]", strDatabasePathPatternKey);
		String databasePathPattern = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, strDatabasePathPatternKey, null);
		logger.debug(className, function, "databasePathPattern[{}]", databasePathPattern);
		
		String function2 = options.get("function");
		String location2 = options.get("location");
		
		String hv_id = options.get("hv_id");
		
		if ( null == function2 || null == location2 ) {
	
			HVID2SCS hvid2scs = new HVID2SCS(hv_id, databasePathPattern);
			
			final String scsEnvId		= hvid2scs.getScsEnvID();
			final String parent			= hvid2scs.getDBAddress();
			
			logger.debug(className, function+"parent[{}]", parent);
			logger.debug(className, function+"scsEnvId[{}]", scsEnvId);

			//
			String strDatabaseGetFullPathKey = inspDialogBoxPropPrefix+UIPanelInspector_i.strDatabaseGetFullPathKey;
			logger.debug(className, function, "strDatabaseGetFullPathKey[{}]", strDatabaseGetFullPathKey);
			String databaseGetFullPathKey = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, strDatabaseGetFullPathKey, "DatabaseGetFullPathProxy");
			logger.debug(className, function, "databaseGetFullPathKey[{}]", databaseGetFullPathKey);
			
			databaseSingle2SingleRead_i = DatabaseGetFullPathFactory.get(databaseGetFullPathKey);
			if ( null != databaseSingle2SingleRead_i ) {
				databaseSingle2SingleRead_i.connect();
				
				DataBaseClientKey ck = new DataBaseClientKey();
				ck.setAPI(API.GetFullPath);
				ck.setWidget(className);
				ck.setStability(Stability.STATIC);
				ck.setScreen(uiNameCard.getUiScreen());
				ck.setEnv(scsEnvId);
				ck.setAdress(parent);
				
				String clientKey = ck.getClientKey();
				
				logger.debug(className, function, "scsEnvId[{}] <alias>+parent[{}] clientKey[{}]", new Object[]{scsEnvId, "<alias>"+parent, clientKey});
				
				databaseSingle2SingleRead_i.addRequest(clientKey, scsEnvId, parent, new DatabaseReadSingle2SingleResult_i() {
					
					@Override
					public void update(final String key, final String values) {
						logger.debug(className, function, "key[{}] values[{}]", key, values);
						
						if (values != null) {
							final String unquotedStr = values.replaceAll("\"", "");
							
							logger.warn(className, function, "unquotedStr[{}]", unquotedStr);
							
							if ( null == databaseMultiRead_i ) {
								
								String strDatabaseMultiReadingKey = inspDialogBoxPropPrefix+UIPanelInspector_i.strDatabaseMultiReadingKey;
								logger.debug(className, function, "strDatabaseMultiReadingKey[{}]", strDatabaseMultiReadingKey);
								String databaseMultiReadingProxyKey = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, strDatabaseMultiReadingKey, "DatabaseMultiReadingProxy");
								logger.debug(className, function, "databaseMultiReadingProxyKey[{}]", databaseMultiReadingProxyKey);
								
								databaseMultiRead_i = DatabaseMultiReadFactory.get(databaseMultiReadingProxyKey);
								if ( null != databaseMultiRead_i ) {
									databaseMultiRead_i.connect();
									
									DataBaseClientKey ck = new DataBaseClientKey();
									ck.setAPI(API.multiReadValue);
									ck.setWidget(className);
									ck.setStability(Stability.STATIC);
									ck.setScreen(uiNameCard.getUiScreen());
									ck.setEnv(scsEnvId);
									ck.setAdress(unquotedStr);
									
									String clientKey = ck.getClientKey();
									
									String [] staticAttibutes = new String[]{PointName.functionalCat.toString(), PointName.geographicalCat.toString()};
									
									String[] dbaddresses = null;
									{
										ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
										for(int y=0;y<staticAttibutes.length;++y) {
											dbaddressesArrayList.add(unquotedStr+staticAttibutes[y]);
										}
										dbaddresses = dbaddressesArrayList.toArray(new String[0]);
									}
									
									if ( logger.isDebugEnabled() ) {
										logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
										for(int i = 0; i < dbaddresses.length; ++i ) {
											logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
										}
									}
									
									databaseMultiRead_i.addMultiReadValueRequest(clientKey, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
										
										@Override
										public void update(String key, String[] dbAddresses, String[] dbValues) {
											
											
											String strFunctionAddress = unquotedStr+PointName.functionalCat.toString();
											String strFunctionValue = DatabaseHelper.getFromPairArray(strFunctionAddress, dbAddresses, dbValues);
											
											// Equipment Label
											if ( null != strFunctionValue ) {

												strFunctionValue = DatabaseHelper.removeDBStringWrapper(strFunctionValue);
												
												logger.debug(className, function, "strFunctionAddress[{}] strFunctionValue[{}]", strFunctionAddress, strFunctionValue);
											}
											
											String strLocationAddress = unquotedStr+PointName.geographicalCat.toString();
											String strLocationValue = DatabaseHelper.getFromPairArray(strLocationAddress, dbAddresses, dbValues);
											
											if ( null != strLocationValue ) {
												
												strLocationValue = DatabaseHelper.removeDBStringWrapper(strLocationValue);
												
												logger.debug(className, function, "strLocationAddress[{}] strLocationValue[{}]", strLocationAddress, strLocationValue);
											}
											
											logger.debug(className, function, "strFunctionValue[{}] strLocationValue[{}]", strFunctionValue, strLocationValue);
											
											options.put("function", strFunctionValue);
											options.put("location", strLocationValue);
											
											if ( null != databaseMultiRead_i) {
												databaseMultiRead_i.disconnect();
												databaseMultiRead_i = null;
											}
											
											openInspectorDialogBoxWithOpm(uiNameCard, options);
										}
									});
								}
							} else {
								logger.warn(className, function, "databaseMultiRead_i IS NOT NULL, Skip the creation of databaseMultiRead_i");
							}
							
						} else {
							logger.warn(className, function, "values IS NULL");
						}
						
					}
				});
			}

		} else {
			logger.warn(className, function, "function2[{}] || location2[{}] IS FALSE, Skip reading function and location from DB", function2, location2);
		}
		logger.end(className, function);
	}
	
	public void openInspectorDialogBoxWithOpm(UINameCard uiNameCard, Map<String, String> options) {
		final String function = "openInspectorDialogBoxWithOpm";
		
		logger.begin(className, function);
		
		String function2 = options.get("function");
		String location2 = options.get("location");
		
		logger.debug(className, function, "function2[{}] location2[{}]", function2, location2);
		
		final String dictionariesCacheName = UIInspector_i.strUIInspector;
		final String inspDialogBoxPropPrefix = "inspectorpaneldialogbox.";
		final String inspDialogBoxProp = inspDialogBoxPropPrefix+"properties";
		
		String opmapi = null;
		
		String keyopmapi = inspDialogBoxPropPrefix+UIPanelInspector_i.strOpmApi;
		String keyopmapivalue = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, keyopmapi, "");
		
		opmapi = keyopmapivalue;
		
		String action = null;
		
		String keyaction = inspDialogBoxPropPrefix+UIPanelInspector_i.strAction;
		String actionvalue = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, keyaction, null);
		
		action = actionvalue;
		
		String mode = null;
		
		String keymode = inspDialogBoxPropPrefix+UIPanelInspector_i.strMode;
		String modevalue = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, keymode, null);
		
		mode = modevalue;
		
		logger.debug(className, function, "action[{}] mode[{}]", action, mode);
		
		boolean right0 = true;
		if ( null != function2 && null != location2 ) {
			OpmMgr opmMgr = OpmMgr.getInstance();
			UIOpm_i uiOpm_i = opmMgr.getOpm(opmapi);
			
			if ( null != uiOpm_i ) {
				right0 = uiOpm_i.checkAccess(function2, location2, action, mode);
			} else {
				logger.warn(className, function, "uiOpm_i IS NULL");
			}
			
		} else {
			logger.warn(className, function, "function2[{}] OR location2[{}] IS NULL", function2, location2);
		}
		
		logger.debug(className, function, "Launch the UIInspectorMgr...");
		
		if ( right0 ) {

			openInspectorDialog(uiNameCard, options);
		
		} else {
			logger.warn(className, function, "right0 function2[{}] location2[{}] action[{}] mode[{}]", new Object[]{function2, location2, action, mode});
			logger.warn(className, function, "right0 IS INSUFFICIENT RIGHT");
			
		}
		
		logger.end(className, function);
	}

	public void openInspectorDialog(final UINameCard uiNameCard, final Map<String, String> options) {
		
		final String function = "openInspectorDialog";
		logger.begin(className, function);
		
		final String dictionariesCacheName = UIInspector_i.strUIInspector;
		final String inspDialogBoxPropPrefix = "inspectorpaneldialogbox.";
		final String inspDialogBoxProp = inspDialogBoxPropPrefix+"properties";

		final String strModal = "modal";
		final boolean modal = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strModal, true);
		
		final String strDBPatternKey = inspDialogBoxPropPrefix+UIPanelInspector_i.strDatabasePathPatternKey;
		logger.debug(className, function, "strDatabasePathPatternKey[{}]", strDBPatternKey);
		String dbPathPattern = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, strDBPatternKey, null);
		logger.debug(className, function, "dbPathPattern[{}]", dbPathPattern);
		
		final String hv_id			= options.get("hv_id");
		final String hv_type		= options.get("hv_type");
		
		final String function2		= options.get("function");
		final String location2		= options.get("location");
		
		final String strMouseX		= options.get("mouseX");
		final String strMouseY		= options.get("mouseY");
		
		final int mouseX = Integer.parseInt(strMouseX);
		final int mouseY = Integer.parseInt(strMouseY);
		
		HVID2SCS hvid2scs = new HVID2SCS(hv_id, dbPathPattern);
		
		final String scsEnvId	= hvid2scs.getScsEnvID();
		final String dbaddress	= hvid2scs.getDBAddress();
		logger.debug(className, function, "scsEnvId[{}] dbaddress[{}]", scsEnvId, dbaddress);
		
		logger.debug(className, function, "hv_id[{}]", hv_id);
		
		logger.debug(className, function, "function2[{}] location2[{}]", function2, location2);
		logger.debug(className, function, "mouseX[{}] mouseY[{}]", mouseX, mouseY);
		
		logger.debug(className, function, "modal[{}]", modal);
		
		String strDatabaseGetFullPathKey = inspDialogBoxPropPrefix+UIPanelInspector_i.strDatabaseGetFullPathKey;
		logger.debug(className, function, "strDatabaseGetFullPathKey[{}]", strDatabaseGetFullPathKey);
		String databaseGetFullPathKey = ReadProp.readString(dictionariesCacheName, inspDialogBoxProp, strDatabaseGetFullPathKey, "DatabaseGetFullPathProxy");
		logger.debug(className, function, "databaseGetFullPathKey[{}]", databaseGetFullPathKey);
		
		databaseSingle2SingleRead_i = DatabaseGetFullPathFactory.get(databaseGetFullPathKey);
		if ( null != databaseSingle2SingleRead_i ) {
			databaseSingle2SingleRead_i.connect();
			
			DataBaseClientKey ck = new DataBaseClientKey();
			ck.setAPI(API.GetFullPath);
			ck.setWidget(className);
			ck.setStability(Stability.STATIC);
			ck.setScreen(uiNameCard.getUiScreen());
			ck.setEnv(scsEnvId);
			ck.setAdress("<alias>"+dbaddress);
			
			String clientKey = ck.getClientKey();
			
			logger.debug(className, function, "scsEnvId[{}] <alias>+dbaddress[{}] clientKey[{}]", new Object[]{scsEnvId, "<alias>"+dbaddress, clientKey});
			
			databaseSingle2SingleRead_i.addRequest(clientKey, scsEnvId, "<alias>"+dbaddress, new DatabaseReadSingle2SingleResult_i() {
				
				@Override
				public void update(String key, String values) {
					logger.debug(className, function, "key[{}] values[{}]", key, values);
					
					if (values != null) {
						final String unquotedStr = values.replaceAll("\"", "");
						
						logger.warn(className, function, "unquotedStr[{}]", unquotedStr);
						
						if ( null == uiInspectorDialogbox ) {
							
							logger.debug(className, function, "Creating uiInspectorDialogbox...");

							uiInspectorDialogbox = new UIInspectorDialogBox();
							
							uiInspectorDialogbox.setUINameCard(uiNameCard);
							uiInspectorDialogbox.setHvInfo(hv_id, hv_type);
							uiInspectorDialogbox.setFunctionLocation(function2, location2);
							uiInspectorDialogbox.init();
							uiInspectorDialogbox.getMainPanel();
							
							uiInspectorDialogbox.setMousePosition(mouseX, mouseY);
							uiInspectorDialogbox.show();
							
							uiInspectorDialogbox.setParent(scsEnvId, unquotedStr);
							
							uiInspectorDialogbox.setModal(modal);
							
							uiInspectorDialogbox.setUIInspectorDialogBoxEvent(new UIInspectorDialogBoxEvent() {
								
								@Override
								public void onOpen() {
									// TODO Auto-generated method stub
								}
								
								@Override
								public void onClick() {
									final String function = "onClick";
									logger.begin(className, function);
									updateInspectorDialogTimer();
									logger.end(className, function);
								}
								
								@Override
								public void onClose() {	
									final String function = "onClose";
									logger.begin(className, function);
									closeInspectorDialog();
									logger.end(className, function);
								}
								
							});
											
							uiInspectorDialogbox.connect();
							
							
							final String strAutoCloseEnable = "autoCloseEnable";
							final String strAutoCloseExpiredMillSecond = "autoCloseExpiredMillSecond";
							
							boolean autoCloseEnable = ReadProp.readBoolean(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseEnable, false);
							int autoCloseExpiredMillSecond = ReadProp.readInt(dictionariesCacheName, inspDialogBoxProp, inspDialogBoxPropPrefix+strAutoCloseExpiredMillSecond, 60*1000);
							
							logger.debug(className, function, "autoCloseEnable[{}]", autoCloseEnable);
							logger.debug(className, function, "autoCloseExpiredMillSecond[{}]", autoCloseExpiredMillSecond);
							
							if ( autoCloseEnable ) {
								
								countdownTimer = new CountdownTimer();
								countdownTimer.setTimerExpiredMillSecond(autoCloseExpiredMillSecond);
								countdownTimer.setCountdownTimerEvent(new CountdownTimerEvent() {
									
									@Override
									public void timeup() {
										final String function = "timeup";
										logger.begin(className, function);
										killInspectorDialogTimer();
										closeInspectorDialog();
										logger.end(className, function);
									}
								});
								
								countdownTimer.init();
								countdownTimer.close();
								countdownTimer.start();
								
							} else {
								logger.debug(className, function, "timer disabled");
							}

						} else {
							logger.debug(className, function, "uiInspectorDialogbox IS NOT NULL");
						}
						
					} else {
						logger.debug(className, function, "values IS NULL");
					}

				}
			});
		}

		logger.end(className, function);

	}
	
	private void updateInspectorDialogTimer() {
		final String function = "updateInspectorDialogTimer";
		logger.begin(className, function);
		if ( null != countdownTimer ) countdownTimer.update();
		logger.end(className, function);
	}
	
	private void killInspectorDialogTimer() {
		final String function = "killInspectorDialogTimer";
		logger.begin(className, function);
		if ( null != countdownTimer ) {
			countdownTimer.close();
			countdownTimer = null;
		}
		logger.end(className, function);
	}
	
	public void closeInspectorDialog() {
		final String function = "closeInspectorDialog";
		logger.begin(className, function);
		if ( null != uiInspectorDialogbox ) {
			killInspectorDialogTimer();
			uiInspectorDialogbox.disconnect();
			uiInspectorDialogbox.hide();
		}
		uiInspectorDialogbox = null;
		logger.end(className, function);
	}
}
