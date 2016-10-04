package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEventHandler;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uiutil.client.UIWidgetUtil;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.DatabaseHelper;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.Database_i.PointName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.Database_i.PointType;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionBus;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionHandler;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIEventActionExecuteOld;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.common.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetDpcControl_i.ParameterName;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidget_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.event.UIWidgetEventOnClickHandler;

import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.UIWidgetGeneric;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.Database;
import com.thalesgroup.scadagen.wrapper.wrapper.client.db.DatabaseEvent;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;

public class UIWidgetDpcManualOverrideControl extends UIWidget_i {
	
	private final String className = UIWidgetUtil.getClassSimpleName(UIWidgetDpcManualOverrideControl.class.getName());
	private UILogger logger = UILoggerFactory.getInstance().getLogger(className);
	
	private SimpleEventBus eventBus 	= null;

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
	private final String strUnSet				= "unset";
	private final String strApply				= "apply";
	
	private ListBox lstValues					= null;
	private TextBox txtValues					= null;
	
	private String address = null;
	private String scsEnvId = null;
	
	private void onButton(ClickEvent event) {
		final String function = "onButton";
		
		logger.begin(className, function);
		
		if ( null != event ) {
			Widget widget = (Widget) event.getSource();
			if ( null != widget ) {
				String element = uiWidgetGeneric.getWidgetElement(widget);
				if ( null != element ) {
					
					String statusSet		= null;
					String statusUnSet	= null;
					String statusApply	= null;
					
					if ( element.equals(strSet) ) {

						statusSet				= WidgetStatus.Down.toString();
						statusUnSet				= WidgetStatus.Disable.toString();
						statusApply				= WidgetStatus.Up.toString();

					} else if ( element.equals(strUnSet) ) {
						
						statusSet				= WidgetStatus.Disable.toString();
						statusUnSet				= WidgetStatus.Down.toString();
						statusApply				= WidgetStatus.Up.toString();

					} else if ( element.equals(strApply) ) {

						statusSet				= WidgetStatus.Disable.toString();
						statusUnSet				= WidgetStatus.Disable.toString();
						statusApply				= WidgetStatus.Disable.toString();
						
						for ( HashMap<String, String> hashMap : selectedSet ) {
							String selectedAlias = hashMap.get(columnAlias);
							String selectedServiceOwner = hashMap.get(columnServiceOwner);
							
							logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
							
							scsEnvId = selectedServiceOwner;
							address = selectedAlias;
							
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
									float value = Float.parseFloat(txtValues.getText());
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

						}

					}

					UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
					
					uiWidgetGenericAction.action("SetWidgetStatus", strSet, statusSet);
					uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, statusUnSet);
					uiWidgetGenericAction.action("SetWidgetStatus", strApply, statusApply);
					
				}
			} else {
				logger.warn(className, function, "button IS NULL");
			}
		}
		
		logger.end(className, function);
	}


	private UIWidgetGeneric uiWidgetGeneric = null;
	
	void onUIEvent(UIEvent uiEvent ) {
	}
	
	@SuppressWarnings("unchecked")
	void onActionReceived(UIEventAction uiEventAction) {
		final String function = "onActionReceived";
		
		logger.begin(className, function);
		
		if ( null != uiEventAction ) {
			String op	= (String) uiEventAction.getParameter(ViewAttribute.Operation.toString());
		
			Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
			
			logger.info(className, function, "op[{}]", op);
			logger.info(className, function, "obj1[{}]", obj1);
			
			if ( null != op ) {
				
				String statusSet		= null;
				String statusUnSet	= null;
				String statusApply	= null;
				
				// Filter Action
				if ( op.equals(ViewerViewEvent.FilterAdded) || op.equals(ViewerViewEvent.FilterRemoved) ) {
					
					statusSet				= WidgetStatus.Disable.toString();
					statusUnSet				= WidgetStatus.Disable.toString();
					statusApply				= WidgetStatus.Disable.toString();
					
				} else if ( op.equals(ViewerViewEvent.RowSelected.toString() ) ) {
					// Activate Selection
					
					selectedSet	= (Set<HashMap<String, String>>) obj1;
					
					String selectedStatus1 = null;
					for ( HashMap<String, String> hashMap : selectedSet ) {
						String selectedAlias = hashMap.get(columnAlias);
						String selectedServiceOwner = hashMap.get(columnServiceOwner);
						
						logger.info(className, function, "selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
						
						logger.info(className, function, "selectedAlias BF [{}]", selectedAlias);
						
						if ( ! selectedAlias.startsWith("<alias>") ) selectedAlias = "<alias>" + selectedAlias;
						
						logger.info(className, function, "selectedAlias AF [{}]", selectedAlias);
						
						scsEnvId = selectedServiceOwner;
						address = selectedAlias;

						selectedStatus1 = hashMap.get(columnStatus);

					}
					
					connect();
	
					if ( null != selectedStatus1 ) {
						if ( valueUnSet.equals(selectedStatus1) ) {
							statusSet				= WidgetStatus.Up.toString();
							statusUnSet				= WidgetStatus.Disable.toString();
						}
						if ( valueSet.equals(selectedStatus1) ) {
							statusSet				= WidgetStatus.Disable.toString();
							statusUnSet				= WidgetStatus.Up.toString();
						}
					}
					
					statusApply				= WidgetStatus.Disable.toString();
				} else {
					logger.warn(className, function, "op[{}] type IS UNKNOW", op);
				}
	
				UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
				
				uiWidgetGenericAction.action("SetWidgetStatus", strSet, statusSet);
				uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, statusUnSet);
				uiWidgetGenericAction.action("SetWidgetStatus", strApply, statusApply);
			}
		} else {
			logger.warn(className, function, "uiEventAction IS NULL");
		}
		
		logger.end(className, function);
	}
	
	// Static Attribute List
	private final String staticDciAttibutes []	= new String[] {PointName.label.toString(), PointName.dalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticAciAttibutes []	= new String[] {PointName.label.toString(), PointName.aalValueTable.toString(), PointName.hmiOrder.toString()};
	private final String staticSciAttibutes []	= new String[] {PointName.label.toString(), PointName.salValueTable.toString(), PointName.hmiOrder.toString()};
	
	// Dynamic Attribute List
	private final String dynamicDciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.dfoForcedStatus.toString()};
	private final String dynamicAciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.afoForcedStatus.toString()};
	private final String dynamicSciAttibutes []	= new String[] {PointName.value.toString(), PointName.validity.toString(), PointName.sfoForcedStatus.toString()};

	private Database database = Database.getInstance();
	public void connect() {
		final String function = "connect";
		
		logger.begin(className, function);

		// Read static
		{
			logger.begin(className, function);
			
			String clientKey = "multiReadValue" + "_" + className + "_" + "static" + "_" + address;
			
			logger.info(className, function, "clientkey[{}]", clientKey);

			String[] dbaddresses = null;
			{
				ArrayList<String> dbaddressesArrayList = new ArrayList<String>();
//				for ( String dbaddress : this.addresses ) {
				String dbaddress = address;
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
//				}
				dbaddresses = dbaddressesArrayList.toArray(new String[0]);
			}			
			
			if (logger.isDebugEnabled() ) {
				logger.debug(className, function, "key[{}] scsEnvId[{}]", clientKey, scsEnvId);
				for(int i = 0; i < dbaddresses.length; ++i ) {
					logger.debug(className, function, "dbaddresses({})[{}]", i, dbaddresses[i]);
				}
			}
			
			String api = "multiReadValue";
			database.addStaticRequest(api, clientKey, scsEnvId, dbaddresses, new DatabaseEvent() {
				
				@Override
				public void update(String key, String[] value) {
					Database database = Database.getInstance();
					String clientKeyStatic = "multiReadValue" + "_" + className + "_" + "static" + "_" + address;
					if ( clientKeyStatic.equals(key) ) {
						String [] dbaddresses	= database.getKeyAndAddress(key);
						String [] dbvalues		= database.getKeyAndValues(key);
						HashMap<String, String> keyAndValue = new HashMap<String, String>();
						for ( int i = 0 ; i < dbaddresses.length ; ++i ) {
							keyAndValue.put(dbaddresses[i], dbvalues[i]);
						}

						updateValue(key, keyAndValue);
					}
				}
			});
			
			logger.end(className, function);
		}
	}
	
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesStatic	= new LinkedHashMap<String, HashMap<String, String>>();
	private LinkedHashMap<String, HashMap<String, String>> keyAndValuesDynamic	= new LinkedHashMap<String, HashMap<String, String>>();
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValue";

		logger.begin(className, function);
		logger.debug(className, function, "clientkey[{}]", clientKey);
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
	
		if ( "static".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesStatic.put(clientKey, keyAndValue);
			
			updateValue(true);
			
		} else if ( "dynamic".equals(clientKey.split("_")[2]) ) {
			
			keyAndValuesDynamic.put(clientKey, keyAndValue);
			
			updateValue(false);
			
		}

		logger.end(className, function);
	}
	
	private void updateValue(boolean withStatic) {
		final String function = "updateValue";
		
		logger.begin(className, function);
		
		if ( withStatic ) {
			for ( String clientKey : keyAndValuesStatic.keySet() ) {
				
				HashMap<String, String> keyAndValue = keyAndValuesStatic.get(clientKey);
				
				updateValueStatic(clientKey, keyAndValue);
			}//End of for keyAndValuesStatic
		}
	
//		for ( String clientKey : keyAndValuesDynamic.keySet() ) {
//			
//			HashMap<String, String> keyAndValue = keyAndValuesDynamic.get(clientKey);
//			
//			updateValueDynamic(clientKey, keyAndValue);
//			
//		}// End of keyAndValuesDynamic
		
		logger.end(className, function);
	}
	
	public void updateValueStatic(String clientKey, HashMap<String, String> keyAndValue) {
		final String function = "updateValueStatic";
		
		logger.begin(className, function);
		logger.info(className, function, "clientkey[{}]", clientKey);
		
//		valueRefreshed = false;
		
//		pageCounter.calc(pageIndex);
//		
//		int rowBegin	= pageCounter.pageRowBegin;
//		int rowEnd		= pageCounter.pageRowEnd;
		
		String clientKeyStatic = "multiReadValue" + "_" + className + "_" + "static" + "_" + address;
		
		logger.info(className, function, "clientKeyStatic[{}]", clientKeyStatic);
		
		if ( clientKeyStatic.equals(clientKey) ) {
	
//			for ( int x = rowBegin, y = 0 ; x < rowEnd ; ++x, ++y ) {
//				String address = addresses[x];

				// Update the Label
//				String label = DatabaseHelper.getAttributeValue(address, PointName.label.toString(), dbvalues);
//				label = DatabaseHelper.removeDBStringWrapper(label);
//				logger.debug(className, function, "label[{}]", label);
//
//				lblAttibuteLabel.setText(label);
				
				// ACI, SCI Show the TextBox
				// DCI, Show the ListBox and store the valueTable

				String point = DatabaseHelper.getPointFromAliasAddress(address);
				PointType pointType = DatabaseHelper.getPointType(point);
				
				if ( PointType.dci == pointType ) {
					
					txtValues.setVisible(false);
					lstValues.setVisible(true);
					
					// Update the Label
					String valueTable = DatabaseHelper.getAttributeValue(address, PointName.dalValueTable.toString(), dbvalues);
					logger.debug(className, function, "valueTable[{}]", valueTable);
					
					if ( null != valueTable ) {
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
					
					txtValues.setVisible(true);
					lstValues.setVisible(false);

					// Update the Label
					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.debug(className, function, "value[{}]", value);
					
					txtValues.setText(value);
					
				} else if ( PointType.sci == pointType ) {
					
					txtValues.setVisible(true);
					lstValues.setVisible(false);

					String value = DatabaseHelper.getAttributeValue(address, PointName.value.toString(), dbvalues);
					value = DatabaseHelper.removeDBStringWrapper(value);
					logger.debug(className, function, "value[{}]", value);
					
					txtValues.setText(value);
					
				}
//			}
			
		}
		
		logger.end(className, function);
	}
	
	@Override
	public void init() {
		final String function = "init";
		
		logger.begin(className, function);
		
		dpcMgr = DpcMgr.getInstance("almmgn");
		
		String strEventBusName = getStringParameter(ParameterName.SimpleEventBus.toString());
		if ( null != strEventBusName ) this.eventBus = UIEventActionBus.getInstance().getEventBus(strEventBusName);
		logger.info(className, function, "strEventBusName[{}]", strEventBusName);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnAlias.toString(), strHeader);
			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnStatus.toString(), strHeader);
			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ColumnServiceOwner.toString(), strHeader);
			valueSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueSet.toString(), strHeader);
			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, ParameterName.ValueUnSet.toString(), strHeader);
		}
		
		logger.info(className, function, "columnAlias[{}]", columnAlias);
		logger.info(className, function, "columnStatus[{}]", columnStatus);
		logger.info(className, function, "columnServiceOwner[{}]", columnServiceOwner);
		logger.info(className, function, "valueSet[{}]", valueSet);
		logger.info(className, function, "valueUnSet[{}]", valueUnSet);
		
		uiWidgetGeneric = new UIWidgetGeneric();
		uiWidgetGeneric.setUINameCard(this.uiNameCard);
		uiWidgetGeneric.setViewXMLFile(viewXMLFile);
		uiWidgetGeneric.setOptsXMLFile(optsXMLFile);
		uiWidgetGeneric.init();
		
		lstValues = (ListBox) uiWidgetGeneric.getWidget(strLstValue);
		txtValues = (TextBox) uiWidgetGeneric.getWidget(strTxtValue);
		
		uiWidgetGeneric.setUIWidgetEvent(new UIWidgetEventOnClickHandler() {
			@Override
			public void onClickHandler(ClickEvent event) {
				onButton(event);
			}
		});
		
		rootPanel = uiWidgetGeneric.getMainPanel();

		handlerRegistrations.add(
			this.uiNameCard.getUiEventBus().addHandler(UIEvent.TYPE, new UIEventHandler() {
				@Override
				public void onEvenBusUIChanged(UIEvent uiEvent) {
					if ( uiEvent.getSource() != this ) {
						onUIEvent(uiEvent);
					}
				}
			})
		);

		handlerRegistrations.add(
			this.eventBus.addHandler(UIEventAction.TYPE, new UIEventActionHandler() {
				@Override
				public void onAction(UIEventAction uiEventAction) {
					if ( uiEventAction.getSource() != this ) {
						onActionReceived(uiEventAction);
					}
				}
			})
		);

		UIEventActionExecuteOld uiWidgetGenericAction = new UIEventActionExecuteOld(className, uiWidgetGeneric);
		
		uiWidgetGenericAction.action("SetWidgetStatus", strSet, "Disable");
		uiWidgetGenericAction.action("SetWidgetStatus", strUnSet, "Disable");
		uiWidgetGenericAction.action("SetWidgetStatus", strApply, "Disable");
		
		txtValues.setVisible(false);
		lstValues.setVisible(false);
		
		logger.end(className, function);
	}

}
