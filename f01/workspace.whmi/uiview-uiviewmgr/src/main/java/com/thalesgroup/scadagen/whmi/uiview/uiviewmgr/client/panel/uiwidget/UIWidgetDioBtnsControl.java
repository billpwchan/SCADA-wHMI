package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIRealize_i;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseSubscribe_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseSubscribeFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseHelper;

public class UIWidgetDioBtnsControl extends UIWidgetRealize implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDioBtnsControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private String columnAlias					= "";
	private String columnServiceOwner			= "";
	
	private boolean isAliasAndAlias2			= false;
	private String columnAlias2					= "";
	private String substituteFrom				= "dci";
	private String substituteTo					= "dio";

	private final String strInvisible			= "Invisible";
	private final String strButton				= "button";
	private final String strButtons				= strButton+"s";
	private final String strUnderline			= "_";
	private final String strButtonWUnderLine	= strButton+strUnderline;
	private final String strSet					= "set";
	
	private final String strButtonSetUp = strButtonWUnderLine+strSet+strUnderline+"up";
//	private final String strButtonSetDown = strButtonWUnderLine+strSet+strUnderline+"down";
	private final String strButtonSetDisable = strButtonWUnderLine+strSet+strUnderline+"disable";
	private final String strButtonSetVisible = strButtonWUnderLine+strSet+strUnderline+"visible";
	private final String strButtonSetInvisible = strButtonWUnderLine+strSet+strUnderline+"invisible";
	private final String strButtonSetValue = strButtonWUnderLine+strSet+strUnderline+"value";
	
	private final String strSend = "send";
	private final String strButtonSendControl = strButtonWUnderLine+strSend+strUnderline+"control";
	
	private boolean isPolling = false;
	
	private String subScribeMethod1			= "DatabaseGroupPollingDiffSingleton";
	private String multiReadMethod1			= "DatabaseMultiReadingProxySingleton";
	private String multiReadMethod2			= "DatabaseMultiReading";
	private String DotValueTable			= ".valueTable";
	private String DotInitCondGL			= ".initCondGL";

	private String [] valueTableDovnames 	= null;
	private String [] valueTableLabels 		= null;
	private String [] valueTableValues 		= null;
	
	private String initCondGLValid 			= "1";
	
	private int rowInValueTable 			= 16;
	private int dovnameCol					= 0;
	private int labelCol					= 1;
	private int valueCol					= 2;
	
	private String selectedEnv 				= null;
	private String selectedAlias			= null;

	private DatabaseSubscribe_i databaseSubscribe_i = null;
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	private DatabaseMultiRead_i databaseMultiRead_i_2 = null;
	
	private void readValueTableAndUpdateButtonStatusLabel(String env, String alias ) {
		final String function = "readValueTableAndUpdateButtonStatusLabel";
		logger.begin(className, function);
		
		logger.info(className, function, "env[{}] alias[{}]", env, alias);
		
		valueTableDovnames = new String[rowInValueTable];
		valueTableLabels = new String[rowInValueTable];
		valueTableValues = new String[rowInValueTable];
		
		String address = alias+DotValueTable;
		
		logger.info(className, function, "address[{}]", address);
		
		DataBaseClientKey ck1 = new DataBaseClientKey();
		ck1.setAPI(API.multiReadValue);
		ck1.setWidget(className);
		ck1.setStability(Stability.STATIC);
		ck1.setScreen(uiNameCard.getUiScreen());
		ck1.setEnv(env);
		ck1.setAdress(address);
		
		String clientKey = ck1.getClientKey();
		
		String [] dbaddresses = new String[]{address};

		databaseMultiRead_i.addMultiReadValueRequest(clientKey, env, dbaddresses, new DatabasePairEvent_i() {
			
			@Override
			public void update(String key, String [] dbaddress, String[] values) {
				final String function = "addMultiReadValueRequest update";
				logger.begin(className, function);
				
				if ( logger.isInfoEnabled() ) {
					for ( int i = 0 ; i < values.length ; ++i ) {
						logger.info(className, function, "values({})[{}]", i, values[i]);
					}
				}
				
				if ( null != values ) {
					if ( values.length > 0 ) {
						String valueTable = values[0];
						logger.info(className, function, "valueTable[{}]", valueTable);
						
						if ( null != valueTable ) {
							
							for( int r = 0 ; r < rowInValueTable ; ++r ) {
								String dovname = "", label = "", value = "";

								dovname	= DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
								dovname	= DatabaseHelper.removeDBStringWrapper(dovname);
								
								logger.info(className, function, "dovname({})[{}]", r, dovname);
								if ( null != dovname && dovname.length() > 0 ) {
									valueTableDovnames[r] = dovname;
								}
								
								label	= DatabaseHelper.getArrayValues(valueTable, labelCol, r );
								label	= DatabaseHelper.removeDBStringWrapper(label);
								logger.info(className, function, "label({})[{}]", r, label);
								
								String button = strButtonWUnderLine+r;
								
								logger.info(className, function, "button[{}]", button);
								
								if ( null != label && label.length() > 0 ) {
									valueTableLabels[r] = label;
									{
										String actionsetkey = strButtonSetVisible;
										String actionkey = strButtonSetVisible;
										
										logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
										
										HashMap<String, Object> parameter = new HashMap<String, Object>();
										parameter.put(ActionAttribute.OperationString2.toString(), button);
										
										HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
										override.put(actionkey, parameter);
										
										uiEventActionProcessor_i.executeActionSet(actionsetkey, override);								
									}
									
									{
										String actionsetkey = strButtonSetDisable;
										String actionkey = strButtonSetDisable;
										
										logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
										
										HashMap<String, Object> parameter = new HashMap<String, Object>();
										parameter.put(ActionAttribute.OperationString2.toString(), button);
										
										HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
										override.put(actionkey, parameter);
										
										uiEventActionProcessor_i.executeActionSet(actionsetkey, override);								
									}
									
									{
										String actionsetkey = strButtonSetValue;
										String actionkey = strButtonSetValue;
										
										logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
										
										HashMap<String, Object> parameter = new HashMap<String, Object>();
										parameter.put(ActionAttribute.OperationString2.toString(), button);
										parameter.put(ActionAttribute.OperationString3.toString(), label);
										
										HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
										override.put(actionkey, parameter);
										
										uiEventActionProcessor_i.executeActionSet(actionsetkey, override);								
									}

								} else {
									
									{
										String actionsetkey = strButtonSetInvisible;
										String actionkey = strButtonSetInvisible;
										
										logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
										
										HashMap<String, Object> parameter = new HashMap<String, Object>();
										parameter.put(ActionAttribute.OperationString2.toString(), button);
										
										HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
										override.put(actionkey, parameter);
										
										uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
									}

								}
								
								value	= DatabaseHelper.getArrayValues(valueTable, valueCol, r );
								value	= DatabaseHelper.removeDBStringWrapper(value);
								logger.info(className, function, "value({})[{}]", r, value);
								if ( null != value && value.length() > 0 ) {
									valueTableValues[r] = value;
								}
								
							}
							
							if ( isPolling ) {
								subscribeValueTable(selectedEnv, selectedAlias);
							} else {
								readValueTable(selectedEnv, selectedAlias);
							}

						} else {
							logger.info(className, function, "valueTable IS NULL");
						}
					} else {
						logger.warn(className, function, "values.length > 0 IS INVALID");
					}
				} else {
					logger.warn(className, function, "values IS NULL");
				}
			
				logger.end(className, function);
			}
		});
		logger.end(className, function);
	}
	
	private void readValueTable(String env, String alias) {
		final String function = "addValueTableSubscribe";
		logger.begin(className, function);
		
		String address = alias+DotValueTable;
		
		DataBaseClientKey ck2 = new DataBaseClientKey();
		ck2.setAPI(API.multiReadValue);
		ck2.setWidget(className);
		ck2.setStability(Stability.STATIC);
		ck2.setScreen(uiNameCard.getUiScreen());
		ck2.setEnv(env);
		ck2.setAdress(address);
		
		String clientKey = ck2.getClientKey();
		
		LinkedList<String> dovaddresslist = new LinkedList<String>();
		for ( int i = 0 ; i < valueTableDovnames.length ; ++i ) {
			String dovname = valueTableDovnames[i];
			if ( null != dovname ) {
				dovaddresslist.add(alias+dovname+DotInitCondGL);
			}
			
		}
		String [] dovaddresses = dovaddresslist.toArray(new String[0]);
		
		databaseMultiRead_i_2.addMultiReadValueRequest(clientKey, env, dovaddresses, new DatabasePairEvent_i() {
			
			@Override
			public void update(String key, String[] dbAddresses, String[] values) {
				final String function = "addSubscribeRequest update";
				logger.begin(className, function);
				if ( null != dbAddresses && null != values ) {
					for ( int i = 0 ; i < dbAddresses.length ; ++i ) {
						String dbAddress = dbAddresses[i];
						String value = values[i];
						logger.warn(className, function, "dbAddress[{}] value[{}]", dbAddress, value);
						if ( null != dbAddress && null != value ) {
							String button = strButtonWUnderLine+i;
							
							logger.info(className, function, "button[{}]", button);
							
							if ( value.equals(initCondGLValid) ) {
								String actionsetkey = strButtonSetUp;
								String actionkey = strButtonSetUp;
								
								logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString2.toString(), button);
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							} else {
								String actionsetkey = strButtonSetDisable;
								String actionkey = strButtonSetDisable;
								
								logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString2.toString(), button);
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							}
						} else {
							logger.warn(className, function, "dbAddress OR value IS NULL");
						}
					}
				} else {
					logger.warn(className, function, "dbAddresses OR values IS NULL");
				}
				logger.end(className, function);
			}
		});
		
		logger.end(className, function);
	}
	
	private void subscribeValueTable(String env, String alias) {
		final String function = "subscribeValueTable";
		logger.begin(className, function);

		String address = alias+DotValueTable;
		
		DataBaseClientKey ck2 = new DataBaseClientKey();
		ck2.setAPI(API.multiReadValue);
		ck2.setWidget(className);
		ck2.setStability(Stability.DYNAMIC);
		ck2.setScreen(uiNameCard.getUiScreen());
		ck2.setEnv(env);
		ck2.setAdress(address);
		
		String clientKey = ck2.getClientKey();
		
		LinkedList<String> dovaddresslist = new LinkedList<String>();
		for ( int i = 0 ; i < valueTableDovnames.length ; ++i ) {
			String dovname = valueTableDovnames[i];
			if ( null != dovname ) {
				dovaddresslist.add(alias+dovname+DotInitCondGL);
			}
			
		}
		String [] dovaddresses = dovaddresslist.toArray(new String[0]);
		
		databaseSubscribe_i.addSubscribeRequest(clientKey, env, dovaddresses, new DatabasePairEvent_i() {
			
			@Override
			public void update(String key, String[] dbAddresses, String[] values) {
				final String function = "addSubscribeRequest update";
				logger.begin(className, function);
				if ( null != dbAddresses && null != values ) {
					for ( int i = 0 ; i < dbAddresses.length ; ++i ) {
						String dbAddress = dbAddresses[i];
						String value = values[i];
						logger.warn(className, function, "dbAddress[{}] value[{}]", dbAddress, value);
						if ( null != dbAddress && null != value ) {
							String button = strButtonWUnderLine+i;
							
							logger.info(className, function, "button[{}]", button);
							
							if ( value.equals(initCondGLValid) ) {
								String actionsetkey = strButtonSetUp;
								String actionkey = strButtonSetUp;
								
								logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString2.toString(), button);
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							} else {
								String actionsetkey = strButtonSetDisable;
								String actionkey = strButtonSetDisable;
								
								logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
								
								HashMap<String, Object> parameter = new HashMap<String, Object>();
								parameter.put(ActionAttribute.OperationString2.toString(), button);
								
								HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
								override.put(actionkey, parameter);
								
								uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
							}
						} else {
							logger.warn(className, function, "dbAddress OR value IS NULL");
						}
					}
				} else {
					logger.warn(className, function, "dbAddresses OR values IS NULL");
				}
				logger.end(className, function);
			}
		});
		logger.end(className, function);
	}
	
	private void unSubscribeValueTable(String env, String alias) {
		final String function = "unSubscribeValueTable";
		logger.begin(className, function);
		
		String address = alias+DotValueTable;
		
		DataBaseClientKey ck2 = new DataBaseClientKey();
		ck2.setAPI(API.multiReadValue);
		ck2.setWidget(className);
		ck2.setStability(Stability.DYNAMIC);
		ck2.setScreen(uiNameCard.getUiScreen());
		ck2.setEnv(env);
		ck2.setAdress(address);
		
		String clientKey = ck2.getClientKey();
		
		databaseSubscribe_i.addUnSubscribeRequest(clientKey);
		
		logger.end(className, function);
	}
		
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
//		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
//		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
//		logger.info(className, function, "strEventBusName[{}]", strEventBusName);
		
		String strIsPolling 			= null;
		String strRowInValueTable		= null;
		String strDovnameCol			= null;
		String strLabelCol				= null;
		String strValueCol				= null;
		
		String strIsAliasAndAlias2		= null;

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			
			columnAlias				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.ColumnAlias.toString(), strHeader);
			columnServiceOwner		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.ColumnServiceOwner.toString(), strHeader);
			
			strIsPolling			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.IsPolling.toString(), strHeader);
			
			subScribeMethod1		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.SubScribeMethod1.toString(), strHeader);
			multiReadMethod1		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.MultiReadMethod1.toString(), strHeader);
			multiReadMethod2		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.MultiReadMethod2.toString(), strHeader);
			
			DotValueTable			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.DotValueTable.toString(), strHeader);
			DotInitCondGL			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.DotInitCondGL.toString(), strHeader);
			
			initCondGLValid			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.InitCondGLValid.toString(), strHeader);
			
			strRowInValueTable		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.RowInValueTable.toString(), strHeader);
			strDovnameCol			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.DovnameCol.toString(), strHeader);
			strLabelCol				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.LabelCol.toString(), strHeader);
			strValueCol				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.ValueCol.toString(), strHeader);
			
			strIsAliasAndAlias2		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.IsAliasAndAlias2.toString(), strHeader);
			columnAlias2			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.ColumnAlias2.toString(), strHeader);
			substituteFrom			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.SubstituteFrom.toString(), strHeader);
			substituteTo			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.SubstituteTo.toString(), strHeader);

		}
		
		logger.info(className, function, "columnAlias[{}]", columnAlias);
		logger.info(className, function, "columnServiceOwner[{}]", columnServiceOwner);

		logger.info(className, function, "subScribeMethod1[{}]", subScribeMethod1);
		logger.info(className, function, "multiReadMethod1[{}]", multiReadMethod1);
		logger.info(className, function, "multiReadMethod2[{}]", multiReadMethod2);
		logger.info(className, function, "DotValueTable[{}]", DotValueTable);
		logger.info(className, function, "DotInitCondGL[{}]", DotInitCondGL);
		
		logger.info(className, function, "strRowInValueTable[{}]", strRowInValueTable);
		logger.info(className, function, "strDovnameCol[{}]", strDovnameCol);
		logger.info(className, function, "strLabelCol[{}]", strLabelCol);
		logger.info(className, function, "strValueCol[{}]", strValueCol);
		
		logger.info(className, function, "strIsAliasAndAlias2[{}]", strIsAliasAndAlias2);
		logger.info(className, function, "columnAlias2[{}]", columnAlias2);
		logger.info(className, function, "substituteFrom[{}]", substituteFrom);
		logger.info(className, function, "substituteTo[{}]", substituteTo);
		
		if ( null != strIsPolling && strIsPolling.equals(Boolean.TRUE.toString()) ) {
			isPolling = true;
		}
		
		try {
			rowInValueTable = Integer.parseInt(strRowInValueTable);
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strRowInValueTable[{}] IS INVALID", strRowInValueTable);
			logger.warn(className, function, "NumberFormatException[{}]", ex.toString());
		}
		
		try {
			dovnameCol = Integer.parseInt(strDovnameCol);
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strDovnameCol[{}] IS INVALID", strDovnameCol);
			logger.warn(className, function, "NumberFormatException[{}]", ex.toString());
		}
		
		try {
			labelCol = Integer.parseInt(strLabelCol);
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strLabelCol[{}] IS INVALID", strLabelCol);
			logger.warn(className, function, "NumberFormatException[{}]", ex.toString());
		}
		
		try {
			valueCol = Integer.parseInt(strValueCol);
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strValueCol[{}] IS INVALID", strValueCol);
			logger.warn(className, function, "NumberFormatException[{}]", ex.toString());
		}
		
		if ( null != strIsAliasAndAlias2 && strIsAliasAndAlias2.equals(Boolean.TRUE.toString()) ) {
			isAliasAndAlias2 = true;
		}
	
//		uiWidgetGeneric = new UIWidgetGeneric();
//		uiWidgetGeneric.setUINameCard(this.uiNameCard);
//		uiWidgetGeneric.setDictionaryFolder(dictionaryFolder);
//		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
//		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
//		uiWidgetGeneric.init();
//		
//		UIEventActionProcessorMgr uiEventActionProcessorMgr = UIEventActionProcessorMgr.getInstance();
//		uiEventActionProcessor_i = uiEventActionProcessorMgr.getUIEventActionProcessor("UIEventActionProcessor");
//
//		uiEventActionProcessor_i.setUINameCard(uiNameCard);
//		uiEventActionProcessor_i.setPrefix(className);
//		uiEventActionProcessor_i.setElement(element);
//		uiEventActionProcessor_i.setDictionariesCacheName("UIWidgetGeneric");
//		uiEventActionProcessor_i.setEventBus(eventBus);
//		uiEventActionProcessor_i.setOptsXMLFile(optsXMLFile);
//		uiEventActionProcessor_i.setUIGeneric(uiWidgetGeneric);
//		uiEventActionProcessor_i.setActionSetTagName(UIActionEventType.actionset.toString());
//		uiEventActionProcessor_i.setActionTagName(UIActionEventType.action.toString());
//		uiEventActionProcessor_i.init();
//		
//		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
//			@Override
//			public void onClickHandler(ClickEvent event) {
//				if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onClick(event);
//			}
//		});
//		
//		rootPanel = uiWidgetGeneric.getMainPanel();
//
//		handlerRegistrations.add(
//			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
//				@Override
//				public void onEvenBusUIChanged(UIEvent uiEvent) {
//					if ( uiEvent.getSource() != this ) {
//						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onUIEvent(uiEvent);
//					}
//				}
//			})
//		);
//
//		handlerRegistrations.add(
//			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
//				@Override
//				public void onAction(UIEventAction uiEventAction) {
//					if ( uiEventAction.getSource() != this ) {
//						if ( null != uiWidgetCtrl_i ) uiWidgetCtrl_i.onActionReceived(uiEventAction);
//					}
//				}
//			})
//		);
//
//		uiEventActionProcessor_i.executeActionSetInit();
		
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
						String element = uiWidgetGeneric.getWidgetElement(widget);
						logger.info(className, function, "element[{}]", element);
						if ( null != element ) {
							
							String [] elements = element.split(strUnderline);
							
							int buttonNumIndex = 1;
							if ( elements.length > buttonNumIndex ) {
								if ( elements[0].equals(strButton)) {
									
									String strSelectedIndex = elements[buttonNumIndex];
									
									logger.info(className, function, "strSelectedIndex[{}]", strSelectedIndex);
									
									int selectedIndex = -1;
									
									selectedIndex = Integer.parseInt(strSelectedIndex);
									
									if ( selectedIndex >= 0 ) {
										
										String selectedValue = valueTableValues[selectedIndex];
										
										logger.info(className, function, "selectedValue[{}]", selectedValue);
										
										String actionsetkey = strButtonSendControl;
										String actionkey = strButtonSendControl;
										
										logger.info(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
										
										logger.info(className, function, "selectedEnv[{}] selectedAlias[{}] selectedValue[{}]", new Object[]{selectedEnv, selectedAlias, selectedValue});
										
										HashMap<String, Object> parameter = new HashMap<String, Object>();
										parameter.put(ActionAttribute.OperationString2.toString(), selectedEnv);
										parameter.put(ActionAttribute.OperationString3.toString(), selectedAlias);
										parameter.put(ActionAttribute.OperationString4.toString(), selectedValue);
										
										HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
										override.put(actionkey, parameter);
										
										uiEventActionProcessor_i.executeActionSet(actionsetkey, override);

									} else {
										logger.warn(className, function, "selectedIndex[{}] >= 0 IS INVALID", selectedIndex);
									}
									
								}
							} else {
								logger.warn(className, function, "elements.length[{}] >= buttonNumIndex[{}] IS INVALID", elements.length, buttonNumIndex);
							}
						}
					}
				}
				logger.begin(className, function);
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public void onActionReceived(UIEventAction uiEventAction) {
				final String function = "onActionReceived";
				
				logger.begin(className, function);
				
				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				
				logger.info(className, function, "os1["+os1+"]");
				
				if ( null != os1 ) {
					// Filter Action
					if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
						
						logger.info(className, function, "FilterAdded");
						
						uiEventActionProcessor_i.executeActionSet(os1);
						
					} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
						
						logger.info(className, function, "FilterRemoved");
						
						uiEventActionProcessor_i.executeActionSet(os1);
					
					} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
						// Activate Selection
						
						// Reset the Button
						logger.info(className, function, "Reset the button to invisible");

						String actionsetkey = strButtons+strUnderline+strInvisible;
						uiEventActionProcessor_i.executeActionSet(actionsetkey);
						
						if ( isPolling ) {
							if ( null != selectedEnv && null != selectedAlias ) {
								unSubscribeValueTable(selectedEnv, selectedAlias);
							}
						}
						
						logger.info(className, function, "Store Selected Row");
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						Set<HashMap<String, String>> selectedSet	= (Set<HashMap<String, String>>) obj1;

						for ( HashMap<String, String> hashMap : selectedSet ) {
							String serviceOwner = hashMap.get(columnServiceOwner);
							String alias = hashMap.get(columnAlias);

							selectedEnv = serviceOwner;
							selectedAlias = alias;
							
							logger.info(className, function, "selectedEnv[{}], selectedAlias[{}]", selectedEnv, selectedAlias);
							
							if ( isAliasAndAlias2 ) {
								String alias2 = hashMap.get(columnAlias2);
								
								logger.info(className, function, "alias2[{}]", alias2);
								
								if ( null != alias2 ) {
									String oldpoint = alias.substring(alias2.length());
									String newpoint = oldpoint.replace(substituteFrom, substituteTo);
									
									logger.info(className, function, "oldpoint[{}], newpoint[{}]", oldpoint, newpoint);
									
									selectedAlias = alias2 + newpoint;
								} else {
									logger.warn(className, function, "alias2 IS NULL");
								}
								
							}
		
							logger.info(className, function, "selectedEnv[{}], selectedAlias[{}]", selectedEnv, selectedAlias);
							
							if ( ! selectedAlias.startsWith("<alias>") ) selectedAlias = "<alias>" + selectedAlias;
								
							logger.info(className, function, "selectedAlias["+selectedAlias+"]");
							
						}
						
//						// SYAU: Testing
//						selectedEnv = "OCCENVCONN";
//						selectedAlias = "<alias>CTVECTMFD_0001dioECT-PTW";
//						logger.warn(className, function, "Testing value selectedEnv[{}] selectedAlias[{}]", selectedEnv, selectedAlias);

						readValueTableAndUpdateButtonStatusLabel(selectedEnv, selectedAlias);

					} else {
						// General Case
						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.info(className, function, "oe ["+oe+"]");
						logger.info(className, function, "os1["+os1+"]");
						
						if ( null != oe ) {
							if ( oe.equals(element) ) {
								
								HashMap<String, HashMap<String, Object>> override = null;
								uiEventActionProcessor_i.executeActionSet(os1, override, new UIExecuteActionHandler_i() {
									
									@Override
									public boolean executeHandler(UIEventAction uiEventAction) {
										String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
										
										logger.info(className, function, "os1["+os1+"]");					
										
										if ( null != os1 ) {
											if ( os1.equals("set_default_up") ) {
												String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
												envUp(os2);
											}
											if ( os1.equals("set_default_down") ) {
												String os2	= (String) uiEventAction.getParameter(ViewAttribute.OperationString2.toString());
												envDown(os2);
											}
											if ( os1.equals("set_default_kill") ) {
												terminate();
											}
										}
										return true;
									}
								});
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
		
				logger.info(className, function, "multiReadMethod1[{}]", multiReadMethod1);
				
				databaseMultiRead_i = DatabaseMultiReadFactory.get(multiReadMethod1);
				databaseMultiRead_i.connect();
				
				if ( isPolling ) {
					
					logger.info(className, function, "subScribeMethod1[{}]", subScribeMethod1);
					
					databaseSubscribe_i = DatabaseSubscribeFactory.get(subScribeMethod1);
					databaseSubscribe_i.connect();
				} else {
					logger.info(className, function, "multiReadMethod2[{}]", multiReadMethod2);
					
					databaseMultiRead_i_2 = DatabaseMultiReadFactory.get(multiReadMethod2);
					databaseMultiRead_i_2.connect();
				}
				
		
				logger.begin(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.begin(className, function);
				
				if ( null != databaseSubscribe_i ) {
					databaseSubscribe_i.disconnect();
				}
				
				if ( null != databaseMultiRead_i ) {
					databaseMultiRead_i.disconnect();
				}
				
				if ( null != databaseMultiRead_i_2 ) {
					databaseMultiRead_i_2.disconnect();
				}
				
				logger.begin(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				
				if ( isPolling ) {
					if ( null != selectedEnv && null != selectedAlias ) {
						unSubscribeValueTable(selectedEnv, selectedAlias);
					} else {
						logger.warn(className, function, "selectedEnv OR selectedAlias IS NULL");
					}
				}
				
				envDown(null);
				
				logger.begin(className, function);
			};
		
		};
		
		logger.end(className, function);
	}

}
