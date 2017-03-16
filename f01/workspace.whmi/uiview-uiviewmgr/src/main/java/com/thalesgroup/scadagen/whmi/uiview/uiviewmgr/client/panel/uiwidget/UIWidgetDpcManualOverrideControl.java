package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
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
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabaseMultiRead_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.common.DatabasePairEvent_i;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.factory.DatabaseMultiReadFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseHelper;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.API;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DataBaseClientKey_i.Stability;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseHelper_i.PointName;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.util.DatabaseHelper_i.PointType;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIWidgetDpcManualOverrideControl extends UIWidgetRealize implements UIRealize_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDpcManualOverrideControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);

	private DpcMgr dpcMgr				= null;
	
	private String columnAlias			= "";
	private String columnStatus			= "";
	private String columnServiceOwner	= "";
	
	private String valueSet				= "";
	private String valueUnSet			= "";
	
	private Set<HashMap<String, String>> selectedSet = null;
	
	private final String strSet					= "set";
	private final String strTxtValue			= "txtvalue";
	private final String strLstValue			= "lstvalue";
	
	private String strLabel = PointName.label.toString();
	private String strValue = PointName.value.toString();
	private String strAalValueTable = PointName.aalValueTable.toString();
	private String strDalValueTable = PointName.dalValueTable.toString();
	private String strSalValueTable = PointName.salValueTable.toString();
	private String strHmiOrder = PointName.salValueTable.toString();
	
	private ListBox lstValues					= null;
	private TextBox txtValues					= null;
	
	private String address = null;
	private String scsEnvId = null;

	private DatabaseMultiRead_i databaseMultiRead_i = null;

	// Static Attribute List
	private final String staticDciAttibutes []	= new String[] {strLabel, strDalValueTable, strHmiOrder};
	private final String staticAciAttibutes []	= new String[] {strLabel, strAalValueTable, strHmiOrder};
	private final String staticSciAttibutes []	= new String[] {strLabel, strSalValueTable, strHmiOrder};
	
	private void updateDropDownListBox(String key, String[] dbAddresses, String[] dbValues) {
		final String function = "updateDropDownListBox";
		logger.begin(className, function);
		
		String address = this.address;

		// ACI, SCI Show the TextBox
		// DCI, Show the ListBox and store the valueTable
		
		logger.info(className, function, "address[{}]", address);
		
		if ( null != address ) {
			
			String point = DatabaseHelper.getPointFromAliasAddress(address);
			
			logger.info(className, function, "point[{}]", point);
			
			PointType pointType = DatabaseHelper.getPointType(point);
			
			logger.info(className, function, "pointType[{}]", pointType);
			
			if ( PointType.dci == pointType ) {

				String actionsetkey = "selectdcipoint";
				uiEventActionProcessor_i.executeActionSet(actionsetkey);

				String targetDbAddress = address+strDalValueTable;
				String targetValue = DatabaseHelper.getFromPairArray(targetDbAddress, dbAddresses, dbValues);
				
				if ( null != targetValue ) {
					// Update the Label
					String valueTable = targetValue;
					logger.debug(className, function, "valueTable[{}]", valueTable);

					int valueCol = 0, labelCol = 1;
					String labels[]	= new String[12];
					String values[]	= new String[12];
					{
						for( int r = 0 ; r < 12 ; ++r ) {
							values[r]	= DatabaseHelper.getArrayValues(valueTable, valueCol, r );
							values[r]	= DatabaseHelper.removeDBStringWrapper(values[r]);
							labels[r]	= DatabaseHelper.getArrayValues(valueTable, labelCol, r );
							labels[r]	= DatabaseHelper.removeDBStringWrapper(labels[r]);
						}					
					}
					
					lstValues.clear();
					for( int r = 0 ; r < 12 ; ++r ) {
						
						if ( null != labels[r] && labels[r].length() > 0 && ! labels[r].equals("null") ) {
							lstValues.addItem(labels[r]);
						} else {
							break;
						}
							
						logger.info(className, function, "names[{}][{}] values[{}][{}]", new Object[]{r, labels[r], r, values[r]});
					}
				} else {
					logger.warn(className, function, "valueTable IS NULL!");
				}
				
			} else if ( PointType.aci == pointType ) {

				String actionsetkey = "selectacipoint";
				uiEventActionProcessor_i.executeActionSet(actionsetkey);

				// Update the Label
				String targetDbAddress = address+strValue;
				String targetValue = DatabaseHelper.getFromPairArray(targetDbAddress, dbAddresses, dbValues);
				logger.debug(className, function, "targetValue[{}]", targetValue);
				
				txtValues.setText(targetValue);
				
			} else if ( PointType.sci == pointType ) {

				String actionsetkey = "selectscipoint";
				uiEventActionProcessor_i.executeActionSet(actionsetkey);

				String targetDbAddress = address+strValue;
				String targetValue = DatabaseHelper.getFromPairArray(targetDbAddress, dbAddresses, dbValues);
				logger.debug(className, function, "targetValue[{}]", targetValue);
				
				txtValues.setText(targetValue);
				
			}
		} else {
			logger.warn(className, function, "address IS NULL");
		}

		logger.end(className, function);
	}
	
	private void readPointValueTable(String dbScsEnvId, String dbaddress) {
		final String function = "readPointValueTable";
		logger.begin(className, function);
		String[] dbaddresses = null;
		
		if ( null != dbScsEnvId && null != dbaddress ) {
			
			ArrayList<String> dbaddressesArrayList = new ArrayList<String>();

			String point = DatabaseHelper.getPointFromAliasAddress(dbaddress);
			logger.info(className, function, "dbaddress[{}] point[{}]", dbaddress, point);
			if ( null != point ) {
				PointType pointType = DatabaseHelper.getPointType(point);
				if ( pointType == PointType.dci ) {
					for ( String attribute : staticDciAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( pointType == PointType.aci ) {
					for ( String attribute : staticAciAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else if ( pointType == PointType.sci ) {
					for ( String attribute : staticSciAttibutes ) {
						dbaddressesArrayList.add(dbaddress+attribute);
					}
				} else {
					logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
				}
			}

			dbaddresses = dbaddressesArrayList.toArray(new String[0]);
		
			DataBaseClientKey ck = new DataBaseClientKey();
			ck.setAPI(API.multiReadValue);
			ck.setWidget(className);
			ck.setStability(Stability.STATIC);
			ck.setScreen(uiNameCard.getUiScreen());
			ck.setEnv(scsEnvId);
			ck.setAdress(address);
			
			String clientKey = ck.getClientKey();
			
			logger.info(className, function, "clientKey[{}]", clientKey);
			
			if (logger.isDebugEnabled() ) {
				logger.debug(className, function, "clientKey[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}
			
			databaseMultiRead_i.addMultiReadValueRequest(clientKey, scsEnvId, dbaddresses, new DatabasePairEvent_i() {
				
				@Override
				public void update(String key, String[] dbAddresses, String[] values) {

					DataBaseClientKey ck = new DataBaseClientKey();
					ck.setAPI(API.multiReadValue);
					ck.setWidget(className);
					ck.setStability(Stability.STATIC);
					ck.setScreen(uiNameCard.getUiScreen());
					ck.setEnv(scsEnvId);
					ck.setAdress(address);
					
					String clientKey = ck.getClientKey();
					
					logger.info(className, function, "key[{}] clientKey[{}]", key, clientKey);
					
					if ( clientKey.equals(key) ) {
						updateDropDownListBox(key, dbAddresses, values);
					}
				}
			});
			
		} else {
			logger.warn(className, function, "dbScsEnvId OR dbaddress IS NULL");
		}

		logger.end(className, function);
	}
	
	@Override
	public void init() {
		super.init();
		
		final String function = "init";
		logger.begin(className, function);
		
		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcManualOverrideControl_i.ParameterName.ColumnAlias.toString(), strHeader);
			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcManualOverrideControl_i.ParameterName.ColumnStatus.toString(), strHeader);
			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcManualOverrideControl_i.ParameterName.ColumnServiceOwner.toString(), strHeader);
			valueSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcManualOverrideControl_i.ParameterName.ValueSet.toString(), strHeader);
			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcManualOverrideControl_i.ParameterName.ValueUnSet.toString(), strHeader);
		}
		
		lstValues = (ListBox) uiWidgetGeneric.getWidget(strLstValue);
		txtValues = (TextBox) uiWidgetGeneric.getWidget(strTxtValue);
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {

			@Override
			public void onUIEvent(UIEvent event) {
				
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
							
							HashMap<String, HashMap<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									// TODO Auto-generated method stub
									String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									
									logger.info(className, function, "os1[{}]", os1);
									
									if ( null != os1 ) {
										if ( os1.equals("SendDpcManualControl") ) {
											
											for ( HashMap<String, String> hashMap : selectedSet ) {
												String selectedAlias = hashMap.get(columnAlias);
												String selectedServiceOwner = hashMap.get(columnServiceOwner);
												
												logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
												
												scsEnvId = selectedServiceOwner;
												address = selectedAlias;
												
												if ( null != scsEnvId && null != address ) {
													
													logger.info(className, function, "alias BF [{}]", address);
													
													address = "<alias>" + selectedAlias;
													
													logger.info(className, function, "alias AF [{}]", address);
													
													WidgetStatus curStatusSet = uiWidgetGeneric.getWidgetStatus(strSet);
													boolean isApply = false;
													if ( WidgetStatus.Down == curStatusSet ) {
														isApply = true;
													}
													logger.info(className, function, "isApply[{}]", isApply);
						
													String key = "changeEqpStatus" + "_"+ className + "_"+ "manualoverride" + "_"+ isApply + "_" + address;
													
													String point = DatabaseHelper.getPointFromAliasAddress(address);
													logger.info(className, function, "address[{}] point[{}]", address, point);
													if ( null != point ) {
														PointType pointType = DatabaseHelper.getPointType(point);
														logger.info(className, function, "pointType[{}]", pointType);
														if ( pointType == PointType.dci ) {
															int value = lstValues.getSelectedIndex();
															logger.info(className, function, "scsEnvIdscsEnvIdvalue[{}]", value);
															dpcMgr.sendChangeVarForce(key, scsEnvId, address, isApply, value);
														} else if ( pointType == PointType.aci ) {
															float value = 0.0f;
															try {
																value = Float.parseFloat(txtValues.getText());
															} catch (NumberFormatException e) {
																logger.warn(className, function, "NumberFormatException[{}]", e.toString());
															}
															logger.info(className, function, "value[{}]", value);
															dpcMgr.sendChangeVarForce(key, scsEnvId, address, isApply, value);
														} else if ( pointType == PointType.sci ) {
															String value = txtValues.getText();
															logger.info(className, function, "value[{}]", value);
															dpcMgr.sendChangeVarForce(key, scsEnvId, address, isApply, value);
														} else {
															logger.warn(className, function, "dbaddress IS UNKNOW TYPE");
														}
													}
												} else {
													logger.warn(className, function, "scsEnvId OR address IS NULL");
												}

											}
					
										}
									}
									return true;
								}
							});
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
				
				logger.info(className, function, "os1["+os1+"]");
				
				if ( null != os1 ) {
					
					// Filter Action
					if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
						
						uiEventActionProcessor_i.executeActionSet(os1);
						
					} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
						
						uiEventActionProcessor_i.executeActionSet(os1);
					
					} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
						// Activate Selection
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						
						if ( null != obj1 ) {
							selectedSet	= (Set<HashMap<String, String>>) obj1;
							
							if ( null != selectedSet ) {
								String selectedStatus1 = null;
								for ( HashMap<String, String> hashMap : selectedSet ) {
									
									if ( null != hashMap ) {
										
										String selectedAlias = hashMap.get(columnAlias);
										String selectedServiceOwner = hashMap.get(columnServiceOwner);
										
										logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
										
										logger.info(className, function, "selectedAlias BF [{}]", selectedAlias);
										
										if ( ! selectedAlias.startsWith("<alias>") ) selectedAlias = "<alias>" + selectedAlias;
										
										logger.info(className, function, "selectedAlias AF [{}]", selectedAlias);
										
										scsEnvId = selectedServiceOwner;
										address = selectedAlias;

										selectedStatus1 = hashMap.get(columnStatus);
									} else {
										logger.warn(className, function, "hashMap IS NULL");
									}
									


								}
								
								if ( null != scsEnvId && null != address ) {
									
									readPointValueTable(scsEnvId, address);
									
									if ( null != selectedStatus1 ) {
										if ( valueSet.equals(selectedStatus1) ) {
											String actionsetkey = os1+"_valueUnset";
											uiEventActionProcessor_i.executeActionSet(actionsetkey);
										}
										if ( valueUnSet.equals(selectedStatus1) ) {
											String actionsetkey = os1+"_valueSet";
											uiEventActionProcessor_i.executeActionSet(actionsetkey);
										}
									}
								} else {
									logger.warn(className, function, "scsEnvId OR address IS NULL");
								}
										

							} else {
								logger.warn(className, function, "selectedSet IS NULL");
							}
							
						} else {
							logger.warn(className, function, "obj1 IS NULL");
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
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {

			}
			
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.begin(className, function);
				
				dpcMgr = DpcMgr.getInstance(className);
				
				String strDatabaseMultiReadingProxyKey = "DatabaseMultiReadingProxy";
				logger.debug(className, function, "strDatabaseMultiReadingProxyKey[{}]", strDatabaseMultiReadingProxyKey);
				if ( null == databaseMultiRead_i ) {
					databaseMultiRead_i = DatabaseMultiReadFactory.get(strDatabaseMultiReadingProxyKey);
					if ( null != databaseMultiRead_i ) {
						databaseMultiRead_i.connect();
					}
				} else {
					logger.warn(className, function, "databaseMultiRead_i IS NOT NULL");
				}
				logger.end(className, function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.begin(className, function);
				if ( null != databaseMultiRead_i ) {
					databaseMultiRead_i.disconnect();
					databaseMultiRead_i = null;
				}
				logger.end(className, function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(className, function);
				envDown(null);
				logger.end(className, function);
			}
		};
		
		logger.end(className, function);
	}
}
