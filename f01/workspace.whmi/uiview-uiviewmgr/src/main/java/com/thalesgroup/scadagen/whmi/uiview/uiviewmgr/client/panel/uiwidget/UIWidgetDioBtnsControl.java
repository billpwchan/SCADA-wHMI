package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
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

public class UIWidgetDioBtnsControl extends UIWidgetRealize {
	
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
	private final String strValue				= "value";
	private final String strIndex				= "index";
	
	private final String strButtonSetUp = strButtonWUnderLine+strSet+strUnderline+"up";
//	private final String strButtonSetDown = strButtonWUnderLine+strSet+strUnderline+"down";
	private final String strButtonSetDisable = strButtonWUnderLine+strSet+strUnderline+"disable";
	private final String strButtonSetVisible = strButtonWUnderLine+strSet+strUnderline+"visible";
	private final String strButtonSetInvisible = strButtonWUnderLine+strSet+strUnderline+"invisible";
	private final String strButtonSetValue = strButtonWUnderLine+strSet+strUnderline+strValue;
	
	private final String strSend = "send";
	private final String strButtonSendControl = strButtonWUnderLine+strSend+strUnderline+"control";
	
	private boolean isPolling = false;
	
	private String subScribeMethod1			= "DatabaseGroupPollingDiffSingleton";
	private String multiReadMethod1			= "DatabaseMultiReadingProxySingleton";
	private String multiReadMethod2			= "DatabaseMultiReading";
	private String multiReadMethod3			= "DatabaseMultiReading";
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
	
	private int delayMSBetweenCmds				= 0;
	
	private boolean initCondValidity		= false;
	
	private Set<Map<String, String>> selectedSet		= null;
	
	private void setButtonVisible(String button) {
		final String function = "setButtonVisible";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSetVisible;
		String actionkey = strButtonSetVisible;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), button);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(className, function);
	}
	private void setButtonInvisible(String button) {
		final String function = "setButtonInvisible";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSetInvisible;
		String actionkey = strButtonSetInvisible;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), button);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(className, function);
	}
	private void setButtonDisable(String button) {
		final String function = "setButtonDisable";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSetDisable;
		String actionkey = strButtonSetDisable;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), button);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(className, function);
	}
	private void setButtonUp(String button) {
		final String function = "setButtonUp";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSetUp;
		String actionkey = strButtonSetUp;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), button);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(className, function);
	}
	private void setButtonValue(String button, String label) {
		final String function = "setButtonValue";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSetValue;
		String actionkey = strButtonSetValue;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), button);
		parameter.put(ActionAttribute.OperationString3.toString(), label);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		
		logger.end(className, function);
	}

	private DatabaseSubscribe_i databaseSubscribe_i = null;
	private DatabaseMultiRead_i databaseMultiRead_i = null;
	private DatabaseMultiRead_i databaseMultiRead_i_2 = null;
	private DatabaseMultiRead_i databaseMultiRead_i_3 = null;
	
	private boolean isSingleEntity(Set<Map<String, String>> set) { return (null != set) ? (set.size() == 1) : false; }
	private boolean isMultiEntity(Set<Map<String, String>> set) { return (null != set) ? (set.size() > 1) : false; }
	
	private void readValueTableAndUpdateButtonStatusLabel(String env, String alias ) {
		final String function = "readValueTableAndUpdateButtonStatusLabel";
		logger.begin(className, function);
		
		logger.debug(className, function, "env[{}] alias[{}]", env, alias);
		
		valueTableDovnames = new String[rowInValueTable];
		valueTableLabels = new String[rowInValueTable];
		valueTableValues = new String[rowInValueTable];
		
		if ( null != databaseMultiRead_i ) {
			String address = alias+DotValueTable;
			
			logger.debug(className, function, "address[{}]", address);
			
			DataBaseClientKey clientKey = new DataBaseClientKey();
			clientKey.setAPI(API.multiReadValue);
			clientKey.setWidget(className);
			clientKey.setStability(Stability.STATIC);
			clientKey.setScreen(uiNameCard.getUiScreen());
			clientKey.setEnv(env);
			clientKey.setAdress(address);
			
			String strClientKey = clientKey.getClientKey();
			
			String [] dbaddresses = new String[]{address};

			databaseMultiRead_i.addMultiReadValueRequest(strClientKey, env, dbaddresses, new DatabasePairEvent_i() {
				
				@Override
				public void update(String key, String [] dbaddress, String[] values) {
					final String function = "addMultiReadValueRequest update";
					logger.begin(className, function);
					
					if ( logger.isDebugEnabled() ) {
						for ( int i = 0 ; i < values.length ; ++i ) {
							logger.debug(className, function, "values({})[{}]", i, values[i]);
						}
					}
					
					if ( null != selectedSet ) { 
						if ( null != values ) {
							if ( values.length > 0 ) {
								String valueTable = values[0];
								logger.debug(className, function, "valueTable[{}]", valueTable);
								
								if ( null != valueTable ) {
									
									for( int r = 0 ; r < rowInValueTable ; ++r ) {
										String dovname = "", label = "", value = "";

										dovname	= DatabaseHelper.getArrayValues(valueTable, dovnameCol, r );
										dovname	= DatabaseHelper.removeDBStringWrapper(dovname);
										
										logger.debug(className, function, "dovname({})[{}]", r, dovname);
										if ( null != dovname && dovname.length() > 0 ) {
											valueTableDovnames[r] = dovname;
										}
										
										label	= DatabaseHelper.getArrayValues(valueTable, labelCol, r );
										label	= DatabaseHelper.removeDBStringWrapper(label);
										logger.debug(className, function, "label({})[{}]", r, label);
										
										String button = strButtonWUnderLine+r;
										
										logger.debug(className, function, "button[{}]", button);
										
										if ( null != label && label.length() > 0 ) {
											valueTableLabels[r] = label;
											
											setButtonVisible(button);

											if ( isMultiEntity(selectedSet) ) {
												setButtonUp(button);
											} else
											if ( isSingleEntity(selectedSet) ) { 
												setButtonDisable(button);
											}
											
											setButtonValue(button, label);

										} else {
											
											setButtonInvisible(button);

										}
										
										value	= DatabaseHelper.getArrayValues(valueTable, valueCol, r );
										value	= DatabaseHelper.removeDBStringWrapper(value);
										logger.debug(className, function, "value({})[{}]", r, value);
										if ( null != value && value.length() > 0 ) {
											valueTableValues[r] = value;
										}
									}
									
									if ( isSingleEntity(selectedSet) ) { 
										for ( Map<String, String> map : selectedSet ) {
											String env = map.get(columnServiceOwner);
											String alias = map.get(columnAlias);
											String alias2 = map.get(columnAlias2);
											
											if ( isAliasAndAlias2 ) {
												alias = getAliasWithAlias2(alias, alias2, substituteFrom, substituteTo);
											}
											
											alias = getAliasWithAliasPrefix(alias);
											
											getInitCond(env, alias);
											
											break;
										}
									}
								} else {
									logger.debug(className, function, "valueTable IS NULL");
								}
							} else {
								logger.warn(className, function, "values.length > 0 IS INVALID");
							}
						} else {
							logger.warn(className, function, "values IS NULL");
						}
					} else {
						logger.warn(className, function, "selectedSet IS NULL");
					}
					
					logger.end(className, function);
				}
			});
		} else {
			logger.warn(className, function, "databaseMultiRead_i IS NULL");
		}

		logger.end(className, function);
	}
	
	private void updateButtonUpDisable(String [] dovaddresses, String [] dbAddresses, String [] values ) {
		final String function = "updateButtonUpDisable";
		logger.begin(className, function);
		if ( null != dbAddresses && null != values ) {
			for ( int i = 0 ; i < dbAddresses.length ; ++i ) {
				
				String dbAddress = dbAddresses[i];
				String value = values[i];
				
				int index = 0;
				for ( int x = 0 ; x < dovaddresses.length ; x++ ) {
					if ( 0 == dovaddresses[x].compareTo(dbAddress) ) {
						index = x;
						break;
					}
				}

				logger.debug(className, function, "dbAddress[{}] value[{}]", dbAddress, value);
				if ( null != dbAddress && null != value ) {
					String button = strButtonWUnderLine+index;
					
					logger.debug(className, function, "button[{}]", button);
					
					if ( value.equals(initCondGLValid) ) {
						
						setButtonUp(button);

					} else {
						
						logger.debug(className, function, "selectedSet.size()[{}]", selectedSet.size());
						
						setButtonDisable(button);

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
	
	private DataBaseClientKey clientKey = null;	
	private void getInitCond(String env, String alias) {
		final String function = "getInitCond";
		logger.begin(className, function);
		
		String address = alias+DotInitCondGL;
		
		clientKey = new DataBaseClientKey();
		clientKey.setAPI(API.multiReadValue);
		clientKey.setWidget(className);
		clientKey.setStability(Stability.STATIC);
		clientKey.setScreen(uiNameCard.getUiScreen());
		clientKey.setEnv(env);
		clientKey.setAdress(address);
		
		String strClientKey = clientKey.getClientKey();
		
		List<String> dovaddresslist = new LinkedList<String>();
		for ( int i = 0 ; i < valueTableDovnames.length ; ++i ) {
			String dovname = valueTableDovnames[i];
			if ( null != dovname ) {
				dovaddresslist.add(alias+dovname+DotInitCondGL);
			}
			
		}
		final String [] dovaddresses = dovaddresslist.toArray(new String[0]);
		
		if ( isPolling ) {
			if ( null != databaseSubscribe_i ) {
				databaseSubscribe_i.addSubscribeRequest(strClientKey, env, dovaddresses, new DatabasePairEvent_i() {
					
					@Override
					public void update(String key, String[] dbAddresses, String[] values) {
						final String function = "addSubscribeRequest update";
						logger.begin(className, function);
						updateButtonUpDisable(dovaddresses, dbAddresses, values);
						logger.end(className, function);
					}
				});
			} else {
				logger.warn(className, function, "databaseSubscribe_i IS NULL");
			}

		} else {
			if ( null != databaseMultiRead_i_2 ) {
				databaseMultiRead_i_2.addMultiReadValueRequest(strClientKey, env, dovaddresses, new DatabasePairEvent_i() {
					
					@Override
					public void update(String key, String[] dbAddresses, String[] values) {
						final String function = "addMultiReadValueRequest update";
						logger.begin(className, function);
						updateButtonUpDisable(dovaddresses, dbAddresses, values);
						logger.end(className, function);
					}
				});
			} else {
				logger.warn(className, function, "databaseMultiRead_i_2 IS NULL");
			}
		}

		logger.end(className, function);
	}
	
	private void unSubscribeValueTable() {
		final String function = "unSubscribeValueTable";
		logger.begin(className, function);
		if ( null != databaseSubscribe_i ) {
			if ( null != clientKey ) {
				databaseSubscribe_i.addUnSubscribeRequest(clientKey.getClientKey());
			}
		} else {
			logger.warn(className, function, "databaseSubscribe_i IS NULL");
		}
		logger.end(className, function);
	}
	
	private static int readingCount = 0;
	private void executeCmdWithInitCond(final String env, final String alias, final String value, final String strIndex) {
		final String function = "executeCmdWithInitCond";
		logger.begin(className, function);
		logger.debug(className, function, "env[{}] alias[{}] value[{}] strIndex[{}]", new Object[]{env, alias, value, strIndex});

		int index = Integer.parseInt(strIndex);
		
		if ( null != valueTableDovnames ) {
			List<String> dovaddresslist = new LinkedList<String>();
			String dovname = valueTableDovnames[index];
			if ( null != dovname ) {
				dovaddresslist.add(alias+dovname+DotInitCondGL);
				
				if ( logger.isDebugEnabled() ) {
					for ( String dovaddress : dovaddresslist)
					logger.debug(className, function, "dovaddress[{}]", dovaddress);
				}
				
				final String [] dovaddresses = dovaddresslist.toArray(new String[0]);

				if ( null != databaseMultiRead_i_3 ) {
					String address = alias+DotInitCondGL;
					
					DataBaseClientKey clientKey = new DataBaseClientKey();
					clientKey.setAPI(API.multiReadValue);
					clientKey.setWidget(className);
					clientKey.setStability(Stability.STATIC);
					clientKey.setScreen(uiNameCard.getUiScreen());
					clientKey.setEnv(env);
					clientKey.setAdress(address+"_"+readingCount++);
					
					String strClientKey = clientKey.getClientKey();
					logger.debug(className, function, "strClientKey[{}]", strClientKey);
					databaseMultiRead_i_3.addMultiReadValueRequest(strClientKey, env, dovaddresses, new DatabasePairEvent_i() {
						
						@Override
						public void update(String key, String[] dbAddresses, String[] dbValues) {
							final String function2 = function +" update";
							logger.begin(className, function2);
							logger.debug(className, function2, "key[{}]", key);
							if ( null != dbAddresses && null != dbValues ) {
								if (dbAddresses.length == 1 ) {
									if ( dbAddresses.length == dbValues.length ) {
										if ( null != dbValues[0] ) {
											logger.debug(className, function2, "env[{}] alias[{}] value[{}] dbValues[0]", new Object[]{env, alias, value, dbValues[0]});
											if ( dbValues[0].equals(initCondGLValid) ) {
												executeCmd(env, alias, value);
											} else {
												logger.warn(className, function2, "dbValues[0][{}] IS NOT EQUAL initCondGLValid[{}]", dbValues[0], initCondGLValid);
											}
										} else {
											logger.warn(className, function2, "dbValues[0] IS NULL");
										}
									} else {
										logger.warn(className, function2, "dbAddresses.length[{}] AND dbValues.length[{}] IS NOT EQUAL", dbAddresses.length, dbValues.length);
									}
								} else {
									logger.warn(className, function2, "dbAddresses.length[{}] IS NOT EQUAL 1", dbAddresses.length);
								}
							} else {
								logger.warn(className, function2, "dbAddresses OR dbValues IS NULL");
							}
							logger.end(className, function2);
						}
					});
				} else {
					logger.warn(className, function, "databaseMultiRead_i_3 IS NULL");
				}
			} else {
				logger.warn(className, function, "dovname IS NULL");
			}
		} else {
			logger.debug(className, function, "valueTableDovnames IS NULL");
		}

		logger.end(className, function);
	}
	
	private void executeCmd(String env, String alias, String value) {
		final String function = "executeCmd";
		logger.begin(className, function);
		
		String actionsetkey = strButtonSendControl;
		String actionkey = strButtonSendControl;
		
		logger.debug(className, function, "actionsetkey[{}] actionkey[{}]", actionsetkey, actionkey);
		
		logger.debug(className, function, "env[{}] alias[{}] value[{}]", new Object[]{env, alias, value});
		
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		parameter.put(ActionAttribute.OperationString2.toString(), env);
		parameter.put(ActionAttribute.OperationString3.toString(), alias);
		parameter.put(ActionAttribute.OperationString4.toString(), value);
		
		HashMap<String, HashMap<String, Object>> override = new HashMap<String, HashMap<String, Object>>();
		override.put(actionkey, parameter);
		
		uiEventActionProcessor_i.executeActionSet(actionsetkey, override);
		logger.end(className, function);
	}
	

	private void executeCmd(String env, String alias, String value, String index, boolean isMultiEntity) {
		final String function = "executeCmd";
		logger.begin(className, function);
		logger.debug(className, function, "env[{}] alias[{}] value[{}] index[{}] isMultiEntity[{}]", new Object[]{env, alias, value, index, isMultiEntity});
		if ( isMultiEntity ) {
			executeCmdWithInitCond(env, alias, value, index);
		} else {
			executeCmd(env, alias, value);
		}
		logger.end(className, function);
	}
	
	private Timer t = null;
	private void executeCmds(final Set<Map<String, String>> set, final boolean isMultiEntity, int delayBetweenCmds) {
		final String function = "executeCmds";
		logger.begin(className, function);
		
		if ( null != set ) {
			logger.debug(className, function, "delayBetweenCmds[{}]", delayBetweenCmds);
			if ( delayBetweenCmds > 0 ) {
				if ( null != t ) {
					t.cancel();
					t = null;
				}
				t = new Timer() {
					public void run() {
						final String function2 = function + " timer";
						logger.begin(className, function2);
						
						if ( null == set || (null != set && set.isEmpty()) ) {
							logger.warn(className, function2, "set IS NULL || set.size <= 0, cancel timer...");
							if ( t != null ) {
								t.cancel();
								t = null;
							}
						} else {
							Iterator<Map<String, String>> its = set.iterator();
							Map<String, String> map = its.next();
							if ( null != map ) {
								
								executeCmd(map.get(columnServiceOwner), map.get(columnAlias), map.get(strValue), map.get(strIndex), isMultiEntity);
							} else {
								logger.debug(className, function2, "its.next() map IS NULL");
							}
							its.remove();
							
							if ( ! its.hasNext() ) {
								logger.debug(className, function2, "its.hasNext() IS FALSE, cancel timer...");
								if ( t != null ) {
									t.cancel();
									t = null;
								}
							}
						}
						logger.end(className, function2);
					}
				};
				
				// Schedule the timer to run once every second, delayBetweenCmd ms.
				t.scheduleRepeating(delayBetweenCmds);
				
			} else {
			
				for ( Map<String, String> map : set ) {
					executeCmd(map.get(columnServiceOwner), map.get(columnAlias), map.get(strValue), map.get(strIndex), isMultiEntity);
				}
			}
		} else {
			logger.warn(className, function, "set IS NULL");
		}
		logger.end(className, function);
	}
	private String getAliasWithAliasPrefix(String alias) {
		final String function = "getAliasWithAliasPrefix";
		logger.begin(className, function);
		logger.debug(className, function, "alias[{}] BF", alias);
		if ( ! alias.startsWith("<alias>") ) alias = "<alias>"+alias;
		logger.debug(className, function, "alias[{}] AF", alias);
		logger.end(className, function);
		return alias;
	}
	private String getAliasWithAlias2(String alias, String alias2, String substituteFrom, String substituteTo) {
		final String function = "getAliasWithAlias2";
		logger.begin(className, function);
		logger.debug(className, function, "alias[{}] alias2[{}]", alias, alias2);
		if ( null != alias2 ) {
			String oldpoint = alias.substring(alias2.length());
			String newpoint = oldpoint.replace(substituteFrom, substituteTo);
			logger.debug(className, function, "oldpoint[{}], newpoint[{}]", oldpoint, newpoint);
			alias = alias2 + newpoint;
		} else {
			logger.warn(className, function, "alias2 IS NULL");
		}
		logger.debug(className, function, "alias[{}]", alias);
		logger.end(className, function);
		return alias;
	}
		
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);

		String strIsPolling 			= null;
		String strRowInValueTable		= null;
		String strDovnameCol			= null;
		String strLabelCol				= null;
		String strValueCol				= null;
		
		String strIsAliasAndAlias2		= null;
		
		String strDelayMSBetweenCmds	= null;
		
		String strInitCondValidity		= null;

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
			
			multiReadMethod3		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.MultiReadMethod3.toString(), strHeader);
			
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
			
			strDelayMSBetweenCmds	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.DelayMSBetweenCmds.toString(), strHeader);
			
			strInitCondValidity		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDioBtnsControl_i.ParameterName.InitCondValidity.toString(), strHeader);
		}
		
		logger.debug(className, function, "columnAlias[{}]", columnAlias);
		logger.debug(className, function, "columnServiceOwner[{}]", columnServiceOwner);

		logger.debug(className, function, "subScribeMethod1[{}]", subScribeMethod1);
		logger.debug(className, function, "multiReadMethod1[{}]", multiReadMethod1);
		logger.debug(className, function, "multiReadMethod2[{}]", multiReadMethod2);
		
		logger.debug(className, function, "multiReadMethod3[{}]", multiReadMethod3);
		
		logger.debug(className, function, "DotValueTable[{}]", DotValueTable);
		logger.debug(className, function, "DotInitCondGL[{}]", DotInitCondGL);
		
		logger.debug(className, function, "initCondGLValid[{}]", initCondGLValid);
		
		logger.debug(className, function, "strRowInValueTable[{}]", strRowInValueTable);
		logger.debug(className, function, "strDovnameCol[{}]", strDovnameCol);
		logger.debug(className, function, "strLabelCol[{}]", strLabelCol);
		logger.debug(className, function, "strValueCol[{}]", strValueCol);
		
		logger.debug(className, function, "strIsAliasAndAlias2[{}]", strIsAliasAndAlias2);
		logger.debug(className, function, "columnAlias2[{}]", columnAlias2);
		logger.debug(className, function, "substituteFrom[{}]", substituteFrom);
		logger.debug(className, function, "substituteTo[{}]", substituteTo);
		
		logger.debug(className, function, "strDelayMSBetweenCmds[{}]", strDelayMSBetweenCmds);
		
		logger.debug(className, function, "strInitCondValidity[{}]", strInitCondValidity);
		
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
		
		try {
			delayMSBetweenCmds = Integer.parseInt(strDelayMSBetweenCmds);
		} catch ( NumberFormatException ex ) {
			logger.warn(className, function, "strDelayMSBetweenCmds[{}] IS INVALID", strDelayMSBetweenCmds);
			logger.warn(className, function, "NumberFormatException[{}]", ex.toString());
		}
		
		if ( null != strInitCondValidity && strInitCondValidity.equals(Boolean.TRUE.toString()) ) {
			initCondValidity = true;
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
							
							String [] elements = element.split(strUnderline);
							
							int buttonNumIndex = 1;
							if ( elements.length > buttonNumIndex ) {
								if ( elements[0].equals(strButton)) {
									
									String strSelectedIndex = elements[buttonNumIndex];
									
									logger.debug(className, function, "strSelectedIndex[{}]", strSelectedIndex);
									
									int selectedIndex = -1;
									
									selectedIndex = Integer.parseInt(strSelectedIndex);
									
									if ( selectedIndex >= 0 ) {
										
										String selectedValue = valueTableValues[selectedIndex];
										
										logger.debug(className, function, "selectedValue[{}]", selectedValue);
										
										if ( null != selectedSet ) {
											Set<Map<String, String>> set = new HashSet<Map<String, String>>();
											for ( Map<String, String> map : selectedSet ) {
												if ( null != map ) {
													Map<String, String> map2 = new HashMap<String, String>();
													map2.put(columnServiceOwner, map.get(columnServiceOwner));
													String alias = map.get(columnAlias);
													if ( isAliasAndAlias2 ) alias = getAliasWithAlias2(alias, map.get(columnAlias2), substituteFrom, substituteTo);
													alias = getAliasWithAliasPrefix(alias);
													map2.put(columnAlias, alias);
													map2.put(strValue, String.valueOf(selectedValue));
													map2.put(strIndex, String.valueOf(selectedIndex));
													set.add(map2);
												}
											}
											executeCmds(set, isMultiEntity(set) && initCondValidity, delayMSBetweenCmds);
										} else {
											logger.warn(className, function, "selectedSet IS NULL");
										}
										
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
				
				logger.debug(className, function, "os1["+os1+"]");
				
				if ( null != os1 ) {
					// Filter Action
					if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
						
						logger.debug(className, function, "FilterAdded");
						
						uiEventActionProcessor_i.executeActionSet(os1);
						
					} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
						
						logger.debug(className, function, "FilterRemoved");
						
						uiEventActionProcessor_i.executeActionSet(os1);
					
					} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
						// Activate Selection
						
						// Reset the Button
						logger.debug(className, function, "Reset the button to invisible");

						String actionsetkey = strButtons+strUnderline+strInvisible;
						uiEventActionProcessor_i.executeActionSet(actionsetkey);
						
						if ( isPolling ) {
							unSubscribeValueTable();
						}
						
						logger.debug(className, function, "Store Selected Row");
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());

						if ( null != obj1 ) {
							selectedSet	= (Set<Map<String, String>>) obj1;
							
							String env = null;
							String alias = null; 
							String alias2 = null;
							for ( Map<String, String> map : selectedSet ) {
								env = map.get(columnServiceOwner);
								alias = map.get(columnAlias);
								alias2 = map.get(columnAlias2);

								if ( isAliasAndAlias2 ) alias = getAliasWithAlias2(alias, alias2, substituteFrom, substituteTo);

								alias = getAliasWithAliasPrefix(alias);
								
								break;
							}

							readValueTableAndUpdateButtonStatusLabel(env, alias);
						}
					} else {
						// General Case
						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.debug(className, function, "oe ["+oe+"]");
						
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
		
				logger.debug(className, function, "multiReadMethod1[{}]", multiReadMethod1);
				
				databaseMultiRead_i = DatabaseMultiReadFactory.get(multiReadMethod1);
				if ( null == databaseMultiRead_i ) logger.warn(className, function, "multiReadMethod1[{}] databaseMultiRead_i IS NULL", multiReadMethod1);
				databaseMultiRead_i.connect();
				
				logger.debug(className, function, "isPolling[{}]", isPolling);
				if ( isPolling ) {
					
					logger.debug(className, function, "subScribeMethod1[{}]", subScribeMethod1);
					
					databaseSubscribe_i = DatabaseSubscribeFactory.get(subScribeMethod1);
					if ( null == databaseMultiRead_i ) logger.warn(className, function, "subScribeMethod1[{}] databaseSubscribe_i IS NULL", subScribeMethod1);
					databaseSubscribe_i.connect();
				} else {
					logger.debug(className, function, "multiReadMethod2[{}]", multiReadMethod2);
					
					databaseMultiRead_i_2 = DatabaseMultiReadFactory.get(multiReadMethod2);
					if ( null == databaseMultiRead_i ) logger.warn(className, function, "multiReadMethod2[{}] databaseMultiRead_i_2 IS NULL", multiReadMethod2);
					databaseMultiRead_i_2.connect();
				}
				
				logger.debug(className, function, "multiReadMethod3[{}]", multiReadMethod3);
				
				databaseMultiRead_i_3 = DatabaseMultiReadFactory.get(multiReadMethod3);
				if ( null == databaseMultiRead_i ) logger.warn(className, function, "multiReadMethod3[{}] databaseMultiRead_i_3 IS NULL", multiReadMethod3);
				databaseMultiRead_i_3.connect();
		
				logger.end(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.begin(className, function);
				
				if ( null != databaseSubscribe_i ) {
					logger.debug(className, function, "subScribeMethod1[{}]", subScribeMethod1);
					databaseSubscribe_i.disconnect();
					databaseSubscribe_i = null;
				}
				
				if ( null != databaseMultiRead_i ) {
					logger.debug(className, function, "multiReadMethod1[{}]", multiReadMethod1);
					databaseMultiRead_i.disconnect();
					databaseMultiRead_i = null;
				}
				
				if ( null != databaseMultiRead_i_2 ) {
					logger.debug(className, function, "multiReadMethod2[{}]", multiReadMethod2);
					databaseMultiRead_i_2.disconnect();
					databaseMultiRead_i_2 = null;
				}
				
				if ( null != databaseMultiRead_i_3 ) {
					logger.debug(className, function, "multiReadMethod3[{}]", multiReadMethod3);
					databaseMultiRead_i_3.disconnect();
					databaseMultiRead_i_3 = null;
				}
				
				logger.end(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				
				if ( isPolling ) {
					unSubscribeValueTable();
				}
				
				envDown(null);
				
				logger.end(className, function);
			};
		};
		logger.end(className, function);
	}
}
