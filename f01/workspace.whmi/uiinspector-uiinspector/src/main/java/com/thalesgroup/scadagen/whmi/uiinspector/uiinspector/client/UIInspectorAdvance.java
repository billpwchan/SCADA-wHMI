package com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.thalesgroup.hypervisor.mwt.core.webapp.core.ui.client.panel.IClientLifeCycle;
import com.thalesgroup.scadagen.whmi.uiinspector.uiinspector.client.UIInspectorTab_i;
import com.thalesgroup.scadagen.whmi.uinamecard.uinamecard.client.UINameCard;

public class UIInspectorAdvance implements UIInspectorTab_i, IClientLifeCycle {
	
	private static Logger logger = Logger.getLogger(UIInspectorAdvance.class.getName());
	
	String tagname								= "advance";

	// Static Attribute
	private final String strLabel				= ".label";
	private final String strValueTable			= ":dal.valueTable";
	private final String strHmiOrder			= ".hmiOrder";
	
	// Dynamic Attribute
	private final String strValue				= ".value";
	private final String strForcedStatus		= ":dfo.forcedStatus"; // 2=MO, AI=8, 512=SS //dfo.forcedStatus

	// Static Attribute List
	private String staticAttibutes [] = new String[] {strLabel, strValueTable, strHmiOrder};

	// Dynamic Attribute List
	private String dynamicAttibutes [] = new String[] {strValue, strForcedStatus};

	private String scsEnvId		= null;
	private String parent		= null;
	private String[] addresses	= null;
	
	@Override
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	@Override
	public void setAddresses(String scsEnvId, String[] addresses) {
		logger.log(Level.FINE, "setAddresses Begin");
		
		this.scsEnvId = scsEnvId;
		
		logger.log(Level.FINE, "setConnection this.scsEnvId["+this.scsEnvId+"]");
		
		this.addresses = addresses;
		
		logger.log(Level.FINE, "setAddresses End");
	}

	private DpcMgr dpcAccess = null;
//	private Observer observer = null;
//	private Subject dpcMgrSubject = null;
	@Override
	public void connect() {
		logger.log(Level.FINE, "connect Begin");
		
		dpcAccess = DpcMgr.getInstance();
//		dpcMgrSubject = DpcMgr.getSubject();
//		
//		observer = new Observer() {
//			@Override
//			public void setSubject(Subject subject){
//				this.subject = subject;
//				this.subject.attach(this);
//			}
//			
//			@Override
//			public void update() {
//
//			}
//		};
//		
//		observer.setSubject(controlMgrSubject);
		
		logger.log(Level.FINE, "connect End");
	}
	
	@Override
	public void disconnect() {
		logger.log(Level.FINE, "disconnect Begin");
		
		logger.log(Level.FINE, "disconnect End");
	}

	@Override
	public void buildWidgets() {
		buildWidgets(this.addresses.length);
	}

	String strApply = "Apply";
	String strCancel = "Cancel";
	
	String strInhibit = "Inhibit";
	String strSuspend = "Suspend";

	String strRestore = "Restore";
	String strOverride = "Override";
	
	private VerticalPanel[] widgetBoxes			= null;
	
	private InlineLabel[] lblAttibuteLabel		= null;
	private ListBox[] lstValues					= null;
	private TextBox[] txtValues					= null;
	
	private CheckBox[][] chkDPMs 				= null;
	
	private UIButtonToggle[] btnApplys			= null;
	
	private void buildWidgets(int numOfWidgets) {
		
		logger.log(Level.SEVERE, "updateDisplay Begin");
		
		if ( null != vpCtrls ) {
			
			vpCtrls.clear();
			
			if ( RTDB_Helper.addressesIsValid(this.addresses) ) {
				
				lblAttibuteLabel	= new InlineLabel[addresses.length];
				
				lstValues			= new ListBox[addresses.length];
				txtValues			= new TextBox[addresses.length];
				
				chkDPMs				= new CheckBox[addresses.length][3];
				
				btnApplys			= new UIButtonToggle[addresses.length];

				flexTableAttibutes = new FlexTable();
				flexTableAttibutes.setWidth("100%");
				
				{
					String [] header = new String[] {"Equipment Point", "AI", "SS", "MO", "Value", ""};
					
					for ( int i = 0 ; i < header.length ; ++i ) {
						InlineLabel label = new InlineLabel();
						label.setWidth("100%");
						label.addStyleName("project-gwt-inlinelabel-inspector-advance-header-label");
						label.setText(header[i]);
						flexTableAttibutes.setWidget(1, i, label);
					}
				}
								
				for( int i = 0 ; i < numOfWidgets ; ++i ) {
					
					int r = 0;
					
					logger.log(Level.FINE, "buildWidgets i["+i+"]");
						
					lblAttibuteLabel[i] = new InlineLabel();
					lblAttibuteLabel[i].setWidth("100%");
					lblAttibuteLabel[i].addStyleName("project-gwt-inlinelabel-inspector-advance-label");
					lblAttibuteLabel[i].setText("ATTRIBUTE_LABEL_"+(i+1)+":");
					flexTableAttibutes.setWidget(i+1+1, r++, lblAttibuteLabel[i]);
					
					chkDPMs[i] = new CheckBox[3];
					chkDPMs[i][0] = new CheckBox();
					chkDPMs[i][0].addStyleName("project-gwt-checkbox-inspector-advance-points-ai");
					
					flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][0]);
					
					chkDPMs[i][1] = new CheckBox();
					chkDPMs[i][1].addStyleName("project-gwt-checkbox-inspector-advance-points-ss");
					
					flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][1]);
					
					chkDPMs[i][2] = new CheckBox();
					chkDPMs[i][2].addStyleName("project-gwt-checkbox-inspector-advance-points-mo");
					
					flexTableAttibutes.setWidget(i+1+1, r++, chkDPMs[i][2]);
					
					lstValues[i] = new ListBox();
					lstValues[i].setVisibleItemCount(1);
					lstValues[i].addStyleName("project-gwt-listbox-inspector-advance-points-value");

					txtValues[i] = new TextBox();
					txtValues[i].setVisible(false);
					txtValues[i].addStyleName("project-gwt-textbox-inspectoradvance-points-value");

					HorizontalPanel hp = new HorizontalPanel();
					hp.add(lstValues[i]);
					hp.add(txtValues[i]);
					
					flexTableAttibutes.setWidget(i+1+1, r++, hp);
					
					btnApplys[i] = new UIButtonToggle(strApply);
					btnApplys[i].addStyleName("project-gwt-button-inspectoradvance-point-apply");
					
					btnApplys[i].addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {

							logger.log(Level.SEVERE, "buildWidgets onClick Begin");
							
							sendControl(event);
							
							logger.log(Level.SEVERE, "buildWidgets onClick End");
						}
					});
					flexTableAttibutes.setWidget(i+1+1, r++, btnApplys[i]);

				}
				
				for ( int i = 0 ; i < 8 ; ++i ) {
					flexTableAttibutes.getColumnFormatter().addStyleName(i, "project-gwt-flextable-inspector-advance-status-col"+i);
				}

			} else {
				logger.log(Level.FINE, "buildWidgets this.pointStatics IS NULL");
			}
			
			vpCtrls.add(flexTableAttibutes);

		} else {
			logger.log(Level.SEVERE, "updateDisplay points IS NULL");
		}
		
		logger.log(Level.SEVERE, "updateDisplay End");
	}
	
	private boolean valueRefreshed = false;
	private HashMap<String, String> dbvalues = new HashMap<String, String>();
	public void updateValue(String clientKey, HashMap<String, String> keyAndValue) {

		logger.log(Level.SEVERE, "updateValue Begin");
		logger.log(Level.SEVERE, "updateValue clientkey["+clientKey+"]");
		
		for ( String key : keyAndValue.keySet() ) {
			dbvalues.put(key, keyAndValue.get(key));
		}
		
		for ( int i = 0 ; i < this.addresses.length ; ++i ) {
			logger.log(Level.FINE, "updateValue addresses("+i+")["+addresses[i]+"]");
		}
		
		for ( String key : dbvalues.keySet() ) {
			logger.log(Level.FINE, "updateValue dbvalues.get("+key+")["+dbvalues.get(key)+"]");
		}

		String clientKey_multiReadValue_inspectorinfo_static = "multiReadValue" + "inspectorinfo" + "static" + parent;
		
		logger.log(Level.SEVERE, "updateValue clientKey_multiReadValue_inspectorinfo_static["+clientKey_multiReadValue_inspectorinfo_static+"]");
		
		if ( 0 == clientKey_multiReadValue_inspectorinfo_static.compareTo(clientKey) ) {
			
			valueRefreshed = false;

			for ( int i = 0 ; i < this.addresses.length ; ++i ) {
				String address = this.addresses[i];
				
				// Update the Label
				{
					String label = null;
					{
						String dbaddress = address + strLabel;
						if ( dbvalues.containsKey(dbaddress) ) {
							label = dbvalues.get(dbaddress);
							label = RTDB_Helper.removeDBStringWrapper(label);
						}
					}
					logger.log(Level.SEVERE, "updateValue label["+label+"]");
					
					// Set the Label
					lblAttibuteLabel[i].setText(label);
				}
				
				// ACI, SCI Show the TextBox
				// DCI, Show the ListBox and store the valueTable

				String point = RTDB_Helper.getPoint(address);
				String type = RTDB_Helper.getPointType(point);
				
				if ( 0 != type.compareTo("dci") ) {
					
					txtValues[i].setVisible(true);
					lstValues[i].setVisible(false);

				} else if ( 0 == type.compareTo("dci") ) {
					
					txtValues[i].setVisible(false);
					lstValues[i].setVisible(true);
					
					String valueTable = null;
					{
						String dbaddress = address + strValueTable;
						logger.log(Level.SEVERE, "updateValue strValueTable["+strValueTable+"] dbaddress["+dbaddress+"]");
						if ( dbvalues.containsKey(dbaddress) ) {
							valueTable = dbvalues.get(dbaddress);
						} else {
							logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
						}
					}
					logger.log(Level.SEVERE, "updateValue valueTable["+valueTable+"]");
					
					if ( null != valueTable ) {
						String names[]	= new String[12];
						String values[]	= new String[12];
						{
							for( int r = 0 ; r < 12 ; ++r ) {
								values[r]	= RTDB_Helper.getArrayValues(valueTable, 4, r );
								values[r]	= RTDB_Helper.removeDBStringWrapper(values[r]);
								names[r]	= RTDB_Helper.getArrayValues(valueTable, 1, r );
								names[r]	= RTDB_Helper.removeDBStringWrapper(names[r]);
							}					
						}
						
						lstValues[i].clear();
						for( int r = 0 ; r < 12 ; ++r ) {
							
							if ( 0 == names[r].compareTo("") ) break;
							
							lstValues[i].addItem(names[r]);
							
							logger.log(Level.SEVERE, "updateValue names["+r+"]["+names[r]+"] values["+r+"]["+values[r]+"]");
						}
					} else {
						logger.log(Level.SEVERE, "updateValue valueTable IS NULL!");
					}
				}
			}
			
		} else {
			
			for ( int x = 0 ; x < this.addresses.length ; ++x ) {
				
				String address = this.addresses[x];
				
				// Update the AI, SS and MO status
				{
					String stylename = "project-gwt-checkbox-inspectoradvance-points-activated";
					
					String sForcedStatus = getStatusValue(address);
					
					if ( null != sForcedStatus ) {
						int forcedStatus = Integer.parseInt(sForcedStatus);
						
						int indexAI = 0;
						int indexSS = 1;
						int indexMO = 2;
						
						if ( null != chkDPMs[x][indexAI] ) {
							if ( RTDB_Helper.isAI(forcedStatus) ) {
								chkDPMs[x][indexAI].addStyleName(stylename);
								if ( !valueRefreshed ) {
									chkDPMs[x][indexAI].setValue(true);
								}
							} else {
								chkDPMs[x][indexAI].removeStyleName(stylename);
							}
						}
						if ( null != chkDPMs[x][indexSS] ) {
							if ( RTDB_Helper.isSS(forcedStatus) ) {
								chkDPMs[x][indexSS].addStyleName(stylename);
								if ( !valueRefreshed ) {
									chkDPMs[x][indexSS].setValue(true);
								}
							} else {
								chkDPMs[x][indexSS].removeStyleName(stylename);
							}
						}
						if ( null != chkDPMs[x][indexMO] ) {
							if ( RTDB_Helper.isMO(forcedStatus) ) {
								chkDPMs[x][indexMO].addStyleName(stylename);
								if ( !valueRefreshed ) {
									chkDPMs[x][indexMO].setValue(true);
								}
							} else {
								chkDPMs[x][indexMO].removeStyleName(stylename);
							}
						}
					} else {
						logger.log(Level.SEVERE, "updateValue sForcedStatus IS NULL!");
					}

				}

				// Update the value Once time
				{
					if ( !valueRefreshed ) {
					
						String value = null;
						{
							String dbaddress = address + strValue;
							logger.log(Level.FINE, "updateValue strValue["+strValue+"] dbaddress["+dbaddress+"]");
							if ( dbvalues.containsKey(dbaddress) ) {
								value = dbvalues.get(dbaddress);
							} else {
								logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
							}
						}
						logger.log(Level.SEVERE, "updateValue value["+value+"]");
						
						
						if ( null != value ) {
							String point = RTDB_Helper.getPoint(address);
							if ( null != point ) {
								
								String type = RTDB_Helper.getPointType(point);
								
								logger.log(Level.FINE, "updateValue point["+point+"]");
								
								if ( null != type ) {
									
									logger.log(Level.FINE, "updateValue type["+type+"]");
									
									if ( 0 != type.compareTo("dci") ) {
										txtValues[x].setValue(value);
									} else {
//										int index = Integer.parseInt(value);
//										lstValues[x].setSelectedIndex(index);
										
										String valueTable = null;
										{
											String dbaddress = address + strValueTable;
											logger.log(Level.FINE, "updateValue strValueTable["+strValueTable+"] dbaddress["+dbaddress+"]");
											if ( dbvalues.containsKey(dbaddress) ) {
												valueTable = dbvalues.get(dbaddress);
											} else {
												logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
											}
										}
										logger.log(Level.FINE, "updateValue valueTable["+valueTable+"]");
										
										if ( null != valueTable ) {
											String sValue = String.valueOf(value);
											String tValue = "";
											for( int r = 0 ; r < 12 ; ++r ) {
												tValue	= RTDB_Helper.getArrayValues(valueTable, 4, r );
												tValue	= RTDB_Helper.removeDBStringWrapper(tValue);
												
												if ( 0 == sValue.compareTo(tValue) ) {
													lstValues[x].setSelectedIndex(r);
													break;
												}
											}
										}
									}
								}
							}							
						} else {
							logger.log(Level.SEVERE, "updateValue value IS NULL!");
						}
					}
				}
			}
			
			if ( !valueRefreshed ) {
				
				valueRefreshed = true;
				
			}
		}

		logger.log(Level.SEVERE, "updateValue End");
	}
	
	private String getStatusValue(String address) {
		String sforcedStatus = null;
		{
			String dbaddress = address + strForcedStatus;
			logger.log(Level.FINE, "updateValue strForcedStatus["+strForcedStatus+"] dbaddress["+dbaddress+"]");
			
			if ( dbvalues.containsKey(dbaddress) ) {
				sforcedStatus = dbvalues.get(dbaddress);	
			} else {
				logger.log(Level.SEVERE, "updateValue dbaddress["+dbaddress+"] VALUE NOT EXISTS!");
			}
			
			logger.log(Level.FINE, "updateValue forcedStatus["+sforcedStatus+"]");
		}
		return sforcedStatus;
	}
	
	private void sendControl(ClickEvent event) {
		
		logger.log(Level.SEVERE, "setValue Begin");
		
		UIButtonToggle button = (UIButtonToggle) event.getSource();
		int index = -1;
		for ( int i = 0 ; i < btnApplys.length ; ++i ) {
			if ( btnApplys[i] == button ) {
				index = i;
				break;
			}
		}
		
		logger.log(Level.SEVERE, "setValue index["+index+"]");
		
		if ( -1 != index ) {
			
			String dbaddress = addresses[index];
			
			logger.log(Level.SEVERE, "setValue dbaddress["+dbaddress+"]");
			
			if ( null != dbaddress ) {
				
				String point	= RTDB_Helper.getPoint(dbaddress);
					
				String type		= RTDB_Helper.getPointType(point);
				
				String alias	= "<alias>" + dbaddress.replace(":", "");
				
				logger.log(Level.SEVERE, "setValue scsEnvId["+scsEnvId+"] alias["+alias+"]");				
				
				logger.log(Level.SEVERE, "setValue point["+point+"] type["+type+"]");
				
				if ( null != chkDPMs[index] ) {
					
					int indexAI = 0;
					int indexSS = 1;
					int indexMO = 2;
					
					boolean isAIApply	= false;
					boolean isAICancel	= false;
					
					boolean isSSApply	= false;
					boolean isSSCancel	= false;
					
					boolean isManualOverrideApply	= false;
					boolean isManualOverrideCancel	= false;
					
					String sForcedStatus = getStatusValue(dbaddress);
					
					if ( null != sForcedStatus ) {
						int forcedStatus = Integer.parseInt(sForcedStatus);
						
						if ( !RTDB_Helper.isAI(forcedStatus) && chkDPMs[index][indexAI].getValue() ) {	isAIApply = true;	}
						if ( RTDB_Helper.isAI(forcedStatus) && !chkDPMs[index][indexAI].getValue() ) {	isAICancel = true;	}
						
						if ( !RTDB_Helper.isSS(forcedStatus) && chkDPMs[index][indexSS].getValue() ) {	isSSApply = true;	}
						if ( RTDB_Helper.isSS(forcedStatus) && !chkDPMs[index][indexSS].getValue() ) {	isSSCancel = true;	}
						
						if ( !RTDB_Helper.isMO(forcedStatus) && chkDPMs[index][indexMO].getValue() ) {	isManualOverrideApply = true;	}
						if ( RTDB_Helper.isMO(forcedStatus) && !chkDPMs[index][indexMO].getValue() ) {	isManualOverrideCancel = true;	}						
					}
					
					int moIValue = 0;
					
					float moFValue	= 0.0f;
					
					String moSValue	= "";					
					
					if ( isAIApply || isAICancel || isSSApply || isSSCancel || isManualOverrideApply || isManualOverrideCancel ) {

						
						if ( 0 == type.compareTo("dci") ) {
							
							int moIndex = lstValues[index].getSelectedIndex();
							
							logger.log(Level.SEVERE, "updateValue moIndex["+moIndex+"]");
							
							String valueTable = null;
							{
								String address = dbaddress + strValueTable;
								logger.log(Level.SEVERE, "updateValue strValueTable["+strValueTable+"] dbaddress["+address+"]");
								if ( dbvalues.containsKey(address) ) {
									valueTable = dbvalues.get(address);
								} else {
									logger.log(Level.SEVERE, "updateValue dbaddress["+address+"] VALUE NOT EXISTS!");
								}
							}
							logger.log(Level.SEVERE, "updateValue valueTable["+valueTable+"]");

							String sValue	= RTDB_Helper.getArrayValues(valueTable, 4, moIndex);
							sValue			= RTDB_Helper.removeDBStringWrapper(sValue);
							
							logger.log(Level.SEVERE, "updateValue sValue["+sValue+"]");
							
							moIValue = Integer.parseInt(sValue);

						} else if ( 0 == type.compareTo("aci") || 0 == type.compareTo("sci")) {
							
							moSValue = txtValues[index].getText();
							
							if ( 0 == type.compareTo("aci") ) {
								
								moFValue = Float.parseFloat(moSValue);
							}
						}
						
						logger.log(Level.FINE, "setValue moIValue["+moIValue+"] setValue moFValue["+moFValue+"] moSValue["+moSValue+"]");
					}
					
					// Alarm Inhibit
					if ( isAIApply ) {
						
						// SCSDPC ALARM_INHIBIT_VAR
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "alarminhibit" + "_"+ "true" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.ALARM_INHIBIT_VAR);
						
						chkDPMs[index][indexAI].setValue(true);
					} 

					if (isAICancel ) {
						// SCSDPC NO_ALARM_INHIBIT_VAR
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "alarminhibit" + "_"+ "false" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.NO_ALARM_INHIBIT_VAR);

						chkDPMs[index][indexAI].setValue(false);
					}
					
					// Scan Suspend
					if ( isSSApply ) {
						// SS
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "scansuspend" + "_"+ "true" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.OPERATOR_INHIBIT);
						
						chkDPMs[index][indexSS].setValue(true);
						
					} 

					if ( isSSCancel ) {
						
						String key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "scansuspend" + "_"+ "false" + "_" + dbaddress;
						
						dpcAccess.sendChangeVarStatus(key, scsEnvId, alias, DCP_i.ValidityStatus.VALID);
						
						chkDPMs[index][indexSS].setValue(false);
					}
					
					// Manual Override
					{
						String key = null;
						boolean forceAction = false;
						if ( isManualOverrideApply ) {
							key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "manualoverride" + "_"+ "true" + "_" + dbaddress;
							forceAction = true;
						}
						if ( isManualOverrideCancel ){
							key = "changeEqpStatus" + "_"+ "inspectoradvance" + "_"+ "manualoverride" + "_"+ "false" + "_" + dbaddress;
							forceAction = false;
						}
						
						if ( null != key ) {
							if ( 0 == type.compareTo("dci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moIValue );
								
							} else if ( 0 == type.compareTo("aci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moFValue );
								
							} else if ( 0 == type.compareTo("sci") ) {
								
								dpcAccess.sendChangeVarForce ( key, scsEnvId, alias, forceAction, moSValue );
								
							}
							
							chkDPMs[index][indexMO].setValue(forceAction);
							
						} else {
							logger.log(Level.SEVERE, "setValue Manual Override Point Type IS INVALID");
						}
					
					}
				} else {
					logger.log(Level.SEVERE, "setValue chkDPMs["+dbaddress+"] IS NULL");
				}
			} else {
				logger.log(Level.SEVERE, "setValue dbaddress["+dbaddress+"] IS NULL");
			}
		} else {
			logger.log(Level.SEVERE, "setValue index["+index+"] IS INVALID");
		}
		logger.log(Level.SEVERE, "setValue End");
		
	}
	
	private MessageBoxEvent messageBoxEvent = null;
	@Override
	public void setMessageBoxEvent(MessageBoxEvent messageBoxEvent) {
		this.messageBoxEvent = messageBoxEvent;
	}
	
	private FlexTable flexTableAttibutes = null;
	private VerticalPanel vpCtrls = null;
	private UINameCard uiNameCard = null;
	@Override
	public ComplexPanel getMainPanel(UINameCard uiNameCard) {
		
		logger.log(Level.SEVERE, "getMainPanel Begin");
		
		this.uiNameCard = new UINameCard(uiNameCard);
		this.uiNameCard.appendUIPanel(this);

		vpCtrls = new VerticalPanel();
		vpCtrls.setWidth("100%");
		
		DockLayoutPanel basePanel = new DockLayoutPanel(Unit.PX);
		basePanel.addStyleName("project-gwt-panel-inspector");
		basePanel.setHeight("400px");
		basePanel.setWidth("400px");
		basePanel.add(vpCtrls);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(basePanel);
		
		logger.log(Level.SEVERE, "getMainPanel End");
		
		return vp;
	}

	@Override
	public void terminate() {
		logger.log(Level.SEVERE, "terminate Begin");

		logger.log(Level.SEVERE, "terminate End");
	}

}
