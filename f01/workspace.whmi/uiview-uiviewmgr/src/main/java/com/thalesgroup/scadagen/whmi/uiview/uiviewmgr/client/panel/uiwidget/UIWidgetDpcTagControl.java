package com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget;

import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import com.thalesgroup.scadagen.whmi.config.configenv.client.DictionariesCache;
import com.thalesgroup.scadagen.whmi.uievent.uievent.client.UIEvent;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILoggerFactory;
import com.thalesgroup.scadagen.whmi.uiutil.uilogger.client.UILogger_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.ActionAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIActionEventAttribute_i.UIActionEventTargetAttribute;
import com.thalesgroup.scadagen.whmi.uiview.uiviewmgr.client.panel.uiwidget.UIWidgetViewer_i.ViewerViewEvent;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIExecuteActionHandler_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UILayoutSummaryAction_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIView_i.ViewAttribute;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetCtrl_i;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIEventAction;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidget.client.UIWidgetGeneric_i.WidgetStatus;
import com.thalesgroup.scadagen.whmi.uiwidget.uiwidgetgeneric.client.realize.UIWidgetRealize;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DCP_i.TaggingStatus;
import com.thalesgroup.scadagen.wrapper.wrapper.client.dpc.DpcMgr;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriorityFactory;
import com.thalesgroup.scadagen.wrapper.wrapper.client.opm.controlpriority.UIControlPriority_i;

public class UIWidgetDpcTagControl extends UIWidgetRealize {
	
	private final String className = this.getClass().getSimpleName();
	private UILogger_i logger = UILoggerFactory.getInstance().getUILogger(this.getClass().getName());
	
	private DpcMgr dpcMgr				= null;
	
	private String columnAlias			= "";
	private String columnStatus			= "";
	private String columnServiceOwner	= "";
	private String columnOpSource		= "";
	
	private String valueOpSourceEmpty		= "";
	
	private String valueSet				= "";
	private String valueUnSet			= "";
	
	private String uiCpApi				= "";
	
	private final String strEMPTY				= "";
	
	private final String strSet					= "set";
//	private final String strUnSet				= "unset";
//	private final String strApply				= "apply";
	
	private final String strTextValue			= "txtvalue";

	private Set<Map<String, String>> selectedRowList = null;
	
	private int isTagged(String selectedStatus) {
		final String f = "isTagged";
		int ret = UIWidgetDpcTagControl_i.TAG_UNKNOW;
		logger.debug(f, "selectedStatus[{}] valueSet[{}] valueUnSet[{}]", new Object[]{selectedStatus, valueSet, valueUnSet});
		if ( valueUnSet.equals(selectedStatus) ) {
			ret = UIWidgetDpcTagControl_i.TAG_DEACTIVATED;
		} else if ( valueSet.equals(selectedStatus) ) {
			ret = UIWidgetDpcTagControl_i.TAG_ACTIVATED;
		}
		logger.debug(f, "ret[{}]", ret);
		return ret;
	}
	
	private boolean controlPriorityIsValid(UIControlPriority_i cp, String selectedOpSource) {
		final String f = "controlPriorityIsValid";
		boolean ret = false;

		logger.debug(f, "selectedOpSource[{}]", selectedOpSource);
		if(null!=selectedOpSource) {
			if(null!=valueOpSourceEmpty&&0==valueOpSourceEmpty.compareTo(selectedOpSource)) {
				// Column is empty
				ret = true;
				logger.debug(f, "selectedOpSource[{}] set as IS Empty[{}], Mark as Empty", selectedOpSource, valueOpSourceEmpty);
			} else {
				// Column contain value, try to check reservation availability
				switch(cp.checkReservationAvailability(selectedOpSource)) {
					case UIControlPriority_i.AVAILABILITY_DENIED:
					case UIControlPriority_i.AVAILABILITY_EQUAL:
						ret = false;
						break;
					case UIControlPriority_i.AVAILABILITY_RESERVED_BYSELF:
					case UIControlPriority_i.AVAILABILITY_EMPTY:
					case UIControlPriority_i.AVAILABILITY_ALLOW_WITH_OVERRIDE:
						ret = true;
						break;
					default:
						ret = false;
						break;
				}
			}
		} else {
			logger.warn(f, "selectedOpSource[{}] IS NULL", selectedOpSource);
		}
		logger.debug(f, "ret[{}]", ret);
		return ret;
	}
		
	@Override
	public void init() {
		super.init();
		
		final String f = "init";
		logger.begin(f);
		
		dpcMgr = DpcMgr.getInstance(className);

		String strUIWidgetGeneric = "UIWidgetGeneric";
		String strHeader = "header";
		DictionariesCache dictionariesCache = DictionariesCache.getInstance(strUIWidgetGeneric);
		if ( null != dictionariesCache ) {
			columnAlias			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ColumnAlias.toString(), strHeader);
			columnStatus		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ColumnStatus.toString(), strHeader);
			columnServiceOwner	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ColumnServiceOwner.toString(), strHeader);
			columnOpSource		= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ColumnOpSource.toString(), strHeader);
			
			valueSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ValueSet.toString(), strHeader);
			valueUnSet			= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ValueUnSet.toString(), strHeader);
			valueOpSourceEmpty	= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.ValueOpSourceEmpty.toString(), strHeader);
			
			uiCpApi				= dictionariesCache.getStringValue(optsXMLFile, UIWidgetDpcTagControl_i.ParameterName.UICpApi.toString(), strHeader);
		}
		
		logger.debug(f, "columnAlias[{}] columnStatus[{}] columnServiceOwner[{}] columnOpSource[{}]", new Object[]{columnAlias, columnStatus, columnServiceOwner, columnOpSource});
		logger.debug(f, "valueSet[{}] valueUnSet[{}]", new Object[]{valueSet, valueUnSet});
		
		logger.debug(f, "valueOpSourceEmpty[{}]", new Object[]{valueOpSourceEmpty});
		
		
		logger.debug(f, "uiCpApi[{}]", new Object[]{uiCpApi});
		
		uiWidgetCtrl_i = new UIWidgetCtrl_i() {
			
			@Override
			public void onUIEvent(UIEvent uiEvent) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClick(ClickEvent event) {
				final String f = "onClick";
				
				logger.begin(f);
				
				if ( null != event ) {
					Widget widget = (Widget) event.getSource();
					if ( null != widget ) {
						String element = uiGeneric.getWidgetElement(widget);
						logger.debug(f, "element[{}]", element);
						if ( null != element ) {
							String actionsetkey = element;
							
							Map<String, Map<String, Object>> override = null;
							
							uiEventActionProcessor_i.executeActionSet(actionsetkey, override, new UIExecuteActionHandler_i() {
								
								@Override
								public boolean executeHandler(UIEventAction uiEventAction) {
									String os1 = (String) uiEventAction.getParameter(ActionAttribute.OperationString1.toString());
									String os2 = (String) uiEventAction.getParameter(ActionAttribute.OperationString2.toString());
									
									logger.debug(f, "os1[{}]", os1);
									
									if ( null != os1 ) {
										if ( os1.equals("SendDpcTagControl") ) {
											
											if(null!=selectedRowList) {
												for ( Map<String, String> columnList : selectedRowList ) {
													String selectedAlias		= columnList.get(columnAlias);
													String selectedServiceOwner	= columnList.get(columnServiceOwner);
													String selectedOpSource		= columnList.get(columnOpSource);
													String selectedStatus 		= columnList.get(columnStatus);
													logger.debug(f, "selectedAlias[{}] selectedServiceOwner[{}] selectedStatus[{}]", new Object[]{selectedAlias, selectedServiceOwner, selectedStatus});
													
													WidgetStatus curStatusSet = uiGeneric.getWidgetStatus(strSet);
													
													TaggingStatus taggingStatus = TaggingStatus.NO_TAGGING;
													if ( WidgetStatus.Down == curStatusSet ) {
														taggingStatus = TaggingStatus.TAGGING_1;
														if ( null != os2 && os2.length() > 0 ) {
															if ( os2.equalsIgnoreCase(
																	Integer.toString(TaggingStatus.ALL_TAGGING.getValue()) ) ) {
																taggingStatus = TaggingStatus.ALL_TAGGING;
															} else if ( os2.equalsIgnoreCase(
																	Integer.toString(TaggingStatus.TAGGING_1.getValue()) ) ) {
																taggingStatus = TaggingStatus.TAGGING_1;
															} else if ( os2.equalsIgnoreCase(
																	Integer.toString(TaggingStatus.TAGGING_2.getValue()) ) ) {
																taggingStatus = TaggingStatus.TAGGING_2;
															}
														}
													}
													
													int tagged = isTagged(selectedStatus);
													logger.debug(f, "tagged[{}]", tagged);
													
													boolean valid = false;
													if(UIWidgetDpcTagControl_i.TAG_DEACTIVATED==tagged) {
														valid = true;
														logger.debug(f, "tagged[{}], skip CP checking valid[{}]", tagged, valid);
													} else if(null==columnOpSource) {
														valid = true;
														logger.debug(f, "columnOpSource[{}] IS NULL, Mask VALID[{}] as true", columnOpSource, valid);
													} else {
														UIControlPriority_i cp = UIControlPriorityFactory.getInstance().get(uiCpApi);
														if(null==cp) {
															logger.warn(f, "uiCpApi[{}] IS NULL", uiCpApi);
														} else {
															valid = controlPriorityIsValid(cp, selectedOpSource);
															logger.warn(f, "after controlPriorityIsValid valid[{}]", valid);
														}
													}
														
													if(!valid) {
														logger.warn(f, "invalid control priority checking for selectedAlias[{}] selectedServiceOwner[{}]", selectedAlias, selectedServiceOwner);
													} else {
														
														String scsEnvId = selectedServiceOwner;
														String alias = selectedAlias;
														
														logger.debug(f, "alias BF [{}]", alias);
														if ( ! selectedAlias.startsWith("<alias>") ) alias = "<alias>" + selectedAlias;
														logger.debug(f, "alias AF [{}]", alias);
														

														String key = "changeEqpStatus" + "_" + className + "_"+ "alarminhibit" + "_" + taggingStatus.toString() + "_" + alias;
															
														String taggingLabel1 = strEMPTY;
														String taggingLabel2 = strEMPTY;
														
//														if ( null != os2 && os2.length() > 0 ) taggingLabel1 = os2;
//														if ( null != os3 && os3.length() > 0 ) taggingLabel2 = os3;
														
														taggingLabel1 = uiGeneric.getWidgetValue(strTextValue);
														
														logger.debug(f, "key[{}]", key);
														
														dpcMgr.sendChangeEqpTag(key, scsEnvId, alias, taggingStatus, taggingLabel1, taggingLabel2);
													}
												}
												uiGeneric.setWidgetValue(strTextValue, strEMPTY);
											} else {
												logger.warn(f, "selectedRowList IS NULL");
											}
										}
									}
									return true;
								}
							});
						}
					}
				}
				logger.end(f);
			}
			
			@Override
			@SuppressWarnings("unchecked")
			public void onActionReceived(UIEventAction uiEventAction) {
				final String f = "onActionReceived";
				
				logger.begin(f);
				
				String os1	= (String) uiEventAction.getParameter(ViewAttribute.OperationString1.toString());
				
				logger.debug(f, "os1[{}]", os1);
				
				if ( null != os1 ) {
					// Filter Action
					if ( os1.equals(ViewerViewEvent.FilterAdded.toString()) ) {
						
						logger.debug(f, "FilterAdded");
						
						uiEventActionProcessor_i.executeActionSet(os1);
						
					} else if ( os1.equals(ViewerViewEvent.FilterRemoved.toString()) ) {
						
						logger.debug(f, "FilterRemoved");
						
						uiEventActionProcessor_i.executeActionSet(os1);
					
					} else if ( os1.equals(ViewerViewEvent.RowSelected.toString() ) ) {
						// Activate Selection
						
						Object obj1 = uiEventAction.getParameter(ViewAttribute.OperationObject1.toString());
						
						logger.debug(f, "Store Selected Row");
						
						String actionsetkey = os1+"_disable";
						
						if(null==obj1) {
							logger.warn(f, "obj1 IS NULL");
							
						} else {
							selectedRowList	= (Set<Map<String, String>>) obj1;
							
							String selectedStatus = null;
							for ( Map<String, String> columnList : selectedRowList ) {
								selectedStatus = columnList.get(columnStatus);
							}
							logger.debug(f, "selectedStatus[{}]", selectedStatus);
							
							int tagged = isTagged(selectedStatus);
							logger.debug(f, "tagged[{}]", tagged);

							boolean valid = false;
							
							int selectedRowListSize = selectedRowList.size();
							logger.debug(f, "selectedRowListSize[{}]", selectedRowListSize);
							
							// Control Priority Checking
							if(selectedRowListSize==1) {
								if(UIWidgetDpcTagControl_i.TAG_DEACTIVATED==tagged) {
									valid = true;
									logger.warn(f, "tagged[{}], Skip CP Check valid[{}]", tagged, valid);
								} else {
									for ( Map<String, String> columnList : selectedRowList ) {
										
										String selectedOpSource = columnList.get(columnOpSource);
										logger.debug(f, "selectedOpSource[{}]", selectedOpSource);
										
										if(null==columnOpSource) {
											valid = true;
											logger.debug(f, "columnOpSource[{}] IS NULL, Mask VALID[{}] as true", columnOpSource, valid);
										} else {
											UIControlPriority_i cp = UIControlPriorityFactory.getInstance().get(uiCpApi);
											if(null==cp) {
												logger.warn(f, "uiCpApi[{}] IS NULL", uiCpApi);
											} else {
												valid = controlPriorityIsValid(cp, selectedOpSource);
											}
										}
										if(!valid) {
											logger.warn(f, "valid[{}] IS FALSE, break", valid);
											break;
										}
									}
								}
							} else if(selectedRowListSize==0){
								// Size is zero
								valid = false;
							} else {
								// Size of list more than 1
								valid = true;;
							}
							
							if(valid) {
								// Control Priority Passed
								if ( UIWidgetDpcTagControl_i.TAG_ACTIVATED==tagged ) {
									actionsetkey = os1+"_valueUnset";
								} else if ( UIWidgetDpcTagControl_i.TAG_DEACTIVATED==tagged ) {
									actionsetkey = os1+"_valueSet";
								}
							}
						}

						logger.debug(f, "actionsetkey[{}]", actionsetkey);
						uiEventActionProcessor_i.executeActionSet(actionsetkey);
			
					} else {
						// General Case
						String oe	= (String) uiEventAction.getParameter(UIActionEventTargetAttribute.OperationElement.toString());
						
						logger.debug(f, "oe [{}]", oe);
						logger.debug(f, "os1[{}]", os1);
						
						if ( null != oe ) {
							if ( oe.equals(element) ) {
								
								Map<String, Map<String, Object>> override = null;
								
								uiEventActionProcessor_i.executeActionSet(os1, override);
							}
						}
					}
				}
				logger.end(f);
			}
		};
		
		uiLayoutSummaryAction_i = new UILayoutSummaryAction_i() {
			
			@Override
			public void init() {
				final String function = "init";
				logger.beginEnd(function);
			}
		
			@Override
			public void envUp(String env) {
				final String function = "envUp";
				logger.beginEnd(function);
			}
			
			@Override
			public void envDown(String env) {
				final String function = "envDown";
				logger.beginEnd(function);
			}
			
			@Override
			public void terminate() {
				final String function = "terminate";
				logger.begin(function);
				envDown(null);
				logger.begin(function);
			};
		};

		logger.end(f);
	}

}
